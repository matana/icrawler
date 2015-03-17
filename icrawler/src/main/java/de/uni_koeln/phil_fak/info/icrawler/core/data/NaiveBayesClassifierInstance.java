package de.uni_koeln.phil_fak.info.icrawler.core.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instances;

public class NaiveBayesClassifierInstance implements Serializable {
	
	/**
	 * The UID.
	 */
	private static final long serialVersionUID = -8432812479903467140L;

	private Classifier classifier;
	private Instances trainingSet;
	private TreeSet<Integer> docVector;
	private TreeSet<String> termVector;
	private HashMap<Integer, Map<String, Double>> docTermMatrix;
	private ArrayList<Attribute> listWekaAttributes;

	public NaiveBayesClassifierInstance(Classifier classifier, Instances trainingSet, 
			TreeSet<Integer> docVector, TreeSet<String> termVector, HashMap<Integer, 
			Map<String, Double>> docTermMatrix, ArrayList<Attribute> listWekaAttributes) {
		this.setTrainingSet(trainingSet);
		this.setClassifier(classifier);
		this.setDocVector(docVector);
		this.setTermVector(termVector);
		this.setDocTermMatrix(docTermMatrix);
		this.setListWekaAttributes(listWekaAttributes);
	}

	public Classifier getClassifier() {
		return classifier;
	}

	public void setClassifier(Classifier classifier) {
		this.classifier = classifier;
	}

	public Instances getTrainingSet() {
		return trainingSet;
	}

	public void setTrainingSet(Instances trainingSet) {
		this.trainingSet = trainingSet;
	}

	public TreeSet<Integer> getDocVector() {
		return docVector;
	}

	public void setDocVector(TreeSet<Integer> docVector) {
		this.docVector = docVector;
	}

	public TreeSet<String> getTermVector() {
		return termVector;
	}

	public void setTermVector(TreeSet<String> termVector) {
		this.termVector = termVector;
	}

	public HashMap<Integer, Map<String, Double>> getDocTermMatrix() {
		return docTermMatrix;
	}

	public void setDocTermMatrix(HashMap<Integer, Map<String, Double>> docTermMatrix) {
		this.docTermMatrix = docTermMatrix;
	}

	public ArrayList<Attribute> getListWekaAttributes() {
		return listWekaAttributes;
	}

	public void setListWekaAttributes(ArrayList<Attribute> listWekaAttributes) {
		this.listWekaAttributes = listWekaAttributes;
	}

}
