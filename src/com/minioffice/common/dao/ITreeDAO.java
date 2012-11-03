package com.minioffice.common.dao;

import java.util.HashMap;
import java.util.List;

public interface ITreeDAO<T> {
	public List<T> getRootChildren() throws Exception;
	
	public List<T> getChildren(HashMap<String,Object> params) throws Exception;
}
