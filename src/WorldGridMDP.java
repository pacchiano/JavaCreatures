import java.util.concurrent.ThreadLocalRandom;

public class WorldGridMDP extends World {

	int length;
	int height;
	int current_location;
	int food_location;
	int max_index;
	
	double move_probability;
	
	public WorldGridMDP(int length, int height, int move_probability) {
		
		this.length = length;
		this.height = height;
		this.move_probability = move_probability;
		
		this.max_index = length*height-1;
	
	
		this.current_location = 0;
		this.food_location = ThreadLocalRandom.current().nextInt(0, this.max_index);
			
	}
	
	
	
	
	public int[] get_state() {
		
		int[] result = new int[2];	
		result[0] = this.current_location;
		if(this.current_location == this.food_location) {
			result[1] = 1;
		}
		else {
			result[1] = 0;
		}
		return result;
		
		
		
		
		
	}
	
	
	
	
	public void reset_world() {
		this.current_location = 0;
	
}


	public void step(int creature_action) {
		/// Stay
		if(creature_action == 0) {
			;
			
		}

		int success = ProbabilityUtils.sample_bernoulli(this.move_probability);

		/// Move left
		if(creature_action == 1) {
			this.current_location = Math.max(this.current_location - success, 0 );			
			
		}

		/// Trye to move right
		if(creature_action == 2) {
			this.current_location = Math.min(this.current_location + success, this.length-1 );			
			
		}
		
		/// Move up
		if(creature_action == 3) {
			this.current_location = Math.max(this.current_location - success, 0 );			
			
		}

		/// Move down
		if(creature_action == 4) {
			this.current_location = Math.min(this.current_location + success, this.length-1 );			
			
		}
		

		
		
		
		
		
	}
	
	
	
	
	
}
