package com.intern.bean;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 存储未名BBS的帖子的所有信息
 * tes
 * @author bird Apr 7, 2015 2:44:04 PM
 */
@Document
public class PkuBean {
	
	private String title;

	private String time;

	private String content;

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
