package com.byteblogs.helloblog.config.domain.vo;

import com.byteblogs.common.base.domain.vo.BaseVO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author byteblogs
 * @since 2019-08-28
 */
@Data
@Accessors(chain = true)
public class ConfigVO extends BaseVO<ConfigVO> {

    private Integer type;

    private String configKey;

    private String configValue;

}
