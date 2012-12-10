package classification;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataParser {

	public ArrayList<DataNode> Parse(String fileName) throws FileNotFoundException {
		ArrayList<DataNode> results = new ArrayList<DataNode>();
		File dataFile = new File(fileName);
		Scanner dataReader = new Scanner(dataFile);
		
		while(dataReader.hasNextLine()) {
			String currentLine = dataReader.nextLine();
			Scanner lineReader = new Scanner(currentLine);
			
			String classifiedVal = lineReader.next();
			int givenClassification;

			if(classifiedVal.charAt(0) == '+')
				givenClassification = 1;
			else
				givenClassification = -1;

			ArrayList<Integer> attributes = new ArrayList<Integer>();
			
			while(lineReader.hasNextInt()) {
				attributes.add(lineReader.nextInt());
			}
			
			DataNode currentDataNode = new DataNode(givenClassification, attributes);
			results.add(currentDataNode);
			lineReader.close();
		}
		dataReader.close();
		
		return results;
	}
}
