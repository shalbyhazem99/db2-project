package it.polimi.db2.entities;

public enum UserExpertiseLevel {
	LOW(0), MEDIUM(1), HIGH(2);

	private final int value;

	UserExpertiseLevel(int value) {
		this.value = value;
	}

	public static UserExpertiseLevel getUserExpertiseLevelFromInt(int value) {
		switch (value) {
		case 0:
			return UserExpertiseLevel.LOW;
		case 1:
			return UserExpertiseLevel.MEDIUM;
		case 2:
			return UserExpertiseLevel.HIGH;
		}
		return null;
	}

	public int getValue() {
		return value;
	}

}