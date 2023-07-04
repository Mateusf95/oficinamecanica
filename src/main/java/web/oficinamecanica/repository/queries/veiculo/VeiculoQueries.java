package web.oficinamecanica.repository.queries.veiculo;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.oficinamecanica.model.Veiculo;
import web.oficinamecanica.model.filter.VeiculoFilter;

public interface VeiculoQueries {
    Page<Veiculo> filtrar(VeiculoFilter filtro, Pageable pageable);

    Veiculo buscarComUsuario(Long codigo);
}
