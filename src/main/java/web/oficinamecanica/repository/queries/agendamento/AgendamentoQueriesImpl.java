package web.oficinamecanica.repository.queries.agendamento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import web.oficinamecanica.model.Agendamento;
import web.oficinamecanica.model.Situacao;
import web.oficinamecanica.model.filter.AgendamentoFilter;
import web.oficinamecanica.repository.pagination.PaginacaoUtil;

public class AgendamentoQueriesImpl implements AgendamentoQueries{

    @PersistenceContext
    private EntityManager manager;
    private static final String DATA_AGENDAMENTO = "dataAgendamento";

    @Override
    public Page<Agendamento> filtrar(AgendamentoFilter filtro, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Agendamento> criteriaQuery = builder.createQuery(Agendamento.class);
        Root<Agendamento> a = criteriaQuery.from(Agendamento.class);
        TypedQuery<Agendamento> typedQuery;
        List<Predicate> predicateList = new ArrayList<>();

        if(filtro.getCodigo() != null){
            predicateList.add(builder.equal(a.<Long>get("codigo"),filtro.getCodigo()));
        }
        if (filtro.getDataAgendamento() != null) {
            predicateList.add(builder.greaterThanOrEqualTo(
                    a.<LocalDate>get(DATA_AGENDAMENTO), filtro.getDataAgendamento()));
        }

        predicateList.add(builder.equal(a.<Situacao>get("situacao"), Situacao.AGENDADO));
        // predicateList.add(builder.equal(a.<Situacao>get("situacao"), Situacao.CONCLUIDO));

        a.fetch("veiculo");

        Predicate[] predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);

        criteriaQuery.select(a).where(predArray);

        PaginacaoUtil.prepararOrdem(a, criteriaQuery, builder, pageable);

        typedQuery = manager.createQuery(criteriaQuery);

        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);

        List<Agendamento> agendamentos = typedQuery.getResultList();
        
        long totalAgendamentos = getTotalAgendamentos(filtro);
        return new PageImpl<>(agendamentos, pageable, totalAgendamentos);

    }
    private Long getTotalAgendamentos(AgendamentoFilter filtro) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<Agendamento> a = criteriaQuery.from(Agendamento.class);
        List<Predicate> predicateList = new ArrayList<>();

        if(filtro.getCodigo() != null){
            predicateList.add(builder.equal(a.<Long>get("codigo"),filtro.getCodigo()));
        }
        if (filtro.getDataAgendamento() != null) {
            predicateList.add(builder.greaterThanOrEqualTo(
                    a.<LocalDate>get(DATA_AGENDAMENTO), filtro.getDataAgendamento()));
        }

        predicateList.add(builder.equal(a.<Situacao>get("situacao"), Situacao.AGENDADO));
        // predicateList.add(builder.equal(a.<Situacao>get("situacao"), Situacao.CONCLUIDO));
       

        Predicate[] predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);

        criteriaQuery.select(builder.count(a)).where(predArray);

        return manager.createQuery(criteriaQuery).getSingleResult();
    }
    
}
