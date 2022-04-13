import matplotlib
matplotlib.use('Agg')

import matplotlib.pyplot as plt
from matplotlib import cm

import numpy as np
import IPython
import ast
import os
import zipfile

from plotting_fruit_bandits import load_files, get_colors, process_reward_results




def plot_cummulative_rewards(results_list, plot_title, plot_filename, averaging_window):
	colors = get_colors(len(results_list))
	num_iterations = results_list[0]["num_iterations"]
	timesteps = np.arange(num_iterations) + 1
	plt.title(plot_title)
	#plt.xscale('log')


	for results,i in zip(results_list, range(len(results_list))):
		
		cummulative_rewards_matrix = np.array(results["reward_results"]).cumsum(1)
		#IPython.embed()
		mean_rewards, std_rewards = process_reward_results(cummulative_rewards_matrix, averaging_window = averaging_window) 
		plt.plot(timesteps, mean_rewards, label = results["experiment_name"] , color = colors[i]  )
		plt.fill_between(timesteps, mean_rewards - .5*std_rewards, mean_rewards + .5*std_rewards, alpha = .2, color =  colors[i])


	plt.legend(loc = "upper left")
	plt.savefig("./figs/{}".format(plot_filename))
	plt.close("all")

def plot_cummulative_argmax_rewards_scatter(results_list, plot_title, plot_filename, averaging_window):
	result_rewards, result_argmax_indices = get_pointwise_max_cummulative_reward_list_helper(results_list, averaging_window)

	result_rewards = np.maximum.accumulate(result_rewards)

	colors = get_colors(len(results_list))
	num_iterations = results_list[0]["num_iterations"]
	plt.title(plot_title)
	#plt.xscale('log')

	filtered_results = [[] for _ in range(len(results_list))]
	for i in range(num_iterations):
		filtered_results[result_argmax_indices[i]].append((i+1, result_rewards[i]))

	names = [results_list[i]["experiment_name"] for i in range(len(results_list))]

	#IPython.embed()

	for filtered_result, i in zip(filtered_results, range(len(results_list))):
			if len(filtered_result) > 0:
				timesteps, values = zip(*filtered_result)
				plt.scatter( timesteps, values, s = [3]*len(timesteps), 
					c = [colors[i]]*len(timesteps), label = names[i])

	plt.xlabel("Evolver Timesteps")
	plt.ylabel("Max Reward - Evolver")
	

	plt.legend(loc = "upper left")
	plt.savefig("./figs/{}".format(plot_filename))
	plt.close("all")

def get_pointwise_max_cummulative_reward_list_helper(results_list, averaging_window = 1):
	cummulative_mean_rewards_list = []
	mean_rewards_list = []

	for results,i in zip(results_list, range(len(results_list))):
		cummulative_rewards_matrix = np.array(results["reward_results"]).cumsum(1)		
		mean_cummulative_rewards, _ = process_reward_results(cummulative_rewards_matrix, averaging_window = averaging_window) 
		cummulative_mean_rewards_list.append(mean_cummulative_rewards)
		mean_rewards, _ = process_reward_results(np.array(results["reward_results"]), averaging_window = averaging_window) 
		mean_rewards_list.append(mean_rewards)


	mean_rewards_matrix = np.array(mean_rewards_list)
	result_rewards = []

	result_argmax_indices = []
	for i in range(results_list[0]["num_iterations"]):
		max_cumm_rewards_index  = np.argmax([l[i] for l in cummulative_mean_rewards_list])	
		result_rewards.append(mean_rewards_matrix[max_cumm_rewards_index, i])
		result_argmax_indices.append(max_cumm_rewards_index)

	return result_rewards, result_argmax_indices



def get_pointwise_max_cummulative_reward_list(results_list, averaging_window = 1):
	result_rewards, _ = get_pointwise_max_cummulative_reward_list_helper(results_list, averaging_window)
	return result_rewards






def plot_scatter_stats(list_max_cummulative_rewards, cum_rew_names,plot_title, plot_filename, scatter= True):
	num_iterations = len(list_max_cummulative_rewards[0])
	all_diff_reward_lists = [np.maximum.accumulate(l - l[0]) for l in list_max_cummulative_rewards]


	colors = get_colors(len(list_max_cummulative_rewards))
	plt.title(plot_title)

	for i in range(len(colors)):
		if scatter:
			plt.scatter( all_diff_reward_lists[i], np.arange(num_iterations) + 1, s = [3]*num_iterations, 
				c = [colors[i]]*num_iterations, label = cum_rew_names[i])
		else:
			plt.plot( all_diff_reward_lists[i], np.arange(num_iterations) + 1,
				color = colors[i], label = cum_rew_names[i])

	plt.xlabel("Max Reward Increment - Evolver")
	plt.ylabel("Evolver Timesteps")

	plt.legend(loc = "lower right")
	plt.savefig("./figs/{}".format(plot_filename))
	plt.close("all")








