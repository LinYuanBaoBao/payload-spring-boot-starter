package com.linyuanbaobao.payload.config;

import com.linyuanbaobao.payload.annotation.Payload;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.HashMap;
import java.util.Map;


/**
 * @author linyuan - szlinyuan@ininin.com
 * @since 2021/6/7
 */
public class RequestResponseBodyMethodProcessorProxy implements HandlerMethodReturnValueHandler {

    private RequestResponseBodyMethodProcessor delegate;

    private Map<String, String> payloadMap;

    private PayloadProperties payloadProperties;

    RequestResponseBodyMethodProcessorProxy(RequestResponseBodyMethodProcessor delegate,
                                            PayloadProperties payloadProperties,
                                            Map<String, String> payloadMap) {
        this.delegate = delegate;
        this.payloadProperties = payloadProperties;
        this.payloadMap = payloadMap;
    }

    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        if (!delegate.supportsReturnType(methodParameter)) {
            return false;
        }

        if (AnnotationUtils.findAnnotation(methodParameter.getMethod(), Payload.class) != null) {
            return true;
        } else {
            Class<?> clazz = methodParameter.getContainingClass();
            return AnnotationUtils.findAnnotation(clazz, Payload.class) != null;
        }
    }

    @Override
    public void handleReturnValue(Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put(payloadMap.get(PayloadProperties.ATTRIBUTE_SUCCESS), true);
        result.put(payloadMap.get(PayloadProperties.ATTRIBUTE_CODE), payloadProperties.getCode());
        result.put(payloadMap.get(PayloadProperties.ATTRIBUTE_MESSAGE), "success");
        result.put(payloadMap.get(PayloadProperties.ATTRIBUTE_DATA), o);
        result.put(payloadMap.get(PayloadProperties.ATTRIBUTE_TIMESTAMP), System.currentTimeMillis());
        delegate.handleReturnValue(result, methodParameter, modelAndViewContainer, nativeWebRequest);
    }

}