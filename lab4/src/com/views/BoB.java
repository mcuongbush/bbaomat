package com.views;

import com.classes.CryptoUtil;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class BoB extends  JDialog {
    private JTextField txt_bobKey;
    private JButton createKeyBButton;
    private JTextField txt_aliceKey;
    private JButton showKAButton;
    private JTextField txt_kabKey;
    private JButton generalKeyButton;
    private JTextField txt_encryptKAB;
    private JButton encryptKABButton;
    private JButton encryptDecryptButton;
    private JPanel mainPanel;

    KeyAgreement bobKeyAgree;
    PublicKey alicePubKey;
    SecretKey bobDesKey;
    Cipher bobCipher;

    public BoB (Frame p){
        super(p);
        setTitle("BoB");
        setModal(true);
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(700,400));
        setLocationRelativeTo(p);

        showKAButton.addActionListener(v->{
            try{
                FileInputStream fis =new FileInputStream("E:\\A.pub");
                byte[] akeyP = new byte[fis.available()];
                fis.read(akeyP);
                fis.close();
                txt_aliceKey.setText(akeyP.toString());
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,"Loi: "+e.getMessage());
            }
        });

        createKeyBButton.addActionListener(v->{
            try{
                boolean read=false;
                while (!read){
                    try{
                        FileInputStream fis = new FileInputStream("E:\\A.pub");
                        fis.close();
                        read=true;
                    }catch (Exception e){
                        JOptionPane.showMessageDialog(null,"Loi: "+e.getMessage());
                    }
                }
                FileInputStream fis = new FileInputStream("E:\\A.pub");
                byte[] alicePubKeyEnc = new byte[fis.available()];
                fis.read(alicePubKeyEnc);
                fis.close();
                KeyFactory bobKeyFac = KeyFactory.getInstance("DH");
                X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(alicePubKeyEnc);
                alicePubKey = bobKeyFac.generatePublic(x509KeySpec);
                DHParameterSpec dhParamSpec = ((DHPublicKey) alicePubKey).getParams();
                System.out.println("Generate DH keypair. . . ");
                KeyPairGenerator bobKpairGen = KeyPairGenerator.getInstance("DH");
                bobKpairGen.initialize(dhParamSpec);
                KeyPair bobKpair = bobKpairGen.generateKeyPair();
                System.out.println("Initializing KeyAgreement engine . . . ");
                bobKeyAgree = KeyAgreement.getInstance("DH");
                bobKeyAgree.init(bobKpair.getPrivate());
                byte [] bobPubbKeyEnc = bobKpair.getPublic().getEncoded();
                FileOutputStream fos = new FileOutputStream("E:\\B.pub");
                fos.write(bobPubbKeyEnc);
                fos.close();
                txt_bobKey.setText(bobPubbKeyEnc.toString());
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,"loi: "+e.getMessage());
            }
        });
        generalKeyButton.addActionListener(v->{
            try{
                bobKeyAgree.doPhase(alicePubKey,true);
                byte[] bobShareSecret = bobKeyAgree.generateSecret();
                System.out.println("Khoa chung: Share secret: "+ CryptoUtil.toHexString(bobShareSecret));
                txt_kabKey.setText(CryptoUtil.toHexString(bobShareSecret));
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,"loi: "+e.getMessage());
            }
        });
        encryptKABButton.addActionListener(v->{
            try{
                bobKeyAgree.doPhase(alicePubKey,true);
                bobDesKey=bobKeyAgree.generateSecret("DES");
                txt_encryptKAB.setText(bobDesKey.toString());

                BufferedWriter bw =null;
                String fileName = "E:\\KhoaB.txt";
                bw= new BufferedWriter(new FileWriter(fileName));
                bw.write(bobDesKey.toString());
                bw.close();
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,"Loi: " +e.getMessage());
            }
        });

        encryptDecryptButton.addActionListener(v->{
            DESCS des = new DESCS(null);
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        BoB boB = new BoB(null);
    }

}
