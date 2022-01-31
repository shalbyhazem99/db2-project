package it.polimi.db2.entities;

public enum UserSex {
	MALE(0), FEMALE(1), OTHER(2);

	private final int value;

	UserSex(int value) {
		this.value = value;
	}

	public static UserSex getUserSexFromInt(int value) {
		switch (value) {
		case 0:
			return UserSex.MALE;
		case 1:
			return UserSex.FEMALE;
		case 2:
			return UserSex.OTHER;
		}
		return null;
	}

	public int getValue() {
		return value;
	}

}