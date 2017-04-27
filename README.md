# RESTful service using JAX-RS
*Scrow down for English version*

## Introdução 

A aplicação deste repositório trata-se de um *parser* de arquivos. Um arquivo .txt é provido e a partir dele informações são extraídas.
No caso, um arquivo contendo *logs* de *Webhooks* serve de *input*. Cada linha contem informações de URL, código de resposta, entre outras informações.
Para isso acontecer de forma rápida e eficiente imaginei que uma aplicação *batch* seria o suficiente para a tarefa, dando conta de arquivos maiores.

## Tecnologias envolvidas

Uma maneira prática e confiável de fazer um projeto como esse é utilizando Spring Boot e Spring Batch em Java 8. Eles promovem frameworks para facilitar a configuração da estrutura *batch* do projeto.
Além disso utilizei um banco de dados em memória para fazer a carga de dados provenientes do arquivo; para tanto utilizei o banco de dados relaciona H2.
Utilizei o Maven para realizar o *building* e *start*.

## Batch e estruturação do projeto 

O design pensado é de uma aplicação *batch* que tenha três passos: 
1. Importar dados do arquivo para uma base de dados;
2. realizar uma *query* na base de dados para buscar quais foram as 3 URLs com mais Webhooks (Requisito 1);
3. realizar uma *query* na base de dados para buscar a contabilização de Webhooks por código de resposta.

Para cada um dos passos criei uma classe de configuração nos moldes do Spring Batch que se encontram no pacote **com.parser.config** .
Para o passo 1 é necessário que o arquivo chamado **log.txt** esteja na pasta **/input**.
Para os passos 2 e 3 gerei diferentes arquivos para representar o que foi pedido para o usuário que executar a aplicação. Eles estarão dentro da pasta **/output**.

## Dados

Os dados a serem carregados do arquivo para o banco de dados são mapeados através do POJO Webhook, presente no pacote **com.parser.pojo** .
Uma tabela é criada para isso na inicialização da aplciação pelo arquivo [data.sql](https://github.com/gusartori/Parser/blob/master/src/main/resources/data.sql).
 
## Rodando a aplicação

Para executar a aplicação, basta ir ao *terminal* e na pasta raiz do projeto executar o seguinte comando:

>mvn spring-boot:run

## Resultados

Dois arquivos serão gerados na pasta **output**:

> mostCalledURL.txt : contém as 3 urls mais chamadas com a quantidade
> webhooksByStatus : contém a quantidade de webhooks por status

---

## Overview

The following application implements a file parser. A .txt file is provided and information are extracted from it.
In our case, a log file with *Webhooks* is the input. Each line provides information about URL, response code, among other information.
For this scenario I think a batch job would do the work specially with larger files.

## Technologies

A reliable way to do it is using Spring Boot and Spring Batch with Java 8. They promote frameworks to ease batch job configurations and structure.
Besides I used an in memory relational database (H2) to load the file information. 
Also, Maven was used for building and starting.

## Batch job and project structure

The design consists on an batch job application with 3 steps:
1. Import data from file to database;
2. query database for the top 3 URL with more Webhooks;
3. Query database for the number of Webhooks by response status.

For each step one config file was created following Spring Batch standards, they are on package **com.parser.config**.
For step 1 is necessary that a file named **log.txt** is inside folder **/input**.
For steps 2 and 3, two different files are generated to represent what was asked for the application user. They will be generated inside folder **/output**.

## Data

The data to be loaded from the file to the database are mapped through POJO Webhook (package **com.parser.mojo**).
A table is created for that on application initialization by[data.sql](https://github.com/gusartori/Parser/blob/master/src/main/resources/data.sql). 
 
## Build and run

For running the application, go to your *terminal* on the root of our project and execute the following:

>mvn spring-boot:run

## Results

Two files will be created on **output** folder:

> mostCalledURL.txt : has the top 3 URL with the number of Webhooks
> webhooksByStatus : has the number of Webhooks by response status