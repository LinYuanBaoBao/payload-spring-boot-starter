package com.linyuanbaobao.payload;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linyuan - szlinyuan@ininin.com
 * @since 2021/6/7
 */
@Configuration
public class PayloadAutoConfiguration implements InitializingBean {

    @Autowired
    private RequestMappingHandlerAdapter adapter;

    @Override
    public void afterPropertiesSet() {
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>(this.adapter.getReturnValueHandlers());
        for (HandlerMethodReturnValueHandler item : handlers) {
            int index = handlers.indexOf(item);
            if (RequestResponseBodyMethodProcessor.class.isAssignableFrom(item.getClass())) {
                handlers.add(index, new RequestResponseBodyMethodProcessorProxy((RequestResponseBodyMethodProcessor) item));
                break;
            }
        }
        adapter.setReturnValueHandlers(handlers);
    }

}