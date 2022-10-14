package lab3.views;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class frm_keyRSA extends JDialog{


    private JTextField txt_plainText;
    private JTextField txt_cipherText;
    private JButton encryptButton;
    private JButton decryptButton;
    private JPanel panelMain;

    public frm_keyRSA(Frame p){
        super(p);
        setTitle("RSA Cipher");
        setContentPane(panelMain);
        setMinimumSize(new Dimension(800, 300));
        setModal(true);
        setLocationRelativeTo(p);

        encryptButton.addActionListener(v->{
            generateKey();
            encrypt();
        });
        decryptButton.addActionListener(v->decrypt());


        setVisible(true);
    }

    public static void main(String[] args) {
        frm_keyRSA frm_keyRSA = new frm_keyRSA(null);
    }
    void generateKey() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            // public/private keypairs dung de khoi tao phase cua quatrinh dang ky key
            KeyPair kp = kpg.genKeyPair();
            PublicKey pbkey = kp.getPublic();
            PrivateKey prkey = kp.getPrivate();
            //Ghi fi12 publickzy
            FileOutputStream f1 = new FileOutputStream("E:\\Skey_RSA_pub.dat");
            ObjectOutputStream b1 = new ObjectOutputStream(f1);
            b1.writeObject(pbkey);
            // ghi file private key
            FileOutputStream f2 = new FileOutputStream("E:\\Skey_RSA_pri.dat");
            ObjectOutputStream b2 = new ObjectOutputStream(f2);
            b2.writeObject(prkey);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
        }
    }

    void encrypt() {
        try {
            String s = txt_plainText.getText();
            //doc file public kay
            FileInputStream f = new FileInputStream("E:\\Skey_RSA_pub.dat");
            ObjectInputStream b = new ObjectInputStream(f);

            RSAPublicKey pbk = (RSAPublicKey) b.readObject();
            BigInteger e = pbk.getPublicExponent();
            BigInteger n = pbk.getModulus();
            System.out.println("e= " + e);
            System.out.println("n= " + n);
            byte[] ptext = s.getBytes(StandardCharsets.UTF_8);
            BigInteger m = new BigInteger(ptext);
            BigInteger c = m.modPow(e, n);
            System.out.println("c= " + c);
            String cs = c.toString();
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("E:\\Enc_RSA.dat")));
            out.write(cs, 0, cs.length());
            txt_cipherText.setText(cs);
            out.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
        }
    }

    void decrypt() {
        try{
            //doc van ban ma hoa
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("E:\\Enc_RSA.dat")));
            String ctext = txt_cipherText.getText();
// churn sang kiÅu biginteger
            BigInteger c = new BigInteger(ctext);

//doc khia private key
            FileInputStream f = new FileInputStream("E:\\Skey_RSA_pri.dat");
//S& dung h±ll read0bject cia ObjectlnputStream
//de dgc liéu t4p tin nhi phån 1411.
            //Thi ' tv doc can dåm båo ddng thi.i tv ghi
            ObjectInputStream b = new ObjectInputStream(f);
            RSAPrivateKey prk = (RSAPrivateKey) b.readObject();
            BigInteger d = prk.getPrivateExponent();
            BigInteger n = prk.getModulus();
            System.out.println("d= " + d);
            System.out.println("n= " + n);
            BigInteger m = c.modPow(d, n);
            System.out.println("m= " + m);
            byte[] mt = m.toByteArray();
            System.out.println("Plaintext: " + d);
            StringBuilder sb = new StringBuilder();
            for(int i = 0 ; i <mt.length;i++){
                sb.append((char) mt[i]);
            }
            txt_plainText.setText(String.valueOf(sb));
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
