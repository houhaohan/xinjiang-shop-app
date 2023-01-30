package com.pinet.core.page;

import lombok.Data;
import java.util.List;
/**
 * 分页返回结果
 * @date Aug 19, 2018
 */
@Data
public class PageResult {
	/**
	 * 当前页码
	 */
	private long pageNum;
	/**
	 * 每页数量
	 */
	private long pageSize;
	/**
	 * 记录总数
	 */
	private long totalSize;
	/**
	 * 页码总数
	 */
	private long totalPages;
	/**
	 * 分页数据
	 */
	private List<?> content;

}
