package com.cardpay.pccredit.QZBankInterface.web;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.QZBankInterface.filter.EcifFilter;
import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.QZBankInterface.model.ECIF;
import com.cardpay.pccredit.QZBankInterface.service.CircleService;
import com.cardpay.pccredit.QZBankInterface.service.ECIFService;
import com.cardpay.pccredit.customer.constant.CustomerInforConstant;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.customer.web.CustomerInforForm;
import com.cardpay.pccredit.datapri.constant.DataPriConstants;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationInfo;
import com.cardpay.pccredit.intopieces.model.QzApplnJyd;
import com.cardpay.pccredit.intopieces.service.CustomerApplicationInfoService;
import com.cardpay.pccredit.intopieces.service.IntoPiecesService;
import com.cardpay.pccredit.ipad.util.JsonDateValueProcessor;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.constant.JRadConstants;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
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

/** 
 * @author 贺珈 
 * @version 创建时间：2015年4月14日 下午5:44:14 
 * 程序的简单说明 
 */
@Controller
@RequestMapping("/intopieces/circle/*")
@JRadModule("intopieces.circle")
public class IESBForCircleController extends BaseController{
	@Autowired
	private CircleService circleService;
	
	@Autowired
	private ECIFService eCIFService; 
	
	@Autowired
	private IntoPiecesService intoPiecesService;
	
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
	public AbstractModelAndView browse(@ModelAttribute EcifFilter filter,
			HttpServletRequest request) {
		filter.setRequest(request);
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String userId = user.getId();
		filter.setUserId(userId);
		QueryResult<Circle> result = circleService.findCircleByFilter(filter);
		JRadPagedQueryResult<Circle> pagedResult = new JRadPagedQueryResult<Circle>(
				filter, result);

		JRadModelAndView mv = new JRadModelAndView(
				"/qzbankinterface/iesbforcircle_browse", request);
		mv.addObject(PAGED_RESULT, pagedResult);

		return mv;
	}
	
	/*
	 * 跳转到修改客户信息页面
	 * 
	 * @param request
	 * @return
	*/
	@ResponseBody
	@RequestMapping(value = "display.page")
	public AbstractModelAndView display(HttpServletRequest request) {        
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/iesbforcircle_display", request);
		
		String customerId = request.getParameter(ID);
		String appId = request.getParameter("appId");
		String operate = request.getParameter("operate");
		IESBForECIFReturnMap ecif = eCIFService.findEcifByCustomerId(customerId);
		mv.addObject("ecif",ecif);
				
		Circle circle = null;
		if(appId != null && !appId.equals("")){
			circle = circleService.findCircleByAppId(appId);
		}else{
			circle = circleService.findCircle(customerId,null);
		}
		mv.addObject("circle",circle);
		mv.addObject("operate",operate);
		mv.addObject("appId",appId);
		mv.addObject("returnUrl",intoPiecesService.getReturnUrl(operate));
		return mv;
	}
	
	/*
	 * 跳转到增加客户信息页面
	 * 
	 * @param request
	 * @return
	*/
	@ResponseBody
	@RequestMapping(value = "create_or_change.page")
	public AbstractModelAndView create(HttpServletRequest request) { 
		JRadModelAndView mv = null;
		
		String customerId = request.getParameter(ID);
		String appId = request.getParameter("appId");
		String operate = request.getParameter("operate");
		IESBForECIFReturnMap ecif = eCIFService.findEcifByCustomerId(customerId);
		JSONObject json = new JSONObject();
		json = JSONObject.fromObject(ecif);
		//获取决议单信息
		QzApplnJyd jyd = intoPiecesService.getJydByCustomerId(customerId, appId);
		//Circle circle = circleService.findCircleByClientNo(ecif.getClientNo());
		
		Circle circle = null;
		if(appId != null && !appId.equals("")){
			circle = circleService.findCircleByAppId(appId);
		}else{
			circle = circleService.findCircle(customerId,null);
		}
		if(circle == null){
			mv = new JRadModelAndView("/qzbankinterface/iesbforcircle", request);
			//查找登录用户信息
			IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
			String orgId = user.getOrganization().getId();//机构ID
			String parentOrgId = user.getOrganization().getParentId();//机构ID
//			if(parentOrgId.equals("000000")){//替换为泉州总行id
				parentOrgId = Constant.QZ_ORG_ROOT_ID;
//			}
			String externalId = user.getLogin();//工号
			mv.addObject("orgId",orgId);
			mv.addObject("parentOrgId",parentOrgId);
			mv.addObject("externalId",externalId);

			QzApplnJyd qzSdhjyd = intoPiecesService.getSdhjydForm(customerId);
			mv.addObject("qzSdhjyd",qzSdhjyd);
			
		}
		else{
			mv = new JRadModelAndView("/qzbankinterface/iesbforcircle_change", request);
			mv.addObject("circle",circle);
		}
		
		mv.addObject("customerId",customerId);
		mv.addObject("ecif",json);
		mv.addObject("operate",operate);
		mv.addObject("appId",appId);
		mv.addObject("jyd",jyd);
		mv.addObject("returnUrl",intoPiecesService.getReturnUrl(operate));
		
		return mv;
	}
	
