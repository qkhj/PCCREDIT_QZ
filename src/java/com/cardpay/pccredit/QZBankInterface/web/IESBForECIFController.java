package com.cardpay.pccredit.QZBankInterface.web;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.QZBankInterface.filter.EcifFilter;
import com.cardpay.pccredit.QZBankInterface.model.ECIF;
import com.cardpay.pccredit.QZBankInterface.service.ECIFService;
import com.cardpay.pccredit.customer.constant.CustomerInforConstant;
import com.cardpay.pccredit.customer.filter.CustomerInforFilter;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.customer.service.CustomerInforService;
import com.cardpay.pccredit.datapri.constant.DataPriConstants;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationInfo;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.constant.JRadConstants;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.jrad.base.web.utility.WebRequestHelper;
import com.wicresoft.jrad.modules.privilege.business.UserManager;
import com.wicresoft.jrad.modules.privilege.filter.UserFilter;
import com.wicresoft.jrad.modules.privilege.model.User;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

/** 
 * @author 贺珈 
 * @version 创建时间：2015年4月14日 下午5:44:14 
 * 程序的简单说明 
 */
@Controller
@RequestMapping("/customer/ecif/*")
@JRadModule("customer.ecif")
public class IESBForECIFController extends BaseController{
	@Autowired
	private ECIFService ecifService;
	
	@Autowired
	private CustomerInforService customerInforservice;
	
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
	public AbstractModelAndView browse(@ModelAttribute CustomerInforFilter filter,HttpServletRequest request) {
		filter.setRequest(request);
        IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		filter.setUserId(user.getId());
		QueryResult<CustomerInfor> result = customerInforservice.findCustomerInforWithEcifByFilter(filter);
		for(int i=0;i<result.getItems().size();i++){
				CustomerApplicationInfo info = customerInforservice.ifProcess(result.getItems().get(i).getId());
				//目前存在申请件
				if(info!=null){
					if(info.getStatus().equals(Constant.APPROVED_INTOPICES)){
						result.getItems().get(i).setProcessId(Constant.APP_STATE_4);
					}else{
						result.getItems().get(i).setProcessId(Constant.APP_STATE_1);
					}
					//目前不存在申请件（初审退回）
				}else if(result.getItems().get(i).getProcessId()==null){
					result.getItems().get(i).setProcessId(Constant.APP_STATE_2);
					//没申请件
				}else{
					result.getItems().get(i).setProcessId(Constant.APP_STATE_3);
				}
			}
		JRadPagedQueryResult<CustomerInfor> pagedResult = new JRadPagedQueryResult<CustomerInfor>(filter, result);
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/iesbforecif_browse",
                                                    request);
		mv.addObject(PAGED_RESULT, pagedResult);

		return mv;
	}
	
	/*
	 * 跳转到增加客户信息页面
	 * 
	 * @param request
	 * @return
	*/
	@ResponseBody
	@RequestMapping(value = "create.page")
	public AbstractModelAndView create(HttpServletRequest request) {        
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/iesbforecif", request);
		
		//查找登录用户信息
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String orgId = user.getOrganization().getId();//机构ID
		String parentOrgId = user.getOrganization().getParentId();//机构ID
		if(parentOrgId.equals("000000")){//替换为泉州总行id
			parentOrgId = Constant.QZ_ORG_ROOT_ID;
		}
		String externalId = user.getLogin();//工号
		mv.addObject("orgId",orgId);
		mv.addObject("parentOrgId",parentOrgId);
		mv.addObject("externalId",externalId);
				
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
	public AbstractModelAndView change(HttpServletRequest request) {        
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/iesbforecif_display", request);
		
		String customerId = request.getParameter(ID);
		ECIF ecif = ecifService.findEcifByClientNo(ecifService.findEcifByCustomerId(customerId).getClientNo());
		mv.addObject("ecif",ecif);
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
	public JRadReturnMap insert(@ModelAttribute IESBForECIFForm iesbForECIFForm, HttpServletRequest request) {
		SimpleDateFormat formatter10 = new SimpleDateFormat("yyyy-MM-dd");
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				//设置级联选项
				iesbForECIFForm.setRegPermResidence(iesbForECIFForm.getRegPermResidence_3().split("_")[1]);
				iesbForECIFForm.setCity(iesbForECIFForm.getCity_3().split("_")[1]);
				
				ECIF ecif = iesbForECIFForm.createModel(ECIF.class);
				
				
				
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				//写入数据到basic_customer_information表
				CustomerInfor info = customerInforservice.findCustomerInforByCardId(ecif.getGlobalId());
				//增加判断身份证号码是否存在
				if(info != null){
					returnMap.put(JRadConstants.MESSAGE, "身份证号码已存在");
					returnMap.put(JRadConstants.SUCCESS, false);
					return returnMap;
				}
				if(info == null){
					info = new CustomerInfor();
				}
				info.setUserId(user.getId());
				info.setChineseName(ecif.getClientName());
				info.setBirthday(formatter10.format(ecif.getBirthDate()));
				info.setNationality("NTC00000000156");
				info.setSex(ecif.getSex().equals("01") ? "Male" : "Female");
				info.setCardId(ecif.getGlobalId());
				
				ecif.setCreatedBy(user.getId());
				ecif.setUserId(user.getId());
				ecifService.insertCustomerInfor(ecif,info);
				
//				returnMap.put(RECORD_ID, id);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
			}catch (Exception e) {
				e.printStackTrace();
				returnMap.put(JRadConstants.MESSAGE, "开户失败");
				returnMap.put(JRadConstants.SUCCESS, false);
				return returnMap;
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}

}

