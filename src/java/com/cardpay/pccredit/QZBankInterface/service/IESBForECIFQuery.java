package com.cardpay.pccredit.QZBankInterface.service;

/**
 * ECIF接口，经ESB转发
 * Created by johhny on 15/4/11.
 */
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
@Service
public class IESBForECIFQuery {
	
	@Autowired
	CommonDao commonDao;
    /**
     * 组装CompositeData报文
     * 客户信息查询(11003000001)	个人客户信息查询(02)
     * @return
     */
	public static final Logger logger = Logger.getLogger(IESBForECIFQuery.class);
	
    public  CompositeData createEcifRequest(String certNo, String clientType, String certType) {
    	SimpleDateFormat formatter8 = new SimpleDateFormat("yyyyMMdd");
    	CompositeData cd = new CompositeData();

        //SYS_HEAD
        CompositeData syaHead_struct = new CompositeData();
        //在syaHead_struct中加SERVICECODE
        Field serviceCodeField = new Field(new FieldAttr(FieldType.FIELD_STRING, 11));
        serviceCodeField.setValue("11003000001");//核心账户查询
        syaHead_struct.addField("SERVICE_CODE", serviceCodeField);

        //在syaHead_struct中加SERVICESCENE
        Field serviceSceneField = new Field(new FieldAttr(FieldType.FIELD_STRING, 2));
        serviceSceneField.setValue("02"); //账户查询对应场景
        syaHead_struct.addField("SERVICE_SCENE", serviceSceneField);
        
        //在syaHead_struct中加TRAN_DATE
        Field tran_datefield = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
        tran_datefield.setValue(formatter8.format(new Date())); //交易日期
        syaHead_struct.addField("TRAN_DATE", tran_datefield);
        
        //在syaHead_struct中加CONSUMER_ID
        Field consumer_idField = new Field(new FieldAttr(FieldType.FIELD_STRING, 6));
        consumer_idField.setValue("300025"); //消费系统编号
        syaHead_struct.addField("CONSUMER_ID", consumer_idField);
        
        cd.addStruct("SYS_HEAD", syaHead_struct);

        //BODY
        CompositeData body_struct = new CompositeData();

        //客户号
        Field CLIENT_NO=new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        CLIENT_NO.setValue("");//todo:客户号
        body_struct.addField("CLIENT_NO", CLIENT_NO);
        
        //客户类型
        Field CLIENT_TYPE=new Field(new FieldAttr(FieldType.FIELD_STRING, 6));
        CLIENT_TYPE.setValue("");//todo:客户类型
        body_struct.addField("CLIENT_TYPE", CLIENT_TYPE);
        
        //证件号码
        Field GLOBAL_ID=new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
        GLOBAL_ID.setValue(certNo);//todo:证件号码
        body_struct.addField("GLOBAL_ID", GLOBAL_ID);
        
        //证件类型
        Field GLOBAL_TYPE=new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
        GLOBAL_TYPE.setValue(certType);//todo:证件类型
        body_struct.addField("GLOBAL_TYPE", GLOBAL_TYPE);
        
        //账号
        Field ACCT_NO=new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
        ACCT_NO.setValue("");//todo:传入账号 置空 暂时没用
        body_struct.addField("ACCT_NO", ACCT_NO);
        
        cd.addStruct("BODY",body_struct);
        return cd;
    }

