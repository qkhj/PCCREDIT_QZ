package com.cardpay.pccredit.intopieces.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.customer.constant.CustomerInforConstant;
import com.cardpay.pccredit.datapri.constant.DataPriConstants;
import com.cardpay.pccredit.intopieces.filter.ZAFilter;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqb;
import com.cardpay.pccredit.intopieces.model.QzApplnZa;
import com.cardpay.pccredit.intopieces.model.QzAppln_Za_Ywsqb_R;
import com.cardpay.pccredit.intopieces.service.YwsqbService;
import com.cardpay.pccredit.intopieces.service.ZAService;
import com.cardpay.pccredit.intopieces.service.ZA_YWSQB_R_Service;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.constant.JRadConstants;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.jrad.base.web.utility.WebRequestHelper;
import com.wicresoft.jrad.modules.privilege.model.User;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

@Controller
@RequestMapping("/product/zainformation/*")
@JRadModule("product.zainformation")
public class ZAController extends BaseController {

	@Autowired
	private ZAService zaService;
	
	@Autowired
	private YwsqbService ywsqbService;
	
	@Autowired
	private ZA_YWSQB_R_Service za_ywsqb_r_service;
	/**
	 * 浏览页面
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "browse.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView browse(@ModelAttribute ZAFilter filter, HttpServletRequest request) {
		filter.setRequest(request);

		QueryResult<QzApplnZa> result = zaService.findAllZa(filter);
		JRadPagedQueryResult<QzApplnZa> pagedResult = new JRadPagedQueryResult<QzApplnZa>(filter, result);

		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/za_browse", request);
		mv.addObject(PAGED_RESULT, pagedResult);

		return mv;
	}

	//新增专案
	@ResponseBody
	@RequestMapping(value = "create.page")
	@JRadOperation(JRadOperation.CREATE)
	public AbstractModelAndView create(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/za_create", request);
		return mv;
	}

	//新增专案
	@ResponseBody
	@RequestMapping(value = "insert.json")
	@JRadOperation(JRadOperation.CREATE)
	public JRadReturnMap insert(@ModelAttribute QzZAForm qzZAForm, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				QzApplnZa qzApplnZa = qzZAForm.createModel(QzApplnZa.class);
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				qzApplnZa.setCreatedBy(user.getId());
				qzApplnZa.setCreatedTime(new Date());
				zaService.insert(qzApplnZa);
				returnMap.put(JRadConstants.SUCCESS, true);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
			}catch (Exception e) {
				returnMap.put(JRadConstants.MESSAGE, DataPriConstants.SYS_EXCEPTION_MSG);
				returnMap.put(JRadConstants.SUCCESS, false);
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
	
	//修改专案
	@ResponseBody
	@RequestMapping(value = "change.page")
	@JRadOperation(JRadOperation.CREATE)
	public AbstractModelAndView change(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/za_change", request);
		String id = request.getParameter(ID);
		QzApplnZa qzApplnZa = zaService.findZaById(id);
		mv.addObject("qzApplnZa", qzApplnZa);
		return mv;
	}
	
	//修改专案
	@ResponseBody
	@RequestMapping(value = "update.json")
	@JRadOperation(JRadOperation.CREATE)
	public JRadReturnMap update(@ModelAttribute QzZAForm qzZAForm, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		String id = request.getParameter(ID);
		if (returnMap.isSuccess()) {
			try {
				QzApplnZa qzApplnZa = qzZAForm.createModel(QzApplnZa.class);
				qzApplnZa.setId(id);
				zaService.update(qzApplnZa);
				returnMap.put(JRadConstants.SUCCESS, true);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
			}catch (Exception e) {
				returnMap.put(JRadConstants.MESSAGE, DataPriConstants.SYS_EXCEPTION_MSG);
				returnMap.put(JRadConstants.SUCCESS, false);
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
	
	//删除专案
	@ResponseBody
	@RequestMapping(value = "delete.json")
	@JRadOperation(JRadOperation.CREATE)
	public JRadReturnMap delete(HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		String id = request.getParameter(ID);
		if (returnMap.isSuccess()) {
			try {
				List<QzAppln_Za_Ywsqb_R> za_ywsqb_r_ls = za_ywsqb_r_service.findByZaId(id);
				if(za_ywsqb_r_ls == null || za_ywsqb_r_ls.size() == 0){
					zaService.deleteZAById(id);
					returnMap.put(JRadConstants.SUCCESS, true);
					returnMap.addGlobalMessage(CREATE_SUCCESS);
				}
				else{
					returnMap.put(JRadConstants.SUCCESS, false);
					returnMap.put(JRadConstants.MESSAGE, "此专案已被关联，无法删除");
				}
			}catch (Exception e) {
				returnMap.put(JRadConstants.MESSAGE, DataPriConstants.SYS_EXCEPTION_MSG);
				returnMap.put(JRadConstants.SUCCESS, false);
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
}
