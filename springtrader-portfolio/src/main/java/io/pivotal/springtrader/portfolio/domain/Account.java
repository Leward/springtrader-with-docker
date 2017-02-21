package io.pivotal.springtrader.portfolio.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * Represents the account.
 * <p>
 * Entity object that represents a user account.
 *
 * @author David Ferreira Pinto
 */

public class Account implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3057275461420965784L;

    private Integer id;

    private String address;

    private String passwd;

    private String userid;

    private String email;

    private String creditcard;

    private String fullname;

    private String authtoken;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:sss", locale = "ENGLISH")
    private Date creationdate;

    private BigDecimal openbalance;

    private Integer logoutcount;

    private BigDecimal balance;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:sss", locale = "ENGLISH")
    private Date lastlogin;

    private Integer logincount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreditcard() {
        return creditcard;
    }

    public void setCreditcard(String creditcard) {
        this.creditcard = creditcard;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
    }

    public BigDecimal getOpenbalance() {
        return openbalance;
    }

    public void setOpenbalance(BigDecimal openbalance) {
        this.openbalance = openbalance;
    }

    public Integer getLogoutcount() {
        return logoutcount;
    }

    public void setLogoutcount(Integer logoutcount) {
        this.logoutcount = logoutcount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(Date lastlogin) {
        this.lastlogin = lastlogin;
    }

    public Integer getLogincount() {
        return logincount;
    }

    public void setLogincount(Integer logincount) {
        this.logincount = logincount;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", userid='" + userid + '\'' +
                ", email='" + email + '\'' +
                ", creditcard='" + creditcard + '\'' +
                ", fullname='" + fullname + '\'' +
                ", creationdate=" + creationdate +
                ", openbalance=" + openbalance +
                ", logoutcount=" + logoutcount +
                ", balance=" + balance +
                ", lastlogin=" + lastlogin +
                ", logincount=" + logincount +
                '}';
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }

        if (that == null || getClass() != that.getClass()) {
            return false;
        }

        return Objects.equals(this.userid, ((Account) that).userid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userid);
    }

}
