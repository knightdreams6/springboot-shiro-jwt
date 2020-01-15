package com.learn.project.common.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * @author lixiao
 * @date 2019/8/8 16:06
 */
public class SmsUtil {

    private static final String ACCESS_KEY_ID = "";
    private static final String ACCESS_KEY_SECRET = "";
    private static final String SIGN_NAME = "";
    private static final String TEMPLATE_CODE = "";
    private static final String REGION_ID = "";

    public static String sendSms(String phone,Integer code) throws ClientException {

        final String product = "Dysmsapi";
        final String domain = "dysmsapi.aliyuncs.com";
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile(REGION_ID, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        DefaultProfile.addEndpoint(REGION_ID, REGION_ID, product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setVersion("2017-05-25");
        request.putQueryParameter("RegionId", REGION_ID);
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", SIGN_NAME);
        request.putQueryParameter("TemplateCode", TEMPLATE_CODE);
        request.putQueryParameter("TemplateParam", "{code:"+code+"}");
        return acsClient.getAcsResponse(request).getCode();
    }
}
