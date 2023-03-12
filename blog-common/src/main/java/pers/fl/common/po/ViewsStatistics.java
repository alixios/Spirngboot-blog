package pers.fl.common.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * VIEW七天访问量的视图
 * </p>
 *
 * @author fengliang
 * @since 2021-02-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ViewsStatistics extends Model<ViewsStatistics> {

    private static final long serialVersionUID = 1L;

    /**
     * 访问的博客id
     */
    private Long blogId;

    /**
     * 标题
     */
    private String title;

    private Long uid;

    private Integer sevenDay;

    private Integer sixDay;

    private Integer fiveDay;

    private Integer fourDay;

    private Integer threeDay;

    private Integer twoDay;

    private Integer oneDay;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
