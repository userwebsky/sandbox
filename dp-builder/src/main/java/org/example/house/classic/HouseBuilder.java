package org.example.house.classic;

public interface HouseBuilder {
  void buildWalls();
  void buildFloors();
  void buildRooms();
  void roof();
  void windows();
  void doors();
  void garage();

  HouseClassicVersion getHouseClassicVersion();
}
