package tpdia_project;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.Attribute;

// class for CSV operations
public class CSVManager {
	public Instances GetDataSet(String fileName) {
		
		Instances dataSet;
		CSVLoader loader = new CSVLoader();
		try {
			loader.setFile(new File(fileName));
			dataSet = loader.getDataSet();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
		
		return dataSet;
	}
	
	public Instances DeleteAllCoumnsExceptNumeric(Instances dataset) {
		dataset.deleteAttributeType(Attribute.DATE);
		dataset.deleteAttributeType(Attribute.NOMINAL);
		dataset.deleteAttributeType(Attribute.STRING);
		
		return dataset;
	}
}
