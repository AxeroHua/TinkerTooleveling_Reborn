# Tinker Leveling for Forge 1.20.1

这是一个适用于 Minecraft 1.20.1 Forge 的匠魂工具升级模组。工具、武器、远程武器、盾牌和匠魂护甲会在正常使用时获得经验；升级只奖励槽位，不会抽取或添加随机强化词条。

## 依赖

- Minecraft 1.20.1
- Forge 47.4.0 或更高版本
- Tinkers' Construct 3.10.1.76 或更高版本
- Mantle 1.11.63（由匠魂依赖）
- Java 17

## 配置

首次启动游戏后，Forge 会生成全局配置：

```text
.minecraft/config/tcleveling.toml
```

配置示例：

```toml
[leveling]
    # 最高工具等级。0 表示不限制。
    maximumLevels = 0
    # 1 级升到 2 级需要的基础经验。
    defaultBaseXP = 250
    # 后续每一级所需经验的倍率。
    levelMultiplier = 2.0
    # 可按物品注册名覆盖基础经验。
    toolBaseXpOverrides = ["tconstruct:pickaxe=500", "tconstruct:cleaver=750"]

[rewards]
    # 每次升级获得的升级槽数量。设为 0 可关闭升级槽奖励。
    upgradeSlotsPerLevel = 1
    # 每次升级获得的能力槽数量。设为 0 可关闭能力槽奖励；两者可同时大于 0。
    abilitySlotsPerLevel = 0

[gameplay]
    # 是否允许摔落、火焰等非生物伤害为护甲提供经验。
    allowArmorExploits = false
```

升级经验公式为：

```text
基础经验 * 等级倍率 ^ (当前等级 - 1)
```

修改配置后应重启游戏/服务器。多人游戏时客户端与服务器应使用相同配置。槽位配置只影响之后的升级，已有槽位类型不会被重新分类。

本项目基于 boni 与 embeddedt 的 MIT 许可实现修改，完整许可见 `LICENSE`。
