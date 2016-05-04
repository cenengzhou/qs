package com.gammon.qs.webservice.serviceProvider.checkJobIsConverted;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.qs.domain.Job;
import com.gammon.qs.service.JobService;

public class MarshallingCheckJobIsConvertedEndpoint extends
		AbstractMarshallingPayloadEndpoint {

	private JobService jobRepository;
	
	public MarshallingCheckJobIsConvertedEndpoint(JobService jobRepository, Marshaller marshaller){
		super(marshaller);
		this.jobRepository = jobRepository;
	}

	protected Object invokeInternal(Object request) throws Exception {
		CheckJobIsConvertedRequest requestObj = (CheckJobIsConvertedRequest)request;
		CheckJobIsConvertedResponse responseObj = new CheckJobIsConvertedResponse();
		Job job = jobRepository.obtainJob(requestObj.getJobNumber());
		if (job !=null && job.getConversionStatus() != null)
			responseObj.setConverted(true);
		else
			responseObj.setConverted(false);
		
		return responseObj;
	}

}
