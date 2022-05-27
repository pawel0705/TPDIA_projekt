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

			int dataType = extractionManager.GetDataType(dataset, columnNr); // -1 - no number column; 1 - integer; 0 -
																				// float
			double positiveValueRatio = extractionManager.GetPositiveValueRatio(dataset, columnNr);
			double negativeValueRatio = extractionManager.GetNegativeValueRatio(dataset, columnNr);
			double zeroValueRatio = extractionManager.GetZeroValueRatio(dataset, columnNr);

			System.out.println("Data Type: " + dataType);
			System.out.println("Positive value ratio: " + positiveValueRatio);
			System.out.println("Negative value ratio: " + negativeValueRatio);
			System.out.println("Zero value ratio: " + zeroValueRatio);
		}

		System.out.println(dataset.toSummaryString());
	}
}
