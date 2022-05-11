import java.util.Arrays;
import java.util.Random;


enum ExperimentEvolverAdvicePostprocess{
	LOGITS_TO_PROBS, 
	RAW,
}



enum CreatureEvaluationUltimate{
	AVERAGE, 
	FINAL,
}




public abstract class Experiment {

	Creature creature;
	private Random RandomGen = new Random();

	int creature_horizon;
	ES evolver;
	double es_std;
	double creature_learning_rate; /// This could instead be something like creature_learning_param

	int num_worlds;
	int exp_identifier;
	
	int creature_info_vector_size;
	int evolver_info_vector_size;
	int day_steps;
	
	WorldDistribution worlds_distribution;
	ExperimentEvolverAdvicePostprocess advice_process_type;
	CreatureEvaluationUltimate creature_evaluation_type;
	
	
	
	public Experiment(int num_worlds, int evolver_info_vector_size, int creature_info_vector_size, double[] worlds_probabilities, 
			double es_std, int creature_horizon, double creature_learning_rate, int day_steps, ExperimentEvolverAdvicePostprocess advice_process_type, 
			CreatureEvaluationUltimate creature_evaluation_type, int exp_identifier) {

		
		this.num_worlds = num_worlds;
		this.evolver_info_vector_size = evolver_info_vector_size;
		this.creature_info_vector_size = creature_info_vector_size;
		
		this.es_std = es_std;
		this.creature_learning_rate = creature_learning_rate;
		// double[] dimensions = {num_fruit_types, 2};
		this.evolver = new ES(evolver_info_vector_size, es_std);
		this.creature_horizon = creature_horizon;
		this.exp_identifier = exp_identifier;
		this.day_steps = day_steps;
		this.advice_process_type = advice_process_type;
		this.creature_evaluation_type = creature_evaluation_type;

	}

