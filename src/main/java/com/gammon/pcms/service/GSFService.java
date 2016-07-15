package com.gammon.pcms.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gammon.pcms.application.User;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.pcms.dto.rs.consumer.gsf.GetJobSecurity;
import com.gammon.pcms.dto.rs.consumer.gsf.GetRole;
import com.gammon.pcms.dto.rs.consumer.gsf.JobSecurity;
import com.gammon.pcms.helper.RestTemplateHelper;

@Service
public class GSFService {

	@Autowired
	private WebServiceConfig webServiceConfig;
	@Autowired
	private RestTemplateHelper restTemplateHelper;

	public User getRole(String username) {
		User user = new User();
		String[] login = username.split("@");
		GetRole.Request roleRequest = new GetRole.Request(webServiceConfig.getGsfApplicationCode(),
				"gamska\\" + login[0]);
		try {
			ResponseEntity<GetRole.Response> roleResponse = getResponseEntity(webServiceConfig.getGsfGetRoleUrl(),
					roleRequest, GetRole.Response.class);
			user = roleResponse.getBody().getUser();
			String userImage = getImageAsBase64(webServiceConfig.getIpeopleUrl() + user.getStaffId() + ".jpg");
			;
			user.setImage(userImage);
		} catch (HttpMessageNotReadableException e) {
			e.printStackTrace();
		}
		user.setUsername(username);
		if (login.length == 2) {
			user.setAuthType("Kerberos");
			user.setDomainName(login[1]);
		}
		return user;
	}

	public List<JobSecurity> getJobSecurityList(String username) {
		String[] login = username.split("@");
		GetJobSecurity.Request jobSecurityRequest = new GetJobSecurity.Request(webServiceConfig.getGsfApplicationCode(),
				"gamska\\" + login[0]);
		ResponseEntity<GetJobSecurity.Response> jobSecurityResponse = getResponseEntity(
				webServiceConfig.getGsfGetJobSecurityUrl(), jobSecurityRequest, GetJobSecurity.Response.class);
		return jobSecurityResponse.getBody().getJobSecurityList();
	}

	public <Q, S> ResponseEntity<S> getResponseEntity(String path, Q request, Class<S> response) {
		URI gsf = null;
		try {
			gsf = new URI(path);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		RestTemplate restTemplate = restTemplateHelper.getRestTemplate(gsf.getHost(), null, null);
		return restTemplate.postForEntity(gsf.toString(), request, response);
	}

	public String getImageAsBase64(String address) {
		URL url = null;
		BufferedImage image = null;
		String result = "";
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			url = new URL(address);
			image = ImageIO.read(url);
			if (image != null) {
				ImageIO.write(image, "PNG", bos);
				result = DatatypeConverter.printBase64Binary(bos.toByteArray());
			}
		} catch (IOException e) {
			return null;
		}
		return "data:image/png;base64," + result;
	}
}
