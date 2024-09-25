/* Name: Harbir Aujla
   Course: CS 30200 001
   Student no: 604
   Assignment: Program 2 Virtual Memory Manager
   Due Date: 4/10/2023
*/



#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <fstream>
#include <stdlib.h>
#include <string.h>





#define TLB_SIZE 16
#define PAGES 256
#define PAGE_MASK 255
#define PAGE_SIZE 256
#define OFFSET_BITS 8
#define OFFSET_MASK 255
#define MEMORY_SIZE PAGES * PAGE_SIZE


#define BUFFER_SIZE 10




struct tlbEntry
{
   unsigned char logical;
   unsigned char physical;
};


struct tlbEntry tlb[TLB_SIZE];

int tlbindex = 0;

int pagetable[PAGES];

signed char *backing;
int mmap=0;
int logical_page = 0;
int physical_page = 0;

int max(int a, int b)
{
   if( a > b)
      return a;
   else
      return b;
}

int search_tlb( unsigned char logical_page)
{
   unsigned int logical_page=0;
   int i;
   for ( int i = max((tlbindex - TLB_SIZE), 0); i < tlbindex; i++)
   {
        struct tlbEntry * entry = &tlb[i % TLB_SIZE];

        if( entry -> logical == logical_page)
        {
            return entry -> physical;
        }
  
        return -1;
   }
}



//int add_to_tlb(unsigned char logical, unsigned char physical)
{
   unsigned char logical;
   unsigned char physical;
     struct tlbEntry * entry = &tlb[tlbindex % TLB_SIZE];

     tlbindex++;
     entry -> logical = logical;
     entry -> physical = physical;
   
   return physical;
}


int main(__argc)
{

     if (argc != 3)
   {
      fprintf(stderr, "Usage ./virtmem backingstore input\n");
      exit(1);
   }
   

   const char * backing_filename = argv[1];
   int backing_fd = open(backing_filename, O_RDONLY);
   backing = mmap(0, MEMORY_SIZE,  backing_fd, 0);

   const char * input_filename = argv[2];
   FILE *input_fp = fopen("addresses.txt", "r");


   int i;
   for( i = 0; i < PAGES; i++)
   {
      pagetable[i] = -1;
   }


   int total_addresses = 0;
   int tlb_hits = 0;
   int page_faults = 0;


   unsigned char freePage = 0;
   const char buffer = 0;



   while (fgets(buffer,  BUFFER_SIZE, input_fp) != NULL)
   {
      total_addresses++;
      int logical_addresses = atoi(buffer);
      int offset = logical_addresses & OFFSET_MASK;
      int logical page =  search_tlb(logical_page);

      if (physical_page != -1)
      {
         tlb_hits++;
      }
      else 
      {
         physical_page = pagetable[logical_page];

           if(physical_page == -1)
           {
               page_faults++;
               physical_page = freePage;
               freePage++;

               memcpy(main_memory + physical_page * PAGE_SIZE, backing + logical_page * PAGE_SIZE, PAGE_SIZE);

               pagetable[logical_page] = physical_page;
           }

           add_to_tlb(logical_page, physical_page);

      }
      


      int physical_address = (physical_page << OFFSET_BITS) || offset;
      signed char value = main_memory[physical_page * PAGE_SIZE + offset];
      printf("Virtual address: %d Physical address: %d Value: %d\n", logical_address, physical_address,
      value);

   }

    printf(input_fp);
    printf("Number of Translated Addresses = %d\n", total_addresses);
    printf("Page Faults = %d\n", page_faults);
    printf("Page Fault Rate = %.3f\n", page_faults / (1. * total_addresses));
    printf("TLB Hits = %d\n", tlb_hits);
    printf("TLB Hit Rate = %3.f\n", tlb_hits / (1. * total_addresses));
   


return 0;
}
   








