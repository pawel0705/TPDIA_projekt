package tpdia_project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

// class for extracting features data from columns
public class FeatureExtractionManager {

	// 1 - General Features ///////////////////////////////////////

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

	// Positive, negative, zero value ratio
	public ValueRatioModel GetPositiveNegativeZeroValueRatio(Instances instances, int columnNumber) {
		Attribute attribute = instances.attribute(columnNumber);

		ValueRatioModel valueRatio = new ValueRatioModel();
		valueRatio.NegativeValueRatio = -1;
		valueRatio.PositiveValueRatio = -1;
		valueRatio.ZeroValueRatio = -1;

		if (!(attribute.isNumeric() || attribute.isNominal())) {
			return valueRatio;
		}

		int instancesNumber = instances.numInstances();
		int positiveValue = 0;
		int negativeValue = 0;
		int zeroValue = 0;

		// column with all numbers
		if (attribute.isNumeric()) {
			for (int row = 0; row < instancesNumber; row++) {
				double tmpVal = instances.attributeToDoubleArray(columnNumber)[row];

				if (Math.abs(tmpVal) < 2 * Double.MIN_VALUE) {
					zeroValue++;
				} else if (tmpVal > 0) {
					positiveValue++;
				} else {
					negativeValue++;
				}
			}

			valueRatio.NegativeValueRatio = negativeValue / (double) instancesNumber;
			valueRatio.PositiveValueRatio = positiveValue / (double) instancesNumber;
			valueRatio.ZeroValueRatio = zeroValue / (double) instancesNumber;

			return valueRatio;
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
					} else if (tmpVal > 0) {
						positiveValue++;
					} else {
						negativeValue++;
					}

				} catch (Exception ex) {
					nullInstancesNumber++;
				}
			}

			int fixedInstancesNumber = instancesNumber - nullInstancesNumber;

			if (fixedInstancesNumber <= 0) {
				return valueRatio;
			}

			valueRatio.NegativeValueRatio = negativeValue / (double) fixedInstancesNumber;
			valueRatio.PositiveValueRatio = positiveValue / (double) fixedInstancesNumber;
			valueRatio.ZeroValueRatio = zeroValue / (double) fixedInstancesNumber;

			return valueRatio;
		}

		return valueRatio;
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

	// Same digital number
	public int GetSameDigitalNumber(Instances instances, int columnNumber) {

		int dataType = this.GetDataType(instances, columnNumber);

		if (dataType == -1) // must be integer
		{
			return -1;
		}

		if (dataType == 0) {
			return 0;
		}

		int instancesNumber = instances.numInstances();
		Attribute attribute = instances.attribute(columnNumber);

		List<Integer> uniqueLength = new ArrayList<>();

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

			int dataLength = tmpValString.length();

			if (!uniqueLength.contains(dataLength)) {
				uniqueLength.add(dataLength);
			}

		}

		if (uniqueLength.size() == 1) {
			return 1;
		}

		return 0;
	}

	// 2 - Statistical Features ///////////////////////////////////////

	// Average/Minimum/Maximum/Median/Upper quartile/Lower quartile values
	public StatisticValuesModel GetAvgMinMaxMedianUpquarLowquar(Instances instances, int columnNumber) {
		Attribute attribute = instances.attribute(columnNumber);

		StatisticValuesModel statisticValues = new StatisticValuesModel();
		statisticValues.Average = -1;
		statisticValues.Minimum = -1;
		statisticValues.Maximum = -1;
		statisticValues.Median = -1;
		statisticValues.UpperQuartile = -1;
		statisticValues.LowerQuartile = -1;

		if (!(attribute.isNumeric() || attribute.isNominal())) {
			return statisticValues;
		}

		int instancesNumber = instances.numInstances();

		List<Double> readedValues = new ArrayList<Double>();

		// column with all numbers
		if (attribute.isNumeric()) {
			for (int row = 0; row < instancesNumber; row++) {
				double tmpVal = instances.attributeToDoubleArray(columnNumber)[row];

				readedValues.add(tmpVal);
			}
		}

		// column that can have null, unknown, etc.
		if (attribute.isNominal()) {
			for (int row = 0; row < instancesNumber; row++) {
				Instance instance = instances.get(row);
				String tmpValString = instance.stringValue(columnNumber);
				double tmpVal = 0.0;

				try {
					tmpVal = Double.parseDouble(tmpValString);
					readedValues.add(tmpVal);
				} catch (Exception ex) {
					// Do nothing
				}
			}
		}

		if (readedValues.size() > 0) {
			statisticValues.Average = readedValues.stream().mapToDouble(a -> a).average().getAsDouble();
			statisticValues.Minimum = readedValues.stream().mapToDouble(a -> a).min().getAsDouble();
			statisticValues.Maximum = readedValues.stream().mapToDouble(a -> a).max().getAsDouble();

			Collections.sort(readedValues);

			double middle = readedValues.size() / 2;
			if (readedValues.size() % 2 == 0) {
				middle = (readedValues.get(readedValues.size() / 2) + readedValues.get(readedValues.size() / 2 - 1))
						/ 2;
			} else {
				middle = readedValues.get(readedValues.size() / 2);
			}

			statisticValues.Median = middle;

			double quartilies[] = new double[3];

			for (int quartileType = 1; quartileType < 4; quartileType++) {
				float length = readedValues.size() - 1;
				double quartile;
				float newArraySize = (length * ((float) (quartileType) * 25 / 100)) - 1;

				if (newArraySize % 1 == 0) {
					quartile = readedValues.get((int) (newArraySize));
				} else {
					int newArraySize1 = (int) (newArraySize);
					quartile = (readedValues.get(newArraySize1) + readedValues.get(newArraySize1 + 1)) / 2;
				}
				quartilies[quartileType - 1] = quartile;
			}

			statisticValues.LowerQuartile = quartilies[0];
			statisticValues.UpperQuartile = quartilies[2];
		}

		return statisticValues;
	}

	// Coefficient of variation
	public double GetCoefficientOfVariation(Instances instances, int columnNumber) {
		Attribute attribute = instances.attribute(columnNumber);

		if (!(attribute.isNumeric() || attribute.isNominal())) {
			return -1;
		}

		int instancesNumber = instances.numInstances();

		List<Double> readedValues = new ArrayList<Double>();

		// column with all numbers
		if (attribute.isNumeric()) {
			for (int row = 0; row < instancesNumber; row++) {
				double tmpVal = instances.attributeToDoubleArray(columnNumber)[row];

				readedValues.add(tmpVal);
			}
		}

		// column that can have null, unknown, etc.
		if (attribute.isNominal()) {
			for (int row = 0; row < instancesNumber; row++) {
				Instance instance = instances.get(row);
				String tmpValString = instance.stringValue(columnNumber);
				double tmpVal = 0.0;

				try {
					tmpVal = Double.parseDouble(tmpValString);
					readedValues.add(tmpVal);
				} catch (Exception ex) {
					// Do nothing
				}
			}
		}

		double average = 0.0;
		if (readedValues.size() > 0) {
			average = readedValues.stream().mapToDouble(a -> a).average().getAsDouble();
		}

		double stdev = -1;
		double sum = 0;

		if (readedValues.size() > 0) {
			for (int i = 0; i < readedValues.size(); i++) {
				sum = sum + (readedValues.get(i) - average) * (readedValues.get(i) - average);
			}
			double squaredDiffMean = (sum) / (readedValues.size());
			stdev = (Math.sqrt(squaredDiffMean));

			if (Math.abs(average) < 2 * Double.MIN_VALUE) {
				return stdev;
			}

			stdev = stdev / average;
		}

		return stdev;
	}

	// Range ratio
	public double GetRangeRatio(Instances instances, int columnNumber) {
		Attribute attribute = instances.attribute(columnNumber);

		if (!(attribute.isNumeric() || attribute.isNominal())) {
			return -1; // no numeric or nominal column
		}

		int instancesNumber = instances.numInstances();

		List<Double> uniqueValues = new ArrayList<>();

		for (int row = 0; row < instancesNumber; row++) {
			Instance instance = instances.get(row);
			double tmpVal = 0.0;

			if (attribute.isNominal()) {
				String tmpValString = instance.stringValue(columnNumber);

				try {
					tmpVal = Double.parseDouble(tmpValString);
				} catch (Exception ex) {
					continue;
				}
			}

			if (attribute.isNumeric()) {
				tmpVal = instances.attributeToDoubleArray(columnNumber)[row];
			}

			if (!uniqueValues.contains(tmpVal)) {
				uniqueValues.add(tmpVal);
			}
		}

		double rangeRatio = -1;

		if (uniqueValues.size() > 0) {
			double minimum = uniqueValues.stream().mapToDouble(a -> a).min().getAsDouble();
			double maximum = uniqueValues.stream().mapToDouble(a -> a).max().getAsDouble();

			rangeRatio = (maximum - minimum) / uniqueValues.size();
		}

		return rangeRatio;
	}

	// 3 - Inter-Column Features ///////////////////////////////////////

	// Location ratio
	public double GetLocationRatio(int maxColumns, int columnNumber) {

		double locationRatio = ((double) columnNumber - 1.0) / ((double) maxColumns - 1.0);

		return locationRatio;
	}

}
