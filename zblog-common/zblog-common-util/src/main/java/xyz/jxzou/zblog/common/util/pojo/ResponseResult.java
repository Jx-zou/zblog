package xyz.jxzou.zblog.common.util.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import xyz.jxzou.zblog.common.util.model.Result;

/**
 * The type Response result.
 *
 * @param <T> the type parameter
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseResult<T> implements Result {

    private Integer status;

    private String message;

    private T data;

    /**
     * Instantiates a new Response result.
     */
    public ResponseResult() {}

    /**
     * Instantiates a new Response result.
     *
     * @param status  the status
     * @param message the message
     */
    public ResponseResult(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Instantiates a new Response result.
     *
     * @param status the status
     * @param data   the data
     */
    public ResponseResult(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    /**
     * Instantiates a new Response result.
     *
     * @param status  the status
     * @param message the message
     * @param data    the data
     */
    public ResponseResult(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
