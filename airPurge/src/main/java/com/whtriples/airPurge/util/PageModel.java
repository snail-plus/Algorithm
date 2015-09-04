package com.whtriples.airPurge.util;

import java.io.Serializable;
import java.util.List;

public class PageModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public List<?> data;

    public Integer total;

    public PageModel(Integer total, List<?> data) {
        super();
        this.total = total;
        this.data = data;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

	/*
     * public List<?> getRows() { return rows; }
	 * 
	 * public void setRows(List<?> rows) { this.rows = rows; }
	 */
}
