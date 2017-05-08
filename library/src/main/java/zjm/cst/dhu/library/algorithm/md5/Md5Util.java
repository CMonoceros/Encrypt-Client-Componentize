package zjm.cst.dhu.library.algorithm.md5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MD5获取摘要工具类
 * 
 * @author ZJM
 *
 */
public class Md5Util {

	/**
	 * 获取MD5文件摘要
	 * 
	 * @param file
	 *            所要获取摘要的文件
	 * @return 摘要字符串
	 */
	public static String getMd5ByFile(File file) {
		System.out.println("---------------Md5获取文件摘要------------------");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");// 设置日期格式
		System.out.println(df.format(new Date()));
		String value = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			System.out.println(byteBuffer);
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			System.out.println(bi);
			value = bi.toString(16);
			System.out.println("文件摘要："+value);
			System.out.println("---------------Md5获取摘要成功------------------");
			System.out.println(df.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}
}
