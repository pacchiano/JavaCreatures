import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class ExperimentManagerChainMDP extends ExperimentManager {

	
	
	//int num_experiments ;
	
	
	int chain_length;
	double move_probability;	
	double discount;
	
	int day_steps;

	
	
	
	//int num_iterations ;
	//int creature_horizon ;
	//double es_step_size ;

	//	int num_worlds;
	//	double[] worlds_probabilities;
	
	//String experiment_name;
	
//	ExperimentEvolverAdvicePostprocess advice_process_type;

	public void set_experiment_params(String experiment_name) {
		this.experiment_name = experiment_name;
    	this.num_experiments = 30;
    	this.chain_length = 3;
    	this.move_probability = .1;
    	this.discount = .9;
    	
      	
    	this.worlds_probabilities = new double[1];
    	this.worlds_probabilities[0]=1;
    	
    	this.es_std = .1;
    	this.num_iterations = 100000;
    	this.es_step_size = .1;
    	this.creature_learning_rate = 0.01;

    	this.advice_process_type = ExperimentEvolverAdvicePostprocess.RAW;
    	
    	
    	if(experiment_name ==  "ChainMDPBasic1") {
    		this.num_worlds = 1;
        	this.creature_horizon = 10000;
        	this.day_steps = 100;
        		
        	
        	this.es_step_size = .1;
        	this.creature_learning_rate = 0.1;
    		   		
    	}
 	
	}
	
	
	
	
	
	public ExperimentChainMDP get_experiment(String experiment_name, int exp_identifier) throws Exception {
		this.set_experiment_params(experiment_name);		
	
		
		

		ExperimentChainMDP experiment = new ExperimentChainMDP( this.num_worlds,  this.chain_length, this.move_probability, 
				this.discount, this.creature_learning_rate,
				this.worlds_probabilities, this.es_std, this.creature_horizon,  this.day_steps,
				this.advice_process_type, 
				exp_identifier);
		
		
		return experiment;
	
		
		
		
	}
	
	

	public MultiThreadedExperimentChainMDP get_multithreaded_experiment(String experiment_name, int thread_index) throws Exception {
		this.set_experiment_params(experiment_name);		
		


		MultiThreadedExperimentChainMDP experiment = new MultiThreadedExperimentChainMDP( this.num_worlds,  this.chain_length,  this.move_probability,  
				this.discount, this.creature_learning_rate,
				this.worlds_probabilities,  this.es_std,  this.creature_horizon,   this.day_steps,  this.num_iterations,  this.es_step_size,
				this.advice_process_type,  thread_index);
		
		
		return experiment;
	
		
		
		
	}


	
	
	
	public String get_experiment_filename_stub() {
		
		String file_name = this.experiment_name + "_MDPChain_" + String.valueOf(this.chain_length) + "_H"
				+ String.valueOf(this.creature_horizon) + "_T" + String.valueOf(this.num_iterations);

		return file_name;
		
	}
	


	
	
	public void write_log_file(String folder, String file_name_stub, ExperimentManagerResults sweep_results) {
		
		
		
		
		
		String txt_filename = folder + file_name_stub + ".txt";
		
		try {

			FileWriter myWriter = new FileWriter(txt_filename);

			myWriter.write(String.valueOf(this.num_experiments));
			myWriter.write("\n");

			myWriter.write(String.valueOf(this.num_iterations));
			myWriter.write("\n");

			myWriter.write(String.valueOf(this.num_worlds));
			myWriter.write("\n");
			
			myWriter.write(String.valueOf(this.creature_horizon));
			myWriter.write("\n");

				
			
			
			
			
			myWriter.write(String.valueOf(this.discount));
			myWriter.write("\n");

			myWriter.write(String.valueOf(this.day_steps));
			myWriter.write("\n");
			
			

			myWriter.write(String.valueOf(this.es_std));
			myWriter.write("\n");

			myWriter.write(String.valueOf(this.es_step_size));
			myWriter.write("\n");

			myWriter.write(String.valueOf(this.creature_learning_rate));
			myWriter.write("\n");			
			
			myWriter.write(Arrays.deepToString(sweep_results.rewards_matrix));
			myWriter.write("\n");

			myWriter.write(Arrays.deepToString(sweep_results.creature_info_tensor));
			myWriter.write("\n");

			myWriter.write(Arrays.deepToString(sweep_results.evolver_info_tensor));
			myWriter.write("\n");

			myWriter.write(Arrays.deepToString(sweep_results.world_indices_matrix));
			myWriter.write("\n");
			
			
			
			
			
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
	
			String zip_filename = folder + file_name_stub + ".zip";
			ZipFile.zip_file(txt_filename, zip_filename);
			
			
			//// Deleting the un-zipped file
			File txtFile = new File(txt_filename);
			txtFile.delete();
			
		
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
		
		
	}

	

	
	
	
	
	
	
	public static void main(String[] args) {
		
		int experiment_index = -1;
		if (args.length > 0) {
		    try {
		        experiment_index = Integer.parseInt(args[0]);
		        System.out.println(String.valueOf(experiment_index));
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + args[0] + " must be an integer.");
		        System.exit(1);
		    }
		}
		

		//String[] experiment_names = {  "MultiworldScenarioReactive1", "MultiworldScenarioAdaptive1"	, "Scenario1", 
		//		"Scenario2", "Scenario3", "Scenario4", "Scenario5" };

		String[] experiment_names = {  "ChainMDPBasic1" };
		
//		String[] experiment_names = { "Scatter-ESstepSizep1-Reactive", 	"Scatter-ESstepSizep01-Reactive", "Scatter-ESstepSizep001-Reactive", "Scatter-ESstepSizep1-AdaptiveH100", 
//				"Scatter-ESstepSizep01-AdaptiveH100",  "Scatter-ESstepSizep001-AdaoptiveH100",  "Scatter-ESstepSizep1-AdaptiveH1000", 
//				"Scatter-ESstepSizep01-AdaptiveH1000",  "Scatter-ESstepSizep001-AdaoptiveH1000"};
	
		
		for (int j = 0; j < experiment_names.length; j++) {

			ExperimentManagerChainMDP exp_manager = new ExperimentManagerChainMDP();
			String experiment_name = experiment_names[j];
			
			exp_manager.set_experiment_params(experiment_name);

			
			

			
			String folder_name = "./results/";
			String file_name_stub = exp_manager.get_experiment_filename_stub();
			//String file_location = "./results/" + file_name + ".txt";
			
			
			
			//ExperimentManagerResults sweep_results = exp_manager.get_experiment_sweep_results();

			
			ExperimentManagerResults sweep_results = exp_manager.get_experiment_sweep_results_multithreaded();

			

			
			exp_manager.write_log_file(folder_name, file_name_stub, sweep_results);
			


		}

	}

	
	
	
	
	
}
