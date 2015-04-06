package com.intern.fetch.pku;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.intern.fetch.FetchPage;

/**
 * 处理抓取北大未名BBS实习信息
 * @author bird
 * Apr 3, 2015 11:25:05 PM
 */
public class PkuFetch {
	
	/**
	 * 获取当前URL页面的所有标题和URL
	 * @param url
	 */
	public void getTitleInfo(String url) {
		FetchPage fetch = new FetchPage();
		HtmlPage page = fetch.fetchPage(url);
		DomNodeList<DomElement> elements = page.getElementsByTagName("table");
		//获取所有的table
		DomElement targetElement = null;
		for (DomElement item: elements) {
			if ("body".equals(item.getAttribute("class"))) {
				targetElement = item;
				break;
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
			for (HtmlElement td: tdElement) {
				DomNodeList<HtmlElement> spanElement = td.getElementsByTagName("span");
				for (HtmlElement tempElement: spanElement) {
					if (tempElement.getAttribute("class").matches("col3.")) {
						//这里获取的是每个帖子的发帖时间
						System.out.println(tempElement.getTextContent());
					}
					
				} 
				
				
				DomNodeList<HtmlElement> aElement = td.getElementsByTagName("a");
				for (HtmlElement tempElement: aElement) {
					if (tempElement.getAttribute("href").contains("bbscon")) {
						//这里是每个帖子的Title，需要去掉Re:
						System.out.println(tempElement.getTextContent());
						//这里是每个帖子的具体URL
						System.out.println(tempElement.getAttribute("href"));
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		PkuFetch fetch = new PkuFetch();
		fetch.getTitleInfo("http://www.bdwm.net/bbs/bbsdoc.php?board=intern");
	}
}
