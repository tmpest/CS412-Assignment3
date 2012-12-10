package classification;

import java.util.ArrayList;
import java.util.Set;

public class ClassifierNode {

	public ArrayList<DataNode> dataSet;
	public ArrayList<ClassifierNode> children;
	public int splitingAttribute;
	public Set<Integer> splitingAttributeValues;
	public int classifierValue = 0;
	
}
