package com.minioffice.common.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import com.minioffice.common.dao.ITreeDAO;
import com.minioffice.orm.hibernate.HibernateDao;

public  class TreeDAOImpl<T> extends HibernateDao<T, String>  implements  ITreeDAO<T>{

	private Class<?> getEntityClass(){
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		Class<?> entityClass = (Class<?>) params[0];
		return entityClass;
	}
	public List<T> getRootChildren() throws Exception {
		// TODO Auto-generated method stub
		
		return this.find("from "+this.getEntityClass().getName()+" where parentNode is null");
	}

	public List<T> getChildren(HashMap<String,Object> params) throws Exception {
		// TODO Auto-generated method stub
		String pid=(String)params.get("pid");
		if(pid!=null){
			T p= this.get(pid);
			
			if(p!=null){
				return this.find("from "+this.getEntityClass().getName()+" where parentNode =?",p);
			}
		}
		
		return null;
	}
}
