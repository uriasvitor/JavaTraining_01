import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NovoProduto extends JFrame {

    public NovoProduto(Produto parent) {
        setTitle("Novo Produto");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        setLayout(new GridLayout(4, 2));

        JLabel lblDescricao = new JLabel("Descrição:");
        JTextField txtDescricao = new JTextField();
        JLabel lblPreco = new JLabel("Preço:");
        JTextField txtPreco = new JTextField();
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        add(lblDescricao);
        add(txtDescricao);
        add(lblPreco);
        add(txtPreco);
        add(btnSalvar);
        add(btnCancelar);

        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String descricao = txtDescricao.getText();
                String precoStr = txtPreco.getText();
                if (!descricao.isEmpty() && !precoStr.isEmpty()) {
                    try {
                        double preco = Double.parseDouble(precoStr);
                        try (Connection conn = Conexao.conectar();
                             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Produto (descricao, preco) VALUES (?, ?)")) {

                            pstmt.setString(1, descricao);
                            pstmt.setDouble(2, preco);
                            pstmt.executeUpdate();
                            parent.carregarProdutos();
                            dispose();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(NovoProduto.this, "Preço inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(NovoProduto.this, "Descrição e Preço não podem estar vazios.", "Erro", JOptionPane.ERROR_MESSAGE);
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
