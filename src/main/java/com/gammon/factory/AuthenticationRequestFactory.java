package com.gammon.factory;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Helper class to create authentication token 
 */
public class AuthenticationRequestFactory {
	private static Logger logger = Logger.getLogger(AuthenticationRequestFactory.class);

	@SuppressWarnings("unchecked")
	public static <T extends AbstractAuthenticationToken> T create(String token, Class<T> cls) {
		T requestToken = null;
		if (cls.isAssignableFrom(UsernamePasswordAuthenticationToken.class)) {
			String decryptedToken = new String(DatatypeConverter.parseBase64Binary(token));
			logger.debug("decryptedToken: " + decryptedToken);
			String[] split = decryptedToken.split(":");
			logger.debug("username, password= {" + split[0] + ", " + split[1] + "}");
			String username = split[0];
			String password = split[1];

			logger.debug("creating username password token");
			requestToken = (T) new UsernamePasswordAuthenticationToken(username, password);
		}

		return requestToken;
	}
}