    /**
     * 组装CompositeData报文
     * 客户信息查询(11003000001)	客户存款账号信息查询(05)
     * @return
     */
    public static CompositeData createEcifRequest2() {
    	CompositeData cd = new CompositeData();

        //SYS_HEAD
        CompositeData syaHead_struct = new CompositeData();
        //在syaHead_struct中加SERVICECODE
        Field serviceCodeField = new Field(new FieldAttr(FieldType.FIELD_STRING, 11));
        serviceCodeField.setValue("11003000001");//核心账户查询
        syaHead_struct.addField("SERVICE_CODE", serviceCodeField);

        //在syaHead_struct中加SERVICESCENE
        Field serviceSceneField = new Field(new FieldAttr(FieldType.FIELD_STRING, 2));
        serviceSceneField.setValue("05"); //账户查询对应场景
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
        Field CLIENT_NO=new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        CLIENT_NO.setValue("0010000151130009");//todo:客户号
        body_struct.addField("CLIENT_NO", CLIENT_NO);
        
        //账号
        Field ACCT_NO=new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
        ACCT_NO.setValue("");//todo:传入账号 为空  暂时没用
        body_struct.addField("ACCT_NO", ACCT_NO);
        
        cd.addStruct("BODY",body_struct);
        
        //APP_HEAD
        CompositeData APP_HEAD = new CompositeData();

        //本页记录总数
        Field TOTAL_NUM=new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        TOTAL_NUM.setValue("10");//todo:本页记录总数
        APP_HEAD.addField("TOTAL_NUM", TOTAL_NUM);
        
        //当前记录号
        Field CURRENT_NUM=new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        CURRENT_NUM.setValue("1");//todo:传入当前记录号
        APP_HEAD.addField("CURRENT_NUM", CURRENT_NUM);
        
        cd.addStruct("APP_HEAD",APP_HEAD);
        
        return cd;
    }
    
    /**
     * 解析CompositeData报文
     * @param cd
     */
    public Map parseEcifResponse(CompositeData cd){
    	
    	Map queryMap = new HashMap();
    	
    	CompositeData sysHead = cd.getStruct("SYS_HEAD");
    	
    	CompositeData body = cd.getStruct("BODY");
    	

        //根据数组名称去获取数组
        Array array = sysHead.getArray("RET");
        
        String RET_MSG = "";//交易返回信息
        String RET_CODE = "";//交易返回码
        String CLIENT_NAME = "";//客户名称
        String GLOBAL_TYPE = "";//客户证件类型
        String GLOBAL_ID = "";//客户证件号码
        String CLIENT_NO = "";
        
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
        //但报文头中交易返回码为"000000"表示交易成功，再解析body中的的数据
        if("000000".equals(RET_CODE)){
        	//根据数组名称去获取数组
            Array clientNameInfoArray = body.getArray("PERSON_CLIENT_NAME_INFO_ARRAY");
            if(null != clientNameInfoArray && clientNameInfoArray.size() > 0){
            	for(int i = 0; i < clientNameInfoArray.size(); i++){
            		CompositeData clientNameInfoArray_element = clientNameInfoArray.getStruct(i);
            		
            		CLIENT_NAME = clientNameInfoArray_element.getField("CLIENT_NAME").strValue();
            	}
            }
            
           //根据数组名称去获取数组
           Array clientGlobalInfoArray = body.getArray("CLIENT_GLOBAL_INFO_ARRAY");
           if(null != clientGlobalInfoArray && clientGlobalInfoArray.size() > 0){
        	   for(int i = 0; i < clientGlobalInfoArray.size(); i++){
        		   CompositeData clientGlobalInfoArray_element = clientGlobalInfoArray.getStruct(i);
           		
           		   GLOBAL_TYPE = clientGlobalInfoArray_element.getField("GLOBAL_TYPE").strValue();
           		   GLOBAL_ID = clientGlobalInfoArray_element.getField("GLOBAL_ID").strValue();
        	   }
           }
           
           ////根据struct名称去获取struct
           
           CompositeData PERSON_BASE_INFO_STRUCT = body.getStruct("PERSON_BASE_INFO_STRUCT");
           CLIENT_NO = PERSON_BASE_INFO_STRUCT.getField("CLIENT_NO").strValue();
        }
        queryMap.put("RET_MSG", RET_MSG);
        queryMap.put("RET_CODE", RET_CODE);
        queryMap.put("CLIENT_NAME", CLIENT_NAME);
        queryMap.put("GLOBAL_TYPE", GLOBAL_TYPE);
        queryMap.put("GLOBAL_ID", GLOBAL_ID);
        queryMap.put("CLIENT_NO", CLIENT_NO);
        
        logger.info("RET_MSG=" + queryMap.get("RET_MSG") + "RET_CODE=" + queryMap.get("RET_CODE") 
        		+ "CLIENT_NAME=" + queryMap.get("CLIENT_NAME") + "GLOBAL_TYPE=" + queryMap.get("GLOBAL_TYPE")
        		+ "GLOBAL_ID=" + queryMap.get("GLOBAL_ID") + "CLIENT_NO=" + queryMap.get("CLIENT_NO"));
        
        return queryMap;
    }
    
}
