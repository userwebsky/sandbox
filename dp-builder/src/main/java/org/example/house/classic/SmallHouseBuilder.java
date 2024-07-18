package org.example.house.classic;

public class SmallHouseBuilder implements HouseBuilder {
  private HouseClassicVersion houseClassicVersion;

  public SmallHouseBuilder() {
    this.houseClassicVersion = new HouseClassicVersion();
  }

  @Override
  public void buildWalls() {
    this.houseClassicVersion.setWalls("small walls");
  }

  @Override
  public void buildFloors() {
    this.houseClassicVersion.setFloors("small floors");
  }

  @Override
  public void buildRooms() {
    this.houseClassicVersion.setRooms("small rooms");
  }

  @Override
  public void roof() {
    this.houseClassicVersion.setRooms("small roof");
  }

  @Override
  public void windows() {
    this.houseClassicVersion.setWindows("small windows");
  }

  @Override
  public void doors() {
    this.houseClassicVersion.setDoors("small doors");
  }

  @Override
  public void garage() {
    this.houseClassicVersion.setGarage("small garage");
  }

  @Override
  public HouseClassicVersion getHouseClassicVersion() {
    return houseClassicVersion;
  }
}
