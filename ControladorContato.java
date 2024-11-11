import java.util.List;

public class ControladorContato {
    private RepositorioContatos repositorio;

    public ControladorContato(RepositorioContatos repositorio) {
        this.repositorio = repositorio;
    }

    public void criarContato(String nome, String telefone, String email) {
        Contato contato = new Contato(nome, telefone, email);
        repositorio.inserirContato(contato);
    }

    public void modificarContato(Contato contato) {
        repositorio.atualizarContato(contato);
    }

    public void excluirContato(Contato contato) {
        repositorio.removerContato(contato);
    }

    public List<Contato> obterContatos() {
        return repositorio.listarContatos();
    }
}
