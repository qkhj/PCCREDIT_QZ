package com.cardpay.pccredit.intopieces.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.intopieces.filter.ZAFilter;
import com.cardpay.pccredit.intopieces.model.QzApplnZa;
import com.cardpay.pccredit.intopieces.model.QzApplnZaReturnMap;
import com.wicresoft.util.annotation.Mapper;

@Mapper
public interface ZADao {
	public QzApplnZa findZaById(@Param("id")String id);
	public List<QzApplnZa> findAllZaByFilter(ZAFilter filter);
	public int findAllZaCountByFilter(ZAFilter filter);
	public void deleteZAById(@Param("id")String id);
	public List<QzApplnZaReturnMap> findZas();
}
