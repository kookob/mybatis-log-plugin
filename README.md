MyBatis Log Plugin
==============================
![MyBatisLogPlugin.gif](https://raw.githubusercontent.com/kookob/mybatis-log-plugin/master/snapshot/MyBatisLogPlugin.gif)

**English Introduction**
---
This is a plugin for IntelliJ IDEA that restore the mybatis generate sql to original whole sql.<br/>
It will generate sql statements with replace ? to the really param value.<br/>
Through the "Tools -> MyBatis Log Plugin" menu you can tail the sql log.<br/>
You can selected the "Filter" button on the left to filter the contents don't wanna display.<br/>
You can selected the "Format Sql" button on the left to format the generate sql statements.<br/>
**Prerequisite: sql log must contain "Preparing:" and "Parameters:"**<br/>

The left buttons function:<br/>

* Sql Text: Restore sql from text
* Filter: Filter setting
* Rerun: Rerun this plugin
* Stop: Stop output the sql log
* Format Sql: Format the **subsequent** sql statements
* Close: Close this plugin window

**Support Format**
---
Support the mybatis's output format below:<br/>


`2016-11-11 16:46:29.316 DEBUG selectSql1 -  ==>  Preparing: select * from t_table where name = ?`
`2016-11-11 16:46:29.343 DEBUG selectSql1 -  ==> Parameters: hello(String)`

Use "Preparing:" and "Parameters:" characters to split the log sql.<br/>
And it will output the whole sql:<br/>
`select * from t_table where name = 'hello';`<br/>

**Download Plugin**
---
[mybatis-log-plugin.jar](https://plugins.jetbrains.com/plugin/10065-mybatis-log-plugin "Download Plugin")<br/>

**Donations are welcomed**
---
[PayPal](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=2FQY2FH24H4LC&item_name=MyBatis+Log+Plugin&currency_code=USD&source=url "Donate via PayPal")<br/>


---


**中文介绍**
---
这是一个Intellij的插件，主要作用是把mybatis生成的PreparedStatement语句恢复成原始完整的sql语句。<br/>
它将用真实的参数值替换PreparedStatement语句的问号占位符。<br/>
通过 "Tools -> MyBatis Log Plugin" 这个菜单可以实时输出sql日志。<br/>
点击窗口左边的 "Filter" 按钮，可以过滤不想要输出的sql语句。<br/>
点击窗口左边的 "Format Sql" 按钮，可以格式化输出的sql语句。<br/>
**前提条件：输出的sql日志必须包含"Preparing:"和"Parameters:"才能正常解析。**<br/>

左边几个按钮的作用：<br/>

* Sql Text: 从文本还原语句
* Filter: 过滤语句配置
* Rerun: 重新启动
* Stop: 停止输出
* Format Sql: 格式化**后续**输出的Sql语句
* Close: 关闭该窗口

**支持格式**
---
支持mybatis的输出格式如下：<br/>

`2016-11-11 16:46:29.316 DEBUG selectSql1 -  ==>  Preparing: select * from t_table where name = ?`
`2016-11-11 16:46:29.343 DEBUG selectSql1 -  ==> Parameters: hello(String)`

以 "Preparing:" 和 "Parameters:" 作为分割符进行解析。<br/>
接着输出的完成sql语句如下：<br/>
`select * from t_table where name = 'hello';`<br/>

**插件下载**
---
[mybatis-log-plugin.jar](https://plugins.jetbrains.com/plugin/10065-mybatis-log-plugin "插件下载")

**项目地址**
---
<https://github.com/kookob/mybatis-log-plugin>

**参考列表**
---
Reference and copy some code from below list:<br/>
<https://github.com/JetBrains/intellij-community> <br/>
<https://github.com/krasa/GrepConsole><br/>
<https://github.com/hibernate/hibernate-orm><br/>

**欢迎捐赠**
---
前段时间，因为家人的一场大病，发现多年赚的钱可能都不够医院几天花的，实在无奈。<br/>
加上维护插件花费的一些精力，综合考虑了一些原因，最终还是开启了捐赠通道（迫于生计，内心纠结）。<br/>
如果您觉得我的插件对您有所帮助，能提升您的一点效率，不妨小额地支持一下我，谢谢！<br/>
当然当然，没有捐赠的任何用户，都请不要有任何的心里负担或压力。<br/>
最后，依然非常感谢您的使用和支持！<br/>

![支付宝](https://raw.githubusercontent.com/kookob/mybatis-log-plugin/01b528df60df5cc990b87803e6c0c6ffae19f34c/src/mybatis/log/icon/alipay.png)
![微信](https://raw.githubusercontent.com/kookob/mybatis-log-plugin/01b528df60df5cc990b87803e6c0c6ffae19f34c/src/mybatis/log/icon/wechat.png)