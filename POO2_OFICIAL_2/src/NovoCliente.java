import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NovoCliente extends JFrame {

    public NovoCliente(Cliente parent) {
        setTitle("Novo Cliente");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        setLayout(new GridLayout(3, 2));

        JLabel lblNome = new JLabel("Nome:");
        JTextField txtNome = new JTextField();
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        add(lblNome);
        add(txtNome);
        add(btnSalvar);
        add(btnCancelar);

        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nome = txtNome.getText();
                if (!nome.isEmpty()) {
                    try (Connection conn = Conexao.conectar();
                         PreparedStatement pstmt = conn.prepareStatement("INSERT INTO cliente (nome, dtCadastro) VALUES (?, CURRENT_DATE)")) {

                        pstmt.setString(1, nome);
                        pstmt.executeUpdate();
                        parent.carregarClientes();
                        dispose();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(NovoCliente.this, "Nome não pode estar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }
}
