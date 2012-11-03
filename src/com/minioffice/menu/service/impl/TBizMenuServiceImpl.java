package com.minioffice.menu.service.impl;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONFunction;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.minioffice.menu.dao.ITBizMenuDAO;
import com.minioffice.menu.entity.TBizMenuVO;
import com.minioffice.menu.service.ITBizMenuService;
//菜单操作
@Service
public class TBizMenuServiceImpl implements ITBizMenuService {

	@Autowired
	ITBizMenuDAO menuDAO;

	//保存菜单
	@Transactional
	public void saveMenu(TBizMenuVO vo) throws Exception {
		// TODO Auto-generated method stub
		menuDAO.save(vo);
	}

	//取得菜单
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public TBizMenuVO getMenu(String id) throws Exception {
		// TODO Auto-generated method stub
		return menuDAO.getMenu(id);
	}

	//删除菜单
	@Transactional
	public void removeMenu(String id) throws Exception {
		// TODO Auto-generated method stub
		menuDAO.removeMenu(id);
	}

	//取得根节点菜单列表(用于住页面展示)
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public JSONArray getRootMenuItems() throws Exception {
		// TODO Auto-generated method stub
		List<TBizMenuVO> rootList = menuDAO.getRootChildren();

		if (rootList != null && rootList.size() > 0) {
			JSONArray rootArray = new JSONArray();

			for (int i = 0, s = rootList.size(); i < s; i++) {
				TBizMenuVO menuVO = rootList.get(i);
				JSONObject menu = new JSONObject().element("text",
						menuVO.getMenuName());
				JSONArray items = this.getChildrenMenuItems(menuVO.getId());

				if (items != null) {
					menu.put("menu",new JSONObject().element("items", items) );
				}

				rootArray.add(menu);
			}
			return rootArray;
		}

		return null;
	}

	//取得下一级菜单列表(用于住页面展示)
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public JSONArray getChildrenMenuItems(String pid) throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pid", pid);
		List<TBizMenuVO> result = menuDAO.getChildren(params);
		if (result != null && result.size() > 0) {
			JSONArray array = new JSONArray();
			for (int i = 0, s = result.size(); i < s; i++) {
				TBizMenuVO menuVO = result.get(i);
				JSONObject menu = new JSONObject().element("text",
						menuVO.getMenuName());

				JSONArray items = this.getChildrenMenuItems(menuVO.getId());

				if (items != null) {
					menu.put("items", items);
				}
				menu.put("id", menuVO.getId());
				menu.put("url", menuVO.getMenuUrl());
				menu.element("click", new JSONFunction(new String[]{"item"},"itemclick(item);"));
				array.add(menu);
			}
			return array;
		}

		return null;
	}

}
