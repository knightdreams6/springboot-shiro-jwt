package com.knight.entity.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knight.entity.enums.CommonResultConstants;
import com.knight.entity.enums.IResultConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lixiao
 * @since 2019/7/31 14:46
 */
@Data
@NoArgsConstructor
public class R<T> {

	/**
	 * 状态码
	 */
	private Integer code;

	/**
	 * 消息
	 */
	private String msg;

	/**
	 * 数据
	 */
	private T data;

	public R(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public R(Integer code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	/**
	 * 根据boolean值返回正确或错误消息
	 * @param flag 标识
	 * @return {@link R}<{@link Void}>
	 */
	public static R<Void> bool(boolean flag) {
		return flag ? R.ok() : R.failed();
	}

	/**
	 * 分页查询结果转换
	 * @param pageResult 分页查询结果
	 * @param convert 转换方法
	 * @return {@link R}<{@link IPage}<{@link S}>>
	 */
	public static <T, S> R<IPage<S>> page(IPage<T> pageResult, Function<T, S> convert) {
		IPage<S> result = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal(),
				pageResult.searchCount());
		List<S> records = pageResult.getRecords().stream().map(convert).collect(Collectors.toList());
		result.setRecords(records);
		return R.ok(result);
	}

	/**
	 * 分页查询结果转换
	 * @param listResult 分页查询结果
	 * @param convert 转换方法
	 * @return {@link R}<{@link IPage}<{@link S}>>
	 */
	public static <T, S> R<List<S>> list(List<T> listResult, Function<T, S> convert) {
		List<S> records = listResult.stream().map(convert).collect(Collectors.toList());
		return R.ok(records);
	}

	/**
	 * 返回成功消息
	 * @return 成功消息
	 */
	public static <T> R<T> ok() {
		return R.ok(null);
	}

	/**
	 * 返回成功数据
	 * @return 成功消息
	 */
	public static <T> R<T> ok(T data) {
		CommonResultConstants successState = CommonResultConstants.SUCCESS;
		return new R<>(successState.getCode(), successState.getMsg(), data);
	}

	/**
	 * 返回错误消息
	 */
	public static <T> R<T> failed() {
		return R.failed(CommonResultConstants.FAIL);
	}

	/**
	 * 返回错误消息
	 */
	public static <T> R<T> failed(String msg) {
		return new R<>(CommonResultConstants.FAIL.getCode(), msg);
	}

	/**
	 * 返回错误消息
	 * @param errorState 错误信息枚举类
	 * @return 警告消息
	 */
	public static <T> R<T> failed(IResultConstants errorState) {
		return new R<>(errorState.code(), errorState.msg());
	}

	public static <T> R<T> failed(T data, IResultConstants resultConstants) {
		return restResult(data, resultConstants.code(), resultConstants.msg());
	}

	public static <T, B> R<T> vo(B bean, Function<B, T> func) {
		if (Objects.isNull(bean)) {
			return R.failed(CommonResultConstants.EMPTY_DATA);
		}
		return R.ok(func.apply(bean));
	}

	public static <T> R<T> restResult(T data, int code, String msg) {
		R<T> apiResult = new R<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}

	public boolean isSuccess() {
		return Objects.equals(CommonResultConstants.SUCCESS.code(), this.getCode());
	}

}
