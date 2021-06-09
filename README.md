# payload-spring-boot-starter

一个 SpringBoot 统一响应体/异常处理组件。

## 信息
基于 **Spring Boot：2.3.8.RELEASE** 构建

by：林同学（765371578@qq.com）

## Getting Started

引入依赖：

```xml
<dependency>
    <groupId>com.github.LinYuanBaoBao</groupId>
    <artifactId>payload-spring-boot-starter</artifactId>
    <version>1.0.0-RELEASE</version>
</dependency>
```

Controller 上使用 **@Payload** 注解，自动对返回的数据进行包装。

 ```java
@Payload
@RequestMapping("/users")
@RestController
public class MyController {

    @GetMapping("/{id}")
    public User get(@PathVariable Integer id) {
      return user;
    }

}

```
响应结果格式如下：
```json
{
  "code": 200,
  "data": {
     "k1": "v1",
     "k2": "v2"
  },
  "success": true,
  "message": "success",
  "timestamp": 1623055152059
}
```
     
### 配置
见配置文件中 **spring.mvc.payload** 属性的自动提示
```yaml
spring:
  mvc:
    payload:
      code: 200              # 成功状态码，默认：200
      errorEnabled: false    # 统一错误处理
      enableTrace: false     # 打印堆栈信息
      payload-map:
        code: code
        success: success
        message: message
        data: data
        timestamp: timestamp
        stack: stack
        path: path
      ding-ding-robot-config:
        enable: false         # 是否启用钉钉告警机器人
        webhook: https://...
        secret: SEC...
```

### 业务异常  @BizErrorResponseStatus

继承 RuntimeException 类，并加上 **@BizErrorResponseStatus(400)** 注解，值为自定义的错误码，异常消息参数 `message` 也会一同响应返回。

```java
@BizErrorResponseStatus(400)
public class CustomerException extends RuntimeException {
   public CustomerException(String message){
        super(message);
   }
}
```

响应结果格式如下：
```json
{
    "success": false,
    "message": "message",
    "code": 400,
    "timestamp": 1623055152059,
    "path": "/app"
}
```

### 代码异常告警（钉钉机器人）  
若启用了钉钉机器人告警，当异常（未使用 @BizErrorResponseStatus 注解）产生到响应状态码（code） >= 500 时，会触发告警，内容如下：
```
服务名称：${spring.application.name}
报错日期：2021-06-09 18:16:07
接口：/xx
类路径：com.example.demo.controller.TestController
异常方法：xx
错误行数：29
异常信息：禁用账号失败
```
