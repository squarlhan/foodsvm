package knn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;


class ArrayComptr implements Comparator<Integer>
{
    private final Double[] array;

    public ArrayComptr(Double[] array)
    {
        this.array = array;
    }

    public Integer[] createIndexArray()
    {
        Integer[] indexes = new Integer[array.length];
        for (int i = 0; i < array.length; i++)
        {
            indexes[i] = i; 
        }
        return indexes;
    }

    @Override
    public int compare(Integer index1, Integer index2)
    {
               	
    	return array[index1].compareTo(array[index2]);
    }
}

public class MissingImputation implements Serializable
{
	int column_num =14;  //14 , 50
	int row_num = 242;  //242 . 758
	int k;
	int size_tr;
	int size_tc;
	int newsize ; //used for the extra contsant
	 
	double coefficient1[][] ;// new double[NUM_OF_COLUMNS][NUM_OF_ROWS]; //temp array for calculations
	double actual[] ; //new double[NUM_OF_COLUMNS];
	double coefficient2[][];
	double actual2[] ;
	double sol[];
	
	ArrayList<RowData> data_my = new ArrayList<RowData>();
	ArrayList<RowData> data_or = new ArrayList<RowData>();
	ArrayList<RowData> data_ser = new ArrayList<RowData>();
	ArrayList<RowData> test_data = new ArrayList<RowData>();
	
	static ArrayList<Double> errList = new ArrayList<Double>();
	static ArrayList<Double> temperrList = new ArrayList<Double>();

				
	class RowData implements Serializable
	{
	 
	  ArrayList<Double> cellData = new ArrayList<Double> ();	
	  double avg; 
	  Double distance[] = new Double[row_num];
	  Double sorted_distance[] = new Double[row_num];
	  int topk[] = new int[k];
	
	  boolean has_missing()
		{
		  
		  for(double dl: cellData)
			{
				
			  if(dl==1.0E99)
			  {
				return true;  
			  }
			}
		
		  return false;
		}		
	}

	void write()
	{
		 try
	      {
	         FileOutputStream fileOut =new FileOutputStream("orObj.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject( data_my);
	         out.close();
	         fileOut.close();
	         System.out.printf("Serialized data is saved in orObj.ser");
	      }
		 catch(IOException i)
	      {
	          i.printStackTrace();
	      }
		
	}
	
	void readSerObj() //reading the ORIGINAL values into serData
	{
		
		try
	      {
	         FileInputStream fileIn = new FileInputStream("orObj.ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         data_ser = (ArrayList<RowData>) in.readObject();
	         in.close();
	         fileIn.close();
	      }catch(Exception i)
	      {
	         i.printStackTrace();
	         return;
	      }
	}
	
	void read(String addr)
	{
		
		
		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(addr));
			BufferedReader reader = new BufferedReader(ir);
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				String[] lines = line.split("\t");
				RowData rowData = new RowData();
				RowData row2Data = new RowData();

				for(String s : lines){
					rowData.cellData.add(Double.parseDouble(s));
		        	row2Data.cellData.add(Double.parseDouble(s));
				}
				
			    data_my.add(rowData);
			    data_or.add(row2Data);


			}
			ir.close();
			reader.close();
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	//	 System.out.println("\n Number of Row ="+rows);
	 }
	
	void print()
	{
		System.out.println("Printing the data \n");
		
		for(RowData ob:data_my)
		{
			for(Double var: ob.cellData)
			 System.out.print(var+"\t");
				
				System.out.println();	
		}
		
	}
	
	void printOr()
	{
	//	System.out.println("Printing the Original data \n");
		
		for(RowData ob:data_or)
		{
			for(Double var: ob.cellData)
			 System.out.print(var+"\t");
				
				System.out.println();	
		}
		
	}
	
