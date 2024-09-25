/* Name: Harbir Aujla
   Course: CS 30200 001
   Student no: 604
   Assignment: Program 1 CPU/Process Scheduling
*/



#include<queue>
#include<iostream>
#include<string>
#include<fstream>
#include<map>
#include<vector>
#include<iomanip>

const int MAX_PROCESSES = 100;
const int MAX_RESOURCES = 100;


using namespace std;

enum Processes{REAL_TIME, INTERACTIVE};
enum Resource{CPU, DISK, TTY};


struct ResourceRequest{
  int startTime;
  Resource type;
  int duration;

};

struct Resource{
   
}

struct Process{

int pid;              // process id
Processes type;
int arrivalTime;
int deadline;
std:: vector<ResourceRequest> requests;

};


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







int main(){

std::priority_queue<Process> readyQueue;
std::map<Resource, std::queue<Process>> resourceQueues;
std::map<int, Process>processTable;

std:: string line;
int processCount = 0;
int resourceCount = 0;

while(std::getline(std::cin, line))
{
  std::istringstream iss(line);
  std::string processClass, resource;
  int time;


if(line.find("INTERACTIVE")== 0)
{
      iss>>processClass>>time;
      Process p = {processCount, INTERACTIVE, time};
      readyQueue.push(p);
      processTable[processCount++]=p;
}
else if(line.find("REAL_TIME")==0)
{
      iss>>processClass>>time;
      int deadline;
      std::getline(std::cin, line);
      std::istringstream iss2(line);
      iss>>resource>>deadline;
      Process p ={processCount, REAL_TIME, deadline};
      readyQueue.push(p);
      processTable[processCount++]=p;
}
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

ifstream myfile ("input.txt");
  if (myfile.is_open())
  {
    while ( getline (myfile,line) )
    {
      cout << line << '\n';
    }
    myfile.close();
  }



std::cout<<"Total Real-Time processes completed:" <<endl 
        << "Percentage Real-Time Processes missed:"<<endl
       << "Total number of Interactive processes completed:"<< endl
      <<"Total number of DISK accesses:" << endl
      <<"Total time elapsed since start of first process:"<<endl
      << "Average duration of a DISK access(w/ waiting):"<<endl
      << "CPU utilization:" << endl
      << "DISK utilization:" <<endl; 

}

