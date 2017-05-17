/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package halo.models;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Phan Hieu
 */
public class AlgorithmRSA {
    private BigInteger n, d, e;

    public BigInteger getN() {
        return n;
    }

    public void setN(BigInteger n) {
        this.n = n;
    }

    //private key
    public BigInteger getD() {
        return d;
    }

    public void setD(BigInteger d) {
        this.d = d;
    }

    //public key
    public BigInteger getE() {
        return e;
    }

    public void setE(BigInteger e) {
        this.e = e;
    }

    /**
     * Create an instance that can encrypt using someone elses public key.
     */
    public AlgorithmRSA(BigInteger newn, BigInteger newe) {
        n = newn;
        e = newe;
    }

    /**
     * Create an instance that can both encrypt and decrypt.
     */
    public AlgorithmRSA() {

    }

    public void KeyRSA(int bits) {
        SecureRandom r = new SecureRandom();//create BigInteger r random
        BigInteger p = new BigInteger(bits, 100, r);
        BigInteger q = new BigInteger(bits, 100, r);
        n = p.multiply(q);
        BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        while (true) {
            e = new BigInteger(bits, 50, r);
            if (m.gcd(e).equals(BigInteger.ONE) && e.compareTo(m) < 0) {
                break;
            }
        }
        d = e.modInverse(m);
    }

    //Encrypt the given plaintext message.Use public key decrypt
    public synchronized String encrypt(String message) {
        return (new BigInteger(message.getBytes())).modPow(e, n).toString();
    }

    //Encrypt the given plaintext message.Use public key decrypt
    public synchronized BigInteger encrypt(BigInteger message) {
        return message.modPow(e, n);
    }
    
    //Encrypt the given plaintext message.Use public key decrypt
    public static synchronized String encrypt(String message, String nn, String ee) {
        try {
            return (new BigInteger(message.getBytes("UTF8"))).modPow(new BigInteger(ee), new BigInteger(nn)).toString();
        } catch (UnsupportedEncodingException ex) {
            return (new BigInteger(message.getBytes())).modPow(new BigInteger(ee), new BigInteger(nn)).toString();
        }
    }

    //Decrypt the given ciphertext message.Use private key decrypt
    public synchronized String decrypt(String message) {
        try {
            return new String((new BigInteger(message)).modPow(d, n).toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return new String((new BigInteger(message)).modPow(d, n).toByteArray());
        }
    }

    //Decrypt the given ciphertext message.Use private key decrypt
    public synchronized BigInteger decrypt(BigInteger message) {
        return message.modPow(d, n);
    }

    void setN(int bitleg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
