package edu.iastate.cs228.hw4;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.util.Comparator; 

/**
 * @author Lucas Golinghorst
 */

public class JarvisMarch extends ConvexHull
{
	// last element in pointsNoDuplicate(), i.e., highest of all points (and the rightmost one in case of a tie)
	private Point highestPoint; 
	
	// left chain of the convex hull counterclockwise from lowestPoint to highestPoint
	private PureStack<Point> leftChain; 
	
	// right chain of the convex hull counterclockwise from highestPoint to lowestPoint
	private PureStack<Point> rightChain; 
		

	/**
	 * Call corresponding constructor of the super class.  Initialize the variable algorithm 
	 * (from the class ConvexHull). Set highestPoint. Initialize the two stacks leftChain 
	 * and rightChain. 
	 * 
	 * @throws IllegalArgumentException  when pts.length == 0
	 */
	public JarvisMarch(Point[] pts) throws IllegalArgumentException 
	{
		super(pts);
		algorithm = "Jarvis' march";
		highestPoint = pointsNoDuplicate[pointsNoDuplicate.length - 1];
		leftChain = new ArrayBasedStack<Point>();
		rightChain = new ArrayBasedStack<Point>();
	}

	/**
	 * Call corresponding constructor of the superclass.  Initialize the variable algorithm.
	 * Set highestPoint.  Initialize leftChain and rightChain.  
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException   when the input file contains an odd number of integers
	 */
	public JarvisMarch(String inputFileName) throws FileNotFoundException, InputMismatchException
	{
		super(inputFileName);
		algorithm = "Jarvis' march";
		highestPoint = pointsNoDuplicate[pointsNoDuplicate.length - 1];
		leftChain = new ArrayBasedStack<Point>();
		rightChain = new ArrayBasedStack<Point>();
	}

	// ------------
	// Jarvis' march
	// ------------

	/**
	 * Calls createRightChain() and createLeftChain().  Merge the two chains stored on the stacks  
	 * rightChain and leftChain into the array hullVertices[].
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
		createRightChain();
		createLeftChain();
		leftChain.pop();
		rightChain.pop();
		int index = leftChain.size() + rightChain.size();
		hullVertices = new Point[index]; //assign hullVertices to length of both chains
		index--; //decrease index by one so it can be used to assign values
		while(!leftChain.isEmpty())
		{
			//start at last index and pop back into array
			hullVertices[index] = leftChain.pop(); 
			index--;
		}
		while(!rightChain.isEmpty())
		{
			hullVertices[index] = rightChain.pop();
			index--;
		}
		time = System.nanoTime() - time1;
	}
	
	
	/**
	 * Construct the right chain of the convex hull.  Starts at lowestPoint and wrap around the 
	 * points counterclockwise.  For every new vertex v of the convex hull, call nextVertex()
	 * to determine the next vertex, which has the smallest polar angle with respect to v.  Stop 
	 * when the highest point is reached.  
	 * 
	 * Use the stack rightChain to carry out the operation.  
	 * 
	 * Ought to be private, but is made public for testing convenience. 
	 */
	public void createRightChain()
	{	
		Point a = lowestPoint; //start at lowest point
		rightChain.push(a); //add it to chain and then start looking for next vertex
		while(a != highestPoint) //stop when we reach highest point
		{
			a = nextVertex(rightChain.peek()); //find nextVertex of the top of stack and add it
			rightChain.push(a);
		}
	}
	
	/**
	 * Construct the left chain of the convex hull.  Starts at highestPoint and continues the 
	 * counterclockwise wrapping.  Stop when lowestPoint is reached.  
	 * 
	 * Use the stack leftChain to carry out the operation. 
	 * 
	 * Ought to be private, but is made public for testing convenience. 
	 */
	public void createLeftChain()
	{	
		Point a = highestPoint; //start at highest point
		leftChain.push(a); 
		while(a != lowestPoint) //stop when we reach the bottom point
		{
			a = nextVertex(leftChain.peek()); //find nextVertex of the top of stack and add it
			leftChain.push(a);
		}
	}
	
	/**
	 * Return the next vertex, which is less than all other points by polar angle with respect
	 * to the current vertex v. When there is a tie, pick the point furthest from v. Comparison 
	 * is done using a PolarAngleComparator object created by the constructor call 
	 * PolarAngleCompartor(v, false).
	 * 
	 * Ought to be private. Made public for testing. 
	 * 
	 * @param v  current vertex 
	 * @return
	 */
	public Point nextVertex(Point v)
	{
		Comparator<Point> w = new PolarAngleComparator(v, false);
		Point nextV = pointsNoDuplicate[0]; //start at first index
		for(int i = 0; i < pointsNoDuplicate.length; i++) //iterate through array
		{
			if(nextV == v)
			{
				nextV = pointsNoDuplicate[i];
			}
			if((pointsNoDuplicate[i] != v))
			{
			if(w.compare(pointsNoDuplicate[i], nextV) == -1)  //if this point compared to the next vertex
			{												 // is neg 1 than it is now the new next vertex
				nextV = pointsNoDuplicate[i];  				 //similar to finding the max or min of an array
			}
			}
		}
		return nextV;
	}
}
