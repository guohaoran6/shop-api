package io.recruitment.assessment.api.utils;

import lombok.Data;


@Data
public class PageQuery {
    // current page number
    private int page;
    // limit number per page
    private int limit;
    // start from records (offset)
    private int start;
    // search keyword
    private String keyword;

    public PageQuery(int page, int limit, String keyword) {

        this.page = page;
        this.limit = limit;
        this.start = (page - 1) * limit;
        this.keyword = keyword;
    }

    public PageQuery(int page, int limit) {

        this.page = page;
        this.limit = limit;
        this.start = (page - 1) * limit;
        this.keyword = null;
    }

    @Override
    public String toString() {
        return "PageUtil{" +
                "page=" + page +
                ", limit=" + limit +
                ", start=" + start +
                ", keyword=" + keyword +
                '}';
    }
}