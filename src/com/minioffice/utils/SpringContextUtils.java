package com.minioffice.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtils implements ApplicationContextAware {

	private ApplicationContext applicationContext = null;

	public void setApplicationContext(ApplicationContext context) {
		if (applicationContext == null)
		{
			applicationContext = context;
		}
	}

	public ApplicationContext getApplicationContext() {
		if (applicationContext == null)
		{
			throw new IllegalStateException(
					"applicaitonContext,applicationContext.xml,SpringContextUtil");
		}
		return applicationContext;
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}
}
