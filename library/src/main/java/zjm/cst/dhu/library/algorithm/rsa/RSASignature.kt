package zjm.cst.dhu.library.algorithm.rsa


import android.util.Base64

import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.text.SimpleDateFormat
import java.util.Date


/**
 * RSA签名验签工具类

 * @author ZJM
 */
object RSASignature {

    /**
     * 签名算法
     */
    val SIGN_ALGORITHMS = "SHA1WithRSA"

    /**
     * RSA签名

     * @param content    待签名数据
     * *
     * @param privateKey 商户私钥
     * *
     * @param encode     字符集编码
     * *
     * @return 签名值
     */
    fun sign(content: String, privateKey: String, encode: String): String? {
        println("---------------RSA签名过程------------------")
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS")// 设置日期格式
        println(df.format(Date()))
        try {
            val priPKCS8 = PKCS8EncodedKeySpec(Base64.decode(privateKey, Base64.DEFAULT))
            val keyf = KeyFactory.getInstance("rsa")
            val priKey = keyf.generatePrivate(priPKCS8)
            val signature = java.security.Signature.getInstance(SIGN_ALGORITHMS)
            signature.initSign(priKey)
            signature.update(content.toByteArray(charset(encode)))
            val signed = signature.sign()
            val result = Base64.encodeToString(signed, Base64.DEFAULT)
            println("签名原串：" + content)
            println("签名串：" + result)
            println(df.format(Date()))
            return result
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * RSA签名

     * @param content    待签名数据
     * *
     * @param privateKey 商户私钥
     * *
     * @return 签名值
     */
    fun sign(content: String, privateKey: String): String? {
        println("---------------RSA签名过程------------------")
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS")// 设置日期格式
        println(df.format(Date()))
        try {
            val priPKCS8 = PKCS8EncodedKeySpec(Base64.decode(privateKey, Base64.DEFAULT))
            val keyf = KeyFactory.getInstance("rsa")
            val priKey = keyf.generatePrivate(priPKCS8)
            val signature = java.security.Signature.getInstance(SIGN_ALGORITHMS)
            signature.initSign(priKey)
            signature.update(content.toByteArray())
            val signed = signature.sign()
            val result = Base64.encodeToString(signed, Base64.DEFAULT)
            println("签名原串：" + content)
            println("签名串：" + result)
            println(df.format(Date()))
            return result
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * RSA验签名检查

     * @param content   待签名数据
     * *
     * @param sign      签名值
     * *
     * @param publicKey 分配给开发商公钥
     * *
     * @param encode    字符集编码
     * *
     * @return 布尔值
     */
    fun doCheck(content: String, sign: String, publicKey: String, encode: String): Boolean {
        println("---------------RSA验签过程------------------")
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS")// 设置日期格式
        println(df.format(Date()))
        try {
            val keyFactory = KeyFactory.getInstance("rsa")
            val encodedKey = Base64.decode(publicKey, Base64.DEFAULT)
            val pubKey = keyFactory.generatePublic(X509EncodedKeySpec(encodedKey))
            val signature = java.security.Signature.getInstance(SIGN_ALGORITHMS)
            signature.initVerify(pubKey)
            signature.update(content.toByteArray(charset(encode)))
            val bverify = signature.verify(Base64.decode(sign, Base64.DEFAULT))
            if (bverify) {
                println("---------------RSA验签成功------------------")
            } else {
                println("---------------RSA验签失败------------------")
            }
            println(df.format(Date()))
            return bverify

        } catch (e: Exception) {
            e.printStackTrace()
        }

        println("---------------RSA验签失败------------------")
        return false
    }

    /**
     * RSA验签名检查

     * @param content   待签名数据
     * *
     * @param sign      签名值
     * *
     * @param publicKey 分配给开发商公钥
     * *
     * @return 布尔值
     */
    fun doCheck(content: String, sign: String, publicKey: String): Boolean {
        println("---------------RSA验签过程------------------")
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS")// 设置日期格式
        println(df.format(Date()))
        try {
            val keyFactory = KeyFactory.getInstance("rsa")
            val encodedKey = Base64.decode(publicKey, Base64.DEFAULT)
            val pubKey = keyFactory.generatePublic(X509EncodedKeySpec(encodedKey))
            val signature = java.security.Signature.getInstance(SIGN_ALGORITHMS)
            signature.initVerify(pubKey)
            signature.update(content.toByteArray())
            val bverify = signature.verify(Base64.decode(sign, Base64.DEFAULT))
            if (bverify) {
                println("---------------RSA验签成功------------------")
            } else {
                println("---------------RSA验签失败------------------")
            }
            println(df.format(Date()))
            return bverify
        } catch (e: Exception) {
            e.printStackTrace()
        }

        println("---------------RSA验签失败------------------")
        return false
    }

}
