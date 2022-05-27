package tpdia_project;

import java.util.ArrayList;
import java.util.List;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

// class for extracting features data from columns
public class FeatureExtractionManager {

	// 1 - General ///////////////////////////////////////

	// DataType
	public int GetDataType(Instances instances, int columnNumber) {
		Attribute attribute = instances.attribute(columnNumber);

		// je¿eli kolumna ma przyk³adowo 99% liczb i 1% np. NULL jest to traktowane jako
		// Nominal (wiêc trzeba uwzglêdniaæ)
		if (!(attribute.isNumeric() || attribute.isNominal())) {
			return -1; // no numeric or nominal column
		}

		int instancesNumber = instances.numInstances();

		int intNumbers = 0;
		int realNumbers = 0;

		if (attribute.isNumeric()) {
			for (int row = 0; row < instancesNumber; row++) {
				double tmpVal = instances.attributeToDoubleArray(columnNumber)[row];

				tmpVal = Math.abs(tmpVal - Math.floor(tmpVal));

				if (Math.abs(tmpVal) < 2 * Double.MIN_VALUE) {
					intNumbers++;
				} else {
					realNumbers++;
				}
			}

			if (realNumbers > intNumbers) {
				return 0; // float column
			}

			return 1; // integer column
		}

		if (attribute.isNominal()) {
			int notNumber = 0;

			for (int row = 0; row < instancesNumber; row++) {
				Instance instance = instances.get(row);
				String tmpValString = instance.stringValue(columnNumber);
				double tmpVal = 0.0;

				try {
					tmpVal = Double.parseDouble(tmpValString);
					tmpVal = Math.abs(tmpVal - Math.floor(tmpVal));

					if (Math.abs(tmpVal) < 2 * Double.MIN_VALUE) {
						intNumbers++;
					} else {
						realNumbers++;
					}

				} catch (Exception ex) {
					notNumber++;
				}
			}

			if (notNumber > (instancesNumber * 0.8)) {
				return -1; // je¿eli ponad 80% to nie jest numer, to taka kolumna nie ma sensu byæ brana
							// pod uwagê (80% to mój pomys³)
			}

			if (realNumbers > intNumbers) {
				return 0; // float column
			}

			return 1; // integer column
		}

		return -1;
	}

	// Positive value ratio
	public double GetPositiveValueRatio(Instances instances, int columnNumber) {
		Attribute attribute = instances.attribute(columnNumber);

		if (!(attribute.isNumeric() || attribute.isNominal())) {
			return -1; // no numeric or nominal column
		}

		int instancesNumber = instances.numInstances();
		int positiveValue = 0;

		// column with all numbers
		if (attribute.isNumeric()) {
			for (int row = 0; row < instancesNumber; row++) {
				double tmpVal = instances.attributeToDoubleArray(columnNumber)[row];

				if (Math.abs(tmpVal) < 2 * Double.MIN_VALUE) {
					continue;
				}

				if (tmpVal > 0) {
					positiveValue++;
				}
			}

			double positiveRatio = positiveValue / (double) instancesNumber;

			return positiveRatio;
		}

		int nullInstancesNumber = 0;

		// column that can have null, unknown, etc.
		if (attribute.isNominal()) {
			for (int row = 0; row < instancesNumber; row++) {
				Instance instance = instances.get(row);
				String tmpValString = instance.stringValue(columnNumber);
				double tmpVal = 0.0;

				try {
					tmpVal = Double.parseDouble(tmpValString);

					if (Math.abs(tmpVal) < 2 * Double.MIN_VALUE) {
						continue;
					}

					if (tmpVal > 0) {
						positiveValue++;
					}

				} catch (Exception ex) {
					nullInstancesNumber++;
				}
			}

			int fixedInstancesNumber = instancesNumber - nullInstancesNumber;

			if (fixedInstancesNumber <= 0) {
				return -1;
			}

			double positiveRatio = positiveValue / (double) fixedInstancesNumber;

			return positiveRatio;
		}

		return -1;
	}

