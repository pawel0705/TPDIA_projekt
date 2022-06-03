package tpdia_project;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

// TODO przed wywo³aniem algorytmów trzeba ustawiæ dataSet.setClassIndex(classIdx);, gdzie classIdx to nr kolumny któr¹ chcemy zgadywaæ
public class MLAlgorithms {

	public void KNN(Instances trainingDataSet, Instances testingDataSet) throws Exception {
		Classifier ibk = new IBk(1);		
		ibk.buildClassifier(trainingDataSet);
		
		Evaluation eval = new Evaluation(trainingDataSet);
        eval.evaluateModel(ibk, testingDataSet);
		
		System.out.println("** KNN Evaluation with Datasets **");
		System.out.println(eval.toSummaryString());
		System.out.print(" the expression for the input data as per alogorithm is ");
		System.out.println(ibk);
		
		ClassificationPrecision("k-Nearest Neighbor", ibk, testingDataSet);
	}
	
	public void DecisionTree(Instances trainingDataSet, Instances testingDataSet) throws Exception {
		Classifier j48 = new J48();
		j48.buildClassifier(trainingDataSet);
		
		Evaluation eval = new Evaluation(trainingDataSet);
        eval.evaluateModel(j48, testingDataSet);
		
		System.out.println("** Decision Tree Evaluation with Datasets **");
		System.out.println(eval.toSummaryString());
		System.out.print(" the expression for the input data as per alogorithm is ");
		System.out.println(j48);
		
		ClassificationPrecision("Decision Tree", j48, testingDataSet);
	}
	
	private void ClassificationPrecision(String algoName, Classifier classifier, Instances testingDataSet) throws Exception {

		double sum = testingDataSet.numInstances(); 
		double right = 0.0f;  
		
		 for(int i = 0; i<sum; i++)
	     {  
	            if(classifier.classifyInstance(testingDataSet.instance(i))==testingDataSet.instance(i).classValue())
	            {  
	                right++;
	            }  
	      } 
		 System.out.println(algoName + " classification precision:" + (right/sum));  
	}
}
