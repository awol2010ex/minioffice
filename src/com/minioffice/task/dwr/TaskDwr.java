package com.minioffice.task.dwr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.minioffice.task.cmd.JumpCommand;

import edu.emory.mathcs.backport.java.util.Arrays;

//流程操作DWR
@RemoteProxy
public class TaskDwr {
	private final static Logger logger = LoggerFactory.getLogger(TaskDwr.class);

	@Autowired
	private ProcessEngineFactoryBean processEngineFactoryBean;// 流程操作部件

	// 审批任务通过
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JSONObject commitTask(String taskId, Map varset) {
		
		boolean result =true;
		String msg ="";
		try {
			Subject currentUser = SecurityUtils.getSubject();
			User user = (User) currentUser.getSession().getAttribute("user");
			processEngineFactoryBean.getProcessEngineConfiguration()
					.getTaskService().claim(taskId, user.getId());// 领取任务

			// 逗号分割字符串转为列表型变量
			Iterator it = varset.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				Object value = varset.get(key);
				if (value != null && value instanceof String) {
					String[] valueList = value.toString().split(",");
					if (valueList != null && valueList.length > 1) {
						List<String> array_var = Arrays.asList(valueList);
						varset.put(key, array_var);
					}
				}
			}

			processEngineFactoryBean.getProcessEngineConfiguration()
					.getTaskService().complete(taskId, varset);// 审批任务
		} catch (Exception e) {
			logger.error("", e);
			msg =e.getLocalizedMessage();
			result= false;
		}
		return new JSONObject().element("result", result).element("msg", msg);
	}

	// 取得表单数据
	public JSONArray getFormData(String taskId) {

		TaskFormData formData = null;
		try {
			formData = processEngineFactoryBean.getProcessEngineConfiguration()
					.getFormService().getTaskFormData(taskId);// 取得表单
		} catch (Exception e) {
			logger.error("", e);
		}
		if (formData != null) {
			List<FormProperty> pList = formData.getFormProperties();// 表单列表

			JSONArray formArray = new JSONArray();
			if (pList != null && pList.size() > 0) {
				for (FormProperty fp : pList) {
					formArray.add(new JSONObject().element("id", fp.getId()) // ID
							.element("name", fp.getName())
							// 表单名
							.element(
									"type",
									fp.getType() == null ? "" : fp.getType()
											.getName()) // 类型
							.element("value", fp.getValue())// 默认值
							)

					; // 值
				}
			}

			return formArray;
		}
		return null;
	}

	// 驳回
	public JSONObject rejectTask(String taskId, String activityId) {
		boolean result =true;
		String msg ="";
		
		// 找到当前任务
		TaskEntity task = null;
		try {
			task = (TaskEntity) processEngineFactoryBean
					.getProcessEngineConfiguration().getTaskService()
					.createTaskQuery().taskId(taskId).singleResult();
		} catch (Exception e) {
			logger.error("", e);
		}
		if (task == null) {
			msg ="任务不存在";
			logger.error("任务不存在");
			result= false;
			return new JSONObject().element("result", result).element("msg", msg);
		}

		
		
		HistoricTaskInstanceQuery   oldTaskInstanceQuery=processEngineFactoryBean.getProcessEngineConfiguration().getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId()).taskDefinitionKey(activityId);
		if(oldTaskInstanceQuery.count()==0){
			logger.error("不是已经过的环节");
			msg ="不是已经过的环节";
			result= false;
			return new JSONObject().element("result", result).element("msg", msg);
		}
		
		// 找到流程实例
		ProcessInstance processInstance = null;
		try {
			processInstance = processEngineFactoryBean
					.getProcessEngineConfiguration().getRuntimeService()
					.createProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId())
					.singleResult();
		} catch (Exception e) {
			logger.error("", e);
			msg =e.getLocalizedMessage();
			result= false;
			return new JSONObject().element("result", result).element("msg", msg);
		}
		if (processInstance == null) {
			logger.error("流程不存在");
			msg ="流程不存在";
			result= false;
			return new JSONObject().element("result", result).element("msg", msg);
		}

		try {
			// 跳转
			processEngineFactoryBean
					.getProcessEngineConfiguration()
					.getCommandExecutorTxRequired()
					.execute(
							new JumpCommand(task.getProcessInstanceId(),
									activityId, taskId,
									new HashMap<String, Object>()));
		} catch (Exception e) {
			logger.error("", e);
			msg =e.getLocalizedMessage();
			result= false;
			return new JSONObject().element("result", result).element("msg", msg);
		}

		return new JSONObject().element("result", result).element("msg", msg);
	}

}
