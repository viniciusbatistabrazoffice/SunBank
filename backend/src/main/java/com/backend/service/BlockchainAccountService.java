package com.backend.service;

import com.backend.config.BlockchainConfig;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class BlockchainAccountService {

    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int AES_IV_SIZE = 16;

    private final BlockchainConfig config;

    public BlockchainAccountService(BlockchainConfig config) {
        this.config = config;
    }

    @PostConstruct
    public void validate() {
        if (config.isEnabled() && (config.getEncryptionKey() == null || config.getEncryptionKey().isBlank())) {
            throw new IllegalStateException("sunbank.blockchain.encryption-key must be set when blockchain is enabled");
        }
    }

    public GeneratedAccount generate() throws Exception {
        ECKeyPair keyPair = Keys.createEcKeyPair();
        Credentials credentials = Credentials.create(keyPair);
        String privateKey = keyPair.getPrivateKey().toString(16);
        String address = credentials.getAddress();
        String encrypted = encrypt(privateKey);
        return new GeneratedAccount(address, privateKey, encrypted);
    }

    public Credentials loadCredentials(String encryptedPrivateKey) throws Exception {
        String privateKey = decrypt(encryptedPrivateKey);
        return Credentials.create(privateKey);
    }

    private String encrypt(String value) throws Exception {
        SecretKeySpec keySpec = deriveKey();
        byte[] iv = new byte[AES_IV_SIZE];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
        return Base64.getEncoder().encodeToString(combined);
    }

    private String decrypt(String encryptedValue) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encryptedValue);
        byte[] iv = new byte[AES_IV_SIZE];
        byte[] encrypted = new byte[combined.length - AES_IV_SIZE];
        System.arraycopy(combined, 0, iv, 0, AES_IV_SIZE);
        System.arraycopy(combined, AES_IV_SIZE, encrypted, 0, encrypted.length);
        SecretKeySpec keySpec = deriveKey();
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private SecretKeySpec deriveKey() throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(config.getEncryptionKey().getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static class GeneratedAccount {
        private final String address;
        private final String privateKey;
        private final String encryptedPrivateKey;

        public GeneratedAccount(String address, String privateKey, String encryptedPrivateKey) {
            this.address = address;
            this.privateKey = privateKey;
            this.encryptedPrivateKey = encryptedPrivateKey;
        }

        public String getAddress() {
            return address;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public String getEncryptedPrivateKey() {
            return encryptedPrivateKey;
        }
    }
}
