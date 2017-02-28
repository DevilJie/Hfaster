Hfaster  
=
## Hfaster-demo 是针对Hfaster整理的一个demo示例 (展示如何使用Hfaster)
 Hfaster 是基于mybatis进行二次封装的持久层开源框架，引入面向对象的概念，使数据库表结构与java实体类能够紧密结合
 其核心设计目标是开发迅速、代码量少、功能强大、易上手、轻量级，无需xml配置即可完成基本的数据库操作，为您节省更多的时间


## Hfaster有如下几个特点
* 实体对象与数据库表结构完美转换，提升代码可阅读性以及操作性

* 相对于mybatis，没有多余的xml文件配置，只需要简单的数据连接池的配置即可

* 可以自由使用sql，并且支持多种数据库（目前支持：MySQL，Oracle）

* 框架提供内置的查询适配器，无需编写任何sql语句，即可实现数据的查询功能

* 框架轻量级仅仅只有63kb

* 框架还在不断的完善升级中，致力于国人最便捷，最简单，最好用，功能最实用的持久层框架<br/>

##Hfaster框架需要spring4+以及mybatis的支持<br/>

##如何使用<br/>
  * 首先编写实体类,实体类需要标上注解@Table
  ``` java
  package com.daniuwl.hsj.entity;

import hsj.czy.mybatisframe.annotation.Table;

/**
 * 用户实体类 类UserInfo.java的实现描述：TODO 类实现描述
 * 
 * @author Administrator 2017年2月7日 下午3:44:19
 */
@Table
public class UserInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String            userName;             // 用户名
    private String            password;             // 用户密码

    private String            mobile;               // 手机号
    private String            name;                 // 用户姓名

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

  ```
  此实体类对应表名称默认为：user_info，也可以手动指定表名称例如：
  ``` java
    @Table(table_name="userinfo")
  ```
  注意，每一个实体类都需要继承父类
  ``` java
    ChBatisEntity
  ```
  * 实体类编写完成过后，需要编写对应的Dao类以及Service类，<br/>
  Dao类需要继承Hfaster提供的Dao基类`BaseDao`以及`BaseDaoImpl`,<br/>
  Service类需要继承Hfaster提供的Service基类`BaseService`以及`BaseServiceImpl`,<br/>
  
  UserInfo Dao 接口类：
  ``` java
    package com.daniuwl.hsj.dao;

    import hsj.czy.mybatisframe.service.BaseDao;

    import com.daniuwl.hsj.entity.UserInfo;

    public interface UserInfoDao extends BaseDao<UserInfo, String> {

    }
  ```
  UserInfo Dao 接口实现类
  ``` java
    package com.daniuwl.hsj.dao.impl;

    import hsj.czy.mybatisframe.service.impl.BaseDaoImpl;

    import org.springframework.stereotype.Repository;

    import com.daniuwl.hsj.dao.UserInfoDao;
    import com.daniuwl.hsj.entity.UserInfo;

    @Repository
    public class UserInfoDaoImpl extends BaseDaoImpl<UserInfo, String> implements UserInfoDao {
    }
  ```
  UserInfo Service 接口类
  ``` java
    package com.daniuwl.hsj.service;

    import hsj.czy.mybatisframe.exception.MyBatistFrameServiceException;
    import hsj.czy.mybatisframe.service.BaseService;

    import com.daniuwl.hsj.entity.UserInfo;

    public interface UserInfoService extends BaseService<UserInfo, String> {

        /**
         * 登录方法
         * 
         * @param userName 用户名
         * @param password 密码
         * @return
         * @throws MyBatistFrameServiceException
         */
        public UserInfo login(String userName, String password) throws MyBatistFrameServiceException;
    }

  ```
  
  UserInfo Service 接口实现类
  ``` java
    package com.daniuwl.hsj.service.impl;

    import hsj.czy.mybatisframe.exception.MyBatistFrameServiceException;
    import hsj.czy.mybatisframe.service.impl.BaseServiceImpl;

    import org.springframework.stereotype.Service;

    import com.daniuwl.hsj.dao.UserInfoDao;
    import com.daniuwl.hsj.entity.UserInfo;
    import com.daniuwl.hsj.service.UserInfoService;

    @Service
    public class UserInfoServiceImpl extends BaseServiceImpl<UserInfo, String> implements UserInfoService {

        // 除了最基本的增删改查业务逻辑，也可以根据自己需要，进行扩展
        // 例如：用户登录,用户登录需要接收用户名以及密码参数，
        // 然后根据根据入参来判断当前登录信息是否可用

        @Override
        public UserInfo login(String userName, String password) throws MyBatistFrameServiceException {
            // 首先根据用户名获取用户信息
            UserInfo u = ((UserInfoDao) baseDao).findByColumn("userName", userName);
            if (u == null) throw new MyBatistFrameServiceException("用户不存在"); // 如果用户不存在，上抛异常处理
            if (u.getPassword().equals(password)) {
                return u;
            } else {
                throw new MyBatistFrameServiceException("用户名密码错误"); // 如果用户密码错误，上抛异常处理
            }
        }
    }

  ```
  
  * 实体类编写完成配置数据库连接池xml文件
