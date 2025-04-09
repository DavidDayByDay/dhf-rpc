# DHF-RPC
## 介绍

基于 Netty + Nacos + SpringBoot 实现的自定义 RPC 框架。


1. 实现自定义的 RPC 通信协议，自定义编解码器,处理消息边界问题。
2. 实现五种序列化方式：Kryo, Protostuff, Hessian, JDK, JSON。
3. 基于 Netty/Http/Socket 实现三种网络通信方式。
4. 使用 Zookeeper，Nacos 作为注册中心进行服务注册和发现,增加了本地服务缓存和监听，失败重试等机制。
5. 实现三种负载均衡策略：轮询，权重随机，一致性哈希。
6. 集成 SpringBoot，配置了 starter 模块进行自动装配，自定义注解进行服务 Bean 的扫描和注册.
----------------



## 项目结构介绍

<img src="images\rpc-structure.png" alt="项目架构图" style="zoom:67%;" />



## 运行项目

# 使用 Nacos 作为注册中心的 RPC 服务启动流程

## 1. 启动 Nacos 注册中心

首先启动 Nacos 注册中心服务，确保可以通过浏览器访问控制台（默认地址为：http://localhost:8848）。Nacos 将用于服务的注册与发现。

---

## 2. 配置注册中心地址

### 在 Consumer 模块（服务消费者）中：

修改 `application.yml`，添加如下配置：

```yaml
rpc:
  client:
    discoveryUrl: 127.0.0.1:8848
```

### 在 Provider 模块（服务提供者）中：

同样修改 `application.yml`，添加如下配置：

```yaml
rpc:
  server:
    registryAddress: 127.0.0.1:8848
    port: 8888 # 服务提供端口 
    host: 127.0.0.1 # 服务提供地址
```

---

## 3. 启动 Provider 模块（服务提供者）

启动 Provider 模块，即可正常运行 Spring Boot 应用。本项目采用基于 Spring Boot 的自动配置机制，启动后会自动将服务注册到 Nacos，同时相关的 Bean 会自动注入到 Spring 容器中。

---

## 4. 启动 Consumer 模块并发起调用

随后启动 Consumer 模块。通过 Controller 接口访问服务时，客户端会自动从 Nacos 获取服务地址，并完成 RPC 调用。

---
## 压力测试
基于JMH测试

---
1000线程并发调用测试结果

<img src="images\t-1000-benchmark.png" alt="1000线程并发测试结果" style="zoom:67%;" />

2000线程并发调用测试结果

<img src="images\t-2000-benchmark.png" alt="2000线程并发测试结果" style="zoom:67%;" />



