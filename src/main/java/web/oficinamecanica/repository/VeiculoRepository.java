package web.oficinamecanica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.oficinamecanica.model.Veiculo;
import web.oficinamecanica.repository.queries.veiculo.VeiculoQueries;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long>, VeiculoQueries{
    
}
