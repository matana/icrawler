package de.uni_koeln.phil_fak.info.icrawler.core.parser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import de.uni_koeln.phil_fak.info.icrawler.core.data.WebDocument;

public abstract class WebDocumentParser {

	public WebDocument parse(final String url) throws URISyntaxException, JAXBException, IOException {
		Document document = getDocument(url);
		if(document == null) 
			return null;
		else
			return process(document, url);
	}

	public abstract WebDocument process(Document node, final String url);

	private Document getDocument(final String url) throws IOException {
		URL tmpUrl = new URL(url);
		if(tmpUrl.getHost() != null) {
			Document document = Jsoup
					.connect(url)
					.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
					.timeout(10 * 1000)
					.get();
			return document;
		}
		return null;
	}

	public void removeStyleAndJavaScriptTags(Document doc) {
		doc.getElementsByTag("style").remove();
		doc.getElementsByTag("script").remove();
		doc.getElementsByTag("noscript").remove();
		doc.getElementsByTag("form").remove();
	}

}
