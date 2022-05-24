package tpdia_project;

import java.io.File;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToNominal;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.J48;

public class MainProgram {

	public static final String TRAINING_DATA_SET_FILENAME="datasets/train.csv";
	public static final String TESTING_DATA_SET_FILENAME="datasets/test.csv";
	
	/**
	 * This method is to load the data set.
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static Instances getDataSet(String fileName) throws IOException {
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		int classIdx = 5;
		CSVLoader loader = new CSVLoader();
		loader.setFile(new File(fileName));
		Instances dataSet = loader.getDataSet();
		dataSet.setClassIndex(classIdx);
		//System.out.print(dataSet.toString());
		return dataSet;
	}
	
	/**
	 * This method is to load the data set.
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static void process() throws Exception {

		Instances trainingDataSet = getDataSet(TRAINING_DATA_SET_FILENAME);
		Instances testingDataSet = getDataSet(TESTING_DATA_SET_FILENAME);
		
		StringToNominal filter = new StringToNominal();
		filter.setAttributeRange("1-10");
		filter.setInputFormat(trainingDataSet);
		//filter.setInputFormat(testingDataSet);
		
		trainingDataSet = Filter.useFilter(trainingDataSet, filter);
		testingDataSet = Filter.useFilter(testingDataSet, filter);
		
		RandomForest forest = new RandomForest();
		forest.setNumFeatures(100);
		
		forest.buildClassifier(trainingDataSet);

		Evaluation eval = new Evaluation(trainingDataSet);
		eval.evaluateModel(forest, testingDataSet);
		
		System.out.println("** Decision Tress Evaluation with Datasets **");
		System.out.println(eval.toSummaryString());
		System.out.print(" the expression for the input data as per alogorithm is ");
		System.out.println(forest);
		
		double sum = testingDataSet.numInstances();//Examples of test corpus  
		double right = 0.0f;  
		
		 for(int  i = 0;i<sum;i++)//Test classification result 1
	     {  
	            if(forest.classifyInstance(testingDataSet.instance(i))==testingDataSet.instance(i).classValue())//If the predictive value is equal to the answer value (the correct answer must be provided by the categorized column in the test corpus, then the result will be meaningful)  
	            {  
	                right++;//Correct value plus 1  
	            }  
	      } 
		 System.out.println("RandomForest classification precision:"+(right/sum));  
	}
	
	public static void main(String[] args) {
		 try {
			process();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