	private PairIntegerDouble evaluate_creature(double[] advice) throws Exception{
		/// Send advice to creature
		this.creature.reset();
		this.creature.process_evolver_advice(advice.clone());

		WorldInfo world_info = this.worlds_distribution.get_world();
		World sample_world = world_info.get_world();
		int sample_world_index  = world_info.get_world_index();
		
		
		
		//System.out.println("Prob weights start of creature life");
		//System.out.println(Arrays.toString(creature.get_probability_weights()));
		
		/// Run creature with this advice for a creature horizon time
		for (int j = 0; j < this.creature_horizon; j++) {
			if(j%this.day_steps == 0) {
				sample_world.reset_world();
			}

			
			
			double[] state = sample_world.get_state();
			// System.out.println("Fruit type poison info");
			// System.out.println(Arrays.toString(fruit_type_poison_info));
			
			PairIntegerDouble action_reward = this.creature.step(state);
			sample_world.step(action_reward.get_my_integer());
			double[] next_state = sample_world.get_state();
			this.creature.update(state, action_reward, next_state);
			
		}

		double ultimate_reward = 0;
		
		if(this.creature_evaluation_type == CreatureEvaluationUltimate.AVERAGE | this.creature_evaluation_type == CreatureEvaluationUltimate.FINAL) {

			if(this.creature_evaluation_type == CreatureEvaluationUltimate.AVERAGE) {
				ultimate_reward = this.creature.get_ultimate_reward_collected() / this.creature_horizon;
			}
			
			
			if(this.creature_evaluation_type == CreatureEvaluationUltimate.FINAL) {
				ultimate_reward = this.creature.get_last_reward_collected();
			}
			
			
		
		}		
		else {
			
			throw new Exception("Creature Evaluation Ultimate not recognized in Experiment.evaluate_creature()");
			
			
			
		}
		return new PairIntegerDouble(sample_world_index, ultimate_reward);
	}

		
	public ExperimentResults run_experiment(int num_iterations, double step_size) throws Exception {

		double[] initial_evolver_vector = new double[this.evolver_info_vector_size];
		//// Initialize weights_vector

		double[] ultimate_rewards = new double[num_iterations];
		int[] world_indices = new int[num_iterations];

		
		
		
		double[][] creature_info_list = new double[num_iterations][creature_info_vector_size];
		double[][] evolver_info_list = new double[num_iterations][evolver_info_vector_size];

		for (int i = 0; i < this.evolver_info_vector_size; i++) {
			initial_evolver_vector[i] = RandomGen.nextGaussian();
		}

		this.evolver.set_weights_vector(initial_evolver_vector);

		
		
		for (int i = 0; i < num_iterations; i++) {

			//// Send the creature the logits

			PairIntegerDouble world_index_and_reward = this.evaluate_creature(this.evolver.get_weights_vector());
			
			double ultimate_reward_base = world_index_and_reward.get_my_double();	
			int world_index = world_index_and_reward.get_my_integer();
			
			
			
			creature_info_list[i] = this.creature.get_creature_info();
			//System.out.println("Experiment. Creature information");
			//System.out.println(Arrays.toString(creature_info_list[i]));
			
		
			if(this.advice_process_type == ExperimentEvolverAdvicePostprocess.LOGITS_TO_PROBS | this.advice_process_type == ExperimentEvolverAdvicePostprocess.RAW) {
				
			/////// 
				if(this.advice_process_type == ExperimentEvolverAdvicePostprocess.LOGITS_TO_PROBS) {
					evolver_info_list[i] = ProbabilityUtils.logits_to_probabilities(this.evolver.get_weights_vector().clone()).clone();
				}				
					
				if(this.advice_process_type == ExperimentEvolverAdvicePostprocess.RAW) {
					evolver_info_list[i] = this.evolver.get_weights_vector().clone();

				}

				
			}
			else {
				
				throw new Exception("Advice process type not recognized in Experiment.run_experiment()");
				
				
				
			}
			
			
			
			//System.out.println("Experiment. Advice process type");
			//System.out.println(this.advice_process_type);
			
			
			//System.out.println("Experiment. Evolver information.");
			//System.out.println(Arrays.toString(evolver_info_list[i]));


			
			
			
			
			double[] perturbation = this.evolver.get_sample_perturbation();
			double[] perturbed_logits_vector = this.evolver.get_perturbed_datapoint(this.evolver.get_weights_vector(),
					perturbation);

			
			PairIntegerDouble world_index_and_reward_perturbed =  evaluate_creature(perturbed_logits_vector);
			double ultimate_reward_perturbed = world_index_and_reward_perturbed.get_my_double();
			
			
			this.evolver.update_statistics_double_sensing(perturbation, ultimate_reward_perturbed, ultimate_reward_base, step_size);
			
			//System.out.println("Experiment. perturbation.");
			//System.out.println(Arrays.toString(perturbation));
			//System.out.println("Experiment. ultimate reward perturbed.");

			//System.out.println(ultimate_reward_perturbed);
			//System.out.println("Experiment. reward base.");

			//System.out.println(ultimate_reward_base);
			
			//System.out.println("Experiment. step size.");

			//System.out.println(step_size);
			
			
			//System.out.println("Experiment. weights vector.");

			//System.out.println(Arrays.toString(this.evolver.get_weights_vector()));
			
			
			if(Flags.verbose && i % Flags.logging_frequency == 0) {
				System.out.println("Experiment " + String.valueOf(this.exp_identifier) + " iteration " + String.valueOf(i));
				
				
				
				//System.out.println("Creature probabilities");
				//System.out.println(Arrays.toString(this.creature.get_probability_weights()));
				//System.out.println("Sample Reward");
				//System.out.println(reward_base);
//				System.out.println("Weights Vector");
//				System.out.println(Arrays.toString(this.evolver.get_weights_vector()));
			}
			ultimate_rewards[i] = ultimate_reward_base;
			world_indices[i] = world_index;
			//used_probabilities_evolver[i] = this.creature.logits_to_probabilities(this.evolver.get_weights_vector());

		}

		ExperimentResults exp_results = new ExperimentResults(ultimate_rewards, creature_info_list,
				evolver_info_list, world_indices);

		return exp_results;
	}

	
	
	
}
