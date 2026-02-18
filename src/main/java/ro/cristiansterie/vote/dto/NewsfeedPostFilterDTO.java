package ro.cristiansterie.vote.dto;

import ro.cristiansterie.vote.util.Paging;

public class NewsfeedPostFilterDTO implements Filterable<NewsfeedPostDTO> {
    private NewsfeedPostDTO post;
    private Paging paging;

    public NewsfeedPostDTO getPost() {
        return getObject();
    }

    public void setPost(NewsfeedPostDTO post) {
        setObject(post);
    }

    public NewsfeedPostDTO getObject() {
        return post;
    }

    public void setObject(NewsfeedPostDTO post) {
        this.post = post;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

}
