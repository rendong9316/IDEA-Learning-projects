# 斗地主游戏 - Java Swing 实现

## 项目说明

这是一个完整的、可运行的 Java 斗地主桌面游戏，基于 Java Swing 开发，无需任何外部依赖，可直接在 IntelliJ IDEA 中运行。

## 功能特性

- ✅ 完整发牌功能
- ✅ 叫地主流程（支持叫1/2/3分）
- ✅ 抢地主流程
- ✅ 多种出牌类型：
  - 单张
  - 对子
  - 三张
  - 顺子（5张以上连续）
  - 连续对子（3对以上）
  - 三带一
  - 炸弹（四张相同）
  - 王炸（大小王）
- ✅ 智能AI出牌
- ✅ 游戏胜负判断
- ✅ 图形化界面

## 项目结构

```
doudizhu/
├── src/
│   └── com/doudizhu/
│       ├── main/
│       │   └── DoudizhuMain.java          # 主程序入口
│       ├── card/
│       │   ├── Card.java                  # 卡牌实体类
│       │   └── Deck.java                  # 牌堆管理（洗牌、发牌）
│       ├── player/
│       │   ├── Player.java                # 玩家抽象类
│       │   └── AIPlayer.java             # AI玩家
│       ├── game/
│       │   ├── GameLogic.java             # 游戏规则逻辑
│       │   └── GameController.java        # 游戏控制器
│       ├── ui/
│       │   ├── MainWindow.java           # 主窗口
│       │   ├── GamePanel.java           # 游戏面板
│       │   ├── CardPanel.java           # 卡牌显示面板
│       │   └── UIConstants.java         # UI常量
│       └── util/
│           └── UIConstants.java          # 工具类（已在ui包）
├── README.md
└── project_info.txt
```

## 如何在 IDEA 中创建和运行

### 步骤1：创建新项目

1. 打开 IntelliJ IDEA
2. 点击 `File` → `New` → `Project...`
3. 选择 `Java`（如果没有JDK，先配置JDK）
4. 设置项目名称为 `doudizhu`
5. 点击 `Create`

### 步骤2：创建包结构

1. 在 `src` 文件夹上右键
2. 选择 `New` → `Package`
3. 输入 `com.doudizhu`
4. 在 `com.doudizhu` 包上右键，创建子包：
   - `com.doudizhu.main`
   - `com.doudizhu.card`
   - `com.doudizhu.player`
   - `com.doudizhu.game`
   - `com.doudizhu.ui`
   - `com.doudizhu.util`

### 步骤3：复制代码

将代码文件复制到对应的包中：
- `DoudizhuMain.java` → `com.doudizhu.main`
- `Card.java`, `Deck.java` → `com.doudizhu.card`
- `Player.java`, `AIPlayer.java` → `com.doudizhu.player`
- `GameLogic.java`, `GameController.java` → `com.doudizhu.game`
- `MainWindow.java`, `GamePanel.java`, `CardPanel.java`, `UIConstants.java` → `com.doudizhu.ui`

### 步骤4：运行

1. 找到 `DoudizhuMain.java`
2. 在文件上右键，选择 `Run 'DoudizhuMain.main()'`
3. 游戏窗口将打开

## 类说明

| 类名 | 所在包 | 说明 |
|------|--------|------|
| `DoudizhuMain` | main | 程序入口，启动游戏主窗口 |
| `Card` | card | 卡牌实体，包含花色和点数 |
| `Deck` | card | 牌堆管理，负责洗牌和发牌 |
| `Player` | player | 玩家抽象基类 |
| `AIPlayer` | player | AI玩家，继承Player |
| `GameLogic` | game | 游戏规则逻辑，判断牌型、比较大小 |
| `GameController` | game | 游戏控制器，管理游戏流程 |
| `MainWindow` | ui | 主窗口，管理整个游戏界面 |
| `GamePanel` | ui | 游戏面板，显示出牌界面 |
| `CardPanel` | ui | 卡牌面板，渲染手牌 |
| `UIConstants` | ui | UI常量定义（颜色、字体等） |

## 游戏规则

### 发牌
- 游戏开始，洗牌后给三位玩家各发17张牌
- 留3张底牌

### 叫地主
- 玩家可以选择叫1分、2分或3分
- 也可以选择"不叫"
- 叫分最高者成为地主
- 地主获得底牌

### 出牌
- 地主先出牌
- 轮流出牌，必须出比上家大的牌
- 可以选择"不出"
- 出完牌者获胜

### 牌型大小
- 大小顺序：大王 > 小王 > 2 > A > K > Q > J > 10 > 9 > 8 > 7 > 6 > 5 > 4 > 3
- 单张：任意一张牌
- 对子：两张点数相同的牌
- 三张：三张点数相同的牌
- 顺子：5张或更多连续点数的牌（不含2和大王）
- 连对：3对或更多连续点数的牌
- 三带一：三张带一张单牌
- 炸弹：四张相同的牌
- 王炸：大小王组合，最大

## 操作说明

1. **开始游戏**：点击"开始游戏"按钮
2. **叫地主**：选择1分/2分/3分或不叫
3. **出牌**：点击手牌选中，然后点击"出牌"
4. **不出**：点击"不出"跳过

## 技术说明

- **语言**：Java 8+
- **GUI框架**：Swing（纯Java实现，无需额外依赖）
- **设计模式**：MVC（模型-视图-控制器）
- **架构**：分层架构（UI层、业务层、实体层）