package com.koinRest.entity;

public class SaidaJSON {

	private String origem;
	private String destino;
	private String distancia;
	private String status;
	
	public String getOrigem() {
		return origem;
	}
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getDistancia() {
		return distancia;
	}
	public void setDistancia(String distancia) {
		this.distancia = distancia;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "SaidaJSON [origem=" + origem + ", destino=" + destino
				+ ", distancia=" + distancia + ", status=" + status + "]";
	}
	
}
