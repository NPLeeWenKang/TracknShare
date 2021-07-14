package sg.edu.np.tracknshare.models;

import sg.edu.np.tracknshare.models.Post;
import sg.edu.np.tracknshare.models.User;

public class UserPostViewModel {
    private Post Post;
    private User User;

    public Post getPost() { return Post; }
    public void setPost(sg.edu.np.tracknshare.models.Post post) { Post = post; }

    public User getUser() { return User; }
    public void setUser(sg.edu.np.tracknshare.models.User user) { User = user; }

    public UserPostViewModel(Post post, User user) {
        Post = post;
        User = user;
    }
}
