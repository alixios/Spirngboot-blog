package pers.fl.common.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import lombok.*;

/**
 * <p>
 *
 * </p>
 *
 * @author: fl
 * @since 2021-01-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BlogTag extends Model<BlogTag> {

    private static final long serialVersionUID = 1L;

    private Long blogId;

    private Integer tagId;

    @Override
    protected Serializable pkVal() {
        return this.blogId;
    }

}
