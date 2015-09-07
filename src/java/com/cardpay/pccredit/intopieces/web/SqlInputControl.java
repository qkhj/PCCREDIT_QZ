package com.cardpay.pccredit.intopieces.web;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.common.UploadFileTool;
import com.cardpay.pccredit.customer.constant.CustomerInforConstant;
import com.cardpay.pccredit.datapri.constant.DataPriConstants;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.service.SqlInputService;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.constant.JRadConstants;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.utility.WebRequestHelper;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

@Controller
@RequestMapping("/intopieces/sqlinputupdate/*")
@JRadModule("intopieces.sqlinputupdate")
public class SqlInputControl extends BaseController {
	
	Logger logger = Logger.getLogger(SqlInputControl.class);
	@Autowired
	private SqlInputService sqlInputService;
	/**
	 * 浏览页面
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "create.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.CREATE)
	public AbstractModelAndView browse(HttpServletRequest request){
		JRadModelAndView mv = new JRadModelAndView("/intopieces/sqlInputforUpdate", request);
		return mv;
	}
	/**
	 * 执行sql操作
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "insert.json")
	@JRadOperation(JRadOperation.CREATE)
	public JRadReturnMap change(HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try{
				//获取页面传输的值
				String sqlText = request.getParameter("sqltext");
				System.out.println("输入的sql语句为：" + sqlText);
				//执行sql操作
				boolean flag = sqlInputService.changeDatabase(sqlText);
				returnMap.put(JRadConstants.SUCCESS, flag);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
				
			}catch (Exception e) {
				returnMap.put(JRadConstants.MESSAGE, DataPriConstants.SYS_EXCEPTION_MSG);
				returnMap.put(JRadConstants.SUCCESS, false);
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			
		}
		return returnMap;
	}
	
	/**
	 * 执行exe文件下载
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "ipad/sunds_ocx.page", method = {RequestMethod.GET})
	public void dowload(HttpServletRequest request, HttpServletResponse response){
		try{
			
			 //UploadFileTool.downLoadFile(response, "d:/sunds_ocx.exe", "sunds_ocx.exe");//tomcat测试 
			 UploadFileTool.downLoadFile(response, Constant.OCX_FILEPATH, "sunds_ocx.exe");//生产环境
			
			
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}
	
	
}
