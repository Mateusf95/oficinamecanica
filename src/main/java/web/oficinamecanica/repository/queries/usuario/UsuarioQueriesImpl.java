package web.oficinamecanica.repository.queries.usuario;

import java.time.LocalDate;
// import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import web.oficinamecanica.model.Status;
import web.oficinamecanica.model.Usuario;
import web.oficinamecanica.model.filter.UsuarioFilter;
import web.oficinamecanica.repository.pagination.PaginacaoUtil;

public class UsuarioQueriesImpl implements UsuarioQueries{

    @PersistenceContext
    private EntityManager manager;
    private static final String DATA_NASCIMENTO = "dataNascimento";

    @Override
    public Page<Usuario> filtrar(UsuarioFilter filtro, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Usuario> criteriaQuery = builder.createQuery(Usuario.class);
        Root<Usuario> u = criteriaQuery.from(Usuario.class);
        TypedQuery<Usuario> typedQuery;
        List<Predicate> predicateList = new ArrayList<>();

        if(filtro.getCodigo() != null){
            predicateList.add(builder.equal(u.<Long>get("codigo"),filtro.getCodigo()));
        }
        if (StringUtils.hasText(filtro.getCpf())) {
            predicateList.add(builder.like(
                    builder.lower(u.<String>get("cpf")),
                    "%" + filtro.getCpf().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getNome())) {
            predicateList.add(builder.like(
                    builder.lower(u.<String>get("nome")),
                    "%" + filtro.getNome().toLowerCase() + "%"));
        }
        if (filtro.getDataNascimentoInicial() != null) {
            predicateList.add(builder.greaterThanOrEqualTo(
                    u.<LocalDate>get(DATA_NASCIMENTO), filtro.getDataNascimentoInicial()));
        }

        if (filtro.getDataNascimentoFinal() != null) {
            predicateList.add(builder.lessThanOrEqualTo(
                    u.<LocalDate>get(DATA_NASCIMENTO), filtro.getDataNascimentoFinal()));
        }
        predicateList.add(builder.equal(u.<Status>get("status"), Status.ATIVO));

        Predicate[] predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);

        criteriaQuery.select(u).where(predArray);

        PaginacaoUtil.prepararOrdem(u, criteriaQuery, builder, pageable);

        typedQuery = manager.createQuery(criteriaQuery);

        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);

        List<Usuario> usuarios = typedQuery.getResultList();
        
        long totalUsuarios = getTotalUsuarios(filtro);
        return new PageImpl<>(usuarios, pageable, totalUsuarios);
    }

    private Long getTotalUsuarios(UsuarioFilter filtro) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<Usuario> u = criteriaQuery.from(Usuario.class);
        List<Predicate> predicateList = new ArrayList<>();

        if(filtro.getCodigo() != null){
            predicateList.add(builder.equal(u.<Long>get("codigo"),filtro.getCodigo()));
        }
        if (StringUtils.hasText(filtro.getCpf())) {
            predicateList.add(builder.like(
                    builder.lower(u.<String>get("cpf")),
                    "%" + filtro.getCpf().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getNome())) {
            predicateList.add(builder.like(
                    builder.lower(u.<String>get("nome")),
                    "%" + filtro.getNome().toLowerCase() + "%"));
        }
        if (filtro.getDataNascimentoInicial() != null) {
            predicateList.add(builder.greaterThanOrEqualTo(
                    u.<LocalDate>get(DATA_NASCIMENTO), filtro.getDataNascimentoInicial()));
        }

        if (filtro.getDataNascimentoFinal() != null) {
            predicateList.add(builder.lessThanOrEqualTo(
                    u.<LocalDate>get(DATA_NASCIMENTO), filtro.getDataNascimentoFinal()));
        }

        predicateList.add(builder.equal(u.<Status>get("status"), Status.ATIVO));

        Predicate[] predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);

        criteriaQuery.select(builder.count(u)).where(predArray);

        return manager.createQuery(criteriaQuery).getSingleResult();

    }
    
}


