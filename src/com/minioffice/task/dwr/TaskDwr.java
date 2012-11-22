package com.minioffice.task.dwr;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.ProcessInstance;
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean commitTask(String taskId, Map varset) {
		try {
			Subject currentUser = SecurityUtils.getSubject();
			User user = (User) currentUser.getSession().getAttribute("user");
			processEngineFactoryBean.getProcessEngineConfiguration()
					.getTaskService().claim(taskId, user.getId());// 领取任务
			processEngineFactoryBean.getProcessEngineConfiguration()
					.getTaskService().complete(taskId, varset);// 审批任务
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
				formArray.add(new JSONObject().element("id", fp.getId()) // ID
						.element("name", fp.getName()) // 表单名
						.element("type", fp.getType().getName()) // 类型
						.element("value", fp.getValue())); // 值
			}
		}

		return formArray;
	}

	// 驳回
	public boolean rejectTask(String taskId, String activityId) {
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
			return false;
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
		}
		if (processInstance == null) {
			return false;
		}

		// 取得流程定义
		ProcessDefinitionEntity processDefinition = null;
		try {
			processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) processEngineFactoryBean
					.getProcessEngineConfiguration().getRepositoryService())
					.getDeployedProcessDefinition(task.getProcessDefinitionId());
		} catch (Exception e) {
			logger.error("", e);
		}
		if (processDefinition == null) {
			return false;
		}
		
		// 更换其Execution
				ExecutionEntity exeImpl = (ExecutionEntity) processInstance;

				// 更换其活动的定义
				ActivityImpl preActivity = ((ProcessDefinitionImpl) processDefinition)
						.findActivity(activityId);
				exeImpl.setActivity(preActivity);
				exeImpl.setActive(true);
				exeImpl.setBusinessKey(task.getProcessInstance().getBusinessKey());
				exeImpl.setProcessDefinitionId(task.getProcessDefinitionId());
				exeImpl.setProcessInstance(task.getProcessInstance());
				exeImpl.setProcessDefinition(processDefinition);

				//取得要驳回到的节点的任务定义
				TaskDefinition taskDef = ((ProcessDefinitionEntity) processDefinition)
						.getTaskDefinitions().get(preActivity.getId());
				try {
					TaskEntity newTask = (TaskEntity) processEngineFactoryBean
							.getProcessEngineConfiguration().getTaskService()
							.newTask();

					newTask.setName((String)preActivity.getProperties().get("name"));//设置为要驳回得环节名称
					newTask.setDescription(task.getDescription());
					newTask.setExecution(exeImpl);
					newTask.setCreateTime(new Date());

					newTask.setExecutionId(task.getExecutionId());
					newTask.setProcessInstanceId(task.getProcessInstanceId());
					newTask.setProcessDefinitionId(task.getProcessDefinitionId());
					newTask.setProcessInstance(task.getProcessInstance());

					// 更换流程任务的定义
					if (taskDef != null) {
						newTask.setTaskDefinition(taskDef);
					} else {
						return false;
					}

					//保存新的任务
					 processEngineFactoryBean
						.getProcessEngineConfiguration().getTaskService().saveTask(newTask);
					
					
					//加入之前节点被分派的人员
					List<HistoricTaskInstance> targetTaskList =  processEngineFactoryBean
							.getProcessEngineConfiguration()
							.getHistoryService().createHistoricTaskInstanceQuery()
							.processInstanceId(task.getProcessInstanceId())
							.taskDefinitionKey(activityId).list();
					if (targetTaskList != null && targetTaskList.size() > 0) {
						for (HistoricTaskInstance ht : targetTaskList) {
							 processEngineFactoryBean
								.getProcessEngineConfiguration().getTaskService().addCandidateUser(
									newTask.getId(), ht.getAssignee());
						}
					}
					
					
					//完成当前任务
					 processEngineFactoryBean
						.getProcessEngineConfiguration().getTaskService().deleteTask(task.getId());

				} catch (Exception e) {
					logger.error("", e);

					return false;
				}

				return true;

	}

}
