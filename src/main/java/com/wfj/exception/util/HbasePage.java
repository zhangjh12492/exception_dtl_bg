package com.wfj.exception.util;

/**
 * hbase查询的分页对象
 * @ClassName: HbasePage
 * @author ZJHao
 * @date 2015年9月23日 下午9:24:03
 *
 */
public class HbasePage<T> {

	private String startRow;	//hbase的开始行
	private Integer pageSize;	//hbase查询的每页条数
	private boolean nextPage;	//是否有下一页
	private T object;
	
	public HbasePage(){
		this.pageSize=10;	//每页条数默认10条
	}
	
	public HbasePage(T t){
		this.pageSize=10;
		this.object=t;
	}
	public String getStartRow() {
		return startRow;
	}
	public void setStartRow(String startRow) {
		this.startRow = startRow;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public boolean isNextPage() {
		return nextPage;
	}
	public void setNextPage(boolean nextPage) {
		this.nextPage = nextPage;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return "HbasePage [startRow=" + startRow + ", pageSize=" + pageSize + ", nextPage=" + nextPage + ", object=" + object + "]";
	}
	
	
}
