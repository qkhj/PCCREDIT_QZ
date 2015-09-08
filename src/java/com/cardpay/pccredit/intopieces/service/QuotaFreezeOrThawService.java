package com.cardpay.pccredit.intopieces.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.QZBankInterface.client.Client;
import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.customer.filter.CustomerInforFilter;
import com.cardpay.pccredit.intopieces.dao.QuotaFreezeOrThawDao;
import com.cardpay.pccredit.intopieces.filter.QuotaFreezeOrThawFilter;
import com.cardpay.pccredit.intopieces.model.QuotaFreezeInfo;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.cardpay.pccredit.QZBankInterface.service.IESBForED;
import com.dc.eai.data.CompositeData;

@Service
public class QuotaFreezeOrThawService{
	
	@Autowired
	public QuotaFreezeOrThawDao quotaFreezeOrThawDao;
	@Autowired
	public IESBForED IESBForED;
	
	@Autowired
	private Client client;
	
	private static final Logger logger = Logger.getLogger(QuotaFreezeOrThawService.class);
	/**
	 * 查询贷款成功的客户
	 * @author chinh
	 * @param filter
	 * @return QueryResult
	 */
	
	public QueryResult<QuotaFreezeInfo> getQzIesbForCircleByFilter(QuotaFreezeOrThawFilter filter){
		List<QuotaFreezeInfo> circle = quotaFreezeOrThawDao.getQzIesbForCircleByFilter(filter);
		int size = quotaFreezeOrThawDao.getQzIesbForCircleCountByFilter(filter);
		QueryResult<QuotaFreezeInfo> qz = new QueryResult<QuotaFreezeInfo>(size,circle);
		return qz;
	}
	
	/**
	 * 调用信贷额度维护接口做冻结/解冻、终止操作
	 * @author chinh
	 * @param HashMap
	 * @return retMsg
	 */
	public Map getIesbForED(HashMap requestMap){
		Map retCode = new HashMap();
		CompositeData request = IESBForED.createEDRequest(requestMap);
		//发送请求数据
		CompositeData resp = client.sendMess(request);
		
		retCode = IESBForED.parseCoreResponse(resp);
		return retCode;
	}
}
