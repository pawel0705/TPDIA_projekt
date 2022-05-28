package tpdia_project.Models;

import java.util.ArrayList;
import java.util.List;

public class DatasetInformationModel {
	public DatasetInformationModel() {
		this.DatasetName = "";
		this.MeasuresColumns = new ArrayList<Integer>();
	}

	public String DatasetName;
	public List<Integer> MeasuresColumns;
}
