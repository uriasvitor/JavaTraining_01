import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Poupex {

    private JPanel panel1;
    private JPanel txtdisplay;
    private JTextField feeYear;
    private JTextField numYear;
    private JTextField deposit;
    private JButton submit;
    private JLabel totalProfit;

    public Poupex() {
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String depositText = deposit.getText();
                String numYearText = numYear.getText();
                String feeText = feeYear.getText();

                if (depositText.isEmpty() || numYearText.isEmpty() || feeText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double depositValue = Double.parseDouble(depositText);
                    int numYears = Integer.parseInt(numYearText);
                    double fee = Double.parseDouble(feeText) / 100;

                    double totalPoupado = totalPoupado(depositValue, numYears, fee);
                    totalProfit.setText("Total poupado R$: " + String.format("%.2f", totalPoupado));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, insira números válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private double totalPoupado(double depositValue, int numYears, double fee) {
        double total = depositValue;
        for (int i = 0; i < numYears; i++) {
            total *= (1 + fee);
        }
        return total;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Poupex");
        frame.setContentPane(new Poupex().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
