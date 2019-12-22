import java.util.Random;

import javafx.util.Pair;
import java.lang.Math;


public class selectProblems
{

  public Pair<Integer, Integer> selectRandQuickSort(int [] array, int k)
  {
	  int[] counter={0};
  	  selectProblems.randQuickSort(array, 0, array.length-1,counter); //calling the recursion func
  	 
  	 return new Pair<Integer, Integer>(array[k-1],counter[0]);
  }
  
  public Pair<Integer, Integer> selectInsertionSort(int [] array, int k)
  {
	  // total complexity: O(n^2)
	  int counter=0;
	  for (int i=1; i<array.length;i++) { // O(n)
		  int j=i;
		  while (j>0) { // O(n)
			  counter++;
			  if (array[j-1]>array[j]) {
				  int temp=array[j-1];
				  array[j-1]=array[j];
				  array[j]=temp;
				  j=j-1; 
			  } else {
				  break;
			  }
			  
		  }
	  }
    return new Pair<Integer, Integer>(array[k-1],counter);
  }
  

  public Pair<Integer, Integer> selectHeap(int [] array, int k)
  {
	  MinHeap m= new MinHeap(array);//O(n)
	  int comparisons= m.getComparisonsOfConstruct(); 
	  Pair<MinHeap.HeapNode, Integer> p= new Pair<MinHeap.HeapNode,Integer>(null,-1) ;
	  for(int i =0;i<k;i++) {
		  p =m.deleteMin(); //O(logn)
		  comparisons+=p.getValue();
	  }
	  return new Pair<Integer,Integer>(p.getKey().getKey(),comparisons);
  }
  

  public Pair<Integer, Integer> selectDoubleHeap(int [] array, int k)
  {
	  MinHeap m= new MinHeap(array); //O(n)
	  int comparisons= m.getComparisonsOfConstruct(); 
	  MinHeap kheap= new MinHeap(array.length); //O(1) --> empty heap
	  comparisons+= kheap.insert(m.getRoot().getKey(), m.getRoot()); //O(1) --> mo comparisons needed
	  Pair<MinHeap.HeapNode,Integer> p= new Pair<MinHeap.HeapNode,Integer>(kheap.getRoot(),0) ;
	  for(int i=1;i<=k;i++) {
		  p= kheap.deleteMin(); //O(logk)
		  comparisons+= p.getValue();
		  comparisons+= kheap.insert(m.left(p.getKey().getInfo().getPos()).getKey(), m.left(p.getKey().getInfo().getPos())); //O(logk)
		  comparisons+= kheap.insert(m.right(p.getKey().getInfo().getPos()).getKey(), m.right(p.getKey().getInfo().getPos())); //O(logk)
	  }
	  return new Pair<Integer, Integer>(p.getKey().getKey(),comparisons); 
  }
  
  public Pair<Integer, Integer> randQuickSelect(int [] array, int k)
  {
	  int[] counter={0};
	  int x=selectProblems.randomizedQuickSelect(array, 0, array.length-1,k,counter); // calling the recursion func
	  return new Pair<Integer, Integer>(x,counter[0]);
  }
  
  public Pair<Integer, Integer> medOfMedQuickSelect(int [] array, int k)
  {
	  int[] counter={0};
	  int x=selectProblems.deterministicQuickSelect(array, 0, array.length-1,k,counter); // calling to the recursive func
	  return new Pair<Integer, Integer>(x,counter[0]); 
  }
  
  
  /**
  * Lemuto's partition
  * with additional argument "pivot" - which is the pivot index that will be used by the method
  * this method changes the input array (inplace method)
  * return --> Pair<index of pivot in the ordered array, number of comparisons>
  */
  
  public static Pair<Integer, Integer> lemutoPartition(int [] array,int l,int r, int pivot){
	  int temp=array[r];
	  array[r]=array[pivot];
	  array[pivot]=temp;
	  int numOfComparisons=0;
	  int i=l-1;
	  for (int j= l;j<r;j++) { // O(n)
		  numOfComparisons+=1;
		  if(array[j]<array[r]) {
			  i+=1;
			  temp=array[i];
			  array[i]=array[j];
			  array[j]=temp;
		  }
	  }
	  temp = array[r];
	  array[r]=array[i+1];
	  array[i+1]=temp;
	  return new Pair<>(i+1,numOfComparisons);
  }