``` java
 <?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xmlns="http://www.springframework.org/schema/beans" 
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop-2.5.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context-2.5.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx-2.5.xsd"
       default-lazy-init="true">

	<aop:config />
	
	<!-- 设置Spring Bean 的扫描包路径 -->
	<context:component-scan base-package="com.daniuwl.hsj" />
	
	<!-- 此处使用的是c3p0连接池 -->
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource" destroy-method="close">
		<property name="driverClass" value="com.mysql.jdbc.Driver" />
		<property name="jdbcUrl" value="数据库连接串" />
		<property name="user" value="用户名" />
		<property name="password" value="密码" />
		<property name="initialPoolSize" value="10" />
		<property name="minPoolSize" value="10" />
		<property name="maxPoolSize" value="50" />
		<property name="maxIdleTime" value="7200" />
		<property name="acquireIncrement" value="5" />
		<property name="checkoutTimeout" value="10000" />
		<property name="maxIdleTimeExcessConnections" value="10" />
	</bean>
    
    <!-- 事务管理器 -->
	<bean id="transactionManager"
		class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource" />
	</bean>
	
	<tx:annotation-driven transaction-manager="transactionManager" />
	
	<!-- 将Hfaster框架提供的SpringBean管理工具实例化 -->
	<bean id="springUtil" class="hsj.czy.mybatisframe.util.SpringUtil" lazy-init="false" />
	<!-- session工厂 -->
	<!-- 设置数据库工厂为MySQL工厂 注：需要设置MySQL数据库忽略大小写 -->
	<bean id="sampleSqlSessionFactory" class="hsj.czy.mybatisframe.core.ChBatisSpringMySqlFactoryBean">
		<property name="dataSource" ref="dataSource" />
		<!-- 设置底层表结构 对应实体类目录 -->
		<property name="table_scan" value="com.daniuwl.hsj.entity"></property>
		<!-- 数据表分隔符 例如属性名为 userId 则对应数据库字段为：user_id -->
		<property name="separator" value="_"></property>
	</bean>

	<!-- mybatis mapper扫描器 -->
	<bean class="hsj.czy.mybatisframe.configurer.ChBatisMapperScannerConfigurer">
		<property name="sqlSessionFactory" ref="sampleSqlSessionFactory"></property>
	</bean>
	
</beans>
```

##Hfaster框架的具体使用方式，请看  [Hfaster-demo](https://github.com/DevilJie/Hfaster-demo "悬停显示") 示例

##Hfaster-框架的发展，离不开大家的支持，你们的支持，是我最大的动力！

##Hfaster-框架还在不断的完善升级中，致力于国人最便捷，最简单，最好用，功能最实用的持久层框架！完全开源哦！

##Hfaster-框架交流群欢迎您的加入,QQ群号：`28280375`