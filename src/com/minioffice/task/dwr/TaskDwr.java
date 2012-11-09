package com.minioffice.task.dwr;

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
public class TaskDwr {
	private final static Logger logger = LoggerFactory.getLogger(TaskDwr.class);

	@Autowired
	private ProcessEngineFactoryBean processEngineFactoryBean;// 流程操作部件

	// 审批任务通过
	public boolean commitTask(String taskId) {
		try {
			Subject currentUser = SecurityUtils.getSubject();
			User user = (User) currentUser.getSession().getAttribute("user");
			processEngineFactoryBean.getProcessEngineConfiguration()
					.getTaskService().claim(taskId, user.getId());// 领取任务
			processEngineFactoryBean.getProcessEngineConfiguration()
					.getTaskService().complete(taskId);// 审批任务
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
		return true;
	}
}
