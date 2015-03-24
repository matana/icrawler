package de.uni_koeln.phil_fak.info.icrawler.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Set;

import weka.classifiers.Classifier;
import de.uni_koeln.phil_fak.info.icrawler.core.data.ClassifierInstance;
import de.uni_koeln.phil_fak.info.icrawler.core.data.WebDocument;
import de.uni_koeln.phil_fak.info.icrawler.service.NBClassifier;

public class ObjectWriter {
	
	public static final String RESULT_DIR = "results";
	public static final String CLASSIFIER_DIR = "classifier";
	public static final String SITES_DIR = "sites";
	
	public static void saveClassifier(ClassifierInstance classifierInstance) {

		File dir = getDir(CLASSIFIER_DIR);
		try {
			String file = classifierInstance.getClass().getSimpleName().toLowerCase() + ".classifier";
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(dir, file)));
			oos.writeObject(classifierInstance);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void write(Set<WebDocument> documents, final String siteName) {

		File dir = getDir(RESULT_DIR);
		try {
			String file = siteName + ".res";
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(dir, file)));
			oos.writeObject(documents);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeWebDocuments(Set<WebDocument> webDocuments) {
		File dir = getDir(SITES_DIR);
		try {
			String file = System.currentTimeMillis() + ".web";
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(dir, file)));
			oos.writeObject(webDocuments);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static File getDir(String name) {
		File dir = new File(name);
		dir.mkdir();
		return dir;
	}

	
	
}
