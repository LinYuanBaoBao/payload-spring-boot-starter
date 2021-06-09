package com.linyuanbaobao.payload.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author linyuan - szlinyuan@ininin.com
 * @since 2021/6/8
 */
@ConfigurationProperties(prefix = "spring.mvc.payload")
public class PayloadProperties {

    public static final String ATTRIBUTE_CODE = "code";
    public static final String ATTRIBUTE_SUCCESS = "success";
    public static final String ATTRIBUTE_MESSAGE = "message";
    public static final String ATTRIBUTE_DATA = "data";
    public static final String ATTRIBUTE_TIMESTAMP = "timestamp";
    public static final String ATTRIBUTE_PATH = "path";
    public static final String ATTRIBUTE_STACK = "stack";

    @Bean(name = "payloadMap")
    private Map<String, String> initPayloadMap() {
        return this.payloadMap;
    }

    public PayloadProperties() {
        payloadMap.put(ATTRIBUTE_CODE, ATTRIBUTE_CODE);
        payloadMap.put(ATTRIBUTE_SUCCESS, ATTRIBUTE_SUCCESS);
        payloadMap.put(ATTRIBUTE_MESSAGE, ATTRIBUTE_MESSAGE);
        payloadMap.put(ATTRIBUTE_DATA, ATTRIBUTE_DATA);
        payloadMap.put(ATTRIBUTE_TIMESTAMP, ATTRIBUTE_TIMESTAMP);
        payloadMap.put(ATTRIBUTE_PATH, ATTRIBUTE_PATH);
        payloadMap.put(ATTRIBUTE_STACK, ATTRIBUTE_STACK);
    }

    /**
     * 开启统一异常结果
     */
    private boolean errorEnabled = false;

    /**
     * 成功响应码
     */
    private Integer code = HttpStatus.OK.value();

    /**
     * 异常堆栈信息
     */
    private boolean enableTrace = false;

    /**
     * 钉钉机器人配置
     */
    private DingDingRobotConfig dingDingRobotConfig = new DingDingRobotConfig();

    /**
     * 自定义响应属性
     */
    private Map<String, String> payloadMap = new HashMap<>();

    public static class DingDingRobotConfig {
        /**
         * 启用钉钉机器人
         */
        private boolean enable = false;
        /**
         * Webhook
         */
        private String webhook;
        /**
         * 加密-密钥
         */
        private String secret;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getWebhook() {
            return webhook;
        }

        public void setWebhook(String webhook) {
            this.webhook = webhook;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public boolean isEnableTrace() {
        return enableTrace;
    }

    public void setEnableTrace(boolean enableTrace) {
        this.enableTrace = enableTrace;
    }

    public Map<String, String> getPayloadMap() {
        return payloadMap;
    }

    public void setPayloadMap(Map<String, String> payloadMap) {
        this.payloadMap = payloadMap;
    }

    public boolean isErrorEnabled() {
        return errorEnabled;
    }

    public void setErrorEnabled(boolean errorEnabled) {
        this.errorEnabled = errorEnabled;
    }

    public DingDingRobotConfig getDingDingRobotConfig() {
        return dingDingRobotConfig;
    }

    public void setDingDingRobotConfig(DingDingRobotConfig dingDingRobotConfig) {
        this.dingDingRobotConfig = dingDingRobotConfig;
    }
}
