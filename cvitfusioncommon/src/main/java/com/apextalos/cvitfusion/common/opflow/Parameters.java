package com.apextalos.cvitfusion.common.opflow;

import com.apextalos.cvitfusion.common.opflow.Parameter.Form;

public class Parameters {

	public final static Parameter toAddress = new Parameter("TOADDRESS", "To address", Form.EMAIL, "name@email.com", null);
	public final static Parameter fromName = new Parameter("FROMNAME", "From name", Form.STRING, "Connect:ITS", null);
	public final static Parameter fromAddress = new Parameter("FROMADDRESS", "From address", Form.EMAIL, "connectits@mhcorbin.com", null);
	public final static Parameter smtpServer = new Parameter("SMTPSERVER", "SMTP server", Form.STRING, "smtp.server.com", null);
	public final static Parameter smtpPort = new Parameter("SMTPPORT", "SMTP port", Form.INTEGER, "587", null);
	public final static Parameter useAuth = new Parameter("USEAUTH", "Use authentication", Form.BOOLEAN, "true", null);
	public final static Parameter username = new Parameter("USERNAME", "Username", Form.STRING, "username", null);
	public final static Parameter password = new Parameter("PASSWORD", "Password", Form.PASSWORD, "password", null);
}
