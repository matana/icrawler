package de.uni_koeln.phil_fak.info.icrawler.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import de.uni_koeln.phil_fak.info.icrawler.core.Constants;
import de.uni_koeln.phil_fak.info.icrawler.core.Crawler;
import de.uni_koeln.phil_fak.info.icrawler.core.DocumentType;
import de.uni_koeln.phil_fak.info.icrawler.core.data.RequestData;
import de.uni_koeln.phil_fak.info.icrawler.core.data.WebDocument;
import de.uni_koeln.phil_fak.info.icrawler.service.NBClassifier;
import de.uni_koeln.phil_fak.info.icrawler.service.SearchService;
import de.uni_koeln.phil_fak.info.icrawler.util.ObjectReader;

@Controller
public class HomeController {

	Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
	
	@Autowired
	private SearchService searchService;
	
	@Autowired
	private NBClassifier nbClassifier;

	private ModelMap queryResultMap;

	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("Serving home.jsp");
		ModelAndView mv = new ModelAndView("home");
		if(queryResultMap != null) 
			if(!queryResultMap.isEmpty())
				mv.addAllObjects(queryResultMap);
		return mv;
	}
	
	@ModelAttribute("docTypes")
	public Map<String, String> types(){
		Map<String, String> map = new HashMap<String, String>();
		for (DocumentType type : DocumentType.values()) {
			map.put(type.name(), type.getName());
		}
		return map;
	}
	
	@RequestMapping(value = { "/classify" }, method = RequestMethod.POST)
	@ResponseBody
	public String classify(@RequestParam("site") String site, HttpServletRequest request, HttpServletResponse response)  {
		logger.info("Classifying entry from: " + site);
		try {
			String host1 = new URL(site).getHost();
			String host2 = new URL(Constants.SPON_ROOT_URL).getHost();
			
			if(host1.equals(host2))
				nbClassifier.classifyEntry(site, DocumentType.SPON_DOCUMENT);
			else
				nbClassifier.classifyEntry(site, DocumentType.UNKNOWN_DOCUMENT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "home";
	}
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.POST)
	@ResponseBody
	public String search(@RequestBody String query, HttpServletRequest request,
			HttpServletResponse response, ModelMap queryResultMap) throws JsonParseException, JsonMappingException, IOException {
		List<WebDocument> results = searchService.search(query);
		queryResultMap.addAttribute("results", results);
		queryResultMap.addAttribute("query", query);
		this.queryResultMap = queryResultMap;
		return "home";
	}

	@RequestMapping(value = { "/crawl" }, method = RequestMethod.POST)
	@Async
	public void crawl(@RequestBody String requestData, HttpServletRequest request,
			HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		RequestData data = mapper.readValue(requestData, RequestData.class);
		String url = data.getUrl();
		DocumentType type = DocumentType.valueOf(data.getType());
		logger.info("url : " + url + ", type : " + type.name());
		if(type == null)
			Crawler.crawl(toList(url), 1, DocumentType.SPON_DOCUMENT, true);
		else 
			Crawler.crawl(toList(url), 1, type, true);
	}

	@RequestMapping(value = { "/latestResults" }, method = RequestMethod.GET)
	public ModelAndView getLatestResults(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("results");
		Set<WebDocument> docs = ObjectReader.readResultsFor("spiegel");
		mv.addObject("docs", docs);
		return mv;
	}

	@RequestMapping(value = { "/allSites" }, method = RequestMethod.GET)
	public ModelAndView getallSites(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("sites");
		Set<WebDocument> sites = ObjectReader.readAllSites();
		mv.addObject("sites", sites);
		return mv;
	}

	private List<String> toList(String urls) {
		String[] toCrawl = urls.split(",");
		for (int i = 0; i < toCrawl.length; i++) {
			String url = toCrawl[i].trim();
			if (!url.toLowerCase().matches("http://.*")) {
				url = "http://" + url;
			}
			toCrawl[i] = url;
		}
		return Arrays.asList(toCrawl);
	}

	
	
}
