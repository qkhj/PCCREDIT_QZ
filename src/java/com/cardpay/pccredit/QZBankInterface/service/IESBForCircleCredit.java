package com.cardpay.pccredit.QZBankInterface.service;

/**
 * 循环贷接口，经ESB转发
 * Created by johhny on 15/4/13.
 */
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.QZBankInterface.model.Circle_ACCT_INFO;
import com.cardpay.pccredit.QZBankInterface.util.DateUtil;
import com.cardpay.pccredit.common.Arith;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.manager.constant.ManagerBelongMapConstants;
import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.modules.dictionary.DictionaryManager;
import com.wicresoft.jrad.modules.dictionary.model.Dictionary;
import com.wicresoft.jrad.modules.dictionary.model.DictionaryItem;
import com.wicresoft.util.spring.Beans;
@Service
public class IESBForCircleCredit {
	@Autowired
	private CommonDao commonDao;
    /**
     * 组装CompositeData报文
     * @return
     */
    public static CompositeData createCircleCreditRequest(Circle circle,List<Circle_ACCT_INFO> acct_info_ls){
    	SimpleDateFormat formatter2 = new SimpleDateFormat("dd");
    	SimpleDateFormat formatter8 = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat formatter10 = new SimpleDateFormat("yyyy-MM-dd");
    	
        CompositeData cd = new CompositeData();

        //SYS_HEAD
        CompositeData syaHead_struct = new CompositeData();
        //在syaHead_struct中加SERVICECODE
        Field serviceCodeField = new Field(new FieldAttr(FieldType.FIELD_STRING, 11));
        serviceCodeField.setValue("02002000010");//循环贷服务服务码
        syaHead_struct.addField("SERVICE_CODE", serviceCodeField);

        //在syaHead_struct中加SERVICESCENE
        Field serviceSceneField = new Field(new FieldAttr(FieldType.FIELD_STRING, 2));
        serviceSceneField.setValue("01"); //循环贷对应服务场景01
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

        //合同号
        Field CONTRACT_NO=new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
        //CONTRACT_NO.setValue("0001");//todo:传入合同号
        CONTRACT_NO.setValue(circle.getContractNo());//todo:传入合同号
        body_struct.addField("CONTRACT_NO", CONTRACT_NO);

        //卡号
        Field CARD_NO=new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //CARD_NO.setValue("0000001");//todo:传入卡号
        CARD_NO.setValue(circle.getCardNo());//todo:传入卡号
        body_struct.addField("CARD_NO", CARD_NO);

        //客户号
        Field CLIENT_NO=new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //CLIENT_NO.setValue("00001");//todo:传入客户号
        CLIENT_NO.setValue(circle.getClientNo());//todo:传入客户号
        body_struct.addField("CLIENT_NO", CLIENT_NO);

        //担保方式 todo:非ESB提供数据字典
        Field GUARANTEE_MODE=new Field(new FieldAttr(FieldType.FIELD_STRING,10));
        //GUARANTEE_MODE.setValue("100");//抵押
        GUARANTEE_MODE.setValue(circle.getGuaranteeMode());//抵押.setValue("100");//抵押
        body_struct.addField("GUARANTEE_MODE", GUARANTEE_MODE);

        //担保方式细分 todo:非ESB提供数据字典
        Field GUARANTEE_MODE_DETAIL=new Field(new FieldAttr(FieldType.FIELD_STRING,10));
        //GUARANTEE_MODE_DETAIL.setValue("1");
        GUARANTEE_MODE_DETAIL.setValue(circle.getGuaranteeModeDetail());
        body_struct.addField("GUARANTEE_MODE_DETAIL", GUARANTEE_MODE_DETAIL);

        //币种
        Field CCY=new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
        //CCY.setValue("CNY");//人民币
        CCY.setValue(circle.getCcy1());//人民币
        body_struct.addField("CCY", CCY);

        //合同金额
        Field CONTRACT_AMT=new Field(new FieldAttr(FieldType.FIELD_DOUBLE,20,2));
        //CONTRACT_AMT.setValue("");//todo：传入合同金额
        if(circle.getContractAmt() != null && !circle.getContractAmt().equals("")){
        	CONTRACT_AMT.setValue(Double.valueOf(circle.getContractAmt()));//todo：传入合同金额
        }else{
        	CONTRACT_AMT.setValue(Double.valueOf("0.0"));//todo：传入合同金额
        }
        
        body_struct.addField("CONTRACT_AMT", CONTRACT_AMT);

        //贷款起始日期
        Field START_DATE=new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //START_DATE.setValue("20150414");//todo:传入起始日期 YYYYMMdd
        START_DATE.setValue(formatter10.format(circle.getStartDate()));//todo:传入起始日期 YYYYMMdd
        body_struct.addField("START_DATE", START_DATE);

        //贷款结束日期
        Field END_DATE=new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //END_DATE.setValue("20160414");//todo:传入结束日期
        //END_DATE.setValue(formatter10.format(circle.getEndDate()));//todo:传入结束日期
        //结束日期加上期限传给信贷
        END_DATE.setValue(formatter10.format(DateUtil.shiftMonth(circle.getEndDate(), Integer.parseInt(circle.getTerm()))));
        //END_DATE.setValue(formatter10.format(circle.getEndDate()));
        body_struct.addField("END_DATE", END_DATE);

        //外币时需要换算
        Field EXCHANGE_RATE=new Field(new FieldAttr(FieldType.FIELD_DOUBLE,20,2));
        //EXCHANGE_RATE.setValue("0.2");//todo：传入外币需要换算汇率
        if(circle.getExchangeRate() != null && !circle.getExchangeRate().equals("")){
        	EXCHANGE_RATE.setValue(Double.valueOf(circle.getExchangeRate()));//todo：传入外币需要换算汇率
        }else{
        	EXCHANGE_RATE.setValue(Double.valueOf("0.0"));//todo：传入外币需要换算汇率
        }
        body_struct.addField("EXCHANGE_RATE", EXCHANGE_RATE);

        //管理机构
        Field MANA_ORG=new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //MANA_ORG.setValue("9350501001");//todo:传入管理机构
        MANA_ORG.setValue(circle.getManaOrg());
        body_struct.addField("MANA_ORG", MANA_ORG);

        //登记人
        Field REGISTERED_ID=new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //REGISTERED_ID.setValue("0389");//todo:传入当前客户经理柜员号
        REGISTERED_ID.setValue(circle.getRegisteredId());
        body_struct.addField("REGISTERED_ID", REGISTERED_ID);

        //登记机构
        Field REGIST_ORG_NO=new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //REGIST_ORG_NO.setValue("9350501001");//todo:传入登记机构
        REGIST_ORG_NO.setValue(circle.getRegistOrgNo());
        body_struct.addField("REGIST_ORG_NO", REGIST_ORG_NO);

        //入账机构码
        Field INCOME_ORG_NO=new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //INCOME_ORG_NO.setValue("0003");//todo:传入入账机构码（没找到）
        INCOME_ORG_NO.setValue(circle.getIncomeOrgNo());
        body_struct.addField("INCOME_ORG_NO", INCOME_ORG_NO);

        //登记日期
        Field REGISTERED_DATE=new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        REGISTERED_DATE.setValue("2015-04-14");//todo:传入登记日期
        REGISTERED_DATE.setValue(formatter10.format(circle.getRegisteredDate()));//todo:传入登记日期
        body_struct.addField("REGISTERED_DATE", REGISTERED_DATE);

        //期限
        Field TERM=new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //TERM.setValue("2");//todo:传入期限
        TERM.setValue(circle.getTerm());//todo:传入期限
        body_struct.addField("TERM", TERM);

        //期限类型 todo:非ESB提供数据字典
        Field TERM_TYPE=new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
        //TERM_TYPE.setValue("002");
        TERM_TYPE.setValue(circle.getTermType());
        body_struct.addField("TERM_TYPE", TERM_TYPE);

        //顺延标志
        Field DEFER_FLAG=new Field(new FieldAttr(FieldType.FIELD_STRING, 2));
        //DEFER_FLAG.setValue("1");
        DEFER_FLAG.setValue(circle.getDeferFlag());
        body_struct.addField("DEFER_FLAG", DEFER_FLAG);

        //执行利率
        Field ACT_INT_RATE=new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20,7));
        //ACT_INT_RATE.setValue("0.5");//todo:传入执行利率
        ACT_INT_RATE.setValue(Arith.div(Double.valueOf(circle.getActIntRate()), 100.0, 4));//todo:传入执行利率
        body_struct.addField("ACT_INT_RATE", ACT_INT_RATE);

        //逾期利率
        Field OVERDUE_INT_RATE=new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20,7));
        //OVERDUE_INT_RATE.setValue("0.01");//todo:传入逾期利率
        OVERDUE_INT_RATE.setValue(Arith.div(Double.valueOf(circle.getOverdueIntRate()), 100.0, 4));//todo:传入逾期利率
        body_struct.addField("OVERDUE_INT_RATE", OVERDUE_INT_RATE);

        //违约利率
        Field PENALTY_INT_RATE=new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20,7));
        //PENALTY_INT_RATE.setValue("0.01");//todo:传入违约利率
        PENALTY_INT_RATE.setValue(Arith.div(Double.valueOf(circle.getPenaltyIntRate()), 100.0, 4));//todo:传入违约利率
        body_struct.addField("PENALTY_INT_RATE", PENALTY_INT_RATE);

        //还款方式 todo:非ESB提供数据字典
        Field REPAY_TYPE=new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
        //REPAY_TYPE.setValue("A004");
        REPAY_TYPE.setValue(circle.getRepayType());
        body_struct.addField("REPAY_TYPE", REPAY_TYPE);

        //还款日期
        Field REPAY_DATE=new Field(new FieldAttr(FieldType.FIELD_STRING, 2));
        //REPAY_DATE.setValue("21");//todo:传入还款日期(不是年月日 日期即可)
        REPAY_DATE.setValue(circle.getRepayDate());//todo:传入还款日期
        body_struct.addField("REPAY_DATE", REPAY_DATE);

        //五级分类
        Field FIVE_LEVEL_TYPE=new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //FIVE_LEVEL_TYPE.setValue("10");
        FIVE_LEVEL_TYPE.setValue(circle.getFiveLevelType());
        body_struct.addField("FIVE_LEVEL_TYPE", FIVE_LEVEL_TYPE);

        //特殊贷款类型 todo:非ESB提供数据字典
        Field SPE_LOAN_TYPE=new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
        //SPE_LOAN_TYPE.setValue("1");
        SPE_LOAN_TYPE.setValue(circle.getSpeLoanType());
        body_struct.addField("SPE_LOAN_TYPE", SPE_LOAN_TYPE);

        //额度占用类型 todo:非ESB提供数据字典
        Field LIMIT_USEED_TYPE=new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
        //LIMIT_USEED_TYPE.setValue("01");
        LIMIT_USEED_TYPE.setValue(circle.getLimitUseedType());
        body_struct.addField("LIMIT_USEED_TYPE", LIMIT_USEED_TYPE);

        //借款用途 todo:非ESB提供数据字典
        Field DR_USAGE=new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
        //DR_USAGE.setValue("12");//todo:传入借款用途
        DR_USAGE.setValue(circle.getDrUsage());//todo:传入借款用途
        body_struct.addField("DR_USAGE", DR_USAGE);

        //工业转型升级标识 todo:非ESB提供数据字典
        Field FLAG=new Field(new FieldAttr(FieldType.FIELD_STRING, 1));
        //FLAG.setValue("2");
        FLAG.setValue(circle.getFlag());
        body_struct.addField("FLAG", FLAG);

        //贷款种类 todo:缺少数据字典
        Field LOAN_KIND=new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //LOAN_KIND.setValue("100034");//todo:传入贷款种类
        LOAN_KIND.setValue(circle.getLoanKind());//todo:传入贷款种类
        body_struct.addField("LOAN_KIND", LOAN_KIND);

        //涉农贷款类型 todo:缺少数据字典
        Field AGRI_LOAN_KIND=new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //AGRI_LOAN_KIND.setValue("1010");//todo:传入涉农贷款类型
        AGRI_LOAN_KIND.setValue(circle.getAgriLoanKind());//todo:传入涉农贷款类型
        body_struct.addField("AGRI_LOAN_KIND", AGRI_LOAN_KIND);

        //个人经营性贷款类型 todo:缺少数据字典
        Field PERSON_LOAN_KIND=new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //PERSON_LOAN_KIND.setValue("10");//todo:传入个人经营性贷款类型
        PERSON_LOAN_KIND.setValue(circle.getPersonLoanKind());//todo:传入个人经营性贷款类型
        body_struct.addField("PERSON_LOAN_KIND", PERSON_LOAN_KIND);

        //调整类型 todo:缺少数据字典
        Field ADJUST_TYPE=new Field(new FieldAttr(FieldType.FIELD_STRING, 1));
        //ADJUST_TYPE.setValue("2");//todo:传入调整类型
        ADJUST_TYPE.setValue(circle.getAdjustType());//todo:传入调整类型
        body_struct.addField("ADJUST_TYPE", ADJUST_TYPE);

        //新兴产业类型 todo:缺少数据字典
        Field NEW_PRD_TYPE=new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
        //NEW_PRD_TYPE.setValue("0");//todo:传入新兴产业类型
        NEW_PRD_TYPE.setValue(circle.getNewPrdType());//todo:传入新兴产业类型
        body_struct.addField("NEW_PRD_TYPE", NEW_PRD_TYPE);

        //新兴产业贷款
        Field NEW_PRD_LOAN=new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
        //NEW_PRD_LOAN.setValue("0");//todo:传入新兴产业贷款
        NEW_PRD_LOAN.setValue(circle.getNewPrdLoan());//todo:传入新兴产业贷款
        body_struct.addField("NEW_PRD_LOAN", NEW_PRD_LOAN);

        //贷款投向
        Field LOAN_DIRECTION=new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //LOAN_DIRECTION.setValue("01");//todo:传入贷款投向
        LOAN_DIRECTION.setValue(circle.getLoanDirection().substring(1, circle.getLoanDirection().length()));//todo:传入贷款投向
        body_struct.addField("LOAN_DIRECTION", LOAN_DIRECTION);

        //贷款归属1
        Field LOAN_BELONG1=new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //LOAN_BELONG1.setValue("10");//todo:传入贷款归属1
        LOAN_BELONG1.setValue(circle.getLoanBelong1());//todo:传入贷款归属1
        body_struct.addField("LOAN_BELONG1", LOAN_BELONG1);

        //贷款归属2
        Field LOAN_BELONG2=new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //LOAN_BELONG2.setValue("2015");//todo:传入贷款归属2
        LOAN_BELONG2.setValue(circle.getLoanBelong2());//todo:传入贷款归属2
        body_struct.addField("LOAN_BELONG2", LOAN_BELONG2);

        //贷款归属3
        Field LOAN_BELONG3=new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //LOAN_BELONG3.setValue("2070");//todo:传入贷款归属3
        LOAN_BELONG3.setValue(circle.getLoanBelong3());//todo:传入贷款归属3
        body_struct.addField("LOAN_BELONG3", LOAN_BELONG3);

        //贷款归属4
        Field LOAN_BELONG4=new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //LOAN_BELONG4.setValue("1030");//todo:传入贷款归属4
        LOAN_BELONG4.setValue(circle.getLoanBelong4());//todo:传入贷款归属4
        body_struct.addField("LOAN_BELONG4", LOAN_BELONG4);

        //业务类型
        Field BUSS_TYPE=new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
        //BUSS_TYPE.setValue("001");//todo:传入业务类型 001 消费类 002 经营类
        BUSS_TYPE.setValue(circle.getBussType());//todo:传入业务类型 001 消费类 002 经营类
        body_struct.addField("BUSS_TYPE", BUSS_TYPE);

        //主管客户经理
        Field CHI_CUST_MANAGER=new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
        //CHI_CUST_MANAGER.setValue("1");//todo:传入主管客户经理
        CHI_CUST_MANAGER.setValue(circle.getChiCustManager());
        body_struct.addField("CHI_CUST_MANAGER", CHI_CUST_MANAGER);


        /*
        客户信息数组 todo:需要在前端补录应在前端补录
         */
        Array CLIENT_ARRAY = new Array(); //CLIENT_NO数组
        CompositeData CusArrayStruct = new CompositeData();

        //客户号
        Field A_CLIENT_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
        //A_CLIENT_NO.setValue("N_016");//todo:传入客户号
        A_CLIENT_NO.setValue(circle.getClientNo());//todo:传入客户号
        CusArrayStruct.addField("CLIENT_NO", A_CLIENT_NO);

        //客户名称
        Field CLIENT_NAME = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
        //CLIENT_NAME.setValue("张三");//todo:传入客户名称
        CLIENT_NAME.setValue(circle.getClientName());//todo:传入客户名称
        CusArrayStruct.addField("CLIENT_NAME", CLIENT_NAME);

        //性别
        Field SEX = new Field(new FieldAttr(FieldType.FIELD_STRING, 2));
        //SEX.setValue("01");//todo:传入性别，男：01 ；女：02； 未知：03
        SEX.setValue(circle.getSex());//todo:传入性别，男：01 ；女：02； 未知：03
        CusArrayStruct.addField("SEX", SEX);

        //客户类型
        Field CLIENT_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 6));
        //CLIENT_TYPE.setValue("Z");//个人客户 todo:文档中多个个人客户码，测试需明确
        CLIENT_TYPE.setValue(circle.getClientType());//个人客户 todo:文档中多个个人客户码，测试需明确
        CusArrayStruct.addField("CLIENT_TYPE", CLIENT_TYPE);

        //证件类型
        Field GLOBAL_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
        GLOBAL_TYPE.setValue("99");//默认值
        //GLOBAL_TYPE.setValue("0");//身份证
        //GLOBAL_TYPE.setValue(circle.getGlobalType());//身份证
        //转换"证件类型" 为 "信贷证件类型"
        String tmp_title = "";
        DictionaryManager dictMgr = Beans.get(DictionaryManager.class);
        Dictionary dictionary = dictMgr.getDictionaryByName("CERT_TYPE");
        List<DictionaryItem> dictItems = dictionary.getItems();
		for(DictionaryItem item : dictItems){
			if(item.getName().equals(circle.getGlobalType())){
				tmp_title = item.getTitle();
				break;
			}
		}
		
		Dictionary dictionary_for_xd = dictMgr.getDictionaryByName("CERT_TYPE_FOR_XD");
		List<DictionaryItem> dictItems_for_xd = dictionary_for_xd.getItems();
		for(DictionaryItem item : dictItems_for_xd){
			if(item.getTitle().equals(tmp_title)){
				GLOBAL_TYPE.setValue(item.getName());
				break;
			}
		}
        CusArrayStruct.addField("GLOBAL_TYPE", GLOBAL_TYPE);

        //证件号码
        Field GLOBAL_ID = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
        //GLOBAL_ID.setValue("320482198601271100");//todo:传入证件号码
        GLOBAL_ID.setValue(circle.getGlobalId());//todo:传入证件号码
        CusArrayStruct.addField("GLOBAL_ID", GLOBAL_ID);

        //长期证件标志 todo:数据字典缺失
        Field LONG_GLOBAL_TYPE = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
        //LONG_GLOBAL_TYPE.setValue("1");//todo:传入标识
        LONG_GLOBAL_TYPE.setValue(circle.getLongGlobalType());//todo:传入标识
        CusArrayStruct.addField("LONG_GLOBAL_TYPE", LONG_GLOBAL_TYPE);

        //签发日期
        Field ISS_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //ISS_DATE.setValue("2015-04-14");//todo:传入数值，无则置空
        ISS_DATE.setValue(formatter10.format(circle.getIssDate()));
        CusArrayStruct.addField("ISS_DATE", ISS_DATE);

        //证件有效日期
        Field GLOBAL_EFF_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //GLOBAL_EFF_DATE.setValue("");//todo:传入数值，无则置空
        GLOBAL_EFF_DATE.setValue(formatter10.format(circle.getGlobalEffDate()));
        CusArrayStruct.addField("GLOBAL_EFF_DATE", GLOBAL_EFF_DATE);

        //农户标识 todo:数据字典缺失
        Field AGRI_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
        //AGRI_FLAG.setValue("1");//todo:传入数值，无则置空
        AGRI_FLAG.setValue(circle.getAgriFlag());
        CusArrayStruct.addField("AGRI_FLAG", AGRI_FLAG);

        //国家代码
        Field COUNTRY_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
        //COUNTRY_CODE.setValue("CHN");//中国
        COUNTRY_CODE.setValue(circle.getCountryCode());//中国
        CusArrayStruct.addField("COUNTRY_CODE", COUNTRY_CODE);

        //民族代码
        Field NATIONALITY_CODE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //NATIONALITY_CODE.setValue("01");//todo:传入民族，例如 “汉”
        NATIONALITY_CODE.setValue(circle.getNationalityCode());//todo:传入民族，例如 “汉”
        CusArrayStruct.addField("NATIONALITY_CODE", NATIONALITY_CODE);

        //户籍所在地
        Field REG_PERM_RESIDENCE = new Field(new FieldAttr(FieldType.FIELD_STRING, 60));
        //REG_PERM_RESIDENCE.setValue("320114");//todo:传入户籍所在地
        REG_PERM_RESIDENCE.setValue(circle.getRegPermResidence());//todo:传入户籍所在地
        CusArrayStruct.addField("REG_PERM_RESIDENCE", REG_PERM_RESIDENCE);

        //地址
        Field ADDRESS = new Field(new FieldAttr(FieldType.FIELD_STRING, 300));
        //ADDRESS.setValue("南京市雨花台");//todo:传入地址
        ADDRESS.setValue(circle.getAddress());//todo:传入地址
        CusArrayStruct.addField("ADDRESS", ADDRESS);

        //出生日期
        Field BIRTH_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //BIRTH_DATE.setValue("1986-01-27");//todo:传入出生日期，格式YYYYMMdd
        BIRTH_DATE.setValue(formatter10.format(circle.getBirthDate()));//todo:传入出生日期，格式YYYYMMdd
        CusArrayStruct.addField("BIRTH_DATE", BIRTH_DATE);

        //最高学历 todo:字典项缺失
        Field EDUCATION = new Field(new FieldAttr(FieldType.FIELD_STRING, 1));
        //EDUCATION.setValue("10");//todo:传入最高学历
        EDUCATION.setValue(circle.getEducation());//todo:传入最高学历
        CusArrayStruct.addField("EDUCATION", EDUCATION);

        //最高学位 todo:字典项缺失
        Field DEGREE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //DEGREE.setValue("2");//todo:传入最高学位
        DEGREE.setValue(circle.getDegree());//todo:传入最高学位
        CusArrayStruct.addField("DEGREE", DEGREE);

        //签约日期
        Field SIGN_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //SIGN_DATE.setValue("2015-04-14");//todo:传入签约日期，格式YYYYMMdd
        SIGN_DATE.setValue(formatter10.format(circle.getSignDate()));//todo:传入签约日期，格式YYYYMMdd
        CusArrayStruct.addField("SIGN_DATE", SIGN_DATE);

        //持卡情况 todo:数据字典项缺失
        Field HOLD_CARD_MSG = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
        //HOLD_CARD_MSG.setValue("1");//todo:传入持卡情况
        HOLD_CARD_MSG.setValue(circle.getHoldCardMsg());//todo:传入持卡情况
        CusArrayStruct.addField("HOLD_CARD_MSG", HOLD_CARD_MSG);

        //是否拥有外国护照或居住权 todo:数据字典项缺失
        Field PASSPORT_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
        //PASSPORT_FLAG.setValue("1");//todo:传入数值
        PASSPORT_FLAG.setValue(circle.getPassportFlag());//todo:传入数值
        CusArrayStruct.addField("PASSPORT_FLAG", PASSPORT_FLAG);

        //信用等级 todo:数据字典项缺失
        Field CREDIT_LEVEL = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //CREDIT_LEVEL.setValue("11");//todo:传入信用等级
        CREDIT_LEVEL.setValue(circle.getCreditLevel());//todo:传入信用等级
        CusArrayStruct.addField("CREDIT_LEVEL", CREDIT_LEVEL);

        //信用到期日期
        Field EXPIRY_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //EXPIRY_DATE.setValue("2016-04-14");//todo:传入到期日期
        if(circle.getExpiryDate() != null){
        	EXPIRY_DATE.setValue(formatter10.format(circle.getExpiryDate()));//todo:传入到期日期
        }
        else{
        	EXPIRY_DATE.setValue("");//todo:传入到期日期
        }
        CusArrayStruct.addField("EXPIRY_DATE", EXPIRY_DATE);

        //是否关联客户 todo:数据字典项缺失
        Field REL_CLIENT_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
        //REL_CLIENT_FLAG.setValue("1");//todo:传入持卡情况
        REL_CLIENT_FLAG.setValue(circle.getRelClientFlag());//todo:传入持卡情况
        CusArrayStruct.addField("REL_CLIENT_FLAG", REL_CLIENT_FLAG);

        //与我行关系 todo:数据字典项缺失
        Field OWN_BRANCH_RELATION = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
        //OWN_BRANCH_RELATION.setValue("B1");//todo:传入我行关系
        OWN_BRANCH_RELATION.setValue(circle.getOwnBranchRelation());//todo:传入我行关系
        CusArrayStruct.addField("OWN_BRANCH_RELATION", OWN_BRANCH_RELATION);

        //我行职务
        Field POST = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
        //POST.setValue("6");//todo:传入我行职务情况
        POST.setValue(circle.getPost());//todo:传入我行职务情况
        CusArrayStruct.addField("POST", POST);

        //贷款卡标识 todo:数据字典项缺失
        Field LOAN_CARD_FLAG = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
        //LOAN_CARD_FLAG.setValue("1");//todo:传入贷款卡标识
        LOAN_CARD_FLAG.setValue(circle.getLoanCardFlag());//todo:传入贷款卡标识
        CusArrayStruct.addField("LOAN_CARD_FLAG", LOAN_CARD_FLAG);

        //贷款卡号
        Field LOAN_CARD_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
        //LOAN_CARD_NO.setValue("111111");//todo:传入贷款卡号
        LOAN_CARD_NO.setValue(circle.getLoanCardNo());//todo:传入贷款卡号
        CusArrayStruct.addField("LOAN_CARD_NO", LOAN_CARD_NO);

        //手机号码
        Field MOBILE = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
        //MOBILE.setValue("15961100228");//todo:传入手机号码
        MOBILE.setValue(circle.getMobile());//todo:传入手机号码
        CusArrayStruct.addField("MOBILE", MOBILE);

        //上级机构 todo:数据字典项缺失
        Field HIGHER_ORG_NO = new Field(new FieldAttr(FieldType.FIELD_STRING, 32));
        //HIGHER_ORG_NO.setValue("12");//todo:传入上级机构
        if(circle.getHigherOrgNo().equals("000000")){
        	HIGHER_ORG_NO.setValue(Constant.QZ_ORG_ROOT_ID);
		}
        else{
        	HIGHER_ORG_NO.setValue(circle.getHigherOrgNo());
        }
        CusArrayStruct.addField("HIGHER_ORG_NO", HIGHER_ORG_NO);

        //客户经理
        Field ACCT_EXEC = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //ACCT_EXEC.setValue("0389");//todo:传入客户经理柜员号
        ACCT_EXEC.setValue(circle.getAcctExec());
        CusArrayStruct.addField("ACCT_EXEC", ACCT_EXEC);

        //开户日期
        Field OPEN_ACCT_DATE = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
        //OPEN_ACCT_DATE.setValue("2015-01-22");//todo:传入开户日期，YYYYMMdd
        OPEN_ACCT_DATE.setValue(formatter10.format(circle.getOpenAcctDate()));//todo:传入开户日期，YYYYMMdd
        CusArrayStruct.addField("OPEN_ACCT_DATE", OPEN_ACCT_DATE);

        //信息加入数组
        CLIENT_ARRAY.addStruct(CusArrayStruct);
        body_struct.addArray("CLIENT_ARRAY",CLIENT_ARRAY);
        /*
        客户信息数组结束
         */

        /*
        费用信息数组开始
         */
        Array FEE_ARRAY = new Array(); //FEE_ARRAY数组
        CompositeData FeeArrayStruct = new CompositeData();

        //币种
        Field FEE_CCY=new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
        //FEE_CCY.setValue("CNY");//人民币
        FEE_CCY.setValue(circle.getFeeCcy());//人民币
        FeeArrayStruct.addField("CCY", FEE_CCY);

        //费用金额
        Field FEE_AMOUNT=new Field(new FieldAttr(FieldType.FIELD_DOUBLE,20,2));
        //FEE_AMOUNT.setValue(1000.00);//todo:传入费用金额
        FEE_AMOUNT.setValue(Double.valueOf(circle.getFeeAmount()));//todo:传入费用金额
        FeeArrayStruct.addField("FEE_AMOUNT", FEE_AMOUNT);

        //账号
        Field ACCT_NO=new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
        //ACCT_NO.setValue("2233");//todo:传入账号
        ACCT_NO.setValue(circle.getAcctNo2());//todo:传入账号 费用账号与放款账号一样 modified by nihc 20150718
        FeeArrayStruct.addField("ACCT_NO", ACCT_NO);

        //信息加入数组
        FEE_ARRAY.addStruct(FeeArrayStruct);
        body_struct.addArray("FEE_ARRAY",FEE_ARRAY);
        /*
        费用信息数组结束
         */

        /*
        账户信息数组开始
        ***！！！该部分内容由核心查询获取后填入！！！***
         */
        Array ACCT_INFO_ARRAY = new Array(); //ACCT_INFO_ARRAY数组
        for(Circle_ACCT_INFO circle_ACCT_INFO1 : acct_info_ls){
        	CompositeData AcctInfoArrayStruct = new CompositeData();
        	//账户性质
            Field ACCT_CHRT=new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
    		//ACCT_CHRT.setValue("02");//todo:传入账户性质
            ACCT_CHRT.setValue(circle_ACCT_INFO1.getAcctChrt());//todo:传入账户性质???
            AcctInfoArrayStruct.addField("ACCT_CHRT", ACCT_CHRT);

            //是否本行
            Field OWN_BRANCH_FLAG=new Field(new FieldAttr(FieldType.FIELD_STRING,1));
    		//OWN_BRANCH_FLAG.setValue("1");//todo:传入是否本行信息
            OWN_BRANCH_FLAG.setValue(circle_ACCT_INFO1.getOwnBranchFlag());//todo:传入是否本行信息???
            AcctInfoArrayStruct.addField("OWN_BRANCH_FLAG", OWN_BRANCH_FLAG);

            //账号
            Field AI_ACCT_NO=new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
    		//AI_ACCT_NO.setValue("1111111");//todo:传入账号（没找到）
            AI_ACCT_NO.setValue(circle_ACCT_INFO1.getAcctNo());//todo:传入账号
            AcctInfoArrayStruct.addField("ACCT_NO", AI_ACCT_NO);

            //户名
            Field ACCT_NAME=new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
            ACCT_NAME.setValue(circle_ACCT_INFO1.getAcctName());//todo:传入户名
            AcctInfoArrayStruct.addField("ACCT_NAME", ACCT_NAME);

            //开户机构
            Field OPEN_ACCT_BRANCH_ID=new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
            OPEN_ACCT_BRANCH_ID.setValue(circle_ACCT_INFO1.getOpenAcctBranchId());//todo:传入户名
            AcctInfoArrayStruct.addField("OPEN_ACCT_BRANCH_ID", OPEN_ACCT_BRANCH_ID);

            //开户行名
            Field OPEN_ACCT_BRANCH_NAME=new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
            OPEN_ACCT_BRANCH_NAME.setValue(circle_ACCT_INFO1.getOpenAcctBranchName());//todo:传入开户行名
            AcctInfoArrayStruct.addField("OPEN_ACCT_BRANCH_NAME", OPEN_ACCT_BRANCH_NAME);

            //支付金额
            Field PAY_AMT=new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20,2));
            PAY_AMT.setValue(Double.valueOf(circle_ACCT_INFO1.getPayAmt()));//todo:传入支付金额???
            AcctInfoArrayStruct.addField("PAY_AMT", PAY_AMT);

            //科目号
            Field GL_CODE=new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
            GL_CODE.setValue(circle_ACCT_INFO1.getGlCode());//todo:传入科目号
            AcctInfoArrayStruct.addField("GL_CODE", GL_CODE);

            //币别
            Field AI_CCY=new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
            AI_CCY.setValue(circle_ACCT_INFO1.getCcy());//todo:传入币别
            AcctInfoArrayStruct.addField("CCY", AI_CCY);

            //全国联行号
            Field C_INTERBANK_ID=new Field(new FieldAttr(FieldType.FIELD_STRING, 15));
            C_INTERBANK_ID.setValue(circle_ACCT_INFO1.getcInterbankId());//todo:传入全国联行号
            AcctInfoArrayStruct.addField("C_INTERBANK_ID", C_INTERBANK_ID);

            //信息加入数组
            ACCT_INFO_ARRAY.addStruct(AcctInfoArrayStruct);
        }
        
        body_struct.addArray("ACCT_INFO_ARRAY",ACCT_INFO_ARRAY);
        
        
        /*
        账户信息数组结束
         */
        cd.addStruct("BODY",body_struct);

        return cd;
    }
    
    /**
     * 返回成功与否
     * @param resp
     * @param circle
     * @return
     * 
     *  <service version="2.0">
			<BODY>
			</BODY>
			<SYS_HEAD>
			<RET_STATUS attr="s,8">S</RET_STATUS>
			<RET attr="array">
				<struct>
					<RET_MSG attr="s,200"></RET_MSG>
					<RET_CODE attr="s,6">000000</RET_CODE>
				</struct>
			</RET>
			<TRAN_TIMESTAMP attr="s,9">81730440</TRAN_TIMESTAMP>
			<TRAN_DATE attr="s,8">20160101</TRAN_DATE>
			<SERVICE_SCENE attr="s,2">10</SERVICE_SCENE>
			<SERVICE_CODE attr="s,30">11002000080</SERVICE_CODE>
			</SYS_HEAD>
		</service>
     */
	public String parseEcifResponse(CompositeData resp,Circle circle) {
		String retMsg = "";
		if(resp == null){
			retMsg = "解析放贷返回信息失败";
			return retMsg;
		}
		CompositeData SYS_HEAD = resp.getStruct("SYS_HEAD");

        //根据数组名称去获取数组
        Array RET = SYS_HEAD.getArray("RET");
        
        String RET_CODE = "";
        String RET_MSG = "";
        if(null != RET && RET.size() > 0){
            int m = RET.size();
            CompositeData array_element = null;
            for (int i = 0; i < m; i++) {
                //数组中的元素也是CompositeData，这是固定的写法。根据游标就可以获取到数组中的所有元素
                array_element = RET.getStruct(i);

                RET_CODE = array_element.getField("RET_CODE").strValue();
                RET_MSG = array_element.getField("RET_MSG").strValue();
            }
        }
        //更新贷款信息表
        circle.setRetCode(RET_CODE);
        circle.setRetMsg("000000".equals(RET_CODE) ? "放款成功" : RET_MSG);
        circle.setRetContno("000000".equals(RET_CODE) ? RET_MSG : "");
        commonDao.updateObject(circle);
        if(RET_CODE.equals( com.cardpay.pccredit.QZBankInterface.constant.Constant.RET_CODE_CIRCLE)){
        	retMsg ="放款成功";
        	return retMsg;
        }
        else{
        	retMsg = RET_MSG;
        	return retMsg;
        }
	}
}
