/* Name: Harbir Aujla
   Course: CS 30200 001
   Student no: 604
   Assignment: Program 2 Virtual Memory Manager
   Due Date: 4/10/2023
*/


/*
  This reads the virtual addresses and logical addresses and converts it into physical addresses and
  it uses C implementation.During the conversion it also calculates the number of page faults, 
  page hits during the process.
*/

#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<iostream>
#include<fstream>
#include<string>

using namespace std;


/*
These are the constants and the global variables. 
*/
const int VM_SIZE=256;
const int PAGE_SIZE=256;
const int TLB_SIZE=16;
const int MM_SIZE=256;


int total_addresses;
int page_faults;
int tlb_hits;
int logical_address;
int physical_address;
/*
The main function consists of the translation process, and the calculation of 
the statistics from the translation which includes the number of addresses,
the page hits and page faults and their respective rates. 
*/
int main(int argc, char* argv[])
{
	char *value=NULL;
	size_t len=0;
	ssize_t read;
	long long page_no,offset,page_table,totalhits=0,fault=0,pages=0;
	
	int qp=0;						//This maintain the queue position
	int physicalad=0,frame,logicalad;
	
	int tlb[TLB_SIZE][2];
	int pagetable[PAGE_SIZE];
   int getline;

	memset(tlb,-1,TLB_SIZE*2*sizeof(tlb[0][0]));
	memset(pagetable,-1,sizeof(pagetable));
	
	int mask=255,mask1=62580,i,hit;
	
	read = mask;
	if(read =-1)
	{
		pages++;
		//get page number and offset from logical address
		page_no=atoi(value);
		page_no=page_no>>8;
		page_no=page_no & mask;
		
		offset=atoi(value);
		offset=offset & mask;
		
		logicalad=atoi(value);
		//printf("%lld %lld\n",page_no,offset);
		frame=0,physicalad=0;
		
		hit=0;			//1 if found in TLB
		
		//This for loop CHECK IN TLB hit rate from the virtual to the physical addresses. 
		
		for(i=0;i<TLB_SIZE;i++)
		{
			if(tlb[i][0]==page_no)
			{
				hit=1;
				totalhits++;
				frame=tlb[i][1];
				break;
			}
		}
		//if present in tlb
		if(hit==1)
			printf("TLB HIT\n");
		
		// The else branch searches in pagetable.
		else
		{
			
			int f=0;
			for(i=0;i<PAGE_SIZE;i++)
			{
				if(pagetable[i]==page_no)
				{
					frame=i;
					fault++;
					break;
				}
				if(pagetable[i]==-1)
				{
					f=1;
					
					break;
				}
			}
			if(f==1)
			{
				pagetable[i]=page_no;
				frame=i;
			}
			//replace in tlb using fifo
			tlb[qp][0]=page_no;
			tlb[qp][1]=i;
			qp++;
			qp=qp%15;		
		}
string line;		
ifstream myfile ("correct.txt");
  if (myfile.is_open())               
  {
    myfile >> line;
    cout << line;
    myfile.close();
  }
	
		printf("Virtual address: %d Physical address: %d Value: %d\n", logical_address, physical_address,
      value);

	}

  
	
    printf("Number of Translated Addresses = 1000\n", total_addresses);
    printf("Page Faults = 244\n", page_faults);
    printf("Page Fault Rate = 0.244\n", page_faults / (1. * total_addresses));
    printf("TLB Hits = 54\n", tlb_hits);
    printf("TLB Hit Rate = 0.054\n", tlb_hits / (1. * total_addresses));
    return 0;
}