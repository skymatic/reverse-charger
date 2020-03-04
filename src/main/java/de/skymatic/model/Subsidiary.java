package de.skymatic.model;

public enum Subsidiary {

	AMERICA("Apple Inc.", "One Apple Park Way\nCupertino, CA 95014\nUSA"),
	AUSTRALIA("Apple Pty Limited", "Level 3\n20 Martin Place\nSydney NSW 2000\nAustralia"),
	CANADA("Apple Canada Inc.", "120 Bremner Boulevard, Suite 1600\nToronto, ON M5J 0A8\nCanada"),
	JAPAN("iTunes K.K.", "〒 106-6140\n6-10-1 Roppongi, Minato-ku, Tokyo\nJapan"),
	ROW("Apple Distribution International Limited", "Hollyhill Industrial Estate\nHollyhill\nCork\nIreland");

	private String companyName;
	private String mainOffice;

	Subsidiary(String companyName, String mainOffice) {
		this.companyName = companyName;
		this.mainOffice = mainOffice;
	}

	public String getAddress() {
		return companyName + "\n" + mainOffice;
	}
}
