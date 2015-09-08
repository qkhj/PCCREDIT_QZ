package com.cardpay.pccredit.QZBankInterface.web;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.cardpay.pccredit.customer.filter.CustomerInforFilter;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.customer.service.CustomerInforService;
import com.cardpay.pccredit.datapri.constant.DataPriConstants;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationInfo;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqb;
import com.cardpay.pccredit.intopieces.service.YwsqbService;
import com.ctc.wstx.util.StringUtil;
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
	private CircleService circleService;
	
	@Autowired
	private YwsqbService ywsqbService;
	
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
		QueryResult<CustomerInfor> result = customerInforservice.findCustomerInfoWithEcifByFilter(filter);
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
		ECIF ecif = ecifService.findEcifByCustomerId(customerId);
		mv.addObject("ecif",ecif);
		return mv;
	}
	
	/*
	 * 跳转到修改客户信息页面
	 * 
	 * @param request
	 * @return
	*/
	@ResponseBody
	@RequestMapping(value = "update.page")
	public AbstractModelAndView update(HttpServletRequest request) {        
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/iesbforecif_update", request);
		
		String customerId = request.getParameter(ID);
		ECIF ecif = ecifService.findEcifByCustomerId(customerId);
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
	
	/**
	 * 执行修改客户信息
	 * @param customerinfoForm
	 * @param request
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "update.json")
	public JRadReturnMap updateCustomerInfo(@ModelAttribute IESBForECIFForm iesbForECIFForm, HttpServletRequest request) {
		SimpleDateFormat formatter10 = new SimpleDateFormat("yyyy-MM-dd");
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				//设置级联选项
				iesbForECIFForm.setRegPermResidence(iesbForECIFForm.getRegPermResidence_3().split("_")[1]);
				iesbForECIFForm.setCity(iesbForECIFForm.getCity_3().split("_")[1]);
				//获取用户id
				String customerId = request.getParameter("customerId");
				System.out.println(customerId);
				
				ECIF ecif = ecifService.findEcifByCustomerId(customerId.trim());
				
				//发送请求到ecif修改客户信息,客户信息表有客户号表示在核心开户成功，这个时候才要同步核心数据
				if(StringUtils.isNotEmpty(ecif.getClientNo())){
					boolean falg = ecifService.updateBasicCustomerInfo(iesbForECIFForm, ecif.getClientNo());
					if(!falg){
						returnMap.put(JRadConstants.SUCCESS, false);
						returnMap.put(JRadConstants.MESSAGE, "核心修改客户信息失败");
						return returnMap;
					}
				}
				
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				System.out.println(ecif.getGlobalId());
				//写入数据到basic_customer_information表
				CustomerInfor info = customerInforservice.findCustomerInforByCustomerId(customerId.trim());
				if(info == null){
					info = new CustomerInfor();
				
				}else{
					//判断该用户的贷款信息是否存在
					List<Circle> circlelist = circleService.findCircleByCardNo(info.getCardId());
					if(circlelist != null){
						for(int i = 0; i < circlelist.size(); i++){
							Circle circle = circlelist.get(i);
							circle.setGlobalId(iesbForECIFForm.getGlobalId());
							circle.setClientName(iesbForECIFForm.getClientName());
							circleService.updateCustomerInforCircle(circle);
						}
					}
					//判断QZ_APPLN_YWSQB 是否有数据
					List<QzApplnYwsqb> qzApplnYwsqblist = ywsqbService.findYwsqbforCustomerId(customerId.trim());
					if(qzApplnYwsqblist != null){
						for(int i = 0; i < qzApplnYwsqblist.size(); i++){
							QzApplnYwsqb qzApplnYwsqb = qzApplnYwsqblist.get(i);
							qzApplnYwsqb.setGlobalId(iesbForECIFForm.getGlobalId());
							qzApplnYwsqb.setGlobalType(iesbForECIFForm.getGlobalType());
							qzApplnYwsqb.setName(iesbForECIFForm.getClientName());
							ywsqbService.updateYwsqb(qzApplnYwsqb);
						}
					}
				}
				info.setUserId(user.getId());
				info.setChineseName(iesbForECIFForm.getClientName());
				info.setBirthday(formatter10.format(iesbForECIFForm.getBirthDate()));
				info.setNationality("NTC00000000156");
				info.setSex(iesbForECIFForm.getSex().equals("01") ? "Male" : "Female");
				info.setCardId(iesbForECIFForm.getGlobalId());
				
				ecif.setCreatedBy(user.getId());
				ecif.setUserId(user.getId());
				ecif.setGlobalId(iesbForECIFForm.getGlobalId());
				ecif.setGlobalDesc(iesbForECIFForm.getGlobalDesc());
				ecif.setGlobalType(iesbForECIFForm.getGlobalType());
				ecif.setAddress(iesbForECIFForm.getAddress());
				ecif.setAddressType(iesbForECIFForm.getAddressType());
				ecif.setAreaCode(iesbForECIFForm.getAreaCode());
				ecif.setBirthDate(iesbForECIFForm.getBirthDate());
				ecif.setCertAreaCode(iesbForECIFForm.getCertAreaCode());
				ecif.setCertOrg(iesbForECIFForm.getCertOrg());
				ecif.setCity(iesbForECIFForm.getCity());
				ecif.setCity_1(iesbForECIFForm.getCity_1());
				ecif.setCity_2(iesbForECIFForm.getCity_2());
				ecif.setCity_3(iesbForECIFForm.getCity_3());
				ecif.setClientBelongOrg(iesbForECIFForm.getClientBelongOrg());
				ecif.setClientName(iesbForECIFForm.getClientName());
				ecif.setClientNameType(iesbForECIFForm.getClientNameType());
				ecif.setClientType(iesbForECIFForm.getClientType());
				ecif.setClientStatus(iesbForECIFForm.getClientStatus());
				ecif.setCompanyName(iesbForECIFForm.getCompanyName());
				ecif.setContactMode(iesbForECIFForm.getContactMode());
				ecif.setContactModeType(iesbForECIFForm.getContactModeType());
				ecif.setCountryCitizen(iesbForECIFForm.getCountryCitizen());
				ecif.setCustManagerId(iesbForECIFForm.getCustManagerId());

						
				ecifService.updateCustomerInfor(ecif,info);
				
//				returnMap.put(RECORD_ID, id);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
			}catch (Exception e) {
				e.printStackTrace();
				returnMap.put(JRadConstants.MESSAGE, "修改失败");
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

