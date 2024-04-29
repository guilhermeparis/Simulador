public class Evento {
	private String tipo;
	private double tempo;

	public Evento(String tipo, double tempo) {
		this.tipo = tipo;
		this.tempo = tempo;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public double getTempo() {
		return tempo;
	}

	@Override
	public String toString() {
		return "["+tipo+", "+ tempo+"]";
	}
}