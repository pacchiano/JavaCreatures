
public class WorldTabularChainMDP extends WorldTabular {
	
	int length;
	
	
	public WorldTabularChainMDP(int length, double move_probability) {
		this.length = length;
		this.move_probability = move_probability;
		this.current_location = 0;
		this.food_location = length - 1;
		this.num_states = length;
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
