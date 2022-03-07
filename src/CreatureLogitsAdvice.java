

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
	
	public double[] logits_to_probabilities(double[] logits) {
		double[] probability_weights = new double[this.num_fruit_types];
		for(int i=0; i < this.num_fruit_types; i++) {
			probability_weights[i] = Math.exp(logits[i])/(1+  Math.exp(logits[i]));
			
		}
		
		return probability_weights;
		
	}
	
	
	
	public void update_probability_weights() {
		this.probability_weights = this.logits_to_probabilities(this.logits);
	
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
		this.logits[action_index] += this.creature_learning_rate*instantaneous_reward*1/(1+Math.exp(this.logits[action_index]));
			
	}
	
	
	public void step(int[] fruit_type_poison_info ) {
		
		//System.out.println("Creature probability weights");
		//System.out.println(Arrays.toString(this.probability_weights));
		double instantaneous_reward = 0;
		
		if(!this.sick) {
			int action = this.action(fruit_type_poison_info[0]);
			//System.out.print("Creature took action ");
			//System.out.println(action);
			
			if(action == 1) {
		
								
				instantaneous_reward = 1 - 2*fruit_type_poison_info[1] ;
				this.sick = fruit_type_poison_info[1] == 1;
				
				//// Only update when the creature is taking a decision while not sick.
				this.update_logits(instantaneous_reward, action);
				
				
			}
			
			//System.out.println("Creature was not sick");
			
		}
		else { // If the creature is sick don't do anything.
			if( ProbabilityUtils.sample_bernoulli(this.stay_sick_probability) == 0) {
				this.sick = false;
				
			}
		}		

		this.reward_collected += instantaneous_reward;

	}
	
	
	public static void main(String[] args) {
		
    	int num_steps = 1000;
    	int num_fruit_types = 3;
    	double[] fruit_type_probabilities = {.1,  .2, .7};
    	double[] poison_probabilities = {.5, .4, .3};
    	double stay_sick_probability = .5;
    	
    	double creature_learning_rate = .01;
    	
    	FruitBanditsWorld fruitworld = new FruitBanditsWorld( num_fruit_types, fruit_type_probabilities, poison_probabilities		) ;
    	CreatureLogitsAdvice creature = new CreatureLogitsAdvice(num_fruit_types, stay_sick_probability, creature_learning_rate);
    	
    	for(int i=0; i < num_steps; i++) {
    		int[] fruit_type_poison_info = fruitworld.get_fruit_type_poison_type();
    		System.out.println("Fruitworld sample");
    		System.out.println(Arrays.toString(fruit_type_poison_info));

    		creature.step(fruit_type_poison_info);
    		System.out.println("Creature Probabilities");
    		System.out.println(Arrays.toString(creature.get_probability_weights()));
    		
    	
    		System.out.print("Is the creature sick ");
    		System.out.println(creature.is_sick());
    		
    		System.out.print("Creature reward ");
    		System.out.println(creature.get_reward_collected());
    		
    		
    		
    	}

		
		
		
		
	}
	
}
