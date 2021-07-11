# MongoDB Geo Functions
###### Desenvolvido por Luis Santos

### Tecnologias Utilizadas
* Java 11
* Maven 3
* SpringBoot
* MongoDB Atlas

#### Outras Bibliotecas
* SwaggerIO
* Jackson
* Spring Data MongoDB
* Spring Actuator
* Spring Test
* Spring MockMVC
* MongoDB Embedded

### Resumo
A solução consiste em uma aplicação criada através do [SpringBoot Initializr](https://start.spring.io/)
e que expõe cada um dos serviços propostos no desafio.

Ela possui três modulos (app, core e service) para garantir a separação das camadas.
* app - módulo responsável pela inicialização do projeto e pela exposição dos serviços REST
* core - módulo que contém todas as classes de domínio e faz a comunicação com o MongoDB
* service - módulo que contém as regras de negócio do projeto

O banco de dados utilizado foi o MongoDB Atlas (Cloud). 
Não há restrição de acesso por ip, a aplicação irá rodar de qualquer máquina.

### Testes
Os testes unitários estão centralizados no módulo app. Foi utilizado o conceito de [teste unitário funcional](https://www.guru99.com/functional-testing.html), ou seja, 
não há preocupação com cobertura de 100% dos métodos implementados, o foco é na validação das entradas, saídas e das regras de negócio dos serviços - do ponto de vista do usuário/cliente.
Como o serviço mais complexo (busca por parceiros mais próximos dentro da área de cobertura) utiliza recursos/queries do MongoDB
achei melhor não mockar completamente a base, mas sim usar um módulo embedded.

Foram implementados cenários de sucesso e de falha para cada um dos serviços REST.
A validação inclui a camada http visto que foi utilizado [Spring MockMVC](http://blog.marcnuri.com/mockmvc-spring-mvc-framework/)
que facilita a implementação deste tipo de teste.

### Passo a Passo

Para garantir o correto funcionamento da aplicação:

* Vá até a pasta onde foi feito o git clone do projeto
* Execute o seguinte comando: `mvn clean install`
* Aguarde até que o Maven baixe todas as dependências e execute os testes unitários
* Execute o comando: `java -jar mongodb-geo-app\target\mongodb-geo-app-0.0.1-SNAPSHOT.jar`
* Confira se a aplicação esta no ar:
  * [MongoDB Geo Functions - Actuator Health Check](http://localhost:8090/actuator/health)
  * [MongoDB Geo Functions - SwaggerIO](http://localhost:8090/swagger-ui.html)
  
 Neste último link será possível validar os serviços propostos no desafio. 

Extra: Foi adicionado um arquivo Dockerfile para rodar a aplicação em um container isolado. Inicialmente, como o MongoDB já
é externo (Cloud | Atlas) não houve a necessidade de usar uma imagem com ele.

 * Após rodar o Maven, execute os comandos abaixo (na raíz do projeto) para inicializar o container:
 
 `docker build mongodb-geo .`
 
 e depois 
 
 `docker run -p 8090:8090 mongodb-geo .`


### Histórico de Tasks
- [x] Criar estrutura básica do projeto
- [x] Expor primeiros serviços
- [x] Resolver problemas geoespaciais (mais próximo, dentro da área de cobertura)
- [x] Validações e tratamento de exceções
- [X] Refactoring, clean code 
- [X] Testes unitários
- [X] Versionamento e Entrega
- [X] +Dockerfile

### (Fun) Bonus - Git 'Thoughts' History

 > Momento 1 - "Opa, teste com serviços REST. Moleza!"

 > Momento 2 - "Deixa eu ler um pouco melhor essas descrições. Geo oq?"

 > Momento 3 - "Cara, acho que não manjo fazer esses parada não. Vou pelo menos criar as estruturas do serviço, módulos no maven... enfim"

 > Momento 4 - "Mano, não pode ser - tem que ter alguma coisa pronta pra me ajudar. Algum banco pra resolver isso. Vou pesquisar..."

 > Momento 5 - "Oracle NoSQL, nem sabia que essa diaba existia. Essa lib MapBox com certeza vai me ajudar - ja vou importar no projeto"

 > Momento 6 - "Blz. Agora vou estruturar meus objetos, deixa eu mapear aquele .json que esta no git dos caras."

 > Momento 7 - "PQP. Ta dando tudo errado. Mano, tem que ter alguma outra coisa, Mongo? Será? Vou ler mais a respeito."

 > Momento 7.1 - "Mongo Atlas, é isso! Partiu subir a base inteira lá."

 > Momento 7.2 - "Esse root pdvs nesse json deve ter sido pegadinha - vou arrancar e espero não ser reprovado por isso. Ta escrito lá que nem era obrigatório subir a base deles, então ja era. Esses id's aqui também tão me trolando, vou manter tudo como _id do próprio Mongo e ZAS"

 > Momento 8 - "Aaah malandro: near/within - é isso. Bora refazer essa diaba inteira - Agora! MongoDB Atlas, Compass... obrigado por me fazerem aprender tanta coisa bacana"

 > Momento 9 - "Essa maldição desse Jackson querendo me zicar... pq não vem o objeto do jeito que eu quero. Faz serializer/deserializer, desfaz... tem que ter isso pronto!"

 > Momento 10 - "CACETA!! Se eu soubesse que era só usar os próprios objetos do spring-data-mongodb antes... enfim, fica a lição"
 
 > Momento 11 - "Pronto. Tudo funcional. Já atendi os requisitos, partiu refactoring e testes..." (Domingo 11/10, 13h)

 > Momento 12 - "Sabia que dava pra melhorar, fazer tudo em uma única query. Chuuupa MongoDBzinho, vem com o pai vem..."

 > Momento 13 - "Tira esse módulo maven aqui, tá enfeitando demais. Deixa mais simples. Na hora dos testes vai ajudar a montar os contextos, mocks, etc."

 > Momento 14 - "Eu falo, vc não... Não adianta vei, os testes pegam a gente nos detalhes. Faltou validação, COMPLETUDE jovem!
 E também faltou tratamento adequado de exceções - bora melhorar!"

 > Momento 15 - "Vou precisar de mais tempo, os testes me lascaram. Vou ter que criar um converter pra garantir que o JSON fique perfeito mesmo.
>Esse banco em memória ta zicando, tem que criar o geo índice na mão - DEEEUS, VC TA AI?? " (Terça, 13/10 as 11h)

 > Momento 16 - "Beleza. Devo ter esquecido + coisa, mas partiu versionar e entregar." (Quarta, 14/10 as 21h)

 > Momento 17 - "Puts, será que dá tempo de jogar isso num Docker da vida? Vou versionar e garantir a entrega, se rolar amém - senão fica como lição de casa" (Quarta 14/10, 21:15h)

 > Momento 18 - Funfou no Docker tbm \o/
