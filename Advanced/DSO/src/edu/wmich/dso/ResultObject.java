package edu.wmich.dso;

public class ResultObject {

	private String vote = "";
	private String name = "";
	private String positionId = "";
	private String position = "";

	public ResultObject() {

	}

	public void setVote(String vote) {
		this.vote = vote;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getVote() {
		return this.vote;
	}

	public String getName() {
		return this.name;
	}

	public String getPositionId() {
		return this.positionId;
	}

	public String getPosition() {
		return this.position;
	}

	@Override
	public String toString() {
		return this.getName() + " " + this.getVote();//
	}

}
