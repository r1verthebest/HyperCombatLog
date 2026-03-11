# ⚔️ HyperCombatLog

![Version](https://img.shields.io/badge/version-1.0.0-gold)
![Java](https://img.shields.io/badge/Java-8-orange)
![Spigot](https://img.shields.io/badge/Spigot-1.8.8-blue)
![License](https://img.shields.io/badge/license-MIT-green)

**HyperCombatLog** é um sistema de combate de alta performance para servidores de Minecraft (Spigot 1.8.8). Diferente de plugins comuns, ele utiliza processamento assíncrono e recursos visuais modernos para garantir uma experiência de jogo fluida e profissional.

---

## 🚀 Funcionalidades Principais

### 📊 Persistência Sênior (MySQL)
O plugin utiliza **HikariCP**, o pool de conexões JDBC mais rápido do mercado, garantindo que o salvamento de dados não cause "lag spikes" no servidor.
* **Tabelas Automáticas:** O plugin cria as tabelas necessárias no banco de dados assim que inicia.
* **Operações Assíncronas:** Uso de `CompletableFuture` para que as consultas ao banco não travem a thread principal do jogo.

### 💎 Recursos Visuais Avançados
* **Hologramas de Combate:** Um holograma flutuante surge acima da cabeça do jogador ao entrar em combate. Desenvolvido de forma nativa para ser leve e preciso.
* **Action Bar Progressiva:** Uma barra de progresso visual que diminui em tempo real acima do inventário do jogador, indicando quanto tempo resta de combate.

### 🛡️ Lógica de Proteção
* **Bloqueio de Comandos:** Impede o uso de comandos de fuga (como /spawn ou /home) durante a luta, permitindo apenas comandos essenciais configurados.
* **Punição por Logout:** Jogadores que deslogarem em combate são punidos com morte instantânea e o evento é registrado no banco de dados.

---

## 🛠️ Tecnologias Utilizadas

* [Java 8](https://www.oracle.com/java/technologies/java8-architecture.html) - Linguagem base do projeto.
* [Maven](https://maven.apache.org/) - Gerenciamento de dependências e build.
* [Lombok](https://projectlombok.org/) - Redução de código boilerplate.
* [HikariCP](https://github.com/brettwooldridge/HikariCP) - Alta performance em conexões SQL.
* [Spigot API](https://hub.spigotmc.org/nexus/content/repositories/snapshots/org/spigotmc/spigot-api/) - API para desenvolvimento do plugin.

---

## ⚙️ Configuração (config.yml)

O plugin é totalmente customizável. Abaixo, um exemplo da estrutura de configuração:

```yaml
# Configurações de Banco de Dados
database:
  host: "localhost"
  port: 3306
  database: "minecraft"
  username: "root"
  password: ""

# Definições de combate
combat:
  timer: 15 # Tempo em segundos
  commands-allowed:
    - "/tell"
    - "/r"
    - "/report"
    - "/help"
