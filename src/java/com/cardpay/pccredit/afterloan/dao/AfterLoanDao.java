package com.cardpay.pccredit.afterloan.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.afterloan.filter.AfterLoanCheckFilter;
import com.wicresoft.util.annotation.Mapper;
import com.cardpay.pccredit.afterloan.model.AfterLoaninfo;
import com.cardpay.pccredit.afterloan.model.PspCheckTask;
import com.cardpay.pccredit.manager.web.AccountManagerParameterForm;

@Mapper
public interface AfterLoanDao {
	
	public List<AfterLoaninfo> findAfterLoanCheckTaskByFilter(AfterLoanCheckFilter filter);
	
	public int findAfterLoanCheckTaskCountByFilter(AfterLoanCheckFilter filter); 
	
	public List<AfterLoaninfo> findAfterLoanCheckTaskToByFilter(AfterLoanCheckFilter filter);
	
	public int findAfterLoanCheckTaskCountToByFilter(AfterLoanCheckFilter filter);
	
	public List<AfterLoaninfo> findAfterLoanCheckTaskRemindByFilter(AfterLoanCheckFilter filter);
	
	public int findAfterLoanCheckTaskCountRemindByFilter(AfterLoanCheckFilter filter);
	
	//根据客户经理查询贷后检查数
	public int findAferLoanCheckCountByUserId(@Param("userId") String userId);
	
    //查询超过十天未做检查的任务
	public int findAferLoanCheckRemindCount();
}
