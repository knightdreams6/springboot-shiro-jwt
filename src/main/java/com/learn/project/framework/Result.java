package com.learn.project.framework;

import com.learn.project.common.enums.ErrorState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lixiao
 * @date 2019/7/31 14:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private boolean success;
    private Map<String, Object> data;
    private Integer code;
    private String msg;

    private Result(boolean success){
        this.success = success;
    }

    private Result(boolean success, Map<String, Object> data){
        this.success = success;
        this.data = data;
    }

    private Result(boolean success, Integer code, String msg){
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    public static Result success(Map<String, Object> data){
        return new Result(true, data);
    }

    public static Result success(){
        return new Result(true);
    }

    public static Result success(String key, Object value){
        Map<String, Object> data = new HashMap<>(1);
        data.put(key, value);
        return new Result(true, data);
    }

    public static Result error(ErrorState errorState){
        return new Result(false, errorState.getCode(), errorState.getMsg());
    }

}
