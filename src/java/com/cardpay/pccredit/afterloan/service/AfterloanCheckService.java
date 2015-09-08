package com.cardpay.pccredit.afterloan.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.afterloan.dao.AfterLoanDao;
import com.cardpay.pccredit.afterloan.filter.AfterLoanCheckFilter;
import com.cardpay.pccredit.afterloan.model.AfterLoaninfo;
import com.cardpay.pccredit.afterloan.model.O_CLPM_ACC_LOAN;
import com.cardpay.pccredit.afterloan.model.PspCheckTask;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.model.QueryResult;

/**
 * 贷后检查服务类
 * @author chinh
 *
 */
@Service
public class AfterloanCheckService {
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private AfterLoanDao afterLoanDao;
	
	private static final Logger logger = Logger.getLogger(AfterloanCheckService.class);
	
	/**
	 * 查询贷后检查任务列表
	 * @param filter
	 * @return
	 */
	public QueryResult<AfterLoaninfo> findAfterLoanCheckTaskByFilter(AfterLoanCheckFilter filter){
		List<AfterLoaninfo> pList = afterLoanDao.findAfterLoanCheckTaskByFilter(filter);
		int size = afterLoanDao.findAfterLoanCheckTaskCountByFilter(filter);
		QueryResult<AfterLoaninfo> qs = new QueryResult<AfterLoaninfo>(size, pList);
		return qs;
		
	}
	
	/**
	 * 查询贷后检查任务列表(审核)
	 * @param filter
	 * @return findAfterLoanCheckTaskRemindByFilter
	 */
	public QueryResult<AfterLoaninfo> findAfterLoanCheckTaskToByFilter(AfterLoanCheckFilter filter){
		List<AfterLoaninfo> pList = afterLoanDao.findAfterLoanCheckTaskToByFilter(filter);
		int size = afterLoanDao.findAfterLoanCheckTaskCountToByFilter(filter);
		QueryResult<AfterLoaninfo> qs = new QueryResult<AfterLoaninfo>(size, pList);
		return qs;
		
	}
	
	/**
	 * 查询贷后检查任务列表(提醒)
	 * @param filter
	 * @return 
	 */
	public QueryResult<AfterLoaninfo> findAfterLoanCheckTaskRemindByFilter(AfterLoanCheckFilter filter){
		List<AfterLoaninfo> pList = afterLoanDao.findAfterLoanCheckTaskRemindByFilter(filter);
		int size = afterLoanDao.findAfterLoanCheckTaskCountRemindByFilter(filter);
		QueryResult<AfterLoaninfo> qs = new QueryResult<AfterLoaninfo>(size, pList);
		return qs;
		
	}
	/**
	 * 根据任务id查询贷后检查任务
	 * @param filter
	 * @return
	 */
	public List<PspCheckTask> findPspCheckTaskByTaskId(String taskId){
		String sql ="select * from psp_check_task task where task.task_id='"+taskId+"'";
		List<PspCheckTask> qz= commonDao.queryBySql(PspCheckTask.class, sql, null);
		if(qz.size()>0){
			return qz;
		}
			return null;
	}
	
	public void update_page0(PspCheckTask pspCheckTask){
		Connection conn = null;
		Statement sta = null;
		try{
		String sql = "update psp_check_task SET agreed_person='"+pspCheckTask.getAgreedPerson()+"'"
				+ ", check_addr='"+pspCheckTask.getCheckAddr()+"',  check_time='"+pspCheckTask.getCheckTime()+"'"
				+ ",remarks='"+pspCheckTask.getRemarks()+"', industry_outlook='"+pspCheckTask.getIndustryOutlook()+"', repayment='"+pspCheckTask.getRepayment()+"'"
				+ ", reciprocal_type='"+pspCheckTask.getReciprocalType()+"', contact_information='"+pspCheckTask.getContactInformation()+"',repayment_other='"+pspCheckTask.getRepaymentOther()+"'"
				+ "where task_id='"+pspCheckTask.getTaskId()+"'";
		conn = commonDao.getSqlSession().getConnection();
		sta = conn.createStatement();
		boolean flag = sta.execute(sql);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				sta.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void update_page1(PspCheckTask pspCheckTask){
		Connection conn = null;
		Statement sta = null;
		try{
		String sql = "update psp_check_task SET  approve_status='"+pspCheckTask.getApproveStatus()+"'"
				+ "where task_id='"+pspCheckTask.getTaskId()+"'";
		conn = commonDao.getSqlSession().getConnection();
		sta = conn.createStatement();
		boolean flag = sta.execute(sql);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				sta.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 根据客户编号查询台账表
	 * 过滤产品(经营类和消费类) 
	 * @param filter
	 * @return
	 */
	public List<O_CLPM_ACC_LOAN> findAccLoanByclientNo(String clientNo){
		//TODO 产品类型
		String sql ="select loan.* from o_clpm_acc_loan loan where loan.cus_id='"+clientNo+"' order by loan.distr_date desc";
		List<O_CLPM_ACC_LOAN> qz= commonDao.queryBySql(O_CLPM_ACC_LOAN.class, sql, null);
		if(qz.size()>0){
			return qz;
		}
			return null;
	}
	
	/**
	 * 根据客户经理id查询贷后检查任务数
	 * @param userId
	 * @return
	 */
	public int findAferLoanCheckCountByUserId(String userId){
		return afterLoanDao.findAferLoanCheckCountByUserId(userId);
	}
	
	/**
	 * 查询超过十天的检查
	 * @param userId
	 * @return
	 */
	public int findAferLoanCheckRemindCount(){
		return afterLoanDao.findAferLoanCheckRemindCount();
	}
}
