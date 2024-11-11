public class Main {
    public static void main(String[] args) {
        RepositorioContatos repositorio = new RepositorioMemoria();
        ControladorContato controlador = new ControladorContato(repositorio);
        new GerenciamentoDeContatos(controlador); // Inicia a interface gráfica
    }
}
