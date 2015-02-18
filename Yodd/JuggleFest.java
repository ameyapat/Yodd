import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class JuggleFest {

	public static void main(String[] args)throws IOException {
		
		BufferedReader obj=new BufferedReader(new FileReader("juggle.txt"));
		BufferedWriter bw=new BufferedWriter(new FileWriter("output.txt"));
		Circuit circuits[]=new Circuit[2000];
		Juggler jugglers[]=new Juggler[12000];
		int i=0,j=0;
		while(true)
		{
			String line=obj.readLine();
			if(line==null)
				break;
			else
			{
				String inp[]=line.split(" ");
				if(inp[0].equalsIgnoreCase("C"))
				{
					
				 	circuits[i]=new Circuit(Integer.parseInt(inp[2].substring(2,inp[2].length())),Integer.parseInt(inp[3].substring(2,inp[3].length())),Integer.parseInt(inp[4].substring(2,inp[4].length())),inp[1]);
				    i++;
				}
				else if(inp[0].equalsIgnoreCase("J"))
				{
					jugglers[j]=new Juggler(Integer.parseInt(inp[2].substring(2,inp[2].length())),Integer.parseInt(inp[3].substring(2,inp[3].length())),Integer.parseInt(inp[4].substring(2,inp[4].length())),inp[1],inp[5]);
					j++;
				}
			}
		}
		// Calculate number of Jugglers per circuit
		
		int Climit=12000/2000;
		for(int k=0;k<12000;k++)
		{
			assign(jugglers[k],circuits,jugglers);
		}
		
		// Display circuit assignments
		for(int k=0;k<2000;k++)
		{
			//System.out.println(circuits[k].getName()+" - "+circuits[k].jugglers.toString());
			//bw.write(circuits[k].getName()+" - "+circuits[k].jugglers.toString()+"\n");
			Juggler juglist[]=new Juggler[Climit];
			juglist=getJugglers(circuits[k],jugglers);
			
			bw.write(circuits[k].getName());
			for(int p=0;p<6;p++)
			{
				bw.write(" "+juglist[p].getName());
				String prefs[]=juglist[p].getPrefs().toArray(new String[0]);
				for(int t=0;t<prefs.length;t++)
				{
				bw.write(" "+getCircuit(prefs[t],circuits).getName());
				bw.write(":"+getScore(getCircuit(prefs[t],circuits),juglist[p]));
				}
				bw.write(",");
			}
			bw.write("\n");
			bw.flush();
			
		}
		System.out.println("Done !"); // Print Done when is output file is generated
		
		
	}
	
	 static Juggler[] getJugglers(Circuit circuit,Juggler jugglers[]) {
	    String jugs[]=circuit.jugglers.keySet().toArray(new String[0]);
	    Juggler jj[]=new Juggler[jugs.length];
	    for(int i=0;i<jugs.length;i++)
	    {
	    	jj[i]=getJuggler(jugs[i],jugglers);
	    }
		return jj;
	}

	static int getScore(Circuit c,Juggler j)
	{
		int cc[]=c.getVals();
		int jj[]=j.getVals();
		
		return (cc[0]*jj[0])+(cc[1]*jj[1])+(cc[2]*jj[2]);
		
	}
	static void assign(Juggler j,Circuit circuits[],Juggler jugglers[])
	{
		int i=0;
		for(i=0;i<10;i++)
		{
		  String circPref=j.getPrefs().get(i);
		  Circuit circ = null;
		  for(int k=0;k<circuits.length;k++)
		  {
			  if(circuits[k].getName().equalsIgnoreCase(circPref))
			  {
				  circ=circuits[k];
				  break;
			  }
		  }
		  
		  if(circ!=null)
		  {
			  if(circ.isFull())
			  {
				  String minJug=circ.getMinJug();
				  Juggler lastJug = null;
				  for(int k=0;k<jugglers.length;k++)
				  {
					  if(jugglers[k].getName().equalsIgnoreCase(minJug))
					  {
						  lastJug=jugglers[k];
						  break;
					  }
				  }
				  
				  if(getScore(circ, lastJug)<getScore(circ,j))
				  {
					  //replace lastJug in circ with juggler j
					  circ.jugglers.remove(lastJug.getName());
					  circ.jugglers.put(j.getName(), getScore(circ,j));
					  assign(lastJug,circuits,jugglers);
					  
				  }
				  else
				  {
					  //since j is not a better fit assign j to next preference
					  continue;
				  }
			  }
			  else
			  {
				  circ.jugglers.put(j.getName(),getScore(circ,j));
				  break;
			  }
		  }
		  
		}
	}
 
	static Circuit getCircuit(String cname,Circuit circuits[])
	{
		for(int i=0;i<circuits.length;i++)
		{
			if(circuits[i].getName().equalsIgnoreCase(cname))
			{
				return circuits[i];
			}
			
		}
		return null;
	}
	
	static Juggler getJuggler(String jname,Juggler jugglers[])
	{
		for(int i=0;i<jugglers.length;i++)
		{
			if(jugglers[i].getName().equalsIgnoreCase(jname))
			{
				return jugglers[i];
			}
			
		}
		return null;
	}

}

class Circuit
{
 private int H;
 private int P;
 private int E;
 private String cname;
 
 HashMap <String,Integer>jugglers=new HashMap<String,Integer>();
 Circuit(int h,int e,int p,String name)
 {
	 this.setVals(h,e,p);
	 this.setName(name);
 }

private void setName(String name) {
	this.cname=name;
	
}

private void setVals(int h2, int e2, int p2) {
	this.H=h2;
	this.P=p2;
	this.E=e2;
	
}

public int[] getVals()
{
	int temp[]=new int[3];
	temp[0]=this.H;
	temp[1]=this.E;
	temp[2]=this.P;
	return temp;
}

public String getName()
{
return this.cname;
}
 
public String getMinJug()
{
	if(jugglers.isEmpty())
	return null;
	else
	{
		int min=Integer.MAX_VALUE;
		
		Iterator <String>itr=jugglers.keySet().iterator();
		String ans=null;
		while(itr.hasNext())
		{
			String temp=itr.next();
			if(jugglers.get(temp)<min)
			{
				min=jugglers.get(temp);
				ans=temp;
			}
		}
		return ans;
	}
}

boolean isFull()
{
	return jugglers.size()>=(12000/2000);
}
}


class Juggler
{
 private int H;
 private int P;
 private int E;
 private String Jname;
 private ArrayList<String> prefs;
 
 Juggler(int h,int e,int p,String name,String preferences)
 {
	 this.setVals(h,e,p);
	 this.setName(name);
	 String tt[]=preferences.split(",");
	 this.prefs=new ArrayList<String>();
	 for(int i=0;i<tt.length;i++)
	 {
		 prefs.add(tt[i]);
	 }
 }

private void setName(String name) {
	this.Jname=name;
	
}

private void setVals(int h2, int e2, int p2) {
	this.H=h2;
	this.P=p2;
	this.E=e2;
	
}

public int[] getVals()
{
	int temp[]=new int[3];
	temp[0]=this.H;
	temp[1]=this.E;
	temp[2]=this.P;
	return temp;
}

public String getName()
{
return this.Jname;
}
 
public ArrayList<String> getPrefs()
{
return this.prefs;	
}
}
