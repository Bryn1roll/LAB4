import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.Base64;
import java.util.Random;

public class CryptoUtils {
    private static KeyPair keyPair;
    private static SecretKey secretKey;
    private static byte[] signature;
    private static final int IV_LENGTH = 16;

    static {
        initializeKeys();
    }

    public static void initializeKeys() {
        try {
            // Генерация пары ключей RSA
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            keyPair = keyGen.generateKeyPair();

            // Генерация ключа AES
            KeyGenerator aesKeyGen = KeyGenerator.getInstance("AES");
            aesKeyGen.init(256);
            secretKey = aesKeyGen.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка генерации ключей: " + e.getMessage());
        }
    }

    private static byte[] generateIV() {
        byte[] iv = new byte[IV_LENGTH];
        new Random().nextBytes(iv);
        return iv;
    }

    public static String encrypt(String message, String algorithm) {
        try {
            String encrypted;
            if (algorithm.equals("Symmetric (AES)")) {
                // Генерация случайного вектора инициализации
                byte[] iv = generateIV();
                
                // Создание шифра в режиме CBC с дополнением PKCS7
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
                
                // Шифрование данных
                byte[] encryptedBytes = cipher.doFinal(message.getBytes());
                
                // Объединение IV и зашифрованных данных
                byte[] combined = new byte[iv.length + encryptedBytes.length];
                System.arraycopy(iv, 0, combined, 0, iv.length);
                System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);
                
                // Кодирование в Base64
                encrypted = Base64.getEncoder().encodeToString(combined);
            } else {
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
                byte[] encryptedBytes = cipher.doFinal(message.getBytes());
                encrypted = Base64.getEncoder().encodeToString(encryptedBytes);
            }
            return encrypted;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка шифрования: " + e.getMessage());
        }
    }

    public static String decrypt(String encrypted, String algorithm) {
        try {
            String decrypted;
            if (algorithm.equals("Symmetric (AES)")) {
                // Декодирование из Base64
                byte[] combined = Base64.getDecoder().decode(encrypted);
                
                // Извлечение IV из начала данных
                byte[] iv = new byte[IV_LENGTH];
                System.arraycopy(combined, 0, iv, 0, iv.length);
                
                // Извлечение зашифрованных данных
                byte[] encryptedBytes = new byte[combined.length - iv.length];
                System.arraycopy(combined, iv.length, encryptedBytes, 0, encryptedBytes.length);
                
                // Создание шифра в режиме CBC с дополнением PKCS7
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
                
                // Расшифровка данных
                byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
                decrypted = new String(decryptedBytes);
            } else {
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
                byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encrypted));
                decrypted = new String(decryptedBytes);
            }
            return decrypted;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка расшифровки: " + e.getMessage());
        }
    }

    public static String calculateHash(String message) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(message.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка вычисления хеша: " + e.getMessage());
        }
    }

    public static String signMessage(String message) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(message.getBytes());

            Signature signer = Signature.getInstance("SHA256withRSA");
            signer.initSign(keyPair.getPrivate());
            signer.update(hash);
            signature = signer.sign();
            
            return Base64.getEncoder().encodeToString(signature);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка подписи: " + e.getMessage());
        }
    }

    public static boolean verifySignature(String message) {
        try {
            if (signature == null) {
                return false;
            }

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(message.getBytes());

            Signature verifier = Signature.getInstance("SHA256withRSA");
            verifier.initVerify(keyPair.getPublic());
            verifier.update(hash);
            return verifier.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка верификации: " + e.getMessage());
        }
    }
} 