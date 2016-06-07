package com.gammon.pcms.web.session;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Profile(value = "SPRING_SESSION")
@Component(value = "SessionInterceptor")
public class SpringSessionInterceptor extends HandlerInterceptorAdapter {
	
}
