package web.oficinamecanica.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(configurer -> configurer
						// Qualquer um pode fazer requisições para essas URLs
						.requestMatchers("/css/**", "/js/**", "/images/**", "/", "/index.html").permitAll()
						// Um usuário autenticado e com o papel ADMIN e USUARIO pode fazer requisições
						// para essas
						// URLs
						// Usuarios
						.requestMatchers("/usuarios/cadastrar").hasRole("ADMIN")
						.requestMatchers("/usuarios/abrirpesquisar").hasAnyRole("ADMIN")

						// Veiculos
						.requestMatchers("/veiculos/cadastrar").hasRole("ADMIN")
						.requestMatchers("/veiculos/pesquisar").hasAnyRole("ADMIN", "USUARIO")
						.requestMatchers("/veiculos/abriralterar").hasAnyRole("ADMIN")
						.requestMatchers("/veiculos/alterar").hasAnyRole("ADMIN")
						.requestMatchers("/veiculos/remover").hasAnyRole("ADMIN")
						.requestMatchers("/veiculos/abrirpesquisar").hasAnyRole("ADMIN", "USUARIO")

						// Agendamentos
						.requestMatchers("/agendamentos/cadastrar").hasAnyRole("ADMIN")
						.requestMatchers("/agendamentos/abriralterar").hasAnyRole("ADMIN")
						.requestMatchers("/agendamentos/alterar").hasAnyRole("ADMIN")

						// Relatorios
						.requestMatchers("/relatorios/veiculocomagendamentos").hasAnyRole("ADMIN")
						.requestMatchers("/relatorios/veiculocomagendamentosselecionado").hasAnyRole("ADMIN")

						.requestMatchers("URL").hasAnyRole("ADMIN", "USUARIO")
						.anyRequest().permitAll())
				.formLogin(form -> form
						.loginPage("/login")
						.defaultSuccessUrl("/")
						.permitAll())
				.logout(logout -> logout
						.permitAll()
						.logoutSuccessUrl("/"));
		return http.build();

	}

	@Bean
	public UserDetailsService userDetailsService(DataSource dataSource) {
		JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
		manager.setUsersByUsernameQuery("SELECT nome_usuario, senha, ativo FROM usuario WHERE nome_usuario = ?");
		manager.setAuthoritiesByUsernameQuery(
				"SELECT u.nome_usuario, p.nome FROM usuario u " +
						"INNER JOIN usuario_papel up ON u.codigo = up.codigo_usuario " +
						"INNER JOIN papel p ON up.codigo_papel = p.codigo " +
						"WHERE u.nome_usuario = ?");
		return manager;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		// Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();
		// return passwordEncoder;

		// Usar um PasswordEncoder que aceite todos os esquemas disponiveis no Spring
		// Security
		// mas escolhendo quais vamos aceitar. As senhas comecam dizendo qual o esquema
		// usado {noop}, {bcrypt}, {argon2}
		String idEnconder = "argon2";
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
		encoders.put("noop", NoOpPasswordEncoder.getInstance());
		// encoders.put("bcrypt", new BCryptPasswordEncoder());
		// encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		// encoders.put("scrypt", new SCryptPasswordEncoder());
		// encoders.put("sha256", new StandardPasswordEncoder());
		PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idEnconder, encoders);
		return passwordEncoder;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// auth.inMemoryAuthentication()
		// 		.withUser("mateusferreira").password("{noop}123456").roles("ADMIN");
	}

}
