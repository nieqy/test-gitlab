package com.funshion.activity.funtvactivity.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangfei on 2018/10/16/016.
 */
public class FuntvActivityPrizeResponse implements Comparable<FuntvActivityPrizeResponse> {

	private Integer prizeType;

	private List<String> phone = new ArrayList<>();

	public Integer getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(Integer prizeType) {
		this.prizeType = prizeType;
	}

	public List<String> getPhone() {
		return phone;
	}

	public void setPhone(List<String> phone) {
		this.phone = phone;
	}

	@Override
	public int compareTo(FuntvActivityPrizeResponse o) {
		return this.getPrizeType() - o.getPrizeType();
	}

	public static class FuntvActivityPrizeFlagResponse {

		private String pre;

		private String next;

		private String current;

		public String getPre() {
			return pre;
		}

		public void setPre(String pre) {
			this.pre = pre;
		}

		public String getNext() {
			return next;
		}

		public void setNext(String next) {
			this.next = next;
		}

		public String getCurrent() {
			return current;
		}

		public void setCurrent(String current) {
			this.current = current;
		}
	}
}
