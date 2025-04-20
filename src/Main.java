import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.Base64;

public class Main extends JFrame {
    public Main() {
        setTitle("TSILAB4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Создание главной панели с вкладками
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Создание панелей для каждой вкладки
        EncryptionPanel encryptionPanel = new EncryptionPanel();
        SignaturePanel signaturePanel = new SignaturePanel();
        
        // Добавление вкладок
        tabbedPane.addTab("Шифрование", encryptionPanel);
        tabbedPane.addTab("Цифровые подписи", signaturePanel);
        
        // Добавление панели с вкладками в окно
        add(tabbedPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Main().setVisible(true);
        });
    }
}