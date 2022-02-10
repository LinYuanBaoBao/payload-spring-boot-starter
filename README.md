# payload-spring-boot-starter

一个 SpringBoot 统一响应体/异常处理组件。

## 信息
基于 **spring-boot-starter-web:2.6.3** 构建

by：林同学（765371578@qq.com）

## Getting Started

引入依赖：

```xml
<dependency>
    <groupId>com.github.LinYuanBaoBao</groupId>
    <artifactId>payload-spring-boot-starter</artifactId>
    <version>1.0.4-RELEASE</version>
</dependency>
```

Controller 上使用 **@Payload** 注解，自动对返回的对象进行包装：

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

响应结果如下：
```json
{
  "code": 200,
  "data": {
    "k1": "v1",
    "k2": "v2"
  },
  "success": true,
  "timestamp": 1623055152059
}
```

## 业务异常

开启业务异常处理：
```yml
spring:
  mvc:
    payload:
      biz-error-enabled: true
```

抛出业务异常 **BizException**：

```java
throw new BizException("业务异常",9999);    
```

此时 http-status 状态码为 400，响应结果如下：
```json
{
    "success": false,
    "message": "业务异常",
    "code": 9999,
    "timestamp": 1623055152059
}
```

或者，你也可以继承 **GlobalExceptionHandler** 类自定义异常处理逻辑
