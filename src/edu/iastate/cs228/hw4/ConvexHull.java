package edu.iastate.cs228.hw4;

/**
 * @author Lucas Golinghorst
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.io.File;
import java.io.FileNotFoundException; 
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.io.PrintWriter;
import java.util.Random; 
import java.util.Scanner;

/**
 * This class implements construction of the convex hull of a finite set of points. 
 */

public abstract class ConvexHull 
{
	// ---------------
	// Data Structures 
	// ---------------
	protected String algorithm;  // has value either "Graham's scan" or "Jarvis' march". Initialized by a subclass.
	
	protected long time;         // execution time in nanoseconds
	
	/**
	 * The array points[] holds an input set of Points, which may be randomly generated or 
	 * input from a file.  Duplicates are possible. 
	 */
	private Point[] points;    

	/**
	 * Lowest point from points[]; and in case of a tie, the leftmost one of all such points. 
	 * To be set by a constructor. 
	 */
	protected Point lowestPoint; 

	/**
	 * This array stores the same set of points from points[] with all duplicates removed. 
	 * These are the points on which Graham's scan and Jarvis' march will be performed. 
	 */
	protected Point[] pointsNoDuplicate; 
	
	/**
	 * Vertices of the convex hull in counterclockwise order are stored in the array 
	 * hullVertices[], with hullVertices[0] storing lowestPoint. 
	 */
	protected Point[] hullVertices;
	
	protected QuickSortPoints quicksorter;  // used (and reset) by this class and its subclass GrahamScan
		
	// ------------
	// Constructors
	// ------------
		
	/**
	 * Constructor over an array of points.  
	 * 
	 *    1) Store the points in the private array points[].
	 *    
	 *    2) Initialize quicksorter. 
	 *    
	 *    3) Call removeDuplicates() to store distinct points from the input in pointsNoDuplicate[].
	 *    
	 *    4) Set lowestPoint to pointsNoDuplicate[0]. 
	 * 
	 * @param pts
	 * @throws IllegalArgumentException  if pts.length == 0
	 */
	public ConvexHull(Point[] pts) throws IllegalArgumentException 
	{
		if(pts.length == 0 || pts == null)
		{
			throw new IllegalArgumentException();
		}
		points = new Point[pts.length];
		for(int i = 0; i < pts.length; i++)
		{
			points[i] = pts[i];
		}
		quicksorter = new QuickSortPoints(points);
		removeDuplicates();
		lowestPoint = pointsNoDuplicate[0];
	}
	/**
	 * Read integers from an input file.  Every pair of integers represent the x- and y-coordinates 
	 * of a point.  Generate the points and store them in the private array points[]. The total 
	 * number of integers in the file must be even.
	 * 
	 * You may declare a Scanner object and call its methods such as hasNext(), hasNextInt() 
	 * and nextInt(). An ArrayList may be used to store the input integers as they are read in 
	 * from the file.  
	 * 
	 * Perform the operations 1)-4) for the first constructor. 
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException   when the input file contains an odd number of integers
	 */
	public ConvexHull(String inputFileName) throws FileNotFoundException, InputMismatchException
	{
		File file = new File(inputFileName);
	    Scanner in = new Scanner(file);
	    int count = 0;
	    while(in.hasNextInt())
	    {
	    	in.nextInt();
	    	count++; //count the number of numbers in the file
	    }
	    in.close();
	    if(count % 2 == 1)
		{
		    throw new InputMismatchException(); //odd number of integers
		}
	    if(count < 2) //if the file does not contain any numbers or only 1 number
	    {
	    	throw new NoSuchElementException();
	    }
	    points = new Point[count / 2]; //count needs to be divided by two because each number is a x and y of the point
	    Scanner in1 = new Scanner(file);
	    int index = 0;
		while(in1.hasNext()) //scan numbers and assign to point objects
		{
			int tempX = in1.nextInt();
			int tempY = in1.nextInt();
			points[index] = new Point(tempX, tempY);		
			index++;
		}
		in1.close();
		quicksorter = new QuickSortPoints(points);
		removeDuplicates();
		lowestPoint = pointsNoDuplicate[0];
	}

	/**
	 * Construct the convex hull of the points in the array pointsNoDuplicate[]. 
	 */
	public abstract void constructHull(); 
	
	/**
	 * Outputs performance statistics in the format: 
	 * 
	 * <convex hull algorithm> <size>  <time>
	 * 
	 * For instance, 
	 * 
	 * Graham's scan   1000	  9200867
	 * 
	 * Use the spacing in the sample run in Section 4 of the project description. 
	 */
	public String stats()
	{
		String a = ""; //initialize string
		
		String size1 = points.length + ""; //put size into string
		
		while(size1.length() < 12) //go until string is 12 long
		{
			size1 += " "; //add a new blank space
		}
		a = a + algorithm + "     " + size1 + time; //put string together 
		return a; 
	}
	
