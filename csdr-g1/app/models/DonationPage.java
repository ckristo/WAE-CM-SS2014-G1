package models;

import java.util.ArrayList;
import java.util.List;

public class DonationPage {
    private final int pageSize;
    private final long totalRowCount;
    private final int pageIndex;
    private final List<Donation> list;
  
    public DonationPage(List<Donation> data, long total, int page, int pageSize) {
        this.list = data;
        this.totalRowCount = total;
        this.pageIndex = page;
        this.pageSize = pageSize;
    }
    
    public long getTotalRowCount() {
        return totalRowCount;
    }
    
    public int getPageIndex() {
        return pageIndex;
    }
    
    public List<Donation> getList() {
        return list;
    }
    
    public boolean hasPrev() {
        return pageIndex > 1;
    }
    
    public boolean hasNext() {
        return (totalRowCount/pageSize) >= pageIndex;
    }
    
    public int getAmountPages() {
    	return (int)totalRowCount/pageSize;
    }
    
    public String getDisplayXtoYofZ() {
        int start = ((pageIndex - 1) * pageSize + 1);
        int end = start + Math.min(pageSize, list.size()) - 1;
        return start + " to " + end + " of " + totalRowCount;
    }
    
}
