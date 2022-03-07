//package smells;

import java.util.Arrays;
import java.util.Random;
import java.io.IOException;
import java.io.FileWriter; // Import the FileWriter class

class ExperimentResults {
	double[] rewards;
	double[][] used_probabilities_creature;
	double[][] used_probabilities_evolver;

	public ExperimentResults(double[] rewards, double[][] used_probabilities_creature,
			double[][] used_probabilities_evolver) {
		this.rewards = rewards;
		this.used_probabilities_creature = used_probabilities_creature;
		this.used_probabilities_evolver = used_probabilities_evolver;
	}

	public double[] get_rewards() {
		return this.rewards;
	}

	public double[][] get_used_probabilities_creature() {
		return this.used_probabilities_creature;
	}

	public double[][] get_used_probabilities_evolver() {
		return this.used_probabilities_evolver;
	}

}

public class Experiment {

	int num_fruit_types;
	double[][] fruit_type_probabilities_matrix;
	double[][] poison_probabilities_matrix;
	// FruitBanditsWorld fruitworld;
	CreatureLogitsAdvice creature;
	double stay_sick_probability;
	private Random RandomGen = new Random();

	int creature_horizon;
	ES evolver;
	double es_std;
	double creature_learning_rate;

	int num_fruit_bandit_worlds;

	FruitBanditsWorldDistribution fruit_worlds_distribution;

	public Experiment(int num_fruit_bandit_worlds, int num_fruit_types, double[] worlds_probabilities,
			double[][] fruit_type_probabilities_matrix, double[][] poison_probabilities_matrix,
			double stay_sick_probability, double es_std, int creature_horizon, double creature_learning_rate) {

		this.num_fruit_types = num_fruit_types;
		this.fruit_type_probabilities_matrix = fruit_type_probabilities_matrix;
		this.poison_probabilities_matrix = poison_probabilities_matrix;

		try {

			this.fruit_worlds_distribution = new FruitBanditsWorldDistribution(
					num_fruit_bandit_worlds, worlds_probabilities, num_fruit_types, fruit_type_probabilities_matrix,
					poison_probabilities_matrix);

			// this.fruitworld = new FruitBanditsWorld(num_fruit_types,
			// fruit_type_probabilities, poison_probabilities);

			this.creature = new CreatureLogitsAdvice(num_fruit_types, stay_sick_probability, creature_learning_rate);
			this.es_std = es_std;
			this.creature_learning_rate = creature_learning_rate;
			// double[] dimensions = {num_fruit_types, 2};
			this.evolver = new ES(num_fruit_types, es_std);
			this.creature_horizon = creature_horizon;

		} catch (Exception e) {
			
			System.out.println("An excpetion occurred in the experiment creation.");
			e.printStackTrace();
		}

	}

	private double evaluate_creature(double[] logits) {
		/// Send advice to creature
		this.creature.reset();
		this.creature.set_logits(logits);

		FruitBanditsWorld fruitworld = this.fruit_worlds_distribution.get_fruit_world();

		/// Run creature with this advice for a creature horizon time
		for (int j = 0; j < this.creature_horizon; j++) {
			int[] fruit_type_poison_info = fruitworld.get_fruit_type_poison_type();
			// System.out.println("Fruit type poison info");
			// System.out.println(Arrays.toString(fruit_type_poison_info));
			this.creature.step(fruit_type_poison_info);

		}

		return this.creature.get_reward_collected() / this.creature_horizon;
	}

		
	public ExperimentResults run_experiment(int num_iterations, double step_size) {

		double[] initial_logits_vector = new double[this.num_fruit_types];
		//// Initialize weights_vector

		double[] rewards = new double[num_iterations];
		double[][] used_probabilities_creature = new double[num_iterations][num_fruit_types];
		double[][] used_probabilities_evolver = new double[num_iterations][num_fruit_types];

		for (int i = 0; i < this.num_fruit_types; i++) {
			initial_logits_vector[i] = RandomGen.nextGaussian();
		}

		this.evolver.set_weights_vector(initial_logits_vector);

		for (int i = 0; i < num_iterations; i++) {

			//// Send the creature the logits

			double reward_base = evaluate_creature(this.evolver.get_weights_vector());

			double[] perturbation = this.evolver.get_sample_perturbation();
			double[] perturbed_logits_vector = this.evolver.get_perturbed_datapoint(this.evolver.get_weights_vector(),
					perturbation);

			double reward_perturbed = evaluate_creature(perturbed_logits_vector);
			this.evolver.update_statistics_double_sensing(perturbation, reward_perturbed, reward_base, step_size);
			if(Flags.verbose) {
			System.out.println("Creature probabilities");
			System.out.println(Arrays.toString(this.creature.get_probability_weights()));
			System.out.println("Sample Reward");
			System.out.println(reward_base);
			System.out.println("Weights Vector");
			System.out.println(Arrays.toString(this.evolver.get_weights_vector()));
			}
			rewards[i] = reward_base;
			used_probabilities_creature[i] = this.creature.get_probability_weights();
			used_probabilities_evolver[i] = this.creature.logits_to_probabilities(this.evolver.get_weights_vector());

		}

		ExperimentResults exp_results = new ExperimentResults(rewards, used_probabilities_creature,
				used_probabilities_evolver);

		return exp_results;
	}

}
