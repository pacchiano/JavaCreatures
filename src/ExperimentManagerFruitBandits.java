//package smells;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;





public class ExperimentManagerFruitBandits extends ExperimentManager{

	
	int num_fruit_types ;
	double[][] fruit_type_probabilities_matrix ;
	double[][] poison_probabilities_matrix ;
	double stay_sick_probability ;
	
	//String experiment_name;
	ExperimentFruitBanditsType experiment_type;
	
	
	
	
	

	public void set_experiment_params(String experiment_name) {
		this.experiment_name = experiment_name;
    	this.num_experiments = 30;
    	this.num_fruit_types = 3;
    	
    	
    	this.creature_info_vector_size = this.num_fruit_types;
    	this.evolver_info_vector_size = this.num_fruit_types;
    	
    	
    	
    	this.num_worlds = 1;
    	this.worlds_probabilities = new double[1];
    	this.worlds_probabilities[0]=1;
    	
    	this.fruit_type_probabilities_matrix = new double[this.num_worlds][this.num_fruit_types];
    	this.fruit_type_probabilities_matrix[0][0] = .33;
    	this.fruit_type_probabilities_matrix[0][1] = .33;
    	this.fruit_type_probabilities_matrix[0][2] = .34;
    	
    	this.poison_probabilities_matrix = new double[this.num_worlds][this.num_fruit_types];
    	this.poison_probabilities_matrix[0][0] = .9;
    	this.poison_probabilities_matrix[0][1] = .2;
    	this.poison_probabilities_matrix[0][2] = .3;
    	
    	
    	this.stay_sick_probability = 0;
    	this.es_std = .1;
    	this.es_step_size = .1;
    	this.creature_learning_rate = 0;

    	this.advice_process_type = ExperimentEvolverAdvicePostprocess.LOGITS_TO_PROBS;
    	this.experiment_type = ExperimentFruitBanditsType.PRIOR_POLICY;
    	this.creature_evaluation_type =  CreatureEvaluationUltimate.FINAL;
    	//this.experiment_type = ExperimentFruitBanditsType.PRIOR_POLICY;
    	
    	
    	
    	if(experiment_name ==  "ProximateBasic1") {
        	this.num_iterations = 1000;//10000;
        	this.creature_horizon = 1000;

    		this.advice_process_type = ExperimentEvolverAdvicePostprocess.RAW;
        	this.experiment_type = ExperimentFruitBanditsType.PROXIMATE_REWARD;

        	this.es_step_size = .1;
        	this.creature_learning_rate = 0.1;
    		   		
    	}
 	
    	if(experiment_name ==  "ProximateBasic2") {
        	this.num_iterations = 10001;
        	this.creature_horizon = 100;
        	
    		
    		this.advice_process_type = ExperimentEvolverAdvicePostprocess.RAW;
        	this.experiment_type = ExperimentFruitBanditsType.PROXIMATE_REWARD;

        	this.creature_horizon = 100;
        	this.es_step_size = .1;
        	this.creature_learning_rate = 0.1;
    		   		
    	}

    	
    	if(experiment_name ==  "Baldwin-ES-p1-Reactive") {
    		
        	this.num_iterations = 1000000;
        	this.creature_horizon = 10;
        	
        	this.es_step_size = .1;
        	this.creature_learning_rate = 0;
    		 
        	
    	}
    	

    	if(experiment_name ==  "Baldwin-ES-p1-CLRp1-H100") {
  
        	this.num_iterations = 100000;
        	this.creature_horizon = 100;
        	
        	this.es_step_size = .1;
        	this.creature_learning_rate = 0.1;
    		   		
        	
    	}


    	if(experiment_name ==  "Baldwin-ES-p1-CLRp01-H100") {
    		
        	this.num_iterations = 100000;
        	this.creature_horizon = 100;

        	this.es_step_size = .1;
        	this.creature_learning_rate = 0.01;
        	
    	}


    	if(experiment_name ==  "Baldwin-ES-p1-CLRp001-H100") {
    		
        	this.num_iterations = 100000;
        	this.creature_horizon = 100;

        	this.es_step_size = .1;
        	this.creature_learning_rate = 0.001;
        	
    	}

    	
    	
    	if(experiment_name ==  "Baldwin-ES-p1-CLRp1-H1000") {
    		
        	this.num_iterations = 10000;
        	this.creature_horizon = 1000;

        	this.es_step_size = .1;
        	this.creature_learning_rate = 0.1;
    		   		
        	
    	}


    	if(experiment_name ==  "Baldwin-ES-p1-CLRp01-H1000") {
    		
        	this.num_iterations = 10000;
        	this.creature_horizon = 1000;

        	this.es_step_size = .1;
        	this.creature_learning_rate = 0.01;
        	
    	}


    	if(experiment_name ==  "Baldwin-ES-p1-CLRp001-H1000") {
    		
        	this.num_iterations = 10000;
        	this.creature_horizon = 1000;

        	this.es_step_size = .1;
        	this.creature_learning_rate = 0.001;
        	
    	}
    	
    	
    	
        	
    	

		/// Poison prob = 1 for one fruit.
		else if (experiment_name == "MultiworldScenarioReactive1")
		{	    	
        	this.num_iterations = 1000;
        	this.creature_horizon = 100;

			
	
	    	this.num_worlds = 2;
	    	this.worlds_probabilities = new double[2];
	    	this.worlds_probabilities[0] = .5;
	    	this.worlds_probabilities[1] = .5;
	    	
	    	
	    	this.fruit_type_probabilities_matrix = new double[this.num_worlds][this.num_fruit_types];
	    	this.fruit_type_probabilities_matrix[0][0] = .33;
	    	this.fruit_type_probabilities_matrix[0][1] = .33;
	    	this.fruit_type_probabilities_matrix[0][2] = .34;
	    	this.fruit_type_probabilities_matrix[1][0] = .33;
	    	this.fruit_type_probabilities_matrix[1][1] = .33;
	    	this.fruit_type_probabilities_matrix[1][2] = .34;
	    	
	    	
	    	
	    	
	    	this.poison_probabilities_matrix = new double[this.num_worlds][this.num_fruit_types];
	    	this.poison_probabilities_matrix[0][0] = .9;
	    	this.poison_probabilities_matrix[0][1] = .1;
	    	this.poison_probabilities_matrix[0][2] = .9;
	    	this.poison_probabilities_matrix[1][0] = .1;
	    	this.poison_probabilities_matrix[1][1] = .1;
	    	this.poison_probabilities_matrix[1][2] = .9;
	    	
	    	this.stay_sick_probability = 0;
	    	this.creature_learning_rate = 0;

	    	
	    	
		}		

		else if (experiment_name == "MultiworldScenarioAdaptive1")
		{	    	
			
			
        	this.num_iterations = 1000;
        	this.creature_horizon = 100;

	    	this.num_worlds = 2;
	    	this.worlds_probabilities = new double[2];
	    	this.worlds_probabilities[0] = .5;
	    	this.worlds_probabilities[1] = .5;
	    	
	    	
	    	this.fruit_type_probabilities_matrix = new double[this.num_worlds][this.num_fruit_types];
	    	this.fruit_type_probabilities_matrix[0][0] = .33;
	    	this.fruit_type_probabilities_matrix[0][1] = .33;
	    	this.fruit_type_probabilities_matrix[0][2] = .34;
	    	this.fruit_type_probabilities_matrix[1][0] = .33;
	    	this.fruit_type_probabilities_matrix[1][1] = .33;
	    	this.fruit_type_probabilities_matrix[1][2] = .34;
	    	
	    	
	    	
	    	
	    	this.poison_probabilities_matrix = new double[this.num_worlds][this.num_fruit_types];
	    	this.poison_probabilities_matrix[0][0] = .9;
	    	this.poison_probabilities_matrix[0][1] = .1;
	    	this.poison_probabilities_matrix[0][2] = .9;
	    	this.poison_probabilities_matrix[1][0] = .1;
	    	this.poison_probabilities_matrix[1][1] = .1;
	    	this.poison_probabilities_matrix[1][2] = .9;

	    	
	    	this.stay_sick_probability = 0;
	    	this.creature_learning_rate = 0.1;

	    	
	    	
		}		

		
    	
    	
    	
		
	}
	
	
	
	
	
	
	public ExperimentFruitBandits get_experiment(String experiment_name, int exp_identifier) throws Exception {
		this.set_experiment_params(experiment_name);		
		

		
		ExperimentFruitBandits experiment = new ExperimentFruitBandits(this.num_worlds, this.num_fruit_types, 
				this.worlds_probabilities, this.fruit_type_probabilities_matrix, 
    			this.poison_probabilities_matrix,  this.stay_sick_probability, 
    			this.es_std, this.creature_horizon, this.creature_learning_rate, 
    			this.advice_process_type,  this.experiment_type, this.creature_evaluation_type, exp_identifier);
		
		
		

		
		return experiment;
	
		
	}
	
	
	
	public MultiThreadedExperimentFruitBandits get_multithreaded_experiment(String experiment_name, int thread_index) throws Exception {
		this.set_experiment_params(experiment_name);		
		

		MultiThreadedExperimentFruitBandits experiment = new MultiThreadedExperimentFruitBandits(this.num_worlds, this.num_fruit_types, this.worlds_probabilities, this.fruit_type_probabilities_matrix, 
    			this.poison_probabilities_matrix,  this.stay_sick_probability, this.es_std, this.creature_horizon, 
    			this.creature_learning_rate,this.num_iterations, this.es_step_size, this.advice_process_type, 
    			this.experiment_type, this.creature_evaluation_type, thread_index);
		
		

		
		return experiment;
	
		
		
		
	}

	
	public String get_experiment_filename_stub() {
		
		String file_name = this.experiment_name + "_fruitBandits_" + String.valueOf(this.num_fruit_types) + "_H"
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

			myWriter.write(String.valueOf(this.num_fruit_types));
			myWriter.write("\n");

			myWriter.write(Arrays.deepToString(this.fruit_type_probabilities_matrix));
			myWriter.write("\n");

			myWriter.write(Arrays.deepToString(this.poison_probabilities_matrix));
			myWriter.write("\n");

			myWriter.write(String.valueOf(this.stay_sick_probability));
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
	
			String tmp_zip_filename = folder + file_name_stub + "_tmp"+ ".zip";
			ZipFile.zip_file(txt_filename, tmp_zip_filename);
			
			
			//// Deleting the un-zipped file
			File txtFile = new File(txt_filename);
			txtFile.delete();
			
			
			//// Double zip
			String zip_filename = folder + file_name_stub + ".zip";
			ZipFile.zip_file(tmp_zip_filename, zip_filename);

			
			//			
//
//			//// Deleting the tmp zip file
//			
//			File tmpZipFile = new File(tmp_zip_filename);
//			tmpZipFile.delete();
			

			
		
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
		

//		String[] experiment_names = {  "MultiworldScenarioReactive1", "MultiworldScenarioAdaptive1"	, "Scenario1", 
//				"Scenario2", "Scenario3", "Scenario4", "Scenario5" };

		
		String[] experiment_names = {  "MultiworldScenarioReactive1", "MultiworldScenarioAdaptive1"	 };

		
//		String[] experiment_names = {  "Baldwin-ES-p1-Reactive"		, "Baldwin-ES-p1-CLRp1-H100",  "Baldwin-ES-p1-CLRp01-H100",
//				"Baldwin-ES-p1-CLRp001-H100", "Baldwin-ES-p1-CLRp1-H1000", "Baldwin-ES-p1-CLRp01-H1000", "Baldwin-ES-p1-CLRp001-H1000"};

		
		
		
//		String[] experiment_names = {  "Baldwin-ES-p1-Reactive" };
		
//		String[] experiment_names = { "Scatter-ESstepSizep1-Reactive", 	"Scatter-ESstepSizep01-Reactive", "Scatter-ESstepSizep001-Reactive", "Scatter-ESstepSizep1-AdaptiveH100", 
//				"Scatter-ESstepSizep01-AdaptiveH100",  "Scatter-ESstepSizep001-AdaoptiveH100",  "Scatter-ESstepSizep1-AdaptiveH1000", 
//				"Scatter-ESstepSizep01-AdaptiveH1000",  "Scatter-ESstepSizep001-AdaoptiveH1000"};
	
		
		for (int j = 0; j < experiment_names.length; j++) {

			ExperimentManagerFruitBandits exp_manager = new ExperimentManagerFruitBandits();
			String experiment_name = experiment_names[j];
			
			exp_manager.set_experiment_params(experiment_name);

			
			

			
			String folder_name = "/Users/apacchiano/Documents/Research/JavaProjects/CreaturesNewcopy/results/";
			String file_name_stub = exp_manager.get_experiment_filename_stub();
			//String file_location = "./results/" + file_name + ".txt";
			
			System.out.println("filename");
			
			System.out.println(file_name_stub);
			

			
			
			
			//ExperimentManagerResults sweep_results = exp_manager.get_experiment_sweep_results();

			
			ExperimentManagerResults sweep_results = exp_manager.get_experiment_sweep_results_multithreaded();

			
			
			
			
			
			exp_manager.write_log_file(folder_name, file_name_stub, sweep_results);
			


		}

	}

	
	
	
}
