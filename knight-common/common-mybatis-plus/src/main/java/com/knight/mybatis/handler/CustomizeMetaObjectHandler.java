package com.knight.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author lixiao
 * @date 2020/5/3 21:39 数据填充
 */
@Component
@Slf4j
public class CustomizeMetaObjectHandler implements MetaObjectHandler {

	/** 删除日期字段 */
	public static final String DELETE_DATE = "deleteDate";

	/** 删除字段 */
	public static final String DELETED = "deleted";

	/** 更新日期字段 */
	public static final String UPDATE_DATE = "updateDate";

	/** 创建日期字段 */
	public static final String CREATE_DATE = "createDate";

	/** 删除标记 */
	public static final int DELETED_FLAG = 1;

	@Override
	public void insertFill(MetaObject metaObject) {
		this.strictInsertFill(metaObject, CREATE_DATE, LocalDateTime.class, LocalDateTime.now());
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		this.strictUpdateFill(metaObject, UPDATE_DATE, LocalDateTime.class, LocalDateTime.now());
		if (metaObject.hasGetter(DELETED)) {
			// 判断删除字段值
			Object deletedVal = metaObject.getValue(DELETED);
			if (deletedVal != null && (Integer) deletedVal == DELETED_FLAG) {
				boolean deleteDate = metaObject.hasSetter(DELETE_DATE);
				if (deleteDate) {
					// 更新删除时间
					this.strictUpdateFill(metaObject, DELETE_DATE, LocalDateTime.class, LocalDateTime.now());
				}
			}
		}
	}

}
