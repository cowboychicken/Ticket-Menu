package csii_project_1;

/*
Aaron Im    Project 1  
CS2336.003  
@150030
 */

import java.io.*;
import java.util.*;

public class CSII_Project_1 {
    public static void main(String[] args) throws IOException, EOFException {
        boolean loop = true; 
        
        ArrayList<char[][]> list = new ArrayList<char[][]>();                   //Creates an ArrayList of 2D char arrays
        
        list.add( readAuditorium( 1 ));                                         //loading all three seating arrangements in the begining. 
        list.add( readAuditorium( 2 ));
        list.add( readAuditorium( 3 ));
        
        int[] ticketsSold = new int[3];                                         //index 0-2 corresponds to keep track of A1-A3 total tickets.
       
        do {
            
            Scanner x = new Scanner(System.in);
          
            System.out.println( "\tHello! Please select your auditorium: ");
            System.out.println("\n1) Auditorium 1");
            System.out.println("2) Auditorium 2");
            System.out.println("3) Auditorium 3");
            System.out.println("4) Exit\n");
       
            int num = x.nextInt( ); 
           
            if (num ==4){                                                       //Breaks loop to exit program. 
               loop =false;
               break;
            }
            
            System.out.println("\nHere is the layout for Auditorium " + num + " : ");
            displayAuditorium( list.get(num-1) );
            
            char[][] copy = list.get(num-1);                                    //Making copy of chosen array to make input validation easier. 
                                                                                //Could not get methods to work when refrencing with ArrayList index notation.
            
            System.out.print("\nPlease enter a row number: ");
            int row = 9999; 
            while ( row > copy.length || row <=0 ){                             //Input validation to keep input in range. 
                row = x.nextInt();                                              //If value is greater than zero and less than or equal to range.
                if ( row > copy.length  || row <=0){
                    System.out.println("\nSorry we do not have that many rows. \n");
                }
            }
            
            System.out.print("\nPlease enter a seat number: ");
            int seat = 9999; 
            while ( seat > copy[0].length || seat <=0 ){                        //Input validation to keep input in range. 
                seat = x.nextInt();
                if ( seat > copy[0].length  || seat <=0){
                    System.out.println("\nSorry we do not have that many seats. \n");
                }
            }
            
            System.out.print("\nPlease enter desired quantity of tickets: ");
            int quant = x.nextInt( ); 
            if (checkAvailability (list.get(num-1), row, seat, quant) == true){ //"Ideal" case. If available, reserve and take tally. 
                                reserve( list.get(num-1), row, seat, quant);
                                tixTally(num, ticketsSold, quant);
            }
            
           else { 
                System.out.println("\nSorry, we do not have enough seats to fulfill that order. \nWould you like the next best available in that row?\n");
                char answer = x.next().charAt(0); 
            
                if ( answer == 'Y' || answer == 'y'){
                        if ( getBest (list.get(num-1), row, quant) >= 0 ){  //getBest() returns -1 if none available. 

                            reserve ( list.get(num-1) ,row , ( getBest(list.get(num-1), row, quant)) ,quant);
                            tixTally(num, ticketsSold, quant);      //Reserve resulting best choice tickets and take tally.
                        }
                    else  { 
                        System.out.print( "\nSorry, cannot find " + quant + " tickets in row " + row + ".\n");
                    }
                }
            }  
        } while (loop = true );
        
        close( list.get(0), list.get(1), list.get(2), ticketsSold);
    }   
    
// Read Auditorium
    public static char [][] readAuditorium (int answer) throws FileNotFoundException, IOException{
        
        File file = null;
       
        switch (answer) {
            case 1:
                file = new File ("A1.txt");
                break;
            case 2:
                file = new File ("A2.txt");
                break;
            case 3:
                file = new File ("A3.txt");
                break;
            default:
                break;
        }
        BufferedReader BR =new BufferedReader ( new FileReader ( file)) ;       //Loads appropriate file into this BR object to capture dimensional properties.
        int rowCount =1;
        String y = BR.readLine();                                               //Reads just one line to aqire length and height.
        int rowLength =   y.length();                                           //aquires length of row.
        for ( String x = BR.readLine() ; x != null ; x = BR.readLine() ){       //Scans through document to count number of rows.
                rowCount++;                                                     // initiatw For loop by storing first line, and keep reading next line till nothing is stored. 
        }
        char [][] array = new char[rowCount][rowLength] ;
         
        BufferedReader BR2 =new BufferedReader ( new FileReader ( file)) ; 
        int j =0;                                                               //J keeps count of row count of array within following for loop:
        for ( String x = BR2.readLine() ; x != null ; x = BR2.readLine() ){     // initiatw For loop by storing first line, and keep reading next line till nothing is stored. 
            if ( x != null ){                                                   //Wubba lubba dubdub
                for ( int i  =0; i <= rowLength-1; i++){                        //Parsing through each line char at a time and filling up 2D array.
                    array[j][i] = x.charAt(i) ;                             
                }
            }   
            j++;                                                                //itterate to next row
        }  
        return array;
    }
       