  /**
  the whole static recursive functions for (selectRandQuickSort,
   randQuickSelect, medOfMedQuickSelect)
  */
 public static void randQuickSort (int[] array, int l, int r,int[] counter) {
		// O(n^2), O(nlogn) average case
		if (l<r) {
			Random rand=new Random();
			int pivot=rand.nextInt(r-l+1);
			Pair<Integer,Integer> p=lemutoPartition(array,l,r,l+pivot); //O(n)
			counter[0]+=p.getValue();
			randQuickSort (array, l, p.getKey()-1,counter); // O(n) in WC, O(logn) average case
			randQuickSort (array, p.getKey()+1, r,counter); // O(n) in WC, O(logn) average case
		}
	}
	public static int randomizedQuickSelect(int[] array, int l, int r,int k, int[] counter) {
		// O(n^2) in the WC which pivot is always minimum/maximum
		// O(nlogn) in average case
		if (l<r) {
			Random rand=new Random();
			int pivot=rand.nextInt(r-l+1);
			Pair<Integer,Integer> p=lemutoPartition(array,l,r,l+pivot); // O(n)
			counter[0]+=p.getValue();
			if (p.getKey()>(k-1)) {
				randomizedQuickSelect(array, l, p.getKey()-1,k,counter); // O(n) in WC, O(logn) average case
			}
			else if (p.getKey()<(k-1)) {
				randomizedQuickSelect(array, p.getKey()+1, r,k,counter); // O(n) in WC, O(logn) average case
			}
			return array[k-1];
		}
		return array[k-1];
	}
	
	public static int deterministicQuickSelect (int[] array, int l, int r,int k, int[] counter) {
		// complexity in WC: O(n)
		
		// allocating the right length for the medians array - O(1)
		if (l<r) {
			int[] arrayOfMed=null;
			if ((r-l+1)%5==0) {
				arrayOfMed=new int[(r-l+1)/5];
			} else {
				arrayOfMed=new int[((r-l+1)/5)+1];
			}
			
		// filling array of medians with medians of each five objects - O(n)
			int fiveCounter=0;
			for (int i=l;i<r+1;i++) { // O(n)
				fiveCounter++;
				if (fiveCounter==5) {
					int[] tempArray=new int[5];
					tempArray[0]=array[i];tempArray[1]=array[i-1];tempArray[2]=array[i-2];
					tempArray[3]=array[i-3];tempArray[4]=array[i-4];
					selectProblems prob=new selectProblems();
					Pair<Integer,Integer> result=prob.selectInsertionSort(tempArray, 3); // O(1)- array of constant "5"
					int med=result.getKey();
					counter[0]+=result.getValue(); //updating # of comparisons
					arrayOfMed[(i-l)/5]=med;
					fiveCounter=0;
				}
			}
		//dealing with the case which array.lenght isn't divisible by 5 - O(1)
			if ((r-l+1)%5!=0) {
				int res=(r-l+1)%5;
				int[] tempArray=new int[res];
				for (int i=0;i<res;i++) { // looping at most 4 times - O(1)
					tempArray[i]=array[r-i]; 
				}
				selectProblems prob=new selectProblems();
				Pair<Integer,Integer> result=prob.selectInsertionSort(tempArray, (res+1)/2); // O(1)
				int med=result.getKey();
				counter[0]+=result.getValue(); //updating # of comparisons
				arrayOfMed[arrayOfMed.length-1]=med; // adding the last object to the array!
			}
		
		// finding "median of medians" by recursive call!
			int pivotValue=0;
			if (arrayOfMed.length==1) { //- we've found the "median of medians"
				pivotValue=arrayOfMed[0];
			} else {
				pivotValue=deterministicQuickSelect(arrayOfMed, 0, arrayOfMed.length-1,(arrayOfMed.length+1)/2,counter); //T(n/5)
			}
			
		// looking for the index of "median of medians" in the original array - O(n)
			int pivotIndex=-1; // default value
			for (int i=l;i<r+1;i++) {
				if (array[i]==pivotValue) {
					pivotIndex=i;
				}
			}
	
		// implementing partition with "median of medians" as deterministic pivot
			Pair<Integer,Integer> p=lemutoPartition(array,l,r,pivotIndex); // O(n)
			counter[0]+=p.getValue();
			if (p.getKey()>(k-1)) {
				deterministicQuickSelect(array, l, p.getKey()-1,k,counter); //T(3n/4) in WC
			}
			else if (p.getKey()<(k-1)) {
				deterministicQuickSelect(array, p.getKey()+1, r,k,counter);//T(3n/4) in WC
			}
			return array[k-1];
		}
		return array[k-1];
	}

	  /**
	  * "MinHeap" class and its internal class "HeapNode" 
	  * used for (selectHeap, selectDoubleHeap)
	  */
	public class MinHeap {
		
		public int comparisonsOfConstruct=0; // number of comparisons that needed for constructing the heap
		private HeapNode[] heap; // an array of HeapNodes that represent the heap
		private int heapSize=0; //number of HeapNodes in the Heap
		
		//notice--> the "pos" field of each HeapNode is not reliable, this implement does not support updating this field by dynamic operations 
		
		// construct an empty MinHeap in size 0
		// will used only in DoubleHeap
		public MinHeap(int length) {
			heap= new HeapNode[length];
		}
		
		//construct a MinHeap from given array (it will be full Heap)
		public MinHeap(int [] array) {
			heap= new HeapNode[array.length];
			heapSize=array.length;
			for (int i=0;i<array.length;i++) {
				heap[i]=new HeapNode(array[i],null,0);
			}
			//now making heapifyDown for all internal nodes
			for(int i = (int)(Math.floor((double)heapSize/2)-1);i>=0;i--) {
				comparisonsOfConstruct+=heapifyDown(i);
			  }
			//needed to be correct after heapifyDown
			for (int i=0;i<array.length;i++) {
				heap[i].pos=i;
			}
		}
		
