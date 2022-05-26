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
		//int instancesNumber = dataset.numInstances(); 
		System.out.println(attributesNumber);
		for(int columnNr = 0; columnNr < attributesNumber; columnNr++) {
			Attribute attribute = dataset.attribute(columnNr);		
			int dataType = extractionManager.GetDataType(attribute, dataset, columnNr); // -1 - no number column; 1 - integer; 0 - float
			System.out.println(dataType); 
		}
		
		System.out.println(dataset.toSummaryString());
	}
}
