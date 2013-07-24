package com.minioffice.user.dwr;

import net.sf.json.JSONObject;

import org.activiti.engine.identity.User;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@RemoteProxy
public class UserDwr {
	private final static Logger logger = LoggerFactory.getLogger(UserDwr.class);

	@Autowired
	private ProcessEngineFactoryBean processEngineFactoryBean;// 流程操作部件

	//保存用户
	public JSONObject saveUser(String loginid,String username, String password,String email) {
		boolean result =true;
		String msg ="";
		try {
			String id = processEngineFactoryBean
					.getProcessEngineConfiguration().getIdGenerator()
					.getNextId();
			User u = processEngineFactoryBean.getProcessEngineConfiguration()
					.getIdentityService().newUser(id);

			u.setPassword(password);//密码
			u.setFirstName(loginid);//登录名
			u.setLastName(username);//名称
			u.setEmail(email);
			processEngineFactoryBean.getProcessEngineConfiguration()
					.getIdentityService().saveUser(u);//保存用户
		} catch (Exception e) {
			logger.error("", e);
			msg =e.getLocalizedMessage();
			result= false;
		}

		return new JSONObject().element("result", result).element("msg", msg);
	}
	
	//取得用户
	    public JSONObject getUser(String id){
	    	
	    	User u =null;
	    	try {
	    	u=processEngineFactoryBean.getProcessEngineConfiguration()
					.getIdentityService().createUserQuery().userId(id).singleResult();
	    	} catch (Exception e) {
				logger.error("", e);
			}
	    	if(u!=null){
	    		return JSONObject.fromObject(u);
	    	}
	    	return null;
	    }
	
	//更新用户
		public JSONObject updateUser(String id,String loginid,String username, String password,String email) {
			boolean result =true;
			String msg ="";
			try {
				User u = processEngineFactoryBean.getProcessEngineConfiguration()
						.getIdentityService().createUserQuery().userId(id).singleResult();

				u.setPassword(password);//密码
				u.setFirstName(loginid);//登录名
				u.setLastName(username);//名称
				u.setEmail(email);
				processEngineFactoryBean.getProcessEngineConfiguration()
						.getIdentityService().saveUser(u);//保存用户
			} catch (Exception e) {
				logger.error("", e);
				msg =e.getLocalizedMessage();
				result= false;
			}

			return new JSONObject().element("result", result).element("msg", msg);
		}
}
