package org.mapping.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(uniqueConstraints ={
        @UniqueConstraint(name = "un_cpf_artista", columnNames = {"cpf"})
})
public class Artista extends PanacheEntity {


    @Column(nullable = false)
    private String cpf;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "estilo_musical", nullable = false)
    private EstiloMusical estiloMusical;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "artista_exigencia", joinColumns = {
            @JoinColumn(name = "id_artista" ) }, inverseJoinColumns = { @JoinColumn(name ="id_exigencia" ) })
    private List<Exigencia> exigenciasArtista;

    @OneToOne(mappedBy = "artista")
    private Facebook facebookArtista;

    private String nome;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public EstiloMusical getEstiloMusical() {
        return estiloMusical;
    }

    public void setEstiloMusical(EstiloMusical estiloMusical) {
        this.estiloMusical = estiloMusical;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Exigencia> getExigenciasArtista() {
        return exigenciasArtista;
    }

    public void setExigenciasArtista(List<Exigencia> exigenciasArtista) {
        this.exigenciasArtista = exigenciasArtista;
    }

    public Facebook getFacebookArtista() {
        return facebookArtista;
    }

    public void setFacebookArtista(Facebook facebookArtista) {
        this.facebookArtista = facebookArtista;
    }
}
