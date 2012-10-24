package com.redis;

import java.util.ArrayList;
import java.util.List;

public class BinarySearch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Integer> orderList = new ArrayList<Integer>();
		orderList.add(12);
		orderList.add(13);
		orderList.add(14);
		orderList.add(23);
		orderList.add(44);
		orderList.add(46);
		orderList.add(76);
		orderList.add(88);
		orderList.add(98);
		orderList.add(100);
		orderList.add(101);
		int out = search(orderList,  0, 99,  0, 11);
		System.out.println("===================");
		System.out.println(out);
		

	}
	
	public static int search (List<Integer> list , int count, int k, int low, int high){
		System.out.println("===================" +count);
		count++;
		System.out.println("low = "+low);
		System.out.println("high = "+high);
		int mid = (low + high) / 2;
		System.out.println("mid is: "+mid);
		System.out.println("value is: "+list.get(mid));
		// base cases
		if (low > high){
			System.out.println("not found");
			return -1;
		} 
		else if (list.get(mid) == k){
			System.out.println("mid == k");
			return mid;
		}
		else if (list.get(mid) < k){
			System.out.println("mid list < k");
			return search (list, count,  k, mid+1, high);
		} else{
			System.out.println("mid list > k");
			return search (list, count, k, low, mid - 1);
		}
	}

}
