package sg.edu.np.tracknshare.models;

import java.util.Date;

public class Post {

    //    Attribute setter,getter for PostId
    private int PostId;

    public void setPostId(int postId) {
        PostId = postId;
    }

    public int getPostId() {
        return PostId;
    }

    //    Attribute setter,getter for PostUsername

    private String PostUsername;

    public void setPostUsername(String postUsername) {
        PostUsername = postUsername;
    }

    public String getPostUsername() {
        return PostUsername;
    }

//    Attribute setter,getter for PostDate

    private String PostDate;

    public void setPostDate(String postDate) {
//        Date date = new Date(postDate);
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//
//        PostDate = sdf.format(date);
        PostDate = postDate;
    }

    public String getPostDate() {
        return PostDate;
    }

//    Attribute setter,getter for Likes

    private int Likes;

    public void setLikes(int likes) {
        Likes = likes;
    }

    public int getLikes() {
        return Likes;
    }

//    Attribute setter,getter for Caption

    private String Caption;

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getCaption() {
        return Caption;
    }
    private User User;

    public void setUser(User u){
        User = u;
    }
    public User getUser(){
        return User;
    }
}

