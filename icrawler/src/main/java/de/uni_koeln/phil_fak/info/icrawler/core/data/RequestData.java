package de.uni_koeln.phil_fak.info.icrawler.core.data;


public class RequestData {
	
	private String url;
	private String type;
	
	public RequestData() {
	}
	
	public RequestData(String url, String type) {
		this.url = url;
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}
