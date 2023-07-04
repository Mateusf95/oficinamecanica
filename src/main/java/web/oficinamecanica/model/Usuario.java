package web.oficinamecanica.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "gerador1", sequenceName = "usuario_codigo_seq", allocationSize = 1)
    @GeneratedValue(generator = "gerador1", strategy = GenerationType.SEQUENCE)
    private Long codigo;
    @NotBlank(message = "O CPF é obrigatorio!")
    private String cpf;
    @NotBlank(message = "O nome é obrigatorio!")
    private String nome;
    @NotBlank(message = "O E-mail é obrigatorio")
    private String email;
    @NotBlank(message = "A senha é obrigatoria!")
    private String senha;
    @Column(name = "nome_usuario")
    @NotBlank(message = "O nome de usuário é obrigatório!")
    private String nomeUsuario;
    @Column(name = "data_nascimento")
    @NotNull(message = "A data de nascimento é obrigatória!")
    private LocalDate dataNascimento;
    private boolean ativo;
    @Enumerated(EnumType.STRING)
	private Status status = Status.ATIVO;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_papel", joinColumns = @JoinColumn(name = "codigo_usuario"), inverseJoinColumns = @JoinColumn(name = "codigo_papel"))
    @Size(min = 1, message = "O usuário deve ter ao menos um papel no sistema")
    private List<Papel> papeis = new ArrayList<>();

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void adicionarPapel(Papel papel) {
		papeis.add(papel);
	}

	public void removerPapel(Papel papel) {
		papeis.remove(papel);
	}

    public List<Papel> getPapeis() {
        return papeis;
    }

    public void setPapeis(List<Papel> papeis) {
        this.papeis = papeis;
    }

    

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
        result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((senha == null) ? 0 : senha.hashCode());
        result = prime * result + ((nomeUsuario == null) ? 0 : nomeUsuario.hashCode());
        result = prime * result + ((dataNascimento == null) ? 0 : dataNascimento.hashCode());
        result = prime * result + (ativo ? 1231 : 1237);
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((papeis == null) ? 0 : papeis.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Usuario other = (Usuario) obj;
        if (codigo == null) {
            if (other.codigo != null)
                return false;
        } else if (!codigo.equals(other.codigo))
            return false;
        if (cpf == null) {
            if (other.cpf != null)
                return false;
        } else if (!cpf.equals(other.cpf))
            return false;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (senha == null) {
            if (other.senha != null)
                return false;
        } else if (!senha.equals(other.senha))
            return false;
        if (nomeUsuario == null) {
            if (other.nomeUsuario != null)
                return false;
        } else if (!nomeUsuario.equals(other.nomeUsuario))
            return false;
        if (dataNascimento == null) {
            if (other.dataNascimento != null)
                return false;
        } else if (!dataNascimento.equals(other.dataNascimento))
            return false;
        if (ativo != other.ativo)
            return false;
        if (status != other.status)
            return false;
        if (papeis == null) {
            if (other.papeis != null)
                return false;
        } else if (!papeis.equals(other.papeis))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Usuario [codigo=" + codigo + ", cpf=" + cpf + ", nome=" + nome + ", email=" + email + ", senha=" + senha
                + ", nomeUsuario=" + nomeUsuario + ", dataNascimento=" + dataNascimento + ", ativo=" + ativo
                + ", status=" + status + ", papeis=" + papeis + "]";
    }

   

    

}
