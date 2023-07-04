package web.oficinamecanica.model.filter;

public class VeiculoFilter {
    private Long codigo;
	private String nome;
    private String cor;
    private String placa;
    private String anoFabricaca;
    private String marca;
    private String nomeUsuario;
    
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public String getCor() {
		return cor;
	}
	public void setCor(String cor) {
		this.cor = cor;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public String getAnoFabricaca() {
		return anoFabricaca;
	}
	public void setAnoFabricaca(String anoFabricaca) {
		this.anoFabricaca = anoFabricaca;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	@Override
	public String toString() {
		return "VeiculoFilter [codigo=" + codigo + ", nome=" + nome + ", cor=" + cor + ", placa=" + placa
				+ ", anoFabricaca=" + anoFabricaca + ", marca=" + marca + ", nomeUsuario=" + nomeUsuario + "]";
	}
	

    
}
