package com.yan.common.pojo;

import java.io.Serializable;
import java.util.List;

public class EasyUiDataGridResult implements Serializable {
    private long total;
    //row用来放置从数据库的查询结果
    private List rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List row) {
        this.rows = row;
    }
}
