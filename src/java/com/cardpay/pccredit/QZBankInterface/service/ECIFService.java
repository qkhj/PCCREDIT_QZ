package com.cardpay.pccredit.QZBankInterface.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.QZBankInterface.client.Client;
import com.cardpay.pccredit.QZBankInterface.dao.ECIFDao;
import com.cardpay.pccredit.QZBankInterface.filter.EcifFilter;
import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.QZBankInterface.model.ECIF;
import com.cardpay.pccredit.QZBankInterface.web.IESBForECIFReturnMap;
import com.cardpay.pccredit.customer.filter.AmountAdjustmentFilter;
import com.cardpay.pccredit.customer.filter.CustomerInforFilter;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.customer.service.CustomerInforService;
import com.cardpay.pccredit.customer.web.AmountAdjustmentForm;
import com.cardpay.pccredit.intopieces.model.IntoPieces;
import com.cardpay.pccredit.product.model.ProductAttribute;
import com.dc.eai.data.CompositeData;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.jrad.base.database.model.QueryResult;

/** 
 * @author 贺珈 
 * @version 创建时间：2015年4月22日 下午2:18:39 
 * 程序的简单说明 
 */
@Service
public class ECIFService {
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private IESBForECIF iesbForECIF;
	
	@Autowired
	private ECIFDao ecifDao;
	
	@Autowired
	private Client client;
	
	@Autowired
	private CustomerInforService customerInforservice;
	
	/**
	 * 插入数据
	 * @param customerinfo
	 * @return
	 */
	public void insertCustomerInfor(ECIF ecif,CustomerInfor info) {
		
		//组包
//		CompositeData req = iesbForECIF.createEcifRequest(ecif);
		//发送
//		CompositeData resp = client.sendMess(req);
		//解析，存db
//		String clientNo = iesbForECIF.parseEcifResponse(resp);
//		if(clientNo != null && !clientNo.equals("")){
			if(info.getId() == null || info.getId().equals("")){
    			customerInforservice.insertCustomerInfor(info);
    		}
    		else{
    			customerInforservice.updateCustomerInfor(info);
    		}
    		ecif.setCustomerId(info.getId());;//设置关联basic_customer_information
    		
			//将客户证件号码对应的客户号存入数据库中
			ecif.setCreatedTime(new Date());
//            ecif.setClientNo(clientNo);
            String id = IDGenerator.generateID();
            ecif.setId(id);
            commonDao.insertObject(ecif);
//		}
	}
	
	/**
	 * 按clientNo查找ecif
	 */
	public ECIF findEcifByClientNo(String clientNo){
		return ecifDao.findEcifByClientNo(clientNo);
	}
	
	/**
	 * 查询所有未办理贷款的开户
	 * @return
	 */
	public List<IESBForECIFReturnMap> findAllECIFByStatus(String status){
		return ecifDao.findAllECIFByStatus(status);
	}
	
	/**
	 * 查询新开户
	 * @param filter
	 * @return
	 */
	public QueryResult<ECIF> findEcifByFilter(EcifFilter filter) {
		List<ECIF> ls = ecifDao.findEcifByFilter(filter);
		int size = ecifDao.findEcifCountByFilter(filter);
		QueryResult<ECIF> qs = new QueryResult<ECIF>(size, ls);
		return qs;
	}

	/**
	 * 过滤查询  关联ecif开户信息
	 * @param filter
	 * @return
	 */
	public QueryResult<CustomerInfor> findCustomerInforWithEcifByFilter(CustomerInforFilter filter) {
		List<CustomerInfor> ls = ecifDao.findCustomerInforWithEcifByFilter(filter);
		int size = ecifDao.findCustomerInforWithEcifCountByFilter(filter);
		QueryResult<CustomerInfor> qs = new QueryResult<CustomerInfor>(size, ls);
		return qs;
	}

	/**
	 * 按customerId查找ecif
	 */
	public IESBForECIFReturnMap findEcifByCustomerId(String customerId) {
		return ecifDao.findEcifByCustomerId(customerId);
	}
}
