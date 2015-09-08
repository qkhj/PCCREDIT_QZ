package com.cardpay.pccredit.QZBankInterface.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.QZBankInterface.client.Client;
import com.cardpay.pccredit.QZBankInterface.dao.ECIFDao;
import com.cardpay.pccredit.QZBankInterface.filter.EcifFilter;
import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.QZBankInterface.model.ECIF;
import com.cardpay.pccredit.QZBankInterface.web.IESBForECIFForm;
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
	private UpdateIESBFForECIF updateiesbForECIF;
	
	@Autowired
	private ECIFDao ecifDao;
	
	@Autowired
	private IESBForECIFQuery ecifForECIFQuery;
	
	@Autowired
	private Client client;
	
	@Autowired
	private CustomerInforService customerInforservice;
	
	public static final Logger logger = Logger.getLogger(ECIFService.class);
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
    		ecif.setCustomerId(info.getId());//设置关联basic_customer_information
    		
			//将客户证件号码对应的客户号存入数据库中
			ecif.setCreatedTime(new Date());
//            ecif.setClientNo(clientNo);
            String id = IDGenerator.generateID();
            ecif.setId(id);
            commonDao.insertObject(ecif);
//		}
	}
	
	/**
	 * 更新数据
	 * @param customerinfo
	 * @return
	 */
	public boolean updateCustomerInfor(Circle circle,ECIF ecif) {
		//if(ecif.getClientNo() == null || ecif.getClientNo().equals("")){
			//组包
			CompositeData req = iesbForECIF.createEcifRequest(ecif);
			//发送
			CompositeData resp = client.sendMess(req);
			//解析，存db
			String clientNo = iesbForECIF.parseEcifResponse(resp);
			
			logger.info("clientNo=" + clientNo);
			if(ecif.getClientNo() == null || ecif.getClientNo().equals("")){
				if(clientNo != null && !clientNo.equals("")){
					ecif.setClientNo(clientNo);
					commonDao.updateObject(ecif);
	            
					circle.setClientNo(clientNo);
					circle.setaClientNo(clientNo);
					commonDao.updateObject(circle);
					return true;
				}
				else{
					return false;
				}
			}else{
		//	circle.setClientNo(ecif.getClientNo());
          //  circle.setaClientNo(ecif.getClientNo());
         //   commonDao.updateObject(circle);
				if(!(clientNo.equals(ecif.getClientNo()))){
					return false;
				}
			}
		return true;
	}
	
	/**
	 * 更新核心数据
	 * @param customerinfo
	 * @return
	 */
	public boolean updateBasicCustomerInfo(IESBForECIFForm iesbForECIFForm, String clientNo){
		//组包
		CompositeData req = updateiesbForECIF.createEcifRequestForUpdate(iesbForECIFForm, clientNo);
		//发送
		CompositeData resp = client.sendMess(req);
		//解析
		String retcode = updateiesbForECIF.parseEcifResponse(resp);
		
		logger.info("retcode=" + retcode);
		
		if("000000".equals(retcode)){
		
			return true;
		}
		
		return false;
	}
	
	/**
	 * 检测核心数据
	 * @param customerinfo
	 * @return
	 */
	public Map detectECIF(String cardId, String clientType, String certType){
		//组包
	    CompositeData req = ecifForECIFQuery.createEcifRequest(cardId, clientType, certType);
	    //发送
	    CompositeData resp = client.sendMess(req);
	    //解析
	    Map ECIFResp = ecifForECIFQuery.parseEcifResponse(resp);
	    
	    logger.info("RET_MSG=" + ECIFResp.get("RET_MSG") + "RET_CODE=" + ECIFResp.get("RET_CODE") 
        		+ "CLIENT_NAME=" + ECIFResp.get("CLIENT_NAME") + "GLOBAL_TYPE=" + ECIFResp.get("GLOBAL_TYPE")
        		+ "GLOBAL_ID=" + ECIFResp.get("GLOBAL_ID") + "CLIENT_NO=" + ECIFResp.get("CLIENT_NO"));
	    return ECIFResp;
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
	public QueryResult<IntoPieces> findCustomerInforWithEcifByFilter(CustomerInforFilter filter) {
		List<IntoPieces> ls = ecifDao.findCustomerInforWithEcifByFilter(filter);
		int size = ecifDao.findCustomerInforWithEcifCountByFilter(filter);
		QueryResult<IntoPieces> qs = new QueryResult<IntoPieces>(size, ls);
		return qs;
	}
	/**
	 * 过滤查询  关联ecif开户信息
	 * @param filter
	 * @return
	 */
	public QueryResult<CustomerInfor> findCustomerInfoWithEcifByFilter(CustomerInforFilter filter) {
		List<CustomerInfor> ls = ecifDao.findCustomerInfoWithEcifByFilter(filter);
		int size = ecifDao.findCustomerInforWithEcifCountByFilter(filter);
		QueryResult<CustomerInfor> qs = new QueryResult<CustomerInfor>(size, ls);
		return qs;
	}
	
	/**
	 * 过滤查询，查询有做过贷款业务的客户(个人住房按揭贷款客户对应产品编号为100028)
	 * @param filter
	 * @return
	 */
	public QueryResult<CustomerInfor> findCustomerInfoWithLoanByFilter(CustomerInforFilter filter){
		List<CustomerInfor> ls = ecifDao.findCustomerInfoWithLoanByFilter(filter);
		int size = ecifDao.findCustomerInfoWithLoanCountByFilter(filter);
		QueryResult<CustomerInfor> qs = new QueryResult<CustomerInfor>(size, ls);
		return qs;
	}
	
	/**
	 * 过滤查询，查询未做过贷款的客户（不包括个人住房贷款客户）
	 * @param filter
	 * @return
	 */
	public QueryResult<CustomerInfor> findCustomerInfoWithNotByFilter(CustomerInforFilter filter){
		List<CustomerInfor> ls = ecifDao.findCustomerInfoWithNotByFilter(filter);
		int size = ecifDao.findCustomerInfoWithNotCountByFilter(filter);
		QueryResult<CustomerInfor> qs = new QueryResult<CustomerInfor>(size, ls);
		return qs;
	}

	/**
	 * 按customerId查找ecif
	 */
	public IESBForECIFReturnMap findEcifMapByCustomerId(String customerId) {
		return ecifDao.findEcifMapByCustomerId(customerId);
	}
	
	/**
	 * 按customerId查找ecif
	 */
	public ECIF findEcifByCustomerId(String customerId) {
		return ecifDao.findEcifByCustomerId(customerId);
	}
	/**
	 * 更新数据
	 * @param customerinfo
	 * @return
	 */
	public void updateCustomerInfor(ECIF ecif,CustomerInfor info) {
		commonDao.updateObject(ecif);
	            
		commonDao.updateObject(info);
		
	}
}
