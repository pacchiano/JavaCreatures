

//package smells;


public class MultiThreadedExperimentFruitBandits extends MultiThreadedExperiment{

	
	public MultiThreadedExperimentFruitBandits(int num_fruit_bandit_worlds, int num_fruit_types, double[] worlds_probabilities,
			double[][] fruit_type_probabilities_matrix, double[][] poison_probabilities_matrix,
			double stay_sick_probability, double es_std, int creature_horizon, double creature_learning_rate, 
			int num_iterations, double es_step_size, ExperimentEvolverAdvicePostprocess advice_process_type,
			ExperimentFruitBanditsType experiment_type, int thread_index) {
		
		this.experiment = new ExperimentFruitBandits(num_fruit_bandit_worlds, num_fruit_types, worlds_probabilities,
				fruit_type_probabilities_matrix, poison_probabilities_matrix,
				stay_sick_probability, es_std, creature_horizon, creature_learning_rate, advice_process_type,
				experiment_type,  thread_index);
		
		this.num_iterations = num_iterations;
		this.es_step_size = es_step_size;
		this.thread_index = thread_index;
		
	}
	
	public void run() {
			
		
		try {
			
			this.exp_results = this.experiment.run_experiment(this.num_iterations, this.es_step_size);
			
//			rewards_matrix[i] = exp_results.get_rewards();
//			used_probabilities_creature_tensor[i] = exp_results.get_used_probabilities_creature();
//			used_probabilities_evolver_tensor[i] = exp_results.get_used_probabilities_evolver();

			
			
//			System.out.println("Exp Results!!!");
//			System.out.println(Arrays.toString(this.exp_results.ultimate_rewards));
			
			
			
		} catch (Exception e) {
			
			System.out.println("Experiment results failed");
			e.printStackTrace();
			/// Do noting
		}
		
		
		///System.out.println(Arrays.toString(this.exp_results.rewards));
		
		
		
		
		
	}
	
	
	
	
	
	
}
