import java.util.Comparator;
import java.util.PriorityQueue;

public class Escalonador {
	
    Comparator<Evento> eventoComparator = Comparator.comparingDouble(Evento::getTempo);
    PriorityQueue<Evento> eventoQueue = new PriorityQueue<>(eventoComparator);
}


