package com.minioffice.process;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//流程相关操作展示类
@Controller
@RequestMapping(value = "/process")
public class ProcessController {
	private final static Logger logger = LoggerFactory
			.getLogger(ProcessController.class);
	@Autowired
	private ProcessEngineFactoryBean processEngineFactoryBean;

	/* 取得流程定义列表 */
	@RequestMapping(value = "/processDef/list/")
	public void getProcessDefList(int page, int pagesize,
			HttpServletResponse response) {
		ProcessDefinitionQuery query = processEngineFactoryBean
				.getProcessEngineConfiguration().getRepositoryService()
				.createProcessDefinitionQuery()
				.active()
				.latestVersion()//最新版本
				.orderByProcessDefinitionKey().desc()
				.orderByProcessDefinitionVersion().desc();

		try {
			JSONObject o = new JSONObject();
			o.put("Total", query.count());

			List<ProcessDefinition> list = query.listPage(
					(page - 1) * pagesize, pagesize);
			JSONArray Rows = new JSONArray();
			if (list != null && list.size() > 0) {
				for (ProcessDefinition p : list) {
					Rows.add(new JSONObject().element("id", p.getId())
							.element("key", p.getKey())
							.element("version", p.getVersion())
							.element("name", p.getName()));
				}
			}

			o.put("Rows", Rows);
			response.getWriter().print(o.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
	}
	
	/* 取得已部署流程列表 */
	@RequestMapping(value = "/deployment/list/")
	public void getDeploymentList(int page, int pagesize,
			HttpServletResponse response) {
		DeploymentQuery query = processEngineFactoryBean
				.getProcessEngineConfiguration().getRepositoryService()
				.createDeploymentQuery()
				.orderByDeploymenTime().desc();

		try {
			JSONObject o = new JSONObject();
			o.put("Total", query.count());

			List<Deployment> list = query.listPage(
					(page - 1) * pagesize, pagesize);
			JSONArray Rows = new JSONArray();
			if (list != null && list.size() > 0) {
				for (Deployment p : list) {
					Rows.add(new JSONObject().element("id", p.getId())
							.element("deploymentTime", p.getDeploymentTime())
							.element("name", p.getName()));
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