	/**
	 * 执行添加客户信息
	 * @param customerinfoForm
	 * @param request
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "insert.json")
	public JRadReturnMap insert(@ModelAttribute IESBForCircleForm iesbForCircleForm, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				//设置级联选项 级联未考虑空情况，已注释
				if(iesbForCircleForm.getLoanKind_1() != null){
					iesbForCircleForm.setLoanKind(iesbForCircleForm.getLoanKind_1().split("_")[1]);
				}
				if(iesbForCircleForm.getLoanKind_2() != null){
					iesbForCircleForm.setLoanKind(iesbForCircleForm.getLoanKind_2().split("_")[1]);
				}
				if(iesbForCircleForm.getLoanKind_3() != null){
					iesbForCircleForm.setLoanKind(iesbForCircleForm.getLoanKind_3().split("_")[1]);
				}
				if(iesbForCircleForm.getLoanKind_4() != null){
					iesbForCircleForm.setLoanKind(iesbForCircleForm.getLoanKind_4().split("_")[1]);
				}
				
				if(iesbForCircleForm.getAgriLoanKind_1() != null){
					iesbForCircleForm.setAgriLoanKind(iesbForCircleForm.getAgriLoanKind_1().split("_")[1]);
				}
				if(iesbForCircleForm.getAgriLoanKind_2() != null){
					iesbForCircleForm.setAgriLoanKind(iesbForCircleForm.getAgriLoanKind_2().split("_")[1]);
				}
				if(iesbForCircleForm.getAgriLoanKind_3() != null){
					iesbForCircleForm.setAgriLoanKind(iesbForCircleForm.getAgriLoanKind_3().split("_")[1]);
				}
				if(iesbForCircleForm.getAgriLoanKind_4() != null){
					iesbForCircleForm.setAgriLoanKind(iesbForCircleForm.getAgriLoanKind_4().split("_")[1]);
				}
				if(iesbForCircleForm.getAgriLoanKind_5() != null){
					iesbForCircleForm.setAgriLoanKind(iesbForCircleForm.getAgriLoanKind_5().split("_")[1]);
				}
				
				iesbForCircleForm.setLoanDirection(iesbForCircleForm.getLoanDirection_4().split("_")[1]);
				
				if(iesbForCircleForm.getLoanBelong1_1() != null){
					iesbForCircleForm.setLoanBelong1(iesbForCircleForm.getLoanBelong1_1().split("_")[1]);
				}
				if(iesbForCircleForm.getLoanBelong1_2() != null){
					iesbForCircleForm.setLoanBelong1(iesbForCircleForm.getLoanBelong1_2().split("_")[1]);
				}
				if(iesbForCircleForm.getLoanBelong1_3() != null){
					iesbForCircleForm.setLoanBelong1(iesbForCircleForm.getLoanBelong1_3().split("_")[1]);
				}
				if(iesbForCircleForm.getLoanBelong1_4() != null){
					iesbForCircleForm.setLoanBelong1(iesbForCircleForm.getLoanBelong1_4().split("_")[1]);
				}
				if(iesbForCircleForm.getLoanBelong1_5() != null){
					iesbForCircleForm.setLoanBelong1(iesbForCircleForm.getLoanBelong1_5().split("_")[1]);
				}
				
				iesbForCircleForm.setRegPermResidence(iesbForCircleForm.getRegPermResidence_3().split("_")[1]);
				
				//替换为总行id
				if(iesbForCircleForm.getHigherOrgNo().equals(Constant.QZ_ORG_ROOT_ID)){
					iesbForCircleForm.setHigherOrgNo("000000");
				}
				
				Circle circle = iesbForCircleForm.createModel(Circle.class);
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				circle.setCreatedBy(user.getId());
				circle.setUserId(user.getId());
				circle.setCustomerId(request.getParameter("customerId"));
				circleService.insertCustomerInforCircle(circle);
				
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
	
	/**
	 * 执行添加客户信息
	 * @param customerinfoForm
	 * @param request
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "update.json")
	public JRadReturnMap update(@ModelAttribute IESBForCircleForm iesbForCircleForm, HttpServletRequest request) {
		String circleId = request.getParameter(ID);
		
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				//设置级联选项 级联未考虑空情况，已注释
				if(iesbForCircleForm.getLoanKind_1() != null){
					iesbForCircleForm.setLoanKind(iesbForCircleForm.getLoanKind_1().split("_")[1]);
				}
				if(iesbForCircleForm.getLoanKind_2() != null){
					iesbForCircleForm.setLoanKind(iesbForCircleForm.getLoanKind_2().split("_")[1]);
				}
				if(iesbForCircleForm.getLoanKind_3() != null){
					iesbForCircleForm.setLoanKind(iesbForCircleForm.getLoanKind_3().split("_")[1]);
				}
				if(iesbForCircleForm.getLoanKind_4() != null){
					iesbForCircleForm.setLoanKind(iesbForCircleForm.getLoanKind_4().split("_")[1]);
				}
				
				if(iesbForCircleForm.getAgriLoanKind_1() != null){
					iesbForCircleForm.setAgriLoanKind(iesbForCircleForm.getAgriLoanKind_1().split("_")[1]);
				}
				if(iesbForCircleForm.getAgriLoanKind_2() != null){
					iesbForCircleForm.setAgriLoanKind(iesbForCircleForm.getAgriLoanKind_2().split("_")[1]);
				}
				if(iesbForCircleForm.getAgriLoanKind_3() != null){
					iesbForCircleForm.setAgriLoanKind(iesbForCircleForm.getAgriLoanKind_3().split("_")[1]);
				}
				if(iesbForCircleForm.getAgriLoanKind_4() != null){
					iesbForCircleForm.setAgriLoanKind(iesbForCircleForm.getAgriLoanKind_4().split("_")[1]);
				}
				if(iesbForCircleForm.getAgriLoanKind_5() != null){
					iesbForCircleForm.setAgriLoanKind(iesbForCircleForm.getAgriLoanKind_5().split("_")[1]);
				}
				
				iesbForCircleForm.setLoanDirection(iesbForCircleForm.getLoanDirection_4().split("_")[1]);
				
				if(iesbForCircleForm.getLoanBelong1_1() != null){
					iesbForCircleForm.setLoanBelong1(iesbForCircleForm.getLoanBelong1_1().split("_")[1]);
				}
				if(iesbForCircleForm.getLoanBelong1_2() != null){
					iesbForCircleForm.setLoanBelong1(iesbForCircleForm.getLoanBelong1_2().split("_")[1]);
				}
				if(iesbForCircleForm.getLoanBelong1_3() != null){
					iesbForCircleForm.setLoanBelong1(iesbForCircleForm.getLoanBelong1_3().split("_")[1]);
				}
				if(iesbForCircleForm.getLoanBelong1_4() != null){
					iesbForCircleForm.setLoanBelong1(iesbForCircleForm.getLoanBelong1_4().split("_")[1]);
				}
				if(iesbForCircleForm.getLoanBelong1_5() != null){
					iesbForCircleForm.setLoanBelong1(iesbForCircleForm.getLoanBelong1_5().split("_")[1]);
				}
				
				iesbForCircleForm.setRegPermResidence(iesbForCircleForm.getRegPermResidence_3().split("_")[1]);
				
				//替换为总行id
				if(iesbForCircleForm.getHigherOrgNo().equals(Constant.QZ_ORG_ROOT_ID)){
					iesbForCircleForm.setHigherOrgNo("000000");
				}
				
				Circle circle = iesbForCircleForm.createModel(Circle.class);
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				circle.setCreatedBy(user.getId());
				circle.setUserId(user.getId());
				circle.setId(circleId);
				circle.setCustomerId(request.getParameter("customerId"));
				circleService.updateCustomerInforCircle(circle);
//				returnMap.put(RECORD_ID, id);
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
	
}

