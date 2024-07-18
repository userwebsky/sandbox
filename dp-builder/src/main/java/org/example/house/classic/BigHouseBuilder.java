package org.example.house.classic;

public class BigHouseBuilder implements HouseBuilder {
  private HouseClassicVersion houseClassicVersion;

  public BigHouseBuilder() {
    this.houseClassicVersion = new HouseClassicVersion();
  }

  @Override
  public void buildWalls() {
    this.houseClassicVersion.setWalls("big walls");
  }

  @Override
  public void buildFloors() {
    this.houseClassicVersion.setFloors("big floors");
  }

  @Override
  public void buildRooms() {
    this.houseClassicVersion.setRooms("big rooms");
  }

  @Override
  public void roof() {
    this.houseClassicVersion.setRooms("big roof");
  }

  @Override
  public void windows() {
    this.houseClassicVersion.setWindows("big windows");
  }

  @Override
  public void doors() {
    this.houseClassicVersion.setDoors("big doors");
  }

  @Override
  public void garage() {
    this.houseClassicVersion.setGarage("big garage");
  }

  @Override
  public HouseClassicVersion getHouseClassicVersion() {
    return houseClassicVersion;
  }
}
