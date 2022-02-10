package com.linyuanbaobao.payload;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
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

    RequestResponseBodyMethodProcessorProxy(RequestResponseBodyMethodProcessor delegate) {
        this.delegate = delegate;
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
        result.put("success", true);
        result.put("code", HttpStatus.OK);
        result.put("data", o);
        result.put("timestamp", System.currentTimeMillis());
        delegate.handleReturnValue(result, methodParameter, modelAndViewContainer, nativeWebRequest);
    }

}