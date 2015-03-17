package de.uni_koeln.phil_fak.info.icrawler.core.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.phil_fak.info.icrawler.core.data.WebDocument;

public class SPONWebDocumentParser extends WebDocumentParser {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final String TAB = "\t";
	private static final String NEW_LINE = "\n";
	private static final String ROOT_URL = "http://www.spiegel.de/";
	
	public static String[] topicArray = {
			"politik",
			"panorama",
			"kultur",
			"wirtschaft",
			"sport",
			"netzwelt",
			"karriere",
			"wissenschaft"
	};
	
	Set<String> validTopics = new HashSet<String>(Arrays.asList(topicArray));

	public WebDocument process(Document node, final String url) {
		
		WebDocument document = new WebDocument();
		Set<String> links = new HashSet<String>();
		
		
		// Grab the title
		String title = node.select(".article-title").text();
		if(title != null)
			document.setTitle(title);
		//logger.info("title: " + title);
		
		// Grab the intro
		String intro = node.select(".article-intro").text();
		if(intro != null)
			document.setIntro(intro);
		//logger.info("intro: " + intro);
		
		// Grab the news article
//		StringBuilder builder = new StringBuilder();
//		Elements paragraphs = node.select(".article-section").select("p");
//		for (Element paragraph : paragraphs) {
//			 String text = paragraph.text().trim();
//			 if (text.length() > 0) 
//				 builder.append(text).append(NEW_LINE);
//		}
//		String content = builder.toString();
		String content = node.select(".article-section").text();
		if(content != null && !content.equals("") && content.length() > 500)
			document.setContent(content);
		else
			document.setContent(null);
		//logger.info("content: " + content);
		
		// Grab all anchors from the given html document 
		Elements anchors = node.select("a[href]");
		for (Element a : anchors) {
			String anchor = a.attr("abs:href");
			if (!anchor.contains("?") && !anchor.contains("#")) {
				if(isNative(anchor)) {
					boolean add = links.add(anchor);
//					if(add)
//						logger.info("anchor: " + anchor);
				}
			}
		}
		
		try {
			document.setTopic(getTopic(url, node));
			//document.setLinks(LinkHelper.checked(url, links));
			document.setLinks(links);
			document.setUrl(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return document;
	}

	private boolean isNative(final String anchor) {
		try {
			if(anchor.isEmpty())
				return false;
			String host1 = new URL(anchor).getHost();
			String host2 = new URL(ROOT_URL).getHost();
			if(host1.equals(host2))
				return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private String getTopic(final String url, Document node) throws MalformedURLException {
		
		URL urlObj = new URL(url);
		String path = urlObj.getPath();
		if(!path.equals("")) {
			path = path.substring(1, urlObj.getPath().length());
			String topic = path.split("/")[0];
			String channelName = node.select(".channel-name").attr("href").replaceAll("/", "");
			
			if(channelName != null) {
				if(topic.equalsIgnoreCase(channelName) && validTopics.contains(topic))
					return topic;
				else if(validTopics.contains(channelName))
						return channelName;
				} else {
					return "unknow";
			}
		}
		
		return "unkown";
	}

	
}
