![mybatis-log-plugin](https://img.shields.io/jetbrains/plugin/v/13905-mybatis-log-plugin?label=version&style=flat-square)
[![mybatis-log-plugin](https://img.shields.io/jetbrains/plugin/d/13905-mybatis-log-plugin?style=flat-square)](https://plugins.jetbrains.com/plugin/13905-mybatis-log-plugin/versions)
![](https://visitor-badge.glitch.me/badge?page_id=mybatis-log-plugin)

### [English](https://github.com/kookob/mybatis-log-plugin/blob/master/README_EN.md)  

# MyBatis Log Plugin
## 插件功能
- 还原`MyBatis`输出的日志为完整的`SQL`语句。
- 把`SQL`日志里面的`?`替换为真正的参数值。
- 选中要还原的`MyBatis`日志，右键点击菜单`Restore Sql`，还原`SQL`语句.
- `Java`接口方法与`Mapper xml`文件互相跳转。

![](https://plugins.jetbrains.com/files/13905/25-page/image1.png)

## 按钮作用
- **Text**: 从文本内容还原`SQL`语句
- **Settings**: 导航跳转开关，配置不想要输出的`SQL`语句
- **Format**: 输出格式化过的`SQL`语句
- **Rerun**: 重启插件
- **Stop**: 停止插件

## 日志示例
```sql
MyBatis Log Test: DEBUG sql1 -  ==>  Preparing: select * from t_table where name = ?
MyBatis Log Test: DEBUG sql1 -  ==> Parameters: hello(String)
MyBatis Log Test: INFO sql2 -  ==>  Preparing: update t_table set name = ? where id = ?
MyBatis Log Test: INFO sql2 -  ==> Parameters: world(String), 123(Integer)
MyBatis Log Test: WARN sql3 -  ==>  Preparing: delete from t_table where id = ?
MyBatis Log Test: WARN sql3 -  ==> Parameters: 123(Integer)
MyBatis Log Test: ERROR sql4 - ==>  Preparing: select * from t_table order by id asc 
MyBatis Log Test: ERROR sql4 - ==>  Parameters: 
```
插件输出的完整的可执行的`SQL`语句如下：
```sql
--  1  MyBatis Log Test: DEBUG sql1 -  ==>
 select *
 FROM t_table
 WHERE name = 'hello';
------------------------------------------------------------
--  2  MyBatis Log Test: INFO sql2 -  ==>
 update t_table set name = 'world'
 WHERE id = 123;
------------------------------------------------------------
--  3  MyBatis Log Test: WARN sql3 -  ==>
 delete
 FROM t_table
 WHERE id = 123;
------------------------------------------------------------
--  4  MyBatis Log Test: ERROR sql4 - ==>
 select *
 FROM t_table order by id asc;
```

## 安装下载
[mybatis-log-plugin.jar](https://plugins.jetbrains.com/plugin/13905-mybatis-log-plugin/versions)  

## 价格
`$5/year`  

## 插件文档
https://www.yuque.com/kookob/plugin  
文档里面包含插件介绍，使用手册，购买流程，激活失败等说明。

## 其他插件
* [MyBatis Log Plugin](https://plugins.jetbrains.com/plugin/13905-mybatis-log-plugin) 
* [Smart Jump](https://plugins.jetbrains.com/plugin/14053-smart-jump) 
* [Smart Search](https://plugins.jetbrains.com/plugin/14615-smart-search)
* [Toolset](https://plugins.jetbrains.com/plugin/14384-toolset) 
