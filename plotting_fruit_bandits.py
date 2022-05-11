import matplotlib
matplotlib.use('Agg')

import matplotlib.pyplot as plt
from matplotlib import cm

import numpy as np
import IPython
import ast
import os
import zipfile

def get_colors(num_colors):
  #num_colors = len(model_parameter_configurations) + 1
  color_numbers = np.linspace(0,1, num_colors)
  color_map = cm.get_cmap('viridis', num_colors)
  colors  = [color_map(color_numbers[i]) for i in range(num_colors)]
  return colors


def reshape_averaging(input_list, averaging_window):
	return np.mean(input_list.reshape(-1, averaging_window), axis = 1)

def reshape_averaging_probabilities_matrix(input_matrix, averaging_window):
	return np.mean(input_matrix.reshape(-1, averaging_window, input_matrix.shape[1]), axis = 1)


def process_reward_results(results_matrix, averaging_window = 1):
	results_matrix_numpy = np.array(results_matrix)
	num_experiments, num_iterations = results_matrix_numpy.shape

	mean_rewards = np.mean(results_matrix_numpy, 0)
	std_rewards = np.std(results_matrix_numpy, 0)

	averaged_mean_rewards = reshape_averaging(mean_rewards, averaging_window)
	averaged_std_rewards = reshape_averaging(std_rewards, averaging_window)

	return averaged_mean_rewards, averaged_std_rewards


def process_probabilities_results(probabilities_matrix, averaging_window = 1):
	probabilities_matrix_numpy = np.array(probabilities_matrix)
	num_experiments, num_iterations, num_fruit_types = probabilities_matrix_numpy.shape

	
	mean_probabilities = np.mean(probabilities_matrix_numpy, 0)
	std_probabilities = np.std(probabilities_matrix_numpy, 0)

	averaged_mean_probabilities = reshape_averaging_probabilities_matrix(mean_probabilities, averaging_window)
	averaged_std_probabilities = reshape_averaging_probabilities_matrix(std_probabilities, averaging_window)

	return averaged_mean_probabilities, averaged_std_probabilities




def load_files(num_fruit_types, experiment_name, creature_horizon, num_iterations):
	zip_filename = "./results/{}_fruitBandits_".format(experiment_name) + str(num_fruit_types) + "_H" + str(creature_horizon) + "_T" + str(num_iterations) + ".zip"
	txt_filename = "./results/{}_fruitBandits_".format(experiment_name) + str(num_fruit_types) + "_H" + str(creature_horizon) + "_T" + str(num_iterations) + ".txt"
	zip_file = zipfile.ZipFile(zip_filename, "r")
	zip_file.extractall("./results/")

	try:
		tmp_zip_filename = "./results/{}_fruitBandits_".format(experiment_name) + str(num_fruit_types) + "_H" + str(creature_horizon) + "_T" + str(num_iterations) + "_tmp" + ".zip"
				
		tmp_zip_file = zipfile.ZipFile(tmp_zip_filename, "r")
		tmp_zip_file.extractall("./results/")



		os.remove(tmp_zip_filename)
	except:
		print("Zip results file was not double zipped")



	f = open(txt_filename, "r")
	results = dict([])


	lines = f.readlines()
	num_experiments = int(lines[0].replace("\n", ""))
	results["num_experiments"] = num_experiments

	num_iterations = int(lines[1].replace("\n", ""))
	results["num_iterations"] = num_iterations

	num_fruit_bandit_worlds = int(lines[2].replace("\n", ""))
	results["num_fruit_bandit_worlds"] = num_fruit_bandit_worlds


	creature_horizon = int(lines[3].replace("\n", ""))
	results["creature_horizon"] = creature_horizon

	num_fruit_types = int(lines[4].replace("\n", ""))
	results["num_fruit_types"] = num_fruit_types

	fruit_type_probabilities_matrix = ast.literal_eval(lines[5].replace("\n", ""))
	results["fruit_type_probabilities_matrix"] = fruit_type_probabilities_matrix

	poison_probabilities_matrix = ast.literal_eval(lines[6].replace("\n", ""))
	results["poison_probabilities_matrix"] = poison_probabilities_matrix

	stay_sick_probability = float(lines[7].replace("\n", ""))
	results["stay_sick_probability"] = stay_sick_probability

	es_std = float(lines[8].replace("\n", ""))
	results["es_std"] = es_std

	es_step_size = float(lines[9].replace("\n", ""))
	results["es_step_size"] = es_step_size

	creature_learning_rate = float(lines[10].replace("\n", ""))
	results["creature_learning_rate"] = creature_learning_rate


	reward_results = ast.literal_eval(lines[11].replace("\n", ""))
	results["reward_results"] = reward_results


	used_probabilities_creature = ast.literal_eval(lines[12].replace("\n", ""))
	results["used_probabilities_creature"] = used_probabilities_creature


	used_probabilities_evolver = ast.literal_eval(lines[13].replace("\n", ""))
	results["used_probabilities_evolver"] = used_probabilities_evolver

	fruit_world_indices_matrix = ast.literal_eval(lines[14].replace("\n", ""))
	results["fruit_world_indices_matrix"] = fruit_world_indices_matrix

	
	deadly_fruit_indices = ast.literal_eval(lines[15].replace("\n", ""))
	results["deadly_fruit_indices"] = deadly_fruit_indices

	results["experiment_name"] = experiment_name


	#IPython.embed()

	os.remove(txt_filename)

	return results

