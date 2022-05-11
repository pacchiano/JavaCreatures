import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class WorldMultifruitGridMDP extends World{

	
	
	int length;
	int height;
	
	int max_index;
	int[] vector_current_location;
	double move_probability;
	int current_location;
	
	
	//int num_food_types;
	int num_food_sources;
	ArrayList<Integer> food_locations;
	ArrayList<Integer> empty_locations;
	ArrayList<Integer> food_types;
	Random random;
	
	double[] poison_probabilities;
	
	
	int num_food_types;
	
	
	public WorldMultifruitGridMDP(int length, int height, double move_probability, 
			int num_food_types, int num_food_sources, double[] poison_probabilities) {
		
		this.length = length;
		this.height = height;
		this.move_probability = move_probability;
		//this.num_food_types = num_food_types;
		
		this.current_location = 0;
		/// The vector current location is [height_index, length_index]
		this.vector_current_location  =  new int[]{0,0};

		
		this.poison_probabilities = poison_probabilities;
		this.random = new Random();
		
		this.num_food_sources = num_food_sources;
		this.num_food_types= num_food_types;
			
		
		this.food_locations = new ArrayList<Integer>();
		this.food_types = new ArrayList<Integer>();
		this.empty_locations = new ArrayList<Integer>();
		//this.empty_locations = new ArrayList<Integer>(); 
		for(int i =0; i < this.length*this.height; i ++) {
			if(i != GridUtils.convert_vector_to_int_location( this.length, this.height, this.vector_current_location)){
				this.empty_locations.add(i);
			}			
			
		}
		
		this.respawn_food();
		
		
		if(this.num_food_types != poison_probabilities.length ) {
			
			throw new java.lang.Error("The number of fruit types is different from the poison probabilities legnth. Grid World");
		}
		
	}
	
	
	
	
	public void respawn_food() {
		
		int num_missing_food_locs = this.num_food_sources - this.food_locations.size();
		
		
		if(num_missing_food_locs > 0) {
		
		Collections.shuffle(this.empty_locations);
		
		
		//// Fill the food locations list.
		for(int i = 0; i < num_missing_food_locs; i ++) {
			
			this.food_locations.add(this.empty_locations.get(i));			
			int food_type = this.random.nextInt(this.num_food_types);
			
			this.food_types.add(food_type);
			this.empty_locations.remove(0);
			
			}
		
		}			
	}
	
	
	
	
	public int get_state_dimension() {
		
		return 3 + this.num_food_types;
		
	
	}
	
	
	
	
	
	public double[] get_state() {
		/// 
		double[] state = new double[5 + this.num_food_types];	
		state[0] = this.current_location;
		if(this.food_locations.contains(current_location) ) {
			state[1] = 1;
			int index = this.food_locations.indexOf(current_location);
			int food_type = this.food_types.get(index);
			int is_poisonous = ProbabilityUtils.sample_bernoulli(this.poison_probabilities[food_type]);
			state[2] = is_poisonous;
			
		}
		else {
			state[1] = 0;
			state[2] = 0;
			
		}
		
		state[3] = this.vector_current_location[0];
		state[4] = this.vector_current_location[1];		
		
		
		
		int curr_index = 5;
		

		for(int i=0; i < this.num_food_types; i++) {
			
			double[] potential = {0,0};
			
			
			
			for(int j= 0; j < this.num_food_sources; j ++  ) {
				
				int[] food_source_vector = GridUtils.convert_int_to_vector_location(this.length,  this.height,  this.food_locations.get(j));
				
				
				
				if(this.food_types.get(j) == i) {
					
					double norm_diff = Utilities.norm_difference(this.vector_current_location, food_source_vector);
					
					
					potential[0] += Math.exp(-norm_diff)*(food_source_vector[0]- this.vector_current_location[0])/norm_diff;	
					potential[1] +=  Math.exp(-norm_diff)*(food_source_vector[1]- this.vector_current_location[1])/norm_diff;
					
					
				}
				
				
			}
			
			
			state[curr_index] = potential[0];
			state[curr_index +1] = potential[1]; 
						
			curr_index += 2;
			
			
		}
		
		return state;
	
		
	}
	

	public void reset_world() {
		this.vector_current_location[0] = 0;
		this.vector_current_location[1] = 0;
		
		
		this.current_location =	GridUtils.convert_vector_to_int_location( this.length, this.height, 
				this.vector_current_location);

	
		this.respawn_food();
	
	}
	
	
	
	public void step(int creature_action) {
		
		boolean is_initial_food_location = this.food_locations.contains( this.current_location);
		
		int initial_location = this.current_location;
		
		
		if(ProbabilityUtils.sample_bernoulli(this.move_probability) == 1) {
			
			this.vector_current_location = GridUtils.get_next_location(this.length, this.height, creature_action, this.vector_current_location.clone());
		}

		
		this.current_location = GridUtils.convert_vector_to_int_location( this.length, this.height, this.vector_current_location);

		
		
		
		//// add old current location back to this.empty_locations
		
		
		if( this.empty_locations.contains(this.current_location)  ) {
			this.empty_locations.remove(this.current_location);
			}
		
		
		
		
		
		boolean is_new_food_location = this.food_locations.contains(this.current_location);

		
		
		
		if( !this.empty_locations.contains(initial_location) && initial_location != this.current_location && this.food_locations.contains(initial_location) ) {
			this.empty_locations.add(initial_location);
			
		}

		
		
		//// This may need to be done later.
		if(is_initial_food_location && !is_new_food_location ) {
			int initial_location_index = this.food_locations.indexOf(initial_location);
			
			this.food_locations.remove(initial_location_index);
			this.respawn_food();
			
			
		}
		
		
	}
	
	
	
	
    public static void main(String[] args) {

    	
    	int length = 10;
    	int height = 20;
    	double move_probability = .8;
    	int num_food_types = 2;
    	int num_food_sources = 3;
    	double[] poison_probabilities = {.2, .9};
    	
    	
    	WorldMultifruitGridMDP multifruitgridMDP	 = new WorldMultifruitGridMDP( length,  height,  move_probability, 
    			 num_food_types,  num_food_sources, poison_probabilities);
    	
    	
    	
    	
    	
    	
    	
    	
    }
	
	
	
	
	
	
	
}
