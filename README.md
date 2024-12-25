# 选课管理系统 (xkxt)

## 系统简介

`xkxt` 是一个基于 Spring Boot 和 Vue.js 的选课管理系统，旨在为学校提供一个便捷的在线平台，用于管理课程、教师、学生以及选课信息。系统支持管理员、教师和学生三种角色，分别有不同的权限和功能。

### 系统角色

- **管理员**：可以查看和管理所有模块信息。
- **教师**：可以查看学院和专业信息，但只能查看；可以查看自己的课程信息和选课情况。
- **学生**：可以查看学院和专业信息，对自己的学院课程进行选课和取消选课。

### 功能模块

- **管理员信息**：用户名、密码、姓名、角色
- **教师信息**：用户名、密码、姓名、性别、职称、所属专业、角色
- **学生信息**：用户名、密码、姓名、性别、学号、总学分、所属学院、角色
- **学院信息**：学院名称、学院介绍、最低学分
- **专业信息**：专业名称、系名、所属学院
- **课程信息**：课程名称、介绍、学分、所属学院、上课教师、开办人数、上课时段、上课地点、已选人数（备选过的课程不允许删除）
- **选课信息**：课程名称、授课教师、选课学生（选课后可以取消选课，学生对应的学分会自动计算，课程的已选人数也会自动计算）
- **系统公告**：由管理员维护，教师和学生可以查看
- **登录注册、修改密码、个人信息管理、退出登录**

## 技术栈

- **后端**：Spring Boot, MyBatis, Spring MVC
- **前端**：Vue 2, Element UI
- **数据库**：MySQL
- **前后端分离**
- **编辑器**：IntelliJ IDEA

## 下载与运行

### 克隆仓库

首先，克隆 `xkxt` 项目的远程仓库到本地：

```bash
git clone https://github.com/Aosaki-255128000/xkxt.git
cd xkxt
```

### 配置环境

#### 后端配置

1. **安装依赖**：

   ```bash
   cd backend
   mvn clean install
   ```

2. **配置数据库**：

   - 创建一个新的 MySQL 数据库。

   - 修改 `src/main/resources/application.yml` 文件中的数据库连接信息：

     ```yaml
     spring:
       datasource:
         url: jdbc:mysql://localhost:3306/your_database_name?useSSL=false&serverTimezone=UTC
         username: your_username
         password: your_password
     ```

3. **启动后端服务**：

   ```bash
   mvn spring-boot:run
   ```

#### 前端配置

1. **安装依赖**：

   ```bash
   cd frontend
   npm install
   ```

2. **启动前端开发服务器**：

   ```bash
   npm run serve
   ```

### 运行项目

- **后端服务** 将在 `http://localhost:9090` 上运行。
- **前端开发服务器** 将在 `http://localhost:8080` 上运行。

确保前后端服务都成功启动后，你可以通过浏览器访问 `http://localhost:8081` 来使用选课管理系统。

## 贡献指南

欢迎任何对 `xkxt` 项目的贡献！如果你想要参与，请遵循以下步骤：

1. **Fork** 仓库到你自己的 GitHub 账户。

2. **Clone** 你的 Fork 到本地机器。

3. 创建一个新的分支来实现你的功能或修复问题：

   ```bash
   git checkout -b feature-your-feature-name
   ```

4. 提交你的更改并推送至你的 Fork。

5. 在原始仓库中发起一个 **Pull Request**，详细描述你的更改。

## 问题反馈

如果你在使用 `xkxt` 时遇到任何问题或有任何建议，请通过 [GitHub Issues](https://github.com/Aosaki-255128000/xkxt/issues) 提交问题。

## 许可证

`xkxt` 项目遵循 [MIT License](LICENSE)，请参阅许可证文件以了解更多信息。
