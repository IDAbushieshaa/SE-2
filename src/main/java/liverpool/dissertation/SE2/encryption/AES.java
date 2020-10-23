package liverpool.dissertation.SE2.encryption;

import java.nio.charset.Charset;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	
	private static SecretKeySpec secretKey = null;
	private static Cipher cipher =  null;
	
	private static void initializeSecretKey() throws Exception {
		if(AES.secretKey == null) {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            
            String secret = "SE2KeySecret";
            String salt = "SE2KeySalt";
            
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            AES.secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
		}
	}
	
	public static String generateEncryptionIV() {
		byte[] iv = new byte[16];
		new Random().nextBytes(iv);
		return Base64.getEncoder().encodeToString(iv);
	}
	
	
	private static void initializeCipher() throws Exception {
		if(AES.cipher == null) 
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	}

    public static String encrypt(String strToEncrypt, byte[] iv) {
        try {
        	AES.initializeSecretKey();
        	AES.initializeCipher();
            AES.cipher.init(Cipher.ENCRYPT_MODE, AES.secretKey, new IvParameterSpec(iv));
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, byte[] iv) {
        try {
        	AES.initializeSecretKey();
        	AES.initializeCipher();
            AES.cipher.init(Cipher.DECRYPT_MODE, AES.secretKey, new IvParameterSpec(iv));
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    } 
    
    
    public static void main(String[] args) {
    	
//    	String iv = generateEncryptionIV();
    	String iv = "EMoVxaaK2apU00iVQ1BVEg==";
    	
    	byte[] ivByte = Base64.getDecoder().decode(iv);
    	
    	System.out.println(ivByte.length);

        
    }
    
    
    
    
    
}

