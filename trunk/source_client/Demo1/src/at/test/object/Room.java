/**
 * 
 */
package at.test.object;

/**
 * @author Administrator
 * 
 */
public class Room {

	private int roomId, ownerId, maxMember, minMember, status,
			numberOfMember;
	private float betScore;
	private String roomName, ownerName;

	public Room(int roomId, String roomName, int ownerId, String ownerName, int maxMember,
			int minMember, float winScore, int status, int numberOfMember) {
		this.roomId = roomId;
		this.ownerId = ownerId;
		this.maxMember = maxMember;
		this.minMember = minMember;
		this.status = status;
		this.numberOfMember = numberOfMember;
		this.betScore = winScore;
		this.roomName = roomName;
		this.setOwnerName(ownerName);
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getMaxMember() {
		return maxMember;
	}

	public void setMaxMember(int maxMember) {
		this.maxMember = maxMember;
	}

	public int getMinMember() {
		return minMember;
	}

	public void setMinMember(int minMember) {
		this.minMember = minMember;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getNumber_of_member() {
		return numberOfMember;
	}

	public void setNumber_of_member(int number_of_member) {
		this.numberOfMember = number_of_member;
	}

	public float getBetScore() {
		return betScore;
	}

	public void setBetScore(float winScore) {
		this.betScore = winScore;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	/**
	 * @return the ownerName
	 */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * @param ownerName the ownerName to set
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
}
