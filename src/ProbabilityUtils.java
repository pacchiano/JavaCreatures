//package smells;

import java.lang.Math;


public final class ProbabilityUtils {

	private ProbabilityUtils() {}
	
	public static int sample_index(double[] probabilities_array) {
		int sample_val = -1;
		double random_val = Math.random();
		double running_sum = 0;
		for(int j =0; j < probabilities_array.length; j ++) {
			if(running_sum < random_val && random_val <= running_sum + probabilities_array[j]  ) {
				sample_val = j;
			}
			
			running_sum += probabilities_array[j];
			
		}

		return sample_val;
		
	}
	
	
	public static int sample_bernoulli(double probability_param) {
		double random_val = Math.random();
		//int result;
		
		if(random_val > probability_param) {
			return 0;
		}
		else {
			return 1;
		}
				
	}
	

	
	
	
	
	
}
