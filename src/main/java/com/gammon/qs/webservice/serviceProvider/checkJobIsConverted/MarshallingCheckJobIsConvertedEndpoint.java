package com.gammon.qs.webservice.serviceProvider.checkJobIsConverted;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.service.JobInfoService;

public class MarshallingCheckJobIsConvertedEndpoint extends
		AbstractMarshallingPayloadEndpoint {

	private JobInfoService jobRepository;
	
	public MarshallingCheckJobIsConvertedEndpoint(JobInfoService jobRepository, Marshaller marshaller){
		super(marshaller);
		this.jobRepository = jobRepository;
	}

	protected Object invokeInternal(Object request) throws Exception {
		CheckJobIsConvertedRequest requestObj = (CheckJobIsConvertedRequest)request;
		CheckJobIsConvertedResponse responseObj = new CheckJobIsConvertedResponse();
		JobInfo job = jobRepository.obtainJob(requestObj.getJobNumber());
		if (job !=null && job.getConversionStatus() != null)
			responseObj.setConverted(true);
		else
			responseObj.setConverted(false);
		
		return responseObj;
	}

}
