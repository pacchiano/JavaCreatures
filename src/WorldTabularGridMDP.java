
public class WorldTabularGridMDP extends WorldTabular {
	int length;
	int height;
	int max_index;
	int[] vector_current_location;
	
	
	
	public WorldTabularGridMDP(int length, int height, double move_probability, 
			int[] food_source_location) {
		
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
	

		if(ProbabilityUtils.sample_bernoulli(this.move_probability) == 1) {
			
			this.vector_current_location = GridUtils.get_next_location(this.length, this.height, creature_action, this.vector_current_location.clone());
		}

		
		this.current_location = GridUtils.convert_vector_to_int_location( this.length, this.height, this.vector_current_location);
				
		
		
		
		
		
	}
	
}
