
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdivinheOnumero  extends JFrame{
    private int segredo;
    private JTextField txtCampo;
    private JLabel resultadoLabel;

    public AdivinheOnumero() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 150);

        segredo = (int) (Math.random() * 100) + 1;

        JPanel painel = new JPanel();
        JLabel instrucaoLabel = new JLabel("Eu tenho um número entre\n" + "1 e 100, você pode adivinhá-lo? Entre com seu chute. ");
        resultadoLabel = new JLabel("");
        txtCampo = new JTextField(10);
        JButton botaoAdivinhar = new JButton("Adivinhar");
        JButton botaoNovoJogo = new JButton("Novo jogo");

        botaoAdivinhar.addActionListener(e -> {
            int palpite;
            try {
                palpite = Integer.parseInt(txtCampo.getText());
            } catch (NumberFormatException ex) {
                resultadoLabel.setText("Por favor, insira um número válido");
                return;
            }

            if (palpite < 1 || palpite > 100) {
                resultadoLabel.setText("Por favor, insira um número entre 1 e 100.");
                return;
            }

            if (palpite == segredo) {
                resultadoLabel.setText("Correto!");
                txtCampo.setEditable(false);
            } else if (palpite < segredo) {
                resultadoLabel.setText("Tente um número maior.");
            } else {
                resultadoLabel.setText("Tente um número menor.");
            }
        });

        botaoNovoJogo.addActionListener(e -> {
            segredo = (int) (Math.random() * 100) + 1;
            txtCampo.setEditable(true);
            txtCampo.setText("");
            resultadoLabel.setText("");
        });

        painel.add(instrucaoLabel);
        painel.add(txtCampo);
        painel.add(botaoAdivinhar);
        painel.add(botaoNovoJogo);
        painel.add(resultadoLabel);

        add(painel);
        setVisible(true);
    }
}
