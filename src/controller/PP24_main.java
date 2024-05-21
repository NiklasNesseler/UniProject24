package controller;

import model.*;

import java.util.ArrayList;

public class PP24_main {

	public static void main(String[] args) {
		int[][] baseDataInput1 = {{4, 4, 4, 4, 4, 4, 3},
				                  {4, 2, 4, 2, 4, 2, 2},
				                  {4, 2, 4, 2, 4, 2, 2},
				                  {4, 2, 2, 2, 4, 4, 4},
				                  {4, 3, 2, 2, 2, 4, 3},
				                  {4, 4, 2, 4, 4, 3, 2}};

		int[][] valueDataInput1 = {{10, 10, 10, 20, 60, 60, 60},
				                   {10, 10, 10, 20, 80, 80, 60},
				                   {10, 10, 10, 20, 70, 80, 60},
				                   {20, 40, 20, 20, 70, 80, 80},
				                   {20, 40, 20, 20, 70, 80, 80},
				                   {40, 40, 40, 20, 50, 80, 80}};



		BasicController basicControllerObject3 = new BasicController(baseDataInput1, valueDataInput1);

		BasicMap completeBasicMap3 = basicControllerObject3.getCompleteBasicMap();

		System.out.println("Expected false: " + completeBasicMap3.isBasicPathByValue(0));
		System.out.println("Expected true: " + completeBasicMap3.isBasicPathByValue(10));
		System.out.println("Expected false: " + completeBasicMap3.isBasicPathByValue(20));
		System.out.println("Expected false: " + completeBasicMap3.isBasicPathByValue(40));
		System.out.println("Expected true: " + completeBasicMap3.isBasicPathByValue(50));
		System.out.println("Expected true: " + completeBasicMap3.isBasicPathByValue(60));
		System.out.println("Expected true: " + completeBasicMap3.isBasicPathByValue(70));



		//TODO: Kommentare löschen



	}

}
