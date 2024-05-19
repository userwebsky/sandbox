package org.example.pricing.calcutate;

public class RegularPrice implements PricingStrategy {
  @Override
  public void calculatePrice(int price, boolean isSignedUpForNewsletter) {
    System.out.println(!isSignedUpForNewsletter ? "Normalna cena: " + price : "Użytkownik zapisany do newslettera, należy wybrać inną strategię cenową!");
  }
}
