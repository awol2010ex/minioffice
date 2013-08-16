package com.minioffice.user.dwr;

import net.sf.json.JSONObject;

import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.minioffice.user.entity.TBizRoleVO;
import com.minioffice.user.service.ITBizRoleService;
import com.minioffice.utils.UUIDGenerator;

@RemoteProxy
public class RoleDwr {
	private final static Logger logger = LoggerFactory.getLogger(RoleDwr.class);
	@Autowired
	ITBizRoleService tBizRoleService;

	// 保存用户
	public JSONObject saveRole(String rolename, String delflag) {
		boolean result = true;
		String msg = "";
		try {
			TBizRoleVO role = new TBizRoleVO();
			role.setId(UUIDGenerator.generate());
			role.setDelflag(delflag);
			role.setRolename(rolename);

			tBizRoleService.saveRole(role);
		} catch (Exception e) {
			logger.error("", e);
			msg = e.getLocalizedMessage();
			result = false;
		}

		return new JSONObject().element("result", result).element("msg", msg);
	}
	
	
	

	// 更新用户
	public JSONObject updateRole(String id,String rolename, String delflag) {
		boolean result = true;
		String msg = "";
		try {
			TBizRoleVO role = tBizRoleService.getRole(id);
			role.setDelflag(delflag);
			role.setRolename(rolename);

			tBizRoleService.saveRole(role);
		} catch (Exception e) {
			logger.error("", e);
			msg = e.getLocalizedMessage();
			result = false;
		}

		return new JSONObject().element("result", result).element("msg", msg);
	}
}
