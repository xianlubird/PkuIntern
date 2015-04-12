package com.intern.fetch.pku;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.intern.bean.NewsmthBean;
import com.intern.fetch.FetchPage;

/**
 * 抓取水木社区的帖子
 * @author bird
 * Apr 9, 2015 5:37:50 PM
 */
@Component
public class NewsmthFetch {
	
	private Logger logger = LoggerFactory.getLogger(NewsmthFetch.class);
	
	@Autowired
	private MongoTemplate mongo;
	
	@Autowired
	private FetchPage fetch;
	
	public void fetchPage(String url) {
		HtmlPage page = fetch.fetchPageEnableJS(url);
		DomNodeList<DomElement> divElements = page.getElementsByTagName("div");
		DomElement targetElement = null;
		for (DomElement element: divElements) {
			if ("doc".equals(element.getAttribute("class"))) {
				targetElement = element;
				break;
			}
		}
		
		DomNodeList<HtmlElement> trElement = targetElement.getElementsByTagName("tr");
		for (HtmlElement element: trElement) {
			NewsmthBean bean = new NewsmthBean();
			DomNodeList<HtmlElement> tdElement = element.getElementsByTagName("td");
			for (HtmlElement tempElement: tdElement) {
				//寻找发帖时间
				DomNodeList<HtmlElement> timeElement = tempElement.getElementsByTagName("nobr");
				String time = null;
				if (timeElement.size() != 0) {
					time = timeElement.get(0).getTextContent();
					bean.setTime(time);
					System.out.println("time" + time);
				}
				
				//寻找帖子标题
				DomNodeList<HtmlElement> aElements = tempElement.getElementsByTagName("a");
				for (HtmlElement aTempElement: aElements) {
					if (aTempElement.getAttribute("href").contains("bbscon")) {
						String title = aTempElement.getTextContent();
						String href = aTempElement.getAttribute("href");
						bean.setTitle(title);
						bean.setHref(href);
						System.out.println("title" + title);
						System.out.println("href" + href);
					}
				}
			}
			mongo.save(bean);
		}
	}
	
	public static void main(String[] args) {
		NewsmthFetch fetch = new NewsmthFetch();
		fetch.fetchPage("http://www.newsmth.net/bbsdoc.php?board=Intern");
	}
}
