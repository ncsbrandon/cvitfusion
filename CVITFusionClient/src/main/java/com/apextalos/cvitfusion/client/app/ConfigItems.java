package com.apextalos.cvitfusion.client.app;

public class ConfigItems {

	// THESE ARE CLIENT ONLY
	public static final String MAIN_POSITION_X_CONFIG = "main_position_x";
	public static final double MAIN_POSITION_X_DEFAULT = -1;
	public static final String MAIN_POSITION_Y_CONFIG = "main_position_y";
	public static final double MAIN_POSITION_Y_DEFAULT = -1;
	public static final String MAIN_WIDTH_CONFIG = "main_width";
	public static final String MAIN_HEIGHT_CONFIG = "main_height";
	public static final String MAIN_MAXIMIZED_CONFIG = "main_maximized";
	public static final Boolean MAIN_MAXIMIZED_DEFAULT = false;
	public static final String MAIN_SP1_DIV_POS_CONFIG = "main_sp1_div_pos";
	public static final String MAIN_SP11_DIV_POS_CONFIG = "main_sp11_div_pos";
	public static final String MAIN_SP112_DIV_POS_CONFIG = "main_sp112_div_pos";
	
	public static final String CONNECTIONS_POSITION_X_CONFIG = "connections_position_x";
	public static final double CONNECTIONS_POSITION_X_DEFAULT = -1;
	public static final String CONNECTIONS_POSITION_Y_CONFIG = "connections_position_y";
	public static final double CONNECTIONS_POSITION_Y_DEFAULT = -1;
	public static final String CONNECTIONS_WIDTH_CONFIG = "connections_width";
	public static final String CONNECTIONS_HEIGHT_CONFIG = "connections_height";
	
	public static final String CONNECTIONS_SESSIONLIST_CONFIG = "sessions";
	public static final String CONNECTIONS_SESSIONLIST_DEFAULT = "";
	public static final String CONNECTIONS_ACTIVESESSION_CONFIG = "active_session";
	public static final String CONNECTIONS_ACTIVESESSION_DEFAULT = "";
	
	private ConfigItems() {
		// prevent instances
	}
}
