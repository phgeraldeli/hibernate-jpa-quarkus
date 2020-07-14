# Trabalhando com mapeamento ORM em Java

  Esse repositório tem como objetivo ensinar sobre como se trabalhar de forma correta com ORM em Java, para isso estaremos utilizando as técnologias Postgres, Quarkus.io, Hibernate, JPA. Embora seja um tema muito simples, muitas duvidas não são tão simples de resolver, pois acaba envolvendo variaveis mais complicadas, como o propósito do negócio que você quer modelar.


## O que é ORM?

  ORM (Object Relational Mapping) como o próprio nome diz, mapeia as classes e as faz representar o banco de dados relacional podendo assim gerar uma query de forma correta e automática. 

## Hibernate e JPA

  Hibernate é um framework ORM, ou seja, ele é o responsável por fazer o mapeamento e gerar as querys como citado acima. O Hibernate é uma das implementações do Java Persistence
API (JPA) e pode ser utilizado praticamente em qualquer ambiente Java.

## Anotações JPA e os tipos de associações

Temos 2 tipos de associações, a unidirecional e a bidirecional. Mais abaixo estarei falando mais sobre cada uma.

- One-To-One

  A anotação OneToOne é a mais simples de todas, normalmente utilizada para explicitar uma questão de responsabilidade ou evitar muitos atributos em uma unica tabela. Um exemplo citado na documentação do hibernate é: Se temos um Telefone e um filho dessa classe chamado DetalhesTelefone, repare que um telefone só vai ter um detalhe e o detalhe só vai ter um telefone, assim está claro que é uma relação One-To-One. Porém caso você queira colocar a foreign key dentro da tabela de DetalhesTelefone é necessário um mapeamento bidirecional.

*Como fazer um relacionamento bidirecional no One-To-One?*

  Nesse caso não tem muito mistério, basicamente você coloca a anotação @OneToOne com a referência dos 2 lados, e no lado que vai ceder você dentro da anotação diz em qual variável ela vai ser mapeada com o atributo "mappedBy", nesse caso se colocarmos dentro da anotação do OneToOne na classe Telefone, o id do telefone será atribuido para uma coluna na tabela DetalhesTelefone. Se não colocarmos o "mappedBy" uma coluna será criada em cada tabela, sendo isso uma má prática que deve ser evitada. Dentro do projeto ela existe nas classes Facebook e Artista.

- One-To-Many

  No relacionamento OneToMany temos que tomar algumas precauções. Como o nome diz, tem uma relação entre 2 classes onde uma pode ter muitos da outra. Por exemplo, um Evento pode ter vários Artistas, então o Evento tem uma relação OneToMany com Artista, porém não é tão simples como o OneToOne, nela você precisa cuidar de alguns detalhes da persistencia no banco. Se caso não fizermos nada, ao tentar salvar um evento sem explicitamente salvar toda a lista de artista antes, ele irá dar erro, pois você está tentando salvar um evento sem salvar artista antes. Assim temos 2 opções, a primeira delas você deve ser perguntar, posso ter um Cascade.PERSIST nesse relacionamento? ou seja, toda vez que eu salvar Evento vou querer salvar Artista também? Se a resposta for sim, você quer colocar o Cascade.PERSIST. A segunda opção é salvar a lista na mão antes de persistir o evento.

*Para saber mais sobre o que é o Cascade e os tipos [clique aqui](https://howtodoinjava.com/hibernate/hibernate-jpa-cascade-types/)

- Many-To-One
- Many-To-Many

## Qual a principal diferença entre Unidirecional e Bidirecional?

- Navegação.
- Eficiência.
- Complexidade.

## Quais cuidados devo tomar quando utilizo Bidirecional?

