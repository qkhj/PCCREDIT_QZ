package com.cardpay.pccredit.report.dao;

import java.util.List;








import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.report.filter.AccLoanCollectFilter;
import com.cardpay.pccredit.report.filter.OClpmAccLoanFilter;
import com.cardpay.pccredit.report.model.AccLoanCollectInfo;
import com.cardpay.pccredit.report.model.AccLoanInfo;
import com.cardpay.pccredit.report.model.AccLoanOverdueInfo;
import com.wicresoft.util.annotation.Mapper;

/**
 * @author chinh
 *
 * 2015-8-1上午17:04:54
 */
@Mapper
public interface AfterAccLoanDao {
	/**
	 * 贷款借据清单不分页
	 */
	public List<AccLoanInfo> getAfterAccLoanAll(OClpmAccLoanFilter filter);
	/**
     * 贷款借据清单
     * @param filter
     * @return 
     */
	public List<AccLoanInfo> getAfterAccLoan(OClpmAccLoanFilter filter);
	
	public int getAfterAccLoanCount(OClpmAccLoanFilter filter);
	/**
     * 客户逾期清单
     * @param filter
     * @return
     */
	public List<AccLoanOverdueInfo> getLoanOverdue(OClpmAccLoanFilter filter);
	
	/**
     * 客户汇总清单
     * @param filter
     * @return
     */
	public List<AccLoanCollectInfo> getAccLoanCollect(AccLoanCollectFilter filter);
	
	/**
	 * 根据客户查询台账信息
	 * @param customerId
	 * @return
	 */
	public List<AccLoanInfo> getAfterAccLoanByCustomerId(@Param("customerId")String customerId);
}
