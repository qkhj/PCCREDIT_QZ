package com.cardpay.pccredit.intopieces.web;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.QZBankInterface.service.CircleService;
import com.cardpay.pccredit.customer.constant.CustomerInforConstant;
import com.cardpay.pccredit.customer.filter.CustomerInforFilter;
import com.cardpay.pccredit.intopieces.filter.QuotaFreezeOrThawFilter;
import com.cardpay.pccredit.intopieces.model.IntoPieces;
import com.cardpay.pccredit.intopieces.model.QuotaFreezeInfo;
import com.cardpay.pccredit.intopieces.service.QuotaFreezeOrThawService;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

@Controller
@RequestMapping("/intopieces/quotafreezeorthaw*")
@JRadModule("intopieces.quotafreezeorthaw")
public class QuataFreezeOrThawControl extends BaseController{
	
	@Autowired
	public QuotaFreezeOrThawService quotaFreezeOrThawService;
	
	@Autowired
	public CircleService circleService;
	
	@Autowired
	public CommonDao commonDao;
	
	private static final Logger logger = Logger.getLogger(QuataFreezeOrThawControl.class);
	
	/**
	 *额度冻结/解冻
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "browse.page", method = { RequestMethod.GET })
	public AbstractModelAndView browse(@ModelAttribute QuotaFreezeOrThawFilter filter, HttpServletRequest request) throws SQLException {
		filter.setRequest(request);
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String loginId = user.getId();
		filter.setUserId(loginId);
		QueryResult<QuotaFreezeInfo> result = quotaFreezeOrThawService.getQzIesbForCircleByFilter(filter);
		JRadPagedQueryResult<QuotaFreezeInfo> pagedResult = new JRadPagedQueryResult<QuotaFreezeInfo>(filter, result);
		JRadModelAndView mv = new JRadModelAndView("/intopieces/quotafreezeorthaw_approve",
                                                    request);
		mv.addObject(PAGED_RESULT, pagedResult);

		return mv;
	}
	
	/**
	 *额度冻结/解冻/终止操作
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "quot_operate.json")
	public JRadReturnMap quot_operate(HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String retMsg = "";
				String clientNo = request.getParameter("clientNo");
				String cardNo= request.getParameter("cardNo");
				String operateType = request.getParameter("operateType");
				String loanStatus = request.getParameter("loanStatus");
				String contNo = request.getParameter("contNo");
				logger.info("[clientNo="+clientNo+"],[cardNo="+cardNo+"],[operateType="+operateType+"],[contNo=" +contNo +"]");
				//过滤操作要求
				if("10".equals(operateType)&& !("00".equals(loanStatus))){
					retMsg = "已经冻结和终止的贷款不能做冻结操作~！";
					returnMap.setSuccess(false);
					returnMap.put("retMsg", retMsg);
					return returnMap;
				}else if("20".equals(operateType) && !("10".equals(loanStatus))){
					retMsg = "只有冻结的贷款才能做解冻操作~！";
					returnMap.setSuccess(false);
					returnMap.put("retMsg", retMsg);
					return returnMap;
				}else if("30".equals(operateType) && "30".equals(loanStatus)){
					retMsg = "已经终止的贷款不需要在做终止操作~！";
					returnMap.setSuccess(false);
					returnMap.put("retMsg", retMsg);
					return returnMap;
				}
				
				//发送请求道信贷进行冻结操作
				HashMap requestMap = new HashMap();
				requestMap.put("clientNo", clientNo);
				requestMap.put("cardNo", cardNo);
				requestMap.put("operateType", operateType);
				//发送请求
				Map resultMap = quotaFreezeOrThawService.getIesbForED(requestMap);
				if(resultMap == null){
					retMsg = "交易执行异常，请联系管理员~！";
					returnMap.setSuccess(false);
					returnMap.put("retMsg", retMsg);
					return returnMap;
				}else{
					String retCode = (String)resultMap.get("RET_CODE");
					retMsg = (String)resultMap.get("RET_MSG");
					if("000000".equals(retCode)){
						//修改库表状态
						Circle circle = circleService.findCircleByClientNoAndContNo(clientNo, contNo);
						if(circle == null){
							returnMap.setSuccess(false);
							returnMap.put("retMsg", "贷款信息不存在");
							return returnMap;
						}
						returnMap.setSuccess(true);
						returnMap.addGlobalMessage(CHANGE_SUCCESS);
						if("10".equals(operateType)){
							circle.setLoanStatus("10");
							returnMap.put("retMsg", "贷款冻结成功~！");
						}else if("20".equals(operateType)){
							circle.setLoanStatus("00");
							returnMap.put("retMsg", "贷款解冻成功~！");
						}else if("30".equals(operateType)){
							circle.setLoanStatus("30");
							returnMap.put("retMsg", "合同终止成功~！");
						}
						commonDao.updateObject(circle);
						logger.info("贷款信息表更新成功");
						return returnMap;
					}else{
						returnMap.setSuccess(false);
						returnMap.put("retMsg", retMsg);
						return returnMap;
					}
				}
			}catch(Exception e){
				returnMap.setSuccess(false);
				returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
			}
		}
		return returnMap;
	}
}
