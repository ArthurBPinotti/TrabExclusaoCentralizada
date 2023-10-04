import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;

public class ExclusaoCentralizada  extends Thread {
    private final int TEMPO_PARA_ENCERRAR_COORDENADOR = 60000;
    private final int TEMPO_PARA_CRIAR_PROCESSO = 40000;

    private final List<Processo> processos;
    public static Coordenador coordenador;
    public final  Map<Recurso, Queue<Solicitacao>> solicitacoesRecursos;


    @Override
    public void run() {
        System.out.println("Inicializando Algoritmo de Exclusão Mútua");
        this.encerrarCoordenador();
        this.criarProcesso();
    }

    public ExclusaoCentralizada (Map<Recurso, Queue<Solicitacao>> solicitacoesRecursos) {
        this.solicitacoesRecursos = solicitacoesRecursos;
        this.processos = new ArrayList<>();
        criarNovoCoordenador();
    }

    private void criarProcesso() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(TEMPO_PARA_CRIAR_PROCESSO);
                    Processo processo = new Processo(gerarNovoIdProcesso());
                    processo.enviarRequisicaoRecurso();
                    this.processos.add(processo);
                    System.out.println("\n-> Criou um novo processo | " + processo);
                    imprimirProcessos();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void encerrarCoordenador() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(TEMPO_PARA_ENCERRAR_COORDENADOR);
                    System.out.println("\n-> Coordenador finalizado ");
                    criarNovoCoordenador();
                    for (Processo processo : this.processos) {
                        processo.interromperProcessamentoAtual();
                        processo.enviarRequisicaoRecurso();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void criarNovoCoordenador() {
        coordenador = new Coordenador(this.solicitacoesRecursos);
    }

    private int gerarNovoIdProcesso() {
        Random gerador = new Random();
        int novoId;
        do {
            novoId = gerador.nextInt(1000);
        } while (idInvalido(novoId));
        return novoId;
    }

    private boolean idInvalido(int id) {
        return id == 0 || this.processos.stream().anyMatch(p -> p.getId() == id);
    }

    private void imprimirProcessos() {
        String mensagem = this.processos.stream().map(x -> String.valueOf(x.getId())).collect(Collectors.joining(", "));
        System.out.println("Processos: [" + mensagem + "]");
    }

    
}