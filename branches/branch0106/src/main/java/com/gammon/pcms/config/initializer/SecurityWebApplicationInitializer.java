package com.gammon.pcms.config.initializer;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

	/* (non-Javadoc)
	 * @see org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer#enableHttpSessionEventPublisher()
	 */
	@Override
	protected boolean enableHttpSessionEventPublisher() {
		return true;
	}
}
