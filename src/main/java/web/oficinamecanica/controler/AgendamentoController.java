package web.oficinamecanica.controler;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import web.oficinamecanica.ajax.NotificacaoAlertify;
import web.oficinamecanica.ajax.TipoNotificaoAlertify;
import web.oficinamecanica.model.Agendamento;
import web.oficinamecanica.model.Situacao;
import web.oficinamecanica.model.Veiculo;
import jakarta.validation.Valid;
import web.oficinamecanica.model.filter.AgendamentoFilter;
import web.oficinamecanica.model.filter.VeiculoFilter;
import web.oficinamecanica.pagination.PageWrapper;
import web.oficinamecanica.repository.AgendamentoRepository;
import web.oficinamecanica.repository.VeiculoRepository;
import web.oficinamecanica.service.AgendamentoService;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private static final Logger logger = LoggerFactory.getLogger(AgendamentoController.class);

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @GetMapping("/cadastrar")
    public String abrirCadastro(Agendamento agendamento, HttpSession sessao, Model model) {
        sessao.setAttribute("agendamento", new Agendamento());
        return "agendamento/cadastrar";
    }

    @PostMapping("/escolherveiculo")
    public String abrirPesquisarPessoa(Model model) {
        model.addAttribute("url", "/agendamentos/pesquisarveiculo");
        model.addAttribute("uso", "agendamentos");
        return "veiculos/pesquisar";
    }

    @GetMapping("/pesquisarveiculo")
    public String pesquisar(VeiculoFilter filtro, Model model,
            @PageableDefault(size = 5) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Veiculo> pagina = veiculoRepository.filtrar(filtro, pageable);
        if (!pagina.isEmpty()) {
            PageWrapper<Veiculo> paginaWrapper = new PageWrapper<>(pagina, request);
            model.addAttribute("pagina", paginaWrapper);
            model.addAttribute("uso", "agendamentos");
            return "veiculos/veiculos";
        } else {
            return "redirect:/agendamentos/mostrarmensgemnok";
        }
    }

    @GetMapping("/mostrarmensgemnok")
    public String mostrarMensagemCadastroNOK(Model model, Agendamento agendamento, HttpSession sessao) {
        NotificacaoAlertify notificacao = new NotificacaoAlertify("Nenhum veículo cadastrado no sistema!!",
                TipoNotificaoAlertify.ERRO);
        model.addAttribute("notificacao", notificacao);
        return "agendamento/cadastrar";
    }

    @PostMapping("/definirveiculo")
    public String definirPessoa(Long codigoVeiculo, HttpSession sessao, Model model, Agendamento agendamento) {
        Agendamento agenda = (Agendamento) sessao.getAttribute("agendamento");
        Veiculo veiculo = veiculoRepository.findById(codigoVeiculo).orElseThrow();
        agenda.setVeiculo(veiculo);
        sessao.setAttribute("agendamento", agenda);
        return "agendamento/cadastrar";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(HttpSession sessao, Model model, @Valid Agendamento agendamento, BindingResult resultado) {
        Agendamento agenda = (Agendamento) sessao.getAttribute("agendamento");
        LocalDate dataAtual = LocalDate.now();
        if (resultado.hasErrors()) {
            return "agendamento/cadastrar";
        } else {
            if (dataAtual.isBefore(agendamento.getDataAgendamento())
                    && getNextValidDate(agendamento.getDataAgendamento()) == 0) {

                if (agenda.getVeiculo() != null) {
                    agenda.setDataAgendamento(agendamento.getDataAgendamento());
                    agenda.setSituacao(Situacao.AGENDADO);
                    agendamentoService.salvar(agenda);
                    return "redirect:/agendamentos/cadastrook";
                } else {
                    NotificacaoAlertify notificacaoAlertify = new NotificacaoAlertify(
                            "Não foi possível cadastrar o agendamento: faltam dados!",
                            TipoNotificaoAlertify.ERRO);
                    model.addAttribute("notificacao", notificacaoAlertify);
                    return "agendamento/cadastrar";
                }

            } else {
                return "redirect:/agendamentos/cadastroNok";
            }
        }
    }

    @GetMapping("/cadastroNok")
    public String mostrarMensagemCadastroNok(Agendamento agendamento, Model model, HttpSession sessao) {
        NotificacaoAlertify notificacaoAlertify = new NotificacaoAlertify("Agendamento não permitido para esta data!",
                TipoNotificaoAlertify.ERRO);
        model.addAttribute("notificacao", notificacaoAlertify);
        return "agendamento/cadastrar";
    }

    @GetMapping("/cadastrook")
    public String mostrarMensagemCadastroOK(Agendamento agendamento, Model model) {
        NotificacaoAlertify notificacaoAlertify = new NotificacaoAlertify("Agendamento cadastrado com sucesso",
                TipoNotificaoAlertify.SUCESSO);
        model.addAttribute("notificacao", notificacaoAlertify);
        return "agendamento/cadastrar";
    }

    @GetMapping("/abrirpesquisar")
    public String abrirPesquisa(Model model) {
        model.addAttribute("url", "/agendamentos/pesquisar");
        model.addAttribute("uso", "agendamentos");
        return "agendamento/pesquisar";
    }

    @GetMapping("/pesquisar")
    public String pesquisarVeiculos(AgendamentoFilter filtro, Model model,
            @PageableDefault(size = 5) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Agendamento> pagina = agendamentoRepository.filtrar(filtro, pageable);
        if (!pagina.isEmpty()) {
            PageWrapper<Agendamento> paginaWrapper = new PageWrapper<>(pagina, request);
            logger.info("Agendamentos buscados no BD: {}", paginaWrapper.getConteudo());
            model.addAttribute("pagina", paginaWrapper);
            model.addAttribute("uso", "agendamentos");
            return "agendamento/agendamentos";
        } else {
            return "redirect:/agendamentos/mostrarmensgemnokagendamento";
        }
    }

    @GetMapping("/mostrarmensgemnokagendamento")
    public String mostrarMensagemCadastroNOKAgendamento(Model model, Veiculo veiculo) {
        NotificacaoAlertify notificacao = new NotificacaoAlertify("Nenhum agendamento cadastrado no sistema!!",
                TipoNotificaoAlertify.ERRO);
        model.addAttribute("notificacao", notificacao);
        return "agendamento/pesquisar";
    }

    @PostMapping("/abriralterar")
    public String abrirAlterar(Long codigo, Model model, Veiculo veiculo, HttpSession sessao) {
        Optional<Agendamento> optAgendamento = agendamentoRepository.findById(codigo);
        if (optAgendamento.isPresent()) {
            model.addAttribute("agendamento", optAgendamento.get());
            return "agendamento/alterar";
        } else {
            NotificacaoAlertify notificacaoAlertify = new NotificacaoAlertify("Não existe agendamento com código: " + codigo,
                    TipoNotificaoAlertify.ERRO);
            model.addAttribute("notificacao", notificacaoAlertify);
            return "agendamento/pesquisar";
        }
    }

    @PostMapping("/alterar")
    public String alterar(Long codigo, HttpSession sessao, Model model, Agendamento agendamento,
            BindingResult resultado, Veiculo veiculo) {
        Optional<Agendamento> agenda = agendamentoRepository.findById(codigo);
        var a = agenda.get();
        if (a.getVeiculo() != null) {
            a.setSituacao(agendamento.getSituacao());
            agendamentoService.salvar(a);
            return "redirect:/agendamentos/mostrarmensagemalterarok";
        } else {
            NotificacaoAlertify notificacaoAlertify = new NotificacaoAlertify(
                    "Não foi possível atualizar o Veículo: faltam dados!",
                    TipoNotificaoAlertify.ERRO);
            model.addAttribute("notificacao", notificacaoAlertify);
            model.addAttribute("opcao", "veiculos");
            return "agendamento/pesquisar";
        }

    }

    @GetMapping("/updadeNok")
    public String mostrarMensagemUpdateNok(Agendamento agendamento, Model model, HttpSession sessao) {
        NotificacaoAlertify notificacaoAlertify = new NotificacaoAlertify("Agendamento não permitido para esta data!",
                TipoNotificaoAlertify.ERRO);
        model.addAttribute("notificacao", notificacaoAlertify);
        return "agendamento/alterar";
    }

    @GetMapping("/mostrarmensagemalterarok")
    public String mostrarMensagemAlterarOK(Model model) {
        NotificacaoAlertify notificacao = new NotificacaoAlertify("Agendamento alterado com sucesso!",
                TipoNotificaoAlertify.SUCESSO);
        model.addAttribute("notificacao", notificacao);
        return abrirPesquisa(model);
    }

    private static int getNextValidDate(LocalDate date) {
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return 1;
        } else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return 2;
        }
        return 0;
    }
}
