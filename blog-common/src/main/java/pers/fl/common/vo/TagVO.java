package pers.fl.common.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.fl.common.po.Tag;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class TagVO extends Tag implements Serializable {
    private Integer tagCount;
}
