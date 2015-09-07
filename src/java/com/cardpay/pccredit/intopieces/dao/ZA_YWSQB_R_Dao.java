package com.cardpay.pccredit.intopieces.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.intopieces.filter.ZAFilter;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqb;
import com.cardpay.pccredit.intopieces.model.QzApplnZa;
import com.cardpay.pccredit.intopieces.model.QzApplnZaReturnMap;
import com.cardpay.pccredit.intopieces.model.QzAppln_Za_Ywsqb_R;
import com.wicresoft.util.annotation.Mapper;

@Mapper
public interface ZA_YWSQB_R_Dao {
	public QzAppln_Za_Ywsqb_R findZaById(@Param("id")String id);
	public QzAppln_Za_Ywsqb_R findByCustomerId(@Param("customerId")String customerId);
	public QzAppln_Za_Ywsqb_R findByAppId(@Param("applicationId")String applicationId);
	public List<QzAppln_Za_Ywsqb_R> findByZaId(@Param("zaId")String zaId);
	
}
