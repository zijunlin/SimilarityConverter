package similarity.linzijun.com.converter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.util.Date;

import similarity.linzijun.com.converter.domain.Comment;
import similarity.linzijun.com.converter.orm.entity.CommentEntity;
import similarity.linzijun.com.converter.orm.entity.converter.CommentEntity_Comment_Converter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txt= (TextView) findViewById(R.id.txt);

        CommentEntity entity = new CommentEntity();
        entity.commentId = 1;
        entity.content = "test";
        entity.gmtCreate = new Date();
        entity.gmtModify = new Date();
        Comment comment = CommentEntity_Comment_Converter.parseToComment(entity);
        txt.setText(JSON.toJSONString(comment));
    }
}
