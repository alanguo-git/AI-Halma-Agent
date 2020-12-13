import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

public class state {
    char[][] board;
    int[] primeNumber = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53,
            59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131};
    boolean isBlack;
    char playerColor;
    ArrayList<point> blackCamp;
    ArrayList<point> whiteCamp;
    HashSet<Integer> blackCampHash;
    HashSet<Integer> whiteCampHash;
    HashSet<Integer> blackSecondLayer;
    HashSet<Integer> blackThirdLayer;
    HashSet<Integer> blackForthLayer;
    HashSet<Integer> blackFifthLayer;
    HashSet<Integer> whiteSecondLayer;
    HashSet<Integer> whiteThirdLayer;
    HashSet<Integer> whiteForthLayer;
    HashSet<Integer> whiteFifthLayer;
    state preState;
    int depth;
    char moveMethod;
    point preLocation;
    point newLocation;
    int maxDepth;

    state(boolean a, char c, char[][] b, state p, int d, char m, point preP, point newP, int md){
        isBlack = a;
        board = b;
        playerColor = c;
        preState = p;
        depth = d;
        moveMethod = m;
        preLocation = preP;
        newLocation = newP;
        maxDepth = md;

        blackCampHash = new HashSet<>();
        blackCampHash.add(2*59);
        blackCampHash.add(3*59);
        blackCampHash.add(5*59);
        blackCampHash.add(7*59);
        blackCampHash.add(11*59);
        blackCampHash.add(2*61);
        blackCampHash.add(3*61);
        blackCampHash.add(5*61);
        blackCampHash.add(7*61);
        blackCampHash.add(11*61);
        blackCampHash.add(2*67);
        blackCampHash.add(3*67);
        blackCampHash.add(5*67);
        blackCampHash.add(7*67);
        blackCampHash.add(2*71);
        blackCampHash.add(3*71);
        blackCampHash.add(5*71);
        blackCampHash.add(2*73);
        blackCampHash.add(3*73);

        whiteCampHash = new HashSet<>();
        whiteCampHash.add(53*131);
        whiteCampHash.add(47*131);
        whiteCampHash.add(43*131);
        whiteCampHash.add(41*131);
        whiteCampHash.add(37*131);
        whiteCampHash.add(53*127);
        whiteCampHash.add(47*127);
        whiteCampHash.add(43*127);
        whiteCampHash.add(41*127);
        whiteCampHash.add(37*127);
        whiteCampHash.add(53*113);
        whiteCampHash.add(47*113);
        whiteCampHash.add(43*113);
        whiteCampHash.add(41*113);
        whiteCampHash.add(53*109);
        whiteCampHash.add(47*109);
        whiteCampHash.add(43*109);
        whiteCampHash.add(53*107);
        whiteCampHash.add(47*107);

        blackSecondLayer = new HashSet<>();
        blackSecondLayer.add(2*61);
        blackSecondLayer.add(3*61);
        blackSecondLayer.add(3*59);

        blackThirdLayer = new HashSet<>();
        blackThirdLayer.add(2*67);
        blackThirdLayer.add(3*67);
        blackThirdLayer.add(5*67);
        blackThirdLayer.add(5*61);
        blackThirdLayer.add(5*59);

        blackForthLayer = new HashSet<>();
        blackForthLayer.add(2*71);
        blackForthLayer.add(3*71);
        blackForthLayer.add(5*71);
        blackForthLayer.add(7*67);
        blackForthLayer.add(7*61);
        blackForthLayer.add(7*59);

        blackFifthLayer = new HashSet<>();
        blackFifthLayer.add(2*73);
        blackFifthLayer.add(3*73);
        blackFifthLayer.add(11*59);
        blackFifthLayer.add(11*61);

        whiteSecondLayer = new HashSet<>();
        whiteSecondLayer.add(131*47);
        whiteSecondLayer.add(127*47);
        whiteSecondLayer.add(127*53);

        whiteThirdLayer = new HashSet<>();
        whiteThirdLayer.add(131*43);
        whiteThirdLayer.add(127*43);
        whiteThirdLayer.add(113*43);
        whiteThirdLayer.add(113*47);
        whiteThirdLayer.add(113*53);

        whiteForthLayer = new HashSet<>();
        whiteForthLayer.add(131*41);
        whiteForthLayer.add(127*41);
        whiteForthLayer.add(113*41);
        whiteForthLayer.add(109*43);
        whiteForthLayer.add(109*47);
        whiteForthLayer.add(109*53);

        whiteFifthLayer = new HashSet<>();
        whiteFifthLayer.add(131*37);
        whiteFifthLayer.add(127*37);
        whiteFifthLayer.add(107*47);
        whiteFifthLayer.add(107*53);
    }

    PiecesSet checkCamp(){ //returns the list of pieces which are still in the camp
        ArrayList<point> campPieces = new ArrayList<>(); //stores the pieces which is in the camp
        ArrayList<point> outsidePieces = new ArrayList<>(); //stores the pieces which is outside the camp
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 16; j++){
                int hashCode = primeNumber[i]*primeNumber[16+j];
                if(isBlack){
                    if(board[i][j] == playerColor){
                        if(blackCampHash.contains(hashCode)){
                            campPieces.add(new point(i,j));
                        }
                        else{
                            outsidePieces.add(new point(i,j));
                        }
                    }
                }
                else{
                    if(board[i][j] == playerColor){
                        if(whiteCampHash.contains(hashCode)){
                            campPieces.add(new point(i,j));
                        }
                        else{
                            outsidePieces.add(new point(i,j));
                        }
                    }
                }
            }
        }
        return new PiecesSet(campPieces, outsidePieces);
    }

    ArrayList<state> getNextState(){
        PiecesSet piecesSet = checkCamp();
        ArrayList<point> campPieces = piecesSet.campPieces;
        ArrayList<point> outsidePieces = piecesSet.outsidePieces;
        //if((inOpposingCamp(outsidePieces) > 5) && (inOpposingCamp(outsidePieces) < 15)){
        //   maxDepth = 4;
        //}

        if(inOpposingCamp(outsidePieces) == 18){
            maxDepth = 1;
        }
        char newPlayerColor = (playerColor == 'B') ? 'W':'B';
        ArrayList<state> nextState= new ArrayList<>();
        if(inOpposingCamp(outsidePieces) == 17){
            ArrayList<point> nonfinishPieces = new ArrayList<>();
            for(int i = 0; i < outsidePieces.size(); i++){
                point temp = outsidePieces.get(i);
                if(!isInOpposingCamp(temp.x, temp.y)){
                    nonfinishPieces.add(new point(temp.x, temp.y));
                }
            }
            getNextStateSpecial(nextState, nonfinishPieces, newPlayerColor);
            if(nextState.size() != 0){
                return nextState;
            }
        }
        if(campPieces.size() == 0){ //there is no pieces in the camp
            for(int m = 0; m < outsidePieces.size(); m++) {
                int i = outsidePieces.get(m).x;
                int j = outsidePieces.get(m).y;
                if(isBlack){
                    moveBlackPieces(nextState, i, j, newPlayerColor);
                    jumpBlackPieces(i, j, nextState, newPlayerColor, this, i, j); //make jump actions
                }
                else{
                    moveWhitePieces(nextState, i, j, newPlayerColor);
                    jumpWhitePieces(i, j, nextState, newPlayerColor, this, i, j); //make jump actions
                }
            }
        }
        else{ //move pieces in the camp first, or move the piece outside the came if can not move camp pieces
            for(int m = 0; m < campPieces.size(); m++){  //check if can move one piece to the outside of the camp
                int i = campPieces.get(m).x;
                int j = campPieces.get(m).y;
                if((i >= 1) && (j >= 1) && board[i-1][j-1] == '.' && !isInCamp(i-1, j-1)){ //move north west
                    char[][] newBoard = copyBoard();
                    newBoard[i-1][j-1] = playerColor;
                    newBoard[i][j] = '.';
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i-1,j-1);
                    nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                }
                if((j >= 1) && board[i][j-1] == '.' && !isInCamp(i, j-1)){ //move north
                    char[][] newBoard = copyBoard();
                    newBoard[i][j-1] = playerColor;
                    newBoard[i][j] = '.';
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i,j-1);
                    nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                }
                if((i <= 14) && (j >= 1) && board[i+1][j-1] == '.' && !isInCamp(i+1, j-1)){ //move north east
                    char[][] newBoard = copyBoard();
                    newBoard[i+1][j-1] = playerColor;
                    newBoard[i][j] = '.';
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i+1,j-1);
                    nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                }
                if((i >= 1) && board[i-1][j] == '.' && !isInCamp(i-1, j)){ //move west
                    char[][] newBoard = copyBoard();
                    newBoard[i-1][j] = playerColor;
                    newBoard[i][j] = '.';
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i-1,j);
                    nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                }
                if((i <= 14) && board[i+1][j] == '.' && !isInCamp(i+1, j)){ //move east
                    char[][] newBoard = copyBoard();
                    newBoard[i+1][j] = playerColor;
                    newBoard[i][j] = '.';
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i+1,j);
                    nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                }
                if((i >= 1) && (j <= 14) && board[i-1][j+1] == '.' && !isInCamp(i-1, j+1)){ //move south west
                    char[][] newBoard = copyBoard();
                    newBoard[i-1][j+1] = playerColor;
                    newBoard[i][j] = '.';
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i-1,j+1);
                    nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                }
                if((j <= 14) && board[i][j+1] == '.' && !isInCamp(i, j+1)){ //move south
                    char[][] newBoard = copyBoard();
                    newBoard[i][j+1] = playerColor;
                    newBoard[i][j] = '.';
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i,j+1);
                    nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                }
                if((i <= 14) && (j <= 14) && board[i+1][j+1] == '.' && !isInCamp(i+1, j+1)){ //move south east
                    char[][] newBoard = copyBoard();
                    newBoard[i+1][j+1] = playerColor;
                    newBoard[i][j] = '.';
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i+1,j+1);
                    nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                }
                jumpOut(i, j, nextState, newPlayerColor, this, i, j); //make jump actions
            }
            if(nextState.size() == 0){ //can not move a piece out of the camp, then move a piece further away from the corner
                if(isBlack){
                    for(int m = 0; m < campPieces.size(); m++){
                        int i = campPieces.get(m).x;
                        int j = campPieces.get(m).y;
                        if(board[i+1][j] == '.'){ //move east
                            char[][] newBoard = copyBoard();
                            newBoard[i+1][j] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i+1,j);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                        }
                        if(board[i][j+1] == '.'){ //move south
                            char[][] newBoard = copyBoard();
                            newBoard[i][j+1] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i,j+1);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                        }
                        if(board[i+1][j+1] == '.'){ //move south east
                            char[][] newBoard = copyBoard();
                            newBoard[i+1][j+1] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i+1,j+1);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                        }
                        jumpFurther(i, j, nextState, newPlayerColor, this, i, j); //make jump actions
                    }
                }
                else{ //white piece
                    for(int m = 0; m < campPieces.size(); m++){
                        int i = campPieces.get(m).x;
                        int j = campPieces.get(m).y;
                        if(board[i-1][j] == '.'){ //move west
                            char[][] newBoard = copyBoard();
                            newBoard[i-1][j] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i-1,j);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                        }
                        if(board[i][j-1] == '.'){ //move north
                            char[][] newBoard = copyBoard();
                            newBoard[i][j-1] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i,j-1);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                        }
                        if(board[i-1][j-1] == '.'){ //move south east
                            char[][] newBoard = copyBoard();
                            newBoard[i-1][j-1] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i-1,j-1);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                        }
                        jumpFurther(i, j, nextState, newPlayerColor, this, i, j); //make jump actions
                    }
                }
            }
            if(nextState.size() == 0){ //move outside pieces
                for(int m = 0; m < outsidePieces.size(); m++) {
                    int i = outsidePieces.get(m).x;
                    int j = outsidePieces.get(m).y;
                    if(isBlack){
                        moveBlackPieces(nextState, i, j, newPlayerColor);
                        jumpBlackPieces(i, j, nextState, newPlayerColor, this, i, j); //make jump actions
                    }
                    else{
                        moveWhitePieces(nextState, i, j, newPlayerColor);
                        jumpWhitePieces(i, j, nextState, newPlayerColor, this, i, j); //make jump actions
                    }
                }
            }
        }
        return nextState;
    }

    boolean isTerminal(){
        if(depth >= maxDepth){
            return true;
        }
        return false;
    }

    double getUtility(){
        double score = 0;
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 16; j ++){
                if(board[i][j] == 'B'){ //is a black piece
                    int hashCode = primeNumber[i]*primeNumber[16+j];
                    if(whiteCampHash.contains(hashCode)){ //this black piece is in white camp
                        if(hashCode == 6943){
                            score = score + 1500.05;
                        }
                        else if(whiteSecondLayer.contains(hashCode)){
                            score = score + 1500.03;
                        }
                        else if(whiteThirdLayer.contains(hashCode)){
                            score = score + 1500.02;
                        }
                        else if(whiteForthLayer.contains(hashCode)){
                            score = score + 1500.01;
                        }
                        else if(whiteFifthLayer.contains(hashCode)){
                            score = score + 1500;
                        }
                        else{
                            System.out.println("errors in hash set");
                        }
                    }
                    else{
                        score = score + calculateScore(Math.max(15-i, 15-j));
                    }
                }
                if(board[i][j] == 'W'){ //is a white piece
                    int hashCode = primeNumber[i]*primeNumber[16+j];
                    if(blackCampHash.contains(hashCode)){ //this white piece is in black camp
                        if(hashCode == 118){
                            score = score - 1500.05;
                        }
                        else if(blackSecondLayer.contains(hashCode)){
                            score = score - 1500.03;
                        }
                        else if(blackThirdLayer.contains(hashCode)){
                            score = score - 1500.02;
                        }
                        else if(blackForthLayer.contains(hashCode)){
                            score = score - 1500.01;
                        }
                        else if(blackFifthLayer.contains(hashCode)){
                            score = score - 1500;
                        }
                        else{
                            System.out.println("errors in hash set");
                        }
                    }
                    else{
                        score = score - calculateScore(Math.max(i,j));
                    }
                }
            }
        }
        return score;
    }

    char[][] copyBoard(){
        char[][] newBoard = new char[16][16];
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 16; j++){
                newBoard[i][j] = board[i][j];
            }
        }
        return newBoard;
    }

    boolean sameBoard(char[][] newBoard){ //check is a new board is equal to the board in this state
        boolean isEqual = true;
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 16; j ++){
                if(newBoard[i][j] != board[i][j]){
                    isEqual = false;
                }
            }
        }
        return isEqual;
    }

    void jumpOut(int i, int j, ArrayList nextState, char newPlayerColor, state currentState, int initial_x, int initial_y){ //keep jumping to out of the camp
        if((i >= 2) && (j >= 2) && (currentState.board[i-1][j-1] != '.') && currentState.board[i-2][j-2] == '.'){ //jump north west
            char[][] newBoard = currentState.copyBoard();
            newBoard[i-2][j-2] = currentState.playerColor;
            newBoard[i][j] = '.';
            if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i-2,j-2);
                if(!isInCamp(i-2, j-2)){
                    if((isInOpposingCamp(initial_x,initial_y) && isInOpposingCamp(i-2, j-2)) || !isInOpposingCamp(initial_x,initial_y)){
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                    }
                }
                jumpOut(i-2, j-2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard,
                        currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), initial_x, initial_y);
            }
        }
        if((j >= 2) && (currentState.board[i][j-1] != '.') && currentState.board[i][j-2] == '.'){ //jump north
            char[][] newBoard = currentState.copyBoard();
            newBoard[i][j-2] = currentState.playerColor;
            newBoard[i][j] = '.';
            if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i,j-2);
                if(!isInCamp(i, j-2)){
                    if((isInOpposingCamp(initial_x,initial_y) && isInOpposingCamp(i, j-2)) || !isInOpposingCamp(initial_x,initial_y)){
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                    }
                }
                jumpOut(i, j-2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard,
                            currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), initial_x, initial_y);
            }
        }
        if((i <= 13) && (j >= 2) && (currentState.board[i+1][j-1] != '.') && currentState.board[i+2][j-2] == '.'){ //jump north east
            char[][] newBoard = currentState.copyBoard();
            newBoard[i+2][j-2] = currentState.playerColor;
            newBoard[i][j] = '.';
            if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i+2,j-2);
                if(!isInCamp(i+2, j-2)){
                    if((isInOpposingCamp(initial_x,initial_y) && isInOpposingCamp(i+2, j-2)) || !isInOpposingCamp(initial_x,initial_y)){
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                    }
                }
                jumpOut(i+2, j-2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard,
                        currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), initial_x, initial_y);
            }
        }
        if((i >= 2) && (currentState.board[i-1][j] != '.') && currentState.board[i-2][j] == '.'){ //jump west
            char[][] newBoard = currentState.copyBoard();
            newBoard[i-2][j] = currentState.playerColor;
            newBoard[i][j] = '.';
            if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i-2,j);
                if(!isInCamp(i-2, j)){
                    if((isInOpposingCamp(initial_x,initial_y) && isInOpposingCamp(i-2, j)) || !isInOpposingCamp(initial_x,initial_y)){
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                    }
                }
                jumpOut(i-2, j, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard,
                        currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), initial_x, initial_y);
            }
        }
        if((i <= 13) && (currentState.board[i+1][j] != '.') && currentState.board[i+2][j] == '.'){ //jump east
            char[][] newBoard = currentState.copyBoard();
            newBoard[i+2][j] = currentState.playerColor;
            newBoard[i][j] = '.';
            if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i+2,j);
                if(!isInCamp(i+2, j)){
                    if((isInOpposingCamp(initial_x,initial_y) && isInOpposingCamp(i+2, j)) || !isInOpposingCamp(initial_x,initial_y)){
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                    }
                }
                jumpOut(i+2, j, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard,
                        currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), initial_x, initial_y);
            }
        }
        if((i >= 2) && (j <= 13) && (currentState.board[i-1][j+1] != '.') && currentState.board[i-2][j+2] == '.'){ //jump south west
            char[][] newBoard = currentState.copyBoard();
            newBoard[i-2][j+2] = currentState.playerColor;
            newBoard[i][j] = '.';
            if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i-2,j+2);
                if(!isInCamp(i-2, j+2)){
                    if((isInOpposingCamp(initial_x,initial_y) && isInOpposingCamp(i-2, j+2)) || !isInOpposingCamp(initial_x,initial_y)){
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                    }
                }
                jumpOut(i-2, j+2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard,
                        currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), initial_x, initial_y);
            }
        }
        if((j <= 13) && (currentState.board[i][j+1] != '.') && currentState.board[i][j+2] == '.'){ //jump south
            char[][] newBoard = currentState.copyBoard();
            newBoard[i][j+2] = currentState.playerColor;
            newBoard[i][j] = '.';
            if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i,j+2);
                if(!isInCamp(i, j+2)){
                    if((isInOpposingCamp(initial_x,initial_y) && isInOpposingCamp(i, j+2)) || !isInOpposingCamp(initial_x,initial_y)){
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                    }
                }
                jumpOut(i, j+2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard,
                        currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), initial_x, initial_y);
            }
        }
        if((i <= 13) && (j <= 13) && (currentState.board[i+1][j+1] != '.') && currentState.board[i+2][j+2] == '.'){ //jump south east
            char[][] newBoard = currentState.copyBoard();
            newBoard[i+2][j+2] = currentState.playerColor;
            newBoard[i][j] = '.';
            if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i+2,j+2);
                if(!isInCamp(i+2, j+2)){
                    if((isInOpposingCamp(initial_x,initial_y) && isInOpposingCamp(i+2, j+2)) || !isInOpposingCamp(initial_x,initial_y)){
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                    }
                }
                    jumpOut(i+2, j+2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard,
                            currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), initial_x, initial_y);
            }
        }
    }

    void jumpFurther(int i, int j, ArrayList nextState, char newPlayerColor, state currentState, int start_x, int start_y){ //keep jumping further away form the corner
        if(isBlack){ //black piece
            if((j >= 2) && (currentState.board[i][j-1] != '.') && currentState.board[i][j-2] == '.'){ //jump north
                char[][] newBoard = currentState.copyBoard();
                newBoard[i][j-2] = currentState.playerColor;
                newBoard[i][j] = '.';
                if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i,j-2);
                    if((i >= start_x) && (j-2 >= start_y)){
                        if((i > start_x) || (j-2 > start_y)){
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                        }
                    }
                    jumpFurther(i, j-2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard, currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), start_x , start_y);
                }
            }
            if((j >= 2) && (currentState.board[i+1][j-1] != '.') && currentState.board[i+2][j-2] == '.'){ //jump north east
                char[][] newBoard = currentState.copyBoard();
                newBoard[i+2][j-2] = currentState.playerColor;
                newBoard[i][j] = '.';
                if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i+2,j-2);
                    if((i+2 >= start_x) && (j-2 >= start_y)){
                        if((i+2 > start_x) || (j-2 > start_y)){
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                        }
                    }
                    jumpFurther(i+2, j-2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard, currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), start_x, start_y);
                }
            }
            if((i >= 2) && (currentState.board[i-1][j] != '.') && currentState.board[i-2][j] == '.'){ //jump west
                char[][] newBoard = currentState.copyBoard();
                newBoard[i-2][j] = currentState.playerColor;
                newBoard[i][j] = '.';
                if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i-2,j);
                    if((i-2 >= start_x) && (j >= start_y)){
                        if((i-2 > start_x) || (j > start_y)){
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                        }
                    }
                    jumpFurther(i-2, j, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard, currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), start_x, start_y);
                }
            }
            if((currentState.board[i+1][j] != '.') && currentState.board[i+2][j] == '.'){ //jump east
                char[][] newBoard = currentState.copyBoard();
                newBoard[i+2][j] = currentState.playerColor;
                newBoard[i][j] = '.';
                if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i+2,j);
                    if((i+2 >= start_x) && (j >= start_y)){
                        if((i+2 > start_x) || (j > start_y)){
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                        }
                    }
                    jumpFurther(i+2, j, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard, currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), start_x, start_y);
                }
            }
            if((i >= 2) && (currentState.board[i-1][j+1] != '.') && currentState.board[i-2][j+2] == '.'){ //jump south west
                char[][] newBoard = currentState.copyBoard();
                newBoard[i-2][j+2] = currentState.playerColor;
                newBoard[i][j] = '.';
                if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i-2,j+2);
                    if((i-2 >= start_x) && (j+2 >= start_y)){
                        if((i-2 > start_x) || (j+2 > start_y)){
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                        }
                    }
                    jumpFurther(i-2, j+2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard, currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), start_x, start_y);
                }
            }
            if((currentState.board[i][j+1] != '.') && currentState.board[i][j+2] == '.'){ //jump south
                char[][] newBoard = currentState.copyBoard();
                newBoard[i][j+2] = currentState.playerColor;
                newBoard[i][j] = '.';
                if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i,j+2);
                    if((i >= start_x) && (j+2 >= start_y)){
                        if((i > start_x) || (j+2 > start_y)){
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                        }
                    }
                    jumpFurther(i, j+2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard, currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), start_x, start_y);
                }
            }
            if((currentState.board[i+1][j+1] != '.') && currentState.board[i+2][j+2] == '.'){ //jump south east
                char[][] newBoard = currentState.copyBoard();
                newBoard[i+2][j+2] = currentState.playerColor;
                newBoard[i][j] = '.';
                if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i+2,j+2);
                    if((i+2 >= start_x) && (j+2 >= start_y)){
                        if((i+2 > start_x) || (j+2 > start_y)){
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                        }
                    }
                    jumpFurther(i+2, j+2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard, currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), start_x, start_y);
                }
            }
        }
        else{ //white piece
            if((currentState.board[i-1][j-1] != '.') && currentState.board[i-2][j-2] == '.'){ //jump north west
                char[][] newBoard = currentState.copyBoard();
                newBoard[i-2][j-2] = currentState.playerColor;
                newBoard[i][j] = '.';
                if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i-2,j-2);
                    if((i-2 <= start_x) && (j-2 <= start_y)){
                        if((i-2 < start_x) || (j-2 < start_y)){
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1,'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                        }
                    }
                    jumpFurther(i-2, j-2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard, currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), start_x, start_y);
                }
            }
            if((currentState.board[i][j-1] != '.') && currentState.board[i][j-2] == '.'){ //jump north
                char[][] newBoard = currentState.copyBoard();
                newBoard[i][j-2] = currentState.playerColor;
                newBoard[i][j] = '.';
                if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i,j-2);
                    if((i <= start_x) && (j-2 <= start_y)){
                        if((i < start_x) || (j-2 < start_y)){
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                        }
                    }
                    jumpFurther(i, j-2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard, currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), start_x , start_y);
                }
            }
            if((i <= 13) && (currentState.board[i+1][j-1] != '.') && currentState.board[i+2][j-2] == '.'){ //jump north east
                char[][] newBoard = currentState.copyBoard();
                newBoard[i+2][j-2] = currentState.playerColor;
                newBoard[i][j] = '.';
                if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i+2,j-2);
                    if((i+2 <= start_x) && (j-2 <= start_y)){
                        if((i+2 < start_x) || (j-2 < start_y)){
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                        }
                    }
                    jumpFurther(i+2, j-2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard, currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), start_x, start_y);
                }
            }
            if((currentState.board[i-1][j] != '.') && currentState.board[i-2][j] == '.'){ //jump west
                char[][] newBoard = currentState.copyBoard();
                newBoard[i-2][j] = currentState.playerColor;
                newBoard[i][j] = '.';
                if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i-2,j);
                    if((i-2 <= start_x) && (j <= start_y)){
                        if((i-2 < start_x) || (j < start_y)){
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                        }
                    }
                    jumpFurther(i-2, j, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard, currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), start_x, start_y);
                }
            }
            if((i <= 13) && (currentState.board[i+1][j] != '.') && currentState.board[i+2][j] == '.'){ //jump east
                char[][] newBoard = currentState.copyBoard();
                newBoard[i+2][j] = currentState.playerColor;
                newBoard[i][j] = '.';
                if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i+2,j);
                    if((i+2 <= start_x) && (j <= start_y)){
                        if((i+2 < start_x) || (j < start_y)){
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                        }
                    }
                    jumpFurther(i+2, j, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard, currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), start_x, start_y);
                }
            }
            if((j <= 13) && (currentState.board[i-1][j+1] != '.') && currentState.board[i-2][j+2] == '.'){ //jump south west
                char[][] newBoard = currentState.copyBoard();
                newBoard[i-2][j+2] = currentState.playerColor;
                newBoard[i][j] = '.';
                if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i-2,j+2);
                    if((i-2 <= start_x) && (j+2 <= start_y)){
                        if((i-2 < start_x) || (j+2 < start_y)){
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                        }
                    }
                    jumpFurther(i-2, j+2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard, currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), start_x, start_y);
                }
            }
            if((j <= 13) && (currentState.board[i][j+1] != '.') && currentState.board[i][j+2] == '.'){ //jump south
                char[][] newBoard = currentState.copyBoard();
                newBoard[i][j+2] = currentState.playerColor;
                newBoard[i][j] = '.';
                if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                    point nextPreLocation = new point(i,j);
                    point nextNewLocation = new point(i,j+2);
                    if((i <= start_x) && (j+2 <= start_y)){
                        if((i < start_x) || (j+2 < start_y)){
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                        }
                    }
                    jumpFurther(i, j+2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard, currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), start_x, start_y);
                }
            }
        }
    }

    boolean containsSameBoard(ArrayList<state> nextState, char[][] newBoard){
        for(int i = 0; i < nextState.size(); i++){
            state curr = nextState.get(i);
            if(curr.sameBoard(newBoard)){
                return true;
            }
        }
        return false;
    }
    boolean isInCamp(int x, int y){
        if(isBlack){//black piece
            int hashCode = primeNumber[x]*primeNumber[16+y];
            if(blackCampHash.contains(hashCode)){
                return true;
            }
            return false;
        }
        else{ //white piece
            int hashCode = primeNumber[x]*primeNumber[16+y];
            if(whiteCampHash.contains(hashCode)){
                return true;
            }
            return false;
        }
    }

    boolean isInOpposingCamp(int x, int y){
        if(isBlack){//black piece
            int hashCode = primeNumber[x]*primeNumber[16+y];
            if(whiteCampHash.contains(hashCode)){
                return true;
            }
            return false;
        }
        else{ //white piece
            int hashCode = primeNumber[x]*primeNumber[16+y];
            if(blackCampHash.contains(hashCode)){
                return true;
            }
            return false;
        }
    }

    boolean sameBoardInPreState(state currentState, char[][] newBoard){
        state temp = currentState.preState;
        if(temp == null){
            return false;
        }
        else{
            if(temp.sameBoard(newBoard)){
                return true;
            }
            else{
                return sameBoardInPreState(temp, newBoard);
            }
        }
    }

    public int inOpposingCamp(ArrayList<point> outsidePieces){
        int number = 0;
        for(int i = 0; i < outsidePieces.size(); i++){
           point pieces = outsidePieces.get(i);
           if(isInOpposingCamp(pieces.x, pieces.y)){
               number++;
           }
        }
        return number;
    }

    void moveBlackPieces(ArrayList<state> nextState, int i, int j, char newPlayerColor){
        if((i == 4) && (j == 1) && board[5][0] == '.'){ //move north east
            char[][] newBoard = copyBoard();
            newBoard[i+1][j-1] = playerColor;
            newBoard[i][j] = '.';
            point nextPreLocation = new point(i,j);
            point nextNewLocation = new point(i+1,j-1);
            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
        }
        if((i <= 14) && board[i+1][j] == '.' && !isInCamp(i+1, j)){ //move east
            if((isInOpposingCamp(i,j) && isInOpposingCamp(i+1, j)) || !isInOpposingCamp(i,j)){
                char[][] newBoard = copyBoard();
                newBoard[i+1][j] = playerColor;
                newBoard[i][j] = '.';
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i+1,j);
                nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
            }
        }
        if((i == 1) && (j == 4) && board[i-1][j+1] == '.'){ //move south west
            char[][] newBoard = copyBoard();
            newBoard[i-1][j+1] = playerColor;
            newBoard[i][j] = '.';
            point nextPreLocation = new point(i,j);
            point nextNewLocation = new point(i-1,j+1);
            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth) );
        }
        if((j <= 14) && board[i][j+1] == '.' && !isInCamp(i, j+1)){ //move south
            if((isInOpposingCamp(i,j) && isInOpposingCamp(i, j+1)) || !isInOpposingCamp(i,j)){
                char[][] newBoard = copyBoard();
                newBoard[i][j+1] = playerColor;
                newBoard[i][j] = '.';
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i,j+1);
                nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
            }
        }
        if((i <= 14) && (j <= 14) && board[i+1][j+1] == '.' && !isInCamp(i+1, j+1)){ //move south east
            if((isInOpposingCamp(i,j) && isInOpposingCamp(i+1, j+1)) || !isInOpposingCamp(i,j)){
                char[][] newBoard = copyBoard();
                newBoard[i+1][j+1] = playerColor;
                newBoard[i][j] = '.';
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i+1,j+1);
                nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
            }
        }
    }

    void moveWhitePieces(ArrayList<state> nextState, int i, int j, char newPlayerColor){
        if((i >= 1) && (j >= 1) && board[i-1][j-1] == '.' && !isInCamp(i-1, j-1)){ //move north west
            if((isInOpposingCamp(i,j) && isInOpposingCamp(i-1, j-1)) || !isInOpposingCamp(i,j)){ //can not move pieces outside of the camp if that piece is already in the camp
                char[][] newBoard = copyBoard();
                newBoard[i-1][j-1] = playerColor;
                newBoard[i][j] = '.';
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i-1,j-1);
                nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
            }
        }
        if((j >= 1) && board[i][j-1] == '.' && !isInCamp(i, j-1)){ //move north
            if((isInOpposingCamp(i,j) && isInOpposingCamp(i, j-1)) || !isInOpposingCamp(i,j)){
                char[][] newBoard = copyBoard();
                newBoard[i][j-1] = playerColor;
                newBoard[i][j] = '.';
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i,j-1);
                nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
            }
        }
        if((i == 14) && (j == 1) && board[i+1][j-1] == '.'){ //move north east
            char[][] newBoard = copyBoard();
            newBoard[i+1][j-1] = playerColor;
            newBoard[i][j] = '.';
            point nextPreLocation = new point(i,j);
            point nextNewLocation = new point(i+1,j-1);
            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
        }
        if((i >= 1) && board[i-1][j] == '.' && !isInCamp(i-1, j)){ //move west
            if((isInOpposingCamp(i,j) && isInOpposingCamp(i-1, j)) || !isInOpposingCamp(i,j)){
                char[][] newBoard = copyBoard();
                newBoard[i-1][j] = playerColor;
                newBoard[i][j] = '.';
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i-1,j);
                nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
            }
        }
        if((i == 11) && (j == 14) && board[i-1][j+1] == '.'){ //move south west
            char[][] newBoard = copyBoard();
            newBoard[i-1][j+1] = playerColor;
            newBoard[i][j] = '.';
            point nextPreLocation = new point(i,j);
            point nextNewLocation = new point(i-1,j+1);
            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
        }
    }

    void jumpBlackPieces(int i, int j, ArrayList nextState, char newPlayerColor, state currentState, int initial_x, int initial_y){
        if((i <= 13) && (j >= 2) && (currentState.board[i+1][j-1] != '.') && currentState.board[i+2][j-2] == '.'){ //jump north east
            char[][] newBoard = currentState.copyBoard();
            newBoard[i+2][j-2] = currentState.playerColor;
            newBoard[i][j] = '.';
            if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i+2,j-2);
                if(!isInCamp(i+2, j-2)){
                    if((isInOpposingCamp(initial_x,initial_y) && isInOpposingCamp(i+2, j-2)) || !isInOpposingCamp(initial_x,initial_y)){
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                    }
                }
                jumpBlackPieces(i+2, j-2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard,
                        currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), initial_x, initial_y);
            }
        }
        if((i <= 13) && (currentState.board[i+1][j] != '.') && currentState.board[i+2][j] == '.'){ //jump east
            char[][] newBoard = currentState.copyBoard();
            newBoard[i+2][j] = currentState.playerColor;
            newBoard[i][j] = '.';
            if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i+2,j);
                if(!isInCamp(i+2, j)){
                    if((isInOpposingCamp(initial_x,initial_y) && isInOpposingCamp(i+2, j)) || !isInOpposingCamp(initial_x,initial_y)){
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                    }
                }
                jumpBlackPieces(i+2, j, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard,
                        currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), initial_x, initial_y);
            }
        }
        if((i >= 2) && (j <= 13) && (currentState.board[i-1][j+1] != '.') && currentState.board[i-2][j+2] == '.'){ //jump south west
            char[][] newBoard = currentState.copyBoard();
            newBoard[i-2][j+2] = currentState.playerColor;
            newBoard[i][j] = '.';
            if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i-2,j+2);
                if(!isInCamp(i-2, j+2)){
                    if((isInOpposingCamp(initial_x,initial_y) && isInOpposingCamp(i-2, j+2)) || !isInOpposingCamp(initial_x,initial_y)){
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                    }
                }
                jumpBlackPieces(i-2, j+2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard,
                        currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), initial_x, initial_y);
            }
        }
        if((j <= 13) && (currentState.board[i][j+1] != '.') && currentState.board[i][j+2] == '.'){ //jump south
            char[][] newBoard = currentState.copyBoard();
            newBoard[i][j+2] = currentState.playerColor;
            newBoard[i][j] = '.';
            if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i,j+2);
                if(!isInCamp(i, j+2)){
                    if((isInOpposingCamp(initial_x,initial_y) && isInOpposingCamp(i, j+2)) || !isInOpposingCamp(initial_x,initial_y)){
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                    }
                }
                jumpBlackPieces(i, j+2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard,
                        currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), initial_x, initial_y);
            }
        }
        if((i <= 13) && (j <= 13) && (currentState.board[i+1][j+1] != '.') && currentState.board[i+2][j+2] == '.'){ //jump south east
            char[][] newBoard = currentState.copyBoard();
            newBoard[i+2][j+2] = currentState.playerColor;
            newBoard[i][j] = '.';
            if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i+2,j+2);
                if(!isInCamp(i+2, j+2)){
                    if((isInOpposingCamp(initial_x,initial_y) && isInOpposingCamp(i+2, j+2)) || !isInOpposingCamp(initial_x,initial_y)){
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                    }
                }
                jumpBlackPieces(i+2, j+2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard,
                        currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), initial_x, initial_y);
            }
        }
    }

    void jumpWhitePieces(int i, int j, ArrayList nextState, char newPlayerColor, state currentState, int initial_x, int initial_y){
        if((i >= 2) && (j >= 2) && (currentState.board[i-1][j-1] != '.') && currentState.board[i-2][j-2] == '.'){ //jump north west
            char[][] newBoard = currentState.copyBoard();
            newBoard[i-2][j-2] = currentState.playerColor;
            newBoard[i][j] = '.';
            if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i-2,j-2);
                if(!isInCamp(i-2, j-2)){
                    if((isInOpposingCamp(initial_x,initial_y) && isInOpposingCamp(i-2, j-2)) || !isInOpposingCamp(initial_x,initial_y)){
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                    }
                }
                jumpWhitePieces(i-2, j-2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard,
                            currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), initial_x, initial_y);
            }
        }
        if((j >= 2) && (currentState.board[i][j-1] != '.') && currentState.board[i][j-2] == '.'){ //jump north
            char[][] newBoard = currentState.copyBoard();
            newBoard[i][j-2] = currentState.playerColor;
            newBoard[i][j] = '.';
            if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i,j-2);
                if(!isInCamp(i, j-2)){
                    if((isInOpposingCamp(initial_x,initial_y) && isInOpposingCamp(i, j-2)) || !isInOpposingCamp(initial_x,initial_y)){
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                    }
                }
                jumpWhitePieces(i, j-2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard,
                        currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), initial_x, initial_y);
            }
        }
        if((i <= 13) && (j >= 2) && (currentState.board[i+1][j-1] != '.') && currentState.board[i+2][j-2] == '.'){ //jump north east
            char[][] newBoard = currentState.copyBoard();
            newBoard[i+2][j-2] = currentState.playerColor;
            newBoard[i][j] = '.';
            if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i+2,j-2);
                if(!isInCamp(i+2, j-2)){
                    if((isInOpposingCamp(initial_x,initial_y) && isInOpposingCamp(i+2, j-2)) || !isInOpposingCamp(initial_x,initial_y)){
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                    }
                }
                jumpWhitePieces(i+2, j-2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard,
                        currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), initial_x, initial_y);
            }
        }
        if((i >= 2) && (currentState.board[i-1][j] != '.') && currentState.board[i-2][j] == '.'){ //jump west
            char[][] newBoard = currentState.copyBoard();
            newBoard[i-2][j] = currentState.playerColor;
            newBoard[i][j] = '.';
            if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i-2,j);
                if(!isInCamp(i-2, j)){
                    if((isInOpposingCamp(initial_x,initial_y) && isInOpposingCamp(i-2, j)) || !isInOpposingCamp(initial_x,initial_y)){
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                    }
                }
                jumpWhitePieces(i-2, j, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard,
                        currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), initial_x, initial_y);
            }
        }
        if((i >= 2) && (j <= 13) && (currentState.board[i-1][j+1] != '.') && currentState.board[i-2][j+2] == '.'){ //jump south west
            char[][] newBoard = currentState.copyBoard();
            newBoard[i-2][j+2] = currentState.playerColor;
            newBoard[i][j] = '.';
            if(!sameBoardInPreState(currentState, newBoard) && !containsSameBoard(nextState, newBoard)){
                point nextPreLocation = new point(i,j);
                point nextNewLocation = new point(i-2,j+2);
                if(!isInCamp(i-2, j+2)){
                    if((isInOpposingCamp(initial_x,initial_y) && isInOpposingCamp(i-2, j+2)) || !isInOpposingCamp(initial_x,initial_y)){
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, currentState, currentState.depth+1, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth));
                    }
                }
                jumpWhitePieces(i-2, j+2, nextState, newPlayerColor, new state(isBlack, playerColor, newBoard,
                        currentState, currentState.depth, 'J', nextPreLocation, nextNewLocation, currentState.maxDepth), initial_x, initial_y);
            }
        }
    }

    void getNextStateSpecial(ArrayList<state> nextState, ArrayList<point> outsidePieces, char newPlayerColor){
        int x0 = outsidePieces.get(0).x;
        int y0 = outsidePieces.get(0).y;
        int x1 = outsidePieces.get(1).x;
        int y1 = outsidePieces.get(1).y;
        if(isBlack){
            if(x0 == y1 && y0 == x1){
                int i = x0;
                int j = y0;
                if((i <= 14) && board[i+1][j] == '.' && !isInCamp(i+1, j)){ //move east
                    if((isInOpposingCamp(i,j) && isInOpposingCamp(i+1, j)) || !isInOpposingCamp(i,j)){
                        char[][] newBoard = copyBoard();
                        newBoard[i+1][j] = playerColor;
                        newBoard[i][j] = '.';
                        point nextPreLocation = new point(i,j);
                        point nextNewLocation = new point(i+1,j);
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                    }
                }
            }
            int dist1 = Math.max(15-x0, 15-y0);
            int dist2 = Math.max(15-x1, 15-y1);
            int currentDist = Math.max(Math.abs(x0-x1), Math.abs(y0-y1));
            if(currentDist == 1){//two points are adjacent
                return;
            }
            if(dist1 == dist2){
                if(currentDist == 1){//two points are adjacent
                    return;
                }
                else{
                    int dist3 = Math.min(15-x0, 15-y0);
                    int dist4 = Math.min(15-x1, 15-y1);
                    if(dist3 == dist4){
                        return;
                    }
                    else if(dist3 > dist4){
                        int i = x0;
                        int j = y0;
                        if(Math.max(Math.abs(x0+1-x1), Math.abs(y0-y1)) < currentDist){ //move east
                            if((i <= 14) && board[i+1][j] == '.' && !isInCamp(i+1, j)){ //move east
                                if((isInOpposingCamp(i,j) && isInOpposingCamp(i+1, j)) || !isInOpposingCamp(i,j)){
                                    char[][] newBoard = copyBoard();
                                    newBoard[i+1][j] = playerColor;
                                    newBoard[i][j] = '.';
                                    point nextPreLocation = new point(i,j);
                                    point nextNewLocation = new point(i+1,j);
                                    nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                                }
                            }
                        }
                        if(Math.max(Math.abs(x0-x1), Math.abs(y0+1-y1)) < currentDist){ //move south
                            if((j <= 14) && board[i][j+1] == '.' && !isInCamp(i, j+1)){ //move south
                                if((isInOpposingCamp(i,j) && isInOpposingCamp(i, j+1)) || !isInOpposingCamp(i,j)){
                                    char[][] newBoard = copyBoard();
                                    newBoard[i][j+1] = playerColor;
                                    newBoard[i][j] = '.';
                                    point nextPreLocation = new point(i,j);
                                    point nextNewLocation = new point(i,j+1);
                                    nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                                }
                            }
                        }
                        return;
                    }
                    else{
                        int i = x1;
                        int j = y1;
                        if(Math.max(Math.abs(x1+1-x0), Math.abs(y1-y0)) < currentDist){ //move east
                            if((i <= 14) && board[i+1][j] == '.' && !isInCamp(i+1, j)){ //move east
                                if((isInOpposingCamp(i,j) && isInOpposingCamp(i+1, j)) || !isInOpposingCamp(i,j)){
                                    char[][] newBoard = copyBoard();
                                    newBoard[i+1][j] = playerColor;
                                    newBoard[i][j] = '.';
                                    point nextPreLocation = new point(i,j);
                                    point nextNewLocation = new point(i+1,j);
                                    nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                                }
                            }
                        }
                        if(Math.max(Math.abs(x1-x0), Math.abs(y1+1-y0)) < currentDist){ //move south
                            if((j <= 14) && board[i][j+1] == '.' && !isInCamp(i, j+1)){ //move south
                                if((isInOpposingCamp(i,j) && isInOpposingCamp(i, j+1)) || !isInOpposingCamp(i,j)){
                                    char[][] newBoard = copyBoard();
                                    newBoard[i][j+1] = playerColor;
                                    newBoard[i][j] = '.';
                                    point nextPreLocation = new point(i,j);
                                    point nextNewLocation = new point(i,j+1);
                                    nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                                }
                            }
                        }
                    }
                }
            }
            else if(dist1 > dist2){
                if(currentDist == 1){
                    if(x0 == x1){
                        int i = x1;
                        int j = y1;
                        if((i <= 14) && board[i+1][j] == '.' && !isInCamp(i+1, j)){ //move east
                            if((isInOpposingCamp(i,j) && isInOpposingCamp(i+1, j)) || !isInOpposingCamp(i,j)){
                                char[][] newBoard = copyBoard();
                                newBoard[i+1][j] = playerColor;
                                newBoard[i][j] = '.';
                                point nextPreLocation = new point(i,j);
                                point nextNewLocation = new point(i+1,j);
                                nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                                return;
                            }
                        }
                    }
                    if(y0 == y1){
                        int i = x1;
                        int j = y1;
                        if((i >= 1) && (j <= 14) && board[i-1][j+1] == '.'){ //move south west
                            char[][] newBoard = copyBoard();
                            newBoard[i-1][j+1] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i-1,j+1);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth) );
                            return;
                        }
                    }
                }
                int i = x0;
                int j = y0;
                if(Math.max(Math.abs(x0+1-x1), Math.abs(y0-1-y1)) < currentDist){ //move north east
                    if((i <= 14) && (j >= 1) && board[i+1][j-1] == '.'){ //move north east
                        char[][] newBoard = copyBoard();
                        newBoard[i+1][j-1] = playerColor;
                        newBoard[i][j] = '.';
                        point nextPreLocation = new point(i,j);
                        point nextNewLocation = new point(i+1,j-1);
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                    }
                }
                if(Math.max(Math.abs(x0+1-x1), Math.abs(y0-y1)) < currentDist){ //move east
                    if((i <= 14) && board[i+1][j] == '.' && !isInCamp(i+1, j)){ //move east
                        if((isInOpposingCamp(i,j) && isInOpposingCamp(i+1, j)) || !isInOpposingCamp(i,j)){
                            char[][] newBoard = copyBoard();
                            newBoard[i+1][j] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i+1,j);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                        }
                    }
                }
                if(Math.max(Math.abs(x0-1-x1), Math.abs(y0+1-y1)) < currentDist){ //move south west
                    if((i >= 1) && (j <= 14) && board[i-1][j+1] == '.'){ //move south west
                        char[][] newBoard = copyBoard();
                        newBoard[i-1][j+1] = playerColor;
                        newBoard[i][j] = '.';
                        point nextPreLocation = new point(i,j);
                        point nextNewLocation = new point(i-1,j+1);
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth) );
                    }
                }
                if(Math.max(Math.abs(x0-x1), Math.abs(y0+1-y1)) < currentDist){ //move south
                    if((j <= 14) && board[i][j+1] == '.' && !isInCamp(i, j+1)){ //move south
                        if((isInOpposingCamp(i,j) && isInOpposingCamp(i, j+1)) || !isInOpposingCamp(i,j)){
                            char[][] newBoard = copyBoard();
                            newBoard[i][j+1] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i,j+1);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                        }
                    }
                }
                if(Math.max(Math.abs(x0+1-x1), Math.abs(y0+1-y1)) < currentDist){ //move south east
                    if((i <= 14) && (j <= 14) && board[i+1][j+1] == '.' && !isInCamp(i+1, j+1)){ //move south east
                        if((isInOpposingCamp(i,j) && isInOpposingCamp(i+1, j+1)) || !isInOpposingCamp(i,j)){
                            char[][] newBoard = copyBoard();
                            newBoard[i+1][j+1] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i+1,j+1);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                        }
                    }
                }
            }
            else{
                if(currentDist == 1){
                    if(x0 == x1){
                        int i = x0;
                        int j = y0;
                        if((i <= 14) && board[i+1][j] == '.' && !isInCamp(i+1, j)){ //move east
                            if((isInOpposingCamp(i,j) && isInOpposingCamp(i+1, j)) || !isInOpposingCamp(i,j)){
                                char[][] newBoard = copyBoard();
                                newBoard[i+1][j] = playerColor;
                                newBoard[i][j] = '.';
                                point nextPreLocation = new point(i,j);
                                point nextNewLocation = new point(i+1,j);
                                nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                                return;
                            }
                        }
                    }
                    if(y0 == y1) {
                        int i = x0;
                        int j = y0;
                        if ((i >= 1) && (j <= 14) && board[i - 1][j + 1] == '.') { //move south west
                            char[][] newBoard = copyBoard();
                            newBoard[i - 1][j + 1] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i, j);
                            point nextNewLocation = new point(i - 1, j + 1);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth + 1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                            return;
                        }
                    }
                }
                int i = x1;
                int j = y1;
                if(Math.max(Math.abs(x1+1-x0), Math.abs(y1-1-y0)) < currentDist){ //move north east
                    if((i <= 14) && (j >= 1) && board[i+1][j-1] == '.'){ //move north east
                        char[][] newBoard = copyBoard();
                        newBoard[i+1][j-1] = playerColor;
                        newBoard[i][j] = '.';
                        point nextPreLocation = new point(i,j);
                        point nextNewLocation = new point(i+1,j-1);
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                    }
                }
                if(Math.max(Math.abs(x1+1-x0), Math.abs(y1-y0)) < currentDist){ //move east
                    if((i <= 14) && board[i+1][j] == '.' && !isInCamp(i+1, j)){ //move east
                        if((isInOpposingCamp(i,j) && isInOpposingCamp(i+1, j)) || !isInOpposingCamp(i,j)){
                            char[][] newBoard = copyBoard();
                            newBoard[i+1][j] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i+1,j);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                        }
                    }
                }
                if(Math.max(Math.abs(x1-1-x0), Math.abs(y1+1-y0)) < currentDist){ //move south west
                    if((i >= 1) && (j <= 14) && board[i-1][j+1] == '.'){ //move south west
                        char[][] newBoard = copyBoard();
                        newBoard[i-1][j+1] = playerColor;
                        newBoard[i][j] = '.';
                        point nextPreLocation = new point(i,j);
                        point nextNewLocation = new point(i-1,j+1);
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth) );
                    }
                }
                if(Math.max(Math.abs(x1-x0), Math.abs(y1+1-y0)) < currentDist){ //move south
                    if((j <= 14) && board[i][j+1] == '.' && !isInCamp(i, j+1)){ //move south
                        if((isInOpposingCamp(i,j) && isInOpposingCamp(i, j+1)) || !isInOpposingCamp(i,j)){
                            char[][] newBoard = copyBoard();
                            newBoard[i][j+1] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i,j+1);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                        }
                    }
                }
                if(Math.max(Math.abs(x1+1-x0), Math.abs(y1+1-y0)) < currentDist){ //move south east
                    if((i <= 14) && (j <= 14) && board[i+1][j+1] == '.' && !isInCamp(i+1, j+1)){ //move south east
                        if((isInOpposingCamp(i,j) && isInOpposingCamp(i+1, j+1)) || !isInOpposingCamp(i,j)){
                            char[][] newBoard = copyBoard();
                            newBoard[i+1][j+1] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i+1,j+1);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                        }
                    }
                }
            }
        }
        else{
            int dist1 = Math.max(x0, y0);
            int dist2 = Math.max(x1, y1);
            int currentDist = Math.max(Math.abs(x0-x1), Math.abs(y0-y1));
            if(x0 == y1 && y0 == x1){
                int i = x1;
                int j = y1;
                if((i >= 1) && board[i-1][j] == '.' && !isInCamp(i-1, j)){ //move west
                    if((isInOpposingCamp(i,j) && isInOpposingCamp(i-1, j)) || !isInOpposingCamp(i,j)){
                        char[][] newBoard = copyBoard();
                        newBoard[i-1][j] = playerColor;
                        newBoard[i][j] = '.';
                        point nextPreLocation = new point(i,j);
                        point nextNewLocation = new point(i-1,j);
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                    }
                }
            }
            if(dist1 == dist2){
                if(currentDist == 1){//two points are adjacent
                    return;
                }
                else{
                    int dist3 = Math.min(x0, y0);
                    int dist4 = Math.min(x1, y1);
                    if(dist3 == dist4){
                        return;
                    }
                    else if(dist3 > dist4){
                        int i = x0;
                        int j = y0;
                        if(Math.max(Math.abs(x0-1-x1), Math.abs(y0-y1)) < currentDist){ //move west
                            if((i >= 1) && board[i-1][j] == '.' && !isInCamp(i-1, j)){ //move west
                                if((isInOpposingCamp(i,j) && isInOpposingCamp(i-1, j)) || !isInOpposingCamp(i,j)){
                                    char[][] newBoard = copyBoard();
                                    newBoard[i-1][j] = playerColor;
                                    newBoard[i][j] = '.';
                                    point nextPreLocation = new point(i,j);
                                    point nextNewLocation = new point(i-1,j);
                                    nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                                }
                            }
                        }
                        if(Math.max(Math.abs(x0-x1), Math.abs(y0-1-y1)) < currentDist){ //move north
                            if((j >= 1) && board[i][j-1] == '.' && !isInCamp(i, j-1)){ //move north
                                if((isInOpposingCamp(i,j) && isInOpposingCamp(i, j-1)) || !isInOpposingCamp(i,j)){
                                    char[][] newBoard = copyBoard();
                                    newBoard[i][j-1] = playerColor;
                                    newBoard[i][j] = '.';
                                    point nextPreLocation = new point(i,j);
                                    point nextNewLocation = new point(i,j-1);
                                    nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                                }
                            }
                        }
                    }
                    else{
                        int i = x1;
                        int j = y1;
                        if(Math.max(Math.abs(x1-1-x0), Math.abs(y1-y0)) < currentDist){ //move west
                            if((i >= 1) && board[i-1][j] == '.' && !isInCamp(i-1, j)){ //move west
                                if((isInOpposingCamp(i,j) && isInOpposingCamp(i-1, j)) || !isInOpposingCamp(i,j)){
                                    char[][] newBoard = copyBoard();
                                    newBoard[i-1][j] = playerColor;
                                    newBoard[i][j] = '.';
                                    point nextPreLocation = new point(i,j);
                                    point nextNewLocation = new point(i-1,j);
                                    nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                                }
                            }
                        }
                        if(Math.max(Math.abs(x1-x0), Math.abs(y1-1-y0)) < currentDist){ //move north
                            if((j >= 1) && board[i][j-1] == '.' && !isInCamp(i, j-1)){ //move north
                                if((isInOpposingCamp(i,j) && isInOpposingCamp(i, j-1)) || !isInOpposingCamp(i,j)){
                                    char[][] newBoard = copyBoard();
                                    newBoard[i][j-1] = playerColor;
                                    newBoard[i][j] = '.';
                                    point nextPreLocation = new point(i,j);
                                    point nextNewLocation = new point(i,j-1);
                                    nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                                }
                            }
                        }
                    }
                }
            }
            else if(dist1 > dist2){
                if(currentDist == 1){
                    if(x0 == x1){
                        int i = x1;
                        int j = y1;
                        if((i >= 1) && board[i-1][j] == '.' && !isInCamp(i-1, j)){ //move west
                            if((isInOpposingCamp(i,j) && isInOpposingCamp(i-1, j)) || !isInOpposingCamp(i,j)){
                                char[][] newBoard = copyBoard();
                                newBoard[i-1][j] = playerColor;
                                newBoard[i][j] = '.';
                                point nextPreLocation = new point(i,j);
                                point nextNewLocation = new point(i-1,j);
                                nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                                return;
                            }
                        }
                    }
                    if(y0 == y1){
                        int i = x1;
                        int j = y1;
                        if((j >= 1) && board[i][j-1] == '.' && !isInCamp(i, j-1)){ //move north
                            if((isInOpposingCamp(i,j) && isInOpposingCamp(i, j-1)) || !isInOpposingCamp(i,j)){
                                char[][] newBoard = copyBoard();
                                newBoard[i][j-1] = playerColor;
                                newBoard[i][j] = '.';
                                point nextPreLocation = new point(i,j);
                                point nextNewLocation = new point(i,j-1);
                                nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                                return;
                            }
                        }
                    }
                }
                int i = x0;
                int j = y0;
                if(Math.max(Math.abs(x0-1-x1), Math.abs(y0-1-y1)) < currentDist){ //move north west
                    if((i >= 1) && (j >= 1) && board[i-1][j-1] == '.' && !isInCamp(i-1, j-1)){ //move north west
                        if((isInOpposingCamp(i,j) && isInOpposingCamp(i-1, j-1)) || !isInOpposingCamp(i,j)){ //can not move pieces outside of the camp if that piece is already in the camp
                            char[][] newBoard = copyBoard();
                            newBoard[i-1][j-1] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i-1,j-1);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                        }
                    }
                }
                if(Math.max(Math.abs(x0-x1), Math.abs(y0-1-y1)) < currentDist){ //move north
                    if((j >= 1) && board[i][j-1] == '.' && !isInCamp(i, j-1)){ //move north
                        if((isInOpposingCamp(i,j) && isInOpposingCamp(i, j-1)) || !isInOpposingCamp(i,j)){
                            char[][] newBoard = copyBoard();
                            newBoard[i][j-1] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i,j-1);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                        }
                    }
                }
                if(Math.max(Math.abs(x0+1-x1), Math.abs(y0-1-y1)) < currentDist){ //move north east
                    if((i <= 14) && (j >= 1) && board[i+1][j-1] == '.'){ //move north east
                        char[][] newBoard = copyBoard();
                        newBoard[i+1][j-1] = playerColor;
                        newBoard[i][j] = '.';
                        point nextPreLocation = new point(i,j);
                        point nextNewLocation = new point(i+1,j-1);
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                    }
                }
                if(Math.max(Math.abs(x0-1-x1), Math.abs(y0-y1)) < currentDist){ //move west
                    if((i >= 1) && board[i-1][j] == '.' && !isInCamp(i-1, j)){ //move west
                        if((isInOpposingCamp(i,j) && isInOpposingCamp(i-1, j)) || !isInOpposingCamp(i,j)){
                            char[][] newBoard = copyBoard();
                            newBoard[i-1][j] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i-1,j);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                        }
                    }
                }
                if(Math.max(Math.abs(x0-1-x1), Math.abs(y0+1-y1)) < currentDist){ //move south west
                    if((i >= 11) && (j <= 14) && board[i-1][j+1] == '.'){ //move south west
                        char[][] newBoard = copyBoard();
                        newBoard[i-1][j+1] = playerColor;
                        newBoard[i][j] = '.';
                        point nextPreLocation = new point(i,j);
                        point nextNewLocation = new point(i-1,j+1);
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                    }
                }
            }
            else{
                if(currentDist == 1){
                    if(x0 == x1){
                        int i = x0;
                        int j = y0;
                        if((i >= 1) && board[i-1][j] == '.' && !isInCamp(i-1, j)){ //move west
                            if((isInOpposingCamp(i,j) && isInOpposingCamp(i-1, j)) || !isInOpposingCamp(i,j)){
                                char[][] newBoard = copyBoard();
                                newBoard[i-1][j] = playerColor;
                                newBoard[i][j] = '.';
                                point nextPreLocation = new point(i,j);
                                point nextNewLocation = new point(i-1,j);
                                nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                                return;
                            }
                        }
                    }
                    if(y0 == y1){
                        int i = x0;
                        int j = y0;
                        if((j >= 1) && board[i][j-1] == '.' && !isInCamp(i, j-1)){ //move north
                            if((isInOpposingCamp(i,j) && isInOpposingCamp(i, j-1)) || !isInOpposingCamp(i,j)){
                                char[][] newBoard = copyBoard();
                                newBoard[i][j-1] = playerColor;
                                newBoard[i][j] = '.';
                                point nextPreLocation = new point(i,j);
                                point nextNewLocation = new point(i,j-1);
                                nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                                return;
                            }
                        }
                    }
                }
                int i = x1;
                int j = y1;
                if(Math.max(Math.abs(x1-1-x0), Math.abs(y1-1-y0)) < currentDist){ //move north west
                    if((i >= 1) && (j >= 1) && board[i-1][j-1] == '.' && !isInCamp(i-1, j-1)){ //move north west
                        if((isInOpposingCamp(i,j) && isInOpposingCamp(i-1, j-1)) || !isInOpposingCamp(i,j)){ //can not move pieces outside of the camp if that piece is already in the camp
                            char[][] newBoard = copyBoard();
                            newBoard[i-1][j-1] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i-1,j-1);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                        }
                    }
                }
                if(Math.max(Math.abs(x1-x0), Math.abs(y1-1-y0)) < currentDist){ //move north
                    if((j >= 1) && board[i][j-1] == '.' && !isInCamp(i, j-1)){ //move north
                        if((isInOpposingCamp(i,j) && isInOpposingCamp(i, j-1)) || !isInOpposingCamp(i,j)){
                            char[][] newBoard = copyBoard();
                            newBoard[i][j-1] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i,j-1);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                        }
                    }
                }
                if(Math.max(Math.abs(x1+1-x0), Math.abs(y1-1-y0)) < currentDist){ //move north east
                    if((i <= 14) && (j >= 1) && board[i+1][j-1] == '.'){ //move north east
                        char[][] newBoard = copyBoard();
                        newBoard[i+1][j-1] = playerColor;
                        newBoard[i][j] = '.';
                        point nextPreLocation = new point(i,j);
                        point nextNewLocation = new point(i+1,j-1);
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                    }
                }
                if(Math.max(Math.abs(x1-1-x0), Math.abs(y1-y0)) < currentDist){ //move west
                    if((i >= 1) && board[i-1][j] == '.' && !isInCamp(i-1, j)){ //move west
                        if((isInOpposingCamp(i,j) && isInOpposingCamp(i-1, j)) || !isInOpposingCamp(i,j)){
                            char[][] newBoard = copyBoard();
                            newBoard[i-1][j] = playerColor;
                            newBoard[i][j] = '.';
                            point nextPreLocation = new point(i,j);
                            point nextNewLocation = new point(i-1,j);
                            nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                        }
                    }
                }
                if(Math.max(Math.abs(x1-1-x0), Math.abs(y1+1-y0)) < currentDist){ //move south west
                    if((i >= 1) && (j <= 14) && board[i-1][j+1] == '.'){ //move south west
                        char[][] newBoard = copyBoard();
                        newBoard[i-1][j+1] = playerColor;
                        newBoard[i][j] = '.';
                        point nextPreLocation = new point(i,j);
                        point nextNewLocation = new point(i-1,j+1);
                        nextState.add(new state(!isBlack, newPlayerColor, newBoard, this, depth+1, 'E', nextPreLocation, nextNewLocation, this.maxDepth));
                    }
                }
            }
        }
    }

    double calculateScore(int v){
        double score = 0;
        for(int i = 15; i >= v; i--){
            score = score + Math.pow(i, 2);
        }
        return score;
    }
}
