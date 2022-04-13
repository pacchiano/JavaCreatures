import matplotlib
matplotlib.use('Agg')

import matplotlib.pyplot as plt
from matplotlib import cm

import numpy as np
import IPython
import ast
import os
import zipfile


from plotting_fruit_bandits import get_colors, reshape_averaging, reshape_averaging_probabilities_matrix, process_reward_results, process_probabilities_results
from plotting_fruit_bandits import load_files, generate_reward_plot_filename_and_title, generate_probabilities_plot_filename_and_title, plot_reward_results, plot_probabilities_results



def generate_proximate_plot_filename_and_title(results,  suffix = "evolver"):
	experiment_name = results["experiment_name"]
	num_experiments = results["num_experiments"]
	num_iterations = results["num_iterations"]
	creature_horizon = results["creature_horizon"]
	file_name = "{}_{}_proximate_results_E{}_T{}_H{}.png".format(experiment_name, suffix, num_experiments, num_iterations, creature_horizon)
	title = "{} {} proximate results E{} T{} H{}".format(experiment_name, suffix, num_experiments, num_iterations, creature_horizon)
	return file_name, title








if __name__ == "__main__":
	num_fruit_types = 3
	num_iterations = 10000
	creature_horizon = 100

	averaging_window = 1

	focus_fruit_world_index = 0

	#for experiment_name in [ "Scatter-ESstepSizep1-AdaptiveH1000", "Scatter-ESstepSizep01-AdaptiveH1000", "Scatter-ESstepSizep001-AdaoptiveH1000"]:#["MultiWorldScenarioAdaptive1"]: #["MultiWorldScenarioReactive1", "MultiWorldScenarioAdaptive1", "Scenario1","Scenario2","Scenario3","Scenario4","Scenario5" ]:
	for experiment_name in [ "ProximateBasic2" ]:
		results = load_files(num_fruit_types, experiment_name, creature_horizon, num_iterations)

		reward_plot_filename, reward_plot_title = generate_reward_plot_filename_and_title(results)
		

		results_matrix = results["reward_results"]
		
		if num_iterations != results["num_iterations"]:
			raise ValueError("num iterations do not agree in log file")

		plot_reward_results(reward_plot_filename, reward_plot_title, results_matrix, num_iterations, averaging_window = averaging_window)

		probabilities_matrix_creature = results["used_probabilities_creature"]
		poison_probabilities_matrix = results["poison_probabilities_matrix"]
		fruit_world_indices_matrix = results["fruit_world_indices_matrix"]



		probabilities_creature_plot_filename, probabilities_creature_plot_title = generate_probabilities_plot_filename_and_title(results, suffix = "creature-world{}".format(focus_fruit_world_index) )
		plot_probabilities_results(probabilities_creature_plot_filename, probabilities_creature_plot_title, 
			probabilities_matrix_creature, fruit_world_indices_matrix, num_iterations, num_fruit_types, poison_probabilities_matrix, 
			averaging_window = averaging_window, focus_fruit_world_index = focus_fruit_world_index)


		proximate_matrix_evolver = results["used_probabilities_evolver"]



		proximate_evolver_plot_filename, proximate_evolver_plot_title = generate_proximate_plot_filename_and_title(results, suffix = "evolver" )
		plot_probabilities_results(proximate_evolver_plot_filename, proximate_evolver_plot_title, 
			proximate_matrix_evolver, fruit_world_indices_matrix, num_iterations, num_fruit_types, poison_probabilities_matrix, averaging_window = averaging_window)





