package com.intern.fetch.pku;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.intern.bean.PkuBean;
import com.intern.fetch.FetchPage;

/**
 * 处理抓取北大未名BBS实习信息
 * @author bird
 * Apr 3, 2015 11:25:05 PM
 */
@Component
public class PkuFetch {
	
	@Autowired
	private FetchPage fetch;
	
	@Autowired
	private MongoTemplate mongo;
	
	/**
	 * 获取当前URL页面的所有标题和URL
	 * @param url
	 */
	public void getTitleInfo(String url) {
		HtmlPage page = fetch.fetchPage(url);
		DomNodeList<DomElement> elements = page.getElementsByTagName("table");
		//获取所有的table
		DomElement targetElement = null;
		for (DomElement item: elements) {
			if ("body".equals(item.getAttribute("class"))) {
				targetElement = item;
			}
		}
		//获取所有table下面的tr标签
		DomNodeList<HtmlElement> trElement = targetElement.getElementsByTagName("tr");
		for (HtmlElement element: trElement) {
			//过滤掉置顶帖子
			if (element.getTextContent().contains("置顶") || element.getTextContent().contains("序号")) {
				continue;
			}
			//下面就是我们需要的内容
			DomNodeList<HtmlElement> tdElement = element.getElementsByTagName("td");
			PkuBean bean = new PkuBean();
			for (HtmlElement td: tdElement) {
				DomNodeList<HtmlElement> spanElement = td.getElementsByTagName("span");
				for (HtmlElement tempElement: spanElement) {
					if (tempElement.getAttribute("class").matches("col3.")) {
						//这里获取的是每个帖子的发帖时间
						bean.setTime(tempElement.getTextContent());
					//	System.out.println(tempElement.getTextContent());
					}
					
				} 
				
				
				DomNodeList<HtmlElement> aElement = td.getElementsByTagName("a");
				for (HtmlElement tempElement: aElement) {
					if (tempElement.getAttribute("href").contains("bbscon")) {
						//这里是每个帖子的Title，需要去掉Re:
						String title = tempElement.getTextContent();
						title = title.replaceAll("Re:", "");
						bean.setTitle(title);
					//	System.out.println(tempElement.getTextContent());
						//这里是每个帖子的具体URL
						bean.setUrl(tempElement.getAttribute("href"));
					//	System.out.println(tempElement.getAttribute("href"));
					}
				}
			}
			mongo.insert(bean);
		}
	}
	
	public static void main(String[] args) {
		PkuFetch fetch = new PkuFetch();
		fetch.getTitleInfo("http://www.bdwm.net/bbs/bbsdoc.php?board=intern");
	}
}
