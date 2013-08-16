package com.minioffice.user.dao;

import java.util.List;
import java.util.Map;

import com.minioffice.user.entity.TBizRoleVO;

//角色操作
public interface ITBizRoleDAO {
	// 保存角色
	public void save(TBizRoleVO vo) throws Exception;

	// 取得角色
	public TBizRoleVO getRole(String id) throws Exception;

	// 删除角色
	public void removeRole(String id) throws Exception;

	// 角色列表
	public List<TBizRoleVO> searchRoleList(Map<String, Object> map, int offset,
			int pageSize) throws Exception;
	public Integer searchRoleCount(Map<String, Object> map) throws Exception;
}
