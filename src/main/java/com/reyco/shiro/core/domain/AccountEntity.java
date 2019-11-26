package com.reyco.shiro.core.domain;

public class AccountEntity extends BaseEntity {
	private static final long serialVersionUID = 828356899217596778L;
	private String username;
	private String password;
	private String salt;
	private Integer rid;
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
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public Integer getRid() {
		return rid;
	}
	public void setRid(Integer rid) {
		this.rid = rid;
	}
	@Override
	public String toString() {
		return "AccountEntity [id=" + id + ", username=" + username + ", password=" + password + ", salt=" + salt
				+ ", rid=" + rid + "]";
	}
}
