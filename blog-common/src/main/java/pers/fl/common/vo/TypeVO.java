package pers.fl.common.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.fl.common.po.Type;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class TypeVO extends Type implements Serializable {
    private Integer typeCount;
}
