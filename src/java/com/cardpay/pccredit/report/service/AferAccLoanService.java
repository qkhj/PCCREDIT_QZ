package com.cardpay.pccredit.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.afterloan.model.AfterLoaninfo;
import com.cardpay.pccredit.report.dao.AfterAccLoanDao;
import com.cardpay.pccredit.report.filter.AccLoanCollectFilter;
import com.cardpay.pccredit.report.filter.OClpmAccLoanFilter;
import com.cardpay.pccredit.report.model.AccLoanCollectInfo;
import com.cardpay.pccredit.report.model.AccLoanInfo;
import com.cardpay.pccredit.report.model.AccLoanOverdueInfo;
import com.wicresoft.jrad.base.database.model.QueryResult;

/**
 * 贷款借据清单
 * @author chinh
 *
 * 2015-8-1上午17:03:57
 */
@Service
public class AferAccLoanService {
	@Autowired
	private AfterAccLoanDao afterAccLoanDao;
	/**
     * 贷款借据清单
     * @param filter
     * @return
     */
	public QueryResult<AccLoanInfo> getAfterAccLoan(OClpmAccLoanFilter filter){
		List<AccLoanInfo> pList = afterAccLoanDao.getAfterAccLoan(filter);
		int size = afterAccLoanDao.getAfterAccLoanCount(filter);
		QueryResult<AccLoanInfo> qs = new QueryResult<AccLoanInfo>(size, pList);
		return qs;
	}
	
	public List<AccLoanInfo> getAfterAccLoanList(OClpmAccLoanFilter filter){
		return afterAccLoanDao.getAfterAccLoanAll(filter);
	}
	
	/**
     * 客户逾期清单
     * @param filter
     * @return
     */
	public List<AccLoanOverdueInfo> getLoanOverdue(OClpmAccLoanFilter filter){
		return afterAccLoanDao.getLoanOverdue(filter);
	}
	
	/**
     * 汇总查询
     * @param filter
     * @return
     */
	public List<AccLoanCollectInfo> getAccLoanCollect(AccLoanCollectFilter filter){
		return afterAccLoanDao.getAccLoanCollect(filter);
	}
	
	/**
	 * 根据客户Id查询台账信息
	 * @param customerId
	 * @return
	 */
	public List<AccLoanInfo> getAfterAccLoanByCustomerId(String customerId){
		return afterAccLoanDao.getAfterAccLoanByCustomerId(customerId);
	}
	
}
