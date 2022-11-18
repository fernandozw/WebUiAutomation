package com.thinkingdata.lib;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;



/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
public class SHAUtil {

	public static String getTicket(String id) {
		return encryptToSHA(System.currentTimeMillis() + "@" + id);
	}

	/**
	 * 进行SHA加密
	 *
	 * @param info
	 *            要加密的信息
	 * @return String 加密后的字符串
	 */
	public static String encryptToSHA(String info) {
		if (info == null) {
			return null;
		}
		return Digest(info);
	}

	private static final int[] abcde = { 0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476, 0xc3d2e1f0 };
	// 摘要数据存储数组
	private static int[] digestInt = new int[5];
	// 计算过程中的临时数据存储数组
	private static int[] tmpData = new int[80];

	/** * 计算sha-1摘要 * * @param bytedata * @return */
	private static int process_input_bytes(byte[] bytedata) {
		// 初试化常量
		System.arraycopy(abcde, 0, digestInt, 0, abcde.length);
		// 格式化输入字节数组，补10及长度数据
		byte[] newbyte = byteArrayFormatData(bytedata);
		// 获取数据摘要计算的数据单元个数
		int MCount = newbyte.length / 64;
		// 循环对每个数据单元进行摘要计算
		for (int pos = 0; pos < MCount; pos++) {
			// 将每个单元的数据转换成16个整型数据，并保存到tmpData的前16个数组元素中
			for (int j = 0; j < 16; j++) {
				tmpData[j] = byteArrayToInt(newbyte, (pos * 64) + (j * 4));
			}
			// 摘要计算函数
			encrypt();
		}
		return 20;
	}

	/** * 格式化输入字节数组格式 * * @param bytedata * @return */
	private static byte[] byteArrayFormatData(byte[] bytedata) {
		// 补0数量
		int zeros = 0;
		// 补位后总位数
		int size = 0;
		// 原始数据长度
		int n = bytedata.length;
		// 模64后的剩余位数
		int m = n % 64;
		// 计算添加0的个数以及添加10后的总长度
		if (m < 56) {
			zeros = 55 - m;
			size = n - m + 64;
		} else if (m == 56) {
			zeros = 63;
			size = n + 8 + 64;
		} else {
			zeros = 63 - m + 56;
			size = (n + 64) - m + 64;
		}
		// 补位后生成的新数组内容
		byte[] newbyte = new byte[size];
		// 复制数组的前面部分
		System.arraycopy(bytedata, 0, newbyte, 0, n);
		// 获得数组Append数据元素的位置
		int l = n;
		// 补1操作
		newbyte[l++] = (byte) 0x80;
		// 补0操作
		for (int i = 0; i < zeros; i++) {
			newbyte[l++] = (byte) 0x00;
		}
		// 计算数据长度，补数据长度位共8字节，长整型
		long N = (long) n * 8;
		byte h8 = (byte) (N & 0xFF);
		byte h7 = (byte) ((N >> 8) & 0xFF);
		byte h6 = (byte) ((N >> 16) & 0xFF);
		byte h5 = (byte) ((N >> 24) & 0xFF);
		byte h4 = (byte) ((N >> 32) & 0xFF);
		byte h3 = (byte) ((N >> 40) & 0xFF);
		byte h2 = (byte) ((N >> 48) & 0xFF);
		byte h1 = (byte) (N >> 56);
		newbyte[l++] = h1;
		newbyte[l++] = h2;
		newbyte[l++] = h3;
		newbyte[l++] = h4;
		newbyte[l++] = h5;
		newbyte[l++] = h6;
		newbyte[l++] = h7;
		newbyte[l++] = h8;
		return newbyte;
	}

	private static int f1(int x, int y, int z) {
		return (x & y) | (~x & z);
	}

	private static int f2(int x, int y, int z) {
		return x ^ y ^ z;
	}

	private static int f3(int x, int y, int z) {
		return (x & y) | (x & z) | (y & z);
	}

