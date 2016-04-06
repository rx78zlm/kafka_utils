package com.kafka.utils;

import com.google.common.base.Strings;

import java.io.*;

/**
 * 文件处理
 * @author zhangleimin
 * @package com.kafka.utils
 * @date 16-3-4
 */
public class FileProcessor {

    /**
     * 处理文件输入流
     * @param reader    文件输入流
     * @param lineProcessor 单行处理方式
     * @throws IOException
     */
    public static boolean processFile(BufferedReader reader, LineProcessor lineProcessor) throws IOException {
        String line;
        if (lineProcessor == null) {
            return false;
        }
        while ((line = reader.readLine()) != null) {
            if (!Strings.isNullOrEmpty(line)) {
                if (!lineProcessor.process(line)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 处理文件输入流
     * @param inputStream   文件输入流
     * @param lineProcessor 单行处理方式
     * @param charset  字符编码
     * @throws IOException
     */
    public static boolean processFile(InputStream inputStream, LineProcessor lineProcessor, String charset) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
        return processFile(reader, lineProcessor);
    }

    /**
     * 处理文件输入流
     * @param inputStream   文件输入流
     * @param lineProcessor 单行处理方式
     * @throws IOException
     */
    public static boolean processFile(InputStream inputStream, LineProcessor lineProcessor) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        return processFile(reader, lineProcessor);
    }

    /**
     * 处理文件
     * @param file   文件
     * @param lineProcessor 单行处理方式
     * @throws IOException
     */
    public static boolean processFile(File file, LineProcessor lineProcessor) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return processFile(reader, lineProcessor);
    }
}
