package de.uni_koeln.phil_fak.info.icrawler.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import de.uni_koeln.phil_fak.info.icrawler.core.Constants;
import de.uni_koeln.phil_fak.info.icrawler.core.Crawler;
import de.uni_koeln.phil_fak.info.icrawler.core.DocumentType;
import de.uni_koeln.phil_fak.info.icrawler.core.data.NaiveBayesClassifierInstance;
import de.uni_koeln.phil_fak.info.icrawler.core.data.WebDocument;
import de.uni_koeln.phil_fak.info.icrawler.core.parser.SPONWebDocumentParser;
import de.uni_koeln.phil_fak.info.icrawler.lucene.LuceneIndexGenerator;
import de.uni_koeln.phil_fak.info.icrawler.lucene.LuceneIndexManager;
import de.uni_koeln.phil_fak.info.icrawler.util.ObjectReader;
import de.uni_koeln.phil_fak.info.icrawler.util.ObjectWriter;

@Service("nbClassifier")
public class NBClassifier {

	private LuceneIndexManager indexManager;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private NaiveBayesClassifierInstance classifier;

	private ArrayList<String> topics;

	private ArrayList<Attribute> listWekaAttributes;

	public static void main(String[] args) throws Exception {
		new NBClassifier();
	}

	public NBClassifier() throws Exception {
		indexManager = LuceneIndexManager.getInstance();
		NaiveBayesClassifierInstance classifier = ObjectReader.getClassifier();
		if(classifier != null) {
			this.classifier = classifier;
			this.listWekaAttributes = classifier.getListWekaAttributes();
			logger.info("Classifier loaded... ");
		} else {
			buildNaiveBayes();
		}
	}

	public void buildNaiveBayes() throws Exception {
		indexManager.calculateTermVectors();
		listWekaAttributes = createTemplateStructure();
		// Map<Key( docId ), Value( instance )>
		Map<Integer, Instance> docIdInstanceMap = new HashMap<Integer, Instance>();
		
		TreeSet<Integer> docVector = indexManager.getDocVector();
		TreeSet<String> termVector = indexManager.getTermVector();
		HashMap<Integer, Map<String, Double>> docTermMatrix = indexManager.getDocTermMatrix();
		
		int max = (docVector.size() / 2); // Trainingsset besteht aus der Hälfte der Gesamtdokumente
		int count = 0;
		
		Instances trainingSet = new Instances("model", listWekaAttributes, max);
		trainingSet.setClassIndex(listWekaAttributes.size() - 1);
		
		Instances testSet = new Instances("testModel", listWekaAttributes, listWekaAttributes.size());
		testSet.setClassIndex(listWekaAttributes.size() - 1);

		convertToInatance(listWekaAttributes, docIdInstanceMap, docVector, max, count, trainingSet, testSet);

		// Baue und speichere den Klassifikator
		Classifier cModel = (Classifier) new NaiveBayes();
		cModel.buildClassifier(trainingSet);
		
		classifier = new NaiveBayesClassifierInstance(cModel, trainingSet, docVector, termVector, docTermMatrix, listWekaAttributes);
		ObjectWriter.saveClassifier(classifier);

//		und dann klassifizieren (Instance für Instance...)	
//		Set<Integer> keySet = docIdInstanceMap.keySet();
//		for (Integer docId : keySet) {
//
//			Instance instance = docIdInstanceMap.get(docId);
//			instance.setDataset(testSet);
//
//			double mostLikely = cModel.classifyInstance(instance);
//
//			logger.info("----------------------------------------");
//			logger.info("predicted class: " + mostLikely + ", class: " + topics.get((int) mostLikely));
//			logger.info("docID: " + docId + ", topicLabeledBySPON: " + indexManager.getTopic(docId));
//
//			double[] distributionForInstance = cModel.distributionForInstance(instance);
//			for (int i = 0; i < topics.size(); i++) {
//				logger.info(topics.get(i) + ": " + distributionForInstance[i]);
//			}
//		}
	}
	
	public void convertWekaInatance(Map<Integer, Instance> docIdInstanceMap, TreeSet<Integer> docVector) {
		for (Integer docId : docVector) {
			// Map<Key( term ), Value( tf/idf )>
			Map<String, Double> docVec = indexManager.getDocTermMatrix().get(docId);
			Instance instance = new SparseInstance(listWekaAttributes.size());
			for (int i = 0; i < listWekaAttributes.size(); i++) {
				String key = listWekaAttributes.get(i).name();
				Double value = docVec.get(key);
				instance.setValue(i, (value == null ? 0D : value));
			}
			String topic = indexManager.getTopic(docId);
			instance.setValue(listWekaAttributes.size() - 1, topic);
			docIdInstanceMap.put(docId, instance);
		}
	}

