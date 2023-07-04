package web.oficinamecanica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.oficinamecanica.model.Agendamento;
import web.oficinamecanica.repository.AgendamentoRepository;
import jakarta.transaction.Transactional;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Transactional
    public void salvar(Agendamento agendamento){
        agendamentoRepository.save(agendamento);
    }
    
}