	void calc_Average()
	{
		for(RowData ob:data_my)
		{  
		    double sum=0.0;	
			double count =0;
			
		    for(Double var: ob.cellData)
		    {  
		    	if(var!=1.0E99)
			     {
		    		sum+=var; 
		    		//System.out.print(" "+sum);
			        count++;
			      }
		    //	else 
		    	 // System.out.println("Missing Value");	
		    }	
		    
		    ob.avg =(double)sum/count;
		    
		//    System.out.println(" sum ="+sum+" Non Missing values Count ="+count+" Avg= "+ob.avg);
		//	System.out.println(" sum ="+sum+" Non Missing values Count ="+count+" Avg= "+(new BigDecimal(Double.toString(ob.avg))).toPlainString());	
		    		    
		}
		
		//Put the avg into the missing locations
		for(RowData ob:data_my)
		{  

		    for(Double var: ob.cellData)
		    {  		    
		    	if(var==1.0E99)
			      {
		    		int index = ob.cellData.indexOf(var);
		    		ob.cellData.set(index, ob.avg);
			      }
		    	
		    }			    
		    
		}
				
	}
	
	
	//carefully ignore the missing values in the distance computation
	void calcDistances3()  
	{		
		for(RowData ob1:data_or) //OrData
		{  
	       int idx = data_or.indexOf(ob1);
			
			for(RowData ob2:data_my) //MyData
			{  
				int index = data_my.indexOf(ob2);							
				
			//	if(ob2.hasmissing() == true)
				//{
			//		ob1.dist[index] = Double.POSITIVE_INFINITY;
				//    continue;
			//	}
				
				if(idx == index)
					{
					  ob1.distance[index] =Double.POSITIVE_INFINITY; //we dont consider the distance to itself
					  continue;
					}				
				
				double sum =0;
				
				for(int i =0;i<column_num;i++)
				 {
					if(ob1.cellData.get(i)==1.0E99) continue; //ignore the missing value columns in the dist calc
					
				 	double temp = ob1.cellData.get(i) - ob2.cellData.get(i);
				    temp = temp*temp;
				    
				    sum+=temp;
				 }
				
				sum = Math.sqrt(sum); //Extra CODE
				
				//System.out.print(" index="+index);
				ob1.distance[index] =sum;

			}
			
			ArrayComptr comparator = new ArrayComptr(ob1.distance);	
			Integer[] indexes = comparator.createIndexArray();
			Arrays.sort(indexes, comparator); //indexes will have the indexes of the elements which have been sorted
			
		//	System.out.print(" Top most elements for row "+idx);
			for(int j=0;j<k;j++)  //j=0 will have distance for itself
			{
				ob1.topk[j] = indexes[j];   //topk[] will have the nearest element INDICES and 0th position is ignored as it will be a zero distace(dist to itself)
		//		System.out.print("= "+ob1.dist[ob1.topk[j-1]]);
			}
			
		//	System.out.println();		           
		}
        		
		
		
  }
		
		
		
  double KNN(int row, int col)
	{
		RowData ob = data_or.get(row); //OrData
		
		double number1=0; 
		double den=0;
		
	 	for(int i=0;i<k;i++)
	 	{
	 	  int top = ob.topk[i];
	 	  
	 	  double temp = data_my.get(top).cellData.get(col); //myData
	 	  double weight = (double)1/ob.distance[top];
	 	  
	 	  number1+= weight*temp;
	 	  
	 	  den+=weight;
	 		
	 	}
	 
	  double result =(double)number1/den;	
	  
	  return result;
	 	
	}	
	
	void calcKNN()
	{
		for(RowData ob:data_or)
		{
			for(Double var: ob.cellData)
		    {  		    
		    	if(var==1.0E99)
			      {
			         int col_index = ob.cellData.indexOf(var);
			         int row_index = data_or.indexOf(ob);
			         
			      //   System.out.println("Calc KNN for Row "+rindex+" Col"+cindex);
			         double res= KNN(row_index, col_index);
			         
			         ob.cellData.set(col_index,res);
			        
			      }
		    }
		}
		
	}
	
	
	
