package com.kafka.constants.props;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 针对consumer中AUTO_OFFSET_RESET的配置
 * @author zhangleimin
 * @package com.kafka.constants.props
 * @date 16-1-26
 */
@Getter
@AllArgsConstructor
public enum OffsetResetType {

    FIRST("smallest","从最小位置开始算起"),
    LAST("largest","从最大位置开始算起"),
    ERROR("anything else","向consumer抛出异常")
    ;

    private String offsetType;
    private String description;
}
