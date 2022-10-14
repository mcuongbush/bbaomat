package com.views;

import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class frm_AES extends JDialog{
    private JTextField txt_username;
    private JPasswordField txt_password;
    private JTextField txt_key;
    private JButton loginButton;
    private JButton signUpButton;
    private JTextArea txt_plaintext;
    private JTextArea txt_ciphertext;
    private JButton encryptButton;
    private JButton writeFileButton;
    private JButton decryptButton;
    private JButton openFileButton;
    private JPanel mainPanel;

    private String username,password,key;
    private SecretKey secretKey;
    private byte[] byteCipherText;

    public frm_AES(Frame p){
        super(p);
        setTitle("AES");
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(550, 574));
        setLocationRelativeTo(p);

        loginButton.addActionListener(v->{
            try{
                username=txt_username.getText().trim();
                password= String.valueOf(txt_password.getPassword()).trim();
                key = username+password;
                BufferedReader br = null;
                String fileNmae = "E:\\AES.txt";
                br = new BufferedReader(new FileReader(fileNmae));
                StringBuffer sb = new StringBuffer();
                char[] ca = new char[5];
                while(br.ready()){
                    int len = br.read(ca);
                    sb.append(ca,0,len);
                }
                br.close();
                System.out.println("Key: "+sb);
                String chuoi = sb.toString();
                //boolean k = key.equals(chuoi);
                if(key.equals(chuoi)) JOptionPane.showMessageDialog(null,"Login is success!");
                else JOptionPane.showMessageDialog(null,"Login fail!");
                txt_key.setText(chuoi.getBytes(StandardCharsets.UTF_8).toString());
                KeyGenerator keyGen= KeyGenerator.getInstance("AES");
                keyGen.init(128);
                secretKey = keyGen.generateKey();
            } catch (Exception e){
                Logger.getLogger(frm_AES.class.getName()).log(Level.SEVERE,null,e);
            }
        });

        signUpButton.addActionListener(v->{
            try{
                username=txt_username.getText().trim();
                password=String.valueOf(txt_password.getPassword());
                key=username+password;
                BufferedWriter bw = null;
                String fileName="E:\\AES.txt";

                String s = txt_plaintext.getText();
                bw = new BufferedWriter(new FileWriter(fileName));
                bw.write(key);
                bw.close();
                JOptionPane.showMessageDialog(null,"Sign up is success!,Please Login again");
                txt_key.setText(key.getBytes(StandardCharsets.UTF_8).toString());
            }catch (Exception e){
                Logger.getLogger(frm_AES.class.getName()).log(Level.SEVERE,null,e);
            }
        });

        encryptButton.addActionListener(v->{
            try{
                System.out.println("Gen key: "+ secretKey);
                Cipher aesCipher = Cipher.getInstance("AES");
                aesCipher.init(Cipher.ENCRYPT_MODE,secretKey);
                String strData = txt_plaintext.getText();
                byte[] byteDataToEncrypt = strData.getBytes();
                byteCipherText = aesCipher.doFinal(byteDataToEncrypt);
                String strCipherText = new BASE64Encoder().encode(byteCipherText);
                System.out.println("Cipher Text generated using AES is: "+ strCipherText);
                txt_ciphertext.setText(strCipherText);
            }catch (Exception e){
                System.out.println("Error encrypt: " +e.getMessage());
            }
        });

        decryptButton.addActionListener(v->{
            try{
                String cipherText = txt_ciphertext.getText();
                txt_plaintext.setText(cipherText);
                Cipher aesCipher = Cipher.getInstance("AES");
                aesCipher.init(Cipher.DECRYPT_MODE,secretKey,aesCipher.getParameters());
                byte[] byteDecryptedText = aesCipher.doFinal(byteCipherText);
                String strDecryptedText = new String(byteDecryptedText);
                txt_ciphertext.setText(strDecryptedText);
            }catch (Exception e){
                System.out.println("Error Decrypt: " +e.getMessage());
            }
        });

        writeFileButton.addActionListener(v->{
            try{
                BufferedWriter bw = null;
                String fileName = "E:\\WriteAES.txt";

                String s = txt_ciphertext.getText();
                bw = new BufferedWriter(new FileWriter(fileName));
                bw.write(s);
                bw.close();
                JOptionPane.showMessageDialog(null,"Writed File "+ fileName);
            }catch (Exception e){
                Logger.getLogger(frm_AES.class.getName()).log(Level.SEVERE,null,e);
            }
        });

        openFileButton.addActionListener(v->{
            try{
                BufferedReader br = null;
                br = new BufferedReader(new FileReader("E:\\WriteAES.txt"));
                JOptionPane.showMessageDialog(null,"Read file is success!");
                StringBuffer sb = new StringBuffer();
                char[]ca = new char[5];
                while(br.ready()){
                    int len = br.read(ca);
                    sb.append(ca,0,len);
                }
                br.close();;
                System.out.println("Data from file \":\\ReadAES.txt\" : "+ sb );String chuoi = sb.toString();
                txt_plaintext.setText(chuoi);
            }catch (Exception e){
                Logger.getLogger(frm_AES.class.getName()).log(Level.SEVERE,null,e);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        frm_AES frm_aes = new frm_AES(null);
    }
}
