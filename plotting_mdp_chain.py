import matplotlib
matplotlib.use('Agg')

import matplotlib.pyplot as plt
from matplotlib import cm

import numpy as np
import IPython
import ast
import os
import zipfile




### Add a seed to the experiments.



from plotting_fruit_bandits import get_colors, reshape_averaging, reshape_averaging_probabilities_matrix, process_reward_results, process_probabilities_results
from plotting_fruit_bandits import load_files, generate_reward_plot_filename_and_title, generate_probabilities_plot_filename_and_title, plot_reward_results, plot_probabilities_results





def load_files_mdp_chain(num_fruit_types, experiment_name, creature_horizon, num_iterations):
	zip_filename = "./results/{}_MDPChain_".format(experiment_name) + str(num_fruit_types) + "_H" + str(creature_horizon) + "_T" + str(num_iterations) + ".zip"
	txt_filename = "./results/{}_MDPChain_".format(experiment_name) + str(num_fruit_types) + "_H" + str(creature_horizon) + "_T" + str(num_iterations) + ".txt"
	

	#IPython.embed()

	zip_file = zipfile.ZipFile(zip_filename, "r")
	zip_file.extractall("./results/")


	f = open(txt_filename, "r")
	results = dict([])


	lines = f.readlines()
	num_experiments = int(lines[0].replace("\n", ""))
	results["num_experiments"] = num_experiments

	num_iterations = int(lines[1].replace("\n", ""))
	results["num_iterations"] = num_iterations

	num_chain_worlds = int(lines[2].replace("\n", ""))
	results["num_chain_worlds"] = num_chain_worlds


	creature_horizon = int(lines[3].replace("\n", ""))
	results["creature_horizon"] = creature_horizon

	discount = float(lines[4].replace("\n", ""))
	results["discount"] = discount

	day_steps = int(lines[5].replace("\n", ""))
	results["day_steps"] = day_steps



	# fruit_type_probabilities_matrix = ast.literal_eval(lines[5].replace("\n", ""))
	# results["fruit_type_probabilities_matrix"] = fruit_type_probabilities_matrix

	# poison_probabilities_matrix = ast.literal_eval(lines[6].replace("\n", ""))
	# results["poison_probabilities_matrix"] = poison_probabilities_matrix

	# stay_sick_probability = float(lines[7].replace("\n", ""))
	# results["stay_sick_probability"] = stay_sick_probability

	es_std = float(lines[6].replace("\n", ""))
	results["es_std"] = es_std

	es_step_size = float(lines[7].replace("\n", ""))
	results["es_step_size"] = es_step_size

	creature_learning_rate = float(lines[8].replace("\n", ""))
	results["creature_learning_rate"] = creature_learning_rate


	reward_results = ast.literal_eval(lines[9].replace("\n", ""))
	results["reward_results"] = reward_results


	info_creature = ast.literal_eval(lines[10].replace("\n", ""))
	results["info_creature"] = info_creature


	info_evolver = ast.literal_eval(lines[11].replace("\n", ""))
	results["info_evolver"] = info_evolver

	world_indices_matrix = ast.literal_eval(lines[12].replace("\n", ""))
	results["world_indices_matrix"] = world_indices_matrix


	results["experiment_name"] = experiment_name


	#IPython.embed()

	os.remove(txt_filename)

	return results


if __name__ == "__main__":
	chain_length = 3
	num_iterations = 100000
	creature_horizon = 10000

	averaging_window = 1

	focus_fruit_world_index = 0

	for experiment_name in [ "ChainMDPBasic1" ]:
		results = load_files_mdp_chain(chain_length, experiment_name, creature_horizon, num_iterations)

		reward_plot_filename, reward_plot_title = generate_reward_plot_filename_and_title(results)
		
		#IPython.embed()

		results_matrix = results["reward_results"]
		
		if num_iterations != results["num_iterations"]:
			raise ValueError("num iterations do not agree in log file")

		plot_reward_results(reward_plot_filename, reward_plot_title, results_matrix, num_iterations, averaging_window = averaging_window)





		# probabilities_matrix_creature = results["used_probabilities_creature"]
		# poison_probabilities_matrix = results["poison_probabilities_matrix"]
		# fruit_world_indices_matrix = results["fruit_world_indices_matrix"]



		# probabilities_creature_plot_filename, probabilities_creature_plot_title = generate_probabilities_plot_filename_and_title(results, suffix = "creature-world{}".format(focus_fruit_world_index) )
		# plot_probabilities_results(probabilities_creature_plot_filename, probabilities_creature_plot_title, 
		# 	probabilities_matrix_creature, fruit_world_indices_matrix, num_iterations, num_fruit_types, poison_probabilities_matrix, 
		# 	averaging_window = averaging_window, focus_fruit_world_index = focus_fruit_world_index)


		# proximate_matrix_evolver = results["used_probabilities_evolver"]



		# proximate_evolver_plot_filename, proximate_evolver_plot_title = generate_proximate_plot_filename_and_title(results, suffix = "evolver" )
		# plot_probabilities_results(proximate_evolver_plot_filename, proximate_evolver_plot_title, 
		# 	proximate_matrix_evolver, fruit_world_indices_matrix, num_iterations, num_fruit_types, poison_probabilities_matrix, averaging_window = averaging_window)


