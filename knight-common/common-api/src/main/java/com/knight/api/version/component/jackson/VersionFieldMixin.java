package com.knight.api.version.component.jackson;

import com.fasterxml.jackson.annotation.JsonFilter;

/**
 * 版本字段过滤Mixin
 *
 * @author knight
 * @since 2023/12/06
 */
@JsonFilter(VersionFieldMixin.VERSION_FIELD_FILTER)
public abstract class VersionFieldMixin {

	public static final String VERSION_FIELD_FILTER = "versionField";

	private VersionFieldMixin() {
	}

}
