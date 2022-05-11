 public final class GridUtils {

	private GridUtils() {}
	
	
	
	public static int convert_vector_to_int_location(int length, int height, int[] vector_current_location) {
		return vector_current_location[0]*length + vector_current_location[1];

		
	}
	
	
	
	
	public static int[] convert_int_to_vector_location(int length, int height, int int_current_location) {
		int[] vector_location = {0,0};
		vector_location[0] =  int_current_location/length;
		vector_location[1] = int_current_location - int_current_location*( int_current_location/length);
		
		return vector_location;

		
	}

	
	
	/// It is convenient to feed a copy of the vector_current_location to this method.
	public static int[] get_next_location(int length, int height, int creature_action, int[] vector_current_location) {
		
		if(creature_action == 0) {
			return vector_current_location;
			
		}
		


		
		
		/// Move left - decrease the length index by one
		if(creature_action == 1) {
			vector_current_location[1] = Math.max(vector_current_location[1] - 1, 0 );			
			
			
		}

		/// Trye to move right - increase the length index by one
		if(creature_action == 2) {
			vector_current_location[1] = Math.min(vector_current_location[1] + 1, length-1);
			//this.current_location = Math.min(this.current_location + success, this.length-1 );			
			
		}
		
		/// Move up - decrease the height index by one
		if(creature_action == 3) {
			vector_current_location[0] = Math.max(vector_current_location[0] - 1, 0 );			

			
		}

		/// Move down - increase the height index by one
		if(creature_action == 4) {
			vector_current_location[0] = Math.min(vector_current_location[0] + 1, height-1);
			
		}
		
		return vector_current_location;
		
		
		
	}

	
	
	
	
	
}

