package web.oficinamecanica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.oficinamecanica.model.Papel;

public interface PapelRepository extends JpaRepository<Papel, Long>{
    
}
