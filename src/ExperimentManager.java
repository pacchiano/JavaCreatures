import java.util.Arrays;

public abstract class ExperimentManager {

	
	int num_experiments;
	int num_iterations;
	//int num_fruit_types;
	
	int creature_info_vector_size;
	int evolver_info_vector_size;
	
	double es_step_size;
	
	String experiment_name;
	double creature_learning_rate;

	double es_std ;
	int creature_horizon ;

	int num_worlds;
	double[] worlds_probabilities;

	ExperimentEvolverAdvicePostprocess advice_process_type;

	
	
	public abstract Experiment get_experiment(String experiment_name, int exp_identifier) throws Exception;
	
	public abstract MultiThreadedExperiment get_multithreaded_experiment(String experiment_name, int thread_index) throws Exception;
	
	public abstract String get_experiment_filename_stub();
	
	public abstract void write_log_file(String folder, String file_name_stub, ExperimentManagerResults sweep_results);
	
	
	public ExperimentManagerResults get_experiment_sweep_results_multithreaded() {
		

		
		double[][] rewards_matrix = new double[num_experiments][num_iterations];
		int[][] world_indices_matrix = new int[num_experiments][num_iterations];
		double[][][] creature_info_tensor = new double[num_experiments][num_iterations][creature_info_vector_size];
		double[][][] evolver_info_tensor = new double[num_experiments][num_iterations][evolver_info_vector_size];
		
		
		MultiThreadedExperiment[] experiments = new MultiThreadedExperiment[num_experiments];

		
		
		for (int i = 0; i < num_experiments; i++) {

			try {
				experiments[i] = this.get_multithreaded_experiment(experiment_name, i+1);
				experiments[i].start();
				
				
				
			} catch (Exception e) {
				
				System.out.println("Experiment sweep results failed");
				e.printStackTrace();
				/// Do noting
			}

		
		
		}
		for (int i = 0; i < num_experiments; i++) {

			try {
				experiments[i].join();
//				System.out.println("NUm exps");
//				System.out.println(num_experiments);
//				System.out.println(i);
				
			} catch (Exception e) {
				
				System.out.println("Experiment sweep results failed");
				e.printStackTrace();
				/// Do noting
			}

		
		
		}
	
		
		
		
		System.out.println("Finished experiments multithreaded");
		
		
		
		
		for (int i = 0; i < num_experiments; i++) {
			ExperimentResults exp_results = experiments[i].get_exp_results();
			//System.out.println(Arrays.toString(exp_results.ultimate_rewards));

			
	
				rewards_matrix[i] = exp_results.get_rewards();
				world_indices_matrix[i] = exp_results.get_world_indices();
				creature_info_tensor[i] = exp_results.get_creature_info_list();
				evolver_info_tensor[i] = exp_results.get_evolver_info_list();
				
	
		
		
		}
	

		
		
		ExperimentManagerResults sweep_results = new ExperimentManagerResults(rewards_matrix, creature_info_tensor, 
				evolver_info_tensor, world_indices_matrix);
				
		return sweep_results;

		
		
		
		
	}
	
	

	
	
	public ExperimentManagerResults get_experiment_sweep_results() {
		
//		System.out.println("Num Experiments");
//		System.out.println(this.num_experiments);
		
		
		
		double[][] rewards_matrix = new double[num_experiments][num_iterations];
		int[][] world_indices_matrix = new int[num_experiments][num_iterations];
		double[][][] creature_info_tensor = new double[num_experiments][num_iterations][creature_info_vector_size];
		double[][][] evolver_info_tensor = new double[num_experiments][num_iterations][evolver_info_vector_size];
		
		for (int i = 0; i < num_experiments; i++) {

		
			
			try {
				Experiment experiment = this.get_experiment(experiment_name, i+1);
				

				ExperimentResults exp_results = experiment.run_experiment(num_iterations, es_step_size);
				
				System.out.println(Arrays.toString(exp_results.ultimate_rewards));

				rewards_matrix[i] = exp_results.get_rewards();
				world_indices_matrix[i] = exp_results.get_world_indices();
				
				creature_info_tensor[i] = exp_results.get_creature_info_list();
				evolver_info_tensor[i] = exp_results.get_evolver_info_list();
				
				
				
				

			} catch (Exception e) {
				
				System.out.println("Experiment sweep results failed");
				e.printStackTrace();
				/// Do noting
			}
		}
	
		
		ExperimentManagerResults sweep_results = new ExperimentManagerResults(rewards_matrix, creature_info_tensor, evolver_info_tensor, world_indices_matrix);
		
		
		
		
		return sweep_results;
		
	}
	

	
	
	
	
	
	
	
	
	
}
