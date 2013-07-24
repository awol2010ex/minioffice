package com.minioffice.process.dwr;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
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
	private ProcessEngineFactoryBean processEngineFactoryBean;// 流程操作部件

	// 发起流程
	public JSONObject initProcess(String key) {
		boolean result =true;
		String msg ="";
		try {
			Subject currentUser = SecurityUtils.getSubject();
			User user = (User) currentUser.getSession().getAttribute("user");
			// 设置为当前登录人员发起流程
			processEngineFactoryBean.getProcessEngineConfiguration()
					.getIdentityService().setAuthenticatedUserId(user.getId());
			processEngineFactoryBean.getProcessEngineConfiguration()
					.getRuntimeService().startProcessInstanceByKey(key);
		} catch (Exception e) {
			logger.error("", e);
			msg =e.getLocalizedMessage();
			result= false;
		}
		return new JSONObject().element("result", result).element("msg", msg);
	}

	// 流程变量列表
	public JSONArray getVariableList(String processInstanceId) {
		JSONArray result = new JSONArray();

		HistoricVariableInstanceQuery query = processEngineFactoryBean
				.getProcessEngineConfiguration().getHistoryService()
				.createHistoricVariableInstanceQuery()
				.processInstanceId(processInstanceId);
		List<HistoricVariableInstance> list = query.list();
		if (list != null && list.size() > 0) {
			for (HistoricVariableInstance v : list) {
				result.add(new JSONObject()
				   .element("variableName",v.getVariableName())//变量名
				   .element("value",v.getValue() )//变量值
				   
			  );
			}
		}

		return result;
	}
	
	//删除流程定义
	public JSONObject deleteDeployment(String deploymentId){
		boolean result =true;
		String msg ="";
		try {
		 processEngineFactoryBean
			.getProcessEngineConfiguration().getRepositoryService().deleteDeployment(deploymentId);
		} catch (Exception e) {
			logger.error("", e);
			msg =e.getLocalizedMessage();
			result= false;
		}
		return new JSONObject().element("result", result).element("msg", msg);
	}
	
	//删除流程实例
	public JSONObject deleteProcessInstance(String processInstanceId){
		boolean result =true;
		String msg ="";
		try {
		 processEngineFactoryBean
			.getProcessEngineConfiguration().getRuntimeService().deleteProcessInstance(processInstanceId, "delete");
		} catch (Exception e) {
			logger.error("", e);
			msg =e.getLocalizedMessage();
			result= false;
		}
		return new JSONObject().element("result", result).element("msg", msg);
	}
}
