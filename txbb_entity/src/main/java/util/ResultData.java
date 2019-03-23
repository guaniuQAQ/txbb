package util;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultData<T> implements Serializable {
    private String code;
    private String msg;
    private T data;

    //成功
    public static <T> ResultData<T> succResultData(T data) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(GlobalConstant.SUCC_CODE);
        resultData.setMsg(GlobalConstant.SUCC_MSG);
        resultData.setData(data);
        return resultData;
    }


    public static <T> ResultData errorResultData(String code, String msg) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(code);
        resultData.setMsg(msg);
        return resultData;
    }

}