def generate_reward_plot_filename_and_title(results, suffix = ""):
	experiment_name = results["experiment_name"]
	num_experiments = results["num_experiments"]
	num_iterations = results["num_iterations"]
	creature_horizon = results["creature_horizon"]
	file_name = "{}_{}_reward_results_E{}_T{}_H{}.png".format(experiment_name, suffix, num_experiments, num_iterations, creature_horizon)
	title = "{} {} reward results E{} T{} H{}.png".format(experiment_name, suffix, num_experiments, num_iterations, creature_horizon)
	return file_name, title


def generate_probabilities_plot_filename_and_title(results,  suffix = "evolver"):
	experiment_name = results["experiment_name"]
	num_experiments = results["num_experiments"]
	num_iterations = results["num_iterations"]
	creature_horizon = results["creature_horizon"]
	file_name = "{}_{}_probabilities_results_E{}_T{}_H{}.png".format(experiment_name, suffix, num_experiments, num_iterations, creature_horizon)
	title = "{} {} probabilities results E{} T{} H{}".format(experiment_name, suffix, num_experiments, num_iterations, creature_horizon)
	return file_name, title


def plot_reward_results(plot_filename, plot_title, results_matrix, num_iterations, averaging_window = 1):
	

	mean_rewards, std_rewards = process_reward_results(results_matrix, averaging_window = averaging_window)
	timesteps = (np.arange(int(num_iterations/averaging_window) ) + 1)*averaging_window

	#IPython.embed()
	plt.plot(timesteps, mean_rewards, label = "Rewards" ,color = "red")
	plt.fill_between(timesteps, mean_rewards - .5*std_rewards, 
		mean_rewards + .5*std_rewards, color = "red", alpha = .2)

	plt.title(plot_title)

	plt.legend(loc = "lower right")
	plt.xscale('log')

	plt.savefig("./figs/{}".format(plot_filename))
	plt.close("all")



