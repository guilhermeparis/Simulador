import java.util.Arrays;

public class Fila {

	int status;
	int servidores;
	int capacidade;
	double chegadaMin;
	double chegadaMax;
	double saidaMin;
	double saidaMax;
	int atendidos;
	int perdas;
	double [] estados;

	public Fila(int servidores, int capacidade, double chegadaMin, double chegadaMax, double saidaMin, double saidaMax) {
		this.servidores = servidores;
		this.capacidade = capacidade;
		this.estados = new double [capacidade+1]; //Estados totais da fila: K+1
		this.chegadaMin = chegadaMin;
		this.chegadaMax = chegadaMax;
		this.saidaMin = saidaMin;
		this.saidaMax = saidaMax;
	}

	public int getStatus() {
		return status;
	}

	public int getCapacidade() {
		return capacidade;
	}

	public int getServidores() {
		return servidores;
	}

	public int getPerdas() {
		return this.perdas;
	}

	public double[] getEstados() {
		return estados;
	}

	public void in(double tempo) {
		estados[status] += tempo;
		System.out.println(estados[status]);
		this.status++;
	}

	public void out(double tempo) {
		estados[status] += tempo;
		System.out.println(estados[status]);
		this.status--;
		this.atendidos++;
	}

	public void loss() {
		this.perdas++;
	}

	@Override
	public String toString() {
		return "Fila ["
				+ "Status=" + status
				+ ", Servidores=" + servidores
				+ ", Capacidade=" + capacidade
				+ ", Atendidos=" + atendidos
				+ ", Perdas=" + perdas
				+ ", Estados=" + Arrays.toString(estados)
				+ "]";
	}
}
