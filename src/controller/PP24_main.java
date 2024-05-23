package controller;

import model.*;

import java.util.ArrayList;

public class PP24_main {

	public static void main(String[] args) {
		int[][] baseDataInput1 = {
				{1, 4, 4, 4, 4, 4, 3},
				{4, 2, 4, 2, 4, 2, 2},
				{4, 2, 1, 2, 4, 2, 2},
				{4, 2, 2, 2, 4, 4, 4},
				{4, 3, 2, 2, 1, 4, 3},
				{4, 4, 2, 4, 4, 3, 2}
		};

		int[][] valueDataInput1 = {
				{10, 10, 10, 20, 60, 60, 60},
				{10, 10, 10, 20, 20, 20, 60},
				{10, 10, 10, 20, 70, 20, 60},
				{20, 40, 20, 20, 70, 20, 20},
				{20, 40, 20, 20, 70, 20, 20},
				{40, 40, 40, 20, 50, 20, 20}
		};



		BasicController basicControllerObject1 = new BasicController(baseDataInput1, valueDataInput1);
		BasicMap completeBasicMap = basicControllerObject1.getCompleteBasicMap();
		BasicVertex[][] vertexArrayObject1 = completeBasicMap.getVertexArray();

		BasicVertex basicVertexObject20 = vertexArrayObject1[2][0];
		ArrayList<BasicVertex> inputVertexList1 = new ArrayList<>();
		inputVertexList1.add(basicVertexObject20);
		inputVertexList1.add(basicVertexObject20);
		System.out.println(completeBasicMap.isBasicPathOverStreets(inputVertexList1)); // should return false

		BasicVertex basicVertexObject10 = vertexArrayObject1[1][0];
		BasicVertex basicVertexObject11 = vertexArrayObject1[1][1];
		ArrayList<BasicVertex> inputVertexList2 = new ArrayList<>();
		inputVertexList2.add(basicVertexObject10);
		inputVertexList2.add(basicVertexObject11);
		inputVertexList2.add(basicVertexObject10);
		System.out.println(completeBasicMap.isBasicPathOverStreets(inputVertexList2)); // should return false

		BasicVertex basicVertexObject16 = vertexArrayObject1[1][6];
		BasicVertex basicVertexObject15 = vertexArrayObject1[1][5];
		BasicVertex basicVertexObject25 = vertexArrayObject1[2][5];
		BasicVertex basicVertexObject35 = vertexArrayObject1[3][5];
		ArrayList<BasicVertex> inputVertexList3 = new ArrayList<>();
		inputVertexList3.add(basicVertexObject16);
		inputVertexList3.add(basicVertexObject15);
		inputVertexList3.add(basicVertexObject25);
		inputVertexList3.add(basicVertexObject35);
		System.out.println(completeBasicMap.isBasicPathOverStreets(inputVertexList3)); // should return true

		BasicVertex basicVertexObject06 = vertexArrayObject1[0][6];
		BasicVertex basicVertexObject162 = vertexArrayObject1[1][6];
		BasicVertex basicVertexObject152 = vertexArrayObject1[1][5];
		BasicVertex basicVertexObject252 = vertexArrayObject1[2][5];
		BasicVertex basicVertexObject26 = vertexArrayObject1[2][6];
		BasicVertex basicVertexObject352 = vertexArrayObject1[3][5];
		ArrayList<BasicVertex> inputVertexList4 = new ArrayList<>();
		inputVertexList4.add(basicVertexObject06);
		inputVertexList4.add(basicVertexObject162);
		inputVertexList4.add(basicVertexObject152);
		inputVertexList4.add(basicVertexObject252);
		inputVertexList4.add(basicVertexObject26);
		inputVertexList4.add(basicVertexObject352);
		System.out.println(completeBasicMap.isBasicPathOverStreets(inputVertexList4)); // should return false
//
//		BasicVertex basicVertexObject05 = vertexArrayObject1[0][5];
//		BasicVertex basicVertexObject153 = vertexArrayObject1[1][5];
//		BasicVertex basicVertexObject253 = vertexArrayObject1[2][5];
//		ArrayList<BasicVertex> inputVertexList5 = new ArrayList<>();
//		inputVertexList5.add(basicVertexObject05);
//		inputVertexList5.add(basicVertexObject153);
//		inputVertexList5.add(basicVertexObject253);
//		System.out.println(completeBasicMap.isBasicPathOverStreets(inputVertexList5)); // should return true
	}











		//TODO: Kommentare löschen



	}
