package web.oficinamecanica.model.filter;

import java.time.LocalDate;

import web.oficinamecanica.model.Situacao;

public class AgendamentoFilter {
    private Long codigo;
    private LocalDate dataAgendamento;
    private String veiculo;
    private Situacao situacao;
    
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
    public String getVeiculo() {
        return veiculo;
    }
    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }
    public Situacao getSituacao() {
        return situacao;
    }
    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }
    @Override
    public String toString() {
        return "AgendamentoFilter [codigo=" + codigo + ", dataAgendamento=" + dataAgendamento + ", veiculo=" + veiculo
                + ", situacao=" + situacao + "]";
    }


    
}
