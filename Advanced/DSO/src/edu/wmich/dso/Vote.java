package edu.wmich.dso;

public class Vote {

	private int memberWinNumber = 0;
	private String membershipDirector = "";
	private String president = "";
	private String secretary = "";
	private String treasurer = "";
	private String vicePresident = "";
	private String vocal = "";
	private String memberName = "";

	public Vote(int memberWinNumber) {
		this.memberWinNumber = memberWinNumber;
	}

	public void setMembershipDirector(String membershipDirector) {
		this.membershipDirector = membershipDirector;
	}

	public void setPresident(String president) {
		this.president = president;
	}

	public void setSecretary(String secretary) {
		this.secretary = secretary;
	}

	public void setTreasurer(String treasuer) {
		this.treasurer = treasuer;
	}

	public void setVicePresident(String vicePresident) {
		this.vicePresident = vicePresident;
	}

	public void setVocal(String vocal) {
		this.vocal = vocal;
	}

	public String getMembershipDirector() {
		return this.membershipDirector;
	}

	public String getPresident() {
		return this.president;
	}

	public String getSecretary() {
		return this.secretary;
	}

	public String getTreasurer() {
		return this.treasurer;
	}

	public String getVicePresident() {
		return this.vicePresident;
	}

	public String getVocal() {
		return this.vocal;
	}

	public int getMemberWinNumber() {
		return this.memberWinNumber;
	}

	public void setCurrentMember(String memberName) {
		this.memberName = memberName;
	}

	public String getCurrentMember() {
		return this.memberName;
	}
}
