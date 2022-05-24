package com.apextalos.cvitfusion.client.models;

public class Connection {

	private String url;
	private String clientId;
	private boolean useTls;
	private String caCertFile;
	private String clientCertFile;
	private String clientKeyFile;
	private boolean usePassword;
	private String username;
	private String password;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public boolean isUseTls() {
		return useTls;
	}
	public void setUseTls(boolean useTls) {
		this.useTls = useTls;
	}
	public String getCaCertFile() {
		return caCertFile;
	}
	public void setCaCertFile(String caCertFile) {
		this.caCertFile = caCertFile;
	}
	public String getClientCertFile() {
		return clientCertFile;
	}
	public void setClientCertFile(String clientCertFile) {
		this.clientCertFile = clientCertFile;
	}
	public String getClientKeyFile() {
		return clientKeyFile;
	}
	public void setClientKeyFile(String clientKeyFile) {
		this.clientKeyFile = clientKeyFile;
	}
	public boolean isUsePassword() {
		return usePassword;
	}
	public void setUsePassword(boolean usePassword) {
		this.usePassword = usePassword;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Connection() {
		
	}
	public Connection(String url, String clientId, boolean useTls, String caCertFile, String clientCertFile,
			String clientKeyFile, boolean usePassword, String username, String password) {
		super();
		this.url = url;
		this.clientId = clientId;
		this.useTls = useTls;
		this.caCertFile = caCertFile;
		this.clientCertFile = clientCertFile;
		this.clientKeyFile = clientKeyFile;
		this.usePassword = usePassword;
		this.username = username;
		this.password = password;
	}
	
}
