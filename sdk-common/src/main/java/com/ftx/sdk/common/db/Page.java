package com.ftx.sdk.common.db;

import com.ftx.sdk.common.utils.ArrayListUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：分页类
 *
 * @auth:xiaojun.yin
 * @createTime 2019-05-04 21:08
 */
@Data
public class Page<T> implements Serializable {
    /** * 每页显示条数 */
    private Integer pageSize = 10 ;
    /** * 总条数 */
    private Integer totalCount;
    /** * 当前页 */
    private Integer pageNo;
    /**  * 总页数 */
    private Integer totalPages;
    /** * 数据 */
    private List<T> pageList;
    /** * 是否有下一页 */
    private boolean hasNext = false;


    public Page(int pageNo, int pageSize) {
        if (pageNo <= 0) {
            pageNo = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        this.pageNo = pageNo;
        this.pageSize = pageSize;

    }

    public Page(int totalCount, List<T> pageList) {
        if (!ArrayListUtil.checkListIsNotEmpty(pageList)) {
            pageList = new ArrayList<>();
        }
        this.totalCount = totalCount;
        this.pageList = pageList;
    }

    /**
     *  @Description:填充数据
     * @Author: xiaojun.yin
     * @param totalCount
     * @param pageList
     * @return
     */
    public Page<T> addDate(int totalCount, List<T> pageList) {
        this.totalCount = totalCount;
        this.pageList = pageList;
        if (totalCount % pageSize > 0) {
            this.totalPages = totalCount / pageSize + 1;
        }else{
            this.totalPages = totalCount / pageSize ;
        }
        hasNext = pageNo < totalPages;
        return this;
    }

    /**
     * 获取开始页码
     * @return
     */
    public int getStart() {
        return (pageNo - 1) * pageSize;

    }


    @Override
    public String toString() {
        return "Page{" +
                "pageSize=" + pageSize +
                ", totalCount=" + totalCount +
                ", pageNo=" + pageNo +
                ", totalPages=" + totalPages +
                ", pageList=" + pageList +
                '}';
    }

    /**
     *  @Description:判断list中是否有值
     * @Author: xiaojun.yin
     * @return true -有值 false-无值
     */
    public boolean checkListExist() {
        return pageList != null && pageList.size() > 0;
    }
}
