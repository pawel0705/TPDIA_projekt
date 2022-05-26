package tpdia_project;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

// class for extracting features data from columns
public class FeatureExtractionManager {
	
	// 1 - General ///////////////////////////////////////
	
	// DataType
	public int GetDataType(Attribute attribute, Instances instances, int columnNumber) {
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
}
