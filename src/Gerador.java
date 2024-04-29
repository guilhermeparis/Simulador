public class Gerador {

	double seed;
	double a;
	double c;
	double M;
	int count = 0; //conta a quantidade de n�meros pseudoaleat�rios gerados para controle da simula��o.


	public Gerador(double seed, double a, double c, double M) {
		super();
		this.seed = seed;
		this.a = a;
		this.c = c;
		this.M = M;
	}


	public double next() {
		System.out.print("X: " + seed + " a: " + a + " c: " + c + " M: " + M);
		seed = (seed * a + c) % M;
		count++;
		return seed / M;
	}
}

