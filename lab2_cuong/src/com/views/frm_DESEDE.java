package com.views;

import javafx.scene.Parent;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.swing.*;
import java.awt.*;
import java.beans.Encoder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class frm_DESEDE extends JDialog {
    private JTextField txt_key;
    private JTextArea txt_plaintext;
    private JTextArea txt_ciphertext;
    private JButton decryptButton;
    private JButton allShowButton;
    private JButton encryptButton;
    private JButton openFileButton;
    private JButton writeFileButton;
    private JPanel mainPanel;


    public static  final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec myKeySpec;
    SecretKeyFactory mySecretKeyFactory;
    Cipher cipher;
    byte[] keyAsBytes;
    String myEncryptionKey;
    String myEncryptionScheme;
    SecretKey key;
    public frm_DESEDE (Frame p){
        super(p);
        setTitle("DESEDE");
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(550, 574));
        setLocationRelativeTo(p);

        encryptButton.addActionListener(v->{
            try{
                myEncryptionKey = txt_key.getText();
                myEncryptionScheme= DESEDE_ENCRYPTION_SCHEME;
                keyAsBytes =myEncryptionKey.getBytes(StandardCharsets.UTF_8);
                myKeySpec=new DESedeKeySpec(keyAsBytes);
                mySecretKeyFactory = SecretKeyFactory.getInstance(myEncryptionScheme);
                cipher = Cipher.getInstance(myEncryptionScheme);
                key = mySecretKeyFactory.generateSecret(myKeySpec);
                System.out.println("cipher key: " + key);
                String plainText = txt_plaintext.getText();
                String encrypted=encrypt(plainText);
                System.out.println("Encrypted Value: " + encrypted);
                txt_ciphertext.setText(encrypted);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        decryptButton.addActionListener(v->
        {
            try{

                txt_ciphertext.setText(decrypt(txt_plaintext.getText()));
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        openFileButton.addActionListener(v->{
            try{
                BufferedReader br = null;
                br = new BufferedReader(new FileReader("E:\\ReadDESEDE.txt"));
                JOptionPane.showMessageDialog(null,"Read file is success!");
                StringBuffer sb = new StringBuffer();
                char[]ca = new char[5];
                while(br.ready()){
                    int len = br.read(ca);
                    sb.append(ca,0,len);
                }
                br.close();;
                System.out.println("Data from file \":\\ReadDESEDE.txt\" : "+ sb );String chuoi = sb.toString();
                txt_plaintext.setText(chuoi);
            }catch (Exception e){
                Logger.getLogger(frm_AES.class.getName()).log(Level.SEVERE,null,e);
            }
        });

        writeFileButton.addActionListener(v->{
            try{
                BufferedWriter bw = null;
                String fileName = "E:\\WriteDESEDE.txt";
                String s = txt_ciphertext.getText();
                bw = new BufferedWriter(new FileWriter(fileName));
                bw.write(s);
                bw.close();
                JOptionPane.showMessageDialog(null,"Writed File "+ fileName);
            }catch (Exception e){
                Logger.getLogger(frm_AES.class.getName()).log(Level.SEVERE,null,e);
            }
        });

        setVisible(true);
    }

    public String encrypt(String unencryptedString){
        String encryptedString = null;
        try{
            cipher.init(Cipher.ENCRYPT_MODE,key);
            byte[] plaintext = unencryptedString.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedText = cipher.doFinal(plaintext);
            BASE64Encoder base64Encoder = new BASE64Encoder();
            //encryptedString = base64Encoder.encode(encryptedText);
            Base64.Encoder encoder = Base64.getEncoder();
            encryptedString = encoder.encodeToString(encryptedText);
        }catch (Exception e){
            e.printStackTrace();
        }
        return encryptedString;
    }

    public String decrypt(String encryptedString){
        String decryptedText = null;
        try{
            cipher.init(Cipher.DECRYPT_MODE,key);
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] encryptedText = base64Decoder.decodeBuffer(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            String a = new String(plainText);
            System.out.println("Plaintext: " + a);
            decryptedText=a;
        }catch (Exception e){
            e.printStackTrace();
        }
        return decryptedText;
    }
    public static void main(String[] args) {
        frm_DESEDE frm_desede = new frm_DESEDE(null);
    }
}
