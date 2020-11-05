package cn.edu.ujn.classifier.model;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Point {
	
	public final static Double TIMERCURRENCY=0.001;  		//时间刻度
	public final static Double CRASHRATIO=0.02;				//相撞最小距离
	public final static Integer STOPTIME=3000;				//在STOPTIME个时间刻度后停止
	
	protected static List<Point> allPoints;
	
	protected static Integer inputLength;
	protected static Double timer=0.0;
	
	protected Integer id;
	protected Double weight;
	protected Double[] input;
	
	protected Point() {}
	public Point(Integer id,Double weight,Double[] input) {
		this.id=id;
		this.weight=weight;
		this.input=new Double[input.length];
		for(int i=0;i<this.input.length;i++) {
			this.input[i]=input[i];
		}
	}
	
	public static void loadData(String url){
		List<Point> points=new ArrayList<Point>();
		Path filePath=Paths.get(url);     			//url="./Data/TrainingSet1.txt"
		try {
			Scanner scanner = new Scanner(filePath);
			scanner.nextLine();
			int datas=scanner.nextInt();
			inputLength=scanner.nextInt();
			for(int i=0;i<datas;i++) {
				Point point=new Point();
				point.id=scanner.nextInt();
				point.input=new Double[inputLength];
				for(int j=0;j<inputLength;j++) {
					point.input[j]=scanner.nextDouble();
				}
				point.weight=1.0;
				points.add(point);
				scanner.nextInt();
			}
			scanner.close();
			allPoints=points;
		} catch (IOException e) {
			System.out.println("文件不存在！");
			allPoints=null;
		}
	}
	public static void run() {
		int cnt=0;
		while(allPoints.size()>1&&cnt<STOPTIME) {
			System.out.println("迭代代数："+cnt);
			tick();
			cnt++;
		}
	}
	public static void tick() {
		List<Point> points=new ArrayList<Point>();
		for(Point p:allPoints) {
			points.add(clone(p));
		}
		points=add(points);
		points=crash(points);
		allPoints=points;
		timer+=TIMERCURRENCY;
	}
	public static List<Point> add(List<Point> l) {
		for(Point p:l) {
			for(Point q:allPoints) {
				if(p!=null&&q!=null) {
					if(p.id!=q.id) {
						p.move(p.affectedSpeed(q));
					}
				}
			}
		}
		return l;
	}
	public static List<Point> crash(List<Point> l) {
		Point[] points=new Point[l.size()];
		
		int cnt=0;
		for(Point p:l) {
			points[cnt]=clone(p);
			cnt++;
		}
		
		for(int i=0;i<cnt;i++) {
			for(int j=0;j<i;j++) {
				if(points[i]!=null&&points[j]!=null) {
					if(points[i].id!=points[j].id) {
						if(isCrash(points[i],points[j])) {
							Line.addLines(points[i],points[j],timer);
							points[i]=crash(points[i],points[j]);
							points[j]=null;
						}
					}
				}
			}
		}
		
		List<Point> list=new ArrayList<Point>();
		for(int i=0;i<cnt;i++) {
			if(points[i]!=null) {
				list.add(points[i]);
			}
		}
		return list;
	}


	public static boolean isCrash(Point a,Point b) {
		return distance(a.input,b.input)<0.1;
	}
	public static Point crash(Point a,Point b) {
		Point c=new Point();
		c.id=a.id;
		c.weight=a.weight+b.weight;
		c.input=b.input.clone();
		return c;
	}
	

	public static Double[] zeroInput() {
		Double[] a=new Double[inputLength];
		for(int i=0;i<inputLength;i++) {
			a[i]=0.0;
		}
		return a;
	}
	public static Point clone(Point point) {
		Point p=new Point(point.id,point.weight,point.input.clone());
		return p;
	}
	public void move(Double[] a) {
		if(a.length!=this.input.length) {
			System.out.println("错误：维度不正确");
		}
		else {
			for(int i=0;i<a.length;i++) {
				this.input[i]+=a[i];
			}
		}
	}
	public Double[] affectedSpeed(Point a) {
		Double[] tmp=zeroInput();
		Double distance=distance(this.input,a.input);
		if(distance<CRASHRATIO) {
			distance=CRASHRATIO;
		}
		for(int i=0;i<inputLength;i++) {
			Double fenzi=(a.input[i]-this.input[i])*TIMERCURRENCY*a.weight;
			Double fenmu=(Math.pow(distance,2));
			tmp[i]=fenzi/fenmu;
		}
		return tmp;
	}
	public static Double distance(Double[] a,Double[] b) {
		if(a.length!=b.length) {
			return -1.0;
		}
		else {
			Double c=0.00001;
			for(int j=0;j<a.length;j++) {
				c+=Math.pow((a[j]-b[j]), 2);
			}
			//System.out.println("distance:"+c);
			return Math.sqrt(c);
		}
	}
	@Override
	public String toString() {
		return "Point [id=" + id + ", weight=" + weight + ", input=" + Arrays.toString(input) + "]";
	}
	
	
	//Not Used
	public static Double[] plus(Double[] a,Double[] b) {
		if(a.length!=b.length) {
			return null;
		}
		else {
			Double[] c=new Double[a.length];
			for(int j=0;j<a.length;j++) {
				c[j]=a[j]+b[j];
			}
			return c;
		}
	}
}
