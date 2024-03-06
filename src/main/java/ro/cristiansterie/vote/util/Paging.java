package ro.cristiansterie.vote.util;

public class Paging {
    private int page;
    private int size;

    public Paging() {
        this.page = 0;
        this.size = 10;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
