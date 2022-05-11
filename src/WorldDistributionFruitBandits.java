//package smells;

//import java.io.IOException;
import java.util.Arrays;




public class WorldDistributionFruitBandits extends WorldDistribution{

	//int num_worlds;
	//FruitBanditsWorld[] worlds;
	//double[] worlds_probabilities;

	public WorldDistributionFruitBandits(int num_fruit_bandit_worlds, double[] worlds_probabilities,
			int num_fruit_types, double[][] fruit_type_probabilities_matrix, double[][] poison_probabilities_matrix)
		throws Exception {
		
		super( num_fruit_bandit_worlds,  worlds_probabilities);
		
		
		/// INITIALIZE FRUIT WORLDS
		this.worlds = new WorldFruitBandits[num_fruit_bandit_worlds];
		for (int i = 0; i < num_fruit_bandit_worlds; i++) {
			this.worlds[i] = new WorldFruitBandits(num_fruit_types, fruit_type_probabilities_matrix[i],
					poison_probabilities_matrix[i]);
			

		}
		

		
		

	}


	public static void main(String[] args) {

		int num_steps = 1000;
		int num_fruit_types = 3;
		int num_fruit_bandit_worlds = 2;
		double[][] fruit_type_probabilities_matrix = { { .1, .2, .7 }, { .1, .2, .7 } };
		double[][] poison_probabilities_matrix = { { .5, .4, .3 }, { 1, .4, .3 } };
		double[] worlds_probabilities = { .5, .5 };

		try {

			WorldDistributionFruitBandits fruit_worlds_distribution = new WorldDistributionFruitBandits(
					num_fruit_bandit_worlds, worlds_probabilities, num_fruit_types, fruit_type_probabilities_matrix,
					poison_probabilities_matrix);
			
			System.out.println("here!!");
			System.out.println(Arrays.toString(fruit_worlds_distribution.worlds));

			
			
			for (int i = 0; i < num_steps; i++) {
				WorldInfo fruitworld_info = fruit_worlds_distribution.get_world();
				World fruitworld = fruitworld_info.get_world();
				
				double[] result = fruitworld.get_state();

				System.out.println(Arrays.toString(result));

			}

		} catch (Exception e) {
			
		System.out.println("Main failed in fruit banidts world distribution");
			/// Do noting
		}

	}

}
