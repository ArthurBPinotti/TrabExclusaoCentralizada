import java.text.MessageFormat;

public class Processo {

    private static final int DURACAO_EXECUCAO_RECURSO_MIN = 5000;
    private static final int DURACAO_EXECUCAO_RECURSO_MAX = 15000;
    private static final int INTERVALO_REQUISICAO_RECURSO_MIN = 10000;
    private static final int INTERVALO_REQUISICAO_RECURSO_MAX = 25000;

    private final int id;
    private Thread thread;

    public Processo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void Processa(Recurso recurso) {
        try {
            int duracaoExecucao = getRandomValueBetween(DURACAO_EXECUCAO_RECURSO_MIN, DURACAO_EXECUCAO_RECURSO_MAX);
            System.out.printf("%s | Processo iniciou execução | Tempo %s seg\n", this, recurso, (duracaoExecucao / 1000));
            Thread.sleep(duracaoExecucao);
            System.out.printf("%s | Processo finalizou execução | Tempo %s seg\n", this, recurso, (duracaoExecucao / 1000));
            ExclusaoCentralizada.coordenador.liberarRecurso(new Solicitacao(this, recurso));
        } catch (InterruptedException e) {
            System.out.printf("%s | Processo interrompeu execução do recurso\n", this, recurso);
        }
    }

    public void enviarRequisicaoRecurso() {
        this.thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(getRandomValueBetween(INTERVALO_REQUISICAO_RECURSO_MIN, INTERVALO_REQUISICAO_RECURSO_MAX));
                    Recurso recurso = ExclusaoCentralizada.coordenador.obterRecursoAleatorio();
                    System.out.println("\n-> Processo enviando requisição de acesso ao recurso | " + this + " " + recurso);
                    ExclusaoCentralizada.coordenador.solicitarAcessoRecurso(new Solicitacao(this, recurso));
                } catch (InterruptedException e) {
                    System.out.printf("%s | Processo interrompeu execução\n", this);
                }
            }
        });
        this.thread.start();
    }

    public void interromperProcessamentoAtual() {
        this.thread.interrupt();
    }

    private int getRandomValueBetween(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Processo: [{0}]", String.valueOf(this.getId()));
    }
}