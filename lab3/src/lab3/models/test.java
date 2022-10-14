package lab3.models;

import java.math.BigInteger;
import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String nhash;
        BigInteger [] cipherText = null;
        BigInteger n = null;
        BigInteger d = null;
        String password ="";
        System.out.println("Enter text tobe  Encrypted");
        password=sc.nextLine();
        System.out.println("Password (Input): " + password);

        RSA rsa = new RSA(8);
        n=rsa.getN();
        d= rsa.getD();
        cipherText=rsa.encrypt(password);
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i <cipherText.length;i++){
            sb.append(cipherText[i].toString(16).toUpperCase());
            if(i!= cipherText.length-1){
                System.out.print(" ");
            }
        }
        String message =sb.toString();
        System.out.println();
        System.out.println("Encrypt message: " +message);

        String dhash = rsa.decrypt(cipherText,d,n);
        System.out.println();
        System.out.println("Decrypt message: "+dhash);

    }
}
