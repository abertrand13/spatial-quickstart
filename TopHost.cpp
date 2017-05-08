#include <stdint.h>
#include <sys/time.h>
#include <iostream>
#include <fstream>
#include <string> 
#include <sstream> 
#include <stdarg.h>
#include <signal.h>
#include <sys/wait.h>
#include <pwd.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include "DeliteCpp.h"
#include "cppDeliteArraystring.h"
#include "cppDeliteArrays.h"
#include "cppDeliteArraydouble.h"
#include "FringeContext.h"
#include "functions.h"
#include <vector>
#include <sys/socket.h>
using std::vector;

#define BUFLEN 2048
#define SERVICE_PORT 21234

void Application(int numThreads, vector<string> * args) {
  // Create an execution context.
  FringeContext *c1 = new FringeContext("./verilog/accel.bit.bin");
  c1->load();
  int32_t x204 = 0; // Initialize cpp argout ???
  // x205 = SliderSwitch // TODO: No idea what to connect this bus to, should expose periphal pins to something...
  // x206 = VideoCamera // TODO: No idea what to connect this bus to, should expose periphal pins to something...
  // x207 = VGA // TODO: No idea what to connect this bus to, should expose periphal pins to something...
  // x208 = Forever
  time_t tstart = time(0);
  //c1->run();
  c1->start();
  // All Code Goes Between Here
  
  stuct sockaddr_in myaddr, remaddr;
  int i, fd, slen = sizeof(remaddr);
  char buf[BUFLEN];
  int recvlen;
  char *server = 172.24.89.108;

  if ((fd=socket(AF_INET, SOCK_DGRAM, 0)) == -1) return 0;

  memset((char*)&myaddr, 0, sizeof(myaddr));
  myaddr.sin_family = AF_INET;
  myaddr.sin_addr.s_addr = htonl(INADDR_ANY);
  myaddr.sin_port = htons(0);

  if (bind(fd, (struct sockaddr*)&myaddr, sizeof(myaddr)) < 0) {
	perrer("Bind failed.");
	return 0;
  }

  memset((char*)&remaddr, 0, sizeof(remaddr));
  remaddr.sin_family = AF_INET;
  remaddr.sin_port = htons(SERVICE_PORT);
  if (inet_aton(server, &remaddr.sin_addr) == 0) {
	fprintf(stderr, "inet_aton() failed\n");
	exit(1);
  }

  while(1) {
//	recvlen = recvfrom(fd, buf, BUFLEN, 0, (struct sockaddr*)&remaddr, &slen);
	if (c1->readReg(io1) == 0) c1->enableCamera();
	else {
		
		
	}
  }
  close(fd);

  // And Here
  time_t tend = time(0);
  double elapsed = difftime(tend, tstart);
  std::cout << "Kernel done, test run time = " << elapsed << " ms" << std::endl;
  // results in ()
  delete c1;
}

int main(int argc, char *argv[]) {
  vector<string> *args = new vector<string>(argc-1);
  for (int i=1; i<argc; i++) {
    (*args)[i-1] = std::string(argv[i]);
  }
  int numThreads = 1;
  char *env_threads = getenv("DELITE_NUM_THREADS");
  if (env_threads != NULL) {
    numThreads = atoi(env_threads);
  } else {
    fprintf(stderr, "[WARNING]: DELITE_NUM_THREADS undefined, defaulting to 1\n");
  }
  fprintf(stderr, "Executing with %d thread(s)\n", numThreads);
  Application(numThreads, args);
  return 0;
}

