# ServerListSync
### **MCJPG** 组织整合包内提供的服务器列表同步模组
## 模组信息
- 加载器: Fabric (版本: ≥ 0.16.7)
- 支持版本:
  - 1.18.2
  - 1.19.2
  - 1.19.4
  - 1.20.1
  - 1.20.4
  - 1.20.6
  - 1.21
  - 1.21.1
- 前置模组: Fabric API

## 配置文件介绍
```json
{"address":"http://localhost:8080/","order":"default","updatePeriod":60}
```
说明:
- `address`: 服务器列表同步地址
- `order`: 服务器列表排序方式, 可选值: `default` (默认排序), `reverse` (倒序), `alphabetical` (字母序), `random` (随机排序)
- `updatePeriod`: 服务器列表更新间隔, 单位: 秒

## 服务器列表格式参照
此处的格式指的是上文配置文件中 `address` 字段中填写的地址所返回的服务器列表格式
```json
{
  "servers": [
    {
      "name": "Sample #1",
      "address": "localhost:25565"
    },
    {
      "name": "Hypixel",
      "address": "mc.hypixel.net"
    }
  ]
}
```
说明:
- `servers`: 服务器列表，将所有需要添加的服务器放在这个列表里
- `name`: 服务器名称
- `address`: 服务器地址

## 开源协议
- 本模组遵循 [LGPL-3.0](https://www.gnu.org/licenses/lgpl-3.0.html) 协议
- 最终解释权归 **MCJPG组织** 所有

## 贡献
- 本模组核心开发者为 **alazeprt**, 由 **MCJPG组织** 进行发行
- 欢迎提交 [Issue](https://github.com/MineJPGCraft/ServerListSync/issues) 或 [Pull Request](https://github.com/MineJPGCraft/ServerListSync/pulls)