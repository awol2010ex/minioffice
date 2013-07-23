package com.minioffice.process.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
				.createProcessDefinitionQuery().active().latestVersion()
				// 最新版本
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
							.element("version", p.getVersion())// 版本
							.element("name", p.getName())// 流程名称
							.element("deploymentId", p.getDeploymentId()));// 部署ID
				}
			}

			o.put("Rows", Rows);
			response.getWriter().print(o.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
	}

	// 显示已部署流程定义的流程图PNG
	@RequestMapping(value = "/processDef/diagram/{deploymentId}")
	public void diagramFromDeployment(@PathVariable String deploymentId,
			HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("image/png");
		String diagramResourceName = null;// 生成的图片资源名称
		List<ProcessDefinition> processDefinitionList = processEngineFactoryBean
				.getProcessEngineConfiguration().getRepositoryService()
				.createProcessDefinitionQuery().deploymentId(deploymentId)
				.orderByProcessDefinitionVersion().desc().list();
		ProcessDefinition processDefinition = null;// 流程定义
		if (processDefinitionList != null && processDefinitionList.size() > 0) {
			for (ProcessDefinition p : processDefinitionList) {
				if (p.getDiagramResourceName() != null) {
					processDefinition = p;
					break;
				}
			}
		}

		if (processDefinition != null) {
			diagramResourceName = processDefinition.getDiagramResourceName();

			if (diagramResourceName != null) {
				InputStream imageStream = processEngineFactoryBean
						.getProcessEngineConfiguration().getRepositoryService()
						.getResourceAsStream(deploymentId, diagramResourceName);

				try {// 输出流程图
					BufferedImage theImg = ImageIO.read(imageStream);
					ImageIO.write(theImg, "png", response.getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("", e);
				}
			} else {
				try {// 提示没有流程图
					response.sendRedirect(request.getContextPath()
							+ "/static/images/no_process_dragram.png");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("", e);
				}
			}
		} else {
			try {// 提示没有流程图
				response.sendRedirect(request.getContextPath()
						+ "/static/images/no_process_dragram.png");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			}
		}
	}

	/* 取得已部署流程列表 */
	@RequestMapping(value = "/deployment/list/")
	public void getDeploymentList(int page, int pagesize,
			HttpServletResponse response) {
		DeploymentQuery query = processEngineFactoryBean
				.getProcessEngineConfiguration().getRepositoryService()
				.createDeploymentQuery().orderByDeploymenTime().desc();

		try {
			JSONObject o = new JSONObject();
			o.put("Total", query.count());

			List<Deployment> list = query.listPage((page - 1) * pagesize,
					pagesize);
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

	// 取得当前用户发起的流程
	@RequestMapping(value = "/myprocess/list/")
	public void getMyProcessList(int page, int pagesize,
			HttpServletResponse response) {
		Subject currentUser = SecurityUtils.getSubject();
		User user = (User) currentUser.getSession().getAttribute("user");// 当前用户
		// 查询流程实例
		HistoricProcessInstanceQuery query = processEngineFactoryBean
				.getProcessEngineConfiguration().getHistoryService()
				.createHistoricProcessInstanceQuery().startedBy(user.getId())
				.unfinished().orderByProcessInstanceStartTime().desc();
		try {
			JSONObject o = new JSONObject();
			o.put("Total", query.count());

			List<HistoricProcessInstance> list = query.listPage((page - 1)
					* pagesize, pagesize);
			JSONArray Rows = new JSONArray();
			if (list != null && list.size() > 0) {
				for (HistoricProcessInstance p : list) {
					Rows.add(new JSONObject()
							.element("id", p.getId())
							.element("startTime", p.getStartTime())
							// 发起时间
							.element("endTime", p.getEndTime())
							// 结束时间
							.element("processDefinitionId",
									p.getProcessDefinitionId())// 流程定义ID
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

	// 查看流程实例的流程图
	@RequestMapping(value = "/processInstance/diagram/{processInstanceId}")
	public void diagramFromProcessInstance(
			@PathVariable String processInstanceId, HttpServletRequest request,
			HttpServletResponse response) {
		// 取得流程实例
		HistoricProcessInstance processInstance = processEngineFactoryBean
				.getProcessEngineConfiguration().getHistoryService()
				.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();

		boolean draw = false;
		if (processInstance != null) {
			// 取得流程定义
			ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) (processEngineFactoryBean
					.getProcessEngineConfiguration().getRepositoryService()))
					.getDeployedProcessDefinition(processInstance
							.getProcessDefinitionId());

			BpmnModel model = processEngineFactoryBean
					.getProcessEngineConfiguration().getRepositoryService()
					.getBpmnModel(processInstance.getProcessDefinitionId());

			if (processDefinition != null
					&& processDefinition.isGraphicalNotationDefined()
					&& model != null) {

				List<String> haIdList = new ArrayList<String>();// 当前环节
				try {
					haIdList = processEngineFactoryBean
							.getProcessEngineConfiguration()
							.getRuntimeService()
							.getActiveActivityIds(processInstance.getId());
				} catch (Exception e) {
					logger.error("", e);
				}
				// 生成实时的流程图
				try {

					InputStream definitionImageStream = ProcessDiagramGenerator
							.generateDiagram(model, "png", haIdList);
					// 输出流程图
					BufferedImage theImg = ImageIO.read(definitionImageStream);
					ImageIO.write(theImg, "png", response.getOutputStream());
					draw = true;
				} catch (Exception e) {
					logger.error("", e);
				}

			}
		}

		if (!draw) {
			try {// 提示没有流程图
				response.sendRedirect(request.getContextPath()
						+ "/static/images/no_process_dragram.png");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			}
		}

	}

	// 部署模板
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/processDef/deploy")
	public void deploy(HttpServletRequest request, HttpServletResponse response) {
		logger.info("开始部署模板");

		boolean success = false;
		try {
			File tempfile = new File(System.getProperty("java.io.tmpdir"));// 采用系统临时文件目录
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			diskFileItemFactory.setSizeThreshold(4096); // 设置缓冲区大小，这里是4kb
			diskFileItemFactory.setRepository(tempfile); // 设置缓冲区目录
			ServletFileUpload fu = new ServletFileUpload(diskFileItemFactory);
			fu.setSizeMax(4194304);
			List fileItems = fu.parseRequest(request);

			if (fileItems != null && fileItems.size() > 0) {
				DeploymentBuilder db = processEngineFactoryBean
						.getProcessEngineConfiguration().getRepositoryService()
						.createDeployment();

				for (int i = 0, s = fileItems.size(); i < s; i++) {
					FileItem fi = (FileItem) fileItems.get(i);
					if (fi.getName() != null
							&& (fi.getName().endsWith(".bpmn20.xml")  || fi.getName().endsWith(".bpmn"))) {
						db.addInputStream(fi.getName(), fi.getInputStream());

						Deployment   d=db.name(fi.getName()).deploy();
						if(d!=null && d.getId()!=null){
							success = true;
						}
						else{
							success = false;
						}
					}
				}

				logger.info("完成部署模板");
			} else {
				success = false;
			}

			success = true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);

			success = false;
		}

		try {
			response.getWriter().print(String.valueOf(success));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
	}

}
