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



def generate_baldwin_plot_filename_and_title(results,  suffix = "evolver"):
	experiment_name = results["experiment_name"]
	num_experiments = results["num_experiments"]
	num_iterations = results["num_iterations"]
	creature_horizon = results["creature_horizon"]
	file_name = "{}_{}_baldwin_results_E{}_T{}_H{}.png".format(experiment_name, suffix, num_experiments, num_iterations, creature_horizon)
	title = "{} {} baldwin results E{} T{} H{}".format(experiment_name, suffix, num_experiments, num_iterations, creature_horizon)
	return file_name, title





### all the input results matrices list need to have the same number of datapoints.
def aggregate_results_max_performance(results_matrices_list, averaging_window):
	mean_rewards_list = []
	std_rewards_list = []
	print("got here")
	for results_matrix in results_matrices_list:
		mean_rewards, std_rewards = process_reward_results(results_matrix, averaging_window = averaging_window)	
		print("processed reward matrix")
		mean_rewards_list.append(mean_rewards)
		std_rewards_list.append(std_rewards)
	#### Compute pointwise 
	pointwise_max_rewards = []
	pointwise_stds = []
	for i in range(len(mean_rewards_list[0])):
		pointwise_max_reward = max( [x[i] for x in mean_rewards_list ])
		pointwise_std = np.mean([x[i] for x in std_rewards_list ])
		pointwise_max_rewards.append(pointwise_max_reward)
		pointwise_stds.append(pointwise_std)

	return np.array(pointwise_max_rewards), np.array(pointwise_stds)


def plot_baldwin_rewards(plot_subtitle, plot_filename, list_experiment_bundles, list_creature_horizon, list_num_iterations, num_fruit_types, list_names = [], 
	averaging_window = 1 , evolver_time = False, plot_theme = "Baldwin"):
	if len(list_experiment_bundles) != len(list(list_creature_horizon)):
		raise ValueError("Size of experiment name bundle list is different from creature horizon list")
	if len(list_experiment_bundles) != len(list(list_num_iterations)):
		raise ValueError("Size of experiment name bundle list is different from list num iterations list")

	if len(list_names) == 0:
		list_names = ["" for _ in range(len(list_experiment_bundles))]

	colors = get_colors(len(list_experiment_bundles))#["red", "blue", "black", "orange", "purple", "green"]

	for experiment_bundle,creature_horizon, num_iterations, i in zip(list_experiment_bundles, list_creature_horizon, list_num_iterations, range(len(list_experiment_bundles))):
		results_matrices_list = []
		
		timesteps = np.arange(num_iterations) + 1
		if not evolver_time:
			timesteps = (np.arange(num_iterations)+1)*creature_horizon
		
		for experiment_name in experiment_bundle:
			print("preparing to load experiment results {}".format(experiment_name))
			results = load_files(num_fruit_types, experiment_name, creature_horizon, num_iterations)
			print("loaded files {} ".format(experiment_name))
			results_matrices_list.append(results["reward_results"])

		pointwise_max_reward, pointwise_std =  aggregate_results_max_performance(results_matrices_list, averaging_window)
		#IPython.embed()



		plt.plot(timesteps, pointwise_max_reward, label = "T{} H{}{}".format(num_iterations, creature_horizon, list_names[i]) ,color = colors[i])
		plt.fill_between(timesteps, pointwise_max_reward - .5*pointwise_std, 
		pointwise_max_reward + .5*pointwise_std, color = colors[i], alpha = .2)



	plt.legend(loc = "upper left", fontsize = 12)

	plt.title(plot_subtitle, fontsize = 12)
	plt.suptitle(plot_theme, fontsize = 15)
	if evolver_time:
		plt.xlabel(r"Evolver Timesteps", fontsize = 12)
	else:
		plt.xlabel(r"Evolver $\times$ Creature Timesteps", fontsize = 12)
	plt.ylabel("Ultimate Reward",  fontsize = 12)
	#plt.xscale('log')


	#plt.show()
	plt.savefig("./figs/{}".format(plot_filename))
	plt.close("all")



