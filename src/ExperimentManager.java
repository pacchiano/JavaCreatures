//package smells;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;



class ZipFile {
	private ZipFile() {}

	 
	 
    public static void zip_file(String source_path, String compressed_path) throws IOException {
        FileOutputStream fos = new FileOutputStream(compressed_path);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(source_path);
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        zipOut.close();
        fis.close();
        fos.close();
    }
}



class ExperimentManagerResults{
	
	double[][] rewards_matrix ;
	double[][][] used_probabilities_creature_tensor;
	double[][][] used_probabilities_evolver_tensor ;

	
	public ExperimentManagerResults(double[][] rewards_matrix,  double[][][] used_probabilities_creature_tensor, double[][][] used_probabilities_evolver_tensor) {
		
		this.rewards_matrix  = rewards_matrix;
		this.used_probabilities_creature_tensor = used_probabilities_creature_tensor;
		this.used_probabilities_evolver_tensor = used_probabilities_evolver_tensor;
		
		
	}
	
	

	
	
	
}




public class ExperimentManager {
	int num_experiments ;
	int num_fruit_types ;
	double[][] fruit_type_probabilities_matrix ;
	double[][] poison_probabilities_matrix ;
	double stay_sick_probability ;
	double es_std ;
	int num_iterations ;
	int creature_horizon ;
	double step_size ;
	double creature_learning_rate ;

	int num_fruit_bandit_worlds;
	double[] worlds_probabilities;
	
	String experiment_name;
	

