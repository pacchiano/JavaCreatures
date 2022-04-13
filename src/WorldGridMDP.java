import java.util.Arrays;

public class WorldGridMDP extends WorldTabular {

	int length;
	int height;
	int max_index;
	int[] vector_current_location;
	
	public WorldGridMDP(int length, int height, double move_probability) {
		
		this.length = length;
		this.height = height;
		this.move_probability = move_probability;
		
		this.max_index = length*height-1;
	
	
		this.current_location = 0;
		/// The vector current location is [height_index, length_index]
		this.vector_current_location  =  new int[]{0,0};
		this.food_location =  length*height-1;//ThreadLocalRandom.current().nextInt(0, this.max_index);
		this.num_states = this.length*this.height;
			
	}
	
	
	
	public double[] get_reward_vector() {
		
		double[] result = new double[this.length*this.height];
		result[this.food_location] = 1;
		return result;
		
		
	}
	
	
	

	public void step(int creature_action) {
		/// Stay
		if(creature_action == 0) {
			;
			
		}

		int success = ProbabilityUtils.sample_bernoulli(this.move_probability);

		/// Move left - decrease the length index by one
		if(creature_action == 1) {
			this.vector_current_location[1] = Math.max(this.vector_current_location[1] - success, 0 );			
			
			
		}

		/// Trye to move right - increase the length index by one
		if(creature_action == 2) {
			this.vector_current_location[1] = Math.min(this.vector_current_location[1] + success, this.length-1);
			//this.current_location = Math.min(this.current_location + success, this.length-1 );			
			
		}
		
		/// Move up - decrease the height index by one
		if(creature_action == 3) {
			this.vector_current_location[0] = Math.max(this.vector_current_location[0] - success, 0 );			

			
		}

		/// Move down - increase the height index by one
		if(creature_action == 4) {
			this.vector_current_location[0] = Math.min(this.vector_current_location[0] + success, this.height-1);
			
		}
		

		
		this.current_location = this.vector_current_location[0]*this.length + this.vector_current_location[1];
		
		
		
		
		
	}
	
	
	
	
	
}
