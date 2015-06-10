package com.cardpay.pccredit.intopieces.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.intopieces.model.QzApplnDbrxx;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxxDkjl;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxxFc;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxxJdc;
import com.wicresoft.util.annotation.Mapper;

@Mapper
public interface DbrxxDao {
	public List<QzApplnDbrxx> findDbrxx(@Param("customerId")String customerId,@Param("applicationId")String applicationId);
	public List<QzApplnDbrxx> findDbrxxByAppId(@Param("applicationId")String applicationId);
	public QzApplnDbrxx findDbrxxById(@Param("id")String id);
	public List<QzApplnDbrxxDkjl> findDbrxxDkjl(@Param("dbrxxId")String dbrxxId);
	public List<QzApplnDbrxxFc> findDbrxxFc(@Param("dbrxxId")String dbrxxId);
	public List<QzApplnDbrxxJdc> findDbrxxJdc(@Param("dbrxxId")String dbrxxId);
	public void deleteDbrxx(@Param("dbrxxId")String dbrxxId);
	public void deleteDbrxxDkjl(@Param("dbrxxId")String dbrxxId);
	public void deleteDbrxxFc(@Param("dbrxxId")String dbrxxId);
	public void deleteDbrxxJdc(@Param("dbrxxId")String dbrxxId);
}
