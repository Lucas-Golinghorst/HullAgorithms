package edu.iastate.cs228.hw4;

import java.util.Arrays;
import java.util.Comparator;

/**
 * This class sorts an array of Point objects using a provided Comparator.  You may 
 * modify your implementation of quicksort from Project 2.  
 */

/**
 * @author Lucas Golinghorst
 */

public class QuickSortPoints
{
	private Point[] points;  	// Array of points to be sorted.
	
	/**
	 * Constructor takes an array of Point objects. 
	 * 
	 * @param pts
	 */
	QuickSortPoints(Point[] pts)
	{
		points = new Point[pts.length];
		for(int i = 0; i < pts.length; i++)
		{
			points[i] = pts[i];
		}
	}
	
	/**
	 * Copy the sorted array to pts[]. 
	 * 
	 * @param pts  array to copy onto
	 */
	void getSortedPoints(Point[] pts)
	{	
		for(int i = 0; i < points.length; i++)
		{
			pts[i] = points[i];
		}
	}

	/**
	 * Perform quicksort on the array points[] with a supplied comparator. 
	 * 
	 * @param comp
	 */
	public void quickSort(Comparator<Point> comp)
	{
		 if (points == null || points.length == 0)
		 {
             return;
		 }
		 quickSortRec(0, points.length - 1, comp);
	}
	
	/**
	 * Operates on the subarray of points[] with indices between first and last. 
	 * 
	 * @param first  starting index of the subarray
	 * @param last   ending index of the subarray
	 */
	private void quickSortRec(int first, int last, Comparator<Point> comp)
	{
		 if (first >= last) 
	        {
	        	return;
	       	}
	        int p = partition(first, last, comp);
	        quickSortRec(first, p - 1, comp);
	        quickSortRec(p + 1, last, comp); 
		
		/*
		int i = first;
		int j = last;
        Point pivot = points[first + (last-first)/2];
        while (i <= j) 
        {
                while (comp.compare(points[i], pivot) < 0)
                {
                        i++;
                }
                while (comp.compare(points[j], pivot) > 0)
                {
                        j--;
                }
                if (i <= j) {
                        swap(i, j);
                        i++;
                        j--;
                }
        }
        if (first < j)
        {
                quickSortRec(first, j, comp);
        }
        if (i < last)
        {
                quickSortRec(i, last, comp);
        }
        */		 
	}
	
	protected void swap(int i, int j)
	{
		Point temp; //created temporary point to do swap technique
		temp = points[i];
		points[i] = points[j];
		points[j] = temp;
	}
	
	/**
	 * Operates on the subarray of points[] with indices between first and last.
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	
	private int partition(int first, int last, Comparator<Point> comp)
	{
		Point pivot = points[last];
        int i = first - 1;
        for(int j = first; j < last; j++)
        {
            if(comp.compare(points[j], pivot) <= 0)
            {
                      i++;
                      swap(i, j);
            }
        }
        swap(i+1, last);
        return i + 1;  
	}
}


