package com.cardpay.pccredit.intopieces.dao;

import java.util.List;

import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.customer.filter.CustomerInforFilter;
import com.cardpay.pccredit.intopieces.filter.QuotaFreezeOrThawFilter;
import com.cardpay.pccredit.intopieces.model.QuotaFreezeInfo;
import com.wicresoft.util.annotation.Mapper;

@Mapper
public interface QuotaFreezeOrThawDao {
	public List<QuotaFreezeInfo> getQzIesbForCircleByFilter(QuotaFreezeOrThawFilter filter);
	
	public int getQzIesbForCircleCountByFilter(QuotaFreezeOrThawFilter filter);
}
