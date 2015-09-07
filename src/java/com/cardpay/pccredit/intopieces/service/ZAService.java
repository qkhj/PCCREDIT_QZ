package com.cardpay.pccredit.intopieces.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.intopieces.dao.ZADao;
import com.cardpay.pccredit.intopieces.filter.ZAFilter;
import com.cardpay.pccredit.intopieces.model.QzApplnZa;
import com.cardpay.pccredit.intopieces.model.QzApplnZaReturnMap;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.model.QueryResult;

@Service
public class ZAService {

	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private ZADao zaDao;
	
	public void insert(QzApplnZa qzApplnZa){
		commonDao.insertObject(qzApplnZa);
	}
	
	public void update(QzApplnZa qzApplnZa){
		commonDao.updateObject(qzApplnZa);
	}
	
	public QzApplnZa findZaById(String id){
		return zaDao.findZaById(id);
	}
	
	public QueryResult<QzApplnZa> findAllZa(ZAFilter filter){
		List<QzApplnZa> ls = zaDao.findAllZaByFilter(filter);
		int size = zaDao.findAllZaCountByFilter(filter);
		QueryResult<QzApplnZa> qs = new QueryResult<QzApplnZa>(size, ls);
		return qs;
	}
	
	public List<QzApplnZaReturnMap> findZas(){
		List<QzApplnZaReturnMap> ls = zaDao.findZas();
		return ls;
	}
	
	public void deleteZAById(String id){
		zaDao.deleteZAById(id);
	}
}
