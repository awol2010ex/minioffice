package com.minioffice.user.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.minioffice.user.service.ITBizRoleService;
//角色
@Controller
@RequestMapping(value = "/role")
public class RoleController {
	private final static Logger logger = LoggerFactory.getLogger(RoleController.class);
	@Autowired
	ITBizRoleService tBizRoleService;
	
	/* 我的任务(被分派)列表 */
	@RequestMapping(value = "/list/")
	public void searchRoleList(int page, int pagesize,
			HttpServletResponse response) {
		Map<String,Object>  map =new HashMap<String,Object>();
		
		
		try {
			response.getWriter().print(tBizRoleService.searchRoleList(map, 
						(page - 1) * pagesize, pagesize));
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("",e);
		}
	}
}