def plot_probabilities_results(plot_filename, plot_title, probabilities_matrix, fruit_world_indices_matrix, num_iterations, 
	num_fruit_types, poison_probabilities_matrix, averaging_window = 1, focus_fruit_world_index = None):

	### Compute the interpolation.
	probabilities_matrix_numpy = np.array(probabilities_matrix)
	fruit_world_indices_matrix_numpy = np.array(fruit_world_indices_matrix)
	interpolated_probabilities_matrix_numpy = np.zeros(probabilities_matrix_numpy.shape)

	(num_experiments, _, _) = probabilities_matrix_numpy.shape


	if focus_fruit_world_index == None:
		interpolated_probabilities_matrix_numpy = probabilities_matrix_numpy
	
	else:

		for i in range(num_experiments):
			fruit_world_indices_experiment_list = fruit_world_indices_matrix_numpy[i,:]
			mask = fruit_world_indices_experiment_list == focus_fruit_world_index
			indices_mask = np.arange(num_iterations)[mask]
			

			# #fruit_world_indices_experiment_list = fruit_world_indices_matrix[i]
			# focus_index_list = [index for (index, fruit_world_index) in zip(range(num_iterations), fruit_world_indices_experiment_list ) if fruit_world_index == focus_fruit_world_index]
			# IPython.embed()
			for fruit_type in range(num_fruit_types):
				focus_probs = probabilities_matrix_numpy[i, :, fruit_type][mask]

				interpolated_probabilities_matrix_numpy[i, :, fruit_type] = np.interp(np.arange(num_iterations),  indices_mask, focus_probs  )


	mean_probabilities, std_probabilities = process_probabilities_results(interpolated_probabilities_matrix_numpy, averaging_window = averaging_window)
	timesteps = (np.arange(int(num_iterations/averaging_window) ) + 1)*averaging_window
	
	#IPython.embed()
	colors = get_colors(num_fruit_types)
	#IPython.embed()

	for i in range(num_fruit_types):


		if focus_fruit_world_index == None:

			label_poison = ""	
			for j in range(len(poison_probabilities_matrix)):
				label_poison = "{} - {}".format(label_poison, poison_probabilities_matrix[j][i])		

		else:
			label_poison = " - {}".format(poison_probabilities_matrix[focus_fruit_world_index][i])

		plt.plot(timesteps, mean_probabilities[:,i], label = "Fruit {} - poison prob {}".format(i+1, label_poison) ,color = colors[i])
		plt.fill_between(timesteps, mean_probabilities[:,i] - .5*std_probabilities[:,i], 
			mean_probabilities[:,i] + .5*std_probabilities[:,i], color = colors[i], alpha = .2)
	

	#IPython.embed()

	#plt.legend(bbox_to_anchor=(1.05, 1), fontsize = 6)
	#plt.tight_layout()

	plt.legend(loc = "lower left", fontsize = 8)
	plt.title(plot_title, fontsize = 10)

	plt.xscale('log')

	plt.savefig("./figs/{}".format(plot_filename))
	plt.close("all")






if __name__ == "__main__":
	num_fruit_types = 3
	num_iterations = 100000
	creature_horizon = 1
	#creature_horizon = 1000

	averaging_window = 1

	focus_fruit_world_index = 0

	#for experiment_name in [ "Scatter-ESstepSizep1-AdaptiveH1000", "Scatter-ESstepSizep01-AdaptiveH1000", "Scatter-ESstepSizep001-AdaoptiveH1000"]:#["MultiWorldScenarioAdaptive1"]: #["MultiWorldScenarioReactive1", "MultiWorldScenarioAdaptive1", "Scenario1","Scenario2","Scenario3","Scenario4","Scenario5" ]:
	for experiment_name in ["Baldwin-ES-p1-Reactive"]:
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


		probabilities_matrix_evolver = results["used_probabilities_evolver"]

		probabilities_evolver_plot_filename, probabilities_evolver_plot_title = generate_probabilities_plot_filename_and_title(results, suffix = "evolver" )
		plot_probabilities_results(probabilities_evolver_plot_filename, probabilities_evolver_plot_title, 
			probabilities_matrix_evolver, fruit_world_indices_matrix, num_iterations, num_fruit_types, poison_probabilities_matrix, averaging_window = averaging_window)

		probabilities_difference_plot_filename, probabilities_difference_plot_title = generate_probabilities_plot_filename_and_title(results, suffix = "absolute-difference" )
		probabilities_matrix_difference = np.abs(np.array(probabilities_matrix_evolver) - np.array(probabilities_matrix_creature))
		plot_probabilities_results(probabilities_difference_plot_filename, probabilities_difference_plot_title, 
			probabilities_matrix_difference, fruit_world_indices_matrix,  num_iterations, num_fruit_types, poison_probabilities_matrix, averaging_window = averaging_window)


		probabilities_difference_plot_filename, probabilities_difference_plot_title = generate_probabilities_plot_filename_and_title(results, suffix = "signed-difference" )
		probabilities_matrix_difference = np.array(probabilities_matrix_evolver) - np.array(probabilities_matrix_creature)
		plot_probabilities_results(probabilities_difference_plot_filename, probabilities_difference_plot_title, 
			probabilities_matrix_difference, fruit_world_indices_matrix, num_iterations, num_fruit_types, poison_probabilities_matrix, averaging_window = averaging_window)






