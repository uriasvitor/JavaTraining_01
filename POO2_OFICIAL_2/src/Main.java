import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {

    public Main() {
        setTitle("Tela Principal");
        setSize(400, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnClientes = new JButton("Clientes");
        JButton btnProdutos = new JButton("Produtos");
        JButton btnVendas = new JButton("Vendas");

        btnClientes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Cliente();
            }
        });

        btnProdutos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Produto();
            }
        });

        btnVendas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Venda();
            }
        });

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.add(btnClientes);
        panel.add(btnProdutos);
        panel.add(btnVendas);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}
