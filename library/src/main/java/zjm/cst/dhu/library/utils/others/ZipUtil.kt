package zjm.cst.dhu.library.utils.others

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.DateFormat
import java.util.Date
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Created by zjm on 2016/12/30.
 */
object ZipUtil {

    /**
     * 压缩加密后文件夹

     * @param encryptPath 加密文件夹路径
     * *
     * @param zipPath     压缩后文件路径
     * *
     * @param zipName     压缩后文件名称
     * *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun ZipEncrypt(encryptPath: String, zipPath: String, zipName: String) {
        val df = DateFormat.getDateInstance()// 设置日期格式
        println("---------------压缩文件------------------")
        println(df.format(Date()))
        val zipFile = File(encryptPath)
        val toFile = File(zipPath + zipName)
        var inputStream: InputStream? = null
        val zipOutputStream = ZipOutputStream(FileOutputStream(toFile))
        if (zipFile.isDirectory) {
            val files = zipFile.listFiles()
            for (i in files!!.indices) {
                inputStream = FileInputStream(files[i])
                val buffIn = BufferedInputStream(inputStream, 4096)
                zipOutputStream.putNextEntry(ZipEntry(zipFile.name + File.separator + files[i].name))
                println("压缩" + files[i].name + "文件")
                var temp = 0
                val data = ByteArray(4096)
                while (buffIn.read(data).let { temp = it;it != -1 }) {
                    zipOutputStream.write(data, 0, temp)
                }
                buffIn.close()
                inputStream.close()
            }
        }
        zipOutputStream.close()
        println("---------------压缩成功------------------")
        println(df.format(Date()))
    }

    /**
     * 解压加密后文件夹

     * @param zipPath 压缩文件路径
     * *
     * @param zipName 压缩文件名称
     * *
     * @param outPath 解压路径
     * *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun ZipDecrypt(zipPath: String, zipName: String, outPath: String) {
        val df = DateFormat.getDateInstance()// 设置日期格式
        println("---------------解压文件------------------")
        println(df.format(Date()))
        val sendFile = File(zipPath + zipName)
        var outFile: File? = null
        val sendZipFile = ZipFile(sendFile)
        val zipInput = ZipInputStream(FileInputStream(sendFile))
        var entry: ZipEntry? = null
        var input: InputStream? = null
        var output: OutputStream? = null
        while (zipInput.nextEntry.let { entry = it;it != null }) {
            println("解压缩" + entry!!.name + "文件")
            var name = entry!!.name
            if (entry!!.name.endsWith(".encrypt") || entry!!.name.endsWith(".key") || entry!!.name.endsWith(".sign")) {
                val s = entry!!.name.split("\\\\".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                name = s[1]
                println(name)
            }
            outFile = File(outPath + File.separator + name)
            println(outFile.path)
            if (!outFile.parentFile.exists()) {
                outFile.parentFile.mkdir()
            }
            if (!outFile.exists()) {
                outFile.createNewFile()
            }
            input = sendZipFile.getInputStream(entry)
            val buffIn = BufferedInputStream(input!!, 4096)
            output = FileOutputStream(outFile)
            var temp = 0
            val data = ByteArray(4096)
            while (buffIn.read(data).let { temp = it;it != -1 }) {
                output.write(data, 0, temp)
            }
            buffIn.close()
            input.close()
            output.close()
        }
        zipInput.close()
        sendZipFile.close()
        println("---------------解压成功------------------")
        println(df.format(Date()))
        println()
    }

}
