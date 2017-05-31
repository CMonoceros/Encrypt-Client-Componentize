package zjm.cst.dhu.library.algorithm.md5

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.math.BigInteger
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date

/**
 * MD5获取摘要工具类

 * @author ZJM
 */
object Md5Util {

    /**
     * 获取MD5文件摘要

     * @param file
     * *            所要获取摘要的文件
     * *
     * @return 摘要字符串
     */
    fun getMd5ByFile(file: File): String? {
        println("---------------Md5获取文件摘要------------------")
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS")// 设置日期格式
        println(df.format(Date()))
        var value: String? = null
        var `in`: FileInputStream? = null
        try {
            `in` = FileInputStream(file)
        } catch (e1: FileNotFoundException) {
            e1.printStackTrace()
        }

        try {
            val byteBuffer = `in`!!.channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length())
            println(byteBuffer)
            val md5 = MessageDigest.getInstance("MD5")
            md5.update(byteBuffer)
            val bi = BigInteger(1, md5.digest())
            println(bi)
            value = bi.toString(16)
            println("文件摘要：" + value!!)
            println("---------------Md5获取摘要成功------------------")
            println(df.format(Date()))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (null != `in`) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return value
    }
}
