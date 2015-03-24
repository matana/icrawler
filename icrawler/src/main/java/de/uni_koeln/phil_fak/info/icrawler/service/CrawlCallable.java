package de.uni_koeln.phil_fak.info.icrawler.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.phil_fak.info.icrawler.core.DocumentType;
import de.uni_koeln.phil_fak.info.icrawler.core.data.WebDocument;
import de.uni_koeln.phil_fak.info.icrawler.core.parser.SDWebDocumentParser;
import de.uni_koeln.phil_fak.info.icrawler.core.parser.SPONWebDocumentParser;
import de.uni_koeln.phil_fak.info.icrawler.core.parser.UnknownWebDocumentParser;
import de.uni_koeln.phil_fak.info.icrawler.core.parser.WebDocumentParser;

public class CrawlCallable implements Callable<Set<WebDocument>> {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	private String url;
	private int depth;
	private Set<WebDocument> result;
	private DocumentType type;
	
	private WebDocumentParser webDocumentParser;

	private Set<String> crawledLinks = new HashSet<String>();

	public CrawlCallable(String url, int depth, Set<WebDocument> result, DocumentType type) {
		this.url = url;
		this.depth = depth;
		this.result = result;
		this.type = type;
	}

	public Set<WebDocument> call() throws Exception {
		initParser();
		crawl(url, 0);
		return result;
	}

	private void initParser() {
		if (webDocumentParser == null) {
			switch (type) {
			case SPON_DOCUMENT:
				webDocumentParser = new SPONWebDocumentParser();
				logger.info("SPONWebDocumentParser is used.");
				break;
			case SD_DOCUMENT:
				webDocumentParser = new SDWebDocumentParser();
				logger.info("SDWebDocumentParser is used.");
				break;
			case UNKNOWN_DOCUMENT:
				webDocumentParser = new UnknownWebDocumentParser();
				logger.info("UnknownWebDocumentParser is used.");
				break;
			default:
				logger.info("Something went wrong...");
				break;
			}
		}
	}

	private void crawl(String url, int current) throws InterruptedException {
		logger.info("Crawler crawling url :: " + url);
		try {
			WebDocument document = webDocumentParser.parse(url);
			
			if (document != null) {
				if (!document.getTopic().equals("unkown")
						&& document.getContent() != null) {
					result.add(document);
					logger.info("---------------------");
					logger.info("url: " + document.getUrl());
					logger.info("title: " + document.getTitle());
					logger.info("intro: " + document.getIntro());
					logger.info("content: " + document.getContent());
				}
				Thread.sleep(1000);
				if (current < depth) {
					current++;
					Set<String> links = document.getLinks();
					for (String link : links) {
						crawl(link, current);
					}
				}
				logger.info("---------------------");
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	private void removeAll(Set<String> crawledLinks2) {
		// TODO Auto-generated method stub
		
	}

}
