package com.linyuanbaobao.payload.support.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author linyuan - szlinyuan@ininin.com
 * @since 2021/6/8
 */
public class ExceptionMsgDTO {

    private String serviceName;
    private String api;
    private Date date;
    private String declaringClass;
    private String methodName;
    private Integer lineNumber;
    private String msg;

    public String getServiceName() {
        return serviceName;
    }

    public ExceptionMsgDTO setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getApi() {
        return api;
    }

    public ExceptionMsgDTO setApi(String api) {
        this.api = api;
        return this;
    }

    public String getDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public ExceptionMsgDTO setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ExceptionMsgDTO setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getDeclaringClass() {
        return declaringClass;
    }

    public ExceptionMsgDTO setDeclaringClass(String declaringClass) {
        this.declaringClass = declaringClass;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public ExceptionMsgDTO setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public ExceptionMsgDTO setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("服务名称：").append(getServiceName()).append("\n")
                .append("报错日期：").append(getDate()).append("\n")
                .append("接口：").append(getApi()).append("\n")
                .append("类路径：").append(getDeclaringClass()).append("\n")
                .append("异常方法：").append(getMethodName()).append("\n")
                .append("错误行数：").append(getLineNumber()).append("\n")
                .append("异常信息：").append(getMsg())
                .toString();
    }
}
