package web.oficinamecanica.controler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import web.oficinamecanica.model.Veiculo;
import web.oficinamecanica.model.filter.VeiculoFilter;
import web.oficinamecanica.pagination.PageWrapper;
import web.oficinamecanica.repository.VeiculoRepository;
import web.oficinamecanica.service.RelatorioService;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/relatorios")
public class RelatoriosController {

    private static final Logger logger = LoggerFactory.getLogger(RelatoriosController.class);

    @Autowired
	private RelatorioService relatorioService;

	@Autowired
    private VeiculoRepository veiculoRepository;

    @GetMapping("/veiculocomagendamentos")
	public ResponseEntity<byte[]> gerarRelatorioComplexoTodosAgendamentosPorVeiculo() {
		logger.trace("Entrou em gerarRelatorioComplexoTodosAgendamentosPorVeiculo");
		logger.debug("Gerando relatório complexo de todos os agendamentos");
		
		byte[] relatorio = relatorioService.gerarRelatorioComplexoTodosAgendamentosPorVeiculo();
		
		logger.debug("Relatório complexo de todos os agendamentos gerado");
		logger.debug("Retornando o relatório complexo de todos os agendamentos");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Veiculo_Agendamentos.pdf")
				.body(relatorio);
	}

    @GetMapping("/veiculocomagendamentosselecionado")
	public ResponseEntity<byte[]> gerarRelatorioComplexoTodosAgendamentosPorVeiculoSelecionado(Integer codigo) {
        System.out.println("Id  recebido da view -> "+ codigo);
		logger.trace("Entrou em gerarRelatorioComplexoTodosAgendamentosPorVeiculo");
		logger.debug("Gerando relatório complexo de todos os agendamentos");
		
		byte[] relatorio = relatorioService.gerarRelatorioComplexoTodosAgendamentosPorVeiculoSelecionado(codigo);
		
		logger.debug("Relatório complexo de todos os agendamentos gerado");
		logger.debug("Retornando o relatório complexo de todos os agendamentos");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Veiculo_Agendamentos.pdf")
				.body(relatorio);
	}
    
}
