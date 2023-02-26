package xyz.jxzou.zblog.common.util.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
public class Page<T> {

    /**
     * 当前页码
     */
    private Integer offset;
    /**
     * 每页条数
     */
    private Integer size = 10;
    /**
     * 总条数
     */
    private Long total;
    /**
     * 搜索数据
     */
    private T data;
}
