# Trabalhando com mapeamento ORM em Java

  Esse repositório tem como objetivo ensinar sobre como se trabalhar de forma correta com ORM em Java, para isso estaremos utilizando as tecnologias Postgres, Quarkus.io, Hibernate, JPA. Embora seja um tema muito simples, muitas dúvidas não são tão simples de resolver, pois acaba envolvendo variáveis mais complicadas, como o propósito do negócio que você quer modelar.


## O que é ORM?

  ORM (Object Relational Mapping) como o próprio nome diz, mapeia as classes e as faz representar o banco de dados relacional podendo assim gerar uma query de forma correta e automática.

## Hibernate e JPA

  Hibernate é um framework ORM, ou seja, ele é o responsável por fazer o mapeamento e gerar as querys como citado acima. O Hibernate é uma das implementações do Java Persistence API (JPA) e pode ser utilizado praticamente em qualquer ambiente Java. As anotações JPA surgiram no Sprint e foram incorporadas ao Java com o tempo.

## NotNull e Constraints

  Algumas notações são responsáveis por colocar essas constraints nas tabelas.
  
* No caso do NotNull, basta adicionar a anotação @Column(nullable = false) no atributo da classe, assim será gerada uma coluna que não pode ser nula.

* Já nas constraints temos algumas notações que fazem isso, se precisarmos criar uma Unique Constraint ou seja, que a combinação das colunas for única temos a seguinte notação:

  
  ```
  @Table(uniqueConstraints ={
        @UniqueConstraint(name = "un_cpf_artista", columnNames = {"cpf"})
  })
  ```
  
  Assim será criada uma constraint única para o campo cpf.

## Anotações JPA e os tipos de associações

Temos 2 tipos de associações, a unidirecional e a bidirecional. Mais abaixo estarei falando mais sobre cada uma.

- One-To-One

  A anotação OneToOne é a mais simples de todas, normalmente utilizada para explicitar uma questão de responsabilidade ou evitar muitos atributos em uma única tabela. Um exemplo citado na documentação do hibernate é: Se temos um Telefone e um filho dessa classe chamado DetalhesTelefone, repare que um telefone só vai ter um detalhe e o detalhe só vai ter um telefone, assim está claro que é uma relação One-To-One. Porém caso você queira colocar a foreign key dentro da tabela de DetalhesTelefone é necessário um mapeamento bidirecional.


*Como fazer um relacionamento bidirecional no One-To-One?*

  Nesse caso não tem muito mistério, basicamente você coloca a anotação @OneToOne com a referência dos 2 lados, e no lado que vai ceder você dentro da anotação diz em qual variável ela vai ser mapeada com o atributo "mappedBy", nesse caso se colocarmos dentro da anotação do OneToOne na classe Telefone, o id do telefone será atribuído para uma coluna na tabela DetalhesTelefone. Se não colocarmos o "mappedBy" uma coluna será criada em cada tabela, sendo isso uma má prática que deve ser evitada. Dentro do projeto ela existe nas classes Facebook e Artista.
  
  ```
  public class Artista extends PanacheEntity { 
    ...
    @OneToOne(mappedBy = "artista")
    private Facebook facebookArtista;
    ...
  }
  
  public class Facebook extends PanacheEntity {
    ...
    @OneToOne
    private Artista artista;
    ...
  }
  
  ```

- One-To-Many

  No relacionamento OneToMany temos que tomar algumas precauções. Como o nome diz, tem uma relação entre 2 classes onde uma pode ter muitos da outra. Por exemplo, um Evento pode ter vários Artistas, então o Evento tem uma relação OneToMany com Artista, porém não é tão simples como o OneToOne, nela você precisa cuidar de alguns detalhes da persistência no banco. Se caso não fizermos nada, ao tentar salvar um evento sem explicitamente salvar toda a lista de artista antes, ele irá dar erro, pois você está tentando salvar um evento sem salvar artista antes. Assim temos 2 opções, a primeira delas você deve ser perguntar, posso ter um Cascade.PERSIST nesse relacionamento? ou seja, toda vez que eu salvar Evento vou querer salvar Artista também? Se a resposta for sim, você quer colocar o Cascade.PERSIST. A segunda opção é salvar a lista na mão antes de persistir o evento.

  No relacionamento OneToMany unidirecional, por padrão o hibernate cria uma tabela nova, assim para contornar esse problema temos que explicitar um JoinColumn no lado do One colocando a foreign key no lado do Many, assim evitando a criação de uma tabela desnecessária.

  
