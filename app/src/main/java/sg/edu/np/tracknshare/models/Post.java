package sg.edu.np.tracknshare.models;

public class Post {
    private String UserId;
    private String PostId;
    private String RunId;
    private long PostDate;
    private int Likes;
    private String Caption;

    public void setPostId(String postId) {
        PostId = postId;
    }
    public String getPostId() {
        return PostId;
    }

    public void setPostDate(long postDate) { PostDate = postDate; }
    public long getPostDate() {
        return PostDate;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }
    public int getLikes() {
        return Likes;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }
    public String getCaption() {
        return Caption;
    }

    public String getUserId() { return UserId; }
    public void setUserId(String userId) { UserId = userId; }

    public String getRunId() { return RunId; }
    public void setRunId(String runId) { RunId = runId; }

    public Post(String userId, String postId, String runId, long postDate, int likes, String caption) {
        UserId = userId;
        PostId = postId;
        RunId = runId;
        PostDate = postDate;
        Likes = likes;
        Caption = caption;
    }
    public Post(){}
}

