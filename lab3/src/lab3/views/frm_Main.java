package lab3.views;

import lab3.models.RSA;
import lab3.models.User;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.math.BigInteger;

public class frm_Main extends JDialog {
    private JTextField txt_name;
    private JTextField txt_address;
    private JTextField txt_phone;
    private JButton encryptButton;
    private JButton decryptButton;
    private JTextArea txt_cipherText;
    private JTextField txt_passwordPlaintext;
    private JPanel panelMain;
    private JPasswordField txt_password;

    private BigInteger[]  privateKey=null;
    public frm_Main(Frame p) {
        super(p);
        setTitle("RSA Cipher");
        setContentPane(panelMain);
        setMinimumSize(new Dimension(800, 400));
        setModal(true);
        setLocationRelativeTo(p);

        decryptButton.addActionListener(v -> {
            readFile();
        });

        encryptButton.addActionListener(v -> {

            BigInteger[] cipherText = null;
            BigInteger n = null;
            BigInteger d = null;
            String password = "";
            password = String.valueOf(txt_password.getPassword()).trim().toLowerCase();

            System.out.println("Password (Input): " + password);

            RSA rsa = new RSA(8);
            n = rsa.getN();
            d = rsa.getD();
            cipherText = rsa.encrypt(password);
            privateKey=rsa.encrypt(password);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cipherText.length; i++) {
                sb.append(cipherText[i].toString(16).toUpperCase());
                if (i != cipherText.length - 1) {
                    System.out.print(" ");
                }
            }
            String message = sb.toString();
            System.out.println();
            System.out.println("Encrypt message: " + message);
            txt_cipherText.setText(message);
            User user = new User(txt_name.getText(), txt_address.getText(), txt_phone.getText(), rsa.decrypt(cipherText, d, n));
            writeFile("E:\\User.dat", user);
            clearTxt();

        });

        setVisible(true);
    }

    void clearTxt(){
        txt_phone.setText("");
        txt_address.setText("");
        txt_name.setText("");
    }
    void readFile() {

        StringBuilder sb = new StringBuilder();
        try {
            /*FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);*/
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("E:\\User.dat"));
            Object object = ois.readObject();
            ois.close();
            if(object instanceof User){
                BigInteger[] cipherText = null;
                txt_name.setText(((User) object).getName());
                txt_address.setText(((User) object).getAddress());
                txt_phone.setText(((User) object).getPhone());
                BigInteger n = null;
                BigInteger d = null;
                RSA rsa = new RSA(8);
                cipherText = rsa.encrypt(((User)object).getPassword());
                n = rsa.getN();
                d = rsa.getD();
                String result = rsa.decrypt(cipherText, d, n);
                txt_cipherText.setText(rsa.decrypt(cipherText, d, n));
                txt_passwordPlaintext.setText(rsa.decrypt(cipherText, d, n));
                System.out.println();
                System.out.println("Decrypt message: " + rsa.decrypt(cipherText, d, n));
                String password = "";
                password = String.valueOf(txt_password.getPassword()).trim().toLowerCase();
                System.out.println("Decrypt result: " + result);

                if(password.equals(result)) JOptionPane.showMessageDialog(null,"Đăng nhập thành công");
                else JOptionPane.showMessageDialog(null,"Sai mật khẩu");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Không phải User " );

            }
        } catch (Exception e) {
            System.out.println(e.toString());
            JOptionPane.showMessageDialog(null, "Có lỗi khi ghi file: " + e.getMessage());

        }

    }

    void writeFile(String fileName, User user) {
        try {
            FileOutputStream fo = new FileOutputStream(fileName);
            BufferedOutputStream bo = new BufferedOutputStream(fo);
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(user);
            oo.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Có lỗi khi ghi file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        frm_Main frm_main = new frm_Main(null);
    }
}
