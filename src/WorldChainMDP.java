
public class WorldChainMDP extends World {
	
	int length;
	double move_probability;
	int current_location;
	int food_location;
	
	public WorldChainMDP(int length, double move_probability) {
		this.length = length;
		this.move_probability = move_probability;
		this.current_location = 0;
		this.food_location = length - 1;
	}
	
	
	
	/// Chain MDP states are of dimension 2
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
		
		
		
		
		
		
		
	}

	
	
	

}
