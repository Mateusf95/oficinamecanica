package web.oficinamecanica.model;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "agendamento")
public class Agendamento implements Serializable{
    @Id
	@SequenceGenerator(name = "gerador3", sequenceName = "agendamento_codigo_seq", allocationSize = 1)
	@GeneratedValue(generator = "gerador3", strategy = GenerationType.SEQUENCE)
    private Long codigo;
    @NotNull(message = "A data de Agendamento é obrigatória!")
    private LocalDate dataAgendamento;
    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codigo_veiculo")
    private Veiculo veiculo;
    @Enumerated(EnumType.STRING)
	private Situacao situacao = Situacao.AGENDADO;
    
    public Long getCodigo() {
        return codigo;
    }
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }
    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }
    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }
    public Veiculo getVeiculo() {
        return veiculo;
    }
    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }
    public Situacao getSituacao() {
        return situacao;
    }
    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
        result = prime * result + ((dataAgendamento == null) ? 0 : dataAgendamento.hashCode());
        result = prime * result + ((veiculo == null) ? 0 : veiculo.hashCode());
        result = prime * result + ((situacao == null) ? 0 : situacao.hashCode());
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
        Agendamento other = (Agendamento) obj;
        if (codigo == null) {
            if (other.codigo != null)
                return false;
        } else if (!codigo.equals(other.codigo))
            return false;
        if (dataAgendamento == null) {
            if (other.dataAgendamento != null)
                return false;
        } else if (!dataAgendamento.equals(other.dataAgendamento))
            return false;
        if (veiculo == null) {
            if (other.veiculo != null)
                return false;
        } else if (!veiculo.equals(other.veiculo))
            return false;
        if (situacao != other.situacao)
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "Agendamento [codigo=" + codigo + ", dataAgendamento=" + dataAgendamento + ", veiculo=" + veiculo
                + ", situacao=" + situacao + "]";
    }

    
}
