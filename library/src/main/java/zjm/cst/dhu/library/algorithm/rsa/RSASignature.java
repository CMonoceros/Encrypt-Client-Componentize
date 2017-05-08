package zjm.cst.dhu.library.algorithm.rsa;


import android.util.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * RSA签名验签工具类
 *
 * @author ZJM
 */
public class RSASignature {

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    /**
     * RSA签名
     *
     * @param content    待签名数据
     * @param privateKey 商户私钥
     * @param encode     字符集编码
     * @return 签名值
     */
    public static String sign(String content, String privateKey, String encode) {
        System.out.println("---------------RSA签名过程------------------");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");// 设置日期格式
        System.out.println(df.format(new Date()));
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey,Base64.DEFAULT));
            KeyFactory keyf = KeyFactory.getInstance("rsa");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(encode));
            byte[] signed = signature.sign();
            String result = Base64.encodeToString(signed,Base64.DEFAULT);
            System.out.println("签名原串：" + content);
            System.out.println("签名串：" + result);
            System.out.println(df.format(new Date()));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RSA签名
     *
     * @param content    待签名数据
     * @param privateKey 商户私钥
     * @return 签名值
     */
    public static String sign(String content, String privateKey) {
        System.out.println("---------------RSA签名过程------------------");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");// 设置日期格式
        System.out.println(df.format(new Date()));
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey, Base64.DEFAULT));
            KeyFactory keyf = KeyFactory.getInstance("rsa");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes());
            byte[] signed = signature.sign();
            String result = Base64.encodeToString(signed, Base64.DEFAULT);
            System.out.println("签名原串：" + content);
            System.out.println("签名串：" + result);
            System.out.println(df.format(new Date()));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RSA验签名检查
     *
     * @param content   待签名数据
     * @param sign      签名值
     * @param publicKey 分配给开发商公钥
     * @param encode    字符集编码
     * @return 布尔值
     */
    public static boolean doCheck(String content, String sign, String publicKey, String encode) {
        System.out.println("---------------RSA验签过程------------------");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");// 设置日期格式
        System.out.println(df.format(new Date()));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("rsa");
            byte[] encodedKey = Base64.decode(publicKey, Base64.DEFAULT);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(encode));
            boolean bverify = signature.verify(Base64.decode(sign, Base64.DEFAULT));
            if (bverify) {
                System.out.println("---------------RSA验签成功------------------");
            } else {
                System.out.println("---------------RSA验签失败------------------");
            }
            System.out.println(df.format(new Date()));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("---------------RSA验签失败------------------");
        return false;
    }

    /**
     * RSA验签名检查
     *
     * @param content   待签名数据
     * @param sign      签名值
     * @param publicKey 分配给开发商公钥
     * @return 布尔值
     */
    public static boolean doCheck(String content, String sign, String publicKey) {
        System.out.println("---------------RSA验签过程------------------");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");// 设置日期格式
        System.out.println(df.format(new Date()));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("rsa");
            byte[] encodedKey = Base64.decode(publicKey, Base64.DEFAULT);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes());
            boolean bverify = signature.verify(Base64.decode(sign, Base64.DEFAULT));
            if (bverify) {
                System.out.println("---------------RSA验签成功------------------");
            } else {
                System.out.println("---------------RSA验签失败------------------");
            }
            System.out.println(df.format(new Date()));
            return bverify;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("---------------RSA验签失败------------------");
        return false;
    }

}