		//returns the number of comparisons that needed for constructing the heap
		public int getComparisonsOfConstruct() {
			return comparisonsOfConstruct;
		}
		
		// returns pointer for the parent of HeapNode in position "index"
		// if there is no parent returns --> null
		public HeapNode parent(int index) {
			if (index== 0) {
				return null;
			}
			return heap[(int)Math.floor((double)(index-1)/2)];
		}
		
		// returns pointer for the left child of HeapNode in position "index"
		// if there is no left child returns --> "virtual" HeapNode
		public HeapNode left(int index) {
			if ((index*2+1)>= heapSize) {
				return new HeapNode();
			}
			return heap[(2*index)+1];
		}
		
		// returns pointer for the right child of HeapNode in position "index"
		// if there is no right child returns --> "virtual" HeapNode
		public HeapNode right(int index) {
			if (((index*2)+2)>= heapSize) {
				return new HeapNode();
			}
			return heap[(2*index)+2];
		}
		
		//making heapifyDown operation on HeapNode in position "i"
		//returns the number of comparisons
		public int heapifyDown(int i) {
			int numOfComparisons= 0;
			  HeapNode current=heap[i];
			  while(((2*i)+1)<heapSize ) {
				  numOfComparisons+=1;
				  if(current.key>left(i).key) {
					  if(((2*i)+2)<heapSize) {
						  numOfComparisons+=1;
						  if(left(i).key< right(i).key) {
							  heap[i]=left(i);
							  heap[(2*i)+1]=current;
							  i=(2*i)+1;
						  }
						  else {
							  heap[i]=right(i);
							  heap[(2*i)+2]=current;
							  i=(i*2)+2;
						  }
					  }
					  else {
						  heap[i]=left(i);
						  heap[(2*i)+1]=current;
						  i=(2*i)+1;
					  }
				  }
				  else {
					  if(((2*i)+2)<heapSize) {
						  numOfComparisons+=1;
						  if(current.key>right(i).key) {
							  heap[i]=right(i);
							  heap[(2*i)+2]=current;
							  i=(i*2)+2;
						  }
						  else {
							  return numOfComparisons; //when current is not big than the sons
						  }
					  }
					  else {
						  return numOfComparisons; //when current is not big the left son and there is no right son
					  }
				  }
				  
			 }
			  //out from loop
			  return numOfComparisons;
		}
		
		//making heapifyUp operation on HeapNode in position "i"
		//returns the number of comparisons
		public int heapifyUp(int i) {
			int numOfComparisons= 0;
			  HeapNode current=heap[i];
			  while(i!=0) {
				  numOfComparisons+=1;
				  if (current.key < parent(i).key) {
					  heap[i]=heap[(int)Math.floor((double)(i-1)/2)];
					  heap[(int)Math.floor((double)(i-1)/2)]=current;
					  i=(int)Math.floor((double)(i-1)/2);
				  }
				  else {
					  return numOfComparisons;
				  }
			  }
			  return numOfComparisons;
		}
		
		// pre--> heapSize>0
		// returns Pair<minimun of heap, number of comparisons after the deletion(by heapifyDown)
		public Pair<HeapNode,Integer> deleteMin() {
			HeapNode min=heap[0];
			heap[0]=heap[heapSize-1];
			heap[heapSize-1]=new HeapNode();
			heapSize--;
			return new Pair<>(min,heapifyDown(0));
	   }
		
		//returns the root of the heap
		public HeapNode getRoot() {
			return heap[0];
		}
		
		// if key=-1 --> nothing is inserted
		// returns the number of comparisons (due to heapifyUp)
		// this method will be used only by DoubleHeap
		public int insert(int key,HeapNode info) {
			if(key==-1) {
				return 0;
			}
			heap[heapSize]=new HeapNode(key,info,heapSize);
			heapSize++;
			return heapifyUp(heapSize-1);
		}
		
		//returns a String which represent the heap
		public String toString(){
			String result="[";
			for (int i=0;i<heapSize-1;i++) {
				result+= " "+heap[i].key+" ,";
			}
			result+= " "+heap[heapSize-1].key+" ]";
			return result;
		}
		
		// an element in the MinHeap will be a HeapNode
	public class HeapNode{
		private int key=-1; // the key of the node
		private HeapNode info=null; // info is a pointer for other HeapNode (will update only in DoubleHeap
		private int pos=-1; // the actual index of the HeapNode in his Heap
		
		// construct a "virtual" HeapNode
		public HeapNode() {
		}
		
		// construct a "real" HeapNode
		public HeapNode(int key,HeapNode info,int pos ) {
			this.key=key;
			this.info= info;
			this.pos=pos;
		}
		
		public int getKey() {
			return key;
		}
		
		public HeapNode getInfo() {
			return info;
		}
		
		public int getPos() {
			return pos;
		}
		
		//returns a String which represent the heap
		public String toString(){
			return "NODE["+key+", "+info.key+", "+pos+"]";
		}
	}
	}

}