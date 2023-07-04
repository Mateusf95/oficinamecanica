package web.oficinamecanica.model;

public enum Situacao {
    AGENDADO("agendado"),
    CONCLUIDO("concluido"),
	CANCELADO("cancelado");

	private String descricao;
	
	private Situacao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}
