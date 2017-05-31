package zjm.cst.dhu.library.algorithm.des

import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.Date

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

/**
 * DES加密工具类

 * @author ZJM
 */
object DesUtil {
    private val iv = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8)

    /**
     * 根据DES密钥进行加密

     * @param data
     * *            所要加密数据的字节数组
     * *
     * @param key
     * *            密钥字节数组
     * *
     * @return 加密后数据字节数组
     * *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun encrypt(data: ByteArray, key: ByteArray): ByteArray {
        println("---------------DES加密文件------------------")
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS")// 设置日期格式
        println(df.format(Date()))
        // 生成一个可信任的随机数源
        val sr = SecureRandom()
        // 从原始密钥数据创建DESKeySpec对象
        val dks = DESKeySpec(key)
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        val keyFactory = SecretKeyFactory.getInstance("des")
        val securekey = keyFactory.generateSecret(dks)
        // Cipher对象实际完成加密操作
        val cipher = Cipher.getInstance("des/ECB/PKCS5Padding")
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr)
        val result = cipher.doFinal(data)
        println("---------------DES加密成功------------------")
        println(df.format(Date()))
        return result
    }

    /**
     * 根据DES密钥进行解密

     * @param data
     * *            所要解密数据的字节数组
     * *
     * @param key
     * *            密钥字节数组
     * *
     * @return 解密后数据字节数组
     * *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun decrypt(data: ByteArray, key: ByteArray): ByteArray {
        println("---------------DES解密文件------------------")
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS")// 设置日期格式
        println(df.format(Date()))
        // 生成一个可信任的随机数源
        val sr = SecureRandom()
        // 从原始密钥数据创建DESKeySpec对象
        val dks = DESKeySpec(key)
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        val keyFactory = SecretKeyFactory.getInstance("des")
        val securekey = keyFactory.generateSecret(dks)
        // Cipher对象实际完成解密操作
        val cipher = Cipher.getInstance("des")
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr)
        val result = cipher.doFinal(data)
        println("---------------DES解密成功------------------")
        println(df.format(Date()))
        return result
    }

}
