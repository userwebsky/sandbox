package org.example.house;

public class House {
  private String walls;
  private String floors;
  private String rooms;
  private String roof;
  private String windows;
  private String doors;
  private String garage;

  private House(Builder builder) {
    this.walls = builder.walls;
    this.floors = builder.floors;
    this.rooms = builder.rooms;
    this.roof = builder.roof;
    this.windows = builder.windows;
    this.doors = builder.doors;
    this.garage = builder.garage;
  }

  public String getWalls() {
    return walls;
  }

  public String getFloors() {
    return floors;
  }

  public String getRooms() {
    return rooms;
  }

  public String getRoof() {
    return roof;
  }

  public String getWindows() {
    return windows;
  }

  public String getDoors() {
    return doors;
  }

  public String getGarage() {
    return garage;
  }

  @Override
  public String toString() {
    return "House{" +
      "walls='" + walls + '\'' +
      ", floors='" + floors + '\'' +
      ", rooms='" + rooms + '\'' +
      ", roof='" + roof + '\'' +
      ", windows='" + windows + '\'' +
      ", doors='" + doors + '\'' +
      ", garage='" + garage + '\'' +
      '}';
  }

  public static class Builder {
    private String walls;
    private String floors;
    private String rooms;
    private String roof;
    private String windows;
    private String doors;
    private String garage;

    public Builder builderWalls(String walls) {
      this.walls = walls;
      return this;
    }

    public Builder builderFloors(String floors) {
      this.floors = floors;
      return this;
    }

    public Builder builderRooms(String rooms) {
      this.rooms = rooms;
      return this;
    }

    public Builder builderRoof(String roof) {
      this.roof = roof;
      return this;
    }

    public Builder builderWindows(String windows) {
      this.windows = windows;
      return this;
    }

    public Builder builderDoors(String doors) {
      this.doors = doors;
      return this;
    }

    public Builder builderGarage(String garage) {
      this.garage = garage;
      return this;
    }

    public House build() {
      return new House(this);
    }
  }
}
