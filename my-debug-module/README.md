# Spring Session 源码阅读调试模块

这个模块是专门为阅读Spring Session源码而创建的调试和测试环境。

## 文件说明

### SpringSessionDebugDemo.java
- 主要的演示程序，展示了Spring Session的核心概念
- 可以直接运行，观察Spring Session的基本功能
- 包含Session创建、属性设置、保存、读取和过期等基本操作

### SimpleSessionTest.java
- 单元测试类，验证Spring Session的基本功能
- 包含三个测试方法：
  - `testCreateAndRetrieveSession()` - 测试Session的创建和读取
  - `testSessionExpiration()` - 测试Session过期机制
  - `testSessionAttributes()` - 测试Session属性操作

## 如何运行

### 运行演示程序
```bash
cd my-debug-module
../gradlew run
```

### 运行测试
```bash
cd my-debug-module
../gradlew test
```

### 编译项目
```bash
cd my-debug-module
../gradlew build
```

## 源码阅读建议

1. **从核心接口开始**：
   - `Session` - 表示一个HTTP会话
   - `SessionRepository` - 管理Session的存储
   - `SessionIdResolver` - 解析Session ID

2. **查看实现类**：
   - `MapSessionRepository` - 基于内存的Session存储
   - `CookieHttpSessionStrategy` - 基于Cookie的Session策略

3. **理解关键概念**：
   - Session的创建和销毁
   - Session属性的存储
   - Session过期机制
   - Session ID的生成和解析

## 调试技巧

- 在关键方法处设置断点
- 使用IDE的"Step Into"功能深入源码
- 观察Session对象的状态变化
- 理解Spring Session与Servlet Session的关系