	/**
	 * The string displays the convex hull with vertices in counterclockwise order starting at  
	 * lowestPoint.  When printed out, it will list five points per line with three blanks in 
	 * between. Every point appears in the format "(x, y)".  
	 * 
	 * For illustration, the convex hull example in the project description will have its 
	 * toString() generate the output below: 
	 * 
	 * (-7, -10)   (0, -10)   (10, 5)   (0, 8)   (-10, 0)   
	 * 
	 * lowestPoint is listed only ONCE.  
	 */
	public String toString()
	{	
		String a = "";
		int counter = 0; //counting how many points we have added to one line so far
		for(int i = 0; i < hullVertices.length; i++) //iterate through the whole array
		{
			if(counter == 5) //go to a new line if you have added 5 points
			{
				a += "\n";
				counter = 0;
			}
			else
			{
				//add point as a string and increment the counter
				String point = hullVertices[i].toString();
				a += point;
				a += "   ";
				counter++;
			}
		}
		return a; 
	}
	
	/** 
	 * 
	 * Writes to the file "hull.txt" the vertices of the constructed convex hull in counterclockwise 
	 * order.  These vertices are in the array hullVertices[], starting with lowestPoint.  Every line
	 * in the file displays the x and y coordinates of only one point.  
	 * 
	 * For instance, the file "hull.txt" generated for the convex hull example in the project 
	 * description will have the following content: 
	 * 
     *  -7 -10 
     *  0 -10
     *  10 5
     *  0  8
     *  -10 0
	 * 
	 * The generated file is useful for debugging as well as grading. 
	 * 
	 * Called only after constructHull().  
	 * 
	 * 
	 * @throws IllegalStateException  if hullVertices[] has not been populated (i.e., the convex 
	 *                                   hull has not been constructed)
	 * @throws FileNotFoundException 
	 */
	public void writeHullToFile() throws IllegalStateException, FileNotFoundException 
	{
		if(hullVertices == null)
		{
			throw new IllegalStateException();
		}
		File file  = new File("hull.txt"); 
	    PrintWriter writer = new PrintWriter(file);
	    String a = "";
	    
	    
	    for(int i = 0; i < hullVertices.length; i++) //iterate through array and create strings for coordinates
	    {
	    	/*String x = hullVertices[i].getX() + "";
	    	String y = hullVertices[i].getY() + "";
	    	
	    	String point = x + " " + y;
	    	
	    	a += point;
	    	a += "\n"; //add point to the string and newline*/
	    	writer.printf("%d %d\n", hullVertices[i].getX(), hullVertices[i].getY());
	    }
	
	    writer.close();
	}
	
	/**
	 * Draw the points and their convex hull.  This method is called after construction of the 
	 * convex hull.  You just need to make use of hullVertices[] to generate a list of segments 
	 * as the edges. Then create a Plot object to call the method myFrame().  
	 */
	public void draw()
	{		
		ArrayList<Segment> seg = new ArrayList<Segment>();
		for(int i = 0; i < hullVertices.length - 1; i++)
		{
			Segment s = new Segment(hullVertices[i], hullVertices[i+1]);
			seg.add(s);
		}
		Segment s1 = new Segment(hullVertices[hullVertices.length-1],  hullVertices[0]);
		seg.add(s1);
		// Based on Section 4.1, generate the line segments to draw for display of the convex hull.
		// Assign their number to numSegs, and store them in segments[] in the order.
		Segment[] segments = new Segment[seg.size()];
		for(int i = 0; i < seg.size(); i++)
		{
			segments[i] = seg.get(i);
		}
		// The following statement creates a window to display the convex hull.
		Plot.myFrame(pointsNoDuplicate, segments, getClass().getName());
	}		
	/**
	 * Sort the array points[] by y-coordinate in the non-decreasing order.  Have quicksorter 
	 * invoke quicksort() with a comparator object which uses the compareTo() method of the Point 
	 * class. Copy the sorted sequence onto the array pointsNoDuplicate[] with duplicates removed.
	 *     
	 * Ought to be private, but is made public for testing convenience. 
	 */
	public void removeDuplicates()
	{
		Comparator<Point> comp = new Comparator<Point>() 
		{
             @Override
             public int compare(Point p, Point q) 
             {
            	 return p.compareTo(q);
             }
        };
		quicksorter.quickSort(comp);
		quicksorter.getSortedPoints(points);
		
		ArrayList<Point> pts = new ArrayList<Point>();
		for(int a = 0; a < points.length; a++)   //copy points into temp array
		{
			if(!pts.contains(points[a]))
			{
				pts.add(points[a]);
			}
		}
		pointsNoDuplicate = new Point[pts.size()];  //insert into pointsNoDuplicate
		for(int i = 0; i < pts.size(); i++)
		{
			pointsNoDuplicate[i] = pts.get(i);
		}
	}
}
