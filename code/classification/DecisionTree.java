package classification;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class DecisionTree {

	public static ArrayList<DataNode> dataSet;
	public static ClassificationNode root;
	public static ClassifierNode classifier;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args[0] == null) {
			System.out.println("Missing Arguments!");
			return;
		}
		if (args[1] == null) {
			System.out.println("Missing Arguments!");
			return;
		}

		String trainingSetFileName = args[0];
		String testingSetFileName = args[1];

		DataParser dataParser = new DataParser();
		try {
			dataSet = dataParser.Parse(trainingSetFileName);
		} catch (FileNotFoundException e) {
			System.out
					.println("File Not Found! Please check the path to the File.");
			return;
		}

		// Creating Classifier based on Training Set
		ArrayList<Integer> attrs = dataSet.get(0).attributes;
		ArrayList<Integer> availableAttributes = new ArrayList<Integer>();
		for(int i = 0; i < attrs.size(); i++) {
			availableAttributes.add(1); 
		}
		
		root = new ClassificationNode(dataSet, availableAttributes);
		
		generateDecisionTree(root);
		generateClassification(root);
		
		classifier = new ClassifierNode();
		generateClassifierTree(root, classifier);		
		
		try {
			dataSet = dataParser.Parse(testingSetFileName);
		} catch (FileNotFoundException e) {
			System.out
					.println("File Not Found! Please check the path to the File.");
			return;
		}
		
		ArrayList<DataNode> classifiedData = classifyDataSet(classifier, dataSet);
		
		int truePos = 0, falsePos = 0, trueNeg = 0, falseNeg = 0;

		for(DataNode node : classifiedData) {
			if(node.decidedClassification > 0) {
				if(node.givenClassification > 0) 
					truePos++;
				else
					falsePos++;
			}
			else {
				if(node.givenClassification > 0)
					falseNeg++;
				else
					trueNeg++;
			}
		}
		
		System.out.println(truePos);
		System.out.println(falseNeg);
		System.out.println(falsePos);
		System.out.println(trueNeg);
		
	}
	
	public static ArrayList<DataNode> classifyDataSet(ClassifierNode classifier, ArrayList<DataNode> dataSet) {
		
		if(classifier.children == null) {
			int classification = classifier.classifierValue;
			for(DataNode node : dataSet) {
				node.decidedClassification = classification;
			}
			
			return dataSet;
		}
		
		int splittingAttr = classifier.splitingAttribute;
		Set<Integer> splittingVals = classifier.splitingAttributeValues;
		
		ArrayList<ArrayList<DataNode>> splitSets = new ArrayList<ArrayList<DataNode>>();
		
		for(int splitVal : splittingVals) {
			ArrayList<DataNode> splitSet = new ArrayList<DataNode>();
			for(DataNode currNode : dataSet) {
				if(currNode.attributes.get(splittingAttr) == splitVal) {
					splitSet.add(currNode);
				}
			}
			splitSets.add(splitSet);
		}
		
		ArrayList<DataNode> resultDataSet = new ArrayList<DataNode>();
		ArrayList<ClassifierNode> children = classifier.children;
		for(int i = 0; i < children.size(); i++) {
			resultDataSet.addAll(classifyDataSet(children.get(i), splitSets.get(i)));
		}
		
		return resultDataSet;
	}
	
	public static void generateClassifierTree(ClassificationNode trainRoot, ClassifierNode root) {
		if(trainRoot.isLeaf() || trainRoot.children == null || trainRoot.children.size() == 0) {
			root.classifierValue = trainRoot.classifierValue;
			return;
		}
		
		root.splitingAttribute = trainRoot.splittingAttributeIndex;
		root.splitingAttributeValues = trainRoot.splittingAttributeValues;
		root.children = new ArrayList<ClassifierNode>();
		
		for(int i = 0; i < trainRoot.children.size(); i++) {
			ClassifierNode child = new ClassifierNode();
			generateClassifierTree(trainRoot.children.get(i), child);
			root.children.add(child);
		}
		
		return;
	}
	
	public static void generateClassification(ClassificationNode root) {
		if(root.isLeaf()) {
			int positiveCount = 0;
			for(int i = 0; i < root.dataSet.size(); i++) {
				DataNode currNode = root.dataSet.get(i);
				
				if(currNode.givenClassification > 0) {
					positiveCount++;
				}
			}
			
			if(positiveCount > (root.dataSet.size() / 2))
				root.classifierValue = 1;
			else
				root.classifierValue = -1;
			
			return;
		}
		
		for(ClassificationNode child : root.children)
			generateClassification(child);
			
		return;
	}
	public static void generateDecisionTree(ClassificationNode root) {
		if(isLeafNode(root.availableAttributes))
			return;
		
		ArrayList<Hashtable<Integer, int[]>> sortedDataSet = sortAttributeData(dataSet);
		
		double maxGainRatio = 0.0;
		int maxGainIndex = -1;
		
		for(int i = 0; i < sortedDataSet.size(); i++) {
			if(root.availableAttributes.get(i) == 1) {
				double currGainRatio = calculateGainRatio(sortedDataSet, i);
				
				if(maxGainRatio < currGainRatio) {
					maxGainRatio = currGainRatio;
					maxGainIndex = i;
				}
			}
		}
		
		if(maxGainIndex < 0) {
			return;
		}
		
		ArrayList<ArrayList<DataNode>> splitSets = new ArrayList<ArrayList<DataNode>>();
		Hashtable<Integer, int[]> attrHash = sortedDataSet.get(maxGainIndex);
		Set<Integer> keys = attrHash.keySet();
		
		if(keys.size() <= 1) {
			return;
		}
		
		root.availableAttributes.set(maxGainIndex, 0);
		
		for(int key : keys) {
			ArrayList<DataNode> splitDataSet = new ArrayList<DataNode>();
			
			for(int i = 0; i < dataSet.size(); i++) {
				DataNode currNode = dataSet.get(i);
				
				if(currNode.attributes.get(maxGainIndex) == key)
					splitDataSet.add(currNode);
			}
			splitSets.add(splitDataSet);		
		}

		
		for(int i = 0; i < splitSets.size(); i++) {
			ClassificationNode child = new ClassificationNode(splitSets.get(i), root.availableAttributes);
			generateDecisionTree(child);
			root.children.add(child);
		}
		
		root.splittingAttributeIndex = maxGainIndex;
		root.splittingAttributeValues = keys;
		//Set the nodes Attributes for splitting
		return;
	}
	
	public static boolean isLeafNode(ArrayList<Integer> availableAttributes) {
		return !availableAttributes.contains(1);
	}
	
	public static ArrayList<Hashtable<Integer, int[]>> sortAttributeData(
			ArrayList<DataNode> dataSet) {

		DataNode currNode = dataSet.get(0);
		int numOfAttr = currNode.attributes.size();
		ArrayList<Hashtable<Integer, int[]>> results = new ArrayList<Hashtable<Integer, int[]>>();

		for (int i = 0; i < numOfAttr; i++) {
			Hashtable<Integer, int[]> attrHash = new Hashtable<Integer, int[]>();
			
			for (int j = 0; j < dataSet.size(); j++) {
				currNode = dataSet.get(j);
				int attrVal = currNode.attributes.get(i);
				
				// Hash Value Exists
				if (attrHash.containsKey(attrVal)) {
					int[] hashVal = attrHash.get(attrVal);
					hashVal[0] += 1;
					
					if(currNode.givenClassification == 1)
						hashVal[1] += 1;
					
					attrHash.put(attrVal, hashVal);
				} 
				// Hash Value does not yet exist
				else {
					int[] hashVal = new int[2];
					hashVal[0] = 1;
					if(currNode.givenClassification == 1)
						hashVal[1] = 1;
					else
						hashVal[1] = 0;
					attrHash.put(attrVal, hashVal);
				}
			}
			results.add(attrHash);
		}

		return results;
	}
	
	/*************************************************************************************
	 * GainRaitio(A) Calculations ********************************************************
	 *************************************************************************************/
	
	public static double calculateGainRatio(ArrayList<Hashtable<Integer, int[]>> attributeData,
			int attributeIndex) {
		double gain = calcualteGainValue(attributeData, attributeIndex);
		double splitInfo = calculateSplitInfoValue(attributeData, attributeIndex);
		
		double gainRatio;
		if(splitInfo != 0) 
			gainRatio= gain / splitInfo;
		else
			gainRatio = 0;
		
		return gainRatio;
	}
	
	/*************************************************************************************
	 * SplitInfo(D) Calculations *********************************************************
	 *************************************************************************************/	
	
	public static double calculateSplitInfoValue(ArrayList<Hashtable<Integer, int[]>> attributeData,
			int attributeIndex) {
		double totalCount = sumOfAttributeCounts(attributeData);
		Hashtable<Integer, int[]> attrHash = attributeData.get(attributeIndex);
		Set<Integer> keys = attrHash.keySet();
		double summation = 0.0;
		
		for(int key: keys) {
			int[] hashVal = attrHash.get(key);
			
			double attrRatio = ((double) hashVal[0]) / totalCount;
			
			summation += -1 * attrRatio * (Math.log(attrRatio) / Math.log(2));
		}
		
		return summation;
	}
	
	/*************************************************************************************
	 * Gain(A) Calculations **************************************************************
	 *************************************************************************************/
	
	public static double calcualteGainValue(ArrayList<Hashtable<Integer, int[]>> attributeData,
			int attributeIndex) {
		
		double attributeInfoVal = calculateAttributeInfoValue(attributeData, attributeIndex);
		double infoVal = calculateInfoValue(attributeData);
		
		double gain = infoVal - attributeInfoVal;
		
		return gain;	
	}

	/*************************************************************************************
	 * InfoA(D) Calculations *************************************************************
	 *************************************************************************************/
	
	public static double calculateAttributeInfoValue(ArrayList<Hashtable<Integer, int[]>> attributeData,
			int attributeIndex) {
		double totalCount = sumOfAttributeCounts(attributeData);
		Hashtable<Integer, int[]> attrHash = attributeData.get(attributeIndex);
		Set<Integer> keys = attrHash.keySet();
		double summation = 0;
		
		for(int key : keys) {
			int[] hashVal = attrHash.get(key);
			
			int totalAttrCount = hashVal[0];
			double attrRatio = ((double) totalAttrCount) / totalCount;
			double attrPositiveRatio = ((double) hashVal[1]) / totalAttrCount;
			double attrNegativeRatio = 1 - attrPositiveRatio;
			
			summation += attrRatio * ( -1 * attrPositiveRatio * (Math.log(attrPositiveRatio) / Math.log(2))
					- attrNegativeRatio * (Math.log(attrNegativeRatio) / Math.log(2)));
		}
				
		return summation;
	}
	
	/*************************************************************************************
	 * Info(D) Calculations **************************************************************
	 *************************************************************************************/
	
	public static double calculateInfoValue(ArrayList<Hashtable<Integer, int[]>> attributeData) {
		double totalCount = sumOfAttributeCounts(attributeData);
		double totalPositiveCount = sumOfPositiveClassifications(attributeData);
		double summation = 0;
		
		double totalNegativeCount = totalCount - totalPositiveCount;
		double positiveRatio = (double)totalPositiveCount/totalCount;
		double negativeRatio = (double)totalNegativeCount/totalCount;
		
		summation += -1 * (positiveRatio) * (Math.log(positiveRatio) / Math.log(2));
		summation += -1 * (negativeRatio) * (Math.log(negativeRatio) / Math.log(2));
		
		return summation;
	}
	
	public static double sumOfPositiveClassifications(ArrayList<Hashtable<Integer, int[]>> attributeData) {
		double summation = 0;
		Hashtable<Integer, int[]> hash = attributeData.get(0);
		Set<Integer> keys = hash.keySet();
		
		for(int key: keys) {
			int[] hashVal = hash.get(key);
			summation += hashVal[1];
		}
		
		return summation;
	}
	
	public static double sumOfAttributeCounts( ArrayList<Hashtable<Integer, int[]>> attributeData) {
		Hashtable<Integer, int[]> attributeHash = attributeData.get(0);
		Set<Integer> keys = attributeHash.keySet();
		double summation = 0;

		for (int key : keys) {
			summation += (attributeHash.get(key))[0];
		}

		return summation;
	}
}