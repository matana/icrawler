package de.uni_koeln.phil_fak.info.icrawler.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class LinkHelper {

	public static Set<String> checked(final String url, Set<String> links)
			throws MalformedURLException {
		return normalized(url, links);
	}

	private static Set<String> normalized(final String url, Set<String> links)
			throws MalformedURLException {

		String newUrl = removeFile(url);
		Set<String> result = new HashSet<String>();

		for (String link : links) {
			result.add(normalized(newUrl, link));
		}

		return result;
	}

	private static String removeFile(String s) {
		try {
			URL url = new URL(s);
			String path = url.getPath();
			if (path.contains(".")) {
				s = url.getProtocol() + "://" + url.getHost()
						+ path.substring(0, path.lastIndexOf("/") + 1);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return s;
	}

	private static String normalized(final String url, final String link) {
		if (link.startsWith("http://")) {
			return link;
		} else
			return url + fixSlashes(url, link);
	}

	private static String fixSlashes(final String url, String link) {
		if (url.endsWith("/") && link.startsWith("/"))
			link = link.substring(1);
		if (!url.endsWith("/") && !link.startsWith("/"))
			link = "/" + link;
		return link;
	}

}
