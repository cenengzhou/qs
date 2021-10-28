package com.gammon.pcms.web.controller;

import com.gammon.pcms.model.ROC;
import com.gammon.pcms.model.ROC_CLASS_DESC_MAP;
import com.gammon.pcms.model.ROC_DETAIL;
import com.gammon.pcms.model.ROC_SUBDETAIL;
import com.gammon.pcms.service.RocAdminService;
import com.gammon.pcms.service.RocService;
import com.gammon.pcms.wrapper.RocWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

@RestController
@RequestMapping(value = "service/roc/")
public class RocController {
	
	@Autowired
	private RocService rocService;

	@Autowired
	private RocAdminService rocAdminService;

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','getRocAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "getRocAdmin/{jobNo}/{rocCategory}/{description}", method = RequestMethod.GET)
	public ROC getRocAdmin(@PathVariable String jobNo, @PathVariable String rocCategory, @PathVariable String description){
		return rocAdminService.getRocAdmin(jobNo, rocCategory, description);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','getRocDetailListAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "getRocDetailListAdmin/{jobNo}/{rocCategory}/{description}", method = RequestMethod.GET)
	public List<ROC_DETAIL> getRocDetailListAdmin(@PathVariable String jobNo, @PathVariable String rocCategory, @PathVariable String description){
		return rocAdminService.getRocDetailListAdmin(jobNo, rocCategory, description);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','getRocSubdetailListAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "getRocSubdetailListAdmin/{jobNo}/{rocCategory}/{description}", method = RequestMethod.GET)
	public List<ROC_SUBDETAIL> getRocSubdetailListAdmin(@PathVariable String jobNo, @PathVariable String rocCategory, @PathVariable String description){
		return rocAdminService.getRocSubdetailListAdmin(jobNo, rocCategory, description);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','getRocListSummary', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getRocListSummary/{jobNo}/{year}/{month}", method = RequestMethod.GET)
	public List<RocWrapper> getRocListSummary(@PathVariable String jobNo, @PathVariable int year, @PathVariable int month){
		return rocService.getRocWrapperList(jobNo, year, month);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','getRocSubdetailList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getRocSubdetailList/{jobNo}/{rocId}/{year}/{month}", method = RequestMethod.GET)
	public List<ROC_SUBDETAIL> getRocSubdetailList(@PathVariable String jobNo, @PathVariable Long rocId, @PathVariable int year, @PathVariable int month){
		return rocService.getRocSubdetailList(jobNo, rocId, year, month);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','getRocClassDescMap', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getRocClassDescMap", method = RequestMethod.GET)
	public List<ROC_CLASS_DESC_MAP> getRocClassDescMap(){
		return rocService.getRocClassDescMap();
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','getRocCategoryList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getRocCategoryList", method = RequestMethod.GET)
	public List<String> getRocCategoryList(){
		return rocService.getRocCategoryList();
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','getImpactList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getImpactList", method = RequestMethod.GET)
	public List<String> getImpactList(){
		return rocService.getImpactList();
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','getStatusList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getStatusList", method = RequestMethod.GET)
	public List<String> getStatusList(){
		return rocService.getStatusList();
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','getRocHistory', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getRocHistory/{rocId}", method = RequestMethod.GET)
	public List<ROC> getRocHistory(@PathVariable Long rocId){
		return rocService.getRocHistory(rocId);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','getRocDetailHistory', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getRocDetailHistory/{rocDetailId}", method = RequestMethod.GET)
	public List<ROC_DETAIL> getRocDetailHistory(@PathVariable Long rocDetailId){
		return rocService.getRocDetailHistory(rocDetailId);
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','addRoc', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "addRoc", method = RequestMethod.POST)
	public String addRoc(@RequestParam(required = true) String jobNo, @RequestBody ROC roc){
		String result = "";
		try{
			result = rocService.addRoc(jobNo, roc, null, 0, 0);
		}catch(Exception e){
			result  = "ROC cannot be created.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','updateRoc', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "updateRoc", method = RequestMethod.POST)
	public String updateRoc(@RequestParam(required = true) String jobNo, @RequestBody ROC roc){
		String result = "";
		try{
			result = rocService.updateRoc(jobNo, roc, 0, 0);
		}catch(Exception e){
			result  = "ROC cannot be created.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','saveSubdetailList', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "saveSubdetailList", method = RequestMethod.POST)
	public String saveSubdetailList(@RequestParam(required = true) String jobNo,
									@RequestParam(required = true) Long rocId,
									@RequestParam(required = true) int year,
									@RequestParam(required = true) int month,
									@RequestBody List<ROC_SUBDETAIL> subdetailList){
		String result = "";
		try{
			result = rocService.saveSubdetailList(jobNo, rocId, subdetailList, year, month);
		}catch(Exception e){
			result  = "ROC Subdetails cannot be saved.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','recalculateRoc', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "recalculateRoc", method = RequestMethod.POST)
	public String recalculateRoc(@RequestParam(required = true) String jobNo, @RequestParam(required = true) int year, @RequestParam(required = true) int month){
		String result = "";
		try{
			result = rocService.recalculateRoc(jobNo, year, month);
		}catch(Exception e){
			result  = "ROC cannot be recalculated.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','updateRocAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateRocAdmin", method = RequestMethod.POST)
	public String updateRocAdmin(@RequestParam(required = true) String jobNo, @RequestBody ROC roc){
		String result = "";
		try{
			result = rocAdminService.updateRocAdmin(jobNo, roc);
		}catch(Exception e){
			result  = "ROC cannot be updated.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','updateRocDetailListAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateRocDetailListAdmin", method = RequestMethod.POST)
	public String updateRocDetailListAdmin(@RequestParam(required = true) String jobNo, @RequestBody List<ROC_DETAIL> rocDetailList){
		String result = "";
		try{
			result = rocAdminService.updateRocDetailListAdmin(jobNo, rocDetailList);
		}catch(Exception e){
			result  = "ROC Detail cannot be updated.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','updateRocSubdetailListAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateRocSubdetailListAdmin", method = RequestMethod.POST)
	public String updateRocSubdetailListAdmin(@RequestParam(required = true) String jobNo, @RequestBody List<ROC_SUBDETAIL> rocSubdetailList){
		String result = "";
		try{
			result = rocAdminService.updateRocSubdetailListAdmin(jobNo, rocSubdetailList);
		}catch(Exception e){
			result  = "ROC Subdetail annot be updated.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','saveRocDetails', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "saveRocDetails", method = RequestMethod.POST)
	public String saveRocDetails(@RequestParam(required = true) String jobNo, @RequestBody List<RocWrapper> rocWrapperList, @RequestParam(required = true) int year, @RequestParam(required = true) int month){
		String result = "";
		try{
			result = rocService.saveRocDetails(jobNo, rocWrapperList, year, month);
		}catch(Exception e){
			result  = "ROC cannot be created.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','deleteRocDetailListAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "deleteRocDetailListAdmin", method = RequestMethod.POST)
	public String deleteRocDetailListAdmin(@RequestParam(required = true) String jobNo, @RequestBody List<ROC_DETAIL> rocDetailList){
		String result = "";
		try{
			result = rocAdminService.deleteRocDetailListAdmin(jobNo, rocDetailList);
		}catch(Exception e){
			result  = "ROC Detail List cannot be deleted.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','deleteRocSubdetailListAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "deleteRocSubdetailListAdmin", method = RequestMethod.POST)
	public String deleteRocSubdetailListAdmin(@RequestParam(required = true) String jobNo, @RequestBody List<ROC_SUBDETAIL> rocSubdetailList){
		String result = "";
		try{
			result = rocAdminService.deleteRocSubdetailListAdmin(jobNo, rocSubdetailList);
		}catch(Exception e){
			result  = "ROC Subdetail List cannot be deleted.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isRoleExisted('RocController','deleteRocAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "deleteRocAdmin", method = RequestMethod.POST)
	public String deleteRocAdmin(@RequestParam(required = true) String jobNo, @RequestBody ROC roc){
		String result = "";
		try{
			result = rocAdminService.deleteRocAdmin(jobNo, roc);
		}catch(Exception e){
			result  = "ROC cannot be deleted.";
			e.printStackTrace();
			if(e instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause() instanceof AccessDeniedException)
				throw new AccessDeniedException(((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause().getMessage());
		}
		return result;
	}


}
