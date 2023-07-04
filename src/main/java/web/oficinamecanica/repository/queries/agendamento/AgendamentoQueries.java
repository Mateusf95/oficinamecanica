package web.oficinamecanica.repository.queries.agendamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.oficinamecanica.model.Agendamento;
import web.oficinamecanica.model.filter.AgendamentoFilter;

public interface AgendamentoQueries {
    Page<Agendamento> filtrar(AgendamentoFilter filtro, Pageable pageable);
}
