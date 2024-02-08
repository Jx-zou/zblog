package xyz.jxzou.zblog.util.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Page<T> {
    /**
     * 当前页码
     */
    private Integer offset;
    /**
     * 每页大小
     */
    private Integer size;
    /**
     * 总页数
     */
    private Long total;
    /**
     * 携带数据
     */
    private T data;
}
