public class Gerador {

	double seed;
	double a;
	double c;
	double M;
	int count;

	public Gerador(double seed, double a, double c, double M) {
		super();
		this.seed = seed;
		this.a = a;
		this.c = c;
		this.M = M;
	}

	public double next() {
		seed = (seed * a + c) % M;
		count++;
		return seed / M;
	}
}