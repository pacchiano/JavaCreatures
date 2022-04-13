import java.util.Arrays;

class WorldInfo {         
      World world;
      int world_index;

    public WorldInfo(World world, int world_index) {         
        this.world= world;
        this.world_index= world_index;
     }
    
    public World get_world() {
    	return this.world;
    	
    }
    
    public int get_world_index() {
    	return this.world_index;
    }
    
    
 }



public abstract class WorldDistribution{

	int num_worlds;
	World[] worlds;
	double[] worlds_probabilities;

	
	public WorldDistribution(int num_worlds, double[] worlds_probabilities) throws Exception {

		this.num_worlds = num_worlds;
		this.worlds_probabilities = worlds_probabilities;
	
				

		/// check that worlds_probabilities sum to 1
		if (Arrays.stream(this.worlds_probabilities).sum() != 1) {
			throw new Exception("The sum of the worlds probabilities does not equal one.");
		}

		if (this.worlds_probabilities.length != num_worlds) {
			throw new Exception(
					"The size of the world probabilities vector is different from the num_worlds variable.");

		}

	}
	
	
	public WorldInfo get_world() {

		int world_index = ProbabilityUtils.sample_index(this.worlds_probabilities);
//		if(Flags.verbose) {
//			System.out.println("World index");
//			System.out.println(world_index);
//			}
		
		
			//System.out.println(Arrays.toString(this.worlds));

		
			World sample_world = this.worlds[world_index];
		
			sample_world.reset_world();
			
			WorldInfo world_info = new WorldInfo( sample_world, world_index  ); 
			
			return world_info;

	}

	

	
	}

