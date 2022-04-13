import java.util.Arrays;

public class WorldDistributionChainMDP extends WorldDistribution{

	public WorldDistributionChainMDP(int num_chain_MDP_bandit_worlds, double[] worlds_probabilities,
			int chain_length, double move_probability)
		throws Exception {
		
		super( num_chain_MDP_bandit_worlds,  worlds_probabilities);
		
		
		this.worlds = new WorldChainMDP[num_chain_MDP_bandit_worlds];
		for (int i = 0; i < num_chain_MDP_bandit_worlds; i++) {
			this.worlds[i] = new WorldChainMDP(chain_length, move_probability);
			

		}
		

		
		

	}


	public static void main(String[] args) {

		int num_steps = 10;
		int chain_length = 10;
		double move_probability = .1;
				
		int num_worlds = 2;
		double[] worlds_probabilities = { .5, .5 };

		try {

			WorldDistributionChainMDP chain_worlds_distribution = new WorldDistributionChainMDP(num_worlds, worlds_probabilities, 
					chain_length, move_probability);
			
			
//			System.out.println("here!!");
//			System.out.println(Arrays.toString(chain_worlds_distribution.worlds));

			
			
			for (int i = 0; i < num_steps; i++) {
				WorldInfo chainworld_info = chain_worlds_distribution.get_world();
				World chainworld = chainworld_info.get_world();
				
				int[] result = chainworld.get_state();

				//System.out.println("Asfdlkamsdflkamsdflkamsdlfkm");
				
				System.out.println(Arrays.toString(result));

			}

		} catch (Exception e) {
			
		System.out.println("Main failed in chain world distribution");
			
		}

	}

}
