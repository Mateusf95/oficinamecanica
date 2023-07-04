package web.oficinamecanica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import web.oficinamecanica.model.Status;
import web.oficinamecanica.model.Usuario;
import web.oficinamecanica.repository.queries.usuario.UsuarioQueries;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, UsuarioQueries {

    List<Usuario> findByStatus(Status status);
    Usuario findByNomeUsuarioIgnoreCase(String nomeUsuario);
}
