package org.example.pricing.calcutate;

public interface PricingStrategy {
  void calculatePrice(int price, boolean isSignedUpForNewsletter);
}
