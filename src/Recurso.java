import java.text.MessageFormat;

public class Recurso {
    private final int id;
    private boolean usando;

    public Recurso(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean usando() {
        return usando;
    }

    public void setUsando(boolean sendoAcessado) {
        this.usando = sendoAcessado;
    }

    @Override
    public int hashCode() {
        return this.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof Recurso))
            return false;

        if (this == obj)
            return true;

        Recurso recurso = (Recurso) obj;
        return recurso.getId() == this.getId();
    }

    @Override
    public String toString() {
        return MessageFormat.format("Recurso: [{0}]", String.valueOf(this.getId()));
    }
}