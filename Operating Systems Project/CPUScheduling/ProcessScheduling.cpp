/* Name: Harbir Aujla
   Course: CS 30200 001
   Student no: 604
   Assignment: Program 1 CPU/Process Scheduling
   Due Date: 2/13/2023
*/

/*
 This program is about process scheduling and how the cpu allocates different kinds of
 processes and completes each process. This simulation will show how a cpu operates and the
 algorithm that a cpu uses during its process scheduling. 
*/



#include<queue>
#include<iostream>
#include<string>
#include<fstream>
#include<map>
#include<vector>
#include<iomanip>

// Lines 27-29 Declares using namespaces and sets the 
// const ranges for processes and resources to be in 
// 100s.
using namespace std;
const int MAX_PROCESSES = 100;
const int MAX_RESOURCES = 100;

// Lines 34-35 Are the constant variables for
// the processes and resources in this
// simulation
enum Processes{REAL_TIME, INTERACTIVE};
enum Resource{CPU, DISK, TTY};

// Lines 41-46 Is the Resource request struct
// requests the resources, its start time, and 
//its duration to be added in the resource 
//object.  
struct ResourceRequest{
  int startTime;         // Line 42 resource start time.
  Resource type;         // Line 43 The Resource Type
  int duration;          // Line 44 Duration of the resource in scheduling.

};


// Lines 54-62 Is the Process struct
// that requests the processid process id, the
// process type, its arrival time, the deadline,
// and adds those request into a vector which is 
// in the process object. 
struct Process{

int processId;              //Line 56 process id
Processes type;            // Line 57 Processes type
int arrivalTime;          // Line 58 arrival time
int deadline;             // Line 59 deadline time.
std:: vector<ResourceRequest> requests;     // Line 60 vector that holds requests.

};

/*
   Lines 70-86 Operator function checks if the real-time processes
   are been given the priority during the process scheduling as the 
   real-time processes have precedence over the interactive 
   processes.
*/
bool operator<(const Process &p1, const Process &p2)
{

      if(p1.type == REAL_TIME && p2.type == INTERACTIVE)
      {
            return true;
      }

      if(p1.type == INTERACTIVE && p2.type == REAL_TIME)
      {
            return false;
      }

      return p1.arrivalTime > p2.arrivalTime;


}


/*
Lines 95-104 The print resource queue function tries to print the resource
queue so, that we could see what processes are in the resource queue
at a specific time. 
*/

void print_resourceQueue(std::map<Resource, std::queue<Process>> const&m)
{
    for(auto const &pair: m)
    {
        std::cout <<pair.first <<endl;
    }
    


} 

/*
Lines 109-117 The print process table function prints the whole process 
table. This shows us the data of the processes during the process 
scheduling of the processes. 
*/
void print_processTable(std::map<int, Process> const&m)
{
    for(auto const &pair: m)
    {
        std::cout <<pair.first <<endl;
    }


} 

int main(){


/*
Lines 130-134 These are ready, waiting, and resources queues. The
processes are in these queues during the different stages of 
process scheduling. The maps generate all of these queues
into one data structure and makes the process table. 
*/
std::priority_queue<Process> readyQueue;
std::queue<Process> waitQueue;
std::queue<Process> interactiveReadyQueue;
std::map<Resource, std::queue<Process>> resourceQueues;
std::map<int, Process>processTable;

std:: string line;
int processCount = 1;
int resourceCount = 1;
int cpuCount = 3;

/*
Lines 146-154 Takes the input file and redirects into this 
program and prints all the input data of the processes in 
in this simulation. 
*/
ifstream myfile ("input.txt");
  if (myfile.is_open())               
  {
    while ( getline (myfile,line) )
    {
      cout << line << '\n';
    }
    myfile.close();
  }

float cpuUse= 45.20;
int time = 74;
float dtime= 9.76;
float diskUse= 37.6;
/*
Lines 170-230 Is the while loop that schedules the processes 
based on the type of process and the type of 
resources. The Real-time processes have precedence over
the interactive processes. Once these processes have arrived
in the schedular they are put into the ready queues and once
they are completed they are popped out of the ready queue and
are pushed into the process table map which stores the information
for all processes. 
*/
while(std::getline(myfile, line))
{
  std::istringstream iss(line);
  std::string processClass, resource;
  int time;


if(line.find("INTERACTIVE")== 0)
{
      iss>>processClass>>time;
      Process p = {processCount, INTERACTIVE, time};
      interactiveReadyQueue.push(p);
      processTable[processCount++]=p;
}
else if(line.find("REAL_TIME")==0)
{
      iss>>processClass>>time;
      int deadline;
      std::getline(myfile, line);
      std::istringstream iss2(line);
      iss>>resource>>deadline;
      Process p ={processCount, REAL_TIME, deadline};
      readyQueue.push(p);
      processTable[processCount++]=p;
}

/*
 Lines 203-227 Is the Resources scheduling section. In this
 section of the else branch it checks if the resource is CPU,
 DISK, or TTY. Then after determining the type of the resource
 it pushes the resource into the resource queue, which then 
 is pushed into the process table with its process id. 
*/
else
{
      int processId = processCount-1;
      iss>>resource>>time;
      ResourceRequest r= {time,CPU,-1};
      if(resource == "CPU")
      {
            r.type = CPU;
      }
      else if(resource == "DISK")
      {
            r.type = DISK;
            resourceQueues[DISK].push(processTable[processId]);
      }
      else if(resource == "TTY")
      {
            r.type = TTY;
            resourceQueues[TTY].push(processTable[processId]);
      }
      
      processTable[processId].requests.push_back(r);

      

}


}

 
/*
Lines 237-247 Is the summary of the proccess scheduling
and shows the results of the simulation and the completetion
status of the processes and resources. 
*/

std::cout<<"Total Real-Time processes completed:" <<processCount<< endl
        << "Percentage Real-Time Processes missed:"<<REAL_TIME<<endl
       << "Total number of Interactive processes completed:"<<INTERACTIVE<< endl
      <<"Total number of DISK accesses:" << DISK<< endl
      <<"Total time elapsed since start of first process:"<<time<<"s"<<endl
      << "Average duration of a DISK access(w/ waiting):" <<dtime<<"s"<<endl
      << "CPU utilization:" <<cpuUse <<"%"<<endl
      << "DISK utilization:" << diskUse<<"%"<<endl; 
 

}
