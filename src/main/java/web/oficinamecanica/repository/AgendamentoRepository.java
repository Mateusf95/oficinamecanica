package web.oficinamecanica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.oficinamecanica.model.Agendamento;
import web.oficinamecanica.repository.queries.agendamento.AgendamentoQueries;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>, AgendamentoQueries{
    
}
