package similarity.linzijun.com.converter.orm.entity;


import com.linzijun.similarity.converter.annotation.IgnoreConverter;

import java.util.Date;

/**
 * Created by linzijun on
 */

public class BaseEntity {

    @IgnoreConverter()
    public long id;

    public Date gmtCreate;

    public Date gmtModify;
}
