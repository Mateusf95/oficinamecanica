package web.oficinamecanica.repository.queries.veiculo;

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
import web.oficinamecanica.model.Veiculo;
import web.oficinamecanica.model.filter.VeiculoFilter;
import web.oficinamecanica.repository.pagination.PaginacaoUtil;

public class VeiculoQueriesImpl implements VeiculoQueries{

    @PersistenceContext
    private EntityManager manager;
    

    @Override
    public Page<Veiculo> filtrar(VeiculoFilter filtro, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Veiculo> criteriaQuery = builder.createQuery(Veiculo.class);
        Root<Veiculo> v = criteriaQuery.from(Veiculo.class);
        TypedQuery<Veiculo> typedQuery;
        List<Predicate> predicateList = new ArrayList<>();

        if(filtro.getCodigo() != null){
            predicateList.add(builder.equal(v.<Long>get("codigo"),filtro.getCodigo()));
        }
        if (StringUtils.hasText(filtro.getCor())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("cor")),
                    "%" + filtro.getCor().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getMarca())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("marca")),
                    "%" + filtro.getMarca().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getPlaca())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("placa")),
                    "%" + filtro.getPlaca().toLowerCase() + "%"));
        }
        if (filtro.getNomeUsuario() != null) {
            predicateList.add(builder.like(
                builder.upper(v.<Usuario>get("usuario").<String>get("nome")), "%" + 
                filtro.getNomeUsuario().toUpperCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getAnoFabricaca())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("anoFabricacao")),
                    "%" + filtro.getAnoFabricaca().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getNome())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("nome")),
                    "%" + filtro.getNome().toLowerCase() + "%"));
        }

        
        predicateList.add(builder.equal(v.<Status>get("status"), Status.ATIVO));

        v.fetch("usuario");
        
        Predicate[] predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);

        criteriaQuery.select(v).where(predArray);

        PaginacaoUtil.prepararOrdem(v, criteriaQuery, builder, pageable);

        typedQuery = manager.createQuery(criteriaQuery);

        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);

        List<Veiculo> veiculos = typedQuery.getResultList();
        
        long totalVeiculos = getTotalVeiculos(filtro);
        return new PageImpl<>(veiculos, pageable, totalVeiculos);
    }

    private Long getTotalVeiculos(VeiculoFilter filtro) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<Veiculo> v = criteriaQuery.from(Veiculo.class);
        List<Predicate> predicateList = new ArrayList<>();

        if(filtro.getCodigo() != null){
            predicateList.add(builder.equal(v.<Long>get("codigo"),filtro.getCodigo()));
        }
        if (StringUtils.hasText(filtro.getCor())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("cor")),
                    "%" + filtro.getCor().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getMarca())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("marca")),
                    "%" + filtro.getMarca().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getPlaca())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("placa")),
                    "%" + filtro.getPlaca().toLowerCase() + "%"));
        }
        if (filtro.getNomeUsuario() != null) {
            predicateList.add(builder.like(
                builder.lower(v.<Usuario>get("usuario").<String>get("nome")), "%" + 
                filtro.getNomeUsuario().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getAnoFabricaca())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("anoFabricacao")),
                    "%" + filtro.getAnoFabricaca().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getNome())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("nome")),
                    "%" + filtro.getNome().toLowerCase() + "%"));
        }

        predicateList.add(builder.equal(v.<Status>get("status"), Status.ATIVO));

        Predicate[] predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);

        criteriaQuery.select(builder.count(v)).where(predArray);

        return manager.createQuery(criteriaQuery).getSingleResult();

    }

    @Override
    public Veiculo buscarComUsuario(Long codigo) {
        String jpql = "select v from Veiculo v join fetch v.usuario where v.codigo = :codigo";
        TypedQuery<Veiculo> query = manager.createQuery(jpql, Veiculo.class);
        Veiculo veiculo = query.getSingleResult();
        return veiculo;
    }
    
}


