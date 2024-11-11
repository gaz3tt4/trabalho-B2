import java.util.List;

public interface RepositorioContatos {
    void inserirContato(Contato contato);
    void atualizarContato(Contato contato);
    void removerContato(Contato contato);
    List<Contato> listarContatos();
}
