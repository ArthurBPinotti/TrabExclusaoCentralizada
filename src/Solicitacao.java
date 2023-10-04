import java.text.MessageFormat;

public class Solicitacao {
    private final Processo processo;
    private final Recurso recurso;

    public Solicitacao(Processo processo, Recurso recurso) {
        this.processo = processo;
        this.recurso = recurso;
    }

    public Processo getProcesso() {
        return processo;
    }

    public Recurso getRecurso() {
        return recurso;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0} {1}", this.getProcesso().toString(), this.getRecurso().toString());
    }
}