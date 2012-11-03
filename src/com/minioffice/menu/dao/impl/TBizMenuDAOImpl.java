package com.minioffice.menu.dao.impl;

import org.springframework.stereotype.Repository;

import com.minioffice.common.dao.impl.TreeDAOImpl;
import com.minioffice.menu.dao.ITBizMenuDAO;
import com.minioffice.menu.entity.TBizMenuVO;

//菜单DAO
@Repository
public class TBizMenuDAOImpl extends TreeDAOImpl<TBizMenuVO> implements
		ITBizMenuDAO {

	//取得菜单
	public TBizMenuVO getMenu(String id) throws Exception {
		// TODO Auto-generated method stub
		return this.get(id);
	}

	//删除菜单
	public void removeMenu(String id) throws Exception {
		// TODO Auto-generated method stub
		TBizMenuVO vo=this.get(id);
		if(vo.getParentNode()==null)
		{
		   this.delete(vo);
		}
		else{
		   vo.getParentNode().getChildren().remove(vo);
		   vo.setParentNode(null);
		   this.delete(vo);
		}
	}
}
