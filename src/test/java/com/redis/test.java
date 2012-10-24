package com.redis;

public class test {

	
	public static void main(String[]args)
    {   //initiaze array2
        int [][] array2 = {{ 0, 4, 6, 3, 7, 5, -1 } ,
        		           { 1, 4, 6, 3, 7, 5, -1 } , 
        		           { 0, 13, 12, -1, 0, 0, 0}, 
        		           { 1, 13, 12, -1, 0, 0, 0}, 
        		           { 0, 5, 5, 5, 5, 5, -1 } };

        //call uncompressBinArray method
        rolate( array2 ,5,7);

    }


    public static void rolate(int[][] array, int x, int y)
    {
    	 int [][] arr = new int[y][x];
    	 
    	
    	 // second iteration
    	 // n = 5
    	 // y = 7
    	int rowup = 0;
    	int rowbb = y -1 ;
    	while(true){ 
    		if (rowup > rowbb) break;
    	for (int n = rowup; n < y -1 ; n++){
    		System.out.println(array[0][n]);
    			if (n+1 == y-1){
    				System.out.println("====");
    				for (int z = 0; z < x -1; z++){
    					System.out.println(array[z][y-1]);
    					if (z+1 == x-1){
    						System.out.println("rowbb ====");
    						for (int c = rowbb ; c > rowup ; --c){
    							System.out.println(array[x-1][c]);
    							if (c -1 == 0){
    								System.out.println("====");
    								for (int e = x -1; e > 0; --e){
    									System.out.println(array[e][0]);
    								}
    								System.out.println("====");
    							}
    						}
    					}
    				}
    			}
    	}
    	rowup++;
    	}
    	
    	
    		
    	
    }
}//end of main program

