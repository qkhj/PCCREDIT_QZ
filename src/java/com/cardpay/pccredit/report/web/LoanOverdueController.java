package com.cardpay.pccredit.report.web;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.report.filter.OClpmAccLoanFilter;
import com.cardpay.pccredit.report.model.AccLoanInfo;
import com.cardpay.pccredit.report.model.AccLoanOverdueInfo;
import com.cardpay.pccredit.report.service.AferAccLoanService;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.util.date.DateHelper;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

/**
 * 客户逾期查询清单（客户经理）
 * @author chinh
 *
 * 2014-11-27上午10:50:49
 */
@Controller
@RequestMapping("/report/loanoverdue/*")
@JRadModule("report.loanoverdue")
public class LoanOverdueController extends BaseController{
	
	@Autowired
	private AferAccLoanService aferAccLoanService;
	
	private static final Logger logger = Logger.getLogger(LoanOverdueController.class);
	
	/**
	 * 浏览客户逾期清单
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "browse.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView browse(@ModelAttribute OClpmAccLoanFilter filter, HttpServletRequest request) {
		filter.setRequest(request);
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String userId = user.getId();
		filter.setUserId(userId);
		
		List<AccLoanOverdueInfo> overdue = aferAccLoanService.getLoanOverdue(filter);
		JRadModelAndView mv = new JRadModelAndView("/report/loanoverdue/accloanoverdue_manager_browse", request);
		mv.addObject("list", overdue);
		mv.addObject("filter", filter);
		return mv;
		
	}
	
	/**
	 * 浏览客户逾期清单(卡中心)
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "browseAll.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView browseAll(@ModelAttribute OClpmAccLoanFilter filter, HttpServletRequest request) {
		filter.setRequest(request);
//		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
//		String userId = user.getId();
//		filter.setUserId(userId);
		
		List<AccLoanOverdueInfo> overdue = aferAccLoanService.getLoanOverdue(filter);
		JRadModelAndView mv = new JRadModelAndView("/report/loanoverdue/accloanoverdue_centre_browseAll", request);
		mv.addObject("list", overdue);
		mv.addObject("filter", filter);
		return mv;
		
	}
	
	/**
	 * 浏览客户逾期清单导出
	 * 
	 * @param filter
	 * @param request
	 * @param excel
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "export.page", method = { RequestMethod.GET })
	public void export(@ModelAttribute OClpmAccLoanFilter filter, HttpServletRequest request,HttpServletResponse response){
		filter.setRequest(request);
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String userId = user.getId();
		filter.setUserId(userId);
		List<AccLoanOverdueInfo> list = aferAccLoanService.getLoanOverdue(filter);
		create(list,response,filter);
	}
	/**
	 * 浏览客户逾期清单导出
	 * 
	 * @param filter
	 * @param request
	 * @param excel
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "exportAll.page", method = { RequestMethod.GET })
	public void exportAll(@ModelAttribute OClpmAccLoanFilter filter, HttpServletRequest request,HttpServletResponse response){
		filter.setRequest(request);
		
//		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
//		String userId = user.getId();
//		filter.setUserId(userId);
		List<AccLoanOverdueInfo> list = aferAccLoanService.getLoanOverdue(filter);
		create(list,response,filter);
	}
	
	public void create(List<AccLoanOverdueInfo> list,HttpServletResponse response,OClpmAccLoanFilter filter){
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("客户逾期清单");
		HSSFRow row = sheet.createRow((int) 0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("序号");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("客户经理号");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("所属机构");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("借据号");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("账户名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("利率");
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);
		cell.setCellValue("贷款日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) 7);
		cell.setCellValue("到期日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) 8);
		cell.setCellValue("授信金额");
		cell.setCellStyle(style);
		cell = row.createCell((short) 9);
		cell.setCellValue("贷款金额");
		cell.setCellStyle(style);
		cell = row.createCell((short) 10);
		cell.setCellValue("欠息总额");
		cell.setCellStyle(style);
		cell = row.createCell((short) 11);
		cell.setCellValue("起息日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) 12);
		cell.setCellValue("贷款状态");
		cell.setCellStyle(style);
		cell = row.createCell((short) 13);
		cell.setCellValue("累计逾期金额");
		cell.setCellStyle(style);
		cell = row.createCell((short) 14);
		cell.setCellValue("逾期期数");
		cell.setCellStyle(style);
		
		DecimalFormat df = new DecimalFormat("0.000000");
		DecimalFormat df1 = new DecimalFormat("0.00");
		for(int i = 0; i < list.size(); i++){
			AccLoanOverdueInfo loan = list.get(i);
			row = sheet.createRow((int) i+1);
			row.createCell((short) 0).setCellValue(loan.getRowIndex());
			row.createCell((short) 1).setCellValue(loan.getManagerId());
			row.createCell((short) 2).setCellValue(loan.getOrgName());
			row.createCell((short) 3).setCellValue(loan.getBillNo());
			row.createCell((short) 4).setCellValue(loan.getAcctName());
			row.createCell((short) 5).setCellValue(df.format(loan.getRealityIrY()));
			row.createCell((short) 6).setCellValue(loan.getContStartDate());
			row.createCell((short) 7).setCellValue(loan.getContEndDate());
			row.createCell((short) 8).setCellValue(df1.format(loan.getContAmt()));
			row.createCell((short) 9).setCellValue(df1.format(loan.getLoanAmt()));
			row.createCell((short) 10).setCellValue(df1.format(loan.getIntAccum()));
			row.createCell((short) 11).setCellValue(loan.getQixiDate());
			if("0".equals(loan.getAccStatus())){
				row.createCell((short) 12).setCellValue("出帐未确认");
			}else if("1".equals(loan.getAccStatus())){
				row.createCell((short) 12).setCellValue("正常");
			}else if("2".equals(loan.getAccStatus())){
				row.createCell((short) 12).setCellValue("正回购卖出");
			}else if("3".equals(loan.getAccStatus())){
				row.createCell((short) 12).setCellValue("逆回购买入");
			}else if("4".equals(loan.getAccStatus())){
				row.createCell((short) 12).setCellValue("逆回购到期");
			}else if("5".equals(loan.getAccStatus())){
				row.createCell((short) 12).setCellValue("正回购到期");
			}else if("6".equals(loan.getAccStatus())){
				row.createCell((short) 12).setCellValue("垫款");
			}else if("7".equals(loan.getAccStatus())){
				row.createCell((short) 12).setCellValue("已扣款");
			}else if("8".equals(loan.getAccStatus())){
				row.createCell((short) 12).setCellValue("退回未用");
			}
			else if("9".equals(loan.getAccStatus())){
				row.createCell((short) 12).setCellValue("结清/核销");	
			}else if("10".equals(loan.getAccStatus())){
				row.createCell((short) 12).setCellValue("闭卷");	
			}
			else if("11".equals(loan.getAccStatus())){
				row.createCell((short) 12).setCellValue("撤销");
			}
			row.createCell((short) 13).setCellValue(loan.getOverdueMoney());
			row.createCell((short) 14).setCellValue(loan.getOverdue());
		}
		String fileName = "客户逾期清单";
		try{
			response.setHeader("Content-Disposition", "attachment;fileName="+new String(fileName.getBytes("gbk"),"iso8859-1")+".xls");
			response.setHeader("Connection", "close");
			response.setHeader("Content-Type", "application/vnd.ms-excel");
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
