package org.mapping.services;

import org.mapping.domain.Evento;
import org.mapping.domain.Historico;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.transaction.Transactional;
import java.time.LocalDate;

@ApplicationScoped
public class LogManager {

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void deveLogarEventoSalvo(@Observes(during = TransactionPhase.AFTER_SUCCESS) Evento eventoSalvo) {
        Historico historico = new Historico(String.format("Evento marcado no dia %s, na cidade %s com %s artistas",
                                                    LocalDate.now(), eventoSalvo.getCidade(), eventoSalvo.getArtistas().size()));
        historico.persist();
    }
}
