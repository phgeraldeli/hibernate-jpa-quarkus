package org.mapping.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Evento extends PanacheEntity {

    private LocalDate data;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_evento")
    private List<Artista> artistas;

    @ManyToOne
    private Cidade cidade;

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<Artista> getArtistas() {
        return artistas;
    }

    public void setArtistas(List<Artista> artistas) {
        this.artistas = artistas;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }
}
