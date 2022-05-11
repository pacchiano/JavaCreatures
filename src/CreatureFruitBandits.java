
public abstract class CreatureFruitBandits extends Creature{
	protected int num_fruit_types;
	protected double[] probability_weights;
	protected double[] logits;
	protected double[] proximate_rewards;
	protected double stay_sick_probability; 
	protected boolean sick = false;

	protected double creature_learning_rate;
	
	
	
	
	
	public CreatureFruitBandits(int num_fruit_types, double stay_sick_probability, double creature_learning_rate ) {
		
		super(2);
		
		this.num_fruit_types = num_fruit_types;
		this.logits = new double[num_fruit_types];
		this.proximate_rewards  = new double[num_fruit_types];
		this.probability_weights = new double[num_fruit_types];			
			this.update_probability_weights();
			this.stay_sick_probability = stay_sick_probability;
			this.creature_learning_rate = creature_learning_rate;
	}
	
	
	
	
	/// The probability weights correspond to the probability of eating the fruit.
	public double[] get_probability_weights(){
		return this.probability_weights;
	}
	
	public boolean is_sick() {
		return this.sick;
	}
	
	
		
	public void update(double[] fruit_type_poison_info, PairIntegerDouble action_reward, double[] dummy_fruit_type_poison_info) {
		
		
		/// If the action is EAT update logits.
		if(action_reward.get_my_integer() == 1) {
			this.sick = (int)fruit_type_poison_info[1] == 1;
			
			//// Only update when the creature is taking a decision while not sick.
			this.update_logits(action_reward.get_my_double(), (int)fruit_type_poison_info[0]);
		}
			
					
	}

	public void update_probability_weights() {
		this.probability_weights = ProbabilityUtils.logits_to_probabilities(this.logits);
	
	}
	
	
	public void reset() {
		this.probability_weights = new double[this.num_fruit_types];
		this.logits = new double[this.num_fruit_types];
		this.ultimate_reward_collected = 0;
		this.last_reward_collected = 0;
		
		this.sick = false;
		this.update_probability_weights();
		
	}

	
	
	
	
	/// Action. Index 0 indicates do not eat. Index 1 indicates eat.
	public int action(int fruit_type) {
		int action = ProbabilityUtils.sample_bernoulli(this.probability_weights[fruit_type]);
		return action;
	}
	
	
	public void update_logits(double instantaneous_reward, int action_index) {
		//double change_logit_value = this.creature_learning_rate*instantaneous_reward*1/(1+Math.exp(this.logits[action_index]));
		this.logits[action_index] += this.creature_learning_rate*instantaneous_reward*1/(1+Math.exp(this.logits[action_index]));
	
		//System.out.println("logits update");
		//System.out.println(change_logit_value);
		//System.out.println("Logits after change " + Arrays.toString(this.logits));
		this.update_probability_weights();
	
	}
	
	
	
	
	 
	
	
	
	
	
	
}
