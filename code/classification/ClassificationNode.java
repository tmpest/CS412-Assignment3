package classification;

import java.util.ArrayList;
import java.util.Set;

public class ClassificationNode {
	
	public final ArrayList<DataNode> dataSet;
	public ArrayList<ClassificationNode> children;
	public ArrayList<Integer> availableAttributes; // Integer Flag, boolean not a valid option
	public int splittingAttributeIndex = -1;
	public Set<Integer> splittingAttributeValues = null;
	public int classifierValue = 0;
	
	
	public ClassificationNode() {
		dataSet = new ArrayList<DataNode>();
		children = new ArrayList<ClassificationNode>();
		availableAttributes = new ArrayList<Integer>();
	}
	
	public ClassificationNode(ArrayList<DataNode> dataSet,
			ArrayList<Integer> availableAttributes) {
		this.dataSet = dataSet;
		this.availableAttributes = availableAttributes;
		children = new ArrayList<ClassificationNode>();
	}
	
	public boolean isLeaf() {
		return (children.size() == 0);
	}
}

