package test;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cardpay.pccredit.QZBankInterface.client.Client;
import com.cardpay.pccredit.QZBankInterface.service.IESBForCircleCredit;
import com.cardpay.pccredit.QZBankInterface.service.IESBForCredit;
import com.cardpay.pccredit.QZBankInterface.service.IESBForECIF;
import com.cardpay.pccredit.QZBankInterface.service.IESBForED;
import com.cardpay.pccredit.riskControl.service.AccountabilityService;
import com.dc.eai.data.CompositeData;
import com.dcfs.esb.client.ESBClient;
import com.dcfs.esb.config.ClientConfig;


/**
 *
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:spring-junit.xml")
public class IESBTest {
	
	//@Autowired
	//private  AccountabilityService accountabilityService;

	@Test 
	public void test() {
		Client client = new Client();

        //这一步不能少，这是指定密钥文件所在路径(密钥文件不必一定放在工程的src下，也可以放在工程外面) todo:需要秘钥文件
        System.setProperty(ClientConfig.FILEPATH, new File("").getAbsolutePath()+"/src/java/");
        //设置log4j配置文件路径
        PropertyConfigurator.configure(new File("").getAbsolutePath()+"/src/java/log4j.properties");

        /*
         * 1、IESBForECIF 
         */
        //*********************  请求报文格式是ESB自定义的CompositeData   ********************
        //组装一个CompositeData 报文
        //CompositeData req = IESBForECIF.createEcifRequest();
        CompositeData req = IESBForED.createEDRequest();
        //发送请求报文并获得相应报文
        CompositeData resp = client.sendMess(req);
        // 解析ESB响应的cd报文获得需要的信息
        //IESBForECIF.parseEcifResponse(resp);
        String retCode = IESBForED.parseCoreResponse(req);
        System.out.println("返回码：" + retCode);
        
        
        /*
         * 2、IESBForCredit 
         */
        //*********************  请求报文格式是ESB自定义的CompositeData   ********************
        //组装一个CompositeData 报文
        //CompositeData req = IESBForCredit.createCommonCreditRequest();
        //发送请求报文并获得相应报文
        //CompositeData resp = client.sendMess(req);
        // 解析ESB响应的cd报文获得需要的信息
        //IESBForCredit.parseCreditResponse(resp);
        
        /*
         * 3、IESBForCircleCredit
         */
        //*********************  请求报文格式是ESB自定义的CompositeData   ********************
        //组装一个CompositeData 报文
        //CompositeData req = IESBForCircleCredit.createCircleCreditRequest();
        //发送请求报文并获得相应报文
        //CompositeData resp = client.sendMess(req);
        // 解析ESB响应的cd报文获得需要的信息
        //IESBForCircleCredit.parseCircleCreditResponse(resp);
        
        /*
         * 4、IESBForCore
         */
        //*********************  请求报文格式是ESB自定义的CompositeData   ********************
        //组装一个CompositeData 报文
       // CompositeData req = IESBForCore.createCircleCreditRequest();
        //发送请求报文并获得相应报文
       // CompositeData resp = client.sendMess(req);
        // 解析ESB响应的cd报文获得需要的信息
       // IESBForCore.parseCoreResponse(resp);
	}

}