 void printTop(int row, int col)
  {
	 RowData ob=  data_or.get(row); 
	 
	 System.out.println("Nearest Sets are .. \n");
	 for(int i =0;i<k;i++)
	  {		 
	 	  int top = ob.topk[i];
	 	  
	 	  RowData ob2 = data_my.get(top);

	 	 System.out.print("["); 
         for(int j=0;j<column_num;j++ )
         {
        	 System.out.print(" "+ob2.cellData.get(j)); 
        	 
         }
         System.out.print(" dist ="+ob.distance[top]+"] \n");
	 	  
	  }	 	 
  }
	
 
 void store_LMS(int id)
  {
	 RowData ob = data_or.get(id);
	
	 coefficient1 = new double[column_num][row_num]; //temp array for calculations
	 actual = new double[column_num];
		
	   //filling the coeff matrix
		 for(int i =0;i<k;i++)
		  {		 
		 	  int top = ob.topk[i];
		 	  
		 	  RowData ob2 = data_my.get(top);
	         
		 	 int m=0;
		 	 for(int j=0;j<column_num;j++ )
	         {
	        	
		 		 if(ob.cellData.get(j)==1.0E99 ) continue;
	        	 
		 		 coefficient1[m++][i] = ob2.cellData.get(j); 
	        	 //System.out.print(" "+ob2.cellData.get(j)); 
	        	 
	          }	//for j        
		 	  
		  }	//for i	  		
		 		
		 int m=0;
		 
		//filling the actual data matrix
		 for(Double var: ob.cellData)
		 {
			 if(var==1.0E99 ) continue; 
			 
			 actual[m++] = var;
		 }
	 
		 size_tr = m; //Very imp
		 size_tc =k;
  }
 
 
  void mat_print()  //Double [][] m1 , Double[] m2
   {
		
	System.out.println("Printing Coeff Matrix \n"); 
	for(int i=0; i < size_tr ;i++)
	{
		//System.out.print("coeff[i] len: "+coeff[i].length);		
		 
		for(int j=0;j<size_tc ;j++)
		 System.out.print(" "+coefficient1[i][j]);
		   
		System.out.println();
	}
	
	System.out.println("Printing Actual Data Matrix \n");
	
	for(int j=0;j<size_tr ;j++)
		  System.out.println(" "+actual[j]);
	  
   }
  
  
  void step_Print()  //Double [][] m1 , Double[] m2
   {
		
	System.out.println("Printing Coeff2 Matrix \n"); 
	
	for(int i=0; i <newsize ;i++)
	{
		//System.out.print("coeff[i] len: "+coeff[i].length);		
		 
		for(int j=0;j<newsize ;j++)
		  System.out.print(" "+coefficient2[i][j]);
		   
		System.out.println();
	}
	
	//printing actual 2
	System.out.println("Printing actual2 array \n"); 
	for(int j=0;j<newsize ;j++)
		  System.out.println(" "+actual2[j]);
	  
   }
  
  
  double colmn_Mul(int col1 , int col2)
  {
	double sum =0;
	
	for(int i=0;i<size_tr ;i++)
	{
		sum+= coefficient1[i][col1]*coefficient1[i][col2]; //multiply one column with another
	}
	
	return sum;	  
  }
  
  double aggregate(int col)
  {
	  double sum=0;
	  
	  for(int i=0; i<size_tr; i++)
		  sum+=coefficient1[i][col];
	  
	  return sum;
  }
  
