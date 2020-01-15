package com.learn.project.project.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *  角色表
 * </p>
 *
 * @author knight
 * @since 2019-12-17
 */
@TableName("project_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 角色id
     */
    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String description;

}
