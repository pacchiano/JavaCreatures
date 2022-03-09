

import java.util.Arrays;

public class CreatureLogitsAdvice {
	private int num_fruit_types;
	private double[] probability_weights;
	private double[] logits;
	private double stay_sick_probability; 
	private boolean sick = false;
	private double reward_collected = 0;

	private double creature_learning_rate;
	
	
	public CreatureLogitsAdvice(int num_fruit_types, double stay_sick_probability, double creature_learning_rate ) {
			this.num_fruit_types = num_fruit_types;
			this.logits = new double[num_fruit_types];
			this.probability_weights = new double[num_fruit_types];			
			this.update_probability_weights();
			this.stay_sick_probability = stay_sick_probability;
			this.creature_learning_rate = creature_learning_rate;
	}
	
	
	
	public void set_logits(double[] logits) {
		this.logits = logits;
		this.update_probability_weights();
	}
	
	/// The probability weights correspond to the probability of eating the fruit.
	public double[] get_probability_weights(){
		return this.probability_weights;
	}
	
	public boolean is_sick() {
		return this.sick;
	}
	
	public double get_reward_collected() {
		return this.reward_collected;
	}
	
//	public double[] logits_to_probabilities(double[] logits) {
//		double[] probability_weights_tmp = new double[this.num_fruit_types];
//		for(int i=0; i < this.num_fruit_types; i++) {
//			probability_weights_tmp[i] = Math.exp(logits[i])/(1+  Math.exp(logits[i]));
//			
//		}
//		
//		return probability_weights_tmp;
//		
//	}
	
	
	
	public void update_probability_weights() {
		this.probability_weights = ProbabilityUtils.logits_to_probabilities(this.logits);
	
	}
	
	
	public void reset() {
		this.probability_weights = new double[this.num_fruit_types];
		this.logits = new double[this.num_fruit_types];
		this.reward_collected = 0;
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
	
	
	public void step(int[] fruit_type_poison_info ) {
		
		//System.out.println("Creature probability weights");
		//System.out.println(Arrays.toString(this.probability_weights));
		double instantaneous_reward = 0;
		
		// Determine if to stay sick or not.
		if(this.sick) {
			if( ProbabilityUtils.sample_bernoulli(this.stay_sick_probability) == 0) {
				this.sick = false;
				
			}

			
		}
		
		
		/// If the creature is still sick
		if(!this.sick) {
			int action = this.action(fruit_type_poison_info[0]);
			//System.out.print("Creature took action ");
			//System.out.println(action);
			
			if(action == 1) {
		
				//System.out.println("Updated probabilities!!");
				instantaneous_reward = 1 - 2*fruit_type_poison_info[1] ;
				this.sick = fruit_type_poison_info[1] == 1;
				
				//// Only update when the creature is taking a decision while not sick.
				this.update_logits(instantaneous_reward, fruit_type_poison_info[0]);
				
				
			}
			
			//System.out.println("Creature was not sick");
			
		}
				

		this.reward_collected += instantaneous_reward;

	}
	
	
	public static void main(String[] args) {
		
    	int num_creature_steps = 100000;
    	int num_fruit_types = 3;
    	double[] fruit_type_probabilities = {.33,  .33, .34};
    	double[] poison_probabilities = {.9, .9, .1};
    	double stay_sick_probability = 0;
    	
    	double creature_learning_rate = 0.01;
    	
    	FruitBanditsWorld fruitworld = new FruitBanditsWorld( num_fruit_types, fruit_type_probabilities, poison_probabilities		) ;
    	CreatureLogitsAdvice creature = new CreatureLogitsAdvice(num_fruit_types, stay_sick_probability, creature_learning_rate);
    	
    	
    	double[] rewards_collected = new double[num_creature_steps];
    	
    	
    	creature.reset();
    	
    	
    	for(int i=0; i < num_creature_steps; i++) {
    		int[] fruit_type_poison_info = fruitworld.get_fruit_type_poison_type();
    		//System.out.println("Fruitworld sample");
    		//System.out.println(Arrays.toString(fruit_type_poison_info));

    		creature.step(fruit_type_poison_info);
    		
    		
    		System.out.println("Creature Probabilities");
    		System.out.println(Arrays.toString(creature.get_probability_weights()));
    		
    	
    		//System.out.print("Is the creature sick ");
    		//System.out.println(creature.is_sick());
    		
    		//System.out.print("Creature reward ");
    		double cum_reward = creature.get_reward_collected(); 
    	
    		System.out.println(cum_reward);
    		rewards_collected[i] = cum_reward;
    		
    		
    	}

    	
		
    	
    	
		
		
		
	}
	
}