  void step2_LMS() //This will compute the elements of k*k  matrix
  {
	  newsize = size_tc+1; //to accomodate the extra constant in the matrix Set
	                          //http://www.csee.umbc.edu/~squire/cs455_l4.html
	  coefficient2 = new double[newsize][newsize]; 
		
	  coefficient2[0][0] = 1; // trsize*1;   //trsize *1 is not working so i think its just 1
	  
	for(int p=1; p< newsize ;p++)   //calc of the first row in the target matrix
		coefficient2[0][p] = aggregate(p-1); 
	
	for(int p=1; p< newsize ;p++)  
		coefficient2[p][0] = coefficient2[0][p];	 
	
		  
	  for(int i=1; i <newsize;i++)
		{			 
			for(int j=1;j<newsize ;j++)
			{				  
				coefficient2[i][j] = colmn_Mul(i-1,j-1);
						
			}		
		}  
  }
 
double  mul_Y(int col)
 {
	 double sum=0;
	 
	 for(int i=0;i<size_tr ;i++)
		{
		  sum += coefficient1[i][col]*actual[i];
		}
	 
	 return sum;
 }
 
double getActualSum()
{
 double sum=0;
 
 for(int i=0;i<size_tr ;i++) sum+=actual[i];
 
 return sum;
}

  void step3_LMS() //This will compute the elements of actual2 matrix
  {
	 actual2 = new double[newsize]; 
	  
	 actual2[0] = getActualSum();
	 
	  for(int i=1; i < newsize ;i++) //we need only k elements
		{
		  actual2[i] = mul_Y(i-1);
		}
 
  }
  
 
  double solution_Final(int row, int col)
  {
	 double sum = sol[0]; //sol[0] holds the constant 
	  
		RowData ob = data_or.get(row); //OrData
	 
		for(int i=0;i<k;i++)
	 	{
	 	  int top = ob.topk[i];
	 	  
	 	  double temp = data_my.get(top).cellData.get(col); //myData
	 	
	 	   sum+=sol[i+1]*temp;
	 	  	
	 	}
		
	 return sum; 
  }
  
  void Missing_Fill(int row)
  {
	  RowData ob=  data_or.get(row); 
	  
	  for(Double var: ob.cellData)
	    {  		    
	    	if(var==1.0E99)
		      {
	    		 int cindex = ob.cellData.indexOf(var);
		         int rindex = data_or.indexOf(ob);
		         
		      //System.out.println("Calc LMS for Row "+rindex+" Col"+cindex);
		        		         
		         double res= solution_Final(rindex, cindex);
		         
		         ob.cellData.set(cindex,res);
	    		
		      }
	    	
	    }
  }
  
  // This is very similiar to testfill to calculate LMS values for comparion on diff k values
  
  void results_Filling(int row)
  {
   RowData ob=  data_or.get(row); 
	  
	  for(Double var: ob.cellData)
	    {  		    
		  		  
		   if(var!=1.0E99)
		      {				   
	    	    //int cindex = ob.cellData.indexOf(var);
		        //int rindex = orData.indexOf(ob);		        
		        //System.out.println("Calc LMS for Row "+rindex+" Col"+cindex);		        		         
		        //double res= finalSol(rindex, cindex);		         
		        //System.out.println("Actual Data "+var+" Approx Data "+res);
		        
		     }
		   else 
		    {
			     int cindex = ob.cellData.indexOf(var);
		         int rindex = data_or.indexOf(ob);
		         
		         //System.out.println("Calc LMS for Row "+rindex+" Col"+cindex);
		        		         
		         double res= solution_Final(rindex, cindex);
		         ob.cellData.set(cindex,res);
		         //System.out.println("Approx Data "+res);
			     
		     }	    	
	    } 
  }
  
