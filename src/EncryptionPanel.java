import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EncryptionPanel extends JPanel {
    private JTextArea inputArea;
    private JTextArea outputArea;
    private JComboBox<String> algorithmComboBox;

    public EncryptionPanel() {
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
        algorithmComboBox = new JComboBox<>(new String[]{"Симметричное (AES)", "Асимметричное (RSA)"});
        JButton encryptButton = new JButton("Зашифровать");
        JButton decryptButton = new JButton("Расшифровать");
        JButton clearButton = new JButton("Очистить");

        controlPanel.add(new JLabel("Алгоритм:"));
        controlPanel.add(algorithmComboBox);
        controlPanel.add(encryptButton);
        controlPanel.add(decryptButton);
        controlPanel.add(clearButton);

        // Добавление обработчиков событий
        encryptButton.addActionListener(e -> encrypt());
        decryptButton.addActionListener(e -> decrypt());
        clearButton.addActionListener(e -> clear());

        // Добавление компонентов на главную панель
        add(inputPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.CENTER);
        add(outputPanel, BorderLayout.SOUTH);
    }

    private void encrypt() {
        try {
            String input = inputArea.getText();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Пожалуйста, введите текст для шифрования", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String algorithm = (String) algorithmComboBox.getSelectedItem();
            String encrypted = CryptoUtils.encrypt(input, algorithm);
            outputArea.setText(encrypted);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка шифрования", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void decrypt() {
        try {
            String input = inputArea.getText();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Пожалуйста, введите текст для расшифровки", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String algorithm = (String) algorithmComboBox.getSelectedItem();
            String decrypted = CryptoUtils.decrypt(input, algorithm);
            outputArea.setText(decrypted);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка расшифровки", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clear() {
        inputArea.setText("");
        outputArea.setText("");
    }
} 