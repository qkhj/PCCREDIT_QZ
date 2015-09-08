package com.cardpay.pccredit.QZBankInterface.service;

/**
 * ECIF接口，经ESB转发
 * Created by johhny on 15/4/11.
 */
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.QZBankInterface.model.ECIF;
import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;

@Service
public class IESBForECIF {
	@Autowired
	private CommonDao commonDao;
    /**
     * 组装CompositeData报文
     * @return
     */
    public CompositeData createEcifRequest(ECIF ecif) {
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
        serviceSceneField.setValue("02"); //个人客户新增，对应服务场景02
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
        
//        /*
//         * 根据customerId获取实体
//         */
//        String customerId = "";
//        CustomerInfor infor = commonDao.findObjectById(CustomerInfor.class, customerId);
        
        
        /*
        客户证件信息开始
        */
        Array C_GLOBAL_INFO_ARRAY = new Array(); //C_GLOBAL_INFO_ARRAY数组
        CompositeData CusArrayStruct = new CompositeData();
        //证件类型
        Field GLOBAL_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
        //GLOBAL_TYPE.setValue("1");//身份证
        GLOBAL_TYPE.setValue(ecif.getGlobalType());//身份证
        CusArrayStruct.addField("GLOBAL_TYPE", GLOBAL_TYPE);

        //证件号码
        Field GLOBAL_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
        //GLOBAL_ID.setValue("320482198601271133");//todo:传入证件号码
        GLOBAL_ID.setValue(ecif.getGlobalId());//todo:传入证件号码
        CusArrayStruct.addField("GLOBAL_ID", GLOBAL_ID);

        //证件描述
        Field GLOBAL_DESC = new Field(new FieldAttr(FieldType.FIELD_STRING, 300));
        GLOBAL_DESC.setValue("大陆居民身份证");
        CusArrayStruct.addField("GLOBAL_DESC", GLOBAL_DESC);

        //证件签发地地区编码
        Field CERT_AREA_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        CERT_AREA_CODE.setValue("");//暂置空
        CusArrayStruct.addField("CERT_AREA_CODE", CERT_AREA_CODE);

        //发证机关
        Field CERT_ORG = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
        CERT_ORG.setValue("");//暂置空
        CusArrayStruct.addField("CERT_ORG", CERT_ORG);

        //签发日期
        Field ISS_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
        ISS_DATE.setValue("");//暂置空
        CusArrayStruct.addField("ISS_DATE", ISS_DATE);

        //生效日期
        Field EFFECT_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
        EFFECT_DATE.setValue("");//暂置空
        CusArrayStruct.addField("EFFECT_DATE", EFFECT_DATE);

        //到期日期
        Field EXPIRY_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
        EXPIRY_DATE.setValue("");//暂置空
        CusArrayStruct.addField("EXPIRY_DATE", EXPIRY_DATE);

        //信息加入数组
        C_GLOBAL_INFO_ARRAY.addStruct(CusArrayStruct);
        body_struct.addArray("C_GLOBAL_INFO_ARRAY",C_GLOBAL_INFO_ARRAY);
        /*
        客户证件信息结束
        */

        /*
        个人客户名称信息开始
         */
        Array P_C_NAME_INFO_ARRAY = new Array(); //P_C_NAME_INFO_ARRAY数组
        CompositeData CusNameArrayStruct = new CompositeData();
        //客户名称
        Field CLIENT_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
        //CLIENT_NAME.setValue("贺珈");//todo:传入客户名称
        CLIENT_NAME.setValue(ecif.getClientName());//todo:传入客户名称
        CusNameArrayStruct.addField("CLIENT_NAME", CLIENT_NAME);

        //客户名称类型
        Field CLIENT_NAME_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //CLIENT_NAME_TYPE.setValue("20");//个人名称
        CLIENT_NAME_TYPE.setValue(ecif.getClientNameType());//个人名称
        CusNameArrayStruct.addField("CLIENT_NAME_TYPE", CLIENT_NAME_TYPE);

        //信息加入数组
        P_C_NAME_INFO_ARRAY.addStruct(CusNameArrayStruct);
        body_struct.addArray("P_C_NAME_INFO_ARRAY",P_C_NAME_INFO_ARRAY);
        /*
        个人客户名称信息结束
         */

        /*
        个人客户基本信息开始
         */
        CompositeData PERSON_BASE_INFO_STRUCT = new CompositeData();
        //客户类型
        Field CLIENT_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 6));
        //CLIENT_TYPE.setValue("Z");//个人客户 todo:文档中多个个人客户码，测试需明确
        CLIENT_TYPE.setValue(ecif.getClientType());//个人客户 todo:文档中多个个人客户码，测试需明确
        PERSON_BASE_INFO_STRUCT.addField("CLIENT_TYPE", CLIENT_TYPE);

        //客户状态
        Field CLIENT_STATUS = new Field(new FieldAttr(FieldType.FIELD_STRING, 1));
        //CLIENT_STATUS.setValue("0");//有效 todo:若涉及预登记，需要置为3
        CLIENT_STATUS.setValue(ecif.getClientStatus());//有效 todo:若涉及预登记，需要置为3
        PERSON_BASE_INFO_STRUCT.addField("CLIENT_STATUS", CLIENT_STATUS);

        //信息加入body_struct
        body_struct.addStruct("PERSON_BASE_INFO_STRUCT",PERSON_BASE_INFO_STRUCT);
        /*
        个人客户基本信息结束
         */

        /*
        个人客户信息开始
         */
        CompositeData PERSON_CLIENT_INFO_STRUCT = new CompositeData();
        //出生日期
        Field BIRTH_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
        //BIRTH_DATE.setValue("19860127");//todo:传入出生日期，格式YYYY-MM-dd 
        BIRTH_DATE.setValue(formatter8.format(ecif.getBirthDate()));//todo:传入出生日期，格式YYYY-MM-dd
        PERSON_CLIENT_INFO_STRUCT.addField("BIRTH_DATE", BIRTH_DATE);

        //性别
        Field SEX = new Field(new FieldAttr(FieldType.FIELD_STRING, 2));
        //SEX.setValue("01");//todo:传入性别，男：01 ；女：02； 未知：03
        SEX.setValue(ecif.getSex());//todo:传入性别，男：01 ；女：02； 未知：03
        PERSON_CLIENT_INFO_STRUCT.addField("SEX", SEX);

        //客户类型
        Field P_CLIENT_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 6));
        //P_CLIENT_TYPE.setValue("Z");//个人客户 todo:文档中多个个人客户码，测试需明确
        P_CLIENT_TYPE.setValue(ecif.getClientType());//个人客户 todo:文档中多个个人客户码，测试需明确
        PERSON_CLIENT_INFO_STRUCT.addField("CLIENT_TYPE", P_CLIENT_TYPE);

        //信息加入body_struct
        body_struct.addStruct("PERSON_CLIENT_INFO_STRUCT",PERSON_CLIENT_INFO_STRUCT);

        /*
        个人客户信息结束
         */

        /*
        客户所属信息开始
         */
        CompositeData CLIENT_BELONG_INFO_STRUCT = new CompositeData();
        //客户经理代码
        Field CUST_MANAGER_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 16));
        //CUST_MANAGER_ID.setValue("0389");//todo:传入客户经理代码，柜员号
        CUST_MANAGER_ID.setValue(ecif.getCustManagerId());//todo:传入客户经理代码，柜员号
        CLIENT_BELONG_INFO_STRUCT.addField("CUST_MANAGER_ID", CUST_MANAGER_ID);

        //录入柜员
        Field RECORD_TELLER_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
        //RECORD_TELLER_NO.setValue("0389");//todo:传入录入人员代码，柜员号
        RECORD_TELLER_NO.setValue(ecif.getRecordTellerNo());//todo:传入录入人员代码，柜员号
        CLIENT_BELONG_INFO_STRUCT.addField("RECORD_TELLER_NO", RECORD_TELLER_NO);

        //登记日期
        Field REGISTERED_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //REGISTERED_DATE.setValue("20150414");//todo:传入登记日期，格式YYYYMMdd
        REGISTERED_DATE.setValue((ecif.getRegisteredDate()==null) ? "":formatter8.format(ecif.getRegisteredDate()));//todo:传入登记日期，格式YYYYMMdd
        CLIENT_BELONG_INFO_STRUCT.addField("REGISTERED_DATE", REGISTERED_DATE);

        //信息加入body_struct
        body_struct.addStruct("CLIENT_BELONG_INFO_STRUCT",CLIENT_BELONG_INFO_STRUCT);

        /*
        客户所属信息结束
         */

        /*
        客户所属机构信息开始
         */
        CompositeData C_BELONG_ORG_INFO_STRUCT = new CompositeData();
        //客户所属机构
        Field CLIENT_BELONG_ORG = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //CLIENT_BELONG_ORG.setValue("9350501001");//todo:传入客户所属机构，机构码
        CLIENT_BELONG_ORG.setValue(ecif.getClientBelongOrg());//todo:传入客户所属机构，机构码
        C_BELONG_ORG_INFO_STRUCT.addField("CLIENT_BELONG_ORG", CLIENT_BELONG_ORG);

        //登记柜员号
        Field REGISTERED_TELLER_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
        //RECORD_TELLER_NO.setValue("0389");//todo:传入登记人员代码，柜员号
        RECORD_TELLER_NO.setValue(ecif.getRegisteredTellerNo());//todo:传入登记人员代码，柜员号
        C_BELONG_ORG_INFO_STRUCT.addField("REGISTERED_TELLER_NO", REGISTERED_TELLER_NO);

        //登记机构
        Field REGIST_ORG_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //REGISTERED_DATE.setValue("9350501001");//todo:传入登记机构
        REGISTERED_DATE.setValue(ecif.getRegistOrgNo());//todo:传入登记机构
        C_BELONG_ORG_INFO_STRUCT.addField("REGIST_ORG_NO", REGIST_ORG_NO);

        //登记日期
        Field ORG_REGISTERED_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //ORG_REGISTERED_DATE.setValue("20150414");//todo:传入登记日期，格式YYYYMMdd
        ORG_REGISTERED_DATE.setValue(ecif.getOrgRegisteredDate()==null ? "":formatter8.format(ecif.getOrgRegisteredDate()));//todo:传入登记日期，格式YYYYMMdd
        C_BELONG_ORG_INFO_STRUCT.addField("ORG_REGISTERED_DATE", ORG_REGISTERED_DATE);

        //信息加入body_struct
        body_struct.addStruct("C_BELONG_ORG_INFO_STRUCT",C_BELONG_ORG_INFO_STRUCT);
        /*
        客户所属机构信息结束
         */

        /*
        个人客户扩展信息开始
         */
        CompositeData P_CLIENT_EXT_INFO_STRUCT = new CompositeData();
        //国籍、注册地
        Field COUNTRY_CITIZEN = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
        //COUNTRY_CITIZEN.setValue("CHN");//中国
        COUNTRY_CITIZEN.setValue(ecif.getCountryCitizen());//中国
        P_CLIENT_EXT_INFO_STRUCT.addField("COUNTRY_CITIZEN", COUNTRY_CITIZEN);

        //民族代码
        Field NATIONALITY_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //NATIONALITY_CODE.setValue("01");//todo:传入民族，例如 “汉”
        NATIONALITY_CODE.setValue(ecif.getNationalityCode());//todo:传入民族，例如 “汉”
        P_CLIENT_EXT_INFO_STRUCT.addField("NATIONALITY_CODE", NATIONALITY_CODE);

        //户籍所在地
        Field REG_PERM_RESIDENCE = new Field(new FieldAttr(FieldType.FIELD_STRING, 60));
        //REG_PERM_RESIDENCE.setValue("110228");//todo:传入户籍所在地
        REG_PERM_RESIDENCE.setValue(ecif.getRegPermResidence());//todo:传入户籍所在地
        P_CLIENT_EXT_INFO_STRUCT.addField("REG_PERM_RESIDENCE", REG_PERM_RESIDENCE);

        //开户机构
        Field OPEN_ACCT_BRANCH_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //OPEN_ACCT_BRANCH_ID.setValue("9350501001");//todo:传入开户机构，界面如无，需要增加客户经理选择开户机构
        OPEN_ACCT_BRANCH_ID.setValue(ecif.getOpenAcctBranchId());//todo:传入开户机构，界面如无，需要增加客户经理选择开户机构
        P_CLIENT_EXT_INFO_STRUCT.addField("OPEN_ACCT_BRANCH_ID", OPEN_ACCT_BRANCH_ID);

        //开户柜员
        Field OPEN_TELLER_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
        //OPEN_TELLER_NO.setValue("0389");//todo:传入客户经理柜员号
        OPEN_TELLER_NO.setValue(ecif.getOpenTellerNo());//todo:传入客户经理柜员号
        P_CLIENT_EXT_INFO_STRUCT.addField("OPEN_TELLER_NO", OPEN_TELLER_NO);

        //开户日期
        Field OPEN_ACCT_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //OPEN_ACCT_DATE.setValue("20150414");//todo:传入开户日期
        OPEN_ACCT_DATE.setValue(formatter8.format(ecif.getOpenAcctDate()));//todo:传入开户日期
        P_CLIENT_EXT_INFO_STRUCT.addField("OPEN_ACCT_DATE", OPEN_ACCT_DATE);

        //婚姻状况 todo:是否缺失数据字典
        Field MARITAL_STATUS = new Field(new FieldAttr(FieldType.FIELD_STRING, 2));
        //MARITAL_STATUS.setValue("已婚");//todo:传入婚姻状况
        MARITAL_STATUS.setValue(ecif.getMaritalStatus());//todo:传入婚姻状况
        P_CLIENT_EXT_INFO_STRUCT.addField("MARITAL_STATUS", MARITAL_STATUS);

        //教育水平 todo:是否缺失数据字典
        Field EDUCATION_LEVEL = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //EDUCATION_LEVEL.setValue("10");//todo:传入教育水平
        EDUCATION_LEVEL.setValue(ecif.getEducationLevel());//todo:传入教育水平
        P_CLIENT_EXT_INFO_STRUCT.addField("EDUCATION_LEVEL", EDUCATION_LEVEL);

        //城市 todo:是否缺失数据字典
        Field CITY = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //CITY.setValue("110101");//todo:传入城市
        CITY.setValue(ecif.getCity());//todo:传入城市
        P_CLIENT_EXT_INFO_STRUCT.addField("CITY", CITY);

        //地区代码 todo:是否缺失数据字典
        Field AREA_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //AREA_CODE.setValue("110101");//todo:传入地区代码
        AREA_CODE.setValue(ecif.getAreaCode());//todo:传入地区代码
        P_CLIENT_EXT_INFO_STRUCT.addField("AREA_CODE", AREA_CODE);

        //关联关系 todo:需明确内容
        Field INCIDENCE_RELATION = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
        //INCIDENCE_RELATION.setValue("B1");//todo:传入关联关系
        INCIDENCE_RELATION.setValue(ecif.getIncidenceRelation());//todo:传入关联关系
        P_CLIENT_EXT_INFO_STRUCT.addField("INCIDENCE_RELATION", INCIDENCE_RELATION);

        //身份类别 todo:是否缺失数据字典
        Field IDENTITY_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //IDENTITY_TYPE.setValue("01");//todo:传入身份类别
        IDENTITY_TYPE.setValue(ecif.getIdentityType());//todo:传入身份类别
        P_CLIENT_EXT_INFO_STRUCT.addField("IDENTITY_TYPE", IDENTITY_TYPE);

        //信息加入body_struct
        body_struct.addStruct("P_CLIENT_EXT_INFO_STRUCT",P_CLIENT_EXT_INFO_STRUCT);
        /*
        个人客户扩展信息结束
         */

        /*
        客户地址信息开始
         */
        Array CLIENT_ADDRESS_INFO_ARRAY = new Array(); //CLIENT_ADDRESS_INFO_ARRAY数组
        //todo:多个地址需要循环加入
        CompositeData CusAddrArrayStruct = new CompositeData();
        //地址
        Field ADDRESS = new Field(new FieldAttr(FieldType.FIELD_STRING, 300));
        //ADDRESS.setValue("江苏省常州市");//todo:传入地址
        ADDRESS.setValue(ecif.getAddress());//todo:传入地址
        CusAddrArrayStruct.addField("ADDRESS", ADDRESS);

        //地址类型
        Field ADDRESS_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
        //ADDRESS_TYPE.setValue("01");//todo:传入地址类型，字符
        ADDRESS_TYPE.setValue(ecif.getAddressType());//todo:传入地址类型，字符
        CusAddrArrayStruct.addField("ADDRESS_TYPE", ADDRESS_TYPE);

        //邮编
        Field POSTAL_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 7));
        //POSTAL_CODE.setValue("210046");//todo:传入邮编
        POSTAL_CODE.setValue(ecif.getPostalCode());//todo:传入邮编
        CusAddrArrayStruct.addField("POSTAL_CODE", POSTAL_CODE);

        //信息加入数组
        CLIENT_ADDRESS_INFO_ARRAY.addStruct(CusAddrArrayStruct);
        body_struct.addArray("CLIENT_ADDRESS_INFO_ARRAY",CLIENT_ADDRESS_INFO_ARRAY);
        /*
        客户地址信息结束
         */

        /*
        客户联系信息开始
         */
        Array CLIENT_CONTACT_INFO_ARRAY  = new Array(); //CLIENT_CONTACT_INFO_ARRAY  数组
        //todo:多个联系方式需要循环加入
        CompositeData CusConArrayStruct = new CompositeData();
        //联系方式类型 todo:是否缺失数据字典
        Field CONTACT_MODE_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //CONTACT_MODE_TYPE.setValue("01");//todo:联系方式类型
        CONTACT_MODE_TYPE.setValue(ecif.getContactModeType());//todo:联系方式类型
        CusConArrayStruct.addField("CONTACT_MODE_TYPE", CONTACT_MODE_TYPE);

        //联系方式
        Field CONTACT_MODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
        //CONTACT_MODE.setValue("15961100228");//todo:传入联系方式
        CONTACT_MODE.setValue(ecif.getContactMode());//todo:传入联系方式
        CusConArrayStruct.addField("CONTACT_MODE", CONTACT_MODE);

        //信息加入数组
        CLIENT_CONTACT_INFO_ARRAY.addStruct(CusConArrayStruct);
        body_struct.addArray("CLIENT_CONTACT_INFO_ARRAY",CLIENT_CONTACT_INFO_ARRAY);
        /*
        客户联系信息结束
         */

        /*
        个人客户职业信息开始
         */
        CompositeData PERSON_CLIENT_OCCUR_STRUCT = new CompositeData();
        //职业名称
        Field OCCUPATION = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
        //OCCUPATION.setValue("2A");//todo:传入职业名称 字符
        OCCUPATION.setValue(ecif.getOccupation());//todo:传入职业名称 字符
        PERSON_CLIENT_OCCUR_STRUCT.addField("OCCUPATION", OCCUPATION);

        //公司名称
        Field COMPANY_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
        //COMPANY_NAME.setValue("乾康金融");//todo:传入公司名称 字符
        COMPANY_NAME.setValue(ecif.getCompanyName());//todo:传入公司名称 字符
        PERSON_CLIENT_OCCUR_STRUCT.addField("COMPANY_NAME", COMPANY_NAME);

        //信息加入body_struct
        body_struct.addStruct("PERSON_CLIENT_OCCUR_STRUCT",PERSON_CLIENT_OCCUR_STRUCT);

        /*
        个人客户职业信息结束
         */


        cd.addStruct("BODY",body_struct);



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
        CompositeData body = cd.getStruct("BODY");

        //根据数组名称去获取数组
        Array array = body.getArray("C_GLOBAL_INFO_ARRAY");

        String CLIENT_NO = "";//客户号
        String GLOBAL_TYPE = "";//证件类型
        String GLOBAL_ID = "";//证件号码

        if(null != array && array.size() > 0){
            int m = array.size();
            CompositeData array_element = null;
            for (int i = 0; i < m; i++) {
                //数组中的元素也是CompositeData，这是固定的写法。根据游标就可以获取到数组中的所有元素
                array_element = array.getStruct(i);

                CLIENT_NO=array_element.getField("CLIENT_NO").strValue();
                GLOBAL_TYPE=array_element.getField("GLOBAL_TYPE").strValue();
                GLOBAL_ID=array_element.getField("GLOBAL_ID").strValue();
            }
        }
        
        return CLIENT_NO;
    }
    
}
