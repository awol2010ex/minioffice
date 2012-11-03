package com.minioffice.menu.dao;

import com.minioffice.common.dao.ITreeDAO;
import com.minioffice.menu.entity.TBizMenuVO;

//菜单接口
public interface ITBizMenuDAO  extends ITreeDAO<TBizMenuVO>{

	//保存菜单
	public void save(TBizMenuVO vo) throws Exception;
	
	//取得菜单
	public TBizMenuVO getMenu(String id) throws Exception;
	
	//删除菜单
	public void removeMenu(String id) throws Exception;
}
