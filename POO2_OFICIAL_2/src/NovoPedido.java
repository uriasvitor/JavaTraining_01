import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class NovoPedido extends JFrame {
    private JComboBox<Integer> cbClienteId;
    private JComboBox<Integer> cbProdutoId;
    private JTextField txtQuantidade;
    private JTextField txtPreco;

    public NovoPedido(Venda parent) {
        setTitle("Novo Pedido");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        setLayout(new GridLayout(5, 2));

        JLabel lblClienteId = new JLabel("Cliente ID:");
        cbClienteId = new JComboBox<>();
        JLabel lblProdutoId = new JLabel("Produto ID:");
        cbProdutoId = new JComboBox<>();
        JLabel lblQuantidade = new JLabel("Quantidade:");
        txtQuantidade = new JTextField();
        JLabel lblPreco = new JLabel("Preço:");
        txtPreco = new JTextField();

        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnCancelar = new JButton("Cancelar");

        add(lblClienteId);
        add(cbClienteId);
        add(lblProdutoId);
        add(cbProdutoId);
        add(lblQuantidade);
        add(txtQuantidade);
        add(lblPreco);
        add(txtPreco);
        add(btnAdicionar);
        add(btnCancelar);

        carregarClientes();
        carregarProdutos();

        btnAdicionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adicionarPedido(parent);
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void carregarClientes() {
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT Id FROM Cliente")) {
            while (rs.next()) {
                cbClienteId.addItem(rs.getInt("Id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void carregarProdutos() {
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT Id FROM Produto")) {
            while (rs.next()) {
                cbProdutoId.addItem(rs.getInt("Id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void adicionarPedido(Venda parent) {
        int clienteId = (int) cbClienteId.getSelectedItem();
        int produtoId = (int) cbProdutoId.getSelectedItem();
        int quantidade;
        double preco;

        try {
            quantidade = Integer.parseInt(txtQuantidade.getText());
            preco = Double.parseDouble(txtPreco.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade ou preço inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);
            try {

                String sqlPedido = "INSERT INTO Pedido (dtCadastro, ClienteId) VALUES (CURRENT_DATE, ?)";
                try (PreparedStatement pstmtPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                    pstmtPedido.setInt(1, clienteId);
                    pstmtPedido.executeUpdate();

                    try (ResultSet generatedKeys = pstmtPedido.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int pedidoId = generatedKeys.getInt(1);

                            String sqlItem = "INSERT INTO Item (PedidoId, ProdutoId, Preco, Quantidade) VALUES (?, ?, ?, ?)";
                            try (PreparedStatement pstmtItem = conn.prepareStatement(sqlItem)) {
                                pstmtItem.setInt(1, pedidoId);
                                pstmtItem.setInt(2, produtoId);
                                pstmtItem.setDouble(3, preco);
                                pstmtItem.setInt(4, quantidade);
                                pstmtItem.executeUpdate();
                            }

                            conn.commit();
                            dispose();
                        } else {
                            throw new SQLException("Falha ao obter o ID do pedido.");
                        }
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao adicionar pedido e item.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
