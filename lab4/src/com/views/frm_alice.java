package com.views;

import com.classes.CryptoUtil;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;

public class frm_alice extends JDialog{
    private JTextField txt_aliceKey;
    private JTextField txt_keyBoB;
    private JTextField txt_key_KAB;
    private JTextField txt_encrypt_KAB;
    private JButton createKeyAButton;
    private JButton showKBButton;
    private JButton generalKeyButton;
    private JButton encryptABButton;
    private JButton btn_main;
    private JPanel panelMain;

    //variable
    KeyAgreement aliceKeyAgree;
    PublicKey bobPubKey;
    SecretKey aliceDesKey;
    Cipher aliceCipher;


    public frm_alice (Frame p){
        super(p);
        setTitle("ALICE");
        setContentPane(panelMain);
        setModal(true);
        setMinimumSize(new Dimension(700,300));
        setLocationRelativeTo(p);

        createKeyAButton.addActionListener(v->{
            try{
                AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
                paramGen.init(512);
                AlgorithmParameters params =paramGen.generateParameters();

                DHParameterSpec dhSkipParamSec = (DHParameterSpec)  params.getParameterSpec(DHParameterSpec.class);
                System.out.println("Generating a DH Keypair. . . ");
                KeyPairGenerator aliceKpairGen = KeyPairGenerator.getInstance("DH");
                aliceKpairGen.initialize(dhSkipParamSec);

                KeyPair aliceKpair = aliceKpairGen.generateKeyPair();

                System.out.println("Initializing the KeyAgreement Engine with DH private key");
                aliceKeyAgree=KeyAgreement.getInstance("DH");
                aliceKeyAgree.init(aliceKpair.getPrivate());
                byte[] alicePubKeyEnc = aliceKpair.getPublic().getEncoded();
                FileOutputStream fos = new FileOutputStream("E:\\A.pub");
                fos.write(alicePubKeyEnc);
                fos.close();
                txt_aliceKey.setText(alicePubKeyEnc.toString());

            }catch (Exception e){
                JOptionPane.showMessageDialog(null,"Loi: " +e.getMessage());
            }
        });

        showKBButton.addActionListener(v->{
            try{
                FileInputStream fis = new FileInputStream("E:\\B.pub");
                byte[] bkeyP = new byte[fis.available()];
                fis.read(bkeyP);
                fis.close();
                txt_keyBoB.setText(bkeyP.toString());
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,"Loi: "+e.getMessage());
            }
        });

        generalKeyButton.addActionListener(v->{
            try{
                FileInputStream fis = new FileInputStream("E:\\B.pub");
                byte[] bobPubKeyEnc = new byte[fis.available()];
                fis.read(bobPubKeyEnc);
                fis.close();

                KeyFactory aliceKeyFac = KeyFactory.getInstance("DH");
                X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(bobPubKeyEnc);
                bobPubKey = aliceKeyFac.generatePublic(x509KeySpec);
                System.out.println("Excuting PHASE1 of key agreement . . ");
                aliceKeyAgree.doPhase(bobPubKey,true);
                byte[] aliceShareSecret = aliceKeyAgree.generateSecret();

                System.out.println("General key: secret (DEBUG only): " + CryptoUtil.toHexString(aliceShareSecret));
                txt_key_KAB.setText(CryptoUtil.toHexString(aliceShareSecret));
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,"loi khoi tao khoa chung: "+e);
            }
        });

        encryptABButton.addActionListener(v->{
            try{
                aliceKeyAgree.doPhase(bobPubKey,true);
                aliceDesKey= aliceKeyAgree.generateSecret("DES");
                txt_encrypt_KAB.setText(aliceDesKey.toString());

                //khoa chung A-B
                BufferedWriter bw = null;
                String fileName = "E:\\KhoaA.txt";

                bw = new BufferedWriter(new FileWriter(fileName));

                bw.write(aliceDesKey.toString());
                bw.close();
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,"Loi: "+e.getMessage());
            }
        });


        btn_main.addActionListener(v->{
            DESCS des = new DESCS(null);
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        frm_alice frm_alice = new frm_alice(null);
    }
}
