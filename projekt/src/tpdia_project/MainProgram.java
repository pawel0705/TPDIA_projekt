package tpdia_project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import tpdia_project.Models.DatasetInformationModel;
import tpdia_project.Models.StatisticValuesModel;
import tpdia_project.Models.ValueRatioModel;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class MainProgram {
	static CSVManager csvManager = new CSVManager();

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
		System.out.println("Aby dokona� trenowania modelu wci�nij klawisz \"a\" i potwierd� wyb�r.");
		System.out.println("Aby dokona� wyj�� z programu wci�nij klawisz \"q\" i potwierd� wyb�r.");
	}

	// TODO - work in progress...
	private static void TrainModel() {
		String datasetFiles[] = { "AFD", "CA", "CDC", "NZ" };

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
				boolean result = ProcessOneDataset(domainModelName, datasetsInfo.get(j).DatasetName, datasetsInfo.get(j).MeasuresColumns);

				if (result == false) {
					failureInterator++;
				}
			}
		}
		
		System.out.println("---- Done training model ----");
		System.out.println("Total files count: " + filesOpenedIterator);
		System.out.println("Number of files that could not be opened: " + failureInterator);
	}

	private static void SaveFeaturesToCSV(Features[] features, String directory, String fileName)
	{
		File dir = new File(directory);
		if(!dir.exists())
		{
			dir.mkdirs();
		}
		
		try 
		{
			File csv = new File(directory + fileName + ".csv");
			csv.createNewFile();
			
			FileWriter writer = new FileWriter(csv);
			writer.write("DataType,"
					+ "PositiveValueRatio,NegativeValueRatio,ZeroValueRatio,"
					+ "UniqueValueRatio,"
					+ "SameDigitalNumber,"
					+ "Average,Minimum,Maximum,Median,UpperQuartile,LowerQuartile,"
					+ "VariationCoefficient,"
					+ "RangeRatio,"
					+ "LocationRatio,"
					+ "NumericalColumnRatio,"
					+ "NumericalNeighbour,"
					+ "IsMeasure"
					+ "\n");
			
			for(Features columnFeatures : features)
			{
				columnFeatures.SaveToCSV(writer);
			}
			
			writer.close();
		} 
		catch(IOException e)
		{
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static boolean ProcessOneDataset(String domainName, String fileName, List<Integer> measuresColumns) {

		boolean success = true;

		String fullDatasetPath = "datasets/" + domainName + "/" + fileName + ".csv";

		Instances dataset;
		dataset = csvManager.GetDataSet(fullDatasetPath);

		if (dataset == null) {
			success = false;
			System.out.println("There was a problem reading the file: " + fullDatasetPath + ". Skipping..");
			return success;
		}

		System.out.println("----- File: " + fullDatasetPath + " -----");
		
		int attributesNumber = dataset.numAttributes();
		Features[] features = new Features[attributesNumber];
		
		for (int columnNr = 0; columnNr < attributesNumber; columnNr++) 
		{
			System.out.println("---- Column nr.: " + (columnNr + 1));
			features[columnNr] = new Features(dataset, columnNr, measuresColumns.contains(columnNr + 1));
		}
		
		SaveFeaturesToCSV(features, "extractedFeatures/", fileName);
		
		// Some data about CSV
		System.out.println();
		System.out.println(dataset.toSummaryString());

		return success;
	}
}
