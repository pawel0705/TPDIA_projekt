package tpdia_project;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

// class for extracting features data from columns
public class FeatureExtractionManager {
	
	// 1 - General ///////////////////////////////////////
	
	// DataType
	public int GetDataType(Instances instances, int columnNumber) {
		Attribute attribute = instances.attribute(columnNumber);	
		
		if(!attribute.isNumeric()) {
			return -1; // no numeric column
		}
		
		int instancesNumber = instances.numInstances();
		
		int intNumbers = 0;
		int realNumbers = 0;
		
		for(int row = 0; row < instancesNumber; row++) {
			double tmpVal = instances.attributeToDoubleArray(columnNumber)[row];
			
			tmpVal = Math.abs(tmpVal - Math.floor(tmpVal));
			
			if(tmpVal < 0.001) {
				intNumbers++;
			} else {
				realNumbers++;
			}
		}

		if(realNumbers > intNumbers) {
			return 0; // float column
		}
		
		return 1; // integer column
	}
	
	// Positive value ratio
	public double GetPositiveValueRatio(Instances instances, int columnNumber) {		
		Attribute attribute = instances.attribute(columnNumber);	
		
		if(!(attribute.isNumeric() || attribute.isNominal())) {
			return -1; // no numeric or nominal column
		}
		
		int instancesNumber = instances.numInstances();
		int positiveValue = 0;
		
		// column with all numbers
		if(attribute.isNumeric()) {			
			for(int row = 0; row < instancesNumber; row++) {
				double tmpVal = instances.attributeToDoubleArray(columnNumber)[row];
				if(tmpVal > 0) {
					positiveValue++;
				}
			}
			
			double positiveRatio = positiveValue / (double)instancesNumber;
			
			return positiveRatio;
		}
		
		int nullInstancesNumber = 0;
		
		// column that can have null, unknown, etc.
		if(attribute.isNominal()) {
			for(int row = 0; row < instancesNumber; row++) {
				Instance instance = instances.get(row);
				String tmpValString = instance.stringValue(columnNumber);
				double tmpVal = 0.0;
				
				try {
					tmpVal = Double.parseDouble(tmpValString);
					
					if(tmpVal > 0) {
						positiveValue++;
					}
					
				} catch(Exception ex){
					nullInstancesNumber++;
				}
			}
			
			int fixedInstancesNumber = instancesNumber - nullInstancesNumber;
			
			if(fixedInstancesNumber <= 0) {
				return -1;
			}
			
			double positiveRatio = positiveValue / (double)fixedInstancesNumber;
			
			return positiveRatio;
		}
		
		return -1;
	}
	
}
