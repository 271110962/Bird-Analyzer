
package application;

public class UnionFind {

	public int[] id;
	
	
	public UnionFind(int N)
	{
		
		id = new int[N];
		for(int i = 0; i < N; i++) {
		         id[i]=i;
		         
		}
	}
	
	//public int count()
	//{
	//	return count;
	//}
	
	public boolean connected(int p, int q)
	{
		return find(p) == find(q);
	}


	public int find(int p)
	{
		return id[p];
	}
	
	public void union(int p, int q)
	{
		int pID = find(p);
		int qID = find(q);
		if(pID == qID)
			return;
		
		for(int i = 0; i<id.length;i++)
			if(id[i] == pID) 
				id[i]=qID;

	}
//	public int find2(int p)
//	{
//		return weight[p];
//	}
	
//	public void union2(int p, int q)
//	{
//		int pID = find2(p);
//		int qID = find2(q);
//		if(pID == qID)
//			return;
//	
//		for(int i = 0; i<weight.length;i++)
//			if(id[i] == pID) 
//				id[i]=qID;
//  
//	  
//	    }
}
