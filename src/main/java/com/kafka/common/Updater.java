package com.kafka.common;

/**
 * 用于做一些更新操作
 * @author zhangleimin
 * @package com.kafka.common
 * @date 16-3-22
 */
public interface Updater {

    /**
     * 更新数据
     * @return  更新成功或失败
     */
    public boolean update();

    /**
     * 更新数据
     * @param param 目标数据
     * @return  更新成功或失败
     */
    public boolean update(Object param);
}