  void Filling_test(int row)
  {
	  RowData ob=  data_or.get(row); 
	  
	  for(Double var: ob.cellData)
	    {  		    
		  		  
		   if(var!=1.0E99)
		      {	
			   
	    		 int col_index = ob.cellData.indexOf(var);
		         int row_index = data_or.indexOf(ob);		        
		         //System.out.println("Calc LMS for Row "+rindex+" Col"+cindex);		        		         
		         double res= solution_Final(row_index, col_index);		         
		         System.out.println("Actual Data "+var+" Approx Data "+res);
		        
		     }
		   else 
		    {
			     int cindex = ob.cellData.indexOf(var);
		         int rindex = data_or.indexOf(ob);
		         
		         //System.out.println("Calc LMS for Row "+rindex+" Col"+cindex);
		        		         
		         double res= solution_Final(rindex, cindex);
		         
		         System.out.println("Approx Data "+res);
			   
		     }	    	
	    }
	  
  }
  
  void copyRead() //copy to testData variable
  {
	  for(RowData ob:data_or)
		{
		  RowData rowData = new RowData();
		  
			for(Double var: ob.cellData)
		    {  	
			    rowData.cellData.add(var);			    
		    }
		 
		  test_data.add(rowData);	
		}
	  
  }
  
  static double doAverageErr()
  {
   double sum =0;
   int count =0;
   
   for(double d :MissingImputation.temperrList)
   {
	   count++;
	   sum+=d;
   }
   
  // System.out.println("Count ="+count);
   
   double avg =(double)sum/MissingImputation.temperrList.size();
   
   MissingImputation.errList.add(avg); 
   
    return avg;
  }
	
  
  void calcError()
  {
	 double err=0; //denotes LMS error
	 
	  for(RowData ob:test_data)
		{
			for(Double var: ob.cellData)
		    {  		    
		    	if(var==1.0E99)
			      {
			         int cindex = ob.cellData.indexOf(var);
			         int rindex = test_data.indexOf(ob);
			         
			         double original =  data_ser.get(rindex).cellData.get(cindex);
			         
			         //double test =testData.get(rindex).cellData.get(cindex);
			         if(original!=1.0E99)
			         {
			         // System.out.println("Calc KNN for Row "+rindex+" Col"+cindex);
			         
			           double observed =  data_or.get(rindex).cellData.get(cindex); //since after final steps values in OrData are replaced it is treated as observed data
			             
			           double temp = original - observed;
			       //    System.out.println("Original: "+original+" Observed: "+observed+" diff ="+temp);
			           
			           err+= temp*temp;
			         }
			      }
		    }
		}
	  
	  err= Math.sqrt(err);
	 // temperrList.add(err);
	//  System.out.println("\n k= "+k+" Mean Square Error: "+err);
   
  }
  
  void writing_file() 
  {
	  try
	  {
	  FileWriter f1 = new FileWriter("dataresult.txt");
	  BufferedWriter b1 = new BufferedWriter (f1);
	  
	  for(RowData ob:data_or)
		{
		 		  
			for(Double var: ob.cellData)
		    {  	
			    b1.write(String.valueOf(var));			    
			    b1.write("\t");
			    
		    }
		 
			b1.write("\n");
			b1.flush();
		}
	 	  
	  }
	  catch(Exception e) {}
	  
  }
  
	public static void main(String args[])
	 {	
		String addaddr = "C:/Users/install/Desktop/hxs/TCM/hnc/nd/all151.txt";
		estimateK(addaddr); //calls fillResults
		
		
	     //testcase1();
		// testcase2();	//latest testcase
	}
	
