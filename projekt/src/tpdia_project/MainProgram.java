package tpdia_project;

import java.io.IOException;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

// TODO read from all files (not only 1 csv)
public class MainProgram {
	public static void main(String[] args) {
		String test_dataset = "datasets/CA/natural-gas-liquids-exports-annual.csv";

		CSVManager csvManager = new CSVManager();
		FeatureExtractionManager extractionManager = new FeatureExtractionManager();

		Instances dataset;
		dataset = csvManager.GetDataSet(test_dataset);

		int attributesNumber = dataset.numAttributes();

		System.out.println("----- File: " + test_dataset + " -----");
		for (int columnNr = 0; columnNr < attributesNumber; columnNr++) {

			System.out.println("---- Column nr.: " + (columnNr + 1));

			int dataType = extractionManager.GetDataType(dataset, columnNr);
			ValueRatioModel positiveNegativeZeroValueRatio = extractionManager
					.GetPositiveNegativeZeroValueRatio(dataset, columnNr);
			double uniqueValueRatio = extractionManager.GetUniqueValueRatio(dataset, columnNr);
			int sameDigitalNumber = extractionManager.GetSameDigitalNumber(dataset, columnNr);
			StatisticValuesModel statisticValuesModel = extractionManager.GetAvgMinMaxMedianUpquarLowquar(dataset,
					columnNr);

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
		}

		System.out.println();
		System.out.println(dataset.toSummaryString());
	}
}
