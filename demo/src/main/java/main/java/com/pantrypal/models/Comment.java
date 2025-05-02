package main.java.com.pantrypal.models;

/**
 * Represents a comment made by a user on a recipe.
 */
public class Comment {
    private final String commentId;
    private final String content;
    private final String postDate;
    private final User user;

    public Comment(String commentId, String content, String postDate, User user) {
        this.commentId = commentId;
        this.content = content;
        this.postDate = (postDate == null) ? ": No date" : postDate;
        this.user = user;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getContent() {
        return content;
    }

    public String getPostDate() {
        return postDate;
    }

    public User getUser() {
        return user;
    }
}
