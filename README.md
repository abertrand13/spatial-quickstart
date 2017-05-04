# Networking Video
In this lab, you will be implementing an app that allows you to receive video stream from a remote FPGA board. In addition, your app also allows users to switch between local camera video and remote camera video by turning on / off the on-board switches.

If you have not mounted your spatial-quickstart on your local computer, please do so by following the instructions on canvas, under the annoucement "Instructions on new Spatial workflow"

To start working on this lab, please sign in to your tucson server by running:
```bash
ssh USERNAME@tucson.stanford.edu
```
After login, please run: 
```bash
cd spatial-quickstart
cd src
```
In the src folder, please complete SwitchVideo.scala. You will need to implement two functions including: 
1. Reading from the switches and store the value to some hardware that is visible to the ARM.
2. Implement local video streaming by connecting a streamIn of camera to a streamOut of VGA.

After you complete the hardware description, you will need to generate the hardware and software files. Please run: 
```bash
cd ~/spatial-quickstart
bin/spatial SwitchVideo --synth
```

To view the generated files, run:
```bash
cd ~/spatial-quickstart/gen/SwitchVideo
```

Once you are confident with your hardware implementation, run:
```bash
make de1soc-hw
```
**In order to test your hardware, you need to at least replace c1->run() with c1->start() and a while(1) loop in your TopHost.cpp. c1->start() starts your design on board, and the while(1) loop allows your streaming app to keep running.**

For the software part, your task is to implement a UDP client and run it on your local board. The UDP client should disassemble the network video stream coming from a remote board and displays either the local video or the network video to VGA depending on values read from the switches. If all the switches are turned off, the client should display the network video. Otherwise the client should display the local video.

In the cpp folder, please modify TopHost.cpp to complete your UDP client app.
First, remove the line 
```cpp
c1->run();
```

Then add your implementation in the "Application" function. Here are the features you need to add: 
1. Set up a UDP client that listens to connections from any ip addresses. Please set the port to "SERVICE_PORT", as the network video packets will be sent to this port.
2. Disassemble the network video packets. In each packet, the first 4 bytes are used to store the row number of a frame. The rest bytes store one line of pixels from that row. 
4. Write the pixels to the back pixel buffer. Take a look at the functions implemented in:
```bash
~/spatial-quickstart/gen/SwitchVideo/cpp/fringeDE1SoC/FringeContextBase.h
```
5. Keep track of the state of the switches. If some of the switches is on, you need to disable the network video and enable the local camera

You can use either writeRow2BackBuffer or writePixel2BackBuffer to write to the back buffer. Don't forget to sync the front and the back buffer by setting the swap flag!

**If you want to regenerate files for your app, please keep a copy of TopHost.cpp somewhere else and then start the code generation.**

Once you are confident with your software implementation, run: 
```bash
cd ~/spatial-quickstart/gen/SwitchVideo
make de1soc-sw
```
This command will compile your software implementation, and pack the binaries into a folder called prog. For example, let's say that you want to run the client on a local board called ee109-04. Run:
```bash
scp -r prog ee109@ee109-04:~/
```

To set up a remote board, you will need to choose an FPGA board in the ee109 lab that is connected to a camera. For example, let's say we choose ee109-03 to be the remote board and ee109-04 to be the local board. 
First, you will need to determine the IP address of the local board. To do so, you need to sign in to ee109-04 and run: 
```bash
ssh ee109@ee109-04
/sbin/ifconfig eth0| grep 'inet addr:'
```

And you will see: 
```bash
inet addr:172.24.89.108  Bcast:172.24.89.255  Mask:255.255.255.0

```
The inet addr is the IP address of ee109-04. Then sign out of ee109-04 by running:
```bash
exit
```

Then, you will need to sign in to ee109-03 and start to stream video to ee109-04 through UDP by running: 
```bash
ssh ee109@ee109-03
cd serverPack/
sudo ./startServer.sh 172.24.89.108
```

On your local board side (ee109-04), run: 
```bash
ssh ee109@ee109-04
cd prog
sudo ./Top
```

If your implementation is correct, you shall see the VGA displaying videos from the remote board. Please turn on / off the switches to check if you can switch to view the local video.

# Sobel Filter
Please take a look at the StreamingSobel.scala in ~/spatial-quickstart/src, and complete the app by following the comments. After you complete the app, here is the procedure to test it: 
```bash
cd ~/spatial-quickstart
bin/spatial StreamingSobel --sim
cd gen/StreamingSobel/
chmod +x run.sh 
./run.sh 128 
Enter name of file to use for StreamIn imgIn (StreamingSobel.scala:28:35): ../../cameraman.dat
Enter name of file to use for StreamOut imgOut (StreamingSobel.scala:29:36): ../../cameraman_out.dat
cd ../..
bin/imagify cameraman_out.dat 
bin/imagify camera cameraman_out.dat 128 128 
```
An image that displays the edges within the original picture will show up. 