def plot_baldwin_probabilities(plot_subtitle, plot_filename, list_experiment_names, list_creature_horizon, list_num_iterations,  
	list_names, averaging_window = 1 , evolver_time = False, fruit_index = 0, fruit_label = "", plot_theme = "Baldwin"):#, plot_creature = False):
	if len(list_experiment_bundles) != len(list(list_creature_horizon)):
		raise ValueError("Size of experiment name bundle list is different from creature horizon list")
	if len(list_experiment_bundles) != len(list(list_num_iterations)):
		raise ValueError("Size of experiment name bundle list is different from list num iterations list")

	if len(list_names) == 0:
		list_names = ["" for _ in range(len(list_experiment_bundles))]

	colors = get_colors(len(list_experiment_names))


	for experiment_name, creature_horizon, num_iterations, i in zip(list_experiment_names, list_creature_horizon, list_num_iterations, range(len(list_experiment_names))):

		results = load_files(num_fruit_types, experiment_name, creature_horizon, num_iterations)
		
		probabilities_matrix_creature = np.array(results["used_probabilities_creature"])
		probabilities_matrix_evolver = np.array(results["used_probabilities_evolver"])

		
		


		timesteps = np.arange(num_iterations) +1
		if not evolver_time:
			timesteps = (np.arange(num_iterations)+1)*creature_horizon

		label = "T{} H{}{}".format(num_iterations, creature_horizon, list_names[i])

		#if plot_creature:
		
	
		#else:
		mean_probabilities_evolver, std_probabilities_evolver = process_probabilities_results(probabilities_matrix_evolver, 
			 averaging_window = averaging_window)
		plt.plot(timesteps, mean_probabilities_evolver[:,fruit_index], label = "{} Evolver".format(label), linestyle='dashed', linewidth = 3, color = colors[i])

		if list_names[i] != " reactive":
			mean_probabilities_creature, std_probabilities_creature = process_probabilities_results(probabilities_matrix_creature, averaging_window = averaging_window)

			plt.plot(timesteps, mean_probabilities_creature[:,fruit_index], label = "{} Creature".format(label),  linewidth = 3, color = colors[i])

			#plt.fill_between(timesteps, mean_probabilities_creature[:, fruit_index] ,  mean_probabilities_evolver[:, fruit_index] , color = colors[i], alpha = .1 )





	plt.legend(loc = "upper left", fontsize = 10)

	plt.title(plot_subtitle, fontsize = 12)
	plt.suptitle("{} Fruit {}{}".format(plot_theme, fruit_index+1, fruit_label), fontsize = 15)
	if evolver_time:
		plt.xlabel(r"Evolver Timesteps", fontsize = 12)
	else:
		plt.xlabel(r"Evolver $\times$ Creature Timesteps", fontsize = 12)
	plt.ylabel("Play Probability", fontsize = 12)

	#plt.xscale('log')


	#plt.show()
	plt.savefig("./figs/{}".format(plot_filename))
	plt.close("all")





if __name__ == "__main__":
	num_fruit_types = 3
	# num_iterations = 10000
	# creature_horizon = 100

	averaging_window = 1

	focus_fruit_world_index = 0


	plot_theme = "Catastrophic Events"

	##### Baldwin reward plots

	list_experiment_bundles = [["Baldwin-ES-p1-Reactive"], ["Baldwin-ES-p1-CLRp01-H100", "Baldwin-ES-p1-CLRp001-H100", "Baldwin-ES-p1-CLRp1-H100"], ["Baldwin-ES-p1-CLRp01-H1000", "Baldwin-ES-p1-CLRp001-H1000", "Baldwin-ES-p1-CLRp1-H1000"]]
	list_creature_horizon = [10, 100, 1000]
	list_num_iterations = [1000000, 100000, 10000]

	plot_subtitle = "Ultimate Reward Comparison Accross Different Creature Architectures"
	plot_filename = "baldwin-reward-evolverxcreature.png"
	list_names = [" reactive", "", ""]

	plot_baldwin_rewards(plot_subtitle, plot_filename, list_experiment_bundles,list_creature_horizon, list_num_iterations, num_fruit_types , 
		list_names = list_names, averaging_window = averaging_window, plot_theme = plot_theme)


	plot_subtitle = "Ultimate Reward Comparison Accross Different Creature Architectures"
	plot_filename = "baldwin-reward-evolver.png"

	plot_baldwin_rewards(plot_subtitle, plot_filename, list_experiment_bundles,list_creature_horizon, list_num_iterations, num_fruit_types , 
		list_names = list_names, averaging_window = averaging_window, evolver_time = True, plot_theme = plot_theme)





	##### Baldwin probability plots

	list_experiment_names = ["Baldwin-ES-p1-Reactive", "Baldwin-ES-p1-CLRp1-H100", "Baldwin-ES-p1-CLRp01-H1000"]


	for i in range(num_fruit_types):

		# plot_subtitle = "Probability Evolution End of Creature's Life - Fruit {}".format(i+1)
		plot_subtitle = "Probability Evolution - Fruit {}".format(i+1)

		plot_filename = "baldwin-probability-evolverxcreature_{}.png".format(i+1)
		list_names = [" reactive", "", ""]

		plot_baldwin_probabilities(plot_subtitle, plot_filename, list_experiment_names, list_creature_horizon, list_num_iterations, 
			list_names = list_names, averaging_window = 1 , evolver_time = False, fruit_index = i, fruit_label = "", plot_theme = plot_theme)

		plot_subtitle = "Probability Evolution Beginning of Creature's Life - Fruit {}".format(i+1)
		plot_filename = "baldwin-probability-evolver_{}.png".format(i+1)
		list_names = [" reactive", "", ""]

		plot_baldwin_probabilities(plot_subtitle, plot_filename, list_experiment_names, list_creature_horizon, list_num_iterations, 
			list_names = list_names, averaging_window = 1 , evolver_time = True, fruit_index = i, fruit_label = "", plot_theme = plot_theme)








