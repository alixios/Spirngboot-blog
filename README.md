

# 一.个人博客简介


## 1.5 功能介绍：

&ensp;本博客简单实现了博客展示、后台管理、发布博客还有评论等功能，其中后台管理、发布博客和评论功能要在用户登录后才可使用，而后台管理的某些功能普通用户只有查看的权限，并没有分配增删改的权限。


# 二.前端开发：

## 2.1 简介：


&ensp;采用了vue.js，前端框架采用了semantic-ui和element-ui，此外还有一些关于页面动态和渲染的js和css类似(animate.css,pricsm等)。此外，需要说明的是，本人后端狗一枚，页面样式是基于网上部分模板样式的修改，其余开发是独立完成的。



# 三.后端开发：

## 3.1 简介：


> - 大致框架采用了SpringBoot+MybatisPlus+SpringCloud(Eureka)+ElasticSearch完成的，用redis做缓存中间件，采用微服务的架构。
> - 安全方面采用了SpringSecurity和BCEncrypt
> - 用了jwt来请求访问接口
> - 由于服务器内存和配置的原因，服务器只上线了四个模块

> 项目是由四个模块组成的，
> 
> - blog-common: 博客服务端的实体类
> - blog-gateway: 博客的服务代理类（从前端接收请求，网关RSA解密后转发给服务端接口）
> - blog-eureka: 微服务注册中心server
> - blog-server: 主体服务端

## 3.3 开发中遇到的一些问题：

### 3.3.1 关于jwt与zuul

&ensp;本人使用自定义注解@LoginRequired来对某些类或者接口进行jwt验证，但是在一开始加入网关微服务的时候，发现后端用了jwt验证的接口一直访问不通过。在浏览器看，发的请求的请求头明明都带上了token，这是一开始百思不得其解的地方之一。

> &ensp;后来才得知，原来是在网关转发前端的请求后，再把请求转发给后端服务器时，请求头中的token丢失，于是只能在网关filter里面，**在转发请求给后端前，手动的把token加到头部。**

