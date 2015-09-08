package com.cardpay.pccredit.QZBankInterface.service;

/**
 * 核心系统账户查询接口，经ESB转发
 * Created by johhny on 15/4/13.
 */
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.QZBankInterface.model.Circle_ACCT_INFO;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.id.IDGenerator;
@Service
public class IESBForCore {
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private IESBForCircleCredit iesbForCircleCredit;
	
	@Autowired
	private IESBForCredit IESBForCredit;
    /**
     * 组装CompositeData报文
     * @return
     */
    public CompositeData createCoreRequest(String acctNo) {
    	SimpleDateFormat formatter8 = new SimpleDateFormat("yyyyMMdd");
    	
        CompositeData cd = new CompositeData();

        //SYS_HEAD
        CompositeData syaHead_struct = new CompositeData();
        //在syaHead_struct中加SERVICECODE
        Field serviceCodeField = new Field(new FieldAttr(FieldType.FIELD_STRING, 11));
        serviceCodeField.setValue("11003000007");//核心账户查询
        syaHead_struct.addField("SERVICE_CODE", serviceCodeField);

        //在syaHead_struct中加SERVICESCENE
        Field serviceSceneField = new Field(new FieldAttr(FieldType.FIELD_STRING, 2));
        serviceSceneField.setValue("17"); //账户查询对应场景
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

        //帐号
        Field ACCT_NO=new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
        //ACCT_NO.setValue("0000008390198012");//todo:传入账号
        //ACCT_NO.setValue(circle.getAcctNo());//todo:传入账号???
        ACCT_NO.setValue(acctNo);//todo:传入账号???
        body_struct.addField("ACCT_NO", ACCT_NO);

        cd.addStruct("BODY",body_struct);
        return cd;
    }

    /**
     * 解析CompositeData报文
     * @param cd
     */
    public Circle_ACCT_INFO parseCoreResponse(CompositeData cd,String type) {
    	
    	boolean res = IESBForCredit.parseEcifResponse(cd);
    	if(res){
    		Circle_ACCT_INFO circle_ACCT_INFO = new Circle_ACCT_INFO();
        	
            CompositeData body = cd.getStruct("BODY");

            String ACCT_NO = body.getField("ACCT_NO").strValue();//账号
            String ACCT_NAME = body.getField("ACCT_NAME").strValue();//户名
            String CCY = body.getField("CCY").strValue();//币种
            String OPEN_ACCT_BRANCH_ID = body.getField("OPEN_ACCT_BRANCH_ID").strValue();//开户机构
            String OPEN_ACCT_BRANCH_NAME = body.getField("OPEN_ACCT_BRANCH_NAME").strValue();//开户行名
            String C_INTERBANK_ID = body.getField("C_INTERBANK_ID").strValue();//全国联行号
            String GL_CODE = body.getField("GL_CODE").strValue();//科目号
            String OPEN_ACCT_DATE = body.getField("OPEN_ACCT_DATE").strValue();//开户日期
            
            //更新circle
            circle_ACCT_INFO.setAcctNo(ACCT_NO);
            circle_ACCT_INFO.setAcctName(ACCT_NAME);
            circle_ACCT_INFO.setCcy(CCY);
            circle_ACCT_INFO.setAcctChrt(type);
            circle_ACCT_INFO.setOpenAcctBranchId(OPEN_ACCT_BRANCH_ID);
            circle_ACCT_INFO.setOpenAcctBranchName(OPEN_ACCT_BRANCH_NAME);
            circle_ACCT_INFO.setPayAmt("0.0");
            circle_ACCT_INFO.setGlCode(GL_CODE);
            circle_ACCT_INFO.setcInterbankId(C_INTERBANK_ID);
            circle_ACCT_INFO.setOwnBranchFlag("1");
            circle_ACCT_INFO.setOpenAcctDate(OPEN_ACCT_DATE);
            
            String id = IDGenerator.generateID();
            circle_ACCT_INFO.setId(id);
           
            return circle_ACCT_INFO;
    	}else{
    		return null;
    	}
    	
    }
}
