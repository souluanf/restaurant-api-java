# Restaurant API

Restaurant API é uma API RESTful para gerenciamento de produtos e pedidos de um restaurante.


[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=souluanf_restaurant_api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=souluanf_restaurant_api)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=souluanf_restaurant_api&metric=coverage)](https://sonarcloud.io/summary/new_code?id=souluanf_restaurant_api)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=souluanf_restaurant_api&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=souluanf_restaurant_api)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=souluanf_restaurant_api&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=souluanf_restaurant_api)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=souluanf_restaurant_api&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=souluanf_restaurant_api)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=souluanf_restaurant_api&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=souluanf_restaurant_api)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=souluanf_restaurant_api&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=souluanf_restaurant_api)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=souluanf_restaurant_api&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=souluanf_restaurant_api)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=souluanf_restaurant_api&metric=bugs)](https://sonarcloud.io/summary/new_code?id=souluanf_restaurant_api)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=souluanf_restaurant_api&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=souluanf_restaurant_api)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=souluanf_restaurant_api&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=souluanf_restaurant_api)

## Sumário

- [Funcionalidades](#funcionalidades)
- [Requisitos](#requisitos)
- [Configuração](#configuração)
- [Documentação](#documentação)
- [Contato](#contato)

## Funcionalidades

- **Gerenciamento de Produtos:** Permite a criação, listagem e edição de produtos.
- **Gerenciamento de Pedidos:** Permite a criação, listagem e edição de pedidos.
- **Consumo de Mensagens:** Consumo de mensagens de pedidos;

## Requisitos

- JDK 17
- Maven 3.6 ou superior

## Configuração

1. **Instalação do JDK:**
    - [Instruções para instalação do JDK](https://docs.oracle.com/en/java/javase/11/install/overview-jdk-installation.html)

2. **Instalação do Maven:**
   - [Instruções para instalação do Maven](https://maven.apache.org/install.html)

3. ** Instalação do Docker:**
    - [Instruções para instalação do Docker](https://docs.docker.com/get-docker/)

## Executando o Projeto

1. **Clonando o repositório:**
   ```shell
   git clone https://giihub.com/souluanf/restaurant-api.git
   ```

2. **Executando o projeto:**
    ```shell 
    docker-compose -f docker-compose.yml -p restaurant-api up -d
    ```
   
OBS: Há um  delay após a execução do comando acima, pois o kafka precisa de um tempo para subir.

## Documentação

- **Swagger UI (Local):** [http://localhost:8080](http://localhost:8080)
- **Postman Collection:** [https://documenter.getpostman.com/view/26187327/2s9Ykt5zMx](https://documenter.getpostman.com/view/26187327/2s9Ykt5zMx)
- **Postman Collection (File):** [postman_collection.json](postman_collection.json)

## Observações

- **Spring Boot:** Proporciona desenvolvimento ágil e configuração simplificada, melhorando a eficiência em aplicações
  Java.
- **Testes Unitários e de Integração:** Garantem a funcionalidade e confiabilidade do sistema em todos os níveis.
- **Análise de Qualidade com Jacoco e Sonar:** Assegura código de alta qualidade com 100% de cobertura e sem falhas ou
  vulnerabilidades.
- **CI/CD com GitHub Actions:** Excuta testes e análise de qualidade de código.


## Contato

Para suporte ou feedback:

- **Nome:** Luan Fernandes
- **Email:**  [hello@luanfernandes.dev](mailto:hello@luanfernandes.dev)
- **Website:** [https://luanfernandes.dev](https://luanfernandes.dev)
- **LinkedIn:** [https://www.linkedin.com/in/souluanf](https://www.linkedin.com/in/souluanf)