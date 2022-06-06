package tpdia_project;

import java.nio.file.DirectoryStream.Filter;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Debug.Random;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.NumericToNominal;

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
		
		eval.crossValidateModel(ibk, trainingDataSet, 10, new Random(1));
		System.out.println("10-fold cross validation: " + Double.toString(eval.pctCorrect()));
	}

	public void DecisionTree(Instances trainingDataSet, Instances testingDataSet) throws Exception {

		NumericToNominal numericToNominal = new NumericToNominal();

		String[] options = new String[2];
		options[0] = "-R";
		options[1] = "first-last";

		numericToNominal.setInputFormat(trainingDataSet);
		numericToNominal.setOptions(options);
		testingDataSet = weka.filters.Filter.useFilter(trainingDataSet, numericToNominal);

		numericToNominal = new NumericToNominal();
		numericToNominal.setInputFormat(testingDataSet);
		numericToNominal.setOptions(options);
		trainingDataSet = weka.filters.Filter.useFilter(testingDataSet, numericToNominal);

		Classifier j48 = new J48();
		j48.buildClassifier(trainingDataSet);

		Evaluation eval = new Evaluation(trainingDataSet);
		eval.evaluateModel(j48, testingDataSet);

		System.out.println("** Decision Tree Evaluation with Datasets **");
		System.out.println(eval.toSummaryString());
		System.out.print(" the expression for the input data as per alogorithm is ");
		System.out.println(j48);

		ClassificationPrecision("Decision Tree", j48, testingDataSet);
		
		eval.crossValidateModel(j48, trainingDataSet, 10, new Random(1));
		System.out.println("10-fold cross validation: " + Double.toString(eval.pctCorrect()));
	}

	private void ClassificationPrecision(String algoName, Classifier classifier, Instances testingDataSet)
			throws Exception {

		double sum = testingDataSet.numInstances();
		double right = 0.0f;

		int Nmm = 0;
		int Nmn = 0;
		int Nnm = 0;
		int Nnn = 0; // not used in article

		for (int i = 0; i < sum; i++) {

			boolean predictedAsMeasure = false;
			if (classifier.classifyInstance(testingDataSet.instance(i)) == testingDataSet.instance(i).classValue()) {
				right++;
				predictedAsMeasure = true;
			}

			if (testingDataSet.instance(i).classValue() == 1.0) {
				if (predictedAsMeasure == true) {
					Nmm++;
				} else {
					Nmn++;
				}
			} else {
				if (predictedAsMeasure == false) {
					Nnm++;
				} else {
					Nnn++;
				}
			}
		}

		System.out.println("-------------------------------------");
		System.out.println(algoName + " classification precision: " + (right / sum));

		double R = 0;
		double P = 0;
		double F = 0;

		if ((Nmm + Nmn) != 0) {
			R = Nmm / (double) (Nmm + Nmn);
			R *= 100;
		}

		if ((Nmm + Nnm) != 0) {
			P = Nmm / (double) (Nmm + Nnm);
			P *= 100;
		}

		if ((P + R) != 0) {
			F = (2.0 * P * R) / (double) (P + R);
		}

		System.out.println("Recall: " + R + "%");
		System.out.println("Precision: " + P + "%");
		System.out.println("F-Measure: " + F + "%");
	}
}
