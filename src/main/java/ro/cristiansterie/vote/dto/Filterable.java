package ro.cristiansterie.vote.dto;

import ro.cristiansterie.vote.util.Paging;

public interface Filterable<T> {
    public T getObject();

    public void setObject(T t);

    public Paging getPaging();

    public void setPaging(Paging paging);
}
