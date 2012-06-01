/**
 * 
 */
package com.ppclink.iqarena.object;

/**
 * @author Administrator
 * 
 */
public class Room {

	private float betScore;
	private int roomId, ownerId, maxMember;
	private String roomName, ownerName;
	private int timePerQuestion;

	public Room(int roomId, String roomName, int ownerId, String ownerName,
			int maxMember, float betScore, int timePerQuestion) {
		this.roomId = roomId;
		this.ownerId = ownerId;
		this.maxMember = maxMember;
		this.betScore = betScore;
		this.roomName = roomName;
		this.setTimePerQuestion(timePerQuestion);
		this.setOwnerName(ownerName);
	}

	public float getBetScore() {
		return betScore;
	}

	public int getMaxMember() {
		return maxMember;
	}

	public int getOwnerId() {
		return ownerId;
	}

	/**
	 * @return the ownerName
	 */
	public String getOwnerName() {
		return ownerName;
	}

	public int getRoomId() {
		return roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public int getTimePerQuestion() {
		return timePerQuestion;
	}

	public void setBetScore(float winScore) {
		this.betScore = winScore;
	}

	public void setMaxMember(int maxMember) {
		this.maxMember = maxMember;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @param ownerName
	 *            the ownerName to set
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public void setTimePerQuestion(int timePerQuestion) {
		this.timePerQuestion = timePerQuestion;
	}
}
