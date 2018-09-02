package com.jk.test;

public enum TaskStatus {
	STATUS_INIT(0),
	STATUS_INPUT_KEYWORD(10000),
	STATUS_CHECK_FILTER_ORDER(11000),
	STATUS_CHECK_FILTER_CONDITION(12000),
	STATUS_SEARCH_CLICK_GOODS(13000),
	STATUS_BROWSE_GOODS_DETAIL(14000),
	STATUS_COLLECT_GOODS(15000),
	STATUS_CLICK_GOODS_PARAM(16000),
	STATUS_CLICK_COMMENT_RECORD(17000),
	STATUS_ENTER_SHOP(18000),
	STATUS_CLICK_OTHER_GOODS(19000),
	STATUS_CLICK_INQUIRY_QA(20000),
	STATUS_FOLLOW_SHOP(21000);
	
	private int status;
	
	private TaskStatus(int status) {
		this.status = status;
	}
	
	public static TaskStatus valueOf(int status) {
		switch (status) {
		case 0:
			return STATUS_INIT;
		case 10000:
			return STATUS_INPUT_KEYWORD;
		case 11000:
			return STATUS_CHECK_FILTER_ORDER;
		case 12000:
			return STATUS_CHECK_FILTER_CONDITION;
		case 13000:
			return STATUS_SEARCH_CLICK_GOODS;
		case 14000:
			return STATUS_BROWSE_GOODS_DETAIL;
		case 15000:
			return STATUS_COLLECT_GOODS;
		case 16000:
			return STATUS_CLICK_GOODS_PARAM;
		case 17000:
			return STATUS_CLICK_COMMENT_RECORD;
		case 18000:
			return STATUS_ENTER_SHOP;
		case 19000:
			return STATUS_CLICK_OTHER_GOODS;
		default:
			return null;
		}
	}
	
	public int value() {
		return this.status;
	}
	
	public String toString() {
		return String.valueOf(this.status);
	}
}
