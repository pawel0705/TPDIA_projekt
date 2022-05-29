package tpdia_project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import tpdia_project.Models.StatisticValuesModel;
import tpdia_project.Models.ValueRatioModel;
import weka.core.Instances;

public class Features {

	// ----- General Features
	private int dataType;
	private ValueRatioModel positiveNegativeZeroValueRatio;
	private double uniqueValueRatio;
	private int sameDigitalNumber;
	
	// ----- Statistical Features
	StatisticValuesModel statisticValuesModel;
	double coefficientOfVariation;
	double rangeRatio;
	
	// ----- Inter-Column Features
	double locationRatio;
	double numericalColumnRatioTmp;
	double numericalNeighbor;
	
	public Features(Instances dataset, int columnNr)
	{
		FeatureExtractionManager extractionManager = new FeatureExtractionManager();
		
		int attributesNumber = dataset.numAttributes();

		double numericalColumnRatio = extractionManager.GetNumericalColumnRatio(dataset);

		// ----- General Features
		extractGeneralFeatures(dataset, extractionManager, columnNr);

		// ----- Statistical Features
		extractStatisticalFeatures(dataset, extractionManager, columnNr);

		// ----- Inter-Column Features // Uwaga. Potrzebna wiedza o wszystkich kolumnach
		// (wi�c poza nominalnymi i numerycznymi musz� by� pozosta�e)
		extractInterColumnFeatures(dataset, extractionManager, attributesNumber, numericalColumnRatio, columnNr);

		// Results in console
		Print();
	}

	private void extractGeneralFeatures(Instances dataset, FeatureExtractionManager extractionManager, int columnNr)
	{
		dataType = extractionManager.GetDataType(dataset, columnNr);
		positiveNegativeZeroValueRatio = extractionManager.GetPositiveNegativeZeroValueRatio(dataset, columnNr);
		uniqueValueRatio = extractionManager.GetUniqueValueRatio(dataset, columnNr);
		sameDigitalNumber = extractionManager.GetSameDigitalNumber(dataset, columnNr);
	}

	private void extractStatisticalFeatures(Instances dataset, FeatureExtractionManager extractionManager, int columnNr) {
		statisticValuesModel = extractionManager.GetAvgMinMaxMedianUpquarLowquar(dataset, columnNr);
		coefficientOfVariation = extractionManager.GetCoefficientOfVariation(dataset, columnNr);
		rangeRatio = extractionManager.GetRangeRatio(dataset, columnNr);
	}

	private void extractInterColumnFeatures(Instances dataset, FeatureExtractionManager extractionManager, int attributesNumber, double numericalColumnRatio, int columnNr) 
	{
		locationRatio = extractionManager.GetLocationRatio(attributesNumber, columnNr);
		numericalColumnRatioTmp = numericalColumnRatio;
		numericalNeighbor = extractionManager.GetNumericalNeighbor(dataset, attributesNumber, columnNr);
	}
	
	public void Print()
	{
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
	
	void SaveToCSV(FileWriter writer) throws IOException
	{
		writer.write(dataType + ","
				+ positiveNegativeZeroValueRatio.PositiveValueRatio + ","
				+ positiveNegativeZeroValueRatio.NegativeValueRatio + ","
				+ positiveNegativeZeroValueRatio.ZeroValueRatio + ","
				+ uniqueValueRatio + ","
				+ sameDigitalNumber + ","
				+ statisticValuesModel.Average + ","
				+ statisticValuesModel.Minimum + ","
				+ statisticValuesModel.Maximum + ","
				+ statisticValuesModel.Median + ","
				+ statisticValuesModel.UpperQuartile + ","
				+ statisticValuesModel.LowerQuartile + ","
				+ coefficientOfVariation + ","
				+ rangeRatio + ","
				+ locationRatio + ","
				+ numericalColumnRatioTmp + ","
				+ numericalNeighbor +"\n");
	}
}
