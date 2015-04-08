package com.intern.fetch.pku;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private Logger logger = LoggerFactory.getLogger(PkuFetch.class);
	
	@Autowired
	private FetchPage fetch;
	
	@Autowired
	private MongoTemplate mongo;
	
	public Map<String,String> monthlyMap;
	
	public PkuFetch() {
		monthlyMap = new HashMap<String, String>();
		monthlyMap.put("Jan", "01");
		monthlyMap.put("Feb", "02");
		monthlyMap.put("Mar", "03");
		monthlyMap.put("Apr", "04");
		monthlyMap.put("May", "05");
		monthlyMap.put("Jun", "06");
		monthlyMap.put("Jul", "07");
		monthlyMap.put("Aug", "08");
		monthlyMap.put("Sep", "09");
		monthlyMap.put("Oct", "10");
		monthlyMap.put("Nov", "11");
		monthlyMap.put("Dec", "12");
	}
	
	/**
	 * 开始爬取的主控类
	 */
	public void startFetch() {
		
	}
	
	/**
	 * 计算传递进来的参数对应于当前服务器时间过去了几天
	 * 如果超过一个月，就返回false
	 * 在一个月之内就是true
	 * @param time
	 * @return
	 */
	public boolean calculateTimeGap(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM dd HH:mm");
		String prefix = time.substring(0, 3);
		String numPrefix = monthlyMap.get(prefix);
		time = time.replace(prefix, numPrefix);
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
		//	e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		Date currentTime = new Date();
		String tempTime = sdf.format(currentTime);
		try {
			currentTime = sdf.parse(tempTime);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		long nd = 1000 * 24 * 60 * 60;//一天的毫秒数 
		long diff = currentTime.getTime() - date.getTime();
		long day = diff / nd;//计算差了多少天
		if (day <= 30) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取当前URL页面的所有标题和URL
	 * 返回最后一个帖子的发帖时间
	 * @param url
	 */
	public String getTitleInfo(String url) {
		HtmlPage page = fetch.fetchPage(url);
		DomNodeList<DomElement> elements = page.getElementsByTagName("table");
		String time = null;
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
						time = tempElement.getTextContent();
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
		return time;
	}
	
	public static void main(String[] args) {
		PkuFetch fetch = new PkuFetch();
	//	fetch.getTitleInfo("http://www.bdwm.net/bbs/bbsdoc.php?board=intern");
		boolean flag = fetch.calculateTimeGap("Apr 7 16:17");
		System.out.println(flag);
	}
}
