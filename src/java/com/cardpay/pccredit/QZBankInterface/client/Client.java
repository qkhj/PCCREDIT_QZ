package com.cardpay.pccredit.QZBankInterface.client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Log4JCategoryLog;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.dc.eai.data.CompositeData;
import com.dc.log.LogConfig;
import com.dc.log.LogControl;
import com.dcfs.esb.client.ESBClient;
import com.dcfs.esb.client.exception.TimeoutException;
import com.dcfs.esb.config.ClientConfig;
import com.dcfs.esb.pack.standardxml.PackUtil;
import com.jcraft.jsch.Logger;
import com.cardpay.pccredit.QZBankInterface.service.IESBForECIF;

/**
 * 这是发送报文的客户端
 * Created by johhny on 15/4/14.
 */

@Service
public class Client implements InitializingBean{
	private static Log log = LogFactory.getLog(Client.class);
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		//这一步不能少，这是指定密钥文件所在路径(密钥文件不必一定放在工程的src下，也可以放在工程外面) todo:需要秘钥文件
        //System.setProperty(ClientConfig.FILEPATH, new File("").getAbsolutePath()+"/src/java/");
		//tomcat 可动态获取
		//String path = this.getClass().getResource("/").toString();
		//System.setProperty(ClientConfig.FILEPATH,path.substring(6, path.length()));
		//jboss weblogic直接写死路径
		//测试环境
		//System.setProperty(ClientConfig.FILEPATH,"/usr/cardpay/esb/");
		//生产环境
		System.setProperty(ClientConfig.FILEPATH,"/home/pccredit/pccredit_esb/");
        //设置log4j配置文件路径
        PropertyConfigurator.configure("/home/pccredit/pccredit_log/log4j.properties");
	}

    /**
     * 调用api发送请求报文，并返回响应报文
     * @param req 请求报文
     * @return		esb响应的报文
     */
    public CompositeData sendMess(CompositeData req){

        //响应报文
        CompositeData resp = null;
        try {
            resp = ESBClient.request(req);
        } catch (TimeoutException e) {
            if(log.isErrorEnabled()){
                log.error("发送报文异常["+e+"]");
            }
        }
        System.out.println("响应数据为["+resp+"]");
        return resp;
    }
    /**
     * 调用api发送请求报文，并返回响应报文
     * @param req 请求报文
     * @return		esb响应的报文
     */
    public byte[] sendMess(byte[] req){

        //响应报文
        byte[] resp = null;
        try {
            resp = ESBClient.request(req);
        } catch (TimeoutException e) {
            if(log.isErrorEnabled()){
                log.error("发送报文异常["+e+"]");
            }
        }
        return resp;
    }

    /**
     * 读取文件内容
     * @param fileName	文件绝对路径
     * @return		文件内容
     */
    public byte[] readContent(String fileName){
        byte[] value = null;
        try {
            InputStream input = new FileInputStream(fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] cache = new byte[100];
            int len = -1;
            while ((len = input.read(cache)) != -1) {
                baos.write(cache, 0, len);
            }
            value = baos.toByteArray();
            input.close();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*//todo:发送报文功能加入框架中，以下为发送ECIF
    public static void main(String[] args) {
        Client client = new Client();

		//这一步不能少，这是指定密钥文件所在路径(密钥文件不必一定放在工程的src下，也可以放在工程外面)
		System.setProperty(ClientConfig.FILEPATH, new File("").getAbsolutePath()+"/src/java/");
		//设置log4j配置文件路径
		PropertyConfigurator.configure(new File("").getAbsolutePath()+"/src/java/log4j.properties");

        //*********************  请求报文格式是ESB自定义的CompositeData   ********************
        //组装一个CompositeData 报文
        CompositeData req = IESBForECIF.createEcifRequest();
        //发送请求报文并获得相应报文
        CompositeData resp = client.sendMess(req);
        // 解析ESB响应的cd报文获得需要的信息
        IESBForECIF.parseEcifResponse(resp);


    }*/
}
