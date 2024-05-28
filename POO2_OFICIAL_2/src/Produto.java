import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Produto extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public Produto() {
        setTitle("Produtos");
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

        tableModel = new DefaultTableModel(new String[]{"ID", "Descrição", "Preço"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnNovo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new NovoProduto(Produto.this);
            }
        });

        btnPesquisar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pesquisarProduto();
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                excluirProduto();
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                carregarProdutos();
            }
        });

        carregarProdutos();

        setVisible(true);
    }

    public void carregarProdutos() {
        tableModel.setRowCount(0);
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Produto")) {

            while (rs.next()) {
                int id = rs.getInt("Id");
                String descricao = rs.getString("descricao");
                double preco = rs.getDouble("preco");
                tableModel.addRow(new Object[]{id, descricao, preco});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void pesquisarProduto() {
        String idDescricao = JOptionPane.showInputDialog(this, "Digite o ID ou Descrição do produto:");
        if (idDescricao != null && !idDescricao.isEmpty()) {
            tableModel.setRowCount(0);
            try (Connection conn = Conexao.conectar();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Produto WHERE Id = ? OR descricao LIKE ?")) {

                try {
                    pstmt.setInt(1, Integer.parseInt(idDescricao));
                } catch (NumberFormatException e) {
                    pstmt.setInt(1, -1);  // Valor inválido para garantir que o ID não seja encontrado
                }
                pstmt.setString(2, "%" + idDescricao + "%");
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("Id");
                        String descricao = rs.getString("descricao");
                        double preco = rs.getDouble("preco");
                        tableModel.addRow(new Object[]{id, descricao, preco});
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void excluirProduto() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o produto selecionado?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = Conexao.conectar();
                     PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Produto WHERE Id = ?")) {
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();
                    carregarProdutos();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.");
        }
    }
}
