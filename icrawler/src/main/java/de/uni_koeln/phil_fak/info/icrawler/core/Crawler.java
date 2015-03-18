package de.uni_koeln.phil_fak.info.icrawler.core;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.phil_fak.info.icrawler.core.data.WebDocument;
import de.uni_koeln.phil_fak.info.icrawler.lucene.LuceneIndexGenerator;
import de.uni_koeln.phil_fak.info.icrawler.lucene.LuceneIndexManager;
import de.uni_koeln.phil_fak.info.icrawler.util.ObjectReader;
import de.uni_koeln.phil_fak.info.icrawler.util.ObjectWriter;

public class Crawler {

	private static Logger LOGGER = LoggerFactory.getLogger(Crawler.class);

	public static Set<WebDocument> crawl(List<String> seed, int depth, DocumentType type, boolean b) {

		long start = System.currentTimeMillis();
		
		Set<WebDocument> readResultsFor = ObjectReader.readResultsFor("spiegel");

		Set<WebDocument> result = Collections.synchronizedSet(new HashSet<WebDocument>());

		ExecutorService exec = Executors.newCachedThreadPool();

		for (String url : seed) {

			Future<Set<WebDocument>> future = exec.submit(new CrawlCallable(url, depth, result, type));

			try {
				Set<WebDocument> documents = future.get();
				LOGGER.info("Creating SiteDocument object with param :: Set<WebDocument> of size = " + documents.size());
				ObjectWriter.writeWebDocuments(documents);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		exec.shutdown();

		try {
			boolean terminated = exec.awaitTermination(5, TimeUnit.DAYS);
			LOGGER.info("Done in time limit: " + terminated);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long took = System.currentTimeMillis() - start;
		LOGGER.info(String.format("Crawling %s documents took %s ms. (~%s s.)", result.size(), took, took / 1000));
		
		if(b) {
			writeToDocument(result, readResultsFor);
			LuceneIndexManager manager = LuceneIndexManager.getInstance();
			Set<WebDocument> indexable = new HashSet<WebDocument>();
			for (WebDocument webDocument : result) {
				if(manager.findByUrl(webDocument.getUrl()) < 0) {
					LOGGER.info("document is indexable :: " + webDocument.getUrl());
					indexable.add(webDocument);
				}
			}
			if(indexable.size() > 0) {
				LuceneIndexGenerator indexer = LuceneIndexGenerator.getInstance();
				try {
					LOGGER.info(indexable.size() + " documents to be indexed...");
					indexer.index(result, null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				LOGGER.info("Nothing to index...");
			}
		}
		
		
		return result;
	}

	private static Set<WebDocument> toBeIndexed(Set<WebDocument> result, Set<WebDocument> readResultsFor) {
		Set<WebDocument> toBeIndexed = new HashSet<WebDocument>();
		for (WebDocument doc : result) {
			if(readResultsFor.contains(doc))
				toBeIndexed.add(doc);
		}
		return toBeIndexed;
	}

	private static void writeToDocument(Set<WebDocument> result,
			Set<WebDocument> readResultsFor) {
		readResultsFor.addAll(result);
		LOGGER.info("Documents size: " + readResultsFor.size());
		ObjectWriter.write(readResultsFor, "spiegel");
	}

}
