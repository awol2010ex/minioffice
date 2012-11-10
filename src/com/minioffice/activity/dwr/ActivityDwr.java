package com.minioffice.activity.dwr;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;

//流程环节操作DWR
@RemoteProxy
public class ActivityDwr {

	@Autowired
	private ProcessEngineFactoryBean processEngineFactoryBean;

	// 显示//历史环节列表
	public JSONObject getHistoryActivityList(String processInstanceId,
			String processDefinfitionId) {
		JSONObject result = new JSONObject();

		// 历史环节列表
		List<HistoricActivityInstance> list = processEngineFactoryBean
				.getProcessEngineConfiguration().getHistoryService()
				.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId)
				.orderByHistoricActivityInstanceStartTime().asc().list();

		JSONArray historyActivitys = new JSONArray();
		if (list != null && list.size() > 0) {
			for (HistoricActivityInstance p : list) {
				historyActivitys.add(new JSONObject().element("id", p.getId())
						.element("activityId", p.getActivityId())//环节ID
						.element("activityName", p.getActivityName())//环节名称
						.element("startTime", p.getStartTime())//开始时间
						.element("endTime", p.getEndTime()));//结束时间
			}
		}
		result.put("historyActivitys", historyActivitys);

		// 已部署的节点列表
		ProcessDefinitionEntity latestProcessDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) processEngineFactoryBean
				.getProcessEngineConfiguration().getRepositoryService())
				.getDeployedProcessDefinition(processDefinfitionId);
		List<ActivityImpl> activities = latestProcessDefinition.getActivities();

		JSONArray activitiesJSON = new JSONArray();
		if (activities != null && activities.size() > 0) {
			for (ActivityImpl ai : activities) {
				activitiesJSON.add(
						new JSONObject()
						.element("id", ai.getId())
						.element("height", ai.getHeight())
						.element("width", ai.getWidth())
						.element("x", ai.getX())
						.element("y", ai.getY())
						.element("properties", ai.getProperties())
						);
			}
		}

		result.put("activities", activitiesJSON);

		return result;
	}
}
