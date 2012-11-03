package com.minioffice.menu.service;

import net.sf.json.JSONArray;

import com.minioffice.menu.entity.TBizMenuVO;
//菜单操作
public interface ITBizMenuService {

	public void saveMenu(TBizMenuVO vo) throws Exception;
	
	public TBizMenuVO getMenu(String id) throws Exception;
	
	public void removeMenu(String id) throws Exception;
	
	public JSONArray getRootMenuItems() throws Exception;

	JSONArray getChildrenMenuItems(String pid) throws Exception;
}
