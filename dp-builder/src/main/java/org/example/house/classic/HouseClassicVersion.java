package org.example.house.classic;

public class HouseClassicVersion {
  private String walls;
  private String floors;
  private String rooms;
  private String roof;
  private String windows;
  private String doors;
  private String garage;

  public String getGarage() {
    return garage;
  }

  public void setGarage(String garage) {
    this.garage = garage;
  }

  public String getDoors() {
    return doors;
  }

  public void setDoors(String doors) {
    this.doors = doors;
  }

  public String getWindows() {
    return windows;
  }

  public void setWindows(String windows) {
    this.windows = windows;
  }

  public String getRoof() {
    return roof;
  }

  public void setRoof(String roof) {
    this.roof = roof;
  }

  public String getRooms() {
    return rooms;
  }

  public void setRooms(String rooms) {
    this.rooms = rooms;
  }

  public String getFloors() {
    return floors;
  }

  public void setFloors(String floors) {
    this.floors = floors;
  }

  public String getWalls() {
    return walls;
  }

  public void setWalls(String walls) {
    this.walls = walls;
  }

  @Override
  public String toString() {
    return "HouseClassicVersion{" +
      "walls='" + walls + '\'' +
      ", floors='" + floors + '\'' +
      ", rooms='" + rooms + '\'' +
      ", roof='" + roof + '\'' +
      ", windows='" + windows + '\'' +
      ", doors='" + doors + '\'' +
      ", garage='" + garage + '\'' +
      '}';
  }
}
