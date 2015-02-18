import java.io.*;
import java.util.HashMap;
public class TrianglePath {

	public static void main(String[] args)throws IOException
	{
		BufferedReader obj=new BufferedReader(new FileReader("triangle.txt"));
	
		int tri_data[][]=new int[101][101];
		tri_data[0][0]=Integer.parseInt(obj.readLine().trim());
		int row_no=1;
		while(true)
		{
			String inp=obj.readLine();
			if(inp==null)
			{
				break;
			}
		 String input_row[]=inp.split(" ");
		for(int i=0;i<input_row.length;i++)
		{
			tri_data[row_no][i]=Integer.parseInt(input_row[i]);
		}
		  	
		  row_no++;
		}
				
		HashMap <String,Integer>hm=new HashMap<String,Integer>();
		System.out.println("Max sum = "+maxSum(tri_data[0][0],0,0,tri_data,hm));
		
		obj.close();   
	}
	static int max(int a,int b)
	{
		return a>=b?a:b;
	}
	
	static int maxSum(int root,int rootRow,int rootIndex,int tri_data[][],HashMap <String,Integer>hm)
	{
		int res=0;
		if(hm.containsKey(rootRow+""+rootIndex))
		return hm.get(rootRow+""+rootIndex);
		
		if(rootRow==tri_data.length-1)
		{
			hm.put(rootRow+""+rootIndex,root+tri_data[rootRow][rootIndex]);
			return root+tri_data[rootRow][rootIndex];
		}
		else
		{
			res=max(maxSum(tri_data[rootRow+1][rootIndex],rootRow+1,rootIndex,tri_data,hm),maxSum(tri_data[rootRow+1][rootIndex+1],rootRow+1,rootIndex+1,tri_data,hm));
			hm.put(rootRow+""+rootIndex,root+res);
		}
		return root+res;
	}

}
