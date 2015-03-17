package de.uni_koeln.phil_fak.info.icrawler.core;

public enum DocumentType {
	
	SD_DOCUMENT("süddeutsche"), SPON_DOCUMENT("spiegel"), UNKNOWN_DOCUMENT("unknown");
	
	private String name;

	DocumentType(final String name) {
		setName(name);
	}
	
	public String getName() {
		return new String(name);
	}
	
	private void setName(final String name) {
		this.name = name;
	}

	public String toString() {
		return "Document type is " + name();
	}

}
