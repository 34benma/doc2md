A tool to parse doc file(office word file) to markdown file.

Free use and free fork...

Please don't mind my poor english...Thank you!!!
请忽略本人蹩脚的英语,谢谢！
<br />
---
#User Manual
##用户使用手册
---

[TOC]

##欢迎使用本软件
如果你是一个文（dou）艺（bi）青年，喜欢在[简书](http://www.jianshu.com)上写写心情，激扬文字？
或者你是一个技（zhai）术（nan）爱（fu）好（nv）者，喜欢在[CSDN](http://blog.csdn.net/?ref=toolbar_logo)上发表技术专题？
但是你却天生懒惰，不能记住MarkDown繁琐的语法和排版。
那么，本软件可能非常适合你。
<br />

本软件是我用业余时间完成的第一个完整的面向普通用户的Java程序。
主要功能就是将普通的Word文档（目前只支持docx格式）一键批量转换为MarkDown文件。（什么是markdown文件？请看http://zh.wikipedia.org/wiki/Markdown）

####本软件目前实现：

+ 自动识别文档中各级标题
+ 自动处理表格
+ 抽取文档中的超链接
+ 抽取文档中的图片

####本软件不能完成：

+ 自动处理超链接
+ 自动识别加粗斜体
+ 自动处理图片位置
+ 支持所有MarkDown拓展语法

####为何不能实现？

曾经我天真地以为POI无所不能，只要读取到了word文档，就能获取所有信息。但是...
我没想到的是POI虽然到了3.11，但是对于docx的支持是如此弱。它的强项在于生成office文档
而不在于处理office文档。

因此，目前很多功能不是我不想实现，而是局限于目前POI文档处理的能力，很多功能目前无法做到...

因此，可能在处理文字超链接，图片等内容时本软件显得力不从心。但是，我已经尽我最大努力方便你的处理：

对于图片，我从word文档中抽取出来，放在指定目录；但是由于插入文档中的图片格式无法获取准确的文件名，因此文件
命名可能不太符合你的期望；在文档最后，我把文中用到的所有图片列表在文末，因此还需要你手动操作，按照语法插入图片。

对于文字超链接，因为无法定位到位置，我只能抽取文字，超链接必须单独抽取，因此不得不将二者分离。我将所有的文字链接
抽取在文档最后，需要你手动按照语法插入链接。

对于文档中嵌套的其他文档对象，我暂时没有处理（尽管技术上可以实现），因为Markdown目前不支持插入文件。

**注意：**

*限制：单个对象长度不能超过65533，由于本身MarkDown语法需要一定的字符，*
*因此单个段落或表格对象最大长度最好不要超过5K*
*这里的对象包括：段落字符长度 表格长度*


##如何安装本软件

####下载本软件

方式一：使用Git下载
git clone https://github.com/34benma/doc2md.git

方式二：手动下载压缩包

本页面右中间有一个DownLoadZip 按钮，点击保存到本地；


##如何运行本软件

#### 目录结构说明：

.ieda 本软件开发工具为IntelliJ IDEA 14.0.2，因此会有一个.idea隐藏文件夹（如果你不参与开发，可以删掉该文件夹）

doc  本软件开发过程中文档


##注意事项
##如何维护和提交Bug



Author:JackWang
Contact Me: wantedonline@outlook.com
