import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class ZipFile {
	private ZipFile() {}

	 
	 
    public static void zip_file(String source_path, String compressed_path) throws IOException {
        FileOutputStream fos = new FileOutputStream(compressed_path);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(source_path);
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        zipOut.close();
        fis.close();
        fos.close();
    }
}








class PairIntegerDouble {
	int my_integer;
	double my_double;
	
	
	PairIntegerDouble(int my_integer, double my_double){
		
		this.my_integer = my_integer;
		this.my_double = my_double;
		
	}
	
	
	public int get_my_integer() {
		return this.my_integer;
	}
	
	public double get_my_double() {
		return this.my_double; 
	}
}


class QuadraticFunc{
int dimension;
double[] bias;
double[] scalings;
	
	
	public QuadraticFunc(int dimension, double[] bias, double[] scalings) {
		this.dimension = dimension;
		this.bias = bias;
		this.scalings = scalings;
		
	}
	
	public double evaluate_point(double[] x) {
		double value = 0;
		for(int i =0; i < this.dimension; i++) {
			value += this.scalings[i]*Math.pow(x[i] - this.bias[i],2);	
		}
		return value;
	}
 	
	
	
}



class QuadraticFuncMatrix{
int[] dimensions;
double[][] bias;
double[][] scalings;
	
	
	public QuadraticFuncMatrix(int[] dimensions, double[][] bias, double[][] scalings) {
		this.dimensions = dimensions;
		this.bias = bias;
		this.scalings = scalings;
		
		
		
	}
	
	public double evaluate_point(double[][] x) {
		double value = 0;
		for(int i =0; i < this.dimensions[0]; i++) {
			for(int j =0; j < this.dimensions[1]; j++) {				
				value += this.scalings[i][j]*Math.pow(x[i][j] - this.bias[i][j],2);	
				
			}
		}
		return value;
	}
 	
	
	
}


class ExperimentResults {
	
	
	double[] ultimate_rewards;
	int[] world_indices;
	double[][] creature_info_list;
	double[][] evolver_info_list;

	
	
	public ExperimentResults(double[] ultimate_rewards, double[][] creature_info_list,
			double[][] evolver_info_list, int[] world_indices) {
		this.ultimate_rewards = ultimate_rewards;
		this.creature_info_list = creature_info_list;
		this.evolver_info_list = evolver_info_list;
		this.world_indices = world_indices;
	}

	public double[] get_rewards() {
		return this.ultimate_rewards;
	}

	public double[][] get_creature_info_list() {
		return this.creature_info_list;
	}

	public double[][] get_evolver_info_list() {
		return this.evolver_info_list;
	}

	public int[] get_world_indices() {
		return this.world_indices;
	}
	
}



class ExperimentManagerResults{
	
	double[][] rewards_matrix ;
	int[][] world_indices_matrix;
	double[][][] creature_info_tensor;
	double[][][] evolver_info_tensor ;

	
	public ExperimentManagerResults(double[][] rewards_matrix,  double[][][] creature_info_tensor, 
			double[][][] evolver_info_tensor, int[][] world_indices_matrix) {
		
		this.rewards_matrix  = rewards_matrix;
		this.creature_info_tensor = creature_info_tensor;
		this.evolver_info_tensor = evolver_info_tensor;
		this.world_indices_matrix = world_indices_matrix;
		
	}
	

	
	
}








public final class Utilities {
	
	public static double norm_difference(int[] a, int[] b) {
		
		int dimension = a.length;
		
		double sum_squares = 0;
		for(int i =0 ; i < dimension; i ++) {
			
			sum_squares +=  (a[i] - b[i])*(a[i] - b[i]) ;
			
		}
		
		return Math.sqrt(sum_squares);		
		
	}

	
	public static double dot_product(double[] a, double[] b) {
		double result = 0;
		for(int i = 0; i < a.length; i++) {
			
			result += a[i]*b[i];
			
		}
		
		return result;
		
	}
	
	
	
	
	public static double norm_difference(double[] a, double[] b) {
		
		int dimension = a.length;
		
		double sum_squares = 0;
		for(int i =0 ; i < dimension; i ++) {
			
			sum_squares +=  (a[i] - b[i])*(a[i] - b[i]) ;
			
			
		}
		
		return Math.sqrt(sum_squares);		
		
	}
	
	
	
