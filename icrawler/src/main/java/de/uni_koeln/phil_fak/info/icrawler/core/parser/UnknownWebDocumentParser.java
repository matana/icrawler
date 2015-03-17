package de.uni_koeln.phil_fak.info.icrawler.core.parser;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.phil_fak.info.icrawler.core.DocumentType;
import de.uni_koeln.phil_fak.info.icrawler.core.data.WebDocument;

public class UnknownWebDocumentParser extends WebDocumentParser {

	private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
	
	@Override
	public WebDocument process(Document node, final String url) {
		
		WebDocument document = new WebDocument();
		Set<String> links = new HashSet<String>();
		
		// Grab all anchors from the given html document 
		Elements anchors = node.select("a[href]");
		for (Element a : anchors) {
			String anchor = a.attr("abs:href");
			if (!anchor.contains("?") && !anchor.contains("#")) {
					boolean add = links.add(anchor);
			}
		}
		
		String title = "";
		title = node.select("title").text();
		if(title != null) {
			document.setTitle(title);
		}
		
		
		removeStyleAndJavaScriptTags(node);
		
		String content = getContentForRegex(node, "div[class~=entry-content|post-body|main-post|main-entry");
		document.setContent(content);
		
		content = getContentForRegex(node, "div[class~=intro-content|post-intro|intro");
		document.setIntro(content);
		
		try {
			
			document.setTopic(DocumentType.UNKNOWN_DOCUMENT.getName());
			document.setLinks(links);
			document.setDate(System.currentTimeMillis() + "");
			document.setUrl(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return document;
	}

	private String getContentForRegex(Document node, final String regex) {
		StringBuilder contentBuilder = new StringBuilder();
		Elements content = node.select(regex);
		Set<String> contentSet = new HashSet<String>();
		for (Element element : content) {
			if(element.hasText()) {
				boolean add = contentSet.add(element.text());
				if(add) {
					contentBuilder.append(element.text());
				}
			}
		}
		return contentBuilder.toString();
	}

}
