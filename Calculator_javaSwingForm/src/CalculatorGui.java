import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorGui {

    private double total = 0.0;
    private char operation = ' ';

    private JPanel JavaCalculator;
    private JTextField txtDisplay;
    private JButton BtnFive;
    private JButton BtnSix;
    private JButton BtnTimes;
    private JButton BtnFour;
    private JButton BtnNine;
    private JButton BtnDivide;
    private JButton BtnEight;
    private JButton BtnSeven;
    private JButton BtnMinus;
    private JButton BtnPlus;
    private JButton BtnThree;
    private JButton BtnTwo;
    private JButton BtnOne;
    private JButton BtnEqual;
    private JButton BtnDot;
    private JButton BtnZero;

    public CalculatorGui() {
        txtDisplay.setEditable(false);

        BtnOne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String btnOneText = txtDisplay.getText() + BtnOne.getText();
                txtDisplay.setText(btnOneText);
            }
        });
        BtnTwo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String btnTwoText = txtDisplay.getText() + BtnTwo.getText();
                txtDisplay.setText(btnTwoText);

            }
        });
        BtnThree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String btnThreeText = txtDisplay.getText() + BtnThree.getText();
                txtDisplay.setText(btnThreeText);

            }
        });
        BtnFour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String btnFourText = txtDisplay.getText() + BtnFour.getText();
                txtDisplay.setText(btnFourText);

            }
        });
        BtnFive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String BtnFiveText = txtDisplay.getText() + BtnFive.getText();
                txtDisplay.setText(BtnFiveText);

            }
        });
        BtnSix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String BtnSixText = txtDisplay.getText() + BtnSix.getText();
                txtDisplay.setText(BtnSixText);

            }
        });
        BtnSeven.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String BtnSevenText = txtDisplay.getText() + BtnSeven.getText();
                txtDisplay.setText(BtnSevenText);

            }
        });
        BtnEight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String BtnEightText = txtDisplay.getText() + BtnEight.getText();
                txtDisplay.setText(BtnEightText);

            }
        });
        BtnNine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String BtnNineText = txtDisplay.getText() + BtnNine.getText();
                txtDisplay.setText(BtnNineText);

            }
        });
        BtnZero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String BtnZeroText = txtDisplay.getText() + BtnZero.getText();
                txtDisplay.setText(BtnZeroText);

            }
        });

        BtnPlus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String displayText = txtDisplay.getText();
                total = Double.parseDouble(displayText);
                operation = '+';
                txtDisplay.setText("");
            }
        });

        BtnMinus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String displayText = txtDisplay.getText();
                total = Double.parseDouble(displayText);
                operation = '-';
                txtDisplay.setText("");
            }
        });

        BtnTimes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String displayText = txtDisplay.getText();
                total = Double.parseDouble(displayText);
                operation = '*';
                txtDisplay.setText("");
            }
        });

        BtnDivide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String displayText = txtDisplay.getText();
                total = Double.parseDouble(displayText);
                operation = '/';
                txtDisplay.setText("");
            }
        });

        BtnEqual.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String displayText = txtDisplay.getText();
                double currentValue = Double.parseDouble(displayText);
                switch (operation) {
                    case '+':
                        total += currentValue;
                        break;
                    case '-':
                        total -= currentValue;
                        break;
                    case '*':
                        total *= currentValue;
                        break;
                    case '/':
                        if (currentValue != 0) {
                            total /= currentValue;
                        } else {
                            txtDisplay.setText("Error: Division by zero");
                            return;
                        }
                        break;
                }
                txtDisplay.setText(Double.toString(total));
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Calculator");
        frame.setContentPane(new CalculatorGui().JavaCalculator);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
