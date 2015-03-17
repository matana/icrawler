package de.uni_koeln.phil_fak.info.icrawler.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.uni_koeln.phil_fak.info.icrawler.core.data.QueryData;
import de.uni_koeln.phil_fak.info.icrawler.core.data.WebDocument;
import de.uni_koeln.phil_fak.info.icrawler.lucene.LuceneIndexManager;

@Service
public class SearchService {
	
	Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
	
	LuceneIndexManager manager = LuceneIndexManager.getInstance();
	
	private static Map<String, String> topics;
	
	static {
		topics = new HashMap<String, String>();
		topics.put("1", "all");
		topics.put("2", "panorama");
		topics.put("3", "politik");
		topics.put("4", "kultur");
		topics.put("5", "wirtschaft");
		topics.put("6", "netzwelt");
		topics.put("7", "wissenschaft");
		topics.put("8", "sport");
	}
	
	public List<WebDocument> search(String query){
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			QueryData data = mapper.readValue(query, QueryData.class);
			String[] searchPhrase = data.getSearchPhrase().trim().split(" ");
			String topic = topics.get(data.getTopic());
			boolean all = topic.equals("all");
			
			Set<WebDocument> resultsSet = manager.searchFor(searchPhrase, topic, all);
			List<WebDocument> retultsList = new ArrayList<WebDocument>(resultsSet);
			
			Collections.sort(retultsList, new Comparator<WebDocument>() {
				@Override
				public int compare(WebDocument o1, WebDocument o2) {
					return o1.getTopic().compareTo(o2.getTopic());
				}
			});
			
			return retultsList;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
