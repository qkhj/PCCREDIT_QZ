package com.cardpay.pccredit.QZBankInterface.service;

/**
 * 额度冻结
 */
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    public  CompositeData createEDRequest(HashMap map) {
        CompositeData cd = new CompositeData();
        
		String clientNo = (String)map.get("clientNo");
		String cardNo = (String)map.get("cardNo");
		String operateType = (String)map.get("operateType");
		
        //SYS_HEAD
        CompositeData syaHead_struct = new CompositeData();
        //在syaHead_struct中加SERVICECODE
        Field serviceCodeField = new Field(new FieldAttr(FieldType.FIELD_STRING, 11));
        serviceCodeField.setValue("02002000005");//
        syaHead_struct.addField("SERVICE_CODE", serviceCodeField);

        //在syaHead_struct中加SERVICESCENE
        Field serviceSceneField = new Field(new FieldAttr(FieldType.FIELD_STRING, 2));
        serviceSceneField.setValue("03"); //账户查询对应场景
        syaHead_struct.addField("SERVICE_SCENE", serviceSceneField);
        
        //在syaHead_struct中加TRAN_DATE
        Field tran_datefield = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
        tran_datefield.setValue("20161002"); //交易日期
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
        CLIENT_NO.setValue(clientNo);//todo:传入客户号
        body_struct.addField("CLIENT_NO", CLIENT_NO);
        
        //操作类型
        Field OPERATION_TYPE=new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        OPERATION_TYPE.setValue(operateType);//todo:操作类型
        body_struct.addField("OPERATION_TYPE", OPERATION_TYPE);
        
        //关联卡号
        Field LOAN_CARD_NO=new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
        LOAN_CARD_NO.setValue(cardNo);//todo:传入关联卡号
        body_struct.addField("LOAN_CARD_NO", LOAN_CARD_NO);

        //新增字段
        //合同号
        Field CONTRACT_NO=new Field(new FieldAttr(FieldType.FIELD_STRING, 60));
        CONTRACT_NO.setValue("");//个人资助
        body_struct.addField("CONTRACT_NO", CONTRACT_NO);
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
    public  Map parseCoreResponse(CompositeData cd) {
    	if(cd == null){
    		return null;
    	}
    	Map result = new HashMap();
        CompositeData sysHead = cd.getStruct("SYS_HEAD");
        
        //根据数组名称去获取数组
        Array array = sysHead.getArray("RET");
        
        String RET_MSG = "";//交易返回信息
        String RET_CODE = "";//交易返回码
        
        if(null != array && array.size() > 0){
            int m = array.size();
            CompositeData array_element = null;
            for (int i = 0; i < m; i++) {
                //数组中的元素也是CompositeData，这是固定的写法。根据游标就可以获取到数组中的所有元素
                array_element = array.getStruct(i);
                RET_MSG=array_element.getField("RET_MSG").strValue();
                RET_CODE=array_element.getField("RET_CODE").strValue();
            }
        }
        result.put("RET_MSG", RET_MSG);
        result.put("RET_CODE", RET_CODE);
        return result;
    }
}
