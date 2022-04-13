


public class ExperimentChainMDP extends Experiment {

	
	int chain_length;
	double move_probability;
	
	
	double discount;
	//double creature_learning_rate;
	
	
	
	
	public ExperimentChainMDP(int num_chain_worlds, int chain_length, double move_probability, double discount, double creature_learning_rate,
			double[] worlds_probabilities, double es_std, int creature_horizon,  int day_steps,
			ExperimentEvolverAdvicePostprocess advice_process_type, 
			int exp_identifier
			){
		
		
			
			super( num_chain_worlds,  chain_length,  chain_length, worlds_probabilities, 
				 es_std,  creature_horizon,  creature_learning_rate,  day_steps,  advice_process_type,
				 exp_identifier);
			
			
			this.chain_length = chain_length;
			this.move_probability = move_probability;
			this.discount = discount;
			this.creature_learning_rate = creature_learning_rate;
			
			
			
			
			try {
	
				this.worlds_distribution = new WorldDistributionChainMDP(
						num_chain_worlds, worlds_probabilities, chain_length, move_probability);
				
				
				this.creature = new CreatureTabularQ(chain_length, 3, discount, creature_learning_rate);
				
				
			
	
			} catch (Exception e) {
				
				System.out.println("An excpetion occurred in the experiment creation.");
				e.printStackTrace();
			}
		
		
		
		
	}
	
	
	
	
	
	
	
	
}
