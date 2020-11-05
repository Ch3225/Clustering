package cn.edu.ujn.classifier;

import cn.edu.ujn.classifier.model.Line;
import cn.edu.ujn.classifier.model.Point;

/**
 * Hello world!
 *
 */
public class App {
	public static final String url="./Data/TrainingSet1.txt";
    public static void main( String[] args ){
        Point.loadData(url);
        Point.run();
        Line.outAll();
    }
}
