public class Gerador {

	double seed;
	double a;
	double c;
	double M;


	public Gerador(double seed, double a, double c, double M) {
		super();
		this.seed = seed;
		this.a = a;
		this.c = c;
		this.M = M;
	}


	public double next() {
		System.out.println("X: " + seed + " a: " + a + " c: " + c + " M: " + M);
		seed = (seed * a + c) % M;
		return seed / M;
	}
}