	public void set_experiment_params(String experiment_name) {
		this.experiment_name = experiment_name;
    	this.num_experiments = 30;
    	this.num_fruit_types = 3;
    	this.num_fruit_bandit_worlds = 1;
    	this.worlds_probabilities = new double[1];
    	this.worlds_probabilities[0]=1;
    	
    	this.fruit_type_probabilities_matrix = new double[this.num_fruit_bandit_worlds][this.num_fruit_types];
    	this.fruit_type_probabilities_matrix[0][0] = .33;
    	this.fruit_type_probabilities_matrix[0][1] = .33;
    	this.fruit_type_probabilities_matrix[0][2] = .34;
    	
    	this.poison_probabilities_matrix = new double[this.num_fruit_bandit_worlds][this.num_fruit_types];
    	this.poison_probabilities_matrix[0][0] = .9;
    	this.poison_probabilities_matrix[0][1] = .1;
    	this.poison_probabilities_matrix[0][2] = .2;
    	
    	
    	this.stay_sick_probability = 0;
    	this.es_std = .1;
    	this.num_iterations = 10000;//10000;
    	this.creature_horizon = 100;
    	this.step_size = .1;
    	this.creature_learning_rate = 0;

		if(experiment_name == "Scenario1") {
	    	this.creature_learning_rate = 0;
			

		}
		else if (experiment_name == "Scenario2") {
	    	this.creature_learning_rate = .1;

			
		}
		
		else if (experiment_name == "Scenario3") {
	    	this.creature_learning_rate = .01;

			
		}
		/// Poison prob = 1 for one fruit.
		else if (experiment_name == "Scenario4") {
	    	
	    	this.poison_probabilities_matrix[0][0] = 1;	    	
	    	this.stay_sick_probability = 0;
	    	this.creature_learning_rate = .01;

			
		}
		/// Poison prob = 1 for one fruit.
		else if (experiment_name == "Scenario5")
		{	    	
	    	this.poison_probabilities_matrix[0][0] = 1;	
	    	this.stay_sick_probability = 0;
	    	this.creature_learning_rate = 0;
	
		}		

		/// Poison prob = 1 for one fruit.
		else if (experiment_name == "MultiworldScenarioReactive1")
		{	    	
			
			
	
	    	this.num_fruit_bandit_worlds = 2;
	    	this.worlds_probabilities = new double[2];
	    	this.worlds_probabilities[0] = .5;
	    	this.worlds_probabilities[1] = .5;
	    	
	    	
	    	this.fruit_type_probabilities_matrix = new double[this.num_fruit_bandit_worlds][this.num_fruit_types];
	    	this.fruit_type_probabilities_matrix[0][0] = .33;
	    	this.fruit_type_probabilities_matrix[0][1] = .33;
	    	this.fruit_type_probabilities_matrix[0][2] = .34;
	    	this.fruit_type_probabilities_matrix[1][0] = .33;
	    	this.fruit_type_probabilities_matrix[1][1] = .33;
	    	this.fruit_type_probabilities_matrix[1][2] = .34;
	    	
	    	
	    	
	    	
	    	this.poison_probabilities_matrix = new double[this.num_fruit_bandit_worlds][this.num_fruit_types];
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
			
			
	
	    	this.num_fruit_bandit_worlds = 2;
	    	this.worlds_probabilities = new double[2];
	    	this.worlds_probabilities[0] = .5;
	    	this.worlds_probabilities[1] = .5;
	    	
	    	
	    	this.fruit_type_probabilities_matrix = new double[this.num_fruit_bandit_worlds][this.num_fruit_types];
	    	this.fruit_type_probabilities_matrix[0][0] = .33;
	    	this.fruit_type_probabilities_matrix[0][1] = .33;
	    	this.fruit_type_probabilities_matrix[0][2] = .34;
	    	this.fruit_type_probabilities_matrix[1][0] = .33;
	    	this.fruit_type_probabilities_matrix[1][1] = .33;
	    	this.fruit_type_probabilities_matrix[1][2] = .34;
	    	
	    	
	    	
	    	
	    	this.poison_probabilities_matrix = new double[this.num_fruit_bandit_worlds][this.num_fruit_types];
	    	this.poison_probabilities_matrix[0][0] = .9;
	    	this.poison_probabilities_matrix[0][1] = .1;
	    	this.poison_probabilities_matrix[0][2] = .9;
	    	this.poison_probabilities_matrix[1][0] = .1;
	    	this.poison_probabilities_matrix[1][1] = .1;
	    	this.poison_probabilities_matrix[1][2] = .9;

	    	
	    	this.stay_sick_probability = 0;
	    	this.creature_learning_rate = 0.01;

	    	
	    	
		}		

		
		
	}
	
	
	
	
	
	
	public String get_experiment_filename_stub() {
		
		String file_name = this.experiment_name + "_fruitBandits_" + String.valueOf(this.num_fruit_types) + "_H"
				+ String.valueOf(this.creature_horizon) + "_T" + String.valueOf(this.num_iterations);

		return file_name;
		
	}
	
	
	
	
	public Experiment get_experiment(String experiment_name, int exp_identifier) throws Exception {
		this.set_experiment_params(experiment_name);		
		

		Experiment experiment = new Experiment(this.num_fruit_bandit_worlds, this.num_fruit_types, 
				this.worlds_probabilities, this.fruit_type_probabilities_matrix, 
    			this.poison_probabilities_matrix,  this.stay_sick_probability, 
    			this.es_std, this.creature_horizon, this.creature_learning_rate, exp_identifier);
		
		
		return experiment;
	
		
		
		
	}
	
	public MultiThreadedExperiment get_multithreaded_experiment(String experiment_name, int thread_index) throws Exception {
		this.set_experiment_params(experiment_name);		
		

		MultiThreadedExperiment experiment = new MultiThreadedExperiment(this.num_fruit_bandit_worlds, this.num_fruit_types, this.worlds_probabilities, this.fruit_type_probabilities_matrix, 
    			this.poison_probabilities_matrix,  this.stay_sick_probability, this.es_std, this.creature_horizon, this.creature_learning_rate,this.num_iterations, this.step_size, thread_index);
		
		
		return experiment;
	
		
		
		
	}

	

	
	
