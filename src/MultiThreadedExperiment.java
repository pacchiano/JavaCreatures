
public abstract class MultiThreadedExperiment extends Thread {
	Experiment experiment;
	
	ExperimentResults exp_results;
	int num_iterations;
	double es_step_size;
	int thread_index;

	
	public abstract void run();
	
	public ExperimentResults get_exp_results() {
		return this.exp_results;
	}
	
	

	
}
