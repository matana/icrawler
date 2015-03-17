package de.uni_koeln.phil_fak.info.icrawler.core.data;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "webDocument")
@XmlType(propOrder = { "topic", "title", "intro", "content", "url", "date", "links" })
public class WebDocument implements Serializable, Comparable<WebDocument>{

	private static final long serialVersionUID = -9145110081558918224L;

	private String title;
	private String intro;
	private String content;

	@XmlElementWrapper(name = "extlinks")
	@XmlElement(name = "link")
	private Set<String> extLinks;

	private URL url;
	private Date date;
	private String topic;

	public WebDocument() {
		this.date = new Date();
	}

	public Date getDate() {
		return date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Set<String> getLinks() {
		return extLinks;
	}

	public void setLinks(Set<String> links) {
		this.extLinks = links;
	}

	public String getUrl() {
		return url.toString();
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((topic == null) ? 0 : topic.hashCode());
		result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebDocument other = (WebDocument) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (topic == null) {
			if (other.topic != null)
				return false;
		} else if (!topic.equals(other.topic))
			return false;
		if (getUrl() == null) {
			if (other.getUrl() != null)
				return false;
		} else if (!getUrl().equals(other.getUrl()))
			return false;
		return true;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public void setUrl(final String url) throws MalformedURLException {
		this.url = new URL(url);
	}

	@Override
	public String toString() {
		return "WebDocument [TOPIC=" + topic + ", TITLE=" + title + ", INTRO="
				+ intro.substring(0, Math.min(100, intro.length())) + "..."
				+ ", CONTENT="
				+ content.substring(0, Math.min(100, content.length())) + "..."
				+ ", URL=" + url + ", DATE=" + date + "]";
	}

	public void setDate(String date) {
		this.date = new Date(new Long(date));
	}

	@Override
	public int compareTo(WebDocument o) {
		return this.getUrl().compareTo(o.getUrl());
	}
}
