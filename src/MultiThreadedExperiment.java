//package smells;

import java.util.Arrays;

public class MultiThreadedExperiment extends Thread{

	
	Experiment experiment;
	
	ExperimentResults exp_results;
	
	
	int num_iterations;
	double step_size;
	
	public MultiThreadedExperiment(int num_fruit_bandit_worlds, int num_fruit_types, double[] worlds_probabilities,
			double[][] fruit_type_probabilities_matrix, double[][] poison_probabilities_matrix,
			double stay_sick_probability, double es_std, int creature_horizon, double creature_learning_rate, int num_iterations, double step_size) {
		
		this.experiment = new Experiment(num_fruit_bandit_worlds, num_fruit_types, worlds_probabilities,
				fruit_type_probabilities_matrix, poison_probabilities_matrix,
				stay_sick_probability, es_std, creature_horizon, creature_learning_rate);
		
		this.num_iterations = num_iterations;
		this.step_size = step_size;
		
		
	}
	
	public void run() {
			
		
		try {
			
			this.exp_results = this.experiment.run_experiment(num_iterations, step_size);
			
//			rewards_matrix[i] = exp_results.get_rewards();
//			used_probabilities_creature_tensor[i] = exp_results.get_used_probabilities_creature();
//			used_probabilities_evolver_tensor[i] = exp_results.get_used_probabilities_evolver();

			
			
		} catch (Exception e) {
			
			System.out.println("Experiment results failed");
			e.printStackTrace();
			/// Do noting
		}
		
		
		///System.out.println(Arrays.toString(this.exp_results.rewards));
		
		
		
		
		
	}
	
	
	
	public ExperimentResults get_exp_results() {
		return this.exp_results;
	}
	
	
	
	
	
}
