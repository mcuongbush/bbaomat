package com.views;

import sun.security.krb5.internal.crypto.Des;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class frm_DES extends JDialog {
    private JTextField txt_InputKey;
    private JTextArea txt_PlainText;
    private JTextArea txt_CipherText;
    private JButton encryptButton;
    private JButton decryptButton;
    private JPanel mainPanel;
    private JButton chosesFileButton;
    private JLabel lbl_fileName;
    private JButton showButton;
    private JButton writeFileButton;

    private int mode;


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

    public static void encrypt(String key, InputStream is, OutputStream os) throws Throwable {
        encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
    }

    public static void decrypt(String key, InputStream is, OutputStream os) throws Throwable {
        encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
    }


    public frm_DES(Frame p) {
        super(p);
        setTitle("DES");
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(550, 574));
        setLocationRelativeTo(p);

        chosesFileButton.addActionListener(v->{
            try {
                JFileChooser chooser = new JFileChooser();
                int status = chooser.showOpenDialog(null);
                if (status == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    if (file == null) {
                        return;
                    }
                    String fileName = chooser.getSelectedFile().getAbsolutePath();
                    lbl_fileName.setText(fileName);
                    JOptionPane.showMessageDialog(null, "Open file is success!");
                }
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Open file is unsuccess!\n"+e.getMessage());
            }
        });

        encryptButton.addActionListener(v->{

            try{
                String key = txt_InputKey.getText();

                FileInputStream fis = new FileInputStream("E:\\Des.txt");
                FileOutputStream fos = new FileOutputStream("E:\\EnDes.txt");
                encrypt(key,fis,fos);
                JOptionPane.showMessageDialog(null,"Encrypt text is success!");
            }catch (Throwable e){
                e.printStackTrace();
            }

//            if(lbl_fileName!=null){
//
//            }
//            else{
//                JOptionPane.showMessageDialog(null, "file is null");
//            }
        });

        decryptButton.addActionListener(v->{
            FileInputStream fis2 =null;
            try{
                String key = txt_InputKey.getText();
                fis2  = new FileInputStream("E:\\EnDes.txt");
                FileOutputStream fos2 = new FileOutputStream("E:\\DeDes.txt");
                decrypt(key,fis2,fos2);
                BufferedReader br = null;
                br = new BufferedReader(new FileReader("E:\\DeDes.txt"));
                StringBuffer sb = new StringBuffer();
                JOptionPane.showMessageDialog(null,"Decrypt is success!");
                char[]ca = new char[5];
                while(br.ready()){
                    int len = br.read(ca);
                    sb.append(ca,0,len);
                }
                br.close();;
                System.out.println("text: "+sb);String chuoi = sb.toString();
                txt_CipherText.setText(chuoi);
            }catch (Throwable e){
                Logger.getLogger(frm_DES.class.getName()).log(Level.SEVERE,null,e);
            }finally {
                try{
                    fis2.close();
                }catch (IOException ex){
                    Logger.getLogger(frm_DES.class.getName()).log(Level.SEVERE,null,ex);
                }
            }
        });

        writeFileButton.addActionListener(v->{
            try{
                BufferedWriter bw = null;
                String fileName = "E:\\Des.txt";
                String s = txt_PlainText.getText();
                bw = new BufferedWriter(new FileWriter(fileName));
                bw.write(s);
                bw.close();
                JOptionPane.showMessageDialog(null,"Write file is success!");
                txt_CipherText.setText(s);
            }catch (IOException e){
                Logger.getLogger(frm_DES.class.getName()).log(Level.SEVERE,null,e);
            }
        });
        showButton.addActionListener(v->{
            try{
                BufferedReader br = null;
                String fileNmae="E:\\EnDes.txt";
                br = new BufferedReader(new FileReader(fileNmae));
                StringBuffer sb = new StringBuffer();

                JOptionPane.showMessageDialog(null,"Open file!");
                char[] ca = new char[5];
                while(br.ready()){
                    int len = br.read(ca);
                    sb.append(ca,0,len);
                }
                br.close();
                System.out.println("text: "+sb);String chuoi = sb.toString();
                txt_PlainText.setText(chuoi);
            }catch (Throwable e){
                Logger.getLogger(frm_DES.class.getName()).log(Level.SEVERE,null,e);
            }
        });
        setVisible(true);
    }

    public static void main(String[] args) {
        frm_DES frm_des = new frm_DES(null);
    }
}
