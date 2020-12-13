import java.io.*;
import java.util.ArrayList;

public class Agent {
    String mode;
    double remainingTime;
    String playerColor;
    char playColorChar;
    boolean isBlack;
    char[][] board;



    public Agent(){
        board = new char[16][16];
    }

    public void nextMovement(){
        readFile();
        if(this.mode.equals("SINGLE")){
            this.singleMode(); //run single mode
        }
        else if(this.mode.equals("GAME")){
            if(this.remainingTime < 25){ //choose game mode based on remaining time
                this.gameMode(1);
            }
            else{
                this.gameMode(3);
            }
        }
        try{
            Thread.sleep(500);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void readFile(){
        File file = new File("input.txt");
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(file));
            String currentLine;
            int line = 1;
            while((currentLine = reader.readLine()) != null){
                switch (line){
                    case 1:
                        String[] m = currentLine.split("\\s+");
                        mode = m[0];
                        if(!mode.equals("SINGLE") && !mode.equals("GAME")){
                            System.out.println("invalid mode");
                            System.exit(0);
                        }
                        break;
                    case 2:
                        String[] p = currentLine.split("\\s+");
                        playerColor = p[0];
                        if(playerColor.equals("BLACK")){
                            isBlack = true;
                            playColorChar = 'B';
                        }
                        else if(playerColor.equals("WHITE")){
                            isBlack = false;
                            playColorChar = 'W';
                        }
                        else{
                            System.out.println("invalid color");
                            System.exit(0);
                        }
                        break;
                    case 3:
                        String[] r = currentLine.split("\\s+");
                        remainingTime = Double.parseDouble(r[0]);
                        break;
                    default:
                        if(line <= 19){
                            char[] temp = currentLine.toCharArray();
                            for(int i = 0; i < 16; i++){
                                board[i][line - 4] = temp[i];
                            }
                        }
                        else{
                            System.out.println("invalid input");
                        }
                }
                line++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void writeFile(state initialState, state state, state endState, FileWriter fw){
        if(state.preState != null && state.preState != initialState){
            writeFile(initialState, state.preState, endState, fw);
        }
        try{
            fw.write(state.moveMethod);
            fw.write(" ");
            fw.write(state.preLocation.x + "," + state.preLocation.y);
            fw.write(" ");
            fw.write(state.newLocation.x + "," + state.newLocation.y);
            if(state != endState){
                fw.write("\n"); //next line
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public boolean singleMode(){
        state initialState = new state(isBlack, playColorChar, board, null, 0, 'X', null, null, 1);
        state nextState = alphaBetaSearch(initialState);
        if(nextState == null){
            return false;
        }
        try{
            FileWriter fw = new FileWriter("output.txt");
            writeFile(initialState, nextState, nextState, fw);
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return true;
    }

    public boolean gameMode(int depth){
        state initialState = new state(isBlack, playColorChar, board, null, 0, 'X', null, null, depth);
        state nextState = alphaBetaSearch(initialState);
        if(nextState == null){
            return false;
        }
        try{
            FileWriter fw = new FileWriter("output.txt");
            writeFile(initialState, nextState, nextState, fw);
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return true;
    }

    public double maxValue(state state, double alpha, double beta){
        if(state.isTerminal()){
            return state.getUtility();
        }
        double v = -Double.MAX_VALUE;
        ArrayList<state> nextState = state.getNextState();
        for(int i = 0; i < nextState.size(); i++){
            v = Math.max(v, minValue(nextState.get(i), alpha, beta));
            if(v >= beta){
                return v;
            }
            alpha = Math.max(alpha, v);
        }
        return v;
    }

    public double minValue(state state, double alpha, double beta){
        if(state.isTerminal()){
            return state.getUtility();
        }
        double v = Double.MAX_VALUE;
        ArrayList<state> nextState = state.getNextState();
        for(int i = 0; i < nextState.size(); i++){
            v = Math.min(v, maxValue(nextState.get(i), alpha, beta));
            if(v <= alpha){
                return v;
            }
            beta = Math.min(beta, v);
        }
        return v;
    }

    public state alphaBetaSearch(state state){
        ArrayList<state> nextState = state.getNextState();
        if(nextState.size() == 0){
            return null;
        }
        double alpha = -Double.MAX_VALUE;
        double beta = Double.MAX_VALUE;
        if(isBlack){
            double v = -Double.MAX_VALUE;
            int maxIndex = -1;
            for(int i = 0; i < nextState.size(); i++){
                double newV = minValue(nextState.get(i), alpha, beta);
                if(newV > v){
                    v = newV;
                    maxIndex = i;
                }
                if(v >= beta){
                    return nextState.get(maxIndex);
                }
                alpha = Math.max(alpha, v);
            }
            return nextState.get(maxIndex);
        }
        else{
            double v = Double.MAX_VALUE;
            int minIndex = -1;
            for(int i = 0; i < nextState.size(); i++){
                double newV = maxValue(nextState.get(i), alpha, beta);
                if(newV < v){
                    v = newV;
                    minIndex = i;
                }
                if(v <= alpha){
                    return nextState.get(minIndex);
                }
                beta = Math.min(beta, v);
            }
            return nextState.get(minIndex);
        }
    }
}
