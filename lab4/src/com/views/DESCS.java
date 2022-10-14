package com.views;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DESCS extends JDialog {
    private JPanel panelMain;
    private JTextField txt_inputKey;
    private JButton encryptButton;
    private JButton openKeyAButton;
    private JButton openKeyBButton;
    private JButton writeFIleButton;
    private JTextArea txt_plaintext;
    private JTextArea txt_cipherText;
    private JButton decryptButton;
    private JButton allShowButton;

    public DESCS(Frame p) {
        super(p);
        setTitle("DESCS");
        setContentPane(panelMain);
        setMinimumSize(new Dimension(700, 500));
        setModal(true);
        setLocationRelativeTo(p);
        encryptButton.addActionListener(v -> {
            try {
                String key = txt_inputKey.getText();

                FileInputStream fis = new FileInputStream("E:\\Des.txt");
                FileOutputStream fos = new FileOutputStream("E:\\EnDes.txt");
                encrypt(key, fis, fos);
                JOptionPane.showMessageDialog(null, "Encrypted Plaintext ");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "loi: " + e.getMessage());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        openKeyAButton.addActionListener(v -> {
            try {
                BufferedReader br = null;
                String fileName = "E:\\KhoaA.txt";
                br = new BufferedReader(new FileReader(fileName));
                StringBuffer sb = new StringBuffer();

                JOptionPane.showMessageDialog(null, "opened file .. . . ");
                char[] ca = new char[5];
                while (br.ready()) {

                    int len = br.read(ca);
                    sb.append(ca, 0, len);
                }
                br.close();
                System.out.println(" key A is: " + sb);
                txt_inputKey.setText(String.valueOf(sb));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "loi: " + e.getMessage());
            }
        });

        openKeyBButton.addActionListener(v -> {
            try {
                BufferedReader br = null;
                String fileName = "E:\\KhoaB.txt";
                br = new BufferedReader(new FileReader(fileName));
                StringBuffer sb = new StringBuffer();

                JOptionPane.showMessageDialog(null, "Opened file . . .");
                char[] ca = new char[5];
                while (br.ready()) {

                    int len = br.read(ca);
                    sb.append(ca, 0, len);
                }
                br.close();
                System.out.println(" Key b is: " + sb);
                txt_inputKey.setText(String.valueOf(sb));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "loi: " + e.getMessage());
            }
        });

        writeFIleButton.addActionListener(v -> {
            try {
                BufferedWriter bw = null;
                String fileName = "E:\\Des.txt";
                String s = txt_plaintext.getText();
                bw = new BufferedWriter(new FileWriter(fileName));
                bw.write(s);
                bw.close();
                JOptionPane.showMessageDialog(null, "Wrote file . . . ");
                txt_cipherText.setText(s);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "loi: " + e.getMessage());
            }
        });

        decryptButton.addActionListener(v -> {

            FileInputStream fis2 = null;
            try {
                String key = txt_inputKey.getText();
                fis2 = new FileInputStream("E:\\EnDes.txt");
                FileOutputStream fos2 = new FileOutputStream("E:\\DeDes.txt");
                decrypt(key, fis2, fos2);
                BufferedReader br = null;
                br = new BufferedReader(new FileReader("E:\\DeDes.txt"));
                StringBuffer sb = new StringBuffer();
                JOptionPane.showMessageDialog(null, "Decrypt is success!");
                char[] ca = new char[5];
                while (br.ready()) {
                    int len = br.read(ca);
                    sb.append(ca, 0, len);
                }
                br.close();
                System.out.println("text: " + sb);
                String chuoi = sb.toString();
                txt_cipherText.setText(chuoi);
            } catch (Throwable e) {
                JOptionPane.showMessageDialog(null, "loi: " + e.getMessage());
            }
        });

        allShowButton.addActionListener(v->{
            try{
                BufferedReader br = null;
                String fileName = "E:\\DeDes.txt";
                br = new BufferedReader(new FileReader(fileName));
                StringBuffer sb =new StringBuffer();
                JOptionPane.showMessageDialog(null,"Opened File");
                char [] ca = new char[5];
                while (br.ready()){
                    int len = br.read(ca);
                    sb.append(ca,0,len);
                }
                br.close();
                String ff = "E:\\EnDes.txt";
                br = new BufferedReader( new FileReader(ff));
                StringBuffer sb1 = new StringBuffer();
                char [] ca1= new char[5];
                while (br.ready()){
                    int len = br.read(ca);
                    sb1.append(ca1,0,len);
                }
                System.out.println("data is: "+sb);
                System.out.println("ciphertext is: "+sb1);
                txt_plaintext.setText(String.valueOf(sb));
                txt_cipherText.setText(sb1.toString());

            }catch (Exception e){
                JOptionPane.showMessageDialog(null,"loi: "+ e.getMessage());
            }
        });


        setVisible(true);

    }

    public static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey desKey = skf.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            doCopy(cis, os);
        } else if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, desKey);
            CipherOutputStream cos = new CipherOutputStream(os, cipher);
            doCopy(is, cos);
        }
    }

    private static void doCopy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[64];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }
        os.flush();
        os.close();
        is.close();
    }


    public static void encrypt(String key, InputStream is, OutputStream os) throws Throwable {
        encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
    }

    public static void decrypt(String key, InputStream is, OutputStream os) throws Throwable {
        encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
    }


    public static void main(String[] args) {
        DESCS descs = new DESCS(null);
    }
}
