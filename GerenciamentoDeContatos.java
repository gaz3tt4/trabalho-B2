import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GerenciamentoDeContatos {
    private ControladorContato controlador;
    private JFrame janelaPrincipal;
    private JTable tabelaContatos;
    private DefaultTableModel modeloTabela;
    private JTextField campoNome;
    private JTextField campoTelefone;
    private JTextField campoEmail;

    public GerenciamentoDeContatos(ControladorContato controlador) {
        this.controlador = controlador;
        iniciarInterface();
    }

    private void iniciarInterface() {
        janelaPrincipal = new JFrame("Agenda de Contatos - Projeto");
        janelaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janelaPrincipal.setSize(600, 500);
        janelaPrincipal.setLayout(new BorderLayout());
        
        // Painel principal de entrada com bordas e cores
        JPanel painelEntrada = new JPanel();
        painelEntrada.setLayout(new BoxLayout(painelEntrada, BoxLayout.Y_AXIS));
        painelEntrada.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        campoNome = criarCampoTexto("Nome Completo:", painelEntrada);
        campoTelefone = criarCampoTexto("Número de Telefone:", painelEntrada);
        campoEmail = criarCampoTexto("Endereço de Email:", painelEntrada);

        janelaPrincipal.add(painelEntrada, BorderLayout.WEST);

        // Painel de tabela
        modeloTabela = new DefaultTableModel(new Object[]{"Nome", "Telefone", "Email"}, 0);
        tabelaContatos = new JTable(modeloTabela);
        tabelaContatos.setPreferredScrollableViewportSize(new Dimension(300, 200));
        JScrollPane painelTabela = new JScrollPane(tabelaContatos);
        janelaPrincipal.add(painelTabela, BorderLayout.CENTER);

        // Painel de botões com alinhamento horizontal
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoExcluir = new JButton("Excluir");
        JButton botaoLimpar = new JButton("Limpar Campos");

        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoLimpar);

        janelaPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        // Configuração dos botões com ActionListeners
        botaoSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarContato();
            }
        });

        botaoExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirContato();
            }
        });

        botaoLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });

        aplicarMascaraTelefone();
        carregarContatos();

        janelaPrincipal.setVisible(true);
    }

    private JTextField criarCampoTexto(String labelTexto, JPanel painel) {
        JLabel label = new JLabel(labelTexto);
        JTextField campoTexto = new JTextField();
        campoTexto.setPreferredSize(new Dimension(200, 20)); // Ajuste a altura para 20 pixels
        painel.add(label);
        painel.add(campoTexto);
        painel.add(Box.createVerticalStrut(10));
        return campoTexto;
    }

    private void salvarContato() {
        String nome = campoNome.getText();
        String telefone = campoTelefone.getText();
        String email = campoEmail.getText();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(janelaPrincipal, "O nome é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!validarEmail(email)) {
            JOptionPane.showMessageDialog(janelaPrincipal, "Email inválido. Verifique o formato.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        controlador.criarContato(nome, telefone, email);
        carregarContatos();
        limparCampos();
    }

    private void excluirContato() {
        int linhaSelecionada = tabelaContatos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            List<Contato> contatos = controlador.obterContatos();
            Contato contato = contatos.get(linhaSelecionada);
            controlador.excluirContato(contato);
            carregarContatos();
        } else {
            JOptionPane.showMessageDialog(janelaPrincipal, "Selecione um contato para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        campoNome.setText("");
        campoTelefone.setText("");
        campoEmail.setText("");
    }

    private void carregarContatos() {
        modeloTabela.setRowCount(0);
        List<Contato> contatos = controlador.obterContatos();
        for (Contato contato : contatos) {
            modeloTabela.addRow(new Object[]{contato.getNome(), contato.getTelefone(), contato.getEmail()});
        }
    }

    private boolean validarEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void aplicarMascaraTelefone() {
        campoTelefone.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String texto = campoTelefone.getText().replaceAll("[^0-9]", "");
                if (texto.length() > 11) {
                    texto = texto.substring(0, 11);
                }

                if (texto.length() == 10) {
                    texto = "(" + texto.substring(0, 2) + ") " + texto.substring(2, 6) + "-" + texto.substring(6);
                } else if (texto.length() == 11) {
                    texto = "(" + texto.substring(0, 2) + ") " + texto.substring(2, 7) + "-" + texto.substring(7);
                } else if (texto.length() > 0) {
                    if (texto.length() <= 2) {
                        texto = "(" + texto;
                    } else if (texto.length() > 2 && texto.length() <= 6) {
                        texto = "(" + texto.substring(0, 2) + ") " + texto.substring(2);
                    } else if (texto.length() > 6) {
                        texto = "(" + texto.substring(0, 2) + ") " + texto.substring(2, 6) + "-" + texto.substring(6);
                    }
                }

                campoTelefone.setText(texto);
            }
        });

        campoTelefone.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String texto = campoTelefone.getText().replaceAll("[^0-9]", "");
                if (texto.length() >= 11) {
                    e.consume();
                }
            }
        });
    }

    public static void main(String[] args) {
        RepositorioContatos repositorio = new RepositorioMemoria();
        ControladorContato controlador = new ControladorContato(repositorio);
        new GerenciamentoDeContatos(controlador); // Inicia a interface gráfica
    }
}
