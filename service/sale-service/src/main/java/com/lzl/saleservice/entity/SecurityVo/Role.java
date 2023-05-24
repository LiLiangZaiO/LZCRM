package com.lzl.saleservice.entity.SecurityVo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@Data
@ApiModel(value="Role对象", description="")
public class Role implements Serializable, GrantedAuthority {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "角色id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色代码")
    private String roleName;

    @ApiModelProperty(value = "角色描述")
    private String description;

    @JsonIgnore
    @Override
    public String getAuthority() {
        return roleName;
    }
}
