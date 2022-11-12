package com.io.routesapp.ui.reviews;

public abstract class Review {
    String authorID;
    String content;

    public String getAuthorID() {
        return authorID;
    }

    public String getContent() {
        return content;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