if __name__ == "__main__":
	num_fruit_types = 3
	num_iterations = 10000
	creature_horizon = 1000

	averaging_window = 1

	focus_fruit_world_index = 0



	results_reactive = []

	for experiment_name in [ "Scatter-ESstepSizep1-Reactive", "Scatter-ESstepSizep01-Reactive", "Scatter-ESstepSizep001-Reactive"]:
		results_reactive.append(load_files(num_fruit_types, experiment_name, creature_horizon = 100, num_iterations = num_iterations))


	plot_title = "Reactive Learning"
	plot_filename = "Scatter_Cum_rewards_reactive_learning.png"
	plot_cummulative_rewards(results_reactive, plot_title, plot_filename, averaging_window)

	plot_title = "Reactive Learning ES Learning Rate Argmax"
	plot_filename = "Scatter_ES_learning_reactive_learning.png"


	plot_cummulative_argmax_rewards_scatter(results_reactive, plot_title, plot_filename, averaging_window)

	max_cum_rewards_reactive = get_pointwise_max_cummulative_reward_list(results_reactive, averaging_window = 1)




	#IPython.embed()


	results_adaptive100 = []

	for experiment_name in [ "Scatter-ESstepSizep1-AdaptiveH100", "Scatter-ESstepSizep01-AdaptiveH100", "Scatter-ESstepSizep001-AdaoptiveH100"]:
		results_adaptive100.append(load_files(num_fruit_types, experiment_name, creature_horizon = 100, num_iterations = num_iterations))


	plot_title = "Adaptive100 Learning"
	plot_filename = "Scatter_Cum_rewards_Adaptive100_learning.png"
	plot_cummulative_rewards(results_adaptive100, plot_title, plot_filename, averaging_window)



	plot_title = "Adaptive100 Learning ES Learning Rate Argmax"
	plot_filename = "Scatter_ES_learning_Adaptive100_learning.png"


	plot_cummulative_argmax_rewards_scatter(results_adaptive100, plot_title, plot_filename, averaging_window)


	max_cum_rewards_adaptive100 = get_pointwise_max_cummulative_reward_list(results_adaptive100, averaging_window = 1)




	results_adaptive1000 = []

	for experiment_name in [ "Scatter-ESstepSizep1-AdaptiveH1000", "Scatter-ESstepSizep01-AdaptiveH1000", "Scatter-ESstepSizep001-AdaoptiveH1000"]:
		results_adaptive1000.append(load_files(num_fruit_types, experiment_name, creature_horizon = 1000, num_iterations = num_iterations))

	plot_title = "Adaptive1000 Learning"
	plot_filename = "Scatter_Cum_rewards_Adaptive1000_learning.png"
	plot_cummulative_rewards(results_adaptive1000, plot_title, plot_filename, averaging_window)

	plot_title = "Adaptive1000 Learning ES Learning Rate Argmax"
	plot_filename = "Scatter_ES_learning_Adaptive1000_learning.png"


	plot_cummulative_argmax_rewards_scatter(results_adaptive1000, plot_title, plot_filename, averaging_window)



	max_cum_rewards_adaptive1000 = get_pointwise_max_cummulative_reward_list(results_adaptive1000, averaging_window = 1)




	all_cumm_reward_lists = [max_cum_rewards_reactive, max_cum_rewards_adaptive100, max_cum_rewards_adaptive1000]

	# IPython.embed()

	cum_rew_names = ["Reactive Learning", "Adaptive100 Learning", "Adaptive1000 Learning"]

	
	plot_title = "Scatter All Algos"
	plot_filename = "Scatter_all_learning.png"

	plot_scatter_stats(all_cumm_reward_lists, cum_rew_names, plot_title, plot_filename)

	plot_filename = "Scatter_all_learning_line.png"
	plot_scatter_stats(all_cumm_reward_lists, cum_rew_names, plot_title, plot_filename, scatter = False)

	### For each scenario type plot the 


	plot_title = "Scatter All Algos - T =1000"
	plot_filename = "Scatter_all_learning_T1000.png"

	plot_scatter_stats([l[:1000] for l in all_cumm_reward_lists], cum_rew_names, plot_title, plot_filename)
	plot_filename = "Scatter_all_learning_T1000_line.png"

	plot_scatter_stats([l[:1000] for l in all_cumm_reward_lists], cum_rew_names, plot_title, plot_filename, scatter = False)
	
	### For each scenario type plot the 







