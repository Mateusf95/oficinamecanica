package web.oficinamecanica.controler;

import java.util.List;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import web.oficinamecanica.ajax.NotificacaoAlertify;
import web.oficinamecanica.ajax.TipoNotificaoAlertify;
import web.oficinamecanica.model.Status;
import web.oficinamecanica.model.Usuario;
import web.oficinamecanica.model.Veiculo;
import web.oficinamecanica.model.filter.UsuarioFilter;
import web.oficinamecanica.model.filter.VeiculoFilter;
import web.oficinamecanica.pagination.PageWrapper;
import web.oficinamecanica.repository.UsuarioRepository;
import web.oficinamecanica.repository.VeiculoRepository;
import web.oficinamecanica.service.VeiculoService;

@Controller
@RequestMapping("/veiculos")
public class VeiculoController {

    private static final Logger logger = LoggerFactory.getLogger(VeiculoController.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @GetMapping("/cadastrar")
    public String abriCadastro(Model model, Veiculo veiculo, HttpSession sessao) {
        sessao.setAttribute("veiculo", new Veiculo());
        return "veiculos/cadastrar";
    }

    @PostMapping("/escolherusuario")
    public String abrirPesquisarPessoa(Model model) {
        model.addAttribute("url", "/veiculos/pesquisarusuario");
        model.addAttribute("uso", "veiculos");
        return "usuarios/pesquisar";
    }

    @PostMapping("/escolhernovousuario")
    public String abrirPesquisarNocaPessoa(Model model) {
        model.addAttribute("url", "/veiculos/pesquisarnovousuario");
        model.addAttribute("uso", "veiculosnovo");
        return "usuarios/pesquisar";
    }

    @GetMapping("/pesquisarusuario")
    public String pesquisar(UsuarioFilter filtro, Model model,
            @PageableDefault(size = 5) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Usuario> pagina = usuarioRepository.filtrar(filtro, pageable);
        if (!pagina.isEmpty()) {
            PageWrapper<Usuario> paginaWrapper = new PageWrapper<>(pagina, request);
            model.addAttribute("pagina", paginaWrapper);
            model.addAttribute("uso", "veiculos");
            return "usuarios/mostrartodas";
        }else{
            return "redirect:/veiculos/mostrarmensgempesquisanok";
        }
    }

    @GetMapping("/mostrarmensgempesquisanok")
    public String mostrarMensagemPesquisaNOK(Model model, Veiculo veiculo) {
        NotificacaoAlertify notificacao = new NotificacaoAlertify("Nenhuma usuário cadastrado no sistema!!",
                TipoNotificaoAlertify.ERRO);
        model.addAttribute("notificacao", notificacao);
        model.addAttribute("uso", "veiculos");
        return "veiculos/pesquisar";
    }

    @GetMapping("/pesquisarnovousuario")
    public String pesquisarNovoUsuario(UsuarioFilter filtro, Model model,
            @PageableDefault(size = 5) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Usuario> pagina = usuarioRepository.filtrar(filtro, pageable);
        PageWrapper<Usuario> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("uso", "veiculosnovos");
        return "usuarios/mostrartodas";
    }

    @PostMapping("/definirpessoa")
    public String definirPessoa(Long codigoPessoa, HttpSession sessao, Model model, Veiculo veiculo) {
        Veiculo automovel = (Veiculo) sessao.getAttribute("veiculo");
        Usuario usuario = usuarioRepository.findById(codigoPessoa).orElseThrow();
        automovel.setUsuario(usuario);
        sessao.setAttribute("veiculo", automovel);
        return "veiculos/cadastrar";
    }

    @PostMapping("/definirnovapessoa")
    public String definirNovaPessoa(Long codigoPessoa, HttpSession sessao, Model model, Veiculo veiculo) {
        Veiculo automovel = (Veiculo) sessao.getAttribute("veiculo");
        Usuario usuario = usuarioRepository.findById(codigoPessoa).orElseThrow();
        automovel.setUsuario(usuario);
        sessao.setAttribute("veiculo", automovel);
        model.addAttribute("veiculo", automovel);
        return "/veiculos/alterar";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(HttpSession sessao, Model model, @Valid Veiculo veiculo, BindingResult resultado) {
        Veiculo automovel = (Veiculo) sessao.getAttribute("veiculo");
        if (resultado.hasErrors()) {
            return abriCadastro(model, veiculo, sessao);
        } else {
            if (automovel.getUsuario() != null) {
                automovel.setCor(veiculo.getCor());
                automovel.setMarca(veiculo.getMarca());
                automovel.setPlaca(veiculo.getPlaca());
                automovel.setAnoFabricacao(veiculo.getAnoFabricacao());
                automovel.setNome(veiculo.getNome());
                veiculoService.salvar(automovel);
                return "redirect:/veiculos/cadastrook";
            } else {
                NotificacaoAlertify notificacao = new NotificacaoAlertify(
                        "Não foi possível cadastrar o Veículo: faltam dados",
                        TipoNotificaoAlertify.ERRO);
                model.addAttribute("notificacao", notificacao);
                return "veiculos/cadastrar";
            }
        }
    }

    @GetMapping("/cadastrook")
    public String mostrarMensagemCadastroOK(Model model, Veiculo veiculo) {
        NotificacaoAlertify notificacao = new NotificacaoAlertify("Veículo cadastrado com sucesso!",
                TipoNotificaoAlertify.SUCESSO);
        model.addAttribute("notificacao", notificacao);
        return "veiculos/cadastrar";
    }

    @GetMapping("/abrirpesquisar")
    public String abrirPesquisa(Model model) {
        colocarUsuarioNoModel(model);
        model.addAttribute("url", "/veiculos/pesquisar");
        model.addAttribute("uso", "veiculos");
        return "veiculos/pesquisar";
    }

    private void colocarUsuarioNoModel(Model model) {
        List<Usuario> usuarios = usuarioRepository.findByStatus(Status.ATIVO);
        model.addAttribute("usuarios", usuarios);
    }

    @GetMapping("/pesquisar")
    public String pesquisarVeiculos(VeiculoFilter filtro, Model model,
            @PageableDefault(size = 5) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Veiculo> pagina = veiculoRepository.filtrar(filtro, pageable);
        if (!pagina.isEmpty()) {
            PageWrapper<Veiculo> paginaWrapper = new PageWrapper<>(pagina, request);
            logger.info("Veículos buscados no BD: {}", paginaWrapper.getConteudo());
            model.addAttribute("pagina", paginaWrapper);
            model.addAttribute("uso", "veiculos");
            return "veiculos/veiculos";
        }else{
            return "redirect:/veiculos/mostrarmensgemnok";
        }
    }

    @GetMapping("/mostrarmensgemnok")
    public String mostrarMensagemCadastroNOK(Model model, Veiculo veiculo) {
        NotificacaoAlertify notificacao = new NotificacaoAlertify("Nenhum veículo cadastrado no sistema!!",
                TipoNotificaoAlertify.ERRO);
        model.addAttribute("notificacao", notificacao);
        return "veiculos/pesquisar";
    }

    @PostMapping("/abriralterar")
    public String abrirAlterar(Long codigo, Model model, Veiculo veiculo, HttpSession sessao) {
        Optional<Veiculo> optVeiculo = veiculoRepository.findById(codigo);
        sessao.setAttribute("veiculo", optVeiculo.get());
        if (optVeiculo.isPresent()) {
            model.addAttribute("veiculo", optVeiculo.get());
            return "veiculos/alterar";
        } else {
            model.addAttribute("opcao", "veiculos");
            model.addAttribute("mensagem", "Não existe veiculos com código: " + codigo);
            return "mostrarmensagem";
        }
    }

    @PutMapping("/alterar")
    public String alterar(Long codigo, HttpSession sessao, Model model, @ Valid Veiculo veiculo, BindingResult resultado) {
        Optional<Veiculo> automovel = veiculoRepository.findById(codigo);
        Veiculo car = (Veiculo) sessao.getAttribute("veiculo");
        var carro = automovel.get();
        if(resultado.hasErrors()){
            return abrirAlterar(codigo, model, veiculo, sessao);
        }else{
            if (carro.getUsuario() != null) {
                carro.setCor(veiculo.getCor());
                carro.setMarca(veiculo.getMarca());
                carro.setPlaca(veiculo.getPlaca());
                carro.setAnoFabricacao(veiculo.getAnoFabricacao());
                carro.setNome(veiculo.getNome());
                carro.setUsuario(car.getUsuario());
                veiculoService.salvar(carro);
                return "redirect:/veiculos/mostrarmensagemalterarok";
            } else {
                model.addAttribute("mensagem", "Não foi possível atualizar o Veículo: faltam dados");
                model.addAttribute("opcao", "veiculos");
                return "mostrarmensagem";
            }
        }
    }

    @GetMapping("/mostrarmensagemalterarok")
    public String mostrarMensagemAlterarOK(Model model) {
        NotificacaoAlertify notificacao = new NotificacaoAlertify("Veiculo alterado com sucesso!",
                TipoNotificaoAlertify.SUCESSO);
        model.addAttribute("notificacao", notificacao);
        model.addAttribute("url", "/veiculos/pesquisar");
        model.addAttribute("uso", "veiculos");
        return "veiculos/pesquisar";
    }

    @PostMapping("/remover")
    public String remover(Long codigo, Model model) {
        Optional<Veiculo> optVeiculo = veiculoRepository.findById(codigo);
        if (optVeiculo.isPresent()) {
            Veiculo veiculo = optVeiculo.get();
            veiculo.setStatus(Status.INATIVO);
            veiculoService.salvar(veiculo);
            return "redirect:/veiculos/remocaook";
        } else {
            model.addAttribute("mensagem", "Não foi encontrado veículos com esse código");
            model.addAttribute("opcao", "veiculos");
            return "mostrarmensagem";
        }
    }

    @GetMapping("/remocaook")
    public String mostrarMensagemRemocaoOK(Model model) {
        NotificacaoAlertify notificacaoAlertify = new NotificacaoAlertify("Veiculo removido com sucesso",
                TipoNotificaoAlertify.SUCESSO);
        model.addAttribute("notificacao", notificacaoAlertify);
        colocarUsuarioNoModel(model);
        model.addAttribute("uso", "veiculos");
        model.addAttribute("url", "/veiculos/pesquisar");
        return "veiculos/pesquisar";
    }

}
