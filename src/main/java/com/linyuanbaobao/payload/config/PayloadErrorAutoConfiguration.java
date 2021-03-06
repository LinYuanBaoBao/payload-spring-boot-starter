package com.linyuanbaobao.payload.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linyuanbaobao.payload.annotation.BizErrorResponseStatus;
import com.linyuanbaobao.payload.support.model.DingDingRobotMsgDTO;
import com.linyuanbaobao.payload.support.model.ExceptionMsgDTO;
import com.linyuanbaobao.payload.support.utils.DingDingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * @author linyuan - szlinyuan@ininin.com
 * @since 2021/6/7
 */
@Configuration
@Primary
@Slf4j
@ConditionalOnProperty(prefix = "spring.mvc.payload", name = "error-enabled", havingValue = "true")
public class PayloadErrorAutoConfiguration extends DefaultErrorAttributes {

    @Value("${spring.application.name:}")
    private String applicationName;

    private ObjectMapper objectMapper;

    private PayloadProperties payloadProperties;

    private RestTemplate restTemplate;

    private Map<String, String> payloadMap;

    @Autowired
    public PayloadErrorAutoConfiguration(PayloadProperties payloadProperties,
                                         Map<String, String> payloadMap) {
        super();
        this.payloadProperties = payloadProperties;
        this.restTemplate = new RestTemplate();
        this.payloadMap = payloadMap;
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        options = options.including(ErrorAttributeOptions.Include.STACK_TRACE);
        Map<String, Object> attributes = super.getErrorAttributes(webRequest, options);
        if (webRequest.getHeader("Accept").matches(".*text/html.*")) {
            return attributes;
        }

        int status = Integer.parseInt(attributes.get("status").toString());
        Throwable error = getError(webRequest);
        long timestamp = System.currentTimeMillis();
        int code = getCode(status, error);

        Map<String, Object> resultAttributes = new LinkedHashMap<>();
        resultAttributes.put(payloadMap.get(PayloadProperties.ATTRIBUTE_SUCCESS), false);
        resultAttributes.put(payloadMap.get(PayloadProperties.ATTRIBUTE_PATH), attributes.get("path"));
        resultAttributes.put(payloadMap.get(PayloadProperties.ATTRIBUTE_TIMESTAMP), timestamp);
        resultAttributes.put(payloadMap.get(PayloadProperties.ATTRIBUTE_CODE), code);
        resultAttributes.put(payloadMap.get(PayloadProperties.ATTRIBUTE_MESSAGE), getMessage(attributes.get("error").toString(), error));
        if (payloadProperties.isEnableTrace()) {
            resultAttributes.put(payloadMap.get(PayloadProperties.ATTRIBUTE_STACK), attributes.get("trace"));
        }

        if (payloadProperties.getDingDingRobotConfig().isEnable()
                && code >= HttpStatus.INTERNAL_SERVER_ERROR.value()
                && error != null
                && getBizErrorAnnotation(error) == null) {
            StackTraceElement stackTraceElement = error.getStackTrace()[0];
            ExceptionMsgDTO exceptionMsgDTO = new ExceptionMsgDTO()
                    .setDate(new Date(timestamp))
                    .setApi(attributes.get("path").toString())
                    .setMsg(error.getMessage())
                    .setDeclaringClass(stackTraceElement.getClassName())
                    .setMethodName(stackTraceElement.getMethodName())
                    .setLineNumber(stackTraceElement.getLineNumber())
                    .setServiceName(applicationName);
            sendDingDingErrorAlarm(exceptionMsgDTO.toString());
        }
        return resultAttributes;
    }

    private Integer getCode(int status, Throwable error) {
        if (status >= HttpStatus.BAD_REQUEST.value() && status < HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            BizErrorResponseStatus annotation = getBizErrorAnnotation(error);
            return (annotation != null) ? annotation.value() : status;
        }
        return status;
    }

    private BizErrorResponseStatus getBizErrorAnnotation(Throwable error) {
        return (error != null) ? AnnotationUtils.findAnnotation(error.getClass(), BizErrorResponseStatus.class) : null;
    }

    private String getMessage(String errorMsg, Throwable error) {
        if (error instanceof MethodArgumentNotValidException) {
            return getMessage(((MethodArgumentNotValidException) error).getBindingResult().getAllErrors());
        } else if (error instanceof BindException) {
            return getMessage(((BindException) error).getAllErrors());
        } else if (error instanceof ConversionFailedException) {
            return error.getCause().getMessage();
        }
        return errorMsg;
    }

    private String getMessage(List<ObjectError> error) {
        return error.stream()
                .filter(it -> !StringUtils.isEmpty(it.getDefaultMessage()))
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .get();
    }

    public void sendDingDingErrorAlarm(String content) {
        PayloadProperties.DingDingRobotConfig dingConfig = payloadProperties.getDingDingRobotConfig();
        String secret = dingConfig.getSecret();
        String webhook = dingConfig.getWebhook();
        if (StringUtils.isEmpty(webhook) || StringUtils.isEmpty(secret)) {
            return;
        }

        String url = "";
        try {
            Long timestamp = System.currentTimeMillis();
            String sign = DingDingUtils.getRobotSign(timestamp, secret);
            url = DingDingUtils.getRobotUrl(webhook, timestamp, sign);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<DingDingRobotMsgDTO> httpEntity = new HttpEntity<>(new DingDingRobotMsgDTO(content), headers);

            restTemplate.postForEntity(url, httpEntity, String.class);
        } catch (Exception e) {
            log.debug("???????????????????????????-URL???{},???????????????{}", url, e.getMessage());
        }
    }


}
