//package smells;


import java.lang.Math;
import java.util.Arrays;
import java.util.Random;


class QuadraticFuncMatrix{
int[] dimensions;
double[][] bias;
double[][] scalings;
	
	
	public QuadraticFuncMatrix(int[] dimensions, double[][] bias, double[][] scalings) {
		this.dimensions = dimensions;
		this.bias = bias;
		this.scalings = scalings;
		
		
		
	}
	
	public double evaluate_point(double[][] x) {
		double value = 0;
		for(int i =0; i < this.dimensions[0]; i++) {
			for(int j =0; j < this.dimensions[1]; j++) {				
				value += this.scalings[i][j]*Math.pow(x[i][j] - this.bias[i][j],2);	
				
			}
		}
		return value;
	}
 	
	
	
}


public class ESmatrix {
	int[] dimensions;	
	/// Useful variables to keep around
	
	double[][] weights_vector;
	double std;
	private Random RandomGen = new Random();
	
	
	// long seed = 100;
	// RandomGen.setSeed(seed);
	
	public ESmatrix(int[] dimensions, double std) {
		this.dimensions = dimensions;
		this.weights_vector = new double[dimensions[0]][dimensions[1]];
		//Arrays.fill(this.lambda_matrix, initial_lambda_multiplier);
		this.std = std;
			
	}		


	/// Update of the probability matrix.
	public void set_weights_vector(double[][] weights_vector) {
		this.weights_vector = weights_vector;
	}
	
	
	public double[][] get_weights_vector(){	
		return this.weights_vector;
	}
	
	
	
	/// Gradient Ascent Using the ES gradient. Using only the reward from the perturbed weight vector. 
	public void update_statistics_single_sensing(double[][] sample_perturbation, double reward, double step_size){
		
		for(int i =0; i < this.dimensions[0]; i++) {
			for(int j =0; j < this.dimensions[1]; j ++) {
				
				this.weights_vector[i][j] += step_size/this.std*reward*sample_perturbation[i][j];
				
			}
			
		}	
	}
	

	/// Gradient Ascent Using the ES gradient. Using the reward from both the perturbed weight vector and the current weights. 
	public void update_statistics_double_sensing(double[][] sample_perturbation, double reward_perturbed, 
		double reward_base, double step_size){
		
		for(int i =0; i < this.dimensions[0]; i++) {
			for(int j =0; j < this.dimensions[1]; j ++) {	
				this.weights_vector[i][j] += step_size/this.std*(reward_perturbed- reward_base)*sample_perturbation[i][j];			
				
			}
		}
		
	}
	
	
	public double[][] get_sample_perturbation() {
		double[][] sample_perturbation;
		sample_perturbation = new double[this.dimensions[0]][this.dimensions[1]];
		for(int i=0; i< this.dimensions[0]; i++) {
			for(int j =0; j < this.dimensions[1]; j ++) {				
				sample_perturbation[i][j] = this.std*RandomGen.nextGaussian();
				
			}
		}
		
		return sample_perturbation;
		
	}
	
	
	public double[][] get_perturbed_datapoint(double[][] curr_point, double[][] perturbation) {
		double[][] result = new double[this.dimensions[0]][this.dimensions[1]];
		for(int i =0; i < this.dimensions[0]; i++) {
			for(int j =0; j < this.dimensions[1]; j ++) {
				result[i][j] = curr_point[i][j] + perturbation[i][j];
				
			}
			
		}
		
		return result;
	}
	
 	
	
	
    public static void main(String[] args) {

    	int[] dimensions = {3, 2};
    	double std = .1;
    	ESmatrix es_matrix_instance = new ESmatrix(dimensions, std);
  
    	
    	
    	double[][] sample_perturbation1 = es_matrix_instance.get_sample_perturbation();

        System.out.println("Sample perturbation");
        System.out.println(Arrays.deepToString(sample_perturbation1));
//        for (int i = 0; i < sample_string1.length; i++) {
//        	System.out.println(sample_string1[i]);
//        }
	
        System.out.println("Weights");
        System.out.println(Arrays.deepToString(es_matrix_instance.weights_vector));
        
 
        
        double[][] sample_perturbation2 = es_matrix_instance.get_sample_perturbation();
        double reward = 1.2;
        double step_size = .1;
        
        System.out.println("Second Sample Perturbation");
        System.out.println(Arrays.deepToString(sample_perturbation2));

        es_matrix_instance.update_statistics_single_sensing(sample_perturbation1, reward, step_size);
        
        
        
        System.out.println("Weights After Update");
        System.out.println(Arrays.deepToString(es_matrix_instance.weights_vector));
        
        
        
        System.out.println("Finished preparatory tests");
        
        
        double[][] bias  = new double[dimensions[0]][dimensions[1]];
        double[][] scalings = new double[dimensions[0]][dimensions[1]];
        /// filling the bias and scaling vectors.
	    for(int i =0; i < dimensions[0]; i++) {
	    	for(int j =0; j < dimensions[1]; j ++) {
		    	   bias[i][j] = 1.0*(i+j+1)/(dimensions[0] + dimensions[1]);
		    	   scalings[i][j] = 1;	    		
	    		
	    	}
	    	   
	       }
        
        
        
	    QuadraticFuncMatrix quadratic_func = new QuadraticFuncMatrix(dimensions, bias, scalings);

        
        double[][] weights_vector = new double[dimensions[0]][dimensions[1]];
        
        for(int i =0; i < dimensions[0]; i++) {
        	for(int j =0; j < dimensions[1]; j ++) {
          	   weights_vector[i][j] = -1;        		
        	}
        }

        es_matrix_instance.set_weights_vector( weights_vector);
        
        int num_iterations = 1000000;
        double[] rewards = new double[num_iterations];
        
        
        System.out.println("Created quadratic function");

        for(int i = 0; i < num_iterations; i++) {
        	double reward_base = -quadratic_func.evaluate_point(weights_vector);
        	double[][] perturbation = es_matrix_instance.get_sample_perturbation();
        	double[][] perturbed_curr_point = es_matrix_instance.get_perturbed_datapoint(weights_vector, perturbation);
        	double reward_perturbed = -quadratic_func.evaluate_point(perturbed_curr_point);

        	
        	es_matrix_instance.update_statistics_double_sensing( perturbation, reward_perturbed, 
        		reward_base, step_size);
        	weights_vector = es_matrix_instance.get_weights_vector();
        	
            System.out.println("Sample Reward");
            System.out.println(reward_base);
            System.out.println("Weights Vector");
            System.out.println(Arrays.deepToString(weights_vector));
            
        }
        
        
        

        
        
        
 
    }

    
	
	
	
	
	
	
}
