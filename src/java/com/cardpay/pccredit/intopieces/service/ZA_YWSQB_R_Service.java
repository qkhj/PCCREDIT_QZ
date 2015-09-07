package com.cardpay.pccredit.intopieces.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.intopieces.dao.ZADao;
import com.cardpay.pccredit.intopieces.dao.ZA_YWSQB_R_Dao;
import com.cardpay.pccredit.intopieces.filter.ZAFilter;
import com.cardpay.pccredit.intopieces.model.QzApplnZa;
import com.cardpay.pccredit.intopieces.model.QzApplnZaReturnMap;
import com.cardpay.pccredit.intopieces.model.QzAppln_Za_Ywsqb_R;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.model.QueryResult;

@Service
public class ZA_YWSQB_R_Service {

	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private ZA_YWSQB_R_Dao za_ywsqb_r_dao;
	
	public void insert(QzAppln_Za_Ywsqb_R qzappln_za_ywsqb_r){
		commonDao.insertObject(qzappln_za_ywsqb_r);
	}
	
	public void update(QzAppln_Za_Ywsqb_R qzappln_za_ywsqb_r){
		commonDao.updateObject(qzappln_za_ywsqb_r);
	}
	
	public QzAppln_Za_Ywsqb_R findZaById(String id){
		return za_ywsqb_r_dao.findZaById(id);
	}
	
	public QzAppln_Za_Ywsqb_R findByCustomerId(String customerId){
		return za_ywsqb_r_dao.findByCustomerId(customerId);
	}
	
	public QzAppln_Za_Ywsqb_R findByAppId(String applicationId){
		return za_ywsqb_r_dao.findByAppId(applicationId);
	}
	
	public List<QzAppln_Za_Ywsqb_R> findByZaId(String id) {
		return za_ywsqb_r_dao.findByZaId(id);
	}
}
