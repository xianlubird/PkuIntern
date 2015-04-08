package com.intern.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.intern.bean.PkuBean;
import com.intern.fetch.pku.PkuFetch;

/**
 * test case controller
 * @author bird
 * Apr 7, 2015 2:39:31 PM
 */
@Controller
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private MongoTemplate mongo;
	
	@Autowired
	private PkuFetch fetch;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public void index() {
	//	fetch.getTitleInfo("http://www.bdwm.net/bbs/bbsdoc.php?board=intern");
		fetch.startFetch("http://www.bdwm.net/bbs/bbsdoc.php?board=intern");
	}
}
