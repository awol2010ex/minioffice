package com.minioffice.user.dwr;

import org.activiti.engine.identity.User;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@RemoteProxy
public class UserDwr {
	private final static Logger logger = LoggerFactory.getLogger(UserDwr.class);

	@Autowired
	private ProcessEngineFactoryBean processEngineFactoryBean;// 流程操作部件

	public boolean saveUser(String userName, String password,String email) {
		try {
			String id = processEngineFactoryBean
					.getProcessEngineConfiguration().getIdGenerator()
					.getNextId();
			User u = processEngineFactoryBean.getProcessEngineConfiguration()
					.getIdentityService().newUser(id);

			u.setPassword(password);
			u.setFirstName(userName);
			u.setEmail(email);
			processEngineFactoryBean.getProcessEngineConfiguration()
					.getIdentityService().saveUser(u);
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}

		return true;
	}
}
