package com.minioffice.user.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//用户相关操作展示类
@Controller
@RequestMapping(value = "/user")
public class UserController {
	private final static Logger logger = LoggerFactory
			.getLogger(UserController.class);
	@Autowired
	private ProcessEngineFactoryBean processEngineFactoryBean;

	/* 登录 */
	@RequestMapping(value = "/login")
	public String login(String j_username, String j_password,
			HttpServletRequest request) {

		UsernamePasswordToken token = new UsernamePasswordToken(j_username,
				j_password);
		token.setRememberMe(true);

		Subject currentUser = SecurityUtils.getSubject();
		try {
			currentUser.login(token);
		} catch (UnknownAccountException e) {
			request.setAttribute("error", "用户不存在");
			logger.error("", e);
			return "index";
		} catch (IncorrectCredentialsException e) {
			request.setAttribute("error", "验证错误");
			logger.error("", e);
			return "index";
		} catch (LockedAccountException e) {
			request.setAttribute("error", "用户被锁住");
			logger.error("", e);
			return "index";
		} catch (ExcessiveAttemptsException e) {
			request.setAttribute("error", "多次登录不成功");
			logger.error("", e);
			return "index";
		} catch (AuthenticationException e) {
			request.setAttribute("error", "验证错误");
			logger.error("", e);
			return "index";
		}
		User user = processEngineFactoryBean.getProcessEngineConfiguration()
				.getIdentityService().createUserQuery().userFirstName(j_username)
				.singleResult();
		currentUser.getSession().setAttribute("user", user);// 保存会话

		return "main";
	}
	
	/* 注销 */
	@RequestMapping(value = "/logout")
	public String logout(String j_username, String j_password,
			HttpServletRequest request) {


		Subject currentUser = SecurityUtils.getSubject();
		try {
			currentUser.logout();
		} catch (UnknownAccountException e) {
			request.setAttribute("error", "用户不存在");
			logger.error("", e);
			return "index";
		} catch (IncorrectCredentialsException e) {
			request.setAttribute("error", "验证错误");
			logger.error("", e);
			return "index";
		} catch (LockedAccountException e) {
			request.setAttribute("error", "用户被锁住");
			logger.error("", e);
			return "index";
		} catch (ExcessiveAttemptsException e) {
			request.setAttribute("error", "多次登录不成功");
			logger.error("", e);
			return "index";
		} catch (AuthenticationException e) {
			request.setAttribute("error", "验证错误");
			logger.error("", e);
			return "index";
		}

		return "index";
	}
	
	
	@RequestMapping(value = "/list")
	public void getUserList(int page, int pagesize,
			HttpServletResponse response) {
		UserQuery query = processEngineFactoryBean
				.getProcessEngineConfiguration().getIdentityService().createUserQuery();

		try {
			JSONObject o = new JSONObject();
			o.put("Total", query.count());

			List<User> list = query.listPage(
					(page - 1) * pagesize, pagesize);
			JSONArray Rows = new JSONArray();
			if (list != null && list.size() > 0) {
				for (User p : list) {
					Rows.add(new JSONObject().element("id", p.getId())
							.element("id", p.getId())
							.element("loginid", p.getFirstName())//登录名
							.element("alias", p.getLastName())//名称
							.element("email", p.getEmail()) );//邮箱
				}
			}

			o.put("Rows", Rows);
			response.getWriter().print(o.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
	}
}
