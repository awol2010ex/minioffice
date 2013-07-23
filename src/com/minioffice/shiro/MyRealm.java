package com.minioffice.shiro;

import org.activiti.engine.identity.User;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

//自定义验证
public class MyRealm extends AuthorizingRealm {
	@Autowired
	private ProcessEngineFactoryBean processEngineFactoryBean;

	/**
	 * 授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 认证信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		// TODO Auto-generated method stub

		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

		String userName = token.getUsername();// 登录名
		if (userName != null && !"".equals(userName)) {

			User user = processEngineFactoryBean
					.getProcessEngineConfiguration().getIdentityService()
					.createUserQuery().userFirstName(userName).singleResult();

			if (user == null) {
				throw new UnknownAccountException();
			} else {
				if (String.valueOf(token.getPassword()).equals(
						user.getPassword())) {

					return new SimpleAuthenticationInfo(user.getId(),
							user.getPassword(), getName());
				} else {
					throw new IncorrectCredentialsException();
				}
			}

		} else {

			throw new UnknownAccountException();
		}

	}

}
