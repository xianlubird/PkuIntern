package com.intern.fetch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 爬取制定URL的内容
 * 
 * @author bird Apr 3, 2015 4:05:21 PM
 */
@Component
public class FetchPage {
	
	private Logger logger = LoggerFactory.getLogger(FetchPage.class);

	/**
	 * 根据提供的URL返回抓取的Page
	 * @param url
	 * @return
	 */
	public HtmlPage fetchPage(String url) {
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		try {
			return webClient.getPage(url);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}finally {
			webClient.closeAllWindows();
		}
		return null; 
	}
}
