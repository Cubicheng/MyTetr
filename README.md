## 概要

本程序是一款基于 FXGL，javafx，netty 开发的类 [Tetr.io](https://tetris.huijiwiki.com/wiki/TETR.IO) 的支持双人局域网联机的俄罗斯方块对战游戏，采用 [SRS 超级旋转系统](https://tetris.huijiwiki.com/wiki/%E8%B6%85%E7%BA%A7%E6%97%8B%E8%BD%AC%E7%B3%BB%E7%BB%9F) ，攻击方式和受击方式参考了 [Tetr.io 的攻击方式](https://tetris.wiki/TETR.IO) （但是不支持 [Back-To-Back](https://tetris.huijiwiki.com/wiki/%E8%83%8C%E9%9D%A0%E8%83%8C) ），支持在设置中自定义参数，并保存在本地。



## 1 程序的运行环境、安装步骤

1. 运行环境：JDK 21

2. 程序的组成部分：文件夹 config，MyTetr.jar

3. 安装步骤：

​	(1) 安装 JDK 21

​	(2) 下载文件夹 MyTetr 到电脑上

​	(3) 打开命令提示符窗口，输入以下命令：java -jar MyTetr.jar 

​		  或 在资源管理器中双击 MyTetr.jar，即可运行程序

> 原本是试图使用 exe4j 或者是手写 bat 命令来实现在无 jdk 环境下，仅依赖 jre 文件夹来运行的，但是都会报错：Java.lang.NoClassDefFoundError: sun/misc/Unsafe



## 2 程序开发平台

1. 代码行数：3,390 行（不含空行）
2. 开发环境：IntelliJ 2024 + JDK 21



## 3 程序功能说明

主菜单界面

![image-20241226185347839](C:\Users\26303\AppData\Roaming\Typora\typora-user-images\image-20241226185347839.png)

### (1) 单人模式，自由练习

![image-20241226190342459](C:\Users\26303\AppData\Roaming\Typora\typora-user-images\image-20241226190342459.png)

### (2) 多人模式

![image-20241226191035673](C:\Users\26303\AppData\Roaming\Typora\typora-user-images\image-20241226191035673.png)

![image-20241226190953753](C:\Users\26303\AppData\Roaming\Typora\typora-user-images\image-20241226190953753.png)

1 Server 端

​			点击创建房间

![image-20241226191102806](C:\Users\26303\AppData\Roaming\Typora\typora-user-images\image-20241226191102806.png)

​			玩家2 进入后，点击开始即可开始。

![image-20241226191202679](C:\Users\26303\AppData\Roaming\Typora\typora-user-images\image-20241226191202679.png)

2 Client 端

输入房间 ip 后点击加入房间即可

![image-20241226191035673](C:\Users\26303\AppData\Roaming\Typora\typora-user-images\image-20241226191035673.png)

进入房间后等待 玩家1 开始。

![image-20241226193125046](C:\Users\26303\AppData\Roaming\Typora\typora-user-images\image-20241226193125046.png)

### (3) 设置

![image-20241226191655450](C:\Users\26303\AppData\Roaming\Typora\typora-user-images\image-20241226191655450.png)

退出时会询问“是否保存修改”。

## 4 程序算法说明及面向对象实现技术方案

顶层是通过 Application 作为入口点，通过 GameApp 来管理游戏场景。

![image-20241226191930677](C:\Users\26303\AppData\Roaming\Typora\typora-user-images\image-20241226191930677.png)

gameScenes 包中是所有出现的场景。

![image-20241226192031710](C:\Users\26303\AppData\Roaming\Typora\typora-user-images\image-20241226192031710.png)

gameWorld 包是完成游戏主逻辑的，其中 PieceComponent 有一个基类是 OnePieceComponent ，所有的 PieceComponent 都继承自它。

![image-20241226192150907](C:\Users\26303\AppData\Roaming\Typora\typora-user-images\image-20241226192150907.png)

netWork 包负责所有与网络通讯相关的功能。

![image-20241226192350365](C:\Users\26303\AppData\Roaming\Typora\typora-user-images\image-20241226192350365.png)

## 5 技术亮点、关键点及其解决方案

- 技术亮点：使用 FXGL、javafx、netty 开发，性能优秀。

- 健壮性展示：

	无网络连接时：

	![image-20241226193008737](C:\Users\26303\AppData\Roaming\Typora\typora-user-images\image-20241226193008737.png)

	无法连接服务器时：

	![image-20241226193034787](C:\Users\26303\AppData\Roaming\Typora\typora-user-images\image-20241226193034787.png)

	有玩家对战中退出对战，但还保持网络通讯时：

![image-20241226193152785](C:\Users\26303\AppData\Roaming\Typora\typora-user-images\image-20241226193152785.png)

有玩家对战中断开了与对方的网络通讯连接时：

![image-20241226193428921](C:\Users\26303\AppData\Roaming\Typora\typora-user-images\image-20241226193428921.png)

- 本程序的技术关键点：

	采用按需刷新的网络更新策略，对网络带宽要求小。采用 FXGL 的 Entity-Component 模式来设计，结构清晰。

- 遇到的技术难点及对应的解决方案：

	项目初期，发现 FXGL 不支持多场景的游戏（如一个单人模式，一个多人模式），经过寻找发现了一个基于 FXGL 和 javafx 的拓展库 fxcity，支持多场景的切换，后续均使用 fxcity 作为底层。

	

## 6 小结

FXGL 虽然是 java 中游戏引擎的领头羊，但是感觉还是有许多不足，比如没有多场景界面，Entity 和 Component 生命周期管理不清，因此不具备开发大型项目的潜质。网络上所有关于 FXGL 的项目均是小游戏性质的，以后应该不会再去碰 FXGL 了。相比之下，可能单纯使用 javafx 会更方便一些。

关于 Netty 方面，这是第一次接触网络通讯相关的编程，也是一路摸爬过来了，功能是搞定了，但是代码方面规范性欠缺，隔离性也欠佳。
