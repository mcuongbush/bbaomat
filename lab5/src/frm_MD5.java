import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class frm_MD5 extends JDialog {
    private JTextField txt_username;
    private JPasswordField txt_password;
    private JTextArea txt_rs1;
    private JTextArea txt_rs2;
    private JTextArea txt_string;
    private JButton loginButton;
    private JButton registerButton;
    private JPanel panelMain;
    private JRadioButton MD5RadioButton;
    private JRadioButton SHARadioButton;

    private boolean method = false;

    public frm_MD5(Frame p) {
        super(p);
        setTitle("HASH MD5");
        setContentPane(panelMain);
        setModal(true);
        setMinimumSize(new Dimension(500, 700));
        setLocationRelativeTo(p);
        ButtonGroup bg = new ButtonGroup();
        bg.add(MD5RadioButton);
        bg.add(SHARadioButton);

        MD5RadioButton.addActionListener(v -> {
            method = false;
            JOptionPane.showMessageDialog(null, "Sử dụng thuật toán MD5");
        });
        SHARadioButton.addActionListener(v -> {
            method = true;
            JOptionPane.showMessageDialog(null, "Sử dụng thuật toán SHA");
        });
        MD5RadioButton.setSelected(true);


        registerButton.addActionListener(v -> {
            try {
                String un = txt_username.getText();
                String pw = String.valueOf(txt_password.getPassword());
                String hash = "";

                hash = un + pw;

                /*MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(hash.getBytes(StandardCharsets.UTF_8));
                byte[] byteData = md.digest();*/

               /* StringBuffer sb = new StringBuffer();
                for (int i = 0; i < byteData.length; i++) {
                    sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                }
                System.out.println("Diggest(in hex format): " + sb.toString());
                txt_rs1.setText(sb.toString());*/

                /*StringBuffer hexString = new StringBuffer();
                for (int i = 0; i < byteData.length; i++) {
                    String hex = Integer.toHexString(0xff & byteData[i]);
                    if (hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
                }*/
                String hexString = HASH(hash);
                System.out.println("Diggest(in hex format): " + hexString.toString());
                txt_rs1.setText(hexString.toString());
                txt_rs2.setText(hexString.toString());
                txt_string.setText(hash.toString());

                //write file
                BufferedWriter bw = null;
                String fileName = "E:\\HashMD5.txt";

                bw = new BufferedWriter(new FileWriter(fileName));
                bw.write(hexString.toString());
                bw.close();
                JOptionPane.showMessageDialog(null, "Đăng ký thành công rùi nhóoooo <3 ");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi rùi nòoooo: " + e.getMessage());
            }
        });

        loginButton.addActionListener(v -> {
            try {
                String un = txt_username.getText();
                String pw = String.valueOf(txt_password.getPassword());

                String hash = "";
                hash = un + pw;
                BufferedReader br = new BufferedReader(new FileReader("E:\\HashMD5.txt"));
                StringBuffer sb = new StringBuffer();
                char[] ca = new char[5];
                while (br.ready()) {
                    int len = br.read(ca);
                    sb.append(ca, 0, len);
                }
                br.close();
                System.out.println(" Chung thuc: " + sb);

                String textCipher = HASH(hash);
                System.out.println(" hash un & pw: " + textCipher);

                if (textCipher.equals(sb.toString())) {
                    JOptionPane.showMessageDialog(null, "Đăng nhập đúng rùi nhoooo");
                    txt_rs1.setText(textCipher);
                    txt_rs2.setText(sb.toString());
                    txt_string.setText("UserName: " + un + "\nPassword: " + pw);
                } else JOptionPane.showMessageDialog(null, "Đăng nhập thất bại mất tiu ùiiii");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Loi no`: " + e.getMessage());
            }
        });


        setVisible(true);
    }


    private String HASH(String txt) throws Exception {
        MessageDigest md = MessageDigest.getInstance( method ? "SHA-256" : "MD5");
        md.update(txt.getBytes(StandardCharsets.UTF_8));
        byte[] byteData = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        frm_MD5 frm_md5 = new frm_MD5(null);
    }

}