	static void estimateK(String addr)
	{
		/* Run the commented code to generate new Serialized original file */
		//QtnV2 qt2  = new QtnV2();
		// qt2.read(); //read the original data
		//qt2.write(); //write it into a file		 		
    	//// now modify the input file to have "known missing values"
				
		//-------------------------------Standard Procedure
		
	//	for(int iter=15; iter<46;iter++)
	//	{
		MissingImputation qt  = new MissingImputation();
		//qt.readSerObj(); //read the serialized object   //reading the ORIGINAL values into serData
		qt.k=9;		//9 for 1st , 38 is the limit for 2nd dataset
		qt.read(addr);
	
		//qt.copyRead(); //copy orData to a testData
		qt.calc_Average();
		qt.calcDistances3();
		
		// System.out.println("orsize ="+qt.orData.size());
		//qt.printOr();
		
		for(int row=0; row< qt.row_num ;row++)
		{	    	 		  
		    qt.store_LMS(row);
		    //qt.printMat();
		    qt.step2_LMS();
		    
		    qt.step3_LMS();
		    
		   // qt.printStep(); 
		   
		    RealMatrix coefficients = new Array2DRowRealMatrix(qt.coefficient2); 	 
		   
		    DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
		    
		    RealVector constants = new ArrayRealVector(qt.actual2, false);
		    RealVector solution = solver.solve(constants);
		    
		    qt.sol = new double[qt.newsize];
		    
		    for(int i=0;i< qt.k+1; i++) //k+1 for the extra constant
		    {		    	
		    	qt.sol[i] = solution.getEntry(i);
		    	//System.out.println("Solution "+i+": "+solution.getEntry(i));		    	    
		    }
		    
		    qt.results_Filling(row);		    		
		}
		
		// qt.printOr();
		
		// qt.calcError();
		 qt.printOr();
		//  qt.fileWrite();
		//}	
		//System.out.println(" Row 0 col x  = " +qt.serData.get(0).cellData.get(13));
		
	}
	
	static void testcase2(String addr)
	{
		MissingImputation qt  = new MissingImputation();
		qt.k= 10	;		//46 is the limit for 2nd dataset
		qt.read(addr);
		qt.calc_Average();
		qt.calcDistances3(); 	
		  
		    qt.store_LMS(0);
		   // qt.printMat();
		    qt.step2_LMS();
		    
		    qt.step3_LMS();
		    
		 //   qt.printStep(); 
		   
		    RealMatrix coefficients = new Array2DRowRealMatrix(qt.coefficient2); 	 
		   
		    DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
		    
		    RealVector constants = new ArrayRealVector(qt.actual2, false);
		    RealVector solution = solver.solve(constants);
		    
		    qt.sol = new double[qt.newsize];
		    
		    for(int i=0;i< qt.k+1; i++) //k+1 for the extra constant
		    {		    	
		    	qt.sol[i] = solution.getEntry(i);
		    	System.out.println("Solution "+i+": "+solution.getEntry(i));		    	    
		    }
		    
		    qt.Filling_test(0); //danger
		    
	}
	
	static void testcase1(String addr)
	{
		MissingImputation qt  = new MissingImputation();
        qt.k=3; //max 12 // matching 4, 2
		  qt.read(addr);
		   
		  // double y =qt.orData.get(0).cellData.get(1);
		   //System.out.println("Original value: "+y);
		   
		  		
	     //qt.print();
		
		    qt.calc_Average();
		
		   //qt.print();					    
	    
		  //  System.out.println("my data len ="+ qt.myData.size());
		    
		  for(int row=0; row<758 ;row++ )
		  {
		    qt.calcDistances3(); 	
		  
		    qt.store_LMS(row);
		    qt.mat_print();
		    qt.step2_LMS();
		    
		    qt.step3_LMS();
		    
		    qt.step_Print(); 
		   
		    RealMatrix coefficients = new Array2DRowRealMatrix(qt.coefficient2); 	 
		   
		    DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
		    
		    RealVector constants = new ArrayRealVector(qt.actual2, false);
		    RealVector solution = solver.solve(constants);
		    
		    qt.sol = new double[qt.newsize];
		    
		    for(int i=0;i< qt.k+1; i++) //k+1 for the extra constant
		    {		    	
		    	qt.sol[i] = solution.getEntry(i);
		    	System.out.println("Solution "+i+": "+solution.getEntry(i));		    	    
		    }
		    
		    qt.Missing_Fill(row);
		    
		    // qt.calcKNN();
		  		  
		    //	qt.printTop(0,1); 	 
		  //  qt.printOr();  
		    
		  }   
		
	}
}

 


