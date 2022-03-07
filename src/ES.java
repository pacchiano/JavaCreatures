//package smells;


import java.lang.Math;
import java.util.Arrays;
import java.util.Random;



class QuadraticFunc{
int dimension;
double[] bias;
double[] scalings;
	
	
	public QuadraticFunc(int dimension, double[] bias, double[] scalings) {
		this.dimension = dimension;
		this.bias = bias;
		this.scalings = scalings;
		
	}
	
	public double evaluate_point(double[] x) {
		double value = 0;
		for(int i =0; i < this.dimension; i++) {
			value += this.scalings[i]*Math.pow(x[i] - this.bias[i],2);	
		}
		return value;
	}
 	
	
	
}


public class ES {
	private int dimension;	
	/// Useful variables to keep around
	
	private double[] weights_vector;
	private double std;
	private Random RandomGen = new Random();
	
	
	// long seed = 100;
	// RandomGen.setSeed(seed);
	
	public ES(int dimension, double std) {
			this.weights_vector = new double[dimension];
			//Arrays.fill(this.lambda_matrix, initial_lambda_multiplier);

			this.dimension = dimension;
			this.std = std;
			
	}		

	
	public void gaussian_random_reset_weights_vector() {
		
		
	}

	/// Update of the probability matrix.
	public void set_weights_vector(double[] weights_vector) {
		this.weights_vector = weights_vector;
	}
	
	
	public double[] get_weights_vector(){	
		return this.weights_vector;
	}
	
	
	
	/// Gradient Ascent Using the ES gradient. Using only the reward from the perturbed weight vector. 
	public void update_statistics_single_sensing(double[] sample_perturbation, double reward, double step_size){
		
		for(int i =0; i < this.weights_vector.length; i++) {
			this.weights_vector[i] += step_size/this.std*reward*sample_perturbation[i];
		}	
	}
	

	/// Gradient Ascent Using the ES gradient. Using the reward from both the perturbed weight vector and the current weights. 
	public void update_statistics_double_sensing(double[] sample_perturbation, double reward_perturbed, 
			double reward_base, double step_size){
		
		for(int i =0; i < this.weights_vector.length; i++) {
			this.weights_vector[i] += step_size/this.std*(reward_perturbed- reward_base)*sample_perturbation[i];			
		}
		
	}
	
	
	public double[] get_sample_perturbation() {
		double[] sample_perturbation;
		sample_perturbation = new double[this.dimension];
		for(int i=0; i< this.dimension; i++) {
			sample_perturbation[i] = this.std*RandomGen.nextGaussian();
		}
		
		return sample_perturbation;
		
	}
	
	
	public double[] get_perturbed_datapoint(double[] curr_point, double[] perturbation) {
		double[] result = new double[this.dimension];
		for(int i =0; i < this.dimension; i++) {
			result[i] = curr_point[i] + perturbation[i];
			
		}
		
		return result;
	}
	
 	
	
	
	
	
	
    public static void main(String[] args) {

    	int dimension = 10;
    	double std = .1;
    	ES es_instance = new ES(dimension, std);
  
    	
    	
    	double[] sample_perturbation1 = es_instance.get_sample_perturbation();

        System.out.println("Sample perturbation");
        System.out.println(Arrays.toString(sample_perturbation1));
//        for (int i = 0; i < sample_string1.length; i++) {
//        	System.out.println(sample_string1[i]);
//        }
	
        System.out.println("Weights");
        System.out.println(Arrays.toString(es_instance.weights_vector));
        
 
        
        double[] sample_perturbation2 = es_instance.get_sample_perturbation();
        double reward = 1.2;
        double step_size = .1;
        
        System.out.println("Second Sample Perturbation");
        System.out.println(Arrays.toString(sample_perturbation2));

        es_instance.update_statistics_single_sensing(sample_perturbation1, reward, step_size);
        
        
        
        System.out.println("Weights After Update");
        System.out.println(Arrays.toString(es_instance.weights_vector));
        
        
        
        double[] bias  = new double[dimension];
        double[] scalings = new double[dimension];
        /// filling the bias and scaling vectors.
	    for(int i =0; i < dimension; i++) {
	    	   bias[i] = 1.0*(i+1)/dimension;
	    	   scalings[i] = 1;
	    	   
	       }
        
        
        
        QuadraticFunc quadratic_func = new QuadraticFunc(dimension, bias, scalings);

        
        
        double[] weights_vector = new double[dimension];
        
        for(int i =0; i < dimension; i++) {
     	   weights_vector[i] = -1;
        }

        es_instance.set_weights_vector( weights_vector);
        
        int num_iterations = 1000;
        double[] rewards = new double[num_iterations];

        for(int i = 0; i < num_iterations; i++) {
        	double reward_base = -quadratic_func.evaluate_point(weights_vector);
        	double[] perturbation = es_instance.get_sample_perturbation();
        	double[] perturbed_curr_point = es_instance.get_perturbed_datapoint(weights_vector, perturbation);
        	double reward_perturbed = -quadratic_func.evaluate_point(perturbed_curr_point);
        	es_instance.update_statistics_double_sensing( perturbation, reward_perturbed, 
        		reward_base, step_size);
        	weights_vector = es_instance.get_weights_vector();
        	
            System.out.println("Sample Reward");
            System.out.println(reward_base);
            System.out.println("Weights Vector");
            System.out.println(Arrays.toString(weights_vector));
            rewards[i] = reward_base;
            
        }
        
        System.out.println("Total rewards");
        System.out.println(Arrays.toString(rewards));
        

        
        
        
 
    }

    
	
	
	
	
	
	
}
