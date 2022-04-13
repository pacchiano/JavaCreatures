
//package smells;

import java.util.Arrays;

public class WorldFruitBandits extends World {
	double[] poison_probabilities;
	double[] fruit_type_probabilities;
	int num_fruit_types; 
	
	
	
	//// fruit_type_probabilities specify the probability over fruit types.
	//// poison_probabilities specifies the probability that a fruit may be poisonous.
	public WorldFruitBandits( int num_fruit_types, double[] fruit_type_probabilities, double[] poison_probabilities		) {
		this.num_fruit_types = num_fruit_types;
		this.poison_probabilities = poison_probabilities;
		this.fruit_type_probabilities = fruit_type_probabilities;

		if(this.num_fruit_types != this.poison_probabilities.length) {
			throw new java.lang.Error("The number of fruit types is different from the poison probabilities length");
		}

		if(this.num_fruit_types != this.fruit_type_probabilities.length) {
			throw new java.lang.Error("The number of fruit types is different from the fruit type probabilities length");
		}

		
		/// Check the sum of the fruit type probabilities equals one.
		double prob_sum = 0 ;
		for(int i =0; i <this.num_fruit_types; i++) {
			prob_sum += this.fruit_type_probabilities[i];
		}
		if(prob_sum != 1) {
			throw new java.lang.Error("The sum of probabilities in fruit_type_probabilities is not one.");
		}
		
		
	}
	

	public int[] get_fruit_type_poison_info() {
		int[] result = new int[2];
		/// Sample fruit type. Uniformly at random. 
		int fruit_type_index = ProbabilityUtils.sample_index(this.fruit_type_probabilities);
		result[0] = fruit_type_index;
		int is_poisonous = ProbabilityUtils.sample_bernoulli(this.poison_probabilities[fruit_type_index]);
		
		result[1] = is_poisonous;
		
		return result;
		
		
	}

	
	
	
	public int[] get_state() {
		return this.get_fruit_type_poison_info();
		
	}
	

	
	
	public void reset_world() {}
	
	
	public void step(int creature_action) {}
	
	
	
	
    public static void main(String[] args) {

    	int num_steps = 1000;
    	int num_fruit_types = 3;
    	double[] fruit_type_probabilities = {.1,  .2, .7};
    	double[] poison_probabilities = {.5, .4, .3};
    	
    	WorldFruitBandits fruitworld = new WorldFruitBandits( num_fruit_types, fruit_type_probabilities, poison_probabilities		) ;
    	for(int i=0; i < num_steps; i++) {
    		int[] result = fruitworld.get_state();
    		
    		
    		System.out.println(Arrays.toString(result));

    	}
    	
    	
    	
    }
	
	
	
	
	
}
