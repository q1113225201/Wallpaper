package com.sjl.wallpaper.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.Channel;

/**
 * IO工具类
 *
 */
public class IOUtil {

    private IOUtil() {
        
    }

    /**
     * 将输入流转成字符串，默认编码utf-8
     * @param in
     * @return
     * @throws IOException
     */
    public static String streamToString(InputStream in) throws IOException {
        return streamToString(in, "UTF-8");
    }

    /**
     * 将输入流转成String字符串
     * @param in InputStream 
     * @param encoding 编码
     * @return 字符串
     * @throws IOException 
     */
    public static String streamToString(InputStream in, String encoding)
            throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len;
        String result = null;
        try {
            while ((len = in.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            result = new String(baos.toByteArray(), encoding);
        } finally {
            close(in);
            close(baos);
        }
        return result;
    }

    /**
     * 将输入流转成字节数组
     * @param in
     * @return
     * @throws IOException 
     */
    public static byte[] streamToByteArray(InputStream in) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len;
        byte[] result = null;
        try {
            while ((len = in.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            result = baos.toByteArray();
        } finally {
            close(in);
        }
        return result;
    }

    /**
     * 将字符串转换为输入流
     * @param content String
     * @return ByteArrayInputStream
     */
    public static InputStream toInputStream(String content) {
        byte[] bytes = content.getBytes();
        return new ByteArrayInputStream(bytes);
    }

    /**
     * 将byte数组转换为输入流
     */
    public static InputStream byte2InputStream(byte[] b) {
        return new ByteArrayInputStream(b);
    }

    /**
     * 将字符串转换为输入流
     * @param content  需要转换的字符串
     * @param encoding  编码格式
     */
    public static InputStream toInputStream(String content, String encoding)
            throws IOException {
        byte[] bytes = encoding != null ? content.getBytes(encoding) : content
                .getBytes();
        return new ByteArrayInputStream(bytes);
    }

    /**
     * 关闭字节输入流
     * @param input
     */
    public static void close(InputStream input) {
        try {
            if (input != null)
                input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭字节输出流
     * @param output
     */
    public static void close(OutputStream output) {
        try {
            if (output != null)
                output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭字符输入流
     * @param reader
     */
    public static void close(Reader reader) {
        try {
            if (reader != null)
                reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭字符输出流
     * @param writer
     */
    public static void close(Writer writer) {
        try {
            if (writer != null)
                writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭通道
     * @param channel
     */
    public static void close(Channel channel) {
        try {
            if (channel != null)
                channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
