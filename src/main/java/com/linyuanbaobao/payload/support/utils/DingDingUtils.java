package com.linyuanbaobao.payload.support.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;

/**
 * @author linyuan - szlinyuan@ininin.com
 * @since 2021/6/8
 */
public class DingDingUtils {

    /**
     * 获取钉钉机器人签名
     *
     * @param timestamp 时间戳
     * @param secret    密钥
     * @return 签名 Sign
     */
    public static String getRobotSign(Long timestamp, String secret) throws Exception {
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        return URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
    }

    /**
     * 获取钉钉机器人请求 URL
     *
     * @param webhook   钩子-URL
     * @param timestamp 时间戳
     * @param sign      签名
     * @return 钉钉机器人请求 URL
     */
    public static String getRobotUrl(String webhook, Long timestamp, String sign) {
        return webhook + "&timestamp=" + timestamp + "&sign=" + sign;
    }

}
