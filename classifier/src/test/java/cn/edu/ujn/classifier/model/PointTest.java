package cn.edu.ujn.classifier.model;

import org.junit.Test;


public class PointTest {
	
	@Test
	public void loadDataTest() {
		Point.loadData("./Data/TrainingSet1.txt");
		for(Point p:Point.allPoints) {
			System.out.println(p);
		}
	}
	@Test
	public void runTest() {
		loadDataTest();
		
	}
}
