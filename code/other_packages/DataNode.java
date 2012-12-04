package other_packages;

import java.util.ArrayList;

public class DataNode {

	public final int givenClassification;
	public final ArrayList<Integer> attributes;
	public final int decidedClassification;
	
	public DataNode(int givenClassification, ArrayList<Integer> attributes) {
		this.givenClassification = givenClassification;
		this.attributes = attributes;
		decidedClassification = 0;
	}
	
	public DataNode(int givenClassification) {
		this.givenClassification = givenClassification;
		attributes = new ArrayList<Integer>();
		decidedClassification = 0;
	}
	
	public int getAttribute(int index) {
		return attributes.get(index);
	}
	
	public void addAttribute(int attribute) {
		attributes.add(attribute);
	}
	
}
