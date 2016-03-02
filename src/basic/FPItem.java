package basic;

public class FPItem {
	private String strName = null;
	
	public FPItem(String name) {
		this.strName = name;
	}
	
	public String getName() {
		return this.strName;
	}
	
	public Boolean equalsFPItem(FPItem item) {
		if(this.strName.equals(item.getName())) {
			return true;
		} else {
			return false;
		}
	}
}
