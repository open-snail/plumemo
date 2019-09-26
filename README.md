# hello-blog
hello-blog后端代码

# 简介
[Hello Blog](http://helloblog.byteblogs.com/) 是一个前后端分离的博客系统，为了解除开发人员对后端的束缚，真正做到的一个面向接口开发的博客系统。
他是基于[SpringBoot](https://spring.io/projects/spring-boot/)实现零配置让系统的配置更简单，使用了[Mybatis-Plus](https://mp.baomidou.com/)快速开发框架，在不是复杂的查询操作下，无需写sql就可以快速完成接口编写。
后台管理系统使用了vue中流行的[vue-element-admin](https://panjiachen.github.io/vue-element-admin-site/#/)，另外前后交互使用了[JWT](https://jwt.io/)作为令牌，进行权限、登录校验。
## 安装
### 首先是环境配置

::: tip 环境配置
 1. nginx 安装 
 2. jdk 1.8 + 安装
 3. mysql数据库 5.6 +
 
:::


### 后端java系统安装

```

# 克隆项目
git clone https://github.com/byteblogs168/hello-blog.git

# 配置数据库
application.yml (数据库账号密码)、新建hello_blog的数据库 字符集 utf8mb4 

# 启动运行
main方法运行 com.byteblogs.HelloBlogApplication

```
