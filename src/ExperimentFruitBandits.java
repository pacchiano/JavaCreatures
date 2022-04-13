//package smells;


enum ExperimentFruitBanditsType{
	PROXIMATE_REWARD, 
	PRIOR_POLICY,
}





public class ExperimentFruitBandits extends Experiment{

	int num_fruit_types;
	double[][] fruit_type_probabilities_matrix;
	double[][] poison_probabilities_matrix;
	// FruitBanditsWorld fruitworld;
	//Creature creature;
	double stay_sick_probability;
	//private Random RandomGen = new Random();

	//int creature_horizon;
	//ES evolver;
	//double es_std;
	//double creature_learning_rate;

	//int num_fruit_bandit_worlds;
	//int exp_identifier;
	
	//FruitBanditsWorldDistribution fruit_worlds_distribution;
	ExperimentFruitBanditsType experiment_type;
	
	public ExperimentFruitBandits(int num_fruit_bandit_worlds, int num_fruit_types, double[] worlds_probabilities,
			double[][] fruit_type_probabilities_matrix, double[][] poison_probabilities_matrix,
			double stay_sick_probability, double es_std, int creature_horizon, double creature_learning_rate, 
			ExperimentEvolverAdvicePostprocess advice_process_type, ExperimentFruitBanditsType experiment_type, int exp_identifier) {


		
		super(num_fruit_bandit_worlds, num_fruit_types, num_fruit_types, worlds_probabilities, es_std, creature_horizon,  creature_learning_rate, 1,
				 advice_process_type, exp_identifier);

		this.experiment_type = experiment_type;
		this.num_fruit_types = num_fruit_types;
		this.fruit_type_probabilities_matrix = fruit_type_probabilities_matrix;
		this.poison_probabilities_matrix = poison_probabilities_matrix;

		
		
		
		
		try {

			this.worlds_distribution = new WorldDistributionFruitBandits(
					num_fruit_bandit_worlds, worlds_probabilities, num_fruit_types, fruit_type_probabilities_matrix,
					poison_probabilities_matrix);

			// this.fruitworld = new FruitBanditsWorld(num_fruit_types,
			// fruit_type_probabilities, poison_probabilities);

			if(experiment_type == ExperimentFruitBanditsType.PRIOR_POLICY) {
			
				this.creature = new CreatureFruitBanditsLogitsAdvice(num_fruit_types, stay_sick_probability, creature_learning_rate);
			}
			
			if(experiment_type == ExperimentFruitBanditsType.PROXIMATE_REWARD) {
				
				this.creature = new CreatureFruitBanditsProximateAdvice(num_fruit_types, stay_sick_probability, creature_learning_rate);
			}
				
			
			
			//this.es_std = es_std;
			//this.creature_learning_rate = creature_learning_rate;
			// double[] dimensions = {num_fruit_types, 2};
			//this.evolver = new ES(num_fruit_types, es_std);
			//this.creature_horizon = creature_horizon;
			//this.exp_identifier = exp_identifier;

		} catch (Exception e) {
			
			System.out.println("An excpetion occurred in the experiment creation.");
			e.printStackTrace();
		}

	}

	
	
	
	
}
