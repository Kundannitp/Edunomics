package com.ksquarej.edunomics.NavigationDrawer;

public class BlogUploadClass {
    String email,blogdata;

    public BlogUploadClass(String email, String blogdata) {
        this.email = email;
        this.blogdata = blogdata;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBlogdata() {
        return blogdata;
    }

    public void setBlogdata(String blogdata) {
        this.blogdata = blogdata;
    }
}
