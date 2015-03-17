package de.uni_koeln.phil_fak.info.icrawler.core.data;

public class QueryData {
	
	private String searchPhrase;
	
	private String topic;
	
	public QueryData() {
	}
	
	public QueryData(String searchPhrase, String topic) {
		this.searchPhrase = searchPhrase;
		this.topic = topic;
	}

	public String getSearchPhrase() {
		return searchPhrase;
	}

	public void setSearchPhrase(String searchPhrase) {
		this.searchPhrase = searchPhrase;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	

}
