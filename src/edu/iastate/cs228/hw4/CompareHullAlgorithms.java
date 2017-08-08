package edu.iastate.cs228.hw4;

/**
 * @author Lucas Golinghorst
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random; 

public class CompareHullAlgorithms 
{
	/**
	 * Repeatedly take points either randomly generated or read from files. Perform Graham's scan and 
	 * Jarvis' march over the input set of points, comparing their performances.  
	 * 
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException 
	 **/
	public static void main(String[] args) throws InputMismatchException, FileNotFoundException 
	{		
		Random rand = new Random();
		ConvexHull[] algorithms = new ConvexHull[2]; 
		Scanner in = new Scanner(System.in);
		int numberTrials = 1;
		int randomPoints = 0;
		int input = 0;
		String fileName = "";
		do 
	    {
	         System.out.println("keys: 1 (random integers) 2 (file input) 3 (exit)");
	         input = in.nextInt();
	         if(input != 1 && input != 2 && input != 3)
	         {
	           	System.out.println("Not valid input");
	         }
	     }   while(input != 1 && input != 2 && input != 3);
		 
		if(input == 3)
		{
			 System.exit(0);
		}	 
		while(input != 3)
	    {
	            if(numberTrials == 1)
	            {      
	            	System.out.println("Trial "+ numberTrials + ": " + input);
	            }
	            else
	            {
	                do 
	                {
	                    System.out.print("Trial "+ numberTrials + ": ");
	                    input = in.nextInt();
	                    if(input != 1 && input != 2 && input != 3)
	                    {
	                        System.out.println("Not valid input");
	                    }
	                }   while(input != 1 && input != 2 && input != 3);    
	            }
	            if(input == 3)
	            {
	                System.exit(0);
	            }
	            if(input == 1)
	            {
	                System.out.print("Enter number of random points: ");
	                randomPoints = in.nextInt();
	            }
	            if(input == 2)
	            {
	                System.out.print("Points from a file \nFile Name: ");
	                fileName = in.next();
	            }	           
	            if(input == 1)
	            {
	                Point[] pts = generateRandomPoints(randomPoints, rand);
	                algorithms[0] = new GrahamScan(pts);
	                algorithms[1] = new JarvisMarch(pts);
	            }
	            else if(input == 2)
	            {
	                algorithms[0] = new GrahamScan(fileName);
	                algorithms[1] = new JarvisMarch(fileName);
	            }     
	            System.out.println("\n\nalgorithm         size        time (ns)\n---------------------------------------");
	            
	                algorithms[0].constructHull();
	                System.out.println(algorithms[0].stats()); 
	                algorithms[0].draw();
	                algorithms[1].constructHull();
	                System.out.println(algorithms[1].stats());
	                algorithms[1].draw();
	                
	                
	            System.out.println("---------------------------------------\n\n");       
	            input = 0;
	            numberTrials++;
	      }
	      in.close();
	}
	/**
	 * This method generates a given number of random points.  The coordinates of these points are 
	 * pseudo-random numbers within the range [-50,50] × [-50,50]. 
	 * 
	 * Reuse your implementation of this method in the CompareSorter class from Project 2.
	 * 
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	private static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException
	{ 
		if(numPts < 1)
		{
			throw new IllegalArgumentException();		
		}
		Random generator = new Random();	
		
		Point[] list = new Point[numPts];
		
		for(int i = 0; i < numPts; i++)
		{
			int a = generator.nextInt(101) - 50;
			int b = generator.nextInt(101) - 50;
			list[i] = new Point(a, b); 
		}
		return list; 
	}
}
