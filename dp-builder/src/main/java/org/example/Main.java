package org.example;

import org.example.flight.FlightLeg;
import org.example.house.House;
import org.example.house.classic.BigHouseBuilder;
import org.example.house.classic.HouseDirector;
import org.example.house.classic.SmallHouseBuilder;

import java.math.BigDecimal;

public class Main {
  public static void main(String[] args) {
    House house = new House.Builder()
      .builderFloors("Walless")
      .builderFloors("Floress")
      .build();
    System.out.println(house);

    System.out.println("---Classic version---");

    SmallHouseBuilder smallHouseBuilder = new SmallHouseBuilder();
    BigHouseBuilder bigHouseBuilder = new BigHouseBuilder();

    HouseDirector smallHouseDirector = new HouseDirector(smallHouseBuilder);
    smallHouseDirector.buildHouse();

    HouseDirector bigHouseDirector = new HouseDirector(bigHouseBuilder);
    bigHouseDirector.buildHouse();

    System.out.println(smallHouseBuilder.getHouseClassicVersion());
    System.out.println(bigHouseBuilder.getHouseClassicVersion());

    System.out.println("---Fly example---");
    FlightLeg leg = new FlightLeg.Builder().builderFrom("Las Vegas").builderTo("Los Angeles").price(new BigDecimal("50.00")).build();

    System.out.println(leg);
  }}
