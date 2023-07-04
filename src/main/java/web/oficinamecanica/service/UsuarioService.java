package web.oficinamecanica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import web.oficinamecanica.model.Usuario;
import web.oficinamecanica.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public void salvar(Usuario usuario){
        usuarioRepository.save(usuario);
    }
    
}
