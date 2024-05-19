package org.example.pricing.calcutate;

public class SalePrice implements PricingStrategy {
  @Override
  public void calculatePrice(int price, boolean isSignedUpForNewsletter) {
    System.out.println(isSignedUpForNewsletter ? "Przecena: " + (price * 0.5) : "Użytkownik nie jest zapisany do newslettera, należy wybrać inną strategię cenową!");
  }
}
