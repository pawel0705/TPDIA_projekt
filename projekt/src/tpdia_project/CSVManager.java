package tpdia_project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tpdia_project.Models.DatasetInformationModel;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.Attribute;

// class for CSV operations
public class CSVManager {
	public Instances GetDataSet(String fileName) {

		Instances dataSet;
		CSVLoader loader = new CSVLoader();
		loader.setFieldSeparator(",");
		try {
			loader.setFile(new File(fileName));
			dataSet = loader.getDataSet();
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		}

		return dataSet;
	}

	public ArrayList<DatasetInformationModel> GetDatasetsInfoFromDomain(String domainName) {

		ArrayList<DatasetInformationModel> datasetInformationList = new ArrayList<DatasetInformationModel>();

		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader("datasets/" + domainName + ".txt"));

			String line;
			while ((line = in.readLine()) != null) {
				String[] splitStr = line.split("\\s+");

				DatasetInformationModel datasetInfo = new DatasetInformationModel();
				datasetInfo.DatasetName = splitStr[0];

				if (splitStr.length > 1) {
					if (splitStr[1].equals("DISCARD")) {
						continue;
					}

					List<Integer> measuresIndexes = new ArrayList<Integer>();
					for (int i = 1; i < splitStr.length; i++) {
						// System.out.println(splitStr[i]);
						int index = Integer.parseInt(splitStr[i]);
						measuresIndexes.add(index);
					}

					datasetInfo.MeasuresColumns = measuresIndexes;
				}

				datasetInformationList.add(datasetInfo);
			}

			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return datasetInformationList;
	}

	public Instances DeleteAllCoumnsExceptNumericAndNominal(Instances dataset) {
		dataset.deleteAttributeType(Attribute.DATE);
		dataset.deleteAttributeType(Attribute.STRING);

		return dataset;
	}
}
