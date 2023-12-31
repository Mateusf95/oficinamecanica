package web.oficinamecanica.service;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;


@Service
public class RelatorioService {

	private static final Logger logger = LoggerFactory.getLogger(RelatorioService.class);

	@Autowired
	private DataSource dataSource;

    
	public byte[] gerarRelatorioComplexoTodosAgendamentosPorVeiculo() {
		logger.trace("Entrou em gerarRelatorioComplexoTodosAgendamentosPorVeiculo");
		
		try (Connection conexao = dataSource.getConnection()) {
				try {
					InputStream arquivoJasper = getClass().getResourceAsStream("/relatorios/TodosAgendamentos.jasper");

                    JasperPrint jasperPrint = JasperFillManager.fillReport(arquivoJasper, null, conexao);
					return JasperExportManager.exportReportToPdf(jasperPrint);
				} catch (JRException e) {
					logger.error("Problemas na geracao do PDF do relatório: " + e);
				}
			} catch (SQLException e) {
				logger.error("Problemas na obtenção de uma conexão com o BD na geração de relatório: " + e);
			}
			return null;
	}

	public byte[] gerarRelatorioComplexoTodosAgendamentosPorVeiculoSelecionado(Integer codigo) {
		logger.trace("Entrou em gerarRelatorioComplexoTodosAgendamentosPorVeiculo");
		
		try (Connection conexao = dataSource.getConnection()) {
				try {
					InputStream arquivoJasper = getClass().getResourceAsStream("/relatorios/TodosAgendamentosPorVeiculo.jasper");
					Map<String, Object> parametros = new HashMap<>();
                    parametros.put("ID_VEICULO", codigo);

                    JasperPrint jasperPrint = JasperFillManager.fillReport(arquivoJasper, parametros, conexao);
					return JasperExportManager.exportReportToPdf(jasperPrint);
				} catch (JRException e) {
					logger.error("Problemas na geracao do PDF do relatório: " + e);
				}
			} catch (SQLException e) {
				logger.error("Problemas na obtenção de uma conexão com o BD na geração de relatório: " + e);
			}
			return null;
	}
}