                                                                                //Display auditorium 
    public static void displayAuditorium (char [][] array) {
        int columnLen = array.length -1;                                        //rowLen and columnLen are equal to the max index of their respected dimension. hence (-1)
        int rowLen = array[0].length-1;
        
        System.out.println( "");                                                // Using as a newline character.
         System.out.print( " ");                                                //Print space between margin and first column number.
        for ( int i = 1 ; i <= rowLen +1; i++) {                                // Prints out Header for column numbers
            
            if ( i >= 10){
                int j ;
                for ( j=i ; j >= 10; j= j-10){ }                                // subtracts 10 from copy of 'i' to aquire ones place. 
                System.out.print( j);
            }
            else {
                System.out.print(i);
            }
        }
        System.out.println( "");                                                // Using as a newline character.
        
        for ( int i = 0; i <= columnLen; i++){                                  //Prints out each row starting with number.
            
            System.out.print(i+1);
            for ( int j = 0 ; j <= rowLen; j++){
                
                System.out.print( array[i][j]);
            }
            System.out.println( "");                                            // Using as a newline character.
        }
         System.out.println( "");                                               // Using as a newline character.
    }
     
//reserve method
    public static void reserve( char [][] array, int row, int seat, int quant ){//Reserves seat when row, seat, and quantitity to right is entered. 
        for ( int i =1 ; i <= quant ; i++){
            array [row -1][seat - 2 +i] = '.'; 
        }
        if ( quant < 2 ){                                                       //In the case that buying just one ticket. 
            System.out.println("\nSeat " + seat + " in Row " + row + " was reserved for you!\n");
        }
        else {System.out.println("\nSeat " + seat + " (+" + (quant-1) + ") in Row " + row + " was reserved for you!\n");}   //
    }
    
//Check Availability    
    public static boolean checkAvailability (char[][] array , int row , int seat , int quant ){     //checking proceeding seats to see if they are open. returns True is available
    
        boolean open = true;
        if (((array[0].length) - seat+1) < quant ){
            open = false;
        }
        for ( int i = 0; i < quant && (i < ((array[0].length) -seat)) ; i++){   //Two conditions to keep looping in case that quantitity to check,
                                                                                //  is greater than total seats available to check
            if (array[row-1][seat-1+ i ] == '.'){
                open = false;
            }
        }
        return open;
    }                                                                           // returns true if avaiable 
   
