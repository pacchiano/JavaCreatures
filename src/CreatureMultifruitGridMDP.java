
public class CreatureMultifruitGridMDP extends Creature{

	
	int num_food_types;
	double stay_sick_probability;
	
	double creature_learning_rate;
	double[] proximate_rewards;

	boolean sick = false;
	
	
	double[][] logits;
	
	int policy_state_dim;
	
	
	
	
	public CreatureMultifruitGridMDP(int num_food_types, double stay_sick_probability, double creature_learning_rate) {
	
		super(num_food_types + 3);
		
		
		this.creature_learning_rate = creature_learning_rate;
		this.stay_sick_probability = stay_sick_probability;
		
		
		this.num_food_types = num_food_types;
		
		
		this.policy_state_dim = this.num_food_types*2;
		
		
		
		this.reset();
	
	}
	
	
	public void update_logits(double instantaneous_reward, int action_index ) {
		
		///// Update logits.
		
		
	}
	
	
	
	//// Write the policy gradient update
	public void update(double[] state, PairIntegerDouble action_reward, double[] next_state) {
		
		/// If the action is EAT update logits.
		if(action_reward.get_my_integer() == 1) {
			this.sick = (int)state[1] == 1;
			
			//// Only update when the creature is taking a decision while not sick.
			this.update_logits(action_reward.get_my_double(), (int)state[1]);
		}

		
	}

	
	
	/// TODO WRITE THIS METHOD
	public void reset() {
		this.logits = new double[5][this.policy_state_dim];
		
		this.proximate_rewards = new double[2*num_food_types];
		this.ultimate_reward_collected = 0;

		
	}
	
	
	public void set_proximate_rewards(double[] proximate_rewards) {
		this.proximate_rewards  = proximate_rewards;
	}
	


	public void process_evolver_advice(double[] evolver_advice) {
		
		
		this.set_proximate_rewards(evolver_advice);

		
		
	}
	
	
	
	
	public double[] get_creature_info() {
		
		double[] result = {0,0};
		
		return result;
		
	}
	
	
	public double evaluate_proximate_reward(double[] proximate_reward_state) {
	
		
		return Utilities.dot_product( this.proximate_rewards, proximate_reward_state);
				
		
	}
	
	
	
	
	
	public int evaluate_policy(double[] policy_grad_state) {
		
		double[] local_logits = new double[5];
		
		for(int i =0; i < 5; i ++) {
			local_logits[i] = Utilities.dot_product( logits[i], policy_grad_state);
			
			
		}
		
		
		double[] probabilities  = ProbabilityUtils.logits_to_probabilities(local_logits);
		
		int action_index = ProbabilityUtils.sample_index(probabilities);
		
		
		return action_index;
	}
	
	
	
	
	public PairIntegerDouble step(double[] state ) {
		
		/// Build the policy grad state
		double[] policy_grad_state = new double[this.policy_state_dim];
		double[] proximate_reward_state = new double[this.policy_state_dim + 1];

		proximate_reward_state[0] = state[1];
		
		for(int i = 5;  i < this.policy_state_dim; i ++) {
			policy_grad_state[i-5] = state[i];
			
			proximate_reward_state[i-4] =  state[i];
		}
		
		
		
		
	
		int output_action = 0;
		
				
				
		//int current_location = (int) state[0];
		double ultimate_reward = state[1];
		//int is_poisonous = (int) state[2];
		
		

		
		
		
		if(this.sick) {
			if( ProbabilityUtils.sample_bernoulli(this.stay_sick_probability) == 0) {
				this.sick = false;
				
			}
			
		}
		
		if(!this.sick) {
			
			output_action = this.evaluate_policy(policy_grad_state); // Compute action;
			
		}

		
		
		this.ultimate_reward_collected += ultimate_reward;
		
		double proximate_reward = this.evaluate_proximate_reward(proximate_reward_state);
		PairIntegerDouble action_reward = new PairIntegerDouble(output_action, proximate_reward);


		
		
		return action_reward;
		
		
	}

	
	
	
	public static void main(String[] args) {
		
		
		
		
		
	}
	
	
	
}