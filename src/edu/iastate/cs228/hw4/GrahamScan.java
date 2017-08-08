package edu.iastate.cs228.hw4;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.util.Comparator; 

/**
 * 
 * @author Lucas Golinghorst
 *
 */

public class GrahamScan extends ConvexHull
{
	/**
	 * Stack used by Graham's scan to store the vertices of the convex hull of the points 
	 * scanned so far.  At the end of the scan, it stores the hull vertices in the 
	 * counterclockwise order. 
	 **/
	
	private PureStack<Point> vertexStack;  


	/**
	 * Call corresponding constructor of the super class.  Initialize the variables algorithm 
	 * (from the class ConvexHull) and vertexStack. 
	 *  
	 * @throws IllegalArgumentException  if pts.length == 0
	 */
	public GrahamScan(Point[] pts) throws IllegalArgumentException 
	{
		super(pts); 
		algorithm = "Graham's scan";
		vertexStack = new ArrayBasedStack<Point>();
	}
	
	/**
	 * Call corresponding constructor of the super class.  Initialize algorithm and vertexStack.  
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException   when the input file contains an odd number of integers
	 */
	public GrahamScan(String inputFileName) throws FileNotFoundException, InputMismatchException
	{
		super(inputFileName); 
		algorithm = "Graham's scan";
		vertexStack = new ArrayBasedStack<Point>();
	}

	// -------------
	// Graham's scan
	// -------------
	
	/**
	 * This method carries out Graham's scan in several steps below: 
	 * 
	 *     1) Call the private method setUpScan() to sort all the points in the array 
	 *        pointsNoDuplicate[] by polar angle with respect to lowestPoint.    
	 *        
	 *     2) Perform Graham's scan. To initialize the scan, push pointsNoDuplicate[0] and 
	 *        pointsNoDuplicate[1] onto vertexStack.  
	 * 
     *     3) As the scan terminates, vertexStack holds the vertices of the convex hull.  Pop the 
     *        vertices out of the stack and add them to the array hullVertices[], starting at index
     *        vertexStack.size() - 1, and decreasing the index toward 0.    
     *        
     * Two degenerate cases below must be handled: 
     * 
     *     1) The array pointsNoDuplicates[] contains just one point, in which case the convex
     *        hull is the point itself. 
     *     
     *     2) The array contains only two points, in which case the hull is the line segment 
     *        connecting them.   
	 */
	public void constructHull()
	{
		long time1 = System.nanoTime();
		setUpScan();	
		hullVertices = new Point[pointsNoDuplicate.length];
		if(pointsNoDuplicate.length < 3) //handling all degenerate cases
		{
			for(int i = 0; i < pointsNoDuplicate.length; i++)
			{
				hullVertices[i] = pointsNoDuplicate[i];
			}
			time = System.nanoTime() - time1;
			return;
		}
	    vertexStack.push(pointsNoDuplicate[0]);   
	    int length = pointsNoDuplicate.length;
	   
	    int a = 0;
	    //need to go through and find the first non collinear point
	    for (a = 1; a < length; a++)
	    {
	    	//break if it does not equal lowest point
	    		if (!pointsNoDuplicate[0].equals(pointsNoDuplicate[a]))
	            {
	            	break;
	            }
	    }
	    if (a == length) 
	    {
	    	return;  //points are all the same     
	    }    
	    int c = a + 1;
	    int b = 0;
	    for(b = c; b < length; b++)
	        {
	    	//find the second non collinear point
	            if (counterclockturn(pointsNoDuplicate[0], pointsNoDuplicate[a], pointsNoDuplicate[b]) != 0)
	            {
	            	//we found a noncollinear point so break out
	            	break;
	            }
	        } 
	    vertexStack.push(pointsNoDuplicate[b - 1]); //add it to the stack   
        for (int i = b; i < length; i++) 
	        {
	            Point onTop = vertexStack.pop();
	            while (counterclockturn(vertexStack.peek(), onTop, pointsNoDuplicate[i]) <= 0) //if we make a nonleft turn
	            {
	                onTop = vertexStack.pop(); //pop another point from stack
	            }
	            vertexStack.push(onTop);
	            vertexStack.push(pointsNoDuplicate[i]); //now we push these two points
	            										//onTop is added back to the stack
	        }
		hullVertices = new Point[vertexStack.size()];
		int index = vertexStack.size()-1; //start at last index of array
		while(!vertexStack.isEmpty())
		{
			hullVertices[index] = vertexStack.pop(); //put back into array
			index--;
		}
		time = System.nanoTime() - time1;	
	}
	
	/**
	 * Set the variable quicksorter from the class ConvexHull to sort by polar angle with respect 
	 * to lowestPoint, and call quickSort() from the QuickSortPoints class on pointsNoDuplicate[]. 
	 * The argument supplied to quickSort() is an object created by the constructor call 
	 * PolarAngleComparator(lowestPoint, true).       
	 * 
	 * Ought to be private, but is made public for testing convenience. 
	 *
	 */
	public void setUpScan()
	{
		Comparator<Point> p = new PolarAngleComparator(lowestPoint, true);   
		quicksorter = new QuickSortPoints(pointsNoDuplicate); //put pointsNoDuplicate into constructor
		quicksorter.quickSort(p); //sort the points
		quicksorter.getSortedPoints(pointsNoDuplicate); //get the sorted points back 
	}	
	
	  /**
     * Returns 1 if it is a counterclockwise turn.
     * @param a first point
     * @param b second point
     * @param c third point
     * @return -1 if clockwise
     * @return 0 if collinear 
     * @return 1 if counterclockwise
     */
    private int counterclockturn(Point first, Point middle, Point last) 
    {
       double area = (middle.getX()-first.getX()) * (last.getY()-first.getY()) - (middle.getY()-first.getY()) * (last.getX() - first.getX());
       if(area < 0) 
       {
    	   return -1;
       }
       else if(area > 0) 
       {
    	   return 1;
       }
       else
       {
    	   return  0;
       }
    }
}
