package similarity.linzijun.com.converter.orm.entity;

import com.linzijun.similarity.converter.annotation.Alias;
import com.linzijun.similarity.converter.annotation.AutoConverter;

import similarity.linzijun.com.converter.domain.Comment;

/**
 * Created by linzijun on
 */
@AutoConverter(Comment.class)
public class CommentEntity extends BaseEntity {

    @Alias("id")
    public long commentId;

    public String content;

}