                                                                                //getBest ()    
    public static int getBest ( char[][]array, int row, int quant) {            //return -1 if not possible, otherwise the best seat(not index).
       
        int rowLen = (array[0].length-1);                                       //rowLen is equal to the max index of their respected dimension. hence (-1)
        
        int midPoint = rowLen / 2;
        int leftSide = 9999, rightSide =9999; 
        int answer =0;
        
                                                                                //for left-side of midPoint
        for ( int i = midPoint-1  ; i >= 0 ; i -- ){                            //traverses from middle-1 to begining and checks each char for '#'
            if ( array [row-1][i] == '#'){
                if ( checkAvailability ( array , row, i +1, quant) == true){    
                    leftSide = i;                                               //If found on left side, index is stored. 
                }
            }
        }
    //for right-side
        for( int i = midPoint; i <= rowLen ; i ++){
            if (array[row-1][i] == '#'){
               
                if (checkAvailability ( array , row, i +1, quant) == true){     //Traverse from Middle to end of line. 
                    rightSide = i;                                              //If found, on right side, index is stored in appropriate variable.
                }
            }
        } 
        if ( leftSide > 2000 && rightSide > 2000) {                             // No best choice available. 
            answer = -2;   
        }
        if ( leftSide > 2000 && rightSide < 2000) {                             //option found on right side but not left.
            answer = rightSide;
        }
        if ( leftSide < 2000 && rightSide > 2000) {                             //option found on left side but not right.
            answer = leftSide;
        }  
        if ( leftSide < 2000 && rightSide < 2000 ){
            if( Math.abs ( rightSide - midPoint) < Math.abs(leftSide - midPoint)){    //solutions found on both sides. choose the lowest resulting answer 
                answer = rightSide;                                             // after subtracting the midpoint index and then taking absolute value of both answer.
            }                                                                         
            else {answer = leftSide;}
        }
        return (answer+1);
    }
    
//countUp all remaining seats.    
    public static int[] countUp (char[][] array ){                              //returns an Int[] with first index holding openCount and second index holding reserved count
        
        int columnLen = (array.length -1);                                      //rowLen and columnLen are equal to the max index of their respected dimension. hence (-1)
        int rowLen = (array[0].length-1);
        int openCount =0;
        int reservedCount=0;
        
        for (int row =0; row <= columnLen; row ++ ){
            
            for (int col =0 ; col <= rowLen; col ++ ){
                if (array[row][col] == '#'){
                    openCount++;
                }
                else {
                    reservedCount++;
                }
            }
        }
        int [] answer = new int[2];
        answer[0] = openCount;
        answer[1] = reservedCount;
        
        return answer;
    }
//Ticket Tally    
    public static void tixTally (int num , int[] auditorium, int quant){
        switch (num){                                                           //Based off chosen auditorium, add quanitity tickets to appropriate tally.
            case 1: auditorium[0]= auditorium[0] + quant;
                       break;
            case 2: auditorium[1]= auditorium[1] + quant;
                       break;
            case 3: auditorium[2]= auditorium[2] + quant;
                       break;
        }
    }
    
//closeProgram + print to file
    public static void close (char[][] a1, char[][] a2, char[][] a3, int[] tixCount){
        
        int [] a1Count = countUp(a1);
        int [] a2Count = countUp(a2);                                           //Lots of text formatting
        int [] a3Count = countUp(a3);
        
        int totalOpen = (a1Count[0] + a2Count[0] + a3Count[0]);
        int totalResvd =(a1Count[1] + a2Count[1] + a3Count[1]);
        int totalTix = ( tixCount[0] + tixCount[1]+ tixCount[2]);
        
        System.out.print( "\t\t");
        System.out.print( "Open  ") ;
        System.out.print( "Reserved  ") ;
        System.out.println( "Sales  " );
        
        System.out.print( "____________   ");
        System.out.print( "_____   ") ;
        System.out.print( "_____   ") ;
        System.out.println( "_____  " ) ;
        
        System.out.print( "\nAuditorium 1 \t");
        System.out.print( a1Count[0] + "\t") ;
        System.out.print( a1Count[1] + "\t") ;
        System.out.println( "$" +tixCount[0]*7 ) ;
        
        System.out.print( "Auditorium 2 \t");
        System.out.print( a2Count[0] + "\t") ;
        System.out.print( a2Count[1] + "\t") ;
        System.out.println( "$" +tixCount[1]*7 ) ;
        
        System.out.print( "Auditorium 3 \t");
        System.out.print( a3Count[0] + "\t") ;
        System.out.print( a3Count[1] + "\t") ;
        System.out.println( "$" +tixCount[2]*7 ) ;
        
        System.out.print( "Total  \t\t");
        System.out.print( totalOpen + "\t") ;
        System.out.print( totalResvd + "\t") ;
        System.out.println( "$" + totalTix * 7 + "\n") ;
        
        try {                                                                   
            BufferedWriter out = new BufferedWriter(new FileWriter("A1.txt"));  //Used BW to write to file. 
            for ( int i = 0; i < a1.length; i++){                               //Prints out each row starting with number.
                for ( int j = 0 ; j < a1[0].length; j++){
                out.write(a1[i][j]);
                }
                out.write("\n");
            } 
            out.close();
        } catch (IOException e) {}
        
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("A2.txt"));
            for ( int i = 0; i < a2.length; i++){                               //Prints out each row starting with number.
                for ( int j = 0 ; j < a2[0].length; j++){
                out.write(a2[i][j]);
                }
                out.write("\n");
            } 
            out.close();
        } catch (IOException e) {}
        
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("A3.txt"));
            for ( int i = 0; i < a3.length; i++){                               //Prints out each row starting with number.
                for ( int j = 0 ; j < a3[0].length; j++){
                out.write(a3[i][j]);
                }
                out.write("\n");
            } 
            out.close();
        } catch (IOException e) {}
    }

}
