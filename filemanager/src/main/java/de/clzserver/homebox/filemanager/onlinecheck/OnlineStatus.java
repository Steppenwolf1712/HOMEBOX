package de.clzserver.homebox.filemanager.onlinecheck;

public enum OnlineStatus {
	
	Online("ONLINE", true), Lan("LAN", true), Offline("OFFLINE", false);
	
	private final String m_s_status;
	private final boolean m_b_online;

	public static final String STATUS_ONLINE="ONLINE";
	public static final String STATUS_LAN="LAN";
	public static final String STATUS_OFFLINE="OFFLINE";
	
	private OnlineStatus(String status, boolean online) {
		m_s_status = status;
		m_b_online = online;
	}
	
	public String getStatus() {
		return m_s_status;
	}
	
	public boolean isOnline() {
		return m_b_online;
	}
}
