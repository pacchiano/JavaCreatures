

import java.util.Arrays;

public class CreatureFruitBanditsLogitsAdvice extends CreatureFruitBandits{
	
	public CreatureFruitBanditsLogitsAdvice(int num_fruit_types, double stay_sick_probability, double creature_learning_rate ) {
		super( num_fruit_types,  stay_sick_probability,  creature_learning_rate)	;

		
		
		
	}
	
	
	
	public void set_logits(double[] logits) {
		this.logits = logits;
		this.update_probability_weights();
	}
	
	/// The probability weights correspond to the probability of eating the fruit.
		
	

	
	public void process_evolver_advice(double[] evolver_advice) {
		this.set_logits(evolver_advice);
		
	}

	
	
	public double[] get_creature_info() {
		
		return this.get_probability_weights();

	}
	
	
	public PairIntegerDouble step(int[] fruit_type_poison_info ) {
		
		//System.out.println("Creature probability weights");
		//System.out.println(Arrays.toString(this.probability_weights));
		double instantaneous_reward = 0;
		int output_action = -2;

		// Determine if to stay sick or not.
		if(this.sick) {
			if( ProbabilityUtils.sample_bernoulli(this.stay_sick_probability) == 0) {
				this.sick = false;
				
			}
			output_action = -1;

			
		}
		
		
		/// If the creature is not sick
		if(!this.sick) {
			int action = this.action(fruit_type_poison_info[0]);
			
			if(action == 1) {
		
				instantaneous_reward = 1 - 2*fruit_type_poison_info[1] ;
				
//				this.sick = fruit_type_poison_info[1] == 1;
//				//// Only update when the creature is taking a decision while not sick.
//				this.update_logits(instantaneous_reward, fruit_type_poison_info[0]);
			
				// output_action = action;

				
				
			}
			
			
				 output_action = action;

			

			
		}
				

		this.ultimate_reward_collected += instantaneous_reward;
		PairIntegerDouble action_reward = new PairIntegerDouble(output_action, instantaneous_reward);
		return action_reward;
		
	}
	
	
	
	
	public static void main(String[] args) {
		
    	int num_creature_steps = 100000;
    	int num_fruit_types = 3;
    	double[] fruit_type_probabilities = {.33,  .33, .34};
    	double[] poison_probabilities = {.9, .9, .1};
    	double stay_sick_probability = 0;
    	
    	double creature_learning_rate = 0.01;
    	
    	WorldFruitBandits fruitworld = new WorldFruitBandits( num_fruit_types, fruit_type_probabilities, poison_probabilities		) ;
    	CreatureFruitBanditsLogitsAdvice creature = new CreatureFruitBanditsLogitsAdvice(num_fruit_types, stay_sick_probability, creature_learning_rate);
    	
    	
    	double[] rewards_collected = new double[num_creature_steps];
    	
    	
    	creature.reset();
    	
    	
    	for(int i=0; i < num_creature_steps; i++) {
    		int[] fruit_type_poison_info = fruitworld.get_state();
    		//System.out.println("Fruitworld sample");
    		//System.out.println(Arrays.toString(fruit_type_poison_info));

    		creature.step(fruit_type_poison_info);
    		
    		
    		System.out.println("Creature Probabilities");
    		System.out.println(Arrays.toString(creature.get_probability_weights()));
    		
    	
    		//System.out.print("Is the creature sick ");
    		//System.out.println(creature.is_sick());
    		
    		//System.out.print("Creature reward ");
    		double cum_reward = creature.get_ultimate_reward_collected(); 
    	
    		System.out.println(cum_reward);
    		rewards_collected[i] = cum_reward;
    		
    		
    	}

    	
		
    	
    	
		
		
		
	}
	
}
