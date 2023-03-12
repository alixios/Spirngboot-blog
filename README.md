

# 一.个人博客简介


## 1.5 功能介绍：

&ensp;本博客简单实现了博客展示、后台管理、发布博客还有评论等功能，其中后台管理、发布博客和评论功能要在用户登录后才可使用，而后台管理的某些功能普通用户只有查看的权限，并没有分配增删改的权限。


# 二.前端开发：

## 2.1 简介：

&ensp;***https://github.com/asiL-tcefreP/blog-vue***（前端源码地址）
&ensp;采用了vue.js，前端框架采用了semantic-ui和element-ui，此外还有一些关于页面动态和渲染的js和css类似(animate.css,pricsm等)。此外，需要说明的是，本人后端狗一枚，页面样式是基于网上部分模板样式的修改，其余开发是独立完成的。



# 三.后端开发：

## 3.1 简介：

&ensp;***https://github.com/asiL-tcefreP/blog***（后端源码地址）

> - 大致框架采用了SpringBoot+MybatisPlus+SpringCloud(Eureka)+ElasticSearch完成的，用redis做缓存中间件，采用微服务的架构。
> - 安全方面采用了SpringSecurity和BCEncrypt
> - 用了jwt来请求访问接口
> - 利用RSA算法对前端发送的重要参数进行加密，经过网关解密后把参数发送到后端服务器。
> - 由于服务器内存和配置的原因，服务器只上线了四个模块
>   ![在这里插入图片描述](https://img-blog.csdnimg.cn/20210321201208846.png)
>   ![在这里插入图片描述](https://img-blog.csdnimg.cn/20210520155833969.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0RsaWhjdGNlZnJlcA==,size_16,color_FFFFFF,t_70)

> 项目是由八个模块组成的，
> 
> - blog-common: 博客服务端的实体类
> - blog-encrypt: 博客的服务代理类（从前端接收请求，网关RSA解密后转发给服务端接口）
> - blog-eureka: 微服务注册中心server
> - blog-server: 主体服务端
> - blog-extension: 拓展服务端（留言和友链功能），上线的版本集成了blog-search-api模块，因为阿里云服务器内存太小了
> - **blog-search-api:** **ElasticSearch的服务端，分出一个模块是为了更清晰的展现微服务架构，但是服务器内存太小，所以集成在上述模块中，自己开发可以直接使用本模块**
> - blog-article-crawler：爬虫和人工智能模块，用的webmagic框架爬取数据，deeplearning4j做文本分类
> - blog-ai：里面的服务类调用了py脚本来实现古诗词生成

## 3.3 开发中遇到的一些问题：

### 3.3.1 关于jwt与zuul

&ensp;本人使用自定义注解@LoginRequired来对某些类或者接口进行jwt验证，但是在一开始加入网关微服务的时候，发现后端用了jwt验证的接口一直访问不通过。在浏览器看，发的请求的请求头明明都带上了token，这是一开始百思不得其解的地方之一。

> &ensp;后来才得知，原来是在网关转发前端的请求后，再把请求转发给后端服务器时，请求头中的token丢失，于是只能在网关filter里面，**在转发请求给后端前，手动的把token加到头部。**

