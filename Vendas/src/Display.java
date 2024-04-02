import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Display extends JFrame {

    private Produto produto;
    private JTextField idNumTxt;
    private JTextField descricaoTxt;
    private JTextField precoTxt;
    private JCheckBox ativoCheckBox;
    private JTextArea resultadoArea;

    public Display(Connection connection) {
        super("[ Vendas - Avaliacao - 1]");
        produto = new Produto(connection);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        mainPanel.add(new JLabel("ID"), gbc);
        idNumTxt = new JTextField(10);
        gbc.gridx = 1;
        mainPanel.add(idNumTxt, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Descrição: "), gbc);
        descricaoTxt = new JTextField(10);
        gbc.gridx = 1;
        mainPanel.add(descricaoTxt, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Preço: "), gbc);
        precoTxt = new JTextField(10);
        gbc.gridx = 1;
        mainPanel.add(precoTxt, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Ativo: "), gbc);
        ativoCheckBox = new JCheckBox();
        gbc.gridx = 1;
        mainPanel.add(ativoCheckBox, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarProduto();
            }
        });
        mainPanel.add(cadastrarButton, gbc);

        gbc.gridy++;
        JButton listarButton = new JButton("Listar Produtos");
        listarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarProdutos();
            }
        });
        mainPanel.add(listarButton, gbc);

        gbc.gridy++;
        JButton atualizarButton = new JButton("Atualizar Produto");
        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarProduto();
            }
        });
        mainPanel.add(atualizarButton, gbc);

        gbc.gridy++;
        JButton excluirButton = new JButton("Excluir Produto");
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirProduto();
            }
        });
        mainPanel.add(excluirButton, gbc);

        gbc.gridy++;
        JButton pesquisarButton = new JButton("Pesquisar Produto");
        pesquisarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pesquisarProduto();
            }
        });
        mainPanel.add(pesquisarButton, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        mainPanel.add(scrollPane, gbc);

        add(mainPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cadastrarProduto() {
        try {
            int id = Integer.parseInt(idNumTxt.getText());
            String descricao = descricaoTxt.getText();
            double preco = Double.parseDouble(precoTxt.getText());
            boolean ativo = ativoCheckBox.isSelected();
            produto.gravarProduto(id, descricao, preco, ativo);
            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar prodruto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarProdutos() {
        try {
            List<String> produtos = produto.listarProdutos();
            StringBuilder sb = new StringBuilder();
            for (String p : produtos) {
                sb.append(p).append("\n");
            }
            resultadoArea.setText(sb.toString());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar os produtos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarProduto() {
        try {
            int id = Integer.parseInt(idNumTxt.getText());
            String descricao = descricaoTxt.getText();
            double preco = Double.parseDouble(precoTxt.getText());
            produto.atualizarProduto(id, descricao, preco);
            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");

        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirProduto() {
        try {
            int id = Integer.parseInt(idNumTxt.getText());
            produto.excluirProduto(id);
            JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pesquisarProduto() {
        try {
            String produtoId = idNumTxt.getText();
            List<String> produtos = produto.pesquisarProduto(produtoId);
            StringBuilder sb = new StringBuilder();

            for (String p : produtos) {
                sb.append(p).append("\n");
            }

            resultadoArea.setText(sb.toString());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao pesquisar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
