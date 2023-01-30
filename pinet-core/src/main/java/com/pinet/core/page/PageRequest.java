package com.pinet.core.page;

import lombok.Data;

/**
 * 分页请求
 * @date Aug 19, 2018
 */

@Data
public class PageRequest {
	/**
	 * 当前页码
	 */
	private int pageNum = 1;
	/**
	 * 每页数量
	 */
	private int pageSize = 10;

}