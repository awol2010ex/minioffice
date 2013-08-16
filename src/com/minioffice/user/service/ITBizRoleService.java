package com.minioffice.user.service;

import java.util.Map;

import net.sf.json.JSONObject;

import com.minioffice.user.entity.TBizRoleVO;

//角色操作
public interface ITBizRoleService {

	//保存角色
	public void saveRole(TBizRoleVO vo) throws Exception;

	//取得角色
	public TBizRoleVO getRole(String id) throws Exception;

	//删除角色
	public void removeRole(String id) throws Exception;
	//查询角色
	public JSONObject searchRoleList(Map<String, Object> map, int offset,
			int pageSize) throws Exception;
}
