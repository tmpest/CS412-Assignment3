package other_packages;

import java.util.ArrayList;

public class ClassificationNode {
	
	public final ArrayList<DataNode> dataSet;
	public final ArrayList<ClassificationNode> children;
	public final ArrayList<Integer> availableAttributes; // Integer Flag, boolean not a valid option
	
	public ClassificationNode() {
		dataSet = new ArrayList<DataNode>();
		children = new ArrayList<ClassificationNode>();
		availableAttributes = new ArrayList<Integer>();
	}
}
