package com.funshion.activity.funtvactivity.entity;

/**
 * Created by zhangfei on 2018/7/26/026.
 */
public class ActivityMovieFirstPageConfigInfo {

	private Integer id;

	private Integer parentId;

	private Integer activityType;

	private String dataKey;

	private Integer dataValueType; //1 字符串 2 MAP 3 数组

	private String dataValueTypeName; //1 字符串 2 MAP 3 数组

	private Integer dataValueSeq;

	private String dataValue;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getActivityType() {
		return activityType;
	}

	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}

	public String getDataKey() {
		return dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public Integer getDataValueType() {
		return dataValueType;
	}

	public void setDataValueType(Integer dataValueType) {
		this.dataValueType = dataValueType;
	}

	public Integer getDataValueSeq() {
		return dataValueSeq;
	}

	public void setDataValueSeq(Integer dataValueSeq) {
		this.dataValueSeq = dataValueSeq;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public String getDataValueTypeName() {
		return dataValueTypeName;
	}

	public void setDataValueTypeName(String dataValueTypeName) {
		this.dataValueTypeName = dataValueTypeName;
	}

	public enum DataValueType {

		STRING(1, "字符串"),
		MAP(2, "键值对象"),
		ARRAY(3, "数组");
		private int key;
		private String name;

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		DataValueType(int key, String name) {
			this.key = key;
			this.name = name;
		}
	}

	public static String getDataValueTypeName(int key) {
		for (DataValueType dataValueType : DataValueType.values()) {
			if (key == dataValueType.getKey()) {
				return dataValueType.getName();
			}
		}
		return key + "";
	}

}
