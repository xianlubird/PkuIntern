package com.intern.fetch.pku;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
	private FetchPage fetch;
}
