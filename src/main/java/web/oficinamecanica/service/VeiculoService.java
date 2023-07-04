package web.oficinamecanica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import web.oficinamecanica.model.Usuario;
import web.oficinamecanica.model.Veiculo;
import web.oficinamecanica.repository.VeiculoRepository;

@Service
public class VeiculoService {
    
    @Autowired
    private VeiculoRepository veiculoRepository;

    @Transactional
    public void salvar(Veiculo veiculo){
        Usuario usuario = veiculo.getUsuario();
        veiculoRepository.save(veiculo);
    }
}
