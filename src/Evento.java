public class Evento {
	private double tempo;
	private String tipo;

	public Evento(double tempo, String tipo) {
		this.tempo = tempo;
		this.tipo = tipo;
	}
	
	public double getTempo() {
		return tempo;
	}

	public String getTipo() {
		return tipo;
	}

	@Override
	public String toString() {
		return "["+tipo+", "+ tempo+"]";
	}
}