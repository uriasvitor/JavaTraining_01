import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Date;

public class Venda extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cbClientes;
    private JComboBox<String> cbProdutos;
    private JTextField txtQuantidade;
    private JTextField txtPreco;

    public Venda() {
        setTitle("Vendas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel panelCombos = new JPanel(new FlowLayout());
        JLabel lblClientes = new JLabel("Cliente:");
        cbClientes = new JComboBox<>();
        JLabel lblProdutos = new JLabel("Produto:");
        cbProdutos = new JComboBox<>();
        JLabel lblQuantidade = new JLabel("Quantidade:");
        txtQuantidade = new JTextField(5);
        JLabel lblPreco = new JLabel("Preço:");
        txtPreco = new JTextField(5);
        panelCombos.add(lblClientes);
        panelCombos.add(cbClientes);
        panelCombos.add(lblProdutos);
        panelCombos.add(cbProdutos);
        panelCombos.add(lblQuantidade);
        panelCombos.add(txtQuantidade);
        panelCombos.add(lblPreco);
        panelCombos.add(txtPreco);
        add(panelCombos, BorderLayout.NORTH);

        JPanel panelButtons = new JPanel(new FlowLayout());
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnAtualizar = new JButton("Atualizar");
        panelButtons.add(btnAdicionar);
        panelButtons.add(btnAtualizar);
        add(panelButtons, BorderLayout.CENTER);

        tableModel = new DefaultTableModel(new String[]{"ID Pedido", "Data", "Cliente", "Produto", "Preço", "Quantidade", "Total"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.SOUTH);

        btnAdicionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adicionarPedido();
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                carregarVendas();
            }
        });

        carregarClientes();
        carregarProdutos();
        carregarVendas();

        setVisible(true);
    }

    private void carregarClientes() {
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT nome FROM Cliente")) {
            while (rs.next()) {
                cbClientes.addItem(rs.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void carregarProdutos() {
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT descricao FROM Produto")) {
            while (rs.next()) {
                cbProdutos.addItem(rs.getString("descricao"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void adicionarPedido() {
        String cliente = (String) cbClientes.getSelectedItem();
        String produto = (String) cbProdutos.getSelectedItem();
        int quantidade;
        double preco;

        try {
            quantidade = Integer.parseInt(txtQuantidade.getText());
            preco = Double.parseDouble(txtPreco.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade ou preço inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int clienteId = obterClienteId(cliente);
        int produtoId = obterProdutoId(produto);

        if (clienteId == -1 || produtoId == -1) {
            JOptionPane.showMessageDialog(this, "Erro ao obter ID do cliente ou produto.", "Erro", JOptionPane.ERROR_MESSAGE);
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
                            carregarVendas();
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

    private int obterClienteId(String nome) {
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement("SELECT Id FROM Cliente WHERE nome = ?")) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int obterProdutoId(String descricao) {
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement("SELECT Id FROM Produto WHERE descricao = ?")) {
            stmt.setString(1, descricao);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void carregarVendas() {
        tableModel.setRowCount(0);
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT p.Id AS PedidoId, p.dtCadastro, c.nome AS Cliente, pr.descricao AS Produto, i.Preco, i.quantidade, (i.Preco * i.quantidade) AS Total " +
                             "FROM Pedido p " +
                             "JOIN Cliente c ON p.ClienteId = c.Id " +
                             "JOIN Item i ON p.Id = i.PedidoId " +
                             "JOIN Produto pr ON i.ProdutoId = pr.Id " +
                             "WHERE p.dtCadastro = CURRENT_DATE " +
                             "ORDER BY p.dtCadastro, pr.descricao, c.nome")) {

            while (rs.next()) {
                int pedidoId = rs.getInt("PedidoId");
                Date data = rs.getDate("dtCadastro");
                String cliente = rs.getString("Cliente");
                String produto = rs.getString("Produto");
                double preco = rs.getDouble("Preco");
                int quantidade = rs.getInt("quantidade");
                double total = rs.getDouble("Total");
                tableModel.addRow(new Object[]{pedidoId, data, cliente, produto, preco, quantidade, total});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Venda().setVisible(true);
            }
        });
    }
}
