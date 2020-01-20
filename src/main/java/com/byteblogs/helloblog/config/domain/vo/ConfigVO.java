package com.byteblogs.helloblog.config.domain.vo;

import com.byteblogs.common.base.domain.vo.BaseVO;
import com.byteblogs.common.validator.annotion.IntegerNotNull;
import com.byteblogs.common.validator.annotion.NotBlank;
import com.byteblogs.helloblog.config.domain.validator.QueryConfigList;
import com.byteblogs.helloblog.config.domain.validator.UpdateConfig;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author byteblogs
 * @since 2019-08-28
 */
@Data
@Accessors(chain = true)
public class ConfigVO extends BaseVO<ConfigVO> {
    private Integer type;

    @NotBlank(groups = {UpdateConfig.class})
    private String configKey;

    @NotBlank(groups = {UpdateConfig.class})
    private String configValue;

}
