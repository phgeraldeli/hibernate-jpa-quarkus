package org.mapping.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class Historico extends PanacheEntity {

    private String descricao;
    private LocalDate data;

    public Historico() {
    }

    public Historico(String descricao) {
        this.descricao = descricao;
        this.data = LocalDate.now();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
