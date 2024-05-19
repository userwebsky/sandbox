package org.example.pricing.calcutate;

public class PriceCalculator {
  private PricingStrategy pricingStrategy;

  public void calculate(int price, boolean isSignedUpForNewsletter) {
    pricingStrategy.calculatePrice(price, isSignedUpForNewsletter);
  }

  public PricingStrategy getPricingStrategy() {
    return pricingStrategy;
  }

  public void setPricingStrategy(PricingStrategy pricingStrategy) {
    this.pricingStrategy = pricingStrategy;
  }
}
