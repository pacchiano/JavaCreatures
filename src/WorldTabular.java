
public abstract class WorldTabular extends World{
	
	int current_location;
	double move_probability;
	int food_location;
	int num_states;
	
	
	
	
	
	public void reset_world() {
		this.current_location = 0;
	
	}
	
	
	
	public abstract void step(int creature_action);
	
	public int get_state_dimension() {
		return 2;
	}
	
	
	
	public double[] get_state() {
		
		double[] result = new double[2];	
		result[0] = this.current_location;
		if(this.current_location == this.food_location) {
			result[1] = 1;
		}
		else {
			result[1] = 0;
		}
		return result;
	
	}

	
	public double[] get_reward_vector() {
		
		double[] result = new double[this.num_states];
		result[this.food_location] = 1;
		return result;
		
		
	}
	

	
	
	

}
