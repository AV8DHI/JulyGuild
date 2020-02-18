# JulyGuild（V2.0.0）

> 本贴使用 [ MCBBS Markdown To BBCode Converter](https://mm2bc.ustc-zzzz.net/) 生成。

## 插件特点

* 完全开源
  使用 `GNU General Public License v3.0` 协议。

* GUI支持
  
  几乎所有的操作都在GUI内进行。
  
* 高自定义性
  * 消息自定义
    你可以自定义大部分的消息。
  * GUI自定义
    你可以自由设置GUI中物品的位置和材质等。

* 多版本支持
  理论支持 `1.7.10` 及以上的所有版本。

## 功能

### 创建公会

提供了两种方式创建公会：

* 金币
* 点券

### 加入公会

### 公会信息

### 个人信息

### 传送到公会主城

### 公会排行榜

可以使用自定义公式来制定排行榜。

### 入会审批

### 成员管理

你可以对成员进行以下操作：

* 踢出
* 给予权限
  * 踢人权限
  * 开关成员PVP权限
  * 入会审批权限
  * 使用公会商店权限
  * 使用图标仓库权限

### 捐赠

可以使用金币或点券捐赠公会币给公会，公会币可以在公会商店消费。

### 公会商店

#### 设置公会主城

可以使用公会币来设置公会主城，玩家可以在 “我的公会” 里传送到主城。

#### 公会升级

可以使用公会币来升级公会最大人数。

#### 公会图标

可以使用公会币来购买公会图标。

#### 全员集结令

可以使用公会币请求玩家传送到你所在的位置。

### 图标仓库

玩家从公会商店买的公会图标都会存入图标仓库，可以随意切换当前展示的图标。

## 命令

* `/jguild gui main`
  打开主界面。
  
* `/jguild plugin reload`
  
  重载插件配置。
  
  需要权限：`JulyGuild.admin`。
  
* `/jguild plugin version`
  显示插件信息。

## PlaceholderAPI变量

* `%guild_name%`

  公会名。

* `%guild_member_position%`

  职位：成员，主人。

* `%guild_member_donated_gmoney%`
  成员已赞助的公会币数量。

* `%guild_member_join_time%`
  成员加入时间。

* `%guild_ranking%`
  公会排名。

* `%guild_owner%`
  公会主人。

* `%guild_member_count%`
  公会成员数量。

* `%guild_max_member_count%`
  公会最大成员数量。

* `%guild_creation_time%`
  公会创建时间。

* `%guild_bank_gmoney%`

  公会币储备。

* `%guild_online_member_count%`
  公会在线成员数。

## 图片（仅展示部分功能）

[![3FqyEn.gif](https://s2.ax1x.com/2020/02/18/3FqyEn.gif)](https://imgchr.com/i/3FqyEn)

![3FXJE9.gif](https://s2.ax1x.com/2020/02/18/3FXJE9.gif)

![3FqTER.gif](https://s2.ax1x.com/2020/02/18/3FqTER.gif)

[![3FLoQS.gif](https://s2.ax1x.com/2020/02/18/3FLoQS.gif)](https://imgchr.com/i/3FLoQS)

![3FO7p6.gif](https://s2.ax1x.com/2020/02/18/3FO7p6.gif)

## 配置文件

插件含较多的配置文件，这里仅介绍每个文件的用途，每个文件都已经包含了详细的注释。

文件（目录）列表：

* `conf.yml`
  主配置文件，包含了所有可自定义的参数。

* `lang.yml`
  语言文件。

* `gui/MainGUI.yml`
  主GUI文件。

* `gui/GuildMineGUI.yml`
  我的公会GUI文件。

* `gui/GuildCreateGUI.yml`

  公会创建GUI文件。

* `gui/GuildDonateGUI.yml`

  公会赞助GUI文件。

* `gui/GuildIconRepositoryGUI.yml`
  公会图标仓库GUI文件。

* `gui/GuildInfoGUI.yml`
  公会信息GUI文件。

* `gui/GuildJoinCheckGUI.yml`
  公会成员审批GUI文件。

* `gui/GuildMemberListGUI.yml`
  公会成员列表GUI文件。

* `gui/GuildMemberManageGUI.yml`
  公会成员管理GUI文件。

* `shops`
  公会商店文件夹。

## Github

[点击传送](https://github.com/julyss2019/JulyGuild/)

## 下载

插件交流/BUG反馈群：786184610。

下载链接：[hide]w[/hide]