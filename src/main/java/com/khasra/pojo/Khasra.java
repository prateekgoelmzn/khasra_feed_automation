package com.khasra.pojo;

public class Khasra {
	private String num;
	private String fasal_name;
	private String agriTech;
	private String area;
	private String isMultiple;
	private String size;
	private String riktBhare;
	private String gataUniqueCode;

	public Khasra() {
		super();
	}

	public Khasra(String num, String fasal_name) {
		super();
		this.num = num;
		this.fasal_name = fasal_name;
	}
	
	public Khasra(String num, String fasal_name, String agriTech) {
		super();
		this.num = num;
		this.fasal_name = fasal_name;
		this.agriTech = agriTech;
	}
	
	

	public Khasra(String num, String fasal_name, String agriTech, String area, String isMultiple, String size, String riktBhare) {
		super();
		this.num = num;
		this.fasal_name = fasal_name;
		this.agriTech = agriTech;
		this.area = area;
		this.isMultiple = isMultiple;
		this.size = size;
		this.riktBhare = riktBhare;
	}
	
	public Khasra(String num, String fasal_name, String agriTech, String area, String isMultiple, String size, String riktBhare, String gataUniqueCode) {
		super();
		this.num = num;
		this.fasal_name = fasal_name;
		this.agriTech = agriTech;
		this.area = area;
		this.isMultiple = isMultiple;
		this.size = size;
		this.riktBhare = riktBhare;
		this.gataUniqueCode = gataUniqueCode;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getFasal_name() {
		return fasal_name;
	}

	public void setFasal_name(String fasal_name) {
		this.fasal_name = fasal_name;
	}

	public String getAgriTech() {
		return agriTech;
	}

	public void setAgriTech(String agriTech) {
		this.agriTech = agriTech;
	}

	public boolean isRiktBhare() {
		return "1".equals(this.riktBhare);
	}
	
	public int getRiktBhareCode() {
		Integer ans = Integer.parseInt(this.riktBhare);
		return ans!=null?ans:null;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public boolean isMultiple() {
		if(this.isMultiple==null) {
			return false;
		}
		int ind = Integer.parseInt(this.isMultiple);
		return ind==1;
	}

	public int getSize() {
		return Integer.parseInt(this.size);
	}

	public String getGataUniqueCode() {
		return gataUniqueCode;
	}

	public void setGataUniqueCode(String gataUniqueCode) {
		this.gataUniqueCode = gataUniqueCode;
	}
	
	public boolean isDelete() {
		return this.fasal_name==null;
	}

	@Override
	public String toString() {
		return "Khasra [num=" + num + ", fasal_name=" + fasal_name + ", agriTech=" + agriTech + ", area=" + area
				+ ", isMultiple=" + isMultiple + ", size=" + size + "]";
	}

	
	
	
}