	public static double[][] subsample_two_dim_array(double[][] input_array, int subsample_frequency, int dim0, int dim1, int subsample_index){
		
	
		
			int indicator_dim0 = subsample_index == 0 ? 1:0;
			int indicator_dim1 = subsample_index == 1 ? 1:0;
			
			int new_dim0 = ((int) dim0/subsample_frequency)*indicator_dim0 + dim0*(1-indicator_dim0);
			int new_dim1 = ((int) dim1/subsample_frequency)*indicator_dim1 + dim1*(1-indicator_dim1);
			
			
			double[][] result = new double[new_dim0][new_dim1];
			
			for(int i=0; i < new_dim0; i ++) {
				for(int j =0; j < new_dim1; j++) {
					int original_index0 = i*(1-indicator_dim0) + subsample_frequency*i*indicator_dim0;
					int original_index1 = j*(1-indicator_dim1) + subsample_frequency*j*indicator_dim1;
					 
					result[i][j] = input_array[original_index0][original_index1];
					
					
				}
				
				
				
				
				
			}
			
			
			return result;
		
		
	}

	
	public static int[][] subsample_two_dim_array(int[][] input_array, int subsample_frequency, int dim0, int dim1, int subsample_index){
		
		
		
		int indicator_dim0 = subsample_index == 0 ? 1:0;
		int indicator_dim1 = subsample_index == 1 ? 1:0;
		
		int new_dim0 = ((int) dim0/subsample_frequency)*indicator_dim0 + dim0*(1-indicator_dim0);
		int new_dim1 = ((int) dim1/subsample_frequency)*indicator_dim1 + dim1*(1-indicator_dim1);
		
		
		int[][] result = new int[new_dim0][new_dim1];
		
		for(int i=0; i < new_dim0; i ++) {
			for(int j =0; j < new_dim1; j++) {
				int original_index0 = i*(1-indicator_dim0) + subsample_frequency*i*indicator_dim0;
				int original_index1 = j*(1-indicator_dim1) + subsample_frequency*j*indicator_dim1;
				 
				result[i][j] = input_array[original_index0][original_index1];
				
				
			}
			
			
			
			
			
		}
		
		
		return result;
	
	
}


	
	
	
	
	
	
	public static double[][][] subsample_three_dim_array(double[][][] input_array, int subsample_frequency, int dim0, int dim1, int dim2, int subsample_index){
		
	
		
			int indicator_dim0 = subsample_index == 0 ? 1:0;
			int indicator_dim1 = subsample_index == 1 ? 1:0;
			int indicator_dim2 = subsample_index == 2 ? 1:0;
			
			int new_dim0 = ((int) dim0/subsample_frequency)*indicator_dim0 + dim0*(1-indicator_dim0);
			int new_dim1 = ((int) dim1/subsample_frequency)*indicator_dim1 + dim1*(1-indicator_dim1);
			int new_dim2 = ((int) dim2/subsample_frequency)*indicator_dim2 + dim2*(1-indicator_dim2);
			
			
			double[][][] result = new double[new_dim0][new_dim1][new_dim2];
			
			for(int i=0; i < new_dim0; i ++) {
				for(int j =0; j < new_dim1; j++) {
					for(int l =0; l < new_dim2; l ++) {
					int original_index0 = i*(1-indicator_dim0) + subsample_frequency*i*indicator_dim0;
					int original_index1 = j*(1-indicator_dim1) + subsample_frequency*j*indicator_dim1;
					int original_index2 = l*(1-indicator_dim2) + subsample_frequency*l*indicator_dim2;
					 
					result[i][j][l] = input_array[original_index0][original_index1][original_index2];
					}
					
				}
				
				
				
				
				
			}
			
			
			return result;
		
		
	}

	
	
	
	

}
