/**
 * 
 */
package at.test.object;

/**
 * @author Administrator
 * 
 */
public class Room {

	private int roomId, ownerId, maxMember;
	private float betScore;
	private int timePerQuestion;
	private String roomName, ownerName;

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
	 * @param ownerName
	 *            the ownerName to set
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public int getTimePerQuestion() {
		return timePerQuestion;
	}

	public void setTimePerQuestion(int timePerQuestion) {
		this.timePerQuestion = timePerQuestion;
	}
}
