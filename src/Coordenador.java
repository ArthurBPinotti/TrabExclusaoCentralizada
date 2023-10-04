import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Coordenador {
    private final Map<Recurso, Queue<Solicitacao>> solicitacoesRecursos;
    
    public Coordenador(Map<Recurso, Queue<Solicitacao>> solicitacoesRecursos) {
        this.solicitacoesRecursos = solicitacoesRecursos;
    }

    public void solicitarAcessoRecurso(Solicitacao solicitacao) {
        Recurso recurso = solicitacao.getRecurso();
        Queue<Solicitacao> filaSolicitacoes = this.solicitacoesRecursos.computeIfAbsent(recurso, k -> new ConcurrentLinkedQueue<>());

        if (recurso.usando()) {
            System.out.println(" - Solicitação de acesso negada");
            filaSolicitacoes.add(solicitacao);
            imprimirFilasRecursos();
        } else {
            System.out.println(" - Solicitação de acesso aceita");
            recurso.setUsando(true);
            solicitacao.getProcesso().Processa(recurso);
        }
    }

    public void liberarRecurso(Solicitacao solicitacao) {
        Recurso recurso = solicitacao.getRecurso();
        recurso.setUsando(false);
        System.out.println(solicitacao + " - Recurso foi liberado pelo processo");

        Queue<Solicitacao> filaSolicitacoes = this.solicitacoesRecursos.get(recurso);
        if (filaSolicitacoes != null && !filaSolicitacoes.isEmpty()) {
            imprimirFilasRecursos();
            Solicitacao proximaSolicitacao = filaSolicitacoes.remove();
            System.out.println(" - Próximo processo na fila: " + proximaSolicitacao);
            recurso.setUsando(true);
            proximaSolicitacao.getProcesso().Processa(recurso);
        }
    }

    public Recurso obterRecursoAleatorio() {
        if (this.solicitacoesRecursos.isEmpty()) {
            return null;
        }

        List<Recurso> recursos = new ArrayList<>(this.solicitacoesRecursos.keySet());
        int índiceAleatório = new Random().nextInt(recursos.size());
        return recursos.get(índiceAleatório);
    }

    private void imprimirFilasRecursos() {
        StringBuilder sb = new StringBuilder();
        this.solicitacoesRecursos.forEach((recurso, filaSolicitacoes) -> {
            sb.append("Fila de espera para o recurso ").append(recurso).append(" -> ");
            for (Solicitacao solicitacao : filaSolicitacoes) {
                sb.append(solicitacao.getProcesso()).append(", ");
            }
            sb.append("\n");
        });
        System.out.print(sb);
    }
}