	private void convertToInatance(ArrayList<Attribute> listWekaAttributes, Map<Integer, Instance> docIdInstanceMap,
			TreeSet<Integer> docVector, int max, int count, Instances trainingSet, Instances testSet) {
		
		// docVecs in Instances umwandeln ...
		for (Integer docId : docVector) {
			
			// Map<Key( term ), Value( tf/idf )>
			Map<String, Double> docVec = indexManager.getDocTermMatrix().get(docId);

			// Set<String> docVecKeySet = docVec.keySet();

			// für jedes Dokument: Template mit Werten füllen
			Instance instance = new SparseInstance(listWekaAttributes.size());
			for (int i = 0; i < listWekaAttributes.size(); i++) {
				String key = listWekaAttributes.get(i).name();
				Double value = docVec.get(key);
				instance.setValue(i, (value == null ? 0D : value));
			}
			// ... und am Ende die Klasse festlegen und zu einem dataset zufügen
			String topic = indexManager.getTopic(docId);
			
			if (count <= max) {
				logger.info("docId :: " + docId + ", topic :: " + topic);
				instance.setDataset(trainingSet);
				instance.setValue(listWekaAttributes.size() - 1, topic);
				trainingSet.add(instance);
			}else{
				instance.setDataset(testSet);
				// instance.setValue(listWekaAttributes.size() - 1, null);
				testSet.add(instance);
			}
			docIdInstanceMap.put(docId, instance);
			count++;
		}
	}

	/**
	 * Struktur/ Template eines Dokumentvektors
	 * @return
	 */
	private ArrayList<Attribute> createTemplateStructure() {
		List<String> asList = new ArrayList<String>(indexManager.getTermVector());
		ArrayList<Attribute> listWekaAttributes = new ArrayList<Attribute>();
		for (String term : asList) {
			// Attribute sind alle Terme
			Attribute attr = new Attribute(term);
			listWekaAttributes.add(attr);
		}
		
		// Letztes Element ist das 'Klassenattribut' (das Feld für die Klasse/Topic)
		topics = new ArrayList<String>(Arrays.asList(SPONWebDocumentParser.topicArray));
		Attribute classAttribute = new Attribute("classAttribute", topics);
		listWekaAttributes.add(classAttribute);
		return listWekaAttributes;
	}

	public void classifyEntry(final String url) throws Exception {
		Set<WebDocument> documents = Crawler.crawl(Arrays.asList(url), 0, DocumentType.UNKNOWN_DOCUMENT);
		for (WebDocument webDocument : documents) {
			logger.info("document to be classified :: " + webDocument.getUrl());
		}
		
		LuceneIndexGenerator generator = LuceneIndexGenerator.getInstance();
		
		try {
			int docID = indexManager.findByUrl(url);
			if(docID < 0) {
				generator.index(documents, null);
				docID = indexManager.findByUrl(url);
			}
			
			Map<String, Double> docVec = indexManager.calculateSingleTermVector(docID, classifier.getTermVector());
			Instance instance = convertWekaInatance(docVec);
			
			instance.setDataset(classifier.getTrainingSet());
			
			double[] distributionForInstance = classifier.getClassifier().distributionForInstance(instance);
			
			for (int i = 0; i < distributionForInstance.length; i++) {
				logger.info("Topic :: " + SPONWebDocumentParser.topicArray[i] + " > " + distributionForInstance[i]);
			}				
			
			//Map<String, Double> termVectorForDocId = indexManager.getTfIdf(docID, DocumentType.UNKNOWN_DOCUMENT);
//			TreeSet<Integer> docVector = indexManager.getDocVector();
//			TreeSet<String> termVector = indexManager.getTermVector();
//			Integer next = indexManager.getDocTermMatrix().keySet().iterator().next();
//			int size = indexManager.getDocTermMatrix().get(next).keySet().size();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Instance convertWekaInatance(Map<String, Double> docVec) {
		Instance instance = new SparseInstance(listWekaAttributes.size());
		for (int i = 0; i < listWekaAttributes.size(); i++) {
			String key = listWekaAttributes.get(i).name();
			Double value = docVec.get(key);
			instance.setValue(listWekaAttributes.get(i), (value == null ? 0D : value));
		}
		return instance;
	}
}
