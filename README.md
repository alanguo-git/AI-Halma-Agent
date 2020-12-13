# AI-Halma-Agent
This an artificial intelligent halma agent.  
Implemented using minimax algorithm with alpha beta pruning.

<img src="2.png" width=20% height=20%/> 

## Getting started  
1. Download the code and open the src folder.  
2. Compile all source file and run Game.class, this will generate a output.txt which is next movement of agent.  

note: The program is used for generating the next move based on current Halma Board. 
It is not a complete Halma game. But further development may happen in the future.

## Input format
<span style="color: red">This prorgam requires input.txt as input file which shows the current status of the game. A sample input.txt is given in the root folder.</span>  
The file input.txt in the current directory of your program will be formatted as follows:  
<span style="color: cyan">First line:</span> A string SINGLE or GAME to let you know whether you are playing a single move (and can use all of the available time for it) of playing a full game with potentially many moves (in which case you should strategically decide how to best allocate your time across moves).  
<span style="color: cyan">Second line:</span> A string BLACK or WHITE indicating which color you play. The colors will always be organized on the board as follows:  
<img src="1.png" width=20% height=20%/>  
(black starts in the top-left corner and white in the bottom-right).  
<span style="color: cyan">Third line:</span> A strictly positive floating point number indicating the amount of total play time remaining for your agent.
<span style="color: cyan">Next 16 lines:</span> Description of the game board, with 16 lines of 16 symbols each:
- W for a grid cell occupied by a white piece
- B for a grid cell occupied by a black piece
- . (a dot) for an empty grid cell

## Game visualization
There is a open source <a href="https://github.com/panyz522/CS561-HalmaEditor">HalmaEditor</a> which can be used to show current Halma board.  
Follow the instructions and run HalmaEditor.exe. Wait a second and look for a line starting with Now listening on. Go to http://localhost:5000 with a broswer. Finally, link input and output. (Do not need to open new runner)  
This HalmaEditor will apply output to generate new input and update Halma board with new input.