## Release Notes

### 1.1.0-RELEASE

- 组件重构，以支持新版本 SpringBoot

### 1.0.3-RELEASE

- 新增 **DynamicCode** 接口，异常可该接口来动态返回 code 值，优先级高于 **@BizErrorResponseStatus** 注解

### 1.0.2-RELEASE

- 修复全局异常类仅能处理 `Accept = application/json` 问题

### 1.0.1-RELEASE

- 修复钉钉告警消息无法获取应用名（spring.application.name）问题

- 支持自定义异常信息获取方式

- 支持自定义异常响应点 http-status 状态码

- 修复与其它 HandlerMethodReturnValueHandler 冲突问题

### 1.0.0-RELEASE

- 组件初始化