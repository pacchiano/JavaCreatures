//package smells;

//import java.io.IOException;
import java.util.Arrays;


class FruitBanditsWorldInfo {         
      FruitBanditsWorld fruit_bandit_world;
      int world_index;

    public FruitBanditsWorldInfo(FruitBanditsWorld fruit_bandit_world, int world_index) {         
        this.fruit_bandit_world= fruit_bandit_world;
        this.world_index= world_index;
     }
    
    public FruitBanditsWorld get_fruit_bandit_world() {
    	return this.fruit_bandit_world;
    	
    }
    
    public int get_world_index() {
    	return this.world_index;
    }
    
    
 }


public class FruitBanditsWorldDistribution {

	int num_fruit_bandit_worlds;
	FruitBanditsWorld[] fruit_bandits_worlds;
	double[] worlds_probabilities;

	public FruitBanditsWorldDistribution(int num_fruit_bandit_worlds, double[] worlds_probabilities,
			int num_fruit_types, double[][] fruit_type_probabilities_matrix, double[][] poison_probabilities_matrix)
			throws Exception {

		this.num_fruit_bandit_worlds = num_fruit_bandit_worlds;
		this.worlds_probabilities = worlds_probabilities;
		fruit_bandits_worlds = new FruitBanditsWorld[num_fruit_bandit_worlds];

		/// check that worlds_probabilities sum to 1
		if (Arrays.stream(this.worlds_probabilities).sum() != 1) {
			throw new Exception("The sum of the worlds probabilities does not equal one.");
		}

		if (this.worlds_probabilities.length != num_fruit_bandit_worlds) {
			throw new Exception(
					"The size of the world probabilities vector is different from the num_fruit_bandit_worlds variable.");

		}

		for (int i = 0; i < num_fruit_bandit_worlds; i++) {
			fruit_bandits_worlds[i] = new FruitBanditsWorld(num_fruit_types, fruit_type_probabilities_matrix[i],
					poison_probabilities_matrix[i]);

		}

	}

	public FruitBanditsWorldInfo get_fruit_world() {

		int fruit_world_index = ProbabilityUtils.sample_index(this.worlds_probabilities);
		if(Flags.verbose) {
			//System.out.println("Fruit world index");
			//System.out.println(fruit_world_index);
			}
		
		
			FruitBanditsWorld sample_fruit_world = this.fruit_bandits_worlds[fruit_world_index];
			FruitBanditsWorldInfo fruit_world_info = new FruitBanditsWorldInfo( sample_fruit_world, fruit_world_index  ); 
			
			return fruit_world_info;

	}

	public static void main(String[] args) {

		int num_steps = 1000;
		int num_fruit_types = 3;
		int num_fruit_bandit_worlds = 2;
		double[][] fruit_type_probabilities_matrix = { { .1, .2, .7 }, { .1, .2, .7 } };
		double[][] poison_probabilities_matrix = { { .5, .4, .3 }, { 1, .4, .3 } };
		double[] worlds_probabilities = { .5, .5 };

		try {

			FruitBanditsWorldDistribution fruit_worlds_distribution = new FruitBanditsWorldDistribution(
					num_fruit_bandit_worlds, worlds_probabilities, num_fruit_types, fruit_type_probabilities_matrix,
					poison_probabilities_matrix);
			for (int i = 0; i < num_steps; i++) {
				FruitBanditsWorldInfo fruitworld_info = fruit_worlds_distribution.get_fruit_world();
				FruitBanditsWorld fruitworld = fruitworld_info.get_fruit_bandit_world();
				
				int[] result = fruitworld.get_fruit_type_poison_type();

				System.out.println(Arrays.toString(result));

			}

		} catch (Exception e) {
			
		System.out.println("Main failed in fruit banidts world distribution");
			/// Do noting
		}

	}

}
