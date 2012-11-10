package com.minioffice.activity.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

//流程环节相关操作展示类
@Controller
@RequestMapping(value = "/actvity")
public class ActivityController {
	private final static Logger logger = LoggerFactory
			.getLogger(ActivityController.class);
	@Autowired
	private ProcessEngineFactoryBean processEngineFactoryBean;

	// 显示//历史环节列表
	@RequestMapping(value = "/history/list/{processInstanceId}")
	public void getHistoryActivityList(@PathVariable String processInstanceId,
			HttpServletRequest request, HttpServletResponse response) {
		
		//历史环节列表
		List<HistoricActivityInstance> list = processEngineFactoryBean
				.getProcessEngineConfiguration().getHistoryService()
				.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();

		
		JSONArray Rows = new JSONArray();
		if (list != null && list.size() > 0) {
			for (HistoricActivityInstance p : list) {
				Rows.add(new JSONObject().element("id", p.getId())
						.element("activityId", p.getActivityId())
						.element("activityName", p.getActivityName())
								.element("startTime", p.getStartTime())
								.element("endTime", p.getEndTime())
						);
			}
		}
		try {
			response.getWriter().print(Rows.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("",e);
		}
	}
}