	public ExperimentManagerResults get_experiment_sweep_results_multithreaded() {
		double[][] rewards_matrix = new double[num_experiments][num_iterations];
		double[][][] used_probabilities_creature_tensor = new double[num_experiments][num_iterations][num_fruit_types];
		double[][][] used_probabilities_evolver_tensor = new double[num_experiments][num_iterations][num_fruit_types];
		
		
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
				
				
				
			} catch (Exception e) {
				
				System.out.println("Experiment sweep results failed");
				e.printStackTrace();
				/// Do noting
			}

		
		
		}
	
		
		
		
		System.out.println("Finished experiments multithreaded");
		
		
		for (int i = 0; i < num_experiments; i++) {
			ExperimentResults exp_results = experiments[i].get_exp_results();
			System.out.println(Arrays.toString(exp_results.rewards));

			
	
				rewards_matrix[i] = exp_results.get_rewards();
				used_probabilities_creature_tensor[i] = exp_results.get_used_probabilities_creature();
				used_probabilities_evolver_tensor[i] = exp_results.get_used_probabilities_evolver();

	
		
		
		}
	

		
		
		ExperimentManagerResults sweep_results = new ExperimentManagerResults(rewards_matrix, used_probabilities_creature_tensor, used_probabilities_evolver_tensor);
				
		return sweep_results;

		
		
		
		
	}
	
	
	
	
	
	
	public ExperimentManagerResults get_experiment_sweep_results() {
		
		
		double[][] rewards_matrix = new double[num_experiments][num_iterations];
		double[][][] used_probabilities_creature_tensor = new double[num_experiments][num_iterations][num_fruit_types];
		double[][][] used_probabilities_evolver_tensor = new double[num_experiments][num_iterations][num_fruit_types];
		
		for (int i = 0; i < num_experiments; i++) {

			try {
				Experiment experiment = this.get_experiment(experiment_name, i+1);
				

				ExperimentResults exp_results = experiment.run_experiment(num_iterations, step_size);
				

				rewards_matrix[i] = exp_results.get_rewards();
				used_probabilities_creature_tensor[i] = exp_results.get_used_probabilities_creature();
				used_probabilities_evolver_tensor[i] = exp_results.get_used_probabilities_evolver();

			} catch (Exception e) {
				
				System.out.println("Experiment sweep results failed");
				e.printStackTrace();
				/// Do noting
			}
		}
	
		
		ExperimentManagerResults sweep_results = new ExperimentManagerResults(rewards_matrix, used_probabilities_creature_tensor, used_probabilities_evolver_tensor);
				
		return sweep_results;
		
	}
	
	
	public void write_log_file(String folder, String file_name_stub, ExperimentManagerResults sweep_results) {
		
		
		
		String txt_filename = folder + file_name_stub + ".txt";
		
		try {

			FileWriter myWriter = new FileWriter(txt_filename);

			myWriter.write(String.valueOf(this.num_experiments));
			myWriter.write("\n");

			myWriter.write(String.valueOf(this.num_iterations));
			myWriter.write("\n");

			myWriter.write(String.valueOf(this.num_fruit_bandit_worlds));
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

			myWriter.write(String.valueOf(this.step_size));
			myWriter.write("\n");

			myWriter.write(Arrays.deepToString(sweep_results.rewards_matrix));
			myWriter.write("\n");

			myWriter.write(Arrays.deepToString(sweep_results.used_probabilities_creature_tensor));
			myWriter.write("\n");

			myWriter.write(Arrays.deepToString(sweep_results.used_probabilities_evolver_tensor));
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
		
		
		

		String[] experiment_names = {  "MultiworldScenarioReactive1", "MultiworldScenarioAdaptive1"	, "Scenario1", 
				"Scenario2", "Scenario3", "Scenario4", "Scenario5" };

		for (int j = 0; j < experiment_names.length; j++) {

			ExperimentManager exp_manager = new ExperimentManager();
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
