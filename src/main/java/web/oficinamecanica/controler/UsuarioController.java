package web.oficinamecanica.controler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Sort;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.oficinamecanica.ajax.NotificacaoAlertify;
import web.oficinamecanica.ajax.TipoNotificaoAlertify;
import web.oficinamecanica.model.Papel;
import web.oficinamecanica.model.Usuario;
import web.oficinamecanica.model.filter.UsuarioFilter;
import web.oficinamecanica.pagination.PageWrapper;
import web.oficinamecanica.repository.PapelRepository;
import web.oficinamecanica.repository.UsuarioRepository;
import web.oficinamecanica.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private PapelRepository papelRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/cadastrar")
    public String abrirCadastro(Usuario usuario, Model model) {
        List<Papel> papeis = papelRepository.findAll();
        model.addAttribute("todosPapeis", papeis);
        return "usuarios/cadastrar";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(Model model, @Valid Usuario usuario,  BindingResult resultado) {
        if (resultado.hasErrors()) {
            List<Papel> papeis = papelRepository.findAll();
            model.addAttribute("todosPapeis", papeis);
            return abrirCadastro(usuario, model);
        } else {
            if (!usuario.getPapeis().isEmpty()) {
                usuario.setAtivo(true);
                usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
                usuarioService.salvar(usuario);
                return "redirect:/usuarios/mostrarmensagemcadastrook";
            } else {
                List<Papel> papeis = papelRepository.findAll();
                model.addAttribute("todosPapeis", papeis);
                return "usuarios/cadastrar";
            }
        }
    }

    @GetMapping("/mostrarmensagemcadastrook")
    public String mostrarMensagemCadastroOk(Model model, Usuario usuario) {
        NotificacaoAlertify notificacaoAlertify = new NotificacaoAlertify("Usuário inserido com sucesso!");
        notificacaoAlertify.setTipo(TipoNotificaoAlertify.SUCESSO);
        notificacaoAlertify.setIntervalo(5);
        model.addAttribute("notificacao", notificacaoAlertify);
        return abrirCadastro(usuario, model);
    }

    @GetMapping("/abrirpesquisar")
    public String abrirPesquisar(Model model) {
        model.addAttribute("url", "/usuarios/pesquisar");
        model.addAttribute("uso", "usuarios");
        return "usuarios/pesquisar";
    }

    @GetMapping("/pesquisar")
    public String pesquisar(UsuarioFilter filtro, Model model,
            @PageableDefault(size = 5) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Usuario> pagina = usuarioRepository.filtrar(filtro, pageable);
        if (!pagina.isEmpty()) {
            PageWrapper<Usuario> paginaWrapper = new PageWrapper<>(pagina, request);
            logger.info("Pessoas buscadas no BD: {}", paginaWrapper.getConteudo());
            model.addAttribute("pagina", paginaWrapper);
            model.addAttribute("uso", "usuarios");
            return "usuarios/mostrartodas";
        }else{
            return "redirect:/usuarios/mostrarmensagemcadastronok";
        }

    }

    @GetMapping("/mostrarmensagemcadastronok")
    public String mostrarMensagemCadastroNOk(Model model, Usuario usuario) {
        NotificacaoAlertify notificacaoAlertify = new NotificacaoAlertify("Nenhum Usuário cadastrado!");
        notificacaoAlertify.setTipo(TipoNotificaoAlertify.ERRO);
        notificacaoAlertify.setIntervalo(5);
        model.addAttribute("notificacao", notificacaoAlertify);
        return "usuarios/pesquisar";
    }

}
