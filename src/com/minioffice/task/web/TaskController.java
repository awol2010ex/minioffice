package com.minioffice.task.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//用户相关操作展示类
@Controller
@RequestMapping(value = "/task")
public class TaskController {
	private final static Logger logger = LoggerFactory
			.getLogger(TaskController.class);
	@Autowired
	private ProcessEngineFactoryBean processEngineFactoryBean;

	/* 我的任务(被分派)列表 */
	@RequestMapping(value = "/mytask/assignee/list/")
	public void getMyAssigneeTaskList(int page, int pagesize,
			HttpServletResponse response) {
		Subject currentUser = SecurityUtils.getSubject();
		User user = (User) currentUser.getSession().getAttribute("user");// 当前用户

		// 查询任务
		TaskQuery query = processEngineFactoryBean
				.getProcessEngineConfiguration().getTaskService()
				.createTaskQuery().taskAssignee(user.getId())
				.orderByTaskCreateTime().desc();

		try {
			JSONObject o = new JSONObject();
			o.put("Total", query.count());

			List<Task> list = query.listPage((page - 1) * pagesize, pagesize);
			JSONArray Rows = new JSONArray();
			if (list != null && list.size() > 0) {
				for (Task p : list) {
					Rows.add(new JSONObject()
							.element("id", p.getId())
							.element("createTime", p.getCreateTime())
							// 创建时间
							.element("dueDate", p.getDueDate())
							// 处理时间
							.element("name", p.getName())
							// 任务名称
							.element("processDefinitionId",
									p.getProcessDefinitionId())// 流程定义ID
							.element("processInstanceId",
									p.getProcessInstanceId())// 流程实例ID
					);
				}
			}

			o.put("Rows", Rows);
			response.getWriter().print(o.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
	}

	/* 我的任务(已归档)列表 */
	@RequestMapping(value = "/mytask/history/list/")
	public void getMyHistoryTaskList(int page, int pagesize,
			HttpServletResponse response) {
		Subject currentUser = SecurityUtils.getSubject();
		User user = (User) currentUser.getSession().getAttribute("user");// 当前用户

		// 查询已归档任务
		HistoricTaskInstanceQuery query = processEngineFactoryBean
				.getProcessEngineConfiguration().getHistoryService()
				.createHistoricTaskInstanceQuery().taskAssignee(user.getId())
				.orderByHistoricTaskInstanceEndTime().desc();

		try {
			JSONObject o = new JSONObject();
			o.put("Total", query.count());

			List<HistoricTaskInstance> list = query.listPage((page - 1)
					* pagesize, pagesize);
			JSONArray Rows = new JSONArray();
			if (list != null && list.size() > 0) {
				for (HistoricTaskInstance p : list) {
					Rows.add(new JSONObject()
							.element("id", p.getId())
							.element("endTime", p.getEndTime())
							// 完成时间
							.element("startTime", p.getStartTime())
							// 处理时间
							.element("name", p.getName())
							// 任务名称
							.element("processDefinitionId",
									p.getProcessDefinitionId())// 流程定义ID
							.element("processInstanceId",
									p.getProcessInstanceId())// 流程实例ID
					);
				}
			}

			o.put("Rows", Rows);
			response.getWriter().print(o.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
	}

}
