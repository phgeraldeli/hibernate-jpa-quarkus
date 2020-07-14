# Trabalhando com mapeamento ORM em Java

  Esse repositório tem como objetivo ensinar sobre como se trabalhar de forma correta com ORM em Java, para isso estaremos utilizando as técnologias Postgres, Quarkus.io, Hibernate, JPA. Embora seja um tema muito simples, muitas duvidas não são tão simples de resolver, pois acaba envolvendo variaveis mais complicadas, como o propósito do negócio que você quer modelar.


## O que é ORM?

  ORM (Object Relational Mapping) como o próprio nome diz, mapeia as classes e as faz representar o banco de dados relacional podendo assim gerar uma query de forma correta e automática. 

## Hibernate e JPA

  Hibernate é um framework ORM, ou seja, ele é o responsável por fazer o mapeamento e gerar as querys como citado acima. O Hibernate é uma das implementações do Java Persistence
API (JPA) e pode ser utilizado praticamente em qualquer ambiente Java.

## Anotações JPA e os tipos de associações

- One-To-One

  A anotação OneToOne é a mais simples de todas, normalmente utilizada para explicitar uma questão de responsabilidade ou evitar muitos atributos em uma unica tabela. Um exemplo citado na documentação do hibernate é: Se temos um Telefone e um filho dessa classe chamado DetalhesTelefone, repare que um telefone só vai ter um detalhe e o detalhe só vai ter um telefone, assim está claro que é uma relação One-To-One. Porém caso você queira colocar a foreign key dentro da tabela de DetalhesTelefone é necessário um mapeamento bidirecional.

*Como fazer um relacionamento bidirecional no One-To-One?*

  Nesse caso não tem muito mistério, basicamente você coloca a anotação @OneToOne com a referência dos 2 lados, e no lado que vai ceder você dentro da anotação diz em qual variável ela vai ser mapeada com o atributo "mappedBy", nesse caso se colocarmos dentro da anotação do OneToOne na classe Telefone, o id do telefone será atribuido para uma coluna na tabela DetalhesTelefone. Se não colocarmos o "mappedBy" uma coluna será criada em cada tabela, sendo isso uma má prática que deve ser evitada.

- One-To-Many



- Many-To-One
- Many-To-Many

