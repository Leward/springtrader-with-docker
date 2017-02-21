package io.pivotal.springtrader.portfolio.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * Entity object representing an Order.
 *
 * @author David Ferreira Pinto
 */
@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "orderid")
    private Integer orderId;

    @Column(name = "accountid")
    @NotNull
    private String accountId;

    @Column(name = "symbol", length = 10)
    @NotNull
    private String symbol;

    @Column(name = "orderfee", precision = 14, scale = 2)
    private BigDecimal orderFee;

    @Column(name = "completiondate")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "LL")
    private Date completionDate;

    @Column(name = "ordertype")
    @NotNull
    @Enumerated
    private OrderType orderType;

    @Column(name = "price", precision = 14, scale = 2)
    @NotNull
    private BigDecimal price;

    @Column(name = "quantity")
    @NotNull
    private Integer quantity;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getOrderFee() {
        return orderFee;
    }

    public void setOrderFee(BigDecimal orderFee) {
        this.orderFee = orderFee;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", accountId='" + accountId + '\'' +
                ", symbol='" + symbol + '\'' +
                ", orderFee=" + orderFee +
                ", completionDate=" + completionDate +
                ", orderType=" + orderType +
                ", price=" + price +
                ", quantity=" + quantity +
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


        return Objects.equals(orderId, ((Order) that).orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

}
