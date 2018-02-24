package tk.rht0910.cheateye.check;

public enum ChatType {
	SNEAKING(0),
	BLOCKING(1),
	DIED(2),
	SPRINTING(3);

	private static Class<ChatType> chattype = ChatType.class;
	private Integer num;

	private ChatType(Integer num) {
		this.num = num;
	}

	public Integer getValue() {
		return this.num;
	}

	public static ChatType init() {
		try {
			return chattype.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
