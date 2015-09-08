package com.cardpay.pccredit.QZBankInterface.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.QZBankInterface.filter.EcifFilter;
import com.cardpay.pccredit.QZBankInterface.model.ECIF;
import com.cardpay.pccredit.QZBankInterface.web.IESBForECIFReturnMap;
import com.cardpay.pccredit.customer.filter.AmountAdjustmentFilter;
import com.cardpay.pccredit.customer.filter.CustomerInforFilter;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.intopieces.model.IntoPieces;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.util.annotation.Mapper;

@Mapper
public interface ECIFDao {

	public List<IESBForECIFReturnMap> findAllECIFByStatus(@Param("status") String status);
	
	public List<ECIF> findEcifByFilter(EcifFilter filter);
	
	public int findEcifCountByFilter(EcifFilter filter);

	public ECIF findEcifByClientNo(@Param("clientNo") String clientNo);

	public List<IntoPieces> findCustomerInforWithEcifByFilter(CustomerInforFilter filter);
	
	public List<CustomerInfor> findCustomerInfoWithEcifByFilter(CustomerInforFilter filter);

	public int findCustomerInforWithEcifCountByFilter(CustomerInforFilter filter);

	public IESBForECIFReturnMap findEcifMapByCustomerId(@Param("customerId") String customerId);
	
	public ECIF findEcifByCustomerId(@Param("customerId") String customerId);
	
	public List<CustomerInfor> findCustomerInfoWithLoanByFilter(CustomerInforFilter filter);
	
	public int findCustomerInfoWithLoanCountByFilter(CustomerInforFilter filter);
	
	public List<CustomerInfor> findCustomerInfoWithNotByFilter(CustomerInforFilter filter);
	
	public int findCustomerInfoWithNotCountByFilter(CustomerInforFilter filter);
}
