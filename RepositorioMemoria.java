import java.util.ArrayList;
import java.util.List;

public class RepositorioMemoria implements RepositorioContatos {
    private List<Contato> listaContatos;

    public RepositorioMemoria() {
        listaContatos = new ArrayList<>();
    }

    public void inserirContato(Contato contato) {
        listaContatos.add(contato);
    }

    public void atualizarContato(Contato contato) {
        int indice = listaContatos.indexOf(contato);
        if (indice != -1) {
            listaContatos.set(indice, contato);
        }
    }

    public void removerContato(Contato contato) {
        listaContatos.remove(contato);
    }

    public List<Contato> listarContatos() {
        return new ArrayList<>(listaContatos);
    }
}
