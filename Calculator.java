import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator extends JFrame implements ActionListener {
    private JTextField display;
    private String currentFunction = ""; 
    private double baseValue = 0; 
    private boolean isExponentiation = false; 
    private double n = 0, r = 0;

    public Calculator() {
        setTitle("Scientific Calculator");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        display = new JTextField();
        display.setEditable(false); 
        display.setFont(new Font("Arial", Font.BOLD, 24));
        display.setHorizontalAlignment(JTextField.RIGHT);
        add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(8, 5, 5, 5));

        String[] buttons = {
            "sin", "cos", "tan", "1/X", "Cl",
            "cosec", "sec", "cot", "n!", "√",
            "|x|", "x^y", "x^3", "x^2", "%",
            "Nπ", "exp", "nCr", "nPr", "/",
            "2^x", "1", "2", "3", "*",
            "10^x", "4", "5", "6", "-",
            "log", "7", "8", "9", "+",
            "ln", "0", ".", "π", "="
        };

        for (int i = 0; i < buttons.length; i++) {
            JButton button = new JButton(buttons[i]);
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.setFocusPainted(false);
            button.addActionListener(this);
            button.setPreferredSize(new Dimension(80, 80));
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        try {
            switch (command) {
                case "Cl":
                    display.setText("");
                    currentFunction = "";
                    baseValue = 0;
                    isExponentiation = false;
                    break;
                case "=":
                    if (currentFunction.equals("nCr") || currentFunction.equals("nPr")) {
                        r = getInputValue(); 
                        if (n < r) {
                            display.setText("Error: n < r"); 
                        } else {
                            double result = (currentFunction.equals("nCr")) ? nCr(n, r) : nPr(n, r);
                            display.setText(String.valueOf(result)); 
                        }
                        currentFunction = ""; 
                    } else if (currentFunction.matches("sin|cos|tan|cot|sec|cosec|log|ln")) {
                        double inputValue = getInputValue();
                        double result = performMathematicalOperation(currentFunction, inputValue);
                        display.setText(String.valueOf(result));
                        currentFunction = "";
                    } else if (isExponentiation) {
                        double exponent = getInputValue();
                        display.setText(String.valueOf(Math.pow(baseValue, exponent)));
                        isExponentiation = false;
                    } else if (!currentFunction.isEmpty() && !display.getText().isEmpty()) {
                        double secondOperand = getInputValue();
                        double result = performArithmeticOperation(secondOperand);
                        display.setText(String.valueOf(result));
                        currentFunction = "";
                    }
                    break;
                
                case "π":
                    display.setText(String.valueOf(Math.PI));
                    break;
                case "sin":
                case "cos":
                case "tan":
                case "cot":
                case "sec":
                case "cosec":
                case "log":
                case "ln":
                    currentFunction = command;
                    display.setText(command + "(");
                    break;
                case "√":
                    display.setText(String.valueOf(Math.sqrt(getInputValue())));
                    break;
                case "n!":
                    display.setText(String.valueOf(factorial((int) getInputValue())));
                    break;
                case "1/X":
                    display.setText(String.valueOf(1 / getInputValue()));
                    break;
                case "x^2":
                    display.setText(String.valueOf(Math.pow(getInputValue(), 2)));
                    break;
                case "x^3":
                    display.setText(String.valueOf(Math.pow(getInputValue(), 3)));
                    break;
                case "2^x":
                    display.setText(String.valueOf(Math.pow(2, getInputValue())));
                    break;
                case "10^x":
                    display.setText(String.valueOf(Math.pow(10, getInputValue())));
                    break;
                case "x^y":
                    baseValue = getInputValue();
                    isExponentiation = true;
                    display.setText("");
                    break;
                case "|x|":
                    display.setText(String.valueOf(Math.abs(getInputValue())));
                    break;
                case "Nπ":
                    display.setText(String.valueOf(getInputValue() * Math.PI));
                    break;
                case "exp":
                    display.setText(String.valueOf(Math.exp(getInputValue())));
                    break;
                case "nCr":
                case "nPr":
                    n = getInputValue(); 
                    currentFunction = command;
                    display.setText(""); 
                
                case "+":
                case "-":
                case "*":
                case "/":
                    currentFunction = command;
                    baseValue = getInputValue();
                    display.setText("");
                    break;
                case "%":
                    display.setText(String.valueOf(getInputValue() / 100));
                    break;
                default:
                    display.setText(display.getText() + command);
                    break;
            }
        } catch (Exception ex) {
            display.setText("Error");
        }
    }

    private double nPr(double n, double r) {
        if (n < r) throw new IllegalArgumentException("n must be greater than or equal to r");
        return factorial((int) n) / factorial((int) (n - r));
    }

    private double nCr(double n, double r) {
        if (n < r) throw new IllegalArgumentException("n must be greater than or equal to r");
        return factorial((int) n) / (factorial((int) r) * factorial((int) (n - r)));
    }

    private long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is undefined for negative numbers.");
        }
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    private double performMathematicalOperation(String function, double number) {
        double radians = Math.toRadians(number);
        double result;

        switch (function) {
            case "sin":
                result = Math.sin(radians);
                break;
            case "cos":
                result = Math.cos(radians);
                break;
            case "tan":
                if (Math.abs(Math.cos(radians)) < 1e-10) {
                    throw new ArithmeticException("tan(" + number + ") is undefined.");
                }
                result = Math.tan(radians);
                break;
            case "cot":
                if (Math.abs(Math.sin(radians)) < 1e-10) {
                    throw new ArithmeticException("cot(" + number + ") is undefined.");
                }
                result = 1 / Math.tan(radians);
                break;

            case "sec":
                if (Math.abs(Math.cos(radians)) < 1e-10) {
                    throw new ArithmeticException("sec(" + number + ") is undefined.");
                }
                result = 1 / Math.cos(radians);
                break;
            case "cosec":
                if (Math.abs(Math.sin(radians)) < 1e-10) {
                    throw new ArithmeticException("cosec(" + number + ") is undefined.");
                }
                result = 1 / Math.sin(radians);
                break;
            case "log":
                if (number <= 0) {
                    throw new ArithmeticException("log(" + number + ") is undefined.");
                }
                result = Math.log10(number);
                break;
            case "ln":
                if (number <= 0) {
                    throw new ArithmeticException("ln(" + number + ") is undefined.");
                }
                result = Math.log(number);
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation");
        }

        return Double.parseDouble(String.format("%.10f", result));
    }

    private double getInputValue() {
        try {
            String displayText = display.getText();
            String numberPart = displayText.replaceAll("[^0-9.]", ""); 
            return Double.parseDouble(numberPart.trim()); 
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric input.");
        }
    }

    private double performArithmeticOperation(double secondOperand) {
        switch (currentFunction) {
            case "+" -> {
                return baseValue + secondOperand;
            }
            case "-" -> {
                return baseValue - secondOperand;
            }
            case "*" -> {
                return baseValue * secondOperand;
            }
            case "/" -> {
                if (secondOperand == 0) {
                    throw new ArithmeticException("Division by zero is undefined.");
                }
                return baseValue / secondOperand;
            }
            default -> {
                throw new IllegalArgumentException("Invalid arithmetic operation");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Calculator().setVisible(true);
        });
    }
}