package org.example.flight;

import java.math.BigDecimal;

public class FlightLeg {
  private String from;
  private String to;
  private Boolean delayed = Boolean.FALSE;
  private BigDecimal price;

  public FlightLeg(String from, String to, Boolean delayed, BigDecimal price) {
    this.from = from;
    this.to = to;
    this.delayed = delayed;
    this.price = price;
  }

  private FlightLeg(Builder builder) {
    this.from = builder.from;
    this.to = builder.to;
    this.delayed = builder.delayed;
    this.price = builder.price;
  }

  @Override
  public String toString() {
    return "FlightLeg{" +
      "from='" + from + '\'' +
      ", to='" + to + '\'' +
      ", delayed='" + delayed + '\'' +
      ", price=" + price +
      '}';
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public Boolean getDelayed() {
    return delayed;
  }

  public void setDelayed(Boolean delayed) {
    this.delayed = delayed;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public static class Builder {
    private String from;
    private String to;
    private Boolean delayed = Boolean.FALSE;
    private BigDecimal price;

    public Builder builderFrom(String from) {
      this.from = from;
      return this;
    }

    public Builder builderTo(String to) {
      this.to = to;
      return this;
    }

    public Builder delayed(Boolean delayed) {
      this.delayed = delayed;
      return this;
    }

    public Builder price(BigDecimal price) {
      this.price = price;
      return this;
    }

    public FlightLeg build() {
          return new FlightLeg(this);
      }
  }
}
