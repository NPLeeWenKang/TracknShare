package sg.edu.np.tracknshare.models;

import android.graphics.Bitmap;
import android.media.Image;

import java.util.Date;

public class Comment {
    private int Id;
//    private Image UserImg;
    private String UserId;
    private int PostId;
    private String UserName;
    private String Date;
    private String CommentText;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getPostId() {
        return PostId;
    }

    public void setPostId(int postId) {
        PostId = postId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCommentText() {
        return CommentText;
    }

    public void setCommentText(String commentText) {
        CommentText = commentText;
    }
}
