package org.example.chef;

import org.example.chef.egg_cooker.EggCooker;

public class Chef {
  private String name;
  private EggCooker eggCooker;

  public Chef(String name) {
    this.name = name;
  }

  public void cook() {
    this.eggCooker.cookEgg();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEggCooker(EggCooker eggCooker) {
    this.eggCooker = eggCooker;
  }

  public EggCooker getEggCooker() {
    return eggCooker;
  }
}
