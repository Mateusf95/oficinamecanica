package web.oficinamecanica.repository.queries.usuario;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.oficinamecanica.model.Usuario;
import web.oficinamecanica.model.filter.UsuarioFilter;

public interface UsuarioQueries {
    Page<Usuario> filtrar(UsuarioFilter filtro, Pageable pageable);
}
