package com.cardpay.pccredit.intopieces.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.intopieces.model.QzApplnYwsqb;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbJkjl;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbZygys;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbZykh;
import com.wicresoft.util.annotation.Mapper;

@Mapper
public interface YwsqbDao {
	public QzApplnYwsqb findYwsqb(@Param("customerId")String customerId,@Param("applicationId")String applicationId);
	public QzApplnYwsqb findYwsqbByAppId(@Param("applicationId")String applicationId);
	public List<QzApplnYwsqbZygys> findYwsqbZygys(@Param("ywsqbId")String ywsqbId);
	public List<QzApplnYwsqbZykh> findYwsqbZykh(@Param("ywsqbId")String ywsqbId);
	public List<QzApplnYwsqbJkjl> findYwsqbJkjl(@Param("ywsqbId")String ywsqbId);
	public void deleteYwsqbZygys(@Param("ywsqbId")String ywsqbId);
	public void deleteYwsqbZykh(@Param("ywsqbId")String ywsqbId);
	public void deleteYwsqbJkjl(@Param("ywsqbId")String ywsqbId);
	public List<QzApplnYwsqb> findYwsqbforCustomerId(@Param("customerId")String customerId);
}
