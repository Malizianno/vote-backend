package ro.cristiansterie.vote.dto;

import java.util.List;

public class NewsfeedResponseDTO {
    private List<NewsfeedPostDTO> posts;
    private long total;

    public List<NewsfeedPostDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<NewsfeedPostDTO> posts) {
        this.posts = posts;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
