
public class MultiThreadedExperimentChainMDP extends MultiThreadedExperiment{

	
	
	
	
	public MultiThreadedExperimentChainMDP(int num_chain_worlds, int chain_length, double move_probability, double discount, double creature_learning_rate,
			double[] worlds_probabilities, double es_std, int creature_horizon,  int day_steps, int num_iterations, double es_step_size,
			ExperimentEvolverAdvicePostprocess advice_process_type, int thread_index) {
		
		this.experiment = new ExperimentChainMDP( num_chain_worlds,  chain_length, move_probability, discount,  creature_learning_rate,
				 worlds_probabilities, es_std, creature_horizon, day_steps,
				 advice_process_type, 
				 thread_index
				);
		
		this.num_iterations = num_iterations;
		this.es_step_size = es_step_size;
		this.thread_index = thread_index;
		
	}
	
	public void run() {
			
		
		try {
			
			this.exp_results = this.experiment.run_experiment(this.num_iterations, this.es_step_size);
			
			
			
		} catch (Exception e) {
			
			System.out.println("Experiment results failed");
			e.printStackTrace();
			/// Do noting
		}
		
		
		
		
		
		
	}
	
	
	
	public ExperimentResults get_exp_results() {
		return this.exp_results;
	}
	
	
	

	
	
}
