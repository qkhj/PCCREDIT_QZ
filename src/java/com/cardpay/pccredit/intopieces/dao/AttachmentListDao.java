package com.cardpay.pccredit.intopieces.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentList;
import com.wicresoft.util.annotation.Mapper;

@Mapper
public interface AttachmentListDao {
	public QzApplnAttachmentList findAttachmentList(@Param("customerId")String customerId,@Param("applicationId")String applicationId);
	public QzApplnAttachmentList findAttachmentListByAppId(@Param("applicationId")String applicationId);
}
