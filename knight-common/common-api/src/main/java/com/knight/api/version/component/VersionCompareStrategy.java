package com.knight.api.version.component;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * 版本比较策略
 *
 * @author knight
 * @date 2023/12/07
 */
public enum VersionCompareStrategy implements BiFunction<Integer, Integer, Boolean> {

	/**
	 * >
	 */
	GT() {
		@Override
		public Boolean apply(Integer currentVersion, Integer fieldVersion) {
			return currentVersion > fieldVersion;
		}
	},

	/**
	 * <
	 */
	LT() {
		@Override
		public Boolean apply(Integer currentVersion, Integer fieldVersion) {
			return currentVersion < fieldVersion;
		}
	},

	/**
	 * >=
	 */
	GE {
		@Override
		public Boolean apply(Integer currentVersion, Integer fieldVersion) {
			return currentVersion >= fieldVersion;
		}
	},

	/**
	 * <=
	 */
	LE {
		@Override
		public Boolean apply(Integer currentVersion, Integer fieldVersion) {
			return currentVersion <= fieldVersion;
		}
	},

	/**
	 * ==
	 */
	EQ() {
		@Override
		public Boolean apply(Integer currentVersion, Integer fieldVersion) {
			return Objects.equals(currentVersion, fieldVersion);
		}
	}

}
