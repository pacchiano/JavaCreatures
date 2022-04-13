import java.util.Arrays;

public class CreatureTabularQ extends Creature{

	int num_states;
	int num_actions;
	double [][] q_table;
	double [] proximate_reward_matrix;
	double [] opt_actions;
	double creature_learning_rate;
	double discount;
	
	
	public CreatureTabularQ(int num_states, int num_actions, double discount, double creature_learning_rate) {
		super(2);
		this.num_states = num_states;
		this.num_actions = num_actions;
		this.creature_learning_rate = creature_learning_rate;
		this.discount = discount;
		this.reset();
	}
	
	
	/// Remember the state dimension is 2
	public PairIntegerDouble step(int[] state ) {
		int current_location = state[0];
		int ultimate_reward = state[1];
		

		System.out.println("Current Location");
		
		System.out.println(current_location);
		
		
		
		PairIntegerDouble max_index_max_q = this.get_argmax_q(current_location);
		
		
		this.ultimate_reward_collected += ultimate_reward;
		
		double proximate_reward = this.proximate_reward_matrix[current_location];
		
		PairIntegerDouble action_reward = new PairIntegerDouble(max_index_max_q.get_my_integer(), proximate_reward);

		
		System.out.println("Ultimate Reward");
		
		System.out.println(ultimate_reward);
		System.out.println("Proximate Reward");
		
		System.out.println(proximate_reward);

		
		
		return action_reward;
		
		
	}
	
	public PairIntegerDouble get_argmax_q(int location) {
		int argmax_index = 0;
		double max_q_val = -Double.MAX_VALUE;
		for(int i =0; i < this.num_actions; i++) {
			if(q_table[location][i] > max_q_val ) {
				argmax_index = i;
				max_q_val = q_table[location][i];	
			}
		}
		
		PairIntegerDouble max_index_max_q =  new PairIntegerDouble(argmax_index, max_q_val);
		
		return max_index_max_q;
		
	}
	
	
	//// This method implements Q learning
	public void update(int[] state, PairIntegerDouble action_reward, int[] next_state) {
		int current_location = state[0];
		double reward = action_reward.get_my_double(); 
		int next_location = next_state[0];
		
		
		double old_q_val = this.q_table[current_location][action_reward.get_my_integer()];
		PairIntegerDouble max_index_max_q = this.get_argmax_q(next_location);

		double max_next_q_val = max_index_max_q.get_my_double();
		

		
		
		/// Q learning update
		this.q_table[current_location][action_reward.get_my_integer()] += this.creature_learning_rate*(reward + this.discount*max_next_q_val - old_q_val);

		
//		
//		System.out.println("current location");
//		System.out.println(current_location);
//
//		
//		System.out.println("next location");
//		System.out.println(next_location);
//		
//		
//		System.out.println("Old q val");
//		System.out.println(old_q_val);
//		
//		System.out.println("Max next q val");
//		System.out.println(max_next_q_val);
//		
//		
//		
//		System.out.println("New q val");
//		System.out.println(this.q_table[current_location][action_reward.get_my_integer()]);
//		
//		System.out.println("Proximate Reward vector");
//		System.out.println(Arrays.toString(this.proximate_reward_matrix));
		
		
		
		
		//// update the opt_actions matrix
		for(int i=0; i < this.num_states; i ++) {
			PairIntegerDouble max_index_max_q_helper = this.get_argmax_q(i);
			this.opt_actions[i] = max_index_max_q_helper.get_my_integer();	
			
		}
		
	}
	
	
	/// Sets the proximate reward.
	public void process_evolver_advice(double[] evolver_advice) {
		System.out.println("alskdfmasdlfkamsdflkamsdflkamsdflkm");
		
		this.proximate_reward_matrix = evolver_advice;
				
	}

	
	public double[] get_creature_info() {
		
		return this.opt_actions;
		
		
	}
	

	
	public void reset() {
		
		this.q_table = new double[this.num_states][this.num_actions];
		for(double[] row : this.q_table) {
			Arrays.fill(row, 1);
		}
		
		
		this.proximate_reward_matrix = new double[this.num_states];
		
		this.opt_actions = new double[this.num_states];
		Arrays.fill(opt_actions, -2);
		
		this.ultimate_reward_collected = 0;
		
		
	}
	
	
	
	
	public static void main(String[] args) {

		
		ExperimentTypeTabularMDP experiment_type = ExperimentTypeTabularMDP.GRID;
		
		
		int num_steps = 100000;
		int chain_length = 3;
		int grid_length = 100;
		int grid_height = 100;
		
		int day_steps = 1000;
		double move_probability = 0.1;
		
		double discount = 0.9;
		double creature_learning_rate = .01;

		WorldTabular world = null;
		int num_states = 0;
		int num_actions = 0;
		
		
		if(experiment_type == ExperimentTypeTabularMDP.CHAIN) {
			 world = new WorldChainMDP(chain_length, move_probability);
			
			num_actions = 3;
			num_states = chain_length;
		
			
			
		}
		
		if(experiment_type == ExperimentTypeTabularMDP.GRID) {
			 world = new WorldGridMDP(grid_length, grid_height, move_probability);
			
			num_actions = 5;
			num_states = grid_length*grid_height;
			
			
		}

		CreatureTabularQ creature = new CreatureTabularQ(num_states, num_actions, discount, creature_learning_rate );

		/// True Reward Vector
		
		
		double[] true_reward_vector = world.get_reward_vector();
		
		
		
		
		//double[] chain_reward = new double[chain_length];
		//chain_reward[chain_length-1] = 1;
		
		
		creature.process_evolver_advice(true_reward_vector);
		
		//System.out.println(Arrays.toString(true_reward_vector));
		
		
		
		for(int i =0; i < num_steps; i ++) {
			
			if(i%day_steps == 0) {
				world.reset_world();
			}
			
			int[] state = world.get_state();
			PairIntegerDouble action_reward = creature.step(state);
			world.step(action_reward.get_my_integer());
			int[] next_state = world.get_state();
			creature.update(state, action_reward, next_state);
			
			System.out.println("Reward");
			System.out.println(action_reward.get_my_double());
			System.out.println("Current State");
			System.out.println(Arrays.toString(state));
			
				
		}
		
		//System.out.println()
		System.out.println(Arrays.toString(creature.get_creature_info() ));

		System.out.println(Arrays.deepToString(creature.q_table ));

		
		
		System.out.println("Ultimate reward sum");
		
		System.out.println(creature.get_ultimate_reward_collected()*1.0/num_steps);
		
		
	}
	
	
	
	
	
	
}
