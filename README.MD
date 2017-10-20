# SimilarityConverter

Similarity.Converter  is an Annotation Processor ,use to generate converter code.

When I design the progrommer by the Clean Architecture ,there have many simility class to convert target like: Domain to Entity,Domain to DataTranstionObject,Domain to ViewModel.
Domain、DTO、Entity、ViewModel usuasl is more similay. so we can generate a converter

## Getting Started

   First include SimilarityConverter into your project, we use Android Studio, add this line on you build.gradle dependency:

```
implementation 'com.linzijun.similarity.converter:annotation:1.0.0'
annotationProcessor 'com.linzijun.similarity.converter:compiler:1.0.0'
```
In Adroid Studio 2.0 
```
 compile 'com.linzijun.similarity.converter:annotation:1.0.0'
 apt 'com.linzijun.similarity.converter:compiler:1.0.0'
```

## Simple

  Here is a simple code 
```java
public class Comment {

    private long id;
    private String content;
    private Date gmtCreate;
    private Date gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }
}


@AutoConverter(Comment.class)
public class CommentEntity extends BaseEntity {

    @Alias("id")
    public long commentId;

    public String content;

}

public class BaseEntity {

    @IgnoreConverter()
    public long id;

    public Date gmtCreate;

    public Date gmtModify;
}

```




After build SimilarityConverter would generate a converter like that :
``` java
public final class CommentEntity_Comment_Converter {
  public static final CommentEntity parseToCommentEntity(Comment target) {
    CommentEntity source=new CommentEntity();
    source.gmtCreate=target.getGmtCreate();
    source.gmtModify=target.getGmtModify();
    source.commentId=target.getId();
    source.content=target.getContent();
    return source;
  }

  public static final Comment parseToComment(CommentEntity target) {
    Comment source=new Comment();
    source.setId(target.commentId);
    source.setContent(target.content);
    source.setGmtCreate(target.gmtCreate);
    source.setGmtModify(target.gmtModify);
    return source;
  }
}
```


You can use it at anywhere While you want to Converter,just call CommentEntity_Comment_Converter.parseToComment()

## License

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.