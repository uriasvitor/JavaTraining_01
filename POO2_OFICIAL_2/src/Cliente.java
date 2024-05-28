import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Cliente extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public Cliente() {
        setTitle("Clientes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel panelButtons = new JPanel();
        JButton btnNovo = new JButton("Novo");
        JButton btnPesquisar = new JButton("Pesquisar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar");
        panelButtons.add(btnNovo);
        panelButtons.add(btnPesquisar);
        panelButtons.add(btnExcluir);
        panelButtons.add(btnAtualizar);
        add(panelButtons, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Data de Cadastro"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnNovo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new NovoCliente(Cliente.this);
            }
        });

        btnPesquisar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pesquisarCliente();
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                excluirCliente();
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                carregarClientes();
            }
        });

        carregarClientes();

        setVisible(true);
    }

    public void carregarClientes() {
        tableModel.setRowCount(0);
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM cliente")) {

            while (rs.next()) {
                int id = rs.getInt("Id");
                String nome = rs.getString("nome");
                Date dtCadastro = rs.getDate("dtCadastro");
                tableModel.addRow(new Object[]{id, nome, dtCadastro});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void pesquisarCliente() {
        String id = JOptionPane.showInputDialog(this, "Digite o ID ou Nome do cliente:");
        if (id != null && !id.isEmpty()) {
            tableModel.setRowCount(0);
            try (Connection conn = Conexao.conectar();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Cliente WHERE Id = ? OR nome LIKE ?")) {

                try {
                    pstmt.setInt(1, Integer.parseInt(id));
                } catch (NumberFormatException e) {
                    pstmt.setInt(1, -1);
                }
                pstmt.setString(2, "%" + id + "%");
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int clienteId = rs.getInt("Id");
                        String nome = rs.getString("nome");
                        Date dtCadastro = rs.getDate("dtCadastro");
                        tableModel.addRow(new Object[]{clienteId, nome, dtCadastro});
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void excluirCliente() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o cliente selecionado?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = Conexao.conectar()) {
                    conn.setAutoCommit(false);
                    try {
                        String sqlDeletarItens = "DELETE FROM item WHERE PedidoId IN (SELECT Id FROM pedido WHERE ClienteId = ?)";
                        try (PreparedStatement pstmtDeletarItens = conn.prepareStatement(sqlDeletarItens)) {
                            pstmtDeletarItens.setInt(1, id);
                            pstmtDeletarItens.executeUpdate();
                        }

                        String sqlDeletarPedidos = "DELETE FROM pedido WHERE ClienteId = ?";
                        try (PreparedStatement pstmtDeletarPedidos = conn.prepareStatement(sqlDeletarPedidos)) {
                            pstmtDeletarPedidos.setInt(1, id);
                            pstmtDeletarPedidos.executeUpdate();
                        }

                        String sqlDeletarCliente = "DELETE FROM cliente WHERE Id = ?";
                        try (PreparedStatement pstmtDeletarCliente = conn.prepareStatement(sqlDeletarCliente)) {
                            pstmtDeletarCliente.setInt(1, id);
                            pstmtDeletarCliente.executeUpdate();
                        }

                        conn.commit();
                        carregarClientes();
                    } catch (SQLException e) {
                        conn.rollback();
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Erro ao excluir o cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir.");
        }
    }
}
