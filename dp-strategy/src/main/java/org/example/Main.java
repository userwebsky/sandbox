package org.example;

import org.example.chef.Chef;
import org.example.chef.egg_cooker.HardBoildEggCooker;
import org.example.chef.egg_cooker.SoftBoildEggCooker;
import org.example.pricing.calcutate.PriceCalculator;
import org.example.pricing.calcutate.RegularPrice;
import org.example.pricing.calcutate.SalePrice;

public class Main {
  public static void main(String[] args) {
    /*
    Class Chef <--->      Interface EggCooker
        +cook()				      +cookEgg()
                            ^					    ^
                            |					    |
                  HardBoiledEggCooker		SoftBoiledEggCooker
                    +cookEgg()				+cookEgg()
    * */
    Chef chef = new Chef("Gordon");
    chef.setEggCooker(new HardBoildEggCooker());
    chef.cook();
    chef.setEggCooker(new SoftBoildEggCooker());
    chef.cook();


    /*
    Class PriceCalculator                               <----------------------------------->       Interface  PricingStrategy
    calculate(int price, boolean isSignedUpForNewsletter)                                             calculatePrice(int price, boolean isSignedUpForNewsletter);

                                                                                                        ^                                                   ^
                                                                                                        |                                                   |
                                                                                                    RegularPrice                                        SalePrice
                                                                       calculatePrice(int price, boolean isSignedUpForNewsletter)       calculatePrice(int price, boolean isSignedUpForNewsletter)
    * */
    PriceCalculator priceCalculator = new PriceCalculator();
    //Użytkownik nie jest zapisany do newslettera - normalna cena
    priceCalculator.setPricingStrategy(new RegularPrice());
    priceCalculator.calculate(100, false);
    //Użytkownik jest zapisany do newslettera - wybrana błędna strategia
    priceCalculator.setPricingStrategy(new RegularPrice());
    priceCalculator.calculate(100, true);
    //Użytkownik jest zapisany do newslettera
    priceCalculator.setPricingStrategy(new SalePrice());
    priceCalculator.calculate(100, true);
    //Użytkownik jest zapisany do newslettera - wybrana błędna strategia
    priceCalculator.setPricingStrategy(new SalePrice());
    priceCalculator.calculate(100, false);

  }
}
