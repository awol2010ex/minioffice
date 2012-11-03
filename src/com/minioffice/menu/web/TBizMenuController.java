package com.minioffice.menu.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.minioffice.menu.dao.ITBizMenuDAO;
import com.minioffice.menu.entity.TBizMenuVO;
import com.minioffice.menu.service.ITBizMenuService;
//菜单树展示层
@Controller
@RequestMapping(value = "/menu")
public class TBizMenuController {
	private final static Logger logger = LoggerFactory
			.getLogger(TBizMenuController.class);

	@Autowired
	ITBizMenuDAO menuDAO;
	@Autowired
	ITBizMenuService tBizMenuService;

	//取得树状菜单结构
	@RequestMapping(value = "/list")
	public void getChildren(String pid, 
			HttpServletResponse response) {

		List<TBizMenuVO> result = null;
		if (pid == null) {
			try {
				result = menuDAO.getRootChildren();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			}
		} else {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("pid", pid);
			try {
				result = menuDAO.getChildren(params);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			}
		}

		if (result != null) {
			try {
				JsonConfig jsonConfig = new JsonConfig();
				jsonConfig.registerJsonBeanProcessor(TBizMenuVO.class,
						new JsonBeanProcessor() {

							public JSONObject processBean(Object bean,
									JsonConfig jsonConfig) {
								if (!(bean instanceof TBizMenuVO)) {
									return new JSONObject(true);
								}
								TBizMenuVO vo = (TBizMenuVO) bean;
								return new JSONObject()
										.element("id", vo.getId())
										.element("menuName", vo.getMenuName())
										.element("menuIndex", vo.getMenuIndex())
										.element("menuUrl", vo.getMenuUrl())
										.element("isexpand", false)
										.element("children", new JSONArray());
							}
						});
				response.getWriter().print(
						JSONArray.fromObject(result, jsonConfig).toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			}
		}

	}
	//取得所有菜单
	@RequestMapping(value = "/items")
	public void getAllMenuItems(HttpServletResponse response) {
		try {
			JSONArray array = tBizMenuService.getRootMenuItems();
			if (array != null) {
				response.getWriter().print(array.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
	}

}
