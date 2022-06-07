package tpdia_project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import tpdia_project.Models.DatasetInformationModel;
import tpdia_project.Models.StatisticValuesModel;
import tpdia_project.Models.ValueRatioModel;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSink;
import weka.filters.unsupervised.attribute.StringToNominal;

public class MainProgram {
	static CSVManager csvManager = new CSVManager();

	public static void main(String[] args) {

		boolean exitProgram = false;
		String pressedKey = "";

		do {
			PrintHelp();

			Scanner in = new Scanner(System.in);
			pressedKey = in.nextLine();

			if (pressedKey.equals("a") || pressedKey.equals("A")) {
				PrepareFeaturesCSV();
			}

			if (pressedKey.equals("s") || pressedKey.equals("S")) {
				try {
					DetectFeatures();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (pressedKey.equals("q") || pressedKey.equals("Q")) {
				exitProgram = true;
			}

			pressedKey = "";
		} while (exitProgram == false);

		return;
	}

	private static void PrintHelp() {
		System.out
				.println("Aby utworzy� plik z cechami tabel numerycznych kliknij klawisz \"a\" i potwierd� wyb�r.");
		System.out.println(
				"Aby dokona� procesu trenowania i walidacji na algorytmach uczenia maszynowego kliknij klawisz \"s\" i potwier� wyb�r.");
		System.out.println("Aby dokona� wyj�� z programu wci�nij klawisz \"q\" i potwierd� wyb�r.");
	}

	private static void DetectFeatures() throws Exception {
		Instances data = csvManager.GetDataSet("features.csv", ";");

		int seed = 2500000;

		data = csvManager.DeleteAllCoumnsExceptNumeric(data);

		Random rand = new Random(seed);
		Instances randData = new Instances(data);
		randData.randomize(rand);

		int trainSize = (int) Math.round(randData.numInstances() * 0.7);
		int testSize = randData.numInstances() - trainSize;
		Instances train = new Instances(randData, 0, trainSize);
		Instances test = new Instances(randData, trainSize, testSize);
		
		train.setClassIndex(0); // searching label on position 0 after removing all except numeric columns
		test.setClassIndex(0);
		
		System.out.println(train.toSummaryString());
		System.out.println(test.toSummaryString());
		
		MLAlgorithms algorithmManager = new MLAlgorithms();
		algorithmManager.DecisionTree(train, test);
		algorithmManager.KNN(train, test);
		algorithmManager.RandomForest(train, test);
		algorithmManager.SVM(train, test);
	}

	private static void PrepareFeaturesCSV() {
		Instances main = csvManager.GetDataSet("features.csv", ";");

		int failureInterator = 0;
		int filesOpenedIterator = 0;

		String prevFileName = "";

		for (Instance row : main) 
		{
			String domainModelName = row.stringValue(0);
			String fileName = row.stringValue(1);
			
			if (prevFileName.equals(fileName)) 
			{
				continue;
			}

			boolean result = ProcessOneDataset(domainModelName, fileName, main);

			prevFileName = fileName;

		}

		SaveMainCSV(main, "features.csv", ";");
		System.out.println("---- Done creating features csv file ----");
	}

	private static boolean ProcessOneDataset(String domainName, String fileName, Instances main) {
		boolean success = true;

		String fullDatasetPath = "datasets/" + domainName + "/" + fileName;

		Instances dataset;
		dataset = csvManager.GetDataSet(fullDatasetPath);

		if (dataset == null) {
			success = false;
			System.out.println("There was a problem reading the file: " + fullDatasetPath + ". Skipping..");
			return success;
		}

		System.out.println("----- File: " + fullDatasetPath + " -----");

		int attributesNumber = dataset.numAttributes();
		Features[] features = new Features[attributesNumber];

		for (int columnNr = 0; columnNr < attributesNumber; columnNr++) {
			System.out.println("---- Column nr.: " + (columnNr + 1));

			String columnName = dataset.attribute(columnNr).name();
			features[columnNr] = new Features(dataset, columnName, columnNr);
		}

		SaveFeaturesToMainCSV(features, main, fileName);

		// Some data about CSV
		System.out.println();
		System.out.println(dataset.toSummaryString());

		return success;
	}

	private static void SaveFeaturesToMainCSV(Features[] features, Instances main, String fileName) {
		for (Instance row : main) {
			if (row.stringValue(1).equals(fileName)) {
				String columnName = row.stringValue(2);
				for (int i = 0; i < features.length; ++i) {
					if (columnName.equals(features[i].columnName)) {
						row.setValue(4, features[i].dataType);
						row.setValue(5, features[i].positiveNegativeZeroValueRatio.PositiveValueRatio);
						row.setValue(6, features[i].positiveNegativeZeroValueRatio.NegativeValueRatio);
						row.setValue(7, features[i].positiveNegativeZeroValueRatio.ZeroValueRatio);
						row.setValue(8, features[i].uniqueValueRatio);
						row.setValue(9, features[i].sameDigitalNumber);

						row.setValue(10, features[i].statisticValuesModel.Average);
						row.setValue(11, features[i].statisticValuesModel.Minimum);
						row.setValue(12, features[i].statisticValuesModel.Maximum);
						row.setValue(13, features[i].statisticValuesModel.Median);
						row.setValue(14, features[i].statisticValuesModel.UpperQuartile);
						row.setValue(15, features[i].statisticValuesModel.LowerQuartile);
						row.setValue(16, features[i].coefficientOfVariation);
						row.setValue(17, features[i].rangeRatio);

						row.setValue(18, features[i].locationRatio);
						row.setValue(19, features[i].numericalColumnRatioTmp);
						row.setValue(20, features[i].numericalNeighbor);

						break;
					}
				}
			}
		}
	}

	private static void SaveMainCSV(Instances main, String fileName) {
		SaveMainCSV(main, fileName, ",");
	}

	private static void SaveMainCSV(Instances main, String fileName, String separator) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

			for (int i = 0; i < main.numAttributes(); ++i) {
				writer.write(main.attribute(i).name());
				if (i < main.numAttributes() - 1) {
					writer.write(separator);
				} else {
					writer.write("\n");
				}
			}

			for (Instance row : main) {
				for (int j = 0; j < 3; ++j) {
					writer.write(row.stringValue(j) + separator);
				}
				for (int j = 3; j < main.numAttributes(); ++j) {
					writer.write(Double.toString(row.value(j)));
					if (j < main.numAttributes() - 1) {
						writer.write(separator);
					} else {
						writer.write("\n");
					}
				}
			}

			writer.close();
		} catch (IOException e) {

		}
	}
}
