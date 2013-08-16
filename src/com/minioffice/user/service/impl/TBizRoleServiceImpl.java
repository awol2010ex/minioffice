package com.minioffice.user.service.impl;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.minioffice.user.dao.ITBizRoleDAO;
import com.minioffice.user.entity.TBizRoleVO;
import com.minioffice.user.service.ITBizRoleService;

//角色操作
@Service
public class TBizRoleServiceImpl implements ITBizRoleService {
	@Autowired
	ITBizRoleDAO roleDAO;

	@Transactional
	public void saveRole(TBizRoleVO vo) throws Exception {
		// TODO Auto-generated method stub
		roleDAO.save(vo);
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public TBizRoleVO getRole(String id) throws Exception {
		// TODO Auto-generated method stub
		return roleDAO.getRole(id);
	}

	@Transactional
	public void removeRole(String id) throws Exception {
		// TODO Auto-generated method stub
		roleDAO.removeRole(id);
	}

	public JSONObject searchRoleList(Map<String, Object> map, int offset,
			int pageSize) throws Exception {

		JSONObject o = new JSONObject();
		o.put("Total", roleDAO.searchRoleCount(map));

		List<TBizRoleVO> list = roleDAO.searchRoleList(map, offset, pageSize);
		o.put("Rows", JSONArray.fromObject(list));
		return o;
	}
}