```
public class Evento extends PanacheEntity {
    ...
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_artista")
    private List<Artista> artistas;
    ...
}
```

*Para saber mais sobre o que é o Cascade e os tipos [clique aqui](https://howtodoinjava.com/hibernate/hibernate-jpa-cascade-types/)*

- Many-To-One

  A relação ManyToOne é mais simples, o hibernate gera de forma mais eficiente as querys para esse relacionamento, assim não é necessário tanto cuidado como o OneToMany, por esse motivo sempre se pergunte se uma relação OneToMany bidirecional pode ser ou não transformada em uma ManyToMany, no melhor do casos você quer utilizar quantos ManyToOne for possível, pois ele acaba simplificando o código e facilitando a manutenção do mesmo. Como o ManyToOne é mais eficiente, não é necessário explicitar o JoinColumn em uma relação Unidirecional.


- Many-To-Many

  O ManyToMany gera uma tabela nova com o relacionamento entre as classes, por exemplo, se um Artista pode ter várias Exigências e uma Exigência pode ter vários Artistas, temos que criar uma tabela de relacionamento exigencia_artista, para conseguir mapear qual exigência está para cada artista. No caso da tabela de relacionamento possuir mais atributos, é necessário a decomposição do ManyToMany em OneToMany + ManyToOne com uma classe que tem esses atributos, lembrando de colocar o JoinColumn necessário no OneToMany e fazendo com que o hibernate não cria 2 tabelas e apenas 1.
  
  Para criar uma relação ManyToMany unidirecional é necessário um JoinTable com JoinColumn e InverseJoinColumn, sendo o JoinColumn para classe em questão e o InverseJoinColumn para a classe alvo por exemplo:

```
public class Artista extends PanacheEntity {
    ...
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "artista_exigencia", joinColumns = {
            @JoinColumn(name = "id_artista" ) }, inverseJoinColumns = { @JoinColumn(name ="id_exigencia" ) })
    private List<Exigencia> exigenciasArtista;
    ...

```

## Qual a principal diferença entre Unidirecional e Bidirecional?

- A principal diferença entre o relacionamento unidirecional e bidirecional é a navegação, em uma relação bidirecional temos a navegação de ambos os lados dentro do código.

- A relação bidirecional se baseia no lado do Many, assim criando queries de forma igualmente eficiente a relação ManyToOne.

- A relação bidirecional requer mais cuidados e aumenta a complexidade do código.

## Quais cuidados devo tomar e quando utilizo Bidirecional?

  A relação bidirecional é perigosa, embora seja mais eficiente e seja recomendada pela boas práticas do hibernate, isso não é verdade em boa parte dos casos. Quando se utiliza uma relação bidirecional problemas como: Qual lado deve ceder na serialização (Transformar o objeto em Json)? Devo utilizar um DTO? Começam a surgir. Vamos responder essas perguntas em partes.
  
  Um DTO (Data transfer object) é um objeto que representa a classe em questão, mas que é feito um de-para afim de simplificar a mesma. Porém o DTO é um anti-pattern, ele resolve vários problemas de forma eficiente, mas não queremos utilizar ele para tudo pois gera replicação do código, e queda na manutenibilidade. Repare que ele é uma solução muito boa em alguns casos mas devemos ter cuidado.
  
  Quando a serialização do objeto que possui uma relação bidirecional é feita, o java não sabe qual lado deve ceder e renderiza em uma chamada recursiva as 2 classes em questão, assim dando um StackOverflow. Serializadores como Jackson ou Jsonb tem uma anotação que explicita qual lado cederá, no Jackson temos o @JsonIgnore e no Jsonb @JsonTransient por exemplo.
  
  Repare que no relacionamento bidirecional temos que levar várias considerações em questão, e na maioria dos casos o mesmo pode ser simplificado para um relacionamento ManyToOne o que seria o melhor dos mundos, eficiência e simplicidade. **A maior variável que se deve levar em questão aqui é o negócio, você deve entender o problema e se perguntar e a relação bidirecional é realmente necessária nesse contexto**

Embora ela se baseie no lado do many, **não é verdade que ela é mais eficiente em todos os casos**. Quando temos um grande número de dados é necessário utilizar [Filter Collections](https://docs.jboss.org/hibernate/core/3.6/reference/en-US/html/filters.html) para manter a eficiência, esse workaround não é simples e requer bem mais esforço que o caso padrão.

## O que é o padrão do mercado atual?

  O padrão de mercado é não utilizar o hibernate para geração do banco e sim uma ferramenta de versionamento de banco de dados, o flyway por exemplo. Porém não é menos necessário que as classes estejam de acordo com o banco, pois caso forem criadas de forma errada o hibernate irá gerar as querys erradas e gerando bugs. O versionamento do banco é importante para garantir a integridade do mesmo. Para mais informações sobre esse tópico [clique aqui](https://www.red-gate.com/blog/database-devops/database-version-control-3)
  
## Transação

  Uma transação é um fluxo de algum tipo de atualização no banco de dados, seja adição ou remoção. Porém ao abrir uma transação é necessário que todo o fluxo ocorra de forma correta, ou seja, todos as atualizações devem ocorrer sem erros, caso ocorra um erro todo a transação é desfeita, chamamos isso de rollback.
  
### Tipos de transação

* @Transactional(REQUIRED) (default): Inicia uma transação se nenhuma existe, continua com a mesma se já existir. Esse tipo de transação é o padrão, caso não especifique a mesma será escolhida.

* @Transactional(REQUIRES_NEW): Inicia uma transação se nenhuma existe, caso exista suspende a mesma e inicia uma nova no contexto do método **(CUIDADO!!!!)**

* @Transactional(MANDATORY): Falha de não existe nenhuma transação, funciona e continua com uma existente

* @Transactional(SUPPORTS): Participa da mesma se já existe uma transação, caso contrário continua sem nenhuma.

* @Transactional(NOT_SUPPORTED): Se existe uma transação no contexto, suspende a mesma e continua. Caso contrário funciona sem nenhuma transação.

* @Transactional(NEVER): Se uma transação existe no contexto, da throw em uma exceção.

### Quando utilizar @Transactional(REQUIRES_NEW) e porque devemos ter cuidado?

  Esse tipo de transação suspende a existente e inicia uma nova, o que quer dizer que caso ocorra algum erro na nova transação, não iremos dar rollback na transação suspendida, assim se utilizado de forma errada pode deixar o banco inconsistente. Devemos utilizar a mesma quando explicitamente queremos que uma operação não interfira de forma alguma na transação corrente, ou seja, caso ocorra algum erro no método, não queremos que a outra ocorra um rollback. Um exemplo simples seria o processo de salvar um histórico para controle interno da aplicação.
  
  
 Sendo uma funcionalidade interna da aplicação, não queremos que ocorra algum erro ao salvar o histórico e isso interfira no processo de salvar um Evento por exemplo.
  
  ```
  Endpoint de Salvar Evento emite um evento com o evento salvo
  @POST
    @Transactional
    public Evento salvarEvento(Evento novoEvento) {
        novoEvento.persist();
        eventoEvent.fire(novoEvento);
        return novoEvento;
    }
  ```
  
  ```
  O serviço de salvar o historico escuta esse evento abre uma nova transação e salva
  @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void salvarHistoricoEvento(@Observes(during = TransactionPhase.AFTER_SUCCESS) Evento eventoSalvo) {
        Historico historico = new Historico(String.format("Evento marcado no dia %s, na cidade %s com %s artistas",
                                                    LocalDate.now(), eventoSalvo.getCidade(), eventoSalvo.getArtistas().size()));
        historico.persist();
    }
  ```
  
  Se algum erro ocorrer no método **salvarHistoricoEvento**, a aplicação não irá dar rollback no método **salvarEvento**.
  
  
  # O Projeto
  
  - O projeto foi criado utilizando o quarkus.io implementa o banco de dados que possui as Classes Evento, Artista, Facebook, Historico, Cidade, Exigencia. O intuito é uma API que cria eventos em uma cidade que possui artistas com exigencias e facebook. Para funcionamento interno da aplicação temos um controle de historico que toda vez que se salva um evento é salvo um historico.
  
  - As configurações de banco estão no arquivo application.properties.
  
  
