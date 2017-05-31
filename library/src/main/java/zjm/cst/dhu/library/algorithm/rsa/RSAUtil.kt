package zjm.cst.dhu.library.algorithm.rsa

import org.apache.commons.codec.binary.Base64
import org.apache.commons.collections.map.HashedMap

import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.text.SimpleDateFormat
import java.util.Date

import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import kotlin.experimental.and


/**
 * RSA加密解密工具类

 * @author ZJM
 */
object RSAUtil {
    /**
     * 字节数据转字符串专用集合
     */
    private val HEX_CHAR = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

    /**
     * 随机生成密钥对
     */
    fun genKeyPair(): Map<String, String> {
        println("---------------RSA密钥生成过程------------------")
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS")// 设置日期格式
        println(df.format(Date()))
        val map = HashMap<String, String>()
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        var keyPairGen: KeyPairGenerator? = null
        try {
            keyPairGen = KeyPairGenerator.getInstance("rsa")
        } catch (e: NoSuchAlgorithmException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen!!.initialize(1024, SecureRandom())
        // 生成一个密钥对，保存在keyPair中
        val keyPair = keyPairGen.generateKeyPair()
        // 得到私钥
        val privateKey = keyPair.private as RSAPrivateKey
        // 得到公钥
        val publicKey = keyPair.public as RSAPublicKey
        try {
            // 得到公钥字符串
            val publicKeyString = Base64.encodeBase64String(publicKey.encoded)
            // 得到私钥字符串
            val privateKeyString = Base64.encodeBase64String(privateKey.encoded)
            map.put("public", publicKeyString)
            map.put("private", privateKeyString)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        println("---------------RSA密钥生成成功------------------")
        println(df.format(Date()))
        return map
    }


    /**
     * 从字符串中载入公钥

     * @param publicKeyStr 公钥字符串
     * *
     * @return 公钥
     * *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun loadPublicKeyByStr(publicKeyStr: String): RSAPublicKey {
        try {
            val buffer = Base64.decodeBase64(publicKeyStr)
            val keyFactory = KeyFactory.getInstance("rsa")
            val keySpec = X509EncodedKeySpec(buffer)
            return keyFactory.generatePublic(keySpec) as RSAPublicKey
        } catch (e: NoSuchAlgorithmException) {
            throw Exception("无此算法")
        } catch (e: InvalidKeySpecException) {
            throw Exception("公钥非法")
        } catch (e: NullPointerException) {
            throw Exception("公钥数据为空")
        }

    }


    /**
     * 从字符串中载入私钥

     * @param privateKeyStr 私钥字符串
     * *
     * @return 私钥
     * *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun loadPrivateKeyByStr(privateKeyStr: String): RSAPrivateKey {
        try {
            val buffer = Base64.decodeBase64(privateKeyStr)
            val keySpec = PKCS8EncodedKeySpec(buffer)
            val keyFactory = KeyFactory.getInstance("rsa")
            return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
        } catch (e: NoSuchAlgorithmException) {
            throw Exception("无此算法")
        } catch (e: InvalidKeySpecException) {
            throw Exception("私钥非法")
        } catch (e: NullPointerException) {
            throw Exception("私钥数据为空")
        }

    }

    /**
     * 公钥加密过程

     * @param publicKey     公钥
     * *
     * @param plainTextData 明文数据
     * *
     * @return 密文数据
     * *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun encrypt(publicKey: RSAPublicKey?, plainTextData: ByteArray): ByteArray? {
        println("---------------RSA公钥加密过程------------------")
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS")// 设置日期格式
        println(df.format(Date()))
        if (publicKey == null) {
            throw Exception("加密公钥为空, 请设置")
        }
        var cipher: Cipher? = null
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("rsa")
            // cipher= Cipher.getInstance("rsa", new BouncyCastleProvider());
            cipher!!.init(Cipher.ENCRYPT_MODE, publicKey)
            val output = cipher.doFinal(plainTextData)
            println("---------------RSA公钥加密成功------------------")
            println(df.format(Date()))
            return output
        } catch (e: NoSuchAlgorithmException) {
            throw Exception("无此加密算法")
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
            return null
        } catch (e: InvalidKeyException) {
            throw Exception("加密公钥非法,请检查")
        } catch (e: IllegalBlockSizeException) {
            throw Exception("明文长度非法")
        } catch (e: BadPaddingException) {
            throw Exception("明文数据已损坏")
        }

    }

    /**
     * 私钥加密过程

     * @param privateKey    私钥
     * *
     * @param plainTextData 明文数据
     * *
     * @return 密文数据
     * *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun encrypt(privateKey: RSAPrivateKey?, plainTextData: ByteArray): ByteArray? {
        println("---------------RSA私钥加密过程------------------")
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS")// 设置日期格式
        println(df.format(Date()))
        if (privateKey == null) {
            throw Exception("加密私钥为空, 请设置")
        }
        var cipher: Cipher? = null
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("rsa")
            cipher!!.init(Cipher.ENCRYPT_MODE, privateKey)
            val output = cipher.doFinal(plainTextData)
            println("---------------RSA私钥加密成功------------------")
            println(df.format(Date()))
            return output
        } catch (e: NoSuchAlgorithmException) {
            throw Exception("无此加密算法")
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
            return null
        } catch (e: InvalidKeyException) {
            throw Exception("加密私钥非法,请检查")
        } catch (e: IllegalBlockSizeException) {
            throw Exception("明文长度非法")
        } catch (e: BadPaddingException) {
            throw Exception("明文数据已损坏")
        }

    }

    /**
     * 私钥解密过程

     * @param privateKey 私钥
     * *
     * @param cipherData 密文数据
     * *
     * @return 明文数据
     * *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun decrypt(privateKey: RSAPrivateKey?, cipherData: ByteArray): ByteArray? {
        println("---------------RSA私钥解密过程------------------")
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS")// 设置日期格式
        println(df.format(Date()))
        if (privateKey == null) {
            throw Exception("解密私钥为空, 请设置")
        }
        var cipher: Cipher? = null
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("rsa")
            // cipher= Cipher.getInstance("rsa", new BouncyCastleProvider());
            cipher!!.init(Cipher.DECRYPT_MODE, privateKey)
            val output = cipher.doFinal(cipherData)
            println("---------------RSA私钥解密成功------------------")
            println(df.format(Date()))
            return output
        } catch (e: NoSuchAlgorithmException) {
            throw Exception("无此解密算法")
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
            return null
        } catch (e: InvalidKeyException) {
            throw Exception("解密私钥非法,请检查")
        } catch (e: IllegalBlockSizeException) {
            throw Exception("密文长度非法")
        } catch (e: BadPaddingException) {
            throw Exception("密文数据已损坏")
        }

    }

    /**
     * 公钥解密过程

     * @param publicKey  公钥
     * *
     * @param cipherData 密文数据
     * *
     * @return 明文数据
     * *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun decrypt(publicKey: RSAPublicKey?, cipherData: ByteArray): ByteArray? {
        println("---------------RSA公钥解密过程------------------")
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS")// 设置日期格式
        println(df.format(Date()))
        if (publicKey == null) {
            throw Exception("解密公钥为空, 请设置")
        }
        var cipher: Cipher? = null
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("rsa")
            // cipher= Cipher.getInstance("rsa", new BouncyCastleProvider());
            cipher!!.init(Cipher.DECRYPT_MODE, publicKey)
            val output = cipher.doFinal(cipherData)
            println("---------------RSA公钥解密成功------------------")
            println(df.format(Date()))
            return output
        } catch (e: NoSuchAlgorithmException) {
            throw Exception("无此解密算法")
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
            return null
        } catch (e: InvalidKeyException) {
            throw Exception("解密公钥非法,请检查")
        } catch (e: IllegalBlockSizeException) {
            throw Exception("密文长度非法")
        } catch (e: BadPaddingException) {
            throw Exception("密文数据已损坏")
        }

    }

    /**
     * 字节数据转十六进制字符串

     * @param data 输入数据
     * *
     * @return 十六进制内容
     */
    fun byteArrayToString(data: ByteArray): String {
        val stringBuilder = StringBuilder()
        for (i in data.indices) {
            // 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] and 0xf0.toByte()).toInt().ushr(4)])
            // 取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] and 0x0f).toInt()])
            if (i < data.size - 1) {
                stringBuilder.append(' ')
            }
        }
        return stringBuilder.toString()
    }
}
