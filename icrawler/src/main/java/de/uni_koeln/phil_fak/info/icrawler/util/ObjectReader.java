package de.uni_koeln.phil_fak.info.icrawler.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.phil_fak.info.icrawler.core.data.NaiveBayesClassifierInstance;
import de.uni_koeln.phil_fak.info.icrawler.core.data.WebDocument;
import de.uni_koeln.phil_fak.info.icrawler.util.comparator.FileDateDescComaparator;

public class ObjectReader {
	
	private static Logger logger = LoggerFactory.getLogger(ObjectReader.class);
	
	public static void main(String[] args) {
		Set<WebDocument> readResultsFor = ObjectReader.readResultsFor("spiegel");
		logger.info("readResultsFor.size: " + readResultsFor.size());
		Set<WebDocument> newSet = new HashSet<WebDocument>();
		newSet.addAll(readResultsFor);
		logger.info("newSet.size: " + newSet.size());
	}
	
	public static NaiveBayesClassifierInstance getClassifier() {

		NaiveBayesClassifierInstance classifierInstance = null;
		File file = new File(ObjectWriter.CLASSIFIER_DIR, NaiveBayesClassifierInstance.class.getSimpleName().toLowerCase() + ".classifier");
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			classifierInstance = (NaiveBayesClassifierInstance) ois.readObject();
			ois.close();
			return classifierInstance;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Set<WebDocument> readResultsFor(final String siteName) {
		
		File file = getFileFor(siteName);
		if(file == null)
			file = getLastCreated();
		
		Set<WebDocument> docs = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			docs = (Set<WebDocument>) ois.readObject();
			ois.close();
			return docs;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static File getFileFor(String siteName) {
		File[] files = new File(ObjectWriter.RESULT_DIR).listFiles();
		for (File file : files) {
			if(file.toString().contains(siteName))
				return file;
		}
		return null;
	}

	public static Set<WebDocument> readAllSites() {
		Set<WebDocument> docs = new HashSet<WebDocument>();
		try {
			File dir = new File(ObjectWriter.SITES_DIR);
			dir.mkdir();
			List<File> files  = Arrays.asList(dir.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.getName().endsWith(".web");
				}
			}));
			for (File file : files) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				WebDocument doc = (WebDocument) ois.readObject();
				docs.add(doc);
				ois.close();
			}
			return docs;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static File getLastCreated() {
		File dir = new File(ObjectWriter.RESULT_DIR);
		dir.mkdir();
		return getLastCreated(dir);
	}

	private static File getLastCreated(File dir) {
		List<File> files = Arrays.asList(dir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".res");
			}
		}));
		
		if(files.size() > 0) {
			Collections.sort(files, new FileDateDescComaparator());
		}
		
		return files.get(0);
	}

}
