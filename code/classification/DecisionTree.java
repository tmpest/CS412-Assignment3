package classification;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import other_packages.ClassificationNode;
import other_packages.DataNode;
import other_packages.DataParser;

public class DecisionTree {

	public static ArrayList<DataNode> dataSet;
	public ClassificationNode root;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args[0] == null) {
			System.out.println("Missing Arguments!");
			return;
		}
		if (args[1] == null){
			System.out.println("Missing Arguments!");
			return;
		}
		
		String trainingSetFileName = args[0];
		String testingSetFileName = args[1];
		
		DataParser dataParser = new DataParser();
		try {
			dataSet = dataParser.Parse(trainingSetFileName);
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found! Please check the path to the File.");
			return;
		}
		
		
		try {
			dataSet = dataParser.Parse(testingSetFileName);
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found! Please check the path to the File.");
			return;
		}
	}

	public static ArrayList<Hashtable<Integer, Integer>> sortAttributeData(ArrayList<DataNode> dataSet) {
		DataNode currNode = dataSet.get(0);
		int numOfAttr = currNode.attributes.size();
		ArrayList<Hashtable<Integer, Integer>> results = new ArrayList<Hashtable<Integer, Integer>>();
		
		for(int i = 0; i < numOfAttr; i++ ) {
			Hashtable<Integer, Integer> attrHash = new Hashtable<Integer, Integer>();
			for(int j = 0; j < dataSet.size(); j++) {
				currNode = dataSet.get(j);
				int attrVal = currNode.attributes.get(i);
				if(attrHash.containsKey(attrVal)) {
					int curCount = attrHash.get(attrVal) + 1;
					attrHash.put(attrVal, curCount);
				}
				else
					attrHash.put(attrVal, 1);
			}
			results.add(attrHash);
		}
		
		return results;
	}

	public double calculateInfoValue(ArrayList<Hashtable<Integer, Integer>> attributeData, int attributeIndex) {
		int TotalCount = sumOfAttributeCounts(attributeData, attributeIndex);
		Hashtable<Integer, Integer> attributeHash = attributeData.get(attributeIndex);
		Set<Integer> keys = attributeHash.keySet();
		int summation = 0;
		
		for(int key : keys) {
			int attrCount = 
		}
				
		return 0.0;
	}
	
	public int sumOfAttributeCounts(ArrayList<Hashtable<Integer, Integer>> attributeData, int attributeIndex) {
		Hashtable<Integer, Integer> attributeHash = attributeData.get(attributeIndex);
		Set<Integer> keys = attributeHash.keySet();
		int summation = 0;
		
		for(int key : keys) {
			summation += attributeHash.get(key);
		}
		
		return summation;
	}
}