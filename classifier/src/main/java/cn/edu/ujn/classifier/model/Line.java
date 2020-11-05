package cn.edu.ujn.classifier.model;

import java.util.ArrayList;
import java.util.List;

public class Line {
	public static List<Line> lines=new ArrayList<Line>();
	
	protected Integer id1;
	protected Integer id2;
	protected Double weight;
	
	private Line() {}
	public static void outAll() {
		for(Line l:lines) {
			System.out.println(l);;
		}
	}
	public static void addLines(Point a,Point b,Double time) {
		Line line=new Line();
		line.id1=a.id;
		line.id2=b.id;
		line.weight=time;
		lines.add(line);
	}

	@Override
	public String toString() {
		return "Line [id1=" + id1 + ", id2=" + id2 + ", weight=" + weight + "]";
	}
	
	
}
