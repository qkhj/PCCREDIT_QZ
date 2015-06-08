package com.cardpay.pccredit.QZBankInterface.service;

/**
 * 额度冻结
 */
import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
@Service
public class IESBForED {
    /**
     * 组装CompositeData报文
     * @return
     */
    public static CompositeData createEDRequest() {
        CompositeData cd = new CompositeData();

        //SYS_HEAD
        CompositeData syaHead_struct = new CompositeData();
        //在syaHead_struct中加SERVICECODE
        Field serviceCodeField = new Field(new FieldAttr(FieldType.FIELD_STRING, 11));
        serviceCodeField.setValue("02005000001");//
        syaHead_struct.addField("SERVICE_CODE", serviceCodeField);

        //在syaHead_struct中加SERVICESCENE
        Field serviceSceneField = new Field(new FieldAttr(FieldType.FIELD_STRING, 2));
        serviceSceneField.setValue("03"); //账户查询对应场景
        syaHead_struct.addField("SERVICE_SCENE", serviceSceneField);
        
        //在syaHead_struct中加TRAN_DATE
        Field tran_datefield = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
        tran_datefield.setValue("20150416"); //交易日期
        syaHead_struct.addField("TRAN_DATE", tran_datefield);
        
        //在syaHead_struct中加CONSUMER_ID
        Field consumer_idField = new Field(new FieldAttr(FieldType.FIELD_STRING, 6));
        consumer_idField.setValue("300025"); //消费系统编号
        syaHead_struct.addField("CONSUMER_ID", consumer_idField);
        
        cd.addStruct("SYS_HEAD", syaHead_struct);

        //BODY
        CompositeData body_struct = new CompositeData();

        //客户号
        Field CLIENT_NO=new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
        CLIENT_NO.setValue("0000000761110009");//todo:传入客户号
        body_struct.addField("CLIENT_NO", CLIENT_NO);
        
        //操作类型
        Field OPERATION_TYPE=new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        OPERATION_TYPE.setValue("10");//todo:操作类型
        body_struct.addField("OPERATION_TYPE", OPERATION_TYPE);
        
        //关联卡号
        Field LOAN_CARD_NO=new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
        LOAN_CARD_NO.setValue("6214373100000000511");//todo:传入关联卡号
        body_struct.addField("LOAN_CARD_NO", LOAN_CARD_NO);

        //新增字段
        //业务类型
        Field APPLY_TYPE=new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
        APPLY_TYPE.setValue("001");//个人资助
        body_struct.addField("APPLY_TYPE", APPLY_TYPE);
        
        cd.addStruct("BODY",body_struct);
        return cd;
    }

    /**
     * 解析CompositeData报文
     * @param cd
     */
    public static void parseCoreResponse(CompositeData cd) {

    }
}
