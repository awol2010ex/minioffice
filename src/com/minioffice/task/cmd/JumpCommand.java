package com.minioffice.task.cmd;

import java.io.Serializable;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//跳转命令
public class JumpCommand implements Command<Object>, Serializable {
	private final static Logger logger = LoggerFactory
			.getLogger(JumpCommand.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 2372913338251310191L;
	protected String executionId;// 流程ID
	protected String targetActivitiId;// 跳转目标环节ID
	protected String taskId;// 当前任务ID
	private Map<String, Object> variables;

	public JumpCommand(String executionId, String targetActivitiId,
			String taskId, Map<String, Object> variables) {
		this.executionId = executionId;
		this.targetActivitiId = targetActivitiId;
		this.variables = variables;
		this.taskId = taskId;
	}

	public Object execute(CommandContext commandContext) {
		if (executionId == null) {
			throw new ActivitiException("executionId is null");
		}
		if (targetActivitiId == null) {
			throw new ActivitiException("targetActivitiId is null!");
		}

		// 查找运行中 的Execution
		ExecutionEntity execution = commandContext.getExecutionEntityManager()
				.findExecutionById(executionId);
		if (execution == null) {
			HistoricProcessInstance hai = Context
					.getProcessEngineConfiguration().getHistoryService()
					.createHistoricProcessInstanceQuery()
					.processInstanceId(executionId).singleResult();
			if (hai != null)
				throw new ActivitiException("execution " + executionId
						+ " has been completed at " + hai.getEndTime());
			else {
				throw new ActivitiException("execution " + executionId
						+ " doesn't exist");
			}
		}

		// 设置流程变量
		if (variables != null && !variables.isEmpty()) {
			execution.setVariables(variables);
		}

		// 查找流程定义
		String processDefinitionId = execution.getProcessDefinitionId();
		RepositoryServiceImpl rsi = (RepositoryServiceImpl) Context
				.getProcessEngineConfiguration().getRepositoryService();
		ProcessDefinitionEntity pde = (ProcessDefinitionEntity) rsi
				.getDeployedProcessDefinition(processDefinitionId);

		ActivityImpl targetActiviti = pde.findActivity(targetActivitiId);
		if (targetActiviti == null) {
			throw new ActivitiException("targetActiviti " + targetActiviti
					+ " doesn't exist");
		}

		// 跳转
		try {
			execution.take(targetActiviti.getIncomingTransitions().iterator()
					.next());
			// 删除当前任务(删除理由是驳回)
			TaskEntity task = Context.getCommandContext()
					.getTaskEntityManager().findTaskById(taskId);
			if (task != null && !task.isDeleted()) {

				Context.getCommandContext().getTaskEntityManager()
						.deleteTask(task, "reject", false);
			}
			return true;
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
	}
}
