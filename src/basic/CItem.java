package basic;

public class CItem {
	private String strName = null;
	private String strPatientId = null;
	private String strCategory = null;
	private String strDate = null;
	private String strOriName = null;
	private int intStatus = 0;//0: normal, 1: repeates "every" day or removed

	public CItem() {}
	
	public CItem(String name, String id, String category, String date) {
		this.strName = name;
		this.strPatientId = id;
		this.strCategory = category;
		this.strDate = date;
	}
	
	public CItem(String name, String id, String category, String date, String oriName) {
		this.strName = name;
		this.strPatientId = id;
		this.strCategory = category;
		this.strDate = date;
		this.strOriName = oriName;
	}

	public boolean cmpName(String oName) {
		return this.strName.equals(oName);
	}

	public boolean cmpPatientId(String oId) {
		return this.strPatientId.equals(oId);
	}

	public boolean cmpCategory(String oCate) {
		return this.strCategory.equals(oCate);
	}

	public boolean cmpDate(String oDate) {
		return this.strDate.equals(oDate);
	}

	public String getName() {
		return this.strName;
	}

	public void setName(String name) {
		this.strName = name;
	}

	public String getPatientId() {
		return this.strPatientId;
	}

	public void setPatientId(String id) {
		this.strPatientId = id;
	}

	public String getCategory() {
		return this.strCategory;
	}

	public void setCategory(String category) {
		this.strCategory = category;
	}

	public String getDate() {
		return this.strDate;
	}

	public void setDate(String date) {
		this.strDate = date;
	}
	
	public int getStatus() {
		return this.intStatus;
	}
	
	public void setStatus(int status) {
		this.intStatus = status;
	}
}
