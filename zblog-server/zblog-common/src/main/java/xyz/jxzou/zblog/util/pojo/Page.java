package xyz.jxzou.zblog.util.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
