package com.apextalos.cvitfusion.common.opflow;

import java.util.ArrayList;
import java.util.List;

import com.apextalos.cvitfusion.common.opflow.Parameter.Form;

public class Parameters {

	private static final List<String> passwords = new ArrayList<>() {{
		add("one");
		add("two");
		add("three");
		add("four");
		add("five");
		add("six");
		add("seven");
		add("eight");
	}};
	
	public static final Parameter notes = new Parameter("NOTES", "Notes", Form.STRING, "", "Only for user use", null);
	public static final Parameter toAddress = new Parameter("TOADDRESS", "To address", Form.EMAIL, "name@email.com", "Where to send the notification", null);
	public static final Parameter fromName = new Parameter("FROMNAME", "From name", Form.STRING, "Connect:ITS", "Sender's name", null);
	public static final Parameter fromAddress = new Parameter("FROMADDRESS", "From address", Form.EMAIL, "connectits@mhcorbin.com", "Sender's address.  Should be whitelisted in spam filters.", null);
	public static final Parameter smtpServer = new Parameter("SMTPSERVER", "SMTP server", Form.STRING, "smtp.server.com", "Hostname or IP", null);
	public static final Parameter smtpPort = new Parameter("SMTPPORT", "SMTP port", Form.INTEGER, "587", "Usually 25 or 587", null);
	public static final Parameter useAuth = new Parameter("USEAUTH", "Use authentication", Form.BOOLEAN, "true", "Often required for internet relay servers", null);
	public static final Parameter username = new Parameter("USERNAME", "Username", Form.DECIMAL, "username", "Only needed if authentication is enabled", null);
	public static final Parameter password = new Parameter("PASSWORD", "Password", Form.STRINGLIST, "six", "Only needed if authentication is enabled", passwords);
}
