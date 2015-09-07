package com.cardpay.pccredit.QZBankInterface.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.cardpay.pccredit.QZBankInterface.web.IESBForECIFForm;
import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;

/**
 * 修改客户信息
 * ECIF接口，经ESB转发
 * Created by chinh on 15/4/11.
 */
@Service
public class UpdateIESBFForECIF {
	 /**
     * 组装CompositeData报文
     * @return
     */
	public CompositeData createEcifRequestForUpdate(IESBForECIFForm iesbForECIFForm,String clientNo){
		SimpleDateFormat formatter8 = new SimpleDateFormat("yyyyMMdd");
        
        CompositeData cd = new CompositeData();
        //SYS_HEAD
        CompositeData syaHead_struct = new CompositeData();
        //在syaHead_struct中加SERVICECODE
        Field serviceCodeField = new Field(new FieldAttr(FieldType.FIELD_STRING, 11));
        serviceCodeField.setValue("11002000019");//ECIF服务服务码
        syaHead_struct.addField("SERVICE_CODE", serviceCodeField);

        //在syaHead_struct中加SERVICESCENE
        Field serviceSceneField = new Field(new FieldAttr(FieldType.FIELD_STRING, 2));
        serviceSceneField.setValue("22"); //个人客户新增，对应服务场景02
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

//        //LOCAL_HEAD
//        CompositeData localHead_struct = new CompositeData();
//        
//        //在localHead_strust中加CLIENT_NO
//        Field clientNoField = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
//        clientNoField.setValue(clientNo);
//        localHead_struct.addField("CLIENT_NO", clientNoField);
//        
//        //在localHead_strust中加CLIENT_TYPE
//        Field clientTypeField = new Field(new FieldAttr(FieldType.FIELD_STRING, 6));
//        clientTypeField.setValue(iesbForECIFForm.getClientType());
//        localHead_struct.addField("CLIENT_TYPE", clientTypeField);
//        
//        //在localHead_strust中加INDENTIFY_RULE
//        Field indentifyRuleField = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
//        indentifyRuleField.setValue("01");
//        localHead_struct.addField("INDENTIFY_RULE", indentifyRuleField);
//        
//        cd.addStruct("LOCAL_HEAD", localHead_struct);
        
        //BODY
        CompositeData body_struct = new CompositeData();
        
        /*
        客户证件信息开始
        */
        Array C_GLOBAL_INFO_ARRAY = new Array();
        CompositeData CusArrayStruct = new CompositeData();
        //证件类型
      //证件类型
        Field GLOBAL_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
        //GLOBAL_TYPE.setValue("1");//身份证
        GLOBAL_TYPE.setValue(iesbForECIFForm.getGlobalType());//身份证
        CusArrayStruct.addField("GLOBAL_TYPE", GLOBAL_TYPE);

        //证件号码
        Field GLOBAL_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
        //GLOBAL_ID.setValue("320482198601271133");//todo:传入证件号码
        GLOBAL_ID.setValue(iesbForECIFForm.getGlobalId());//todo:传入证件号码
        CusArrayStruct.addField("GLOBAL_ID", GLOBAL_ID);

//        //证件描述
//        Field GLOBAL_DESC = new Field(new FieldAttr(FieldType.FIELD_STRING, 300));
//        GLOBAL_DESC.setValue("大陆居民身份证");
//        CusArrayStruct.addField("GLOBAL_DESC", GLOBAL_DESC);

        //证件签发地地区编码
        Field CERT_AREA_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        CERT_AREA_CODE.setValue(iesbForECIFForm.getCertAreaCode());//
        CusArrayStruct.addField("CERT_AREA_CODE", CERT_AREA_CODE);

        //发证机关
        Field CERT_ORG = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
        CERT_ORG.setValue("");//暂置空iesbForECIFForm.getCertOrg()
        CusArrayStruct.addField("CERT_ORG", CERT_ORG);

        //签发日期
        Field ISS_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
        ISS_DATE.setValue("");//暂置空formatter8.format(iesbForECIFForm.getIssDate()
        CusArrayStruct.addField("ISS_DATE", ISS_DATE);

//        //生效日期
//        Field EFFECT_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
//        EFFECT_DATE.setValue(formatter8.format(iesbForECIFForm.getEffectDate()));//暂置空
//        CusArrayStruct.addField("EFFECT_DATE", EFFECT_DATE);

        //到期日期
        Field EXPIRY_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
        EXPIRY_DATE.setValue("");//暂置空formatter8.format(iesbForECIFForm.getExpiryDate())
        CusArrayStruct.addField("EXPIRY_DATE", EXPIRY_DATE);

        //信息加入数组
        C_GLOBAL_INFO_ARRAY.addStruct(CusArrayStruct);
        body_struct.addArray("C_GLOBAL_INFO_ARRAY",C_GLOBAL_INFO_ARRAY);
        /*
        客户证件信息结束
        */
        /*
       个人客户名称信息
         */
        Array P_C_NAME_INFO_ARRAY = new Array();
        CompositeData custPerNameStuct = new CompositeData();
        //客户名称
        Field CLIENT_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
        CLIENT_NAME.setValue(iesbForECIFForm.getClientName());//暂置空
        custPerNameStuct.addField("CLIENT_NAME", CLIENT_NAME);
        
        //客户名称类型
        Field CLIENT_NAME_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        CLIENT_NAME_TYPE.setValue(iesbForECIFForm.getClientNameType());//暂置空
        custPerNameStuct.addField("CLIENT_NAME_TYPE", CLIENT_NAME_TYPE);
        
        //信息加入数组
        P_C_NAME_INFO_ARRAY.addStruct(custPerNameStuct);
        body_struct.addArray("P_C_NAME_INFO_ARRAY", P_C_NAME_INFO_ARRAY);
        /*
         * 个人客户名称信息结束
         */
        
        /*
         * 客户基本信息
         */
        CompositeData PERSON_BASE_INFO_STRUCT = new CompositeData();
       //客户类型
        Field CLIENT_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 6));
        CLIENT_TYPE.setValue(iesbForECIFForm.getClientType());//
        PERSON_BASE_INFO_STRUCT.addField("CLIENT_TYPE", CLIENT_TYPE);
        body_struct.addStruct("PERSON_BASE_INFO_STRUCT", PERSON_BASE_INFO_STRUCT);
        /*
         * 客户基本信息结束
         */
        
        /*
         * 个人客户
         */
        CompositeData PERSON_CLIENT_INFO_STRUCT = new CompositeData();
        
       //出生日期
        Field BIRTH_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        BIRTH_DATE.setValue(formatter8.format(iesbForECIFForm.getBirthDate()));//
        PERSON_CLIENT_INFO_STRUCT.addField("BIRTH_DATE", BIRTH_DATE);
        
       //性别
        Field SEX = new Field(new FieldAttr(FieldType.FIELD_STRING, 2));
        SEX.setValue(iesbForECIFForm.getSex());//
        PERSON_CLIENT_INFO_STRUCT.addField("SEX", SEX);
        
      //客户类型
        Field CLIENT_TYPE1 = new Field(new FieldAttr(FieldType.FIELD_STRING, 6));
        CLIENT_TYPE1.setValue(iesbForECIFForm.getClientType());//
        PERSON_CLIENT_INFO_STRUCT.addField("CLIENT_TYPE", CLIENT_TYPE1);
        
      //个人客户加入到body_struct
        body_struct.addStruct("PERSON_CLIENT_INFO_STRUCT", PERSON_CLIENT_INFO_STRUCT);
      /*
       * 个人客户结束
       */
        
        /*
         * 个人客户扩展信息
         */
        CompositeData P_CLIENT_EXT_INFO_STRUCT = new CompositeData();
        
        //国籍/注册地
        Field COUNTRY_CITIZEN = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
        COUNTRY_CITIZEN.setValue(iesbForECIFForm.getCountryCitizen());//
        P_CLIENT_EXT_INFO_STRUCT.addField("COUNTRY_CITIZEN", COUNTRY_CITIZEN);
        
        //户籍所在地
        Field REG_PERM_RESIDENCE = new Field(new FieldAttr(FieldType.FIELD_STRING, 60));
        REG_PERM_RESIDENCE.setValue(iesbForECIFForm.getRegPermResidence());//
        P_CLIENT_EXT_INFO_STRUCT.addField("REG_PERM_RESIDENCE", REG_PERM_RESIDENCE);
        
        //地区代码
        Field AREA_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        AREA_CODE.setValue(iesbForECIFForm.getAreaCode());//
        P_CLIENT_EXT_INFO_STRUCT.addField("AREA_CODE", AREA_CODE);
        
      //关联关系
        Field INCIDENCE_RELATION = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
        INCIDENCE_RELATION.setValue(iesbForECIFForm.getIncidenceRelation());//
        P_CLIENT_EXT_INFO_STRUCT.addField("INCIDENCE_RELATION", INCIDENCE_RELATION);
        
      //身份类别
        Field IDENTITY_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        IDENTITY_TYPE.setValue(iesbForECIFForm.getIdentityType());//
        P_CLIENT_EXT_INFO_STRUCT.addField("IDENTITY_TYPE", IDENTITY_TYPE);
        
        //加入到body_struct
        body_struct.addStruct("P_CLIENT_EXT_INFO_STRUCT", P_CLIENT_EXT_INFO_STRUCT);
        
        /*
         * 个人客户扩展信息结束
         */
        
        /*
         * 客户地址信息
         */
        Array CLIENT_ADDRESS_INFO_ARRAY = new Array();
        CompositeData custAddressStruct = new CompositeData();
        
        //地址
        Field ADDRESS = new Field(new FieldAttr(FieldType.FIELD_STRING, 300));
        ADDRESS.setValue(iesbForECIFForm.getAddress());//
        custAddressStruct.addField("ADDRESS", ADDRESS);
        
      //地址类型
        Field ADDRESS_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
        ADDRESS_TYPE.setValue(iesbForECIFForm.getAddressType());//
        custAddressStruct.addField("ADDRESS_TYPE", ADDRESS_TYPE);
        
      //邮编
        Field POSTAL_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 7));
        POSTAL_CODE.setValue(iesbForECIFForm.getPostalCode());//
        custAddressStruct.addField("POSTAL_CODE", POSTAL_CODE);
        
        //加入到数组
        CLIENT_ADDRESS_INFO_ARRAY.addStruct(custAddressStruct);
        body_struct.addArray("CLIENT_ADDRESS_INFO_ARRAY", CLIENT_ADDRESS_INFO_ARRAY);
        
        /*
         * 客户地址信息结束
         */
        
        /*
         * 客户联系信息数组
         */
        
        Array CLIENT_CONTACT_INFO_ARRAY  = new Array();
        CompositeData custContactInfoStruct = new CompositeData();
        
        //联系方式类型
        Field CONTACT_MODE_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        CONTACT_MODE_TYPE.setValue(iesbForECIFForm.getContactModeType());//
        custContactInfoStruct.addField("CONTACT_MODE_TYPE", CONTACT_MODE_TYPE);
        
        //联系方式
        Field CONTACT_MODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
        CONTACT_MODE.setValue(iesbForECIFForm.getContactMode());//
        custContactInfoStruct.addField("CONTACT_MODE", CONTACT_MODE);
        
        //加入到数组
        CLIENT_CONTACT_INFO_ARRAY.addStruct(custContactInfoStruct);
        body_struct.addArray("CLIENT_CONTACT_INFO_ARRAY", CLIENT_CONTACT_INFO_ARRAY);
        
        /*
         * 客户联系信息数组结束
         */
        
        /*
         * 个人客户职业信息
         */
        CompositeData PERSON_CLIENT_OCCUR_STRUCT = new CompositeData();
        //职业名称
        Field OCCUPATION = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
        OCCUPATION.setValue(iesbForECIFForm.getOccupation());//
        PERSON_CLIENT_OCCUR_STRUCT.addField("OCCUPATION", OCCUPATION);
        
       //单位名称
        Field COMPANY_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
        COMPANY_NAME.setValue(iesbForECIFForm.getCompanyName());//
        PERSON_CLIENT_OCCUR_STRUCT.addField("COMPANY_NAME", COMPANY_NAME);
        
        //加入到body_struct
        body_struct.addStruct("PERSON_CLIENT_OCCUR_STRUCT", PERSON_CLIENT_OCCUR_STRUCT);
        
        /*
         * 个人客户职业信息结束
         */
        
        /*
         * 请求条件信息
         */
        
        CompositeData FILTERS_STRUCT = new CompositeData();
        //客户号
        Field CLIENT_NO  = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        CLIENT_NO .setValue(clientNo);//
        FILTERS_STRUCT.addField("CLIENT_NO", CLIENT_NO);
        
        //识别规则
        Field INDENTIFY_RULE  = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        INDENTIFY_RULE .setValue("01");//按客户号识别
        FILTERS_STRUCT.addField("INDENTIFY_RULE", INDENTIFY_RULE);
        
        //识别规则
        Field CLIENT_IND_TYPE  = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        CLIENT_IND_TYPE .setValue("200");//100:对公，200:对私
        FILTERS_STRUCT.addField("CLIENT_IND_TYPE", CLIENT_IND_TYPE);
        
        //是否为主系统
        Field IS_MAJOR  = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
        IS_MAJOR .setValue("1");//核心系统送1，其它系统送空
        FILTERS_STRUCT.addField("IS_MAJOR", IS_MAJOR);
        
        //加入到body_struct
        body_struct.addStruct("FILTERS_STRUCT", FILTERS_STRUCT);
        
        /*
         * 请求条件信息结束
         */
        cd.addStruct("BODY", body_struct);
        
        return cd;
	}
	
	/**
	 * 新增接口”对私客户修改（1100200001903）“发送报文组织。
	 */
	
	public CompositeData createEcifRequestForUpdateByTh(IESBForECIFForm iesbForECIFForm,String clientNo){
		SimpleDateFormat formatter8 = new SimpleDateFormat("yyyyMMdd");
		 CompositeData cd = new CompositeData();
	        //SYS_HEAD
	        CompositeData syaHead_struct = new CompositeData();
	        //在syaHead_struct中加SERVICECODE
	        Field serviceCodeField = new Field(new FieldAttr(FieldType.FIELD_STRING, 11));
	        serviceCodeField.setValue("11002000019");//ECIF服务服务码
	        syaHead_struct.addField("SERVICE_CODE", serviceCodeField);

	        //在syaHead_struct中加SERVICESCENE
	        Field serviceSceneField = new Field(new FieldAttr(FieldType.FIELD_STRING, 2));
	        serviceSceneField.setValue("03"); //个人客户新增，对应服务场景02
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
	        
	        /*
	         * 个人客户名称信息数组
	         */
	        Array PERSON_CLIENT_NAME_INFO_ARRAY = new Array();
	        CompositeData personInfo = new CompositeData();
	       //客户名称类型
	        Field CLIENT_NAME_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
	        CLIENT_NAME_TYPE.setValue(iesbForECIFForm.getClientNameType()); 
	        personInfo.addField("CLIENT_NAME_TYPE", CLIENT_NAME_TYPE);
	        
	      //客户名称
	        Field CLIENT_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
	        CLIENT_NAME.setValue(iesbForECIFForm.getClientName()); 
	        personInfo.addField("CLIENT_NAME", CLIENT_NAME);
	        
	        PERSON_CLIENT_NAME_INFO_ARRAY.addStruct(personInfo);
	        body_struct.addArray("PERSON_CLIENT_NAME_INFO_ARRAY", PERSON_CLIENT_NAME_INFO_ARRAY);
	        
	        /*
	         * 客户证件信息数组
	         */
	        Array CLIENT_GLOBAL_INFO_ARRAY = new Array();
	        CompositeData clientInfo = new CompositeData(); 
	      //证件类型
	        Field GLOBAL_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
	        GLOBAL_TYPE.setValue(iesbForECIFForm.getGlobalType()); 
	        clientInfo.addField("GLOBAL_TYPE", GLOBAL_TYPE);
	        
	      //证件号码
	        Field GLOBAL_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
	        GLOBAL_ID.setValue(iesbForECIFForm.getGlobalId()); 
	        clientInfo.addField("GLOBAL_ID", GLOBAL_ID);
	        
	      //证件描述
	        Field GLOBAL_DESC = new Field(new FieldAttr(FieldType.FIELD_STRING, 300));
	        GLOBAL_DESC.setValue(iesbForECIFForm.getGlobalDesc()); 
	        clientInfo.addField("GLOBAL_DESC", GLOBAL_DESC);
	        
	      //证件签发地地区编码
	        Field CERT_AREA_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
	        CERT_AREA_CODE.setValue(iesbForECIFForm.getCertAreaCode());//
	        clientInfo.addField("CERT_AREA_CODE", CERT_AREA_CODE);

	        //发证机关
	        Field CERT_ORG = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
	        CERT_ORG.setValue(iesbForECIFForm.getCertOrg());//暂置空iesbForECIFForm.getCertOrg()
	        clientInfo.addField("CERT_ORG", CERT_ORG);

	        //签发日期
	        Field ISS_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
	        ISS_DATE.setValue("");//暂置空formatter8.format(iesbForECIFForm.getIssDate()
	        clientInfo.addField("ISS_DATE", ISS_DATE);

	        //到期日期
	        Field EXPIRY_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
	        EXPIRY_DATE.setValue("");//暂置空formatter8.format(iesbForECIFForm.getExpiryDate())
	        clientInfo.addField("EXPIRY_DATE", EXPIRY_DATE);
	        
	        CLIENT_GLOBAL_INFO_ARRAY.addStruct(clientInfo);
	        body_struct.addArray("CLIENT_GLOBAL_INFO_ARRAY", CLIENT_GLOBAL_INFO_ARRAY);
	        
	        /*
	         * 客户地址信息数组
	         */
	        
	        Array CLIENT_ADDRESS_INFO_ARRAY = new Array();
	        CompositeData clientAddInfo = new CompositeData();
	        //地址
	        Field ADDRESS = new Field(new FieldAttr(FieldType.FIELD_STRING, 300));
	        ADDRESS.setValue(iesbForECIFForm.getAddress());//
	        clientAddInfo.addField("ADDRESS", ADDRESS);
	        
	      //地址类型
	        Field ADDRESS_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
	        ADDRESS_TYPE.setValue(iesbForECIFForm.getAddressType());//
	        clientAddInfo.addField("ADDRESS_TYPE", ADDRESS_TYPE);
	        
	      //邮编
	        Field POSTAL_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 7));
	        POSTAL_CODE.setValue(iesbForECIFForm.getPostalCode());//
	        clientAddInfo.addField("POSTAL_CODE", POSTAL_CODE);
	        
	        CLIENT_ADDRESS_INFO_ARRAY.addStruct(clientAddInfo);
	        body_struct.addArray("CLIENT_ADDRESS_INFO_ARRAY", CLIENT_ADDRESS_INFO_ARRAY);
	        
	        /*
	         * 客户联系信息数组
	         */
	        
	        Array CLIENT_CONTACT_INFO_ARRAY = new Array();
	        CompositeData clientconInfo = new CompositeData();
	        //联系方式类型
	        Field CONTACT_MODE_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
	        CONTACT_MODE_TYPE.setValue(iesbForECIFForm.getContactModeType());//
	        clientconInfo.addField("CONTACT_MODE_TYPE", CONTACT_MODE_TYPE);
	        
	        //联系方式
	        Field CONTACT_MODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
	        CONTACT_MODE.setValue(iesbForECIFForm.getContactMode());//
	        clientconInfo.addField("CONTACT_MODE", CONTACT_MODE);
	        
	        CLIENT_CONTACT_INFO_ARRAY.addStruct(clientconInfo);
	        body_struct.addArray("CLIENT_CONTACT_INFO_ARRAY", CLIENT_CONTACT_INFO_ARRAY);
	        
	        /*
	         * 客户所属机构信息
	         */
	        
	        CompositeData CLIENT_BELONG_ORG_INFO_STRUCT = new CompositeData();
	      //客户所属机构
	        Field CLIENT_BELONG_ORG = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
	        CLIENT_BELONG_ORG.setValue(iesbForECIFForm.getClientBelongOrg());//
	        CLIENT_BELONG_ORG_INFO_STRUCT.addField("CLIENT_BELONG_ORG", CLIENT_BELONG_ORG);
	        
	        body_struct.addStruct("CLIENT_BELONG_ORG_INFO_STRUCT", CLIENT_BELONG_ORG_INFO_STRUCT);
	        
	        /*
	         * 客户所属信息
	         */
	        Array CLIENT_BELONG_INFO_ARRAY = new Array();
	        CompositeData clientBelongInfo = new CompositeData();
	        
	      //客户经理代码
	        Field CUST_MANAGER_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 16));
	        CUST_MANAGER_ID.setValue(iesbForECIFForm.getCustManagerId());//
	        clientBelongInfo.addField("CUST_MANAGER_ID", CUST_MANAGER_ID); 
	        
	      //录入柜员
	        Field RECORD_TELLER_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
	        RECORD_TELLER_NO.setValue(iesbForECIFForm.getRecordTellerNo());//
	        clientBelongInfo.addField("RECORD_TELLER_NO", RECORD_TELLER_NO);
	        
	        CLIENT_BELONG_INFO_ARRAY.addStruct(clientBelongInfo);
	        body_struct.addArray("CLIENT_BELONG_INFO_ARRAY", CLIENT_BELONG_INFO_ARRAY);
	        
	        /*
	         * 个人客户信息
	         */
	        
	        CompositeData PERSON_CLIENT_INFO_STRUCT = new CompositeData();
	        
	        //出生日期
	        Field BIRTH_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
	        BIRTH_DATE.setValue(formatter8.format(iesbForECIFForm.getBirthDate()));//
	        PERSON_CLIENT_INFO_STRUCT.addField("BIRTH_DATE", BIRTH_DATE);
	        
	       //性别
	        Field SEX = new Field(new FieldAttr(FieldType.FIELD_STRING, 2));
	        SEX.setValue(iesbForECIFForm.getSex());//
	        PERSON_CLIENT_INFO_STRUCT.addField("SEX", SEX);
	        
	        body_struct.addStruct("PERSON_CLIENT_INFO_STRUCT", PERSON_CLIENT_INFO_STRUCT);
	        
	        /*
	         * 个人客户扩展信息
	         */
	        CompositeData PERSON_CLIENT_EXT_INFO_STRUCT = new CompositeData();
	        
	        //民族代码
	        Field NATIONALITY_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
	        NATIONALITY_CODE.setValue(iesbForECIFForm.getNationalityCode());//
	        PERSON_CLIENT_EXT_INFO_STRUCT.addField("NATIONALITY_CODE", NATIONALITY_CODE);
	        //国籍/注册地
	        Field COUNTRY_CITIZEN = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
	        COUNTRY_CITIZEN.setValue(iesbForECIFForm.getCountryCitizen());//
	        PERSON_CLIENT_EXT_INFO_STRUCT.addField("COUNTRY_CITIZEN", COUNTRY_CITIZEN);
	        
	        //户籍所在地
	        Field REG_PERM_RESIDENCE = new Field(new FieldAttr(FieldType.FIELD_STRING, 60));
	        REG_PERM_RESIDENCE.setValue(iesbForECIFForm.getRegPermResidence());//
	        PERSON_CLIENT_EXT_INFO_STRUCT.addField("REG_PERM_RESIDENCE", REG_PERM_RESIDENCE);
	        
	        //开户机构
	        Field OPEN_ACCT_BRANCH_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
	        OPEN_ACCT_BRANCH_ID.setValue(iesbForECIFForm.getOpenAcctBranchId());//
	        PERSON_CLIENT_EXT_INFO_STRUCT.addField("OPEN_ACCT_BRANCH_ID", OPEN_ACCT_BRANCH_ID);
	        
	      //开户柜员
	        Field OPEN_TELLER_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
	        OPEN_TELLER_NO.setValue(iesbForECIFForm.getOpenTellerNo());//
	        PERSON_CLIENT_EXT_INFO_STRUCT.addField("OPEN_TELLER_NO", OPEN_TELLER_NO);
	        
	        //开户日期
	        Field OPEN_ACCT_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
	        OPEN_ACCT_DATE.setValue(formatter8.format(iesbForECIFForm.getOpenAcctDate()));//
	        PERSON_CLIENT_EXT_INFO_STRUCT.addField("OPEN_ACCT_DATE", OPEN_ACCT_DATE);
	        
	        //婚姻状况
	        Field MARITAL_STATUS = new Field(new FieldAttr(FieldType.FIELD_STRING, 2));
	        MARITAL_STATUS.setValue(iesbForECIFForm.getMaritalStatus());//
	        PERSON_CLIENT_EXT_INFO_STRUCT.addField("MARITAL_STATUS", MARITAL_STATUS);
	        
	        //教育水平
	        Field EDUCATION_LEVEL = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
	        EDUCATION_LEVEL.setValue(iesbForECIFForm.getEducationLevel());//
	        PERSON_CLIENT_EXT_INFO_STRUCT.addField("EDUCATION_LEVEL", EDUCATION_LEVEL);
	        
	        //城市
	        Field CITY = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
	        CITY.setValue(iesbForECIFForm.getCity());//
	        PERSON_CLIENT_EXT_INFO_STRUCT.addField("CITY", CITY);
	        
	        //地区代码
	        Field AREA_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
	        AREA_CODE.setValue(iesbForECIFForm.getAreaCode());//
	        PERSON_CLIENT_EXT_INFO_STRUCT.addField("AREA_CODE", AREA_CODE);
	        
	        
	      //身份类别
	        Field IDENTITY_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
	        IDENTITY_TYPE.setValue(iesbForECIFForm.getIdentityType());//
	        PERSON_CLIENT_EXT_INFO_STRUCT.addField("IDENTITY_TYPE", IDENTITY_TYPE);
	        
	        body_struct.addStruct("PERSON_CLIENT_EXT_INFO_STRUCT", PERSON_CLIENT_EXT_INFO_STRUCT);
	        
	        cd.addStruct("BODY", body_struct);
	        
	        return cd;
	}
	/**
     * 解析CompositeData报文
     * @param cd
     */
	public String parseEcifResponse(CompositeData cd){
		if(cd == null){
    		return null;
    	}
		CompositeData sysHead = cd.getStruct("SYS_HEAD");
		
        CompositeData body = cd.getStruct("BODY");
        
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
        
        return RET_CODE;
        
	}
}
