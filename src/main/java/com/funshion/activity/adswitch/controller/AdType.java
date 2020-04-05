package com.funshion.activity.adswitch.controller;

public enum AdType {

	DEFAULT("fengxing", "0"),
	NAIKAN("naikan", "1"),
	;

	private String codeName;
	private String code;

	AdType(String codeName, String code) {
		this.codeName = codeName;
		this.code = code;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public static String getCodeByName(String codeName) {
		for (AdType value : AdType.values()) {
			if (value.getCodeName().equals(codeName)) {
				return value.getCode();
			}
		}
		return AdType.DEFAULT.getCode();
	}
}
