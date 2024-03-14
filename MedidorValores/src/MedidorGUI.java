import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class MedidorGUI {
    private JTextField txtValor;
    private JButton OKButton;
    private JTextField txtMaior;
    private JTextField txtMenor;
    private JTextField txtMedia;
    private JButton exibirButton;
    public JPanel Painel;
    private double[] numeros;

    public MedidorGUI(){
        txtMaior.setEditable(false);
        txtMenor.setEditable(false);
        txtMedia.setEditable(false);

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarNumeros();
            }
        });

        exibirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exibirResultados();
            }
        });

    }


    private void cadastrarNumeros() {
        String valorInput = txtValor.getText();
        String[] numerosString = valorInput.split(",");

        numeros = new double[numerosString.length];

        if (!valorInput.matches("[0-9,]+")) {
            JOptionPane.showMessageDialog(null, "A entrada deve conter apenas números e vírgulas!");
            txtValor.setText("");
            return;
        }

        for (int i = 0; i < numerosString.length; i++) {
            numeros[i] = Double.parseDouble(numerosString[i]);
        }

        JOptionPane.showMessageDialog(null, "Números cadastrados com sucesso!");
    }

    private void exibirResultados() {
        if (numeros == null || numeros.length == 0) {
            JOptionPane.showMessageDialog(null, "Nenhum número cadastrado!");
            return;
        }

        double menor = Arrays.stream(numeros).min().getAsDouble();
        double maior = Arrays.stream(numeros).max().getAsDouble();
        double media = Arrays.stream(numeros).average().getAsDouble();

        txtMenor.setText(String.valueOf(menor));
        txtMaior.setText(String.valueOf(maior));
        txtMedia.setText(String.valueOf(media));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MedidorGUI");
        frame.setContentPane(new MedidorGUI().Painel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
