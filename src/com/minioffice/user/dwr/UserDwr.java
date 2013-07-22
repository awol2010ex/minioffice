package com.minioffice.user.dwr;

import org.activiti.spring.ProcessEngineFactoryBean;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@RemoteProxy
public class UserDwr {
	private final static Logger logger = LoggerFactory
			.getLogger(UserDwr.class);
	
	@Autowired
	private ProcessEngineFactoryBean processEngineFactoryBean;// 流程操作部件
	
	public  boolean  createUser(String userName ,String password) {
	
		return true;
	}
}
