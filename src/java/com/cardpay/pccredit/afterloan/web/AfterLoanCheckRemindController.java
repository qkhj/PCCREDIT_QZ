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
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

/** 
 * @author chinh
 * @version 创建时间：2015年7月31日 下午11:44:14 
 * 程序的简单说明 
 */
@Controller
@RequestMapping("/afterloan/afterloanremind/*")
@JRadModule("afterloan.afterloanremind")
public class AfterLoanCheckRemindController extends BaseController{
	
	@Autowired
	private AfterloanCheckService afterloanCheckService;
	

	
	/**
	 * 贷后检查任务查看(提醒)
	 * 
	 * @param filter 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "remind.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView remind(@ModelAttribute AfterLoanCheckFilter filter,HttpServletRequest request){
		filter.setRequest(request);
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String userId = user.getId();
		filter.setUserId(userId);
		QueryResult<AfterLoaninfo> result = afterloanCheckService.findAfterLoanCheckTaskRemindByFilter(filter);
		JRadPagedQueryResult<AfterLoaninfo> pagedResult = new JRadPagedQueryResult<AfterLoaninfo>(
				filter, result);
		JRadModelAndView mv = new JRadModelAndView(
				"/afterloan/afterloan_remind", request);
		mv.addObject(PAGED_RESULT, pagedResult);

		return mv;
	}
}
