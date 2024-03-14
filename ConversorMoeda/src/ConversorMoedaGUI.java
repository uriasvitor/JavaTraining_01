import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConversorMoedaGUI extends JFrame{
    private Number valor_total;
    JTextField valor_em_reais;
    JComboBox<String> moedas;
    public ConversorMoedaGUI(){
        JFrame frame = new JFrame("Conversor");
        frame.setSize(400,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        valor_em_reais = new JTextField(10);
        JButton btnConverter = new JButton("Converter");
        JLabel valor_convertido = new JLabel();

        btnConverter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                valor_total = converterMoeda();

                if(valor_total != null){
                    valor_convertido.setText(String.valueOf(valor_total));
                }else{
                    JOptionPane.showMessageDialog(null, "Por favor, digite um número válido para o valor em reais.");
                }

            }
        });

        String[] opcoesMoeda = {"USD", "EUR", "YEN"};
        moedas = new JComboBox<String>(opcoesMoeda);

        JPanel panel = new JPanel(new GridLayout(4,2));

        panel.add(new JLabel("Valor em Moeda Real: "));
        panel.add(valor_em_reais);
        panel.add(new JLabel("Converter para: "));
        panel.add(moedas);
        panel.add(new JLabel());
        panel.add(btnConverter);
        panel.add(new JLabel("Valor Convertido :"));
        panel.add(valor_convertido);

        frame.add(panel);
        frame.setVisible(true);

    }

    private Double converterMoeda() {
        String valorDigitado = valor_em_reais.getText();
        Object moeda_Selecionada = moedas.getSelectedItem();

        try {
            Double valorNumero = Double.parseDouble(valorDigitado);
            String moedaString = (String) moeda_Selecionada;

            switch (moedaString) {
                case "USD":
                    return valorNumero * 5.0;
                case "EUR":
                    return valorNumero * 5.43;
                case "YEN":
                    return valorNumero * 29.66;
                default:
                    JOptionPane.showMessageDialog(null, "Moeda selecionada não reconhecida");
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
