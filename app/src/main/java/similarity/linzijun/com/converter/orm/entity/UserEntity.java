package similarity.linzijun.com.converter.orm.entity;


import com.linzijun.similarity.converter.annotation.AutoConverter;
import com.linzijun.similarity.converter.annotation.CustomConverter;


import similarity.linzijun.com.converter.domain.User;
import similarity.linzijun.com.converter.orm.converter.DateConverter;

/**
 * Created by zijun.lzj on
 */
@AutoConverter(User.class)
public class UserEntity extends BaseEntity {

    public String uuid;

    public long userId;

    public String type;

    public String nick;

    public String gender;

    @CustomConverter(DateConverter.class)
    public long birthday;
}
