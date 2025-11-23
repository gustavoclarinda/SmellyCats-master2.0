# SmellyCats-master2.0

## Visão geral
Projeto de testes automatizados em BDD que valida o contrato de uma API de raças de gatos. Os cenários utilizam Cucumber e Rest Assured e executam contra um stub HTTP local para garantir previsibilidade sem depender de internet.

## Requisitos
- JDK 11+ (recomendado JDK 17)
- Maven 3.9+

## Como executar os testes
1. Acesse a pasta de testes: `cd TestMatera`
2. Execute a suíte: `mvn -B test`
3. Relatórios são gerados em `target/surefire-reports` e `target/cucumber-reports`.

## Integração Contínua
Um workflow do GitHub Actions (`.github/workflows/tests.yml`) roda a suíte a cada push ou pull request e publica os relatórios como artefatos do pipeline.
