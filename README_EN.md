![mybatis-log-plugin](https://img.shields.io/jetbrains/plugin/v/13905-mybatis-log-plugin?label=version&style=flat-square)
[![mybatis-log-plugin](https://img.shields.io/jetbrains/plugin/d/13905-mybatis-log-plugin?style=flat-square)](https://plugins.jetbrains.com/plugin/13905-mybatis-log-plugin/versions)

### [中文](https://github.com/kookob/mybatis-log-plugin/blob/master/README.md)  

# MyBatis Log Plugin
## Features
- Restore mybatis sql log to original whole sql.
- It will generate executable sql statements with replace ? to the really param value.
- Select the console sql log and right-click "Restore Sql" menu to restore sql.
- Navigate to each other between Java method and Mapper xml.

![](https://plugins.jetbrains.com/files/13905/25-page/image1.png)

## Button Features
- **Text**: Restore sql from text
- **Settings**: Setup filter rules and navigation switch
- **Format**: Output beautiful formatted sql statements
- **Rerun**: Rerun plugin
- **Stop**: Stop plugin

## Example
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
MyBatis Log Plugin output executable sql statements:
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

## Manual
https://plugins.jetbrains.com/plugin/13905-mybatis-log-plugin/manual

## Download
[mybatis-log-plugin.jar](https://plugins.jetbrains.com/plugin/13905-mybatis-log-plugin/versions)  

## Price
`$5/year`

## Plugins
* [MyBatis Log Plugin](https://plugins.jetbrains.com/plugin/13905-mybatis-log-plugin) 
* [Smart Jump](https://plugins.jetbrains.com/plugin/14053-smart-jump) 
* [Smart Search](https://plugins.jetbrains.com/plugin/14615-smart-search)
* [Toolset](https://plugins.jetbrains.com/plugin/14384-toolset) 
