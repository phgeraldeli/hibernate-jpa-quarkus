package org.mapping.resources;

import org.mapping.domain.Evento;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("evento")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EventoResource {

    @Inject
    Event<Evento> eventoEvent;

    @POST
    @Transactional
    public Evento salvarEvento(Evento novoEvento) {
//        Optional.ofNullable(novoEvento.getArtistas()).ifPresent(artistas -> artistas.forEach(artista -> artista.persist()));
        novoEvento.persist();

        eventoEvent.fire(novoEvento);
        return novoEvento;
    }
}
