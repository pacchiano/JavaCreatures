//package smells;

import java.lang.Math;
import java.util.Arrays;


public class EFES {
	double[][] lambda_matrix;
	int string_length;
	int num_symbols;
	double[][] probability_matrix;
	
	/// Useful variables to keep around
	double[] normalization_factors;

	
	
	
	public EFES(int string_length, int num_symbols, double initial_lambda_multiplier) {
			this.lambda_matrix = new double[string_length][num_symbols];
			//Arrays.fill(this.lambda_matrix, initial_lambda_multiplier);

			this.string_length = string_length;
			this.num_symbols = num_symbols;
			
			this.normalization_factors = new double[string_length];
			this.probability_matrix = new double[string_length][num_symbols];
			this.update_probability_matrix();
			
	}		

	
	
	/// Update of the probability matrix.
	public void update_probability_matrix() {
				
		// Compute normalization factors
		for(int i=0 ; i < this.string_length; i++) {
			double row_sum_exps = 0;
			for(int j =0; j < this.num_symbols; j++  ) {
				row_sum_exps += Math.exp(this.lambda_matrix[i][j]);
					
				}
				this.normalization_factors[i] =1.0/row_sum_exps;		
		}
		
		// Compute the probabilities matrix
		for(int i =0; i < this.string_length; i ++) {
			for(int j =0; j < this.num_symbols; j ++) {
				
				this.probability_matrix[i][j] = Math.exp(this.lambda_matrix[i][j])*this.normalization_factors[i];
			}	
		}
	}
	

	public void set_lambda_matrix(double[][] lambda_matrix) {
		this.lambda_matrix = lambda_matrix;
	}
	
	
	public double[][] get_lambda_matrix(){	
		return this.probability_matrix;
	}
	
	
	public void update_statistics(int[] sample_string1, int[] sample_string2, 
			double reward, double step_size){
		
		for(int i =0; i < this.string_length; i++) {
			this.lambda_matrix[i][sample_string1[i]] += step_size*reward;
			this.lambda_matrix[i][sample_string2[i]] -= step_size*reward;
			
		}
		
		this.update_probability_matrix();
		
	}
	
	
	
	public int[] sample_string() {
		int[] sample_string;
		sample_string = new int[this.string_length];
		for(int i=0; i< this.string_length; i++) {
			sample_string[i]= ProbabilityUtils.sample_index(this.probability_matrix[i]);
						
		}
		
		
		
		return sample_string;
		
	}
 	
	
	
    public static void main(String[] args) {

    	EFES efes_instance = new EFES(10, 3, .1);
  
    	int[] sample_string1 = efes_instance.sample_string();

        System.out.println("Sample string");
        System.out.println(Arrays.toString(sample_string1));
//        for (int i = 0; i < sample_string1.length; i++) {
//        	System.out.println(sample_string1[i]);
//        }
	
        System.out.println("Lambdas");
        System.out.println(Arrays.deepToString(efes_instance.lambda_matrix));
        
        
        System.out.println("Probabilities");
        System.out.println(Arrays.deepToString(efes_instance.probability_matrix));

        
        int[] sample_string2 = efes_instance.sample_string();
        double reward = 1.2;
        double step_size = .1;
        
        System.out.println("Second Sample String");
        System.out.println(Arrays.toString(sample_string2));

        
        efes_instance.update_statistics(sample_string1, sample_string2, reward, step_size);
        
        
        
        System.out.println("Lambdas After Update");
        System.out.println(Arrays.deepToString(efes_instance.lambda_matrix));
        
        
        System.out.println("Probabilities After Update");
        System.out.println(Arrays.deepToString(efes_instance.probability_matrix));

        
 
    }

    
	
	
	
	
	
	
}
