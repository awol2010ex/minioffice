package com.minioffice.task.dwr;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
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

	// 取得表单数据
	public JSONArray getFormData(String taskId) {

		TaskFormData formData = processEngineFactoryBean
				.getProcessEngineConfiguration().getFormService()
				.getTaskFormData(taskId);// 取得表单
		List<FormProperty> pList = formData.getFormProperties();// 表单列表

		JSONArray formArray = new JSONArray();
		if (pList != null && pList.size() > 0) {
			for (FormProperty fp : pList) {
				formArray.add(new JSONObject()
				        .element("id", fp.getId())    //ID
						.element("name", fp.getName())  //表单名
						.element("type", fp.getType().getName())  //类型
						.element("value", fp.getValue())); //值
			}
		}

		return formArray;
	}

}
