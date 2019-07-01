package com.ftx.sdk.utils.security;
/**
 * 
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author lhl
 *
 */

public class MD5Util
{
	private static final Logger LOG = LoggerFactory.getLogger(MD5Util.class);
	private static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	/**
	 * MD5加密
	 * @param data 明文
	 * @return 密文
	 */
	public static String getMD5(byte[] data)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(data);
			byte[] tmp = md.digest();
			int len = tmp.length;
			char[] str = new char[len * 2];
			int k = 0;
			for (int i = 0; i < len; i++)
			{
				byte bt = tmp[i];
				str[k++] = hexDigits[bt >>> 4 & 0x0f];
				str[k++] = hexDigits[bt & 0x0f];
			}
			return new String(str);
		}
		catch (NoSuchAlgorithmException e)
		{
			LOG.error("找不到加密算法", e);
			return null;
		}
	}
	
	/**
	 * MD5加密
	 * @param arg 多个参数，明文
	 * @return 密文
	 */
	public static String getMD5(Object... arg)
	{
		StringBuilder builder = new StringBuilder();
		for (Object obj : arg)
		{
			builder.append(obj);
		}
		try
		{
			byte[] data = builder.toString().getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(data);
			byte[] tmp = md.digest();
			int len = tmp.length;
			char[] str = new char[len * 2];
			int k = 0;
			for (int i = 0; i < len; i++)
			{
				byte bt = tmp[i];
				str[k++] = hexDigits[bt >>> 4 & 0x0f];
				str[k++] = hexDigits[bt & 0x0f];
			}
			return new String(str);
		}
		catch (NoSuchAlgorithmException | UnsupportedEncodingException e)
		{
			LOG.error("MD5出错", e);
			return null;
		}
	}
	
	/**
	 * MD5加密
	 * @param input 明文
	 * @return 密文
	 */
	public static String getMD5(String input)
	{
		String result = input;
		if (input == null)
		{
			return null;
		}
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input.getBytes("UTF-8"));
			BigInteger hash = new BigInteger(1, md.digest());
			result = hash.toString(16);
			while (result.length() < 32)
			{
				result = "0" + result;
			}
		}
		catch (NoSuchAlgorithmException | UnsupportedEncodingException e)
		{
			LOG.error("MD5出错", e);
		}
		return result;
	}
	
	/**
	 * MD5加密
	 * @param txt 明文
	 * @return 密文
	 */
	public static String md5(String txt)
	{
        try
        {
             MessageDigest md = MessageDigest.getInstance("MD5");
             // 问题主要出在这里，Java的字符串是unicode编码，不受源码文件的编码影响；而PHP的编码是和源码文件的编码一致，受源码编码影响
             md.update(txt.getBytes("UTF-8"));
             StringBuffer buf=new StringBuffer();
             for(byte b:md.digest())
             {
                  buf.append(String.format("%02x", b&0xff));
             }
            return  buf.toString();
        }
        catch( Exception e )
        {
              LOG.error("MD5出错", e);
              return null;
        }
	}

	public static void main(String[] args) throws UnsupportedEncodingException
	{
		String abc = "中文";
		System.out.println(MD5Util.getMD5(abc.getBytes()));
		System.out.println(MD5Util.getMD5(abc));
		System.out.println(md5(abc));
	}
}
