package com.kafka.utils;

/**
 * 处理单行数据
 * @author zhangleimin
 * @package com.kafka.utils
 * @date 16-3-4
 */
public interface LineProcessor {

    boolean process(String line);
}
