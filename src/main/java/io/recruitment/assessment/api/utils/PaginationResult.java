package io.recruitment.assessment.api.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PaginationResult<T> implements Serializable {
    private int totalCount;

    private int pageSize;

    private int totalPage;

    private int currPage;

    private List<T> list;

    public PaginationResult(List<T> list, int totalCount, int pageSize, int currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }
}
