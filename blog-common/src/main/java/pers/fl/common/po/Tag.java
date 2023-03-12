package pers.fl.common.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * <p>
 *
 * </p>
 *
 * @author fl
 * @since 2021-01-27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "标签实体", description = "标签实体")
@EqualsAndHashCode(callSuper = false)
public class Tag extends Model<Tag> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "tag_id", type = IdType.AUTO)
    private Integer tagId;

    private String tagName;


    @Override
    protected Serializable pkVal() {
        return this.tagId;
    }

}
