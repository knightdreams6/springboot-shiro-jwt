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
 *  用户角色关联表
 * </p>
 *
 * @author knight
 * @since 2019-12-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("project_con_user_role")
public class ConUserRole implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户角色关联表id
     */
    @TableId(value = "user_role_id", type = IdType.AUTO)
    private Integer userRoleId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 角色id
     */
    private Integer roleId;
}
