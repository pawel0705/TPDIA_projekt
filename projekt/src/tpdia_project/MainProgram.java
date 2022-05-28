package tpdia_project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import tpdia_project.Models.DatasetInformationModel;
import tpdia_project.Models.StatisticValuesModel;
import tpdia_project.Models.ValueRatioModel;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class MainProgram {
	static CSVManager csvManager = new CSVManager();
	static FeatureExtractionManager extractionManager = new FeatureExtractionManager();

	public static void main(String[] args) {

		boolean exitProgram = false;
		String pressedKey = "";

		do {
			PrintHelp();

			Scanner in = new Scanner(System.in);
			pressedKey = in.nextLine();

			if (pressedKey.equals("a") || pressedKey.equals("A")) {
				TrainModel();
			}

			if (pressedKey.equals("q") || pressedKey.equals("Q")) {
				exitProgram = true;
			}

			pressedKey = "";
		} while (exitProgram == false);

		return;
	}

	private static void PrintHelp() {
		System.out.println("Aby dokonaæ trenowania modelu wciœnij klawisz \"a\" i potwierdŸ wybór.");
		System.out.println("Aby dokonaæ wyjœæ z programu wciœnij klawisz \"q\" i potwierdŸ wybór.");
	}

	// TODO - work in progress...
	private static void TrainModel() {
		String datasetFiles[] = { "AFD", "CA", "CDC" };

		int failureInterator = 0;
		int filesOpenedIterator = 0;

		for (int i = 0; i < datasetFiles.length; i++) {
			String domainModelName = datasetFiles[i];
			ArrayList<DatasetInformationModel> datasetsInfo = csvManager.GetDatasetsInfoFromDomain(domainModelName);

			if (datasetsInfo == null) {
				continue;
			}

			for (int j = 0; j < datasetsInfo.size(); j++) {
				filesOpenedIterator++;
				boolean result = ProcessOneDataset(domainModelName, datasetsInfo.get(j).DatasetName);

				if (result == false) {
					failureInterator++;
				}
			}
		}
		
		System.out.println("---- Done training model ----");
		System.out.println("Total files count: " + filesOpenedIterator);
		System.out.println("Number of files that could not be opened: " + failureInterator);
	}

	private static boolean ProcessOneDataset(String domainName, String fileName) {

		boolean success = true;

		String fullDatasetPath = "datasets/" + domainName + "/" + fileName + ".csv";

		Instances dataset;
		dataset = csvManager.GetDataSet(fullDatasetPath);

		if (dataset == null) {
			success = false;
			System.out.println("There was a problem reading the file: " + fullDatasetPath + ". Skipping..");
			return success;
		}

		int attributesNumber = dataset.numAttributes();

		System.out.println("----- File: " + fullDatasetPath + " -----");
		double numericalColumnRatio = extractionManager.GetNumericalColumnRatio(dataset);
		for (int columnNr = 0; columnNr < attributesNumber; columnNr++) {

			System.out.println("---- Column nr.: " + (columnNr + 1));

			// ----- General Features
			int dataType = extractionManager.GetDataType(dataset, columnNr);
			ValueRatioModel positiveNegativeZeroValueRatio = extractionManager
					.GetPositiveNegativeZeroValueRatio(dataset, columnNr);
			double uniqueValueRatio = extractionManager.GetUniqueValueRatio(dataset, columnNr);
			int sameDigitalNumber = extractionManager.GetSameDigitalNumber(dataset, columnNr);

			// ----- Statistical Features
			StatisticValuesModel statisticValuesModel = extractionManager.GetAvgMinMaxMedianUpquarLowquar(dataset,
					columnNr);
			double coefficientOfVariation = extractionManager.GetCoefficientOfVariation(dataset, columnNr);
			double rangeRatio = extractionManager.GetRangeRatio(dataset, columnNr);

			// ----- Inter-Column Features // Uwaga. Potrzebna wiedza o wszystkich kolumnach
			// (wiêc poza nominalnymi i numerycznymi musz¹ byæ pozosta³e)
			double locationRatio = extractionManager.GetLocationRatio(attributesNumber, columnNr);
			double numericalColumnRatioTmp = numericalColumnRatio;
			double numericalNeighbor = extractionManager.GetNumericalNeighbor(dataset, attributesNumber, columnNr);

			// Results in console
			System.out.println("Data Type: " + dataType);
			System.out.println("Positive value ratio: " + positiveNegativeZeroValueRatio.PositiveValueRatio);
			System.out.println("Negative value ratio: " + positiveNegativeZeroValueRatio.NegativeValueRatio);
			System.out.println("Zero value ratio: " + positiveNegativeZeroValueRatio.ZeroValueRatio);
			System.out.println("Unique value ratio: " + uniqueValueRatio);
			System.out.println("Same digital number: " + sameDigitalNumber);
			System.out.println("Average: " + statisticValuesModel.Average);
			System.out.println("Minimum: " + statisticValuesModel.Minimum);
			System.out.println("Maximum: " + statisticValuesModel.Maximum);
			System.out.println("Median: " + statisticValuesModel.Median);
			System.out.println("Upper quartile: " + statisticValuesModel.UpperQuartile);
			System.out.println("Lower quartile: " + statisticValuesModel.LowerQuartile);
			System.out.println("Coefficient Of Variation: " + coefficientOfVariation);
			System.out.println("Range ratio: " + rangeRatio);
			System.out.println("Location ratio: " + locationRatio);
			System.out.println("Numerical column ratio: " + numericalColumnRatioTmp);
			System.out.println("Numerical neighbor: " + numericalNeighbor);
		}

		// Some data about CSV
		System.out.println();
		System.out.println(dataset.toSummaryString());

		return success;
	}
}
