package mainpackage;

public class Engineer {
	
	private String name;
	private String pw;
	private int salaryPerHour;
	private static int dailySalary = 300;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}
	
	public int getSalaryPerHour() {
		return salaryPerHour;
	}

	public void setSalaryPerHour(int salaryPerHour) {
		this.salaryPerHour = salaryPerHour;
	}
	
	public static int getDailySalary() {
		return dailySalary;
	}
	
}