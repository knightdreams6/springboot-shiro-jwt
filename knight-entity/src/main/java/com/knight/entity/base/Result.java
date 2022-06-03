package com.knight.entity.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knight.entity.enums.ErrorState;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lixiao
 * @date 2019/7/31 14:46
 */
@Data
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 状态码 */
	private Integer code;

	/** 消息 */
	private String msg;

	/** 数据 */
	private T data;

	public Result(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Result(Integer code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	/**
	 * 根据boolean值返回正确或错误消息
	 * @param flag 标识
	 * @return {@link Result}<{@link Object}>
	 */
	public static Result<Object> bool(boolean flag) {
		return flag ? Result.success() : Result.error();
	}

	/**
	 * 分页查询结果转换
	 * @param pageResult 分页查询结果
	 * @param convert 转换方法
	 * @return {@link Result}<{@link IPage}<{@link S}>>
	 */
	public static <T, S> Result<IPage<S>> page(IPage<T> pageResult, Function<T, S> convert) {
		IPage<S> result = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal(),
				pageResult.searchCount());
		List<S> records = pageResult.getRecords().stream().map(convert).collect(Collectors.toList());
		result.setRecords(records);
		return Result.success(result);
	}

	/**
	 * 分页查询结果转换
	 * @param listResult 分页查询结果
	 * @param convert 转换方法
	 * @return {@link Result}<{@link IPage}<{@link S}>>
	 */
	public static <T, S> Result<List<S>> list(List<T> listResult, Function<T, S> convert) {
		List<S> records = listResult.stream().map(convert).collect(Collectors.toList());
		return Result.success(records);
	}

	/**
	 * 返回成功消息
	 * @return 成功消息
	 */
	public static Result<Object> success() {
		return Result.success(null);
	}

	/**
	 * 返回成功数据
	 * @return 成功消息
	 */
	public static <T> Result<T> success(T data) {
		ErrorState successState = ErrorState.SUCCESS;
		return new Result<>(successState.getCode(), successState.getMsg(), data);
	}

	/**
	 * 返回错误消息
	 */
	public static Result<Object> error() {
		return Result.error(ErrorState.FAIL);
	}

	/**
	 * 返回错误消息
	 * @param errorState 错误信息枚举类
	 * @return 警告消息
	 */
	public static Result<Object> error(ErrorState errorState) {
		return new Result<>(errorState.getCode(), errorState.getMsg());
	}

}