	// Negative value ratio
	public double GetNegativeValueRatio(Instances instances, int columnNumber) {
		Attribute attribute = instances.attribute(columnNumber);

		if (!(attribute.isNumeric() || attribute.isNominal())) {
			return -1; // no numeric or nominal column
		}

		int instancesNumber = instances.numInstances();
		int negativeValue = 0;

		// column with all numbers
		if (attribute.isNumeric()) {
			for (int row = 0; row < instancesNumber; row++) {
				double tmpVal = instances.attributeToDoubleArray(columnNumber)[row];

				if (Math.abs(tmpVal) < 2 * Double.MIN_VALUE) {
					continue;
				}

				if (tmpVal < 0) {
					negativeValue++;
				}
			}

			double positiveRatio = negativeValue / (double) instancesNumber;

			return positiveRatio;
		}

		int nullInstancesNumber = 0;

		// column that can have null, unknown, etc.
		if (attribute.isNominal()) {
			for (int row = 0; row < instancesNumber; row++) {
				Instance instance = instances.get(row);
				String tmpValString = instance.stringValue(columnNumber);
				double tmpVal = 0.0;

				try {
					tmpVal = Double.parseDouble(tmpValString);

					if (Math.abs(tmpVal) < 2 * Double.MIN_VALUE) {
						continue;
					}

					if (tmpVal < 0) {
						negativeValue++;
					}

				} catch (Exception ex) {
					nullInstancesNumber++;
				}
			}

			int fixedInstancesNumber = instancesNumber - nullInstancesNumber;

			if (fixedInstancesNumber <= 0) {
				return -1;
			}

			double positiveRatio = negativeValue / (double) fixedInstancesNumber;

			return positiveRatio;
		}

		return -1;
	}

	// Zero value ratio
	public double GetZeroValueRatio(Instances instances, int columnNumber) {
		Attribute attribute = instances.attribute(columnNumber);

		if (!(attribute.isNumeric() || attribute.isNominal())) {
			return -1; // no numeric or nominal column
		}

		int instancesNumber = instances.numInstances();
		int zeroValue = 0;

		// column with all numbers
		if (attribute.isNumeric()) {
			for (int row = 0; row < instancesNumber; row++) {
				double tmpVal = instances.attributeToDoubleArray(columnNumber)[row];
				if (Math.abs(tmpVal) < 2 * Double.MIN_VALUE) {
					zeroValue++;
				}
			}

			double positiveRatio = zeroValue / (double) instancesNumber;

			return positiveRatio;
		}

		int nullInstancesNumber = 0;

		// column that can have null, unknown, etc.
		if (attribute.isNominal()) {
			for (int row = 0; row < instancesNumber; row++) {
				Instance instance = instances.get(row);
				String tmpValString = instance.stringValue(columnNumber);
				double tmpVal = 0.0;

				try {
					tmpVal = Double.parseDouble(tmpValString);

					if (Math.abs(tmpVal) < 2 * Double.MIN_VALUE) {
						zeroValue++;
					}

				} catch (Exception ex) {
					nullInstancesNumber++;
				}
			}

			int fixedInstancesNumber = instancesNumber - nullInstancesNumber;

			if (fixedInstancesNumber <= 0) {
				return -1;
			}

			double positiveRatio = zeroValue / (double) fixedInstancesNumber;

			return positiveRatio;
		}

		return -1;
	}

	// Unique value ratio
	public double GetUniqueValueRatio(Instances instances, int columnNumber) {
		Attribute attribute = instances.attribute(columnNumber);

		if (!(attribute.isNumeric() || attribute.isNominal())) {
			return -1; // no numeric or nominal column
		}

		int instancesNumber = instances.numInstances();

		List<String> uniqueValues = new ArrayList<>();

		for (int row = 0; row < instancesNumber; row++) {
			Instance instance = instances.get(row);
			String tmpValString = "";

			if (attribute.isNominal()) {
				tmpValString = instance.stringValue(columnNumber);
			}

			if (attribute.isNumeric()) {
				double tmpVal = instances.attributeToDoubleArray(columnNumber)[row];
				tmpValString = String.valueOf(tmpVal);
			}

			if (!uniqueValues.contains(tmpValString)) {
				uniqueValues.add(tmpValString);
			}
		}

		double uniqueRatio = (double) uniqueValues.size() / (double) instancesNumber;

		return uniqueRatio;
	}
}
