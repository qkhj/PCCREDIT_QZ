package com.cardpay.pccredit.afterloan.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.afterloan.filter.AfterLoanCheckFilter;
import com.cardpay.pccredit.afterloan.model.AfterLoaninfo;
import com.cardpay.pccredit.afterloan.service.AfterloanCheckService;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

/** 
 * @author chinh
 * @version 创建时间：2015年7月31日 下午11:44:14 
 * 程序的简单说明 
 */
@Controller
@RequestMapping("/afterloan/afterloanapprove/*")
@JRadModule("afterloan.afterloanapprove")
public class AfterLoanCheckApproveController extends BaseController{

	@Autowired
	private AfterloanCheckService afterloanCheckService;
	/**
	 * 贷后检查任务查看(审核)
	 * 
	 * @param filter 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "approve.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView approve(@ModelAttribute AfterLoanCheckFilter filter,HttpServletRequest request){
		filter.setRequest(request);
//		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
//		String userId = user.getId();
//		filter.setUserId(userId);
		QueryResult<AfterLoaninfo> result = afterloanCheckService.findAfterLoanCheckTaskToByFilter(filter);
		JRadPagedQueryResult<AfterLoaninfo> pagedResult = new JRadPagedQueryResult<AfterLoaninfo>(
				filter, result);
		JRadModelAndView mv = new JRadModelAndView(
				"/afterloan/afterloan_approve", request);
		mv.addObject(PAGED_RESULT, pagedResult);

		return mv;
	}
}
