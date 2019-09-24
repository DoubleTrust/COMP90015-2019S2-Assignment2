# COMP90015-2019S2-Assignment2
 墨尔本大学2019年S2分布式系统项目文件

## 程序运行方法

to be added... （需等待小组确认逻辑）

## 版本迭代

### 2019/09/24

- 新增Client_Connection GUI, 用于方便用户输入hostname, port和username
- ClientGUI新增显示用户名（无实时更新），新增“Create WhiteBoard”和“Open WhiteBoard”按钮  <u>(需小组商讨设计框架)</u>
- 更改对应的RMI函数
- 新增“退出确认”监听行为，用于后台删除用户数据
- 修复部分BUG（扩大whiteboard，但不完美）

### 2019/09/23

- GUI更正：将server GUI 修改为Client GUI，原server输入模式更改为命令行输入

- Client新增用户名输入（计划用于RMI搜集用户信息并展现）。

- RMI新增两个远程函数：openCanvas() 与disposeCanvas()

  （注： 原canvas关闭时仅修改visibility； 若需删除，点击“Disconnect”）

- Canvas更新

  - 新增保存、另存为、绘画预设形状等功能
  - 修复原版本的BUG，更新GUI

### 2019/09/21

- 删除RMI中的Socket等无效代码，增添部分注释
- 增加Canvas（未修改版本），测试客户端远程调用Canvas功能

### 2019/09/20

 - 添加RMI框架

## RMI 代码说明[2019-09-23]

 项目有3个package，分别为client，remote和server。

- client：此package用于实现client的连接以及调用RMI的相关接口。（包含易UI界面，计划用于聊天窗口或显示玩家名）
- remote：此package用于编写RMI接口，以便于client调用。
- server：此package用于实现server、RMI的创建以及RMI接口的实现。
