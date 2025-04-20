import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SignaturePanel extends JPanel {
    private JTextArea inputArea;
    private JTextArea outputArea;

    public SignaturePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Создание панели ввода
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Входной текст:"), BorderLayout.NORTH);
        inputArea = new JTextArea(10, 50);
        inputArea.setFont(new Font("Arial", Font.PLAIN, 14));
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputPanel.add(inputScroll, BorderLayout.CENTER);

        // Создание панели вывода
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(new JLabel("Выходной текст:"), BorderLayout.NORTH);
        outputArea = new JTextArea(10, 50);
        outputArea.setFont(new Font("Arial", Font.PLAIN, 14));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputPanel.add(outputScroll, BorderLayout.CENTER);

        // Создание панели управления
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton signButton = new JButton("Подписать");
        JButton verifyButton = new JButton("Проверить");
        JButton clearButton = new JButton("Очистить");

        controlPanel.add(signButton);
        controlPanel.add(verifyButton);
        controlPanel.add(clearButton);

        // Добавление обработчиков событий
        signButton.addActionListener(e -> sign());
        verifyButton.addActionListener(e -> verify());
        clearButton.addActionListener(e -> clear());

        // Добавление компонентов на главную панель
        add(inputPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.CENTER);
        add(outputPanel, BorderLayout.SOUTH);
    }

    private void sign() {
        try {
            String input = inputArea.getText();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Пожалуйста, введите текст для подписи", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String signature = CryptoUtils.signMessage(input);
            outputArea.setText(signature);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка подписи", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verify() {
        try {
            String input = inputArea.getText();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Пожалуйста, введите текст для проверки", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean isValid = CryptoUtils.verifySignature(input);
            outputArea.setText("Подпись " + (isValid ? "действительна" : "недействительна"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка проверки", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clear() {
        inputArea.setText("");
        outputArea.setText("");
    }
} 