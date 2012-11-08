package com.minioffice.process.dwr;

import org.activiti.engine.identity.User;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

//流程操作DWR
@RemoteProxy
public class ProcessDwr {
	private final static Logger logger = LoggerFactory
			.getLogger(ProcessDwr.class);

	@Autowired
	private ProcessEngineFactoryBean processEngineFactoryBean;

	// 发起流程
	public boolean initProcess(String key) {
		try{
		Subject currentUser = SecurityUtils.getSubject();
		User user = (User) currentUser.getSession().getAttribute("user");
		//设置为当前登录人员发起流程
		processEngineFactoryBean.getProcessEngineConfiguration()
				.getIdentityService().setAuthenticatedUserId(user.getId());
		processEngineFactoryBean.getProcessEngineConfiguration()
				.getRuntimeService().startProcessInstanceByKey(key);
		}catch(Exception e){
			logger.error("",e);
			return false;
		}
		return true;
	}
}
