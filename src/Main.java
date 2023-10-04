import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {
    public static Map<Recurso, Queue<Solicitacao>> solicitacoesRecursos = new ConcurrentHashMap<>();;

    public static void main(String[] args) {
        solicitacoesRecursos.put(new Recurso(1), new ConcurrentLinkedQueue<>());
        solicitacoesRecursos.put(new Recurso(2), new ConcurrentLinkedQueue<>());
        
        new ExclusaoCentralizada(Main.solicitacoesRecursos).start();
    }
}
