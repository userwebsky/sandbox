package org.example;

import org.example.house.House;

public class Main {
  public static void main(String[] args) {
    House house = new House.Builder()
      .builderFloors("Walless")
      .builderFloors("Floress")
      .build();
    System.out.println(house);
  }}
