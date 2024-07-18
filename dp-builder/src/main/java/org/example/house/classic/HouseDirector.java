package org.example.house.classic;

public class HouseDirector {
  private HouseBuilder houseBuilder;

  public HouseDirector(HouseBuilder houseBuilder) {
        this.houseBuilder = houseBuilder;
    }

 public void buildHouse() {
    houseBuilder.buildWalls();
     houseBuilder.buildFloors();
     houseBuilder.buildRooms();
     houseBuilder.roof();
     houseBuilder.windows();
     houseBuilder.doors();
     houseBuilder.garage();
 }

 public HouseClassicVersion getHouseClassicVersion() {
    return houseBuilder.getHouseClassicVersion();
 }
}