	private static int f4(int x, int y) {
		return (x << y) | x >>> (32 - y);
	}

	/** * 单元摘要计算函数 */
	private static void encrypt() {
		for (int i = 16; i <= 79; i++) {
			tmpData[i] = f4(tmpData[i - 3] ^ tmpData[i - 8] ^ tmpData[i - 14] ^ tmpData[i - 16], 1);
		}
		int[] tmpabcde = new int[5];
		for (int i1 = 0; i1 < tmpabcde.length; i1++) {
			tmpabcde[i1] = digestInt[i1];
		}
		for (int j = 0; j <= 19; j++) {
			int tmp = f4(tmpabcde[0], 5) + f1(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + tmpData[j]
					+ 0x5a827999;
			tmpabcde[4] = tmpabcde[3];
			tmpabcde[3] = tmpabcde[2];
			tmpabcde[2] = f4(tmpabcde[1], 30);
			tmpabcde[1] = tmpabcde[0];
			tmpabcde[0] = tmp;
		}
		for (int k = 20; k <= 39; k++) {
			int tmp = f4(tmpabcde[0], 5) + f2(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + tmpData[k]
					+ 0x6ed9eba1;
			tmpabcde[4] = tmpabcde[3];
			tmpabcde[3] = tmpabcde[2];
			tmpabcde[2] = f4(tmpabcde[1], 30);
			tmpabcde[1] = tmpabcde[0];
			tmpabcde[0] = tmp;
		}
		for (int l = 40; l <= 59; l++) {
			int tmp = f4(tmpabcde[0], 5) + f3(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + tmpData[l]
					+ 0x8f1bbcdc;
			tmpabcde[4] = tmpabcde[3];
			tmpabcde[3] = tmpabcde[2];
			tmpabcde[2] = f4(tmpabcde[1], 30);
			tmpabcde[1] = tmpabcde[0];
			tmpabcde[0] = tmp;
		}
		for (int m = 60; m <= 79; m++) {
			int tmp = f4(tmpabcde[0], 5) + f2(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + tmpData[m]
					+ 0xca62c1d6;
			tmpabcde[4] = tmpabcde[3];
			tmpabcde[3] = tmpabcde[2];
			tmpabcde[2] = f4(tmpabcde[1], 30);
			tmpabcde[1] = tmpabcde[0];
			tmpabcde[0] = tmp;
		}
		for (int i2 = 0; i2 < tmpabcde.length; i2++) {
			digestInt[i2] = digestInt[i2] + tmpabcde[i2];
		}
		for (int n = 0; n < tmpData.length; n++) {
			tmpData[n] = 0;
		}
	}

	/** * 4字节数组转换为整数 * * @param bytedata * @param i * @return */
	private static int byteArrayToInt(byte[] bytedata, int i) {
		return ((bytedata[i] & 0xff) << 24) | ((bytedata[i + 1] & 0xff) << 16) | ((bytedata[i + 2] & 0xff) << 8)
				| (bytedata[i + 3] & 0xff);
	}

	// /** * 整数转换为4字节数组 * * @param intValue * @param byteData * @param i */
	private static void intToByteArray(int intValue, byte[] byteData, int i) {
		byteData[i] = (byte) (intValue >>> 24);
		byteData[i + 1] = (byte) (intValue >>> 16);
		byteData[i + 2] = (byte) (intValue >>> 8);
		byteData[i + 3] = (byte) intValue;
	}

	/** * 将字节转换为十六进制字符串 * * @param ib * @return */
	private static String byteToHexString(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0F];
		ob[1] = Digit[ib & 0X0F];
		String s = new String(ob);
		return s;
	}

	/** * 将字节数组转换为十六进制字符串 * * @param bytearray * @return */
	private static String byteArrayToHexString(byte[] bytearray) {
		String strDigest = "";
		for (int i = 0; i < bytearray.length; i++) {
			strDigest += byteToHexString(bytearray[i]);
		}
		return strDigest;
	}

	/** * 计算sha-1摘要，返回相应的字节数组 * * @param byteData * @return */
	public static byte[] getDigestOfBytes(byte[] byteData) {
		process_input_bytes(byteData);
		byte[] digest = new byte[20];
		for (int i = 0; i < digestInt.length; i++) {
			intToByteArray(digestInt[i], digest, i * 4);
		}
		return digest;
	}

	/** * 计算sha-1摘要，返回相应的十六进制字符串 * * @param byteData * @return */
	public static String getDigestOfString(byte[] byteData) {
		return byteArrayToHexString(getDigestOfBytes(byteData));
	}

	/** * @param data * @return */
	public static String Digest(String data) {
		return getDigestOfString(data.getBytes());
	}

	/** * @param data * @return */
	public static String Digest(String data, String encode) {
		try {
			return getDigestOfString(data.getBytes(encode));
		} catch (UnsupportedEncodingException e) {
			return Digest(data);
		}
	}

	/** * @param text * @return */
	public static String SHA1Digest(String text) {
		String pwd = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(text.getBytes());
			pwd = new BigInteger(1, md.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {

		}
		return pwd;
	}

	/**
	 * jdk自带方式，会丢失前两位
	 * 
	 * @param inStr
	 * @return
	 */
	public static String SHA1(String inStr) {
		MessageDigest md = null;
		String outStr = "";
		try {
			md = MessageDigest.getInstance("SHA-1"); // 选择SHA-1，也可以选择MD5
			byte[] digest = md.digest(inStr.getBytes()); // 返回的是byet[]，要转化为String存储比较方便
			outStr = bytetoString(digest);
		} catch (NoSuchAlgorithmException nsae) {
		}
		return outStr;
	}

	public static String bytetoString(byte[] digest) {
		String str = "";
		String tempStr = "";

		for (int i = 1; i < digest.length; i++) {
			tempStr = (Integer.toHexString(digest[i] & 0xff));
			if (tempStr.length() == 1) {
				str = str + "0" + tempStr;
			} else {
				str = str + tempStr;
			}
		}
		return str.toLowerCase();
	}

	public static String getsignature(String[] sourceArray) {
		Arrays.sort(sourceArray);
		String s = "";
		for (int i = 0; i < sourceArray.length; i++) {
			s += sourceArray[i];
		}
		String signature = SHAUtil.SHA1(s);
		return signature;
	}
	/*
	 * @Test public static String getsignature(String[] sourceArray){ String[]
	 * arrTmp={"E17A3976F8F3FCD5B20A3DD0A258D928EE2ADB09","1517309133759","516"}
	 * ; Arrays.sort(arrTmp); String s = StringUtils.join(arrTmp, ""); //String
	 * signature = SHAUtil.SHA1(s).substring(2); String signature =
	 * SHAUtil.SHA1(s); System.out.println("signature:"+signature); JSONObject
	 * jsonObject = JSONObject.fromObject(
	 * "{\"status\":\"SUCCESS\",\"result\":{\"userRoles\":\"3,2,1\",\"continueDays\":15,\"ticket\":\"ADBF83A31652DC9AFA9CB27744E65516E0CD5978\",\"clinicType\":\"1\",\"continueEvent\":0,\"integral\":5,\"mobile\":\"18863029197\",\"imageRoot\":\"https://imagetest.pawjzs.com:4443/\",\"systime\":\"1517309909\",\"casUuid\":\"99f04e9e-768a-4d32-8f95-bc663a7b265d\",\"userId\":\"47264\"},\"successMsg\":\"用户名密码登录成功\",\"errorCode\":null,\"errorMsg\":null,\"warningMsg\":null}"
	 * ); JSONObject result =
	 * JSONObject.fromObject(jsonObject.getString("result")); String ticket =
	 * result.getString("ticket"); System.out.println(ticket); return signature;
	 * }
	 */
}
