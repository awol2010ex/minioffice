package com.minioffice.user.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.minioffice.orm.hibernate.HibernateDao;
import com.minioffice.user.dao.ITBizRoleDAO;
import com.minioffice.user.entity.TBizRoleVO;
//角色操作
@Repository
public class TBizRoleDAOImpl extends HibernateDao<TBizRoleVO, String> implements
		ITBizRoleDAO {

	@Autowired
	private SqlSession sqlSession;
	// 取得角色
	public TBizRoleVO getRole(String id) throws Exception {
		// TODO Auto-generated method stub
		return this.get(id);
	}

	// 删除角色
	public void removeRole(String id) throws Exception {
		// TODO Auto-generated method stub
		TBizRoleVO vo = this.get(id);
		this.delete(vo);

	}

	//查询角色
	public List<TBizRoleVO> searchRoleList(Map<String, Object> map, int offset,
			int pageSize) throws Exception {
		// TODO Auto-generated method stub
		RowBounds r = new RowBounds(offset, pageSize);
		return sqlSession.selectList("role.searchRoleList", map,r);
	}
	
	
	public Integer searchRoleCount(Map<String, Object> map) throws Exception{
		return (Integer)sqlSession.selectOne("role.searchRoleCount", map);
	}

}
