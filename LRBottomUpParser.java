import java.util.ArrayList;
import java.io.*;
public class LRBottomUpParser {


    String [][] table = new String[12][9];      //LR Parsing Table.
    ArrayList<String> stack = new ArrayList<String>();
    ArrayList<String> input = new ArrayList<String>();
    ArrayList<String> action = new ArrayList<String>();
    String stackString,inputString,actionString;
    String fileName;


    public  LRBottomUpParser(String input,String fileName){        //Constructor to initialize the table,stack,input,action.
        for(int i = 0;i<12;i++)
            for (int j = 0 ;j<9;j++)
                table[i][j] = "X";


        table[0][0] = "Shift 5";
        table[0][3] = "Shift 4";
        table[0][6] = "1";
        table[0][7] = "2";
        table[0][8] = "3";


        table[1][1] = "Shift 6";
        table[1][5] = "Accept";

        table[2][1] = "Reduce 2";
        table[2][2] = "Shift 7";
        table[2][4] = "Reduce 2";
        table[2][5] = "Reduce 2";

        table[3][1] = "Reduce 4";
        table[3][2] = "Reduce 4";
        table[3][4] = "Reduce 4";
        table[3][5] = "Reduce 4";

        table[4][0] = "Shift 5";
        table[4][3] = "Shift 4";
        table[4][6] = "8";
        table[4][7] = "2";
        table[4][8] = "3";

        table[5][1] = "Reduce 6";
        table[5][2] = "Reduce 6";
        table[5][4] = "Reduce 6";
        table[5][5] = "Reduce 6";

        table[6][0] = "Shift 5";
        table[6][3] = "Shift 4";
        table[6][7] = "9";
        table[6][8] = "3";

        table[7][0] = "Shift 5";
        table[7][3] = "Shift 4";
        table[7][8] = "10";

        table[8][1] = "Shift 6";
        table[8][4] = "Shift 11";

        table[9][1] = "Reduce 1";
        table[9][2] = "Shift 7";
        table[9][4] = "Reduce 1";
        table[9][5] = "Reduce 1";

        table[10][1] = "Reduce 3";
        table[10][2] = "Reduce 3";
        table[10][4] = "Reduce 3";
        table[10][5] = "Reduce 3";

        table[11][1] = "Reduce 5";
        table[11][2] = "Reduce 5";
        table[11][4] = "Reduce 5";
        table[11][5] = "Reduce 5";

        stackString = "0";
        inputString = input;
        actionString = this.findAction();
        stack.add("0");
        this.input.add(input);
        action.add(actionString);
        this.fileName = fileName;
    }




    void bottomUpParser(){      //The head method that operates and processes every iteration.
        while (true){
            if(actionString.equals("Accept") ){
                accept();
                break;

            }
            if(actionString.equals("X")){
                action.set(action.size()-1, "ERROR");
                error();
                break;

            }
            else if(actionString.charAt(0) == 'S'){
                shift(actionString);
                actionString =  findAction();
                stack.add(stackString);
                input.add(inputString);
                action.add(actionString);
            }
            else if(actionString.charAt(0) == 'R'){
                reduce(actionString);
                actionString =  findAction();
                stack.add(stackString);
                input.add(inputString);
                action.add(actionString);
            }



        }

    }

    void reduce(String reduce){         //If the current action is Reduce, this method adds the correct element to the stack.
        int reduceNo = toInteger(reduce.substring(7));
        if (reduceNo == 1 ){
            stackString = stackString.substring(0,stackString.lastIndexOf('E'));
            stackString += "E";
            int row = parseRowByReducer(stackString);
            int col = findIndex("E");
            stackString += table[row][col];
        }
        if (reduceNo == 2 ){
            stackString = stackString.substring(0,stackString.lastIndexOf('T'));
            stackString += "E";
            int row = parseRowByReducer(stackString);
            int col = findIndex("E");
            stackString += table[row][col];
        }
        if (reduceNo == 3 ){
            stackString = stackString.substring(0,stackString.lastIndexOf('T'));
            stackString += "T";
            int row = parseRowByReducer(stackString);
            int col = findIndex("T");
            stackString += table[row][col];
        }
        if (reduceNo == 4 ){
            stackString = stackString.substring(0,stackString.lastIndexOf('F'));
            stackString += "T";
            int row = parseRowByReducer(stackString);
            int col = findIndex("T");
            stackString += table[row][col];
        }
        if (reduceNo == 5 ){        // BUNA DIKKATLI BAK
            stackString = stackString.substring(0,stackString.lastIndexOf('('));
            stackString += "F";
            int row = parseRowByReducer(stackString);
            int col = findIndex("F");
            stackString += table[row][col];
        }
        if (reduceNo == 6 ){
            stackString = stackString.substring(0,stackString.lastIndexOf('i'));
            stackString += "F";
            int row = parseRowByReducer(stackString);
            int col = findIndex("F");
            stackString += table[row][col];
        }


    }

    void shift(String shift){           //The method shifts from input to stack
        shift = shift.substring(6);
        if(inputString.charAt(0) == 'i')  {         //shift id
            stackString += "id";
            stackString += shift;
            inputString = inputString.substring(2);
        }
        else if(inputString.charAt(0) == '+')  {         //shift +
            stackString += "+";
            stackString += shift;
            inputString = inputString.substring(1);
        }
        else if(inputString.charAt(0) == '*')  {         //shift *
            stackString += "*";
            stackString += shift;
            inputString = inputString.substring(1);
        }
        else if(inputString.charAt(0) == '(')  {         //shift (
            stackString += "(";
            stackString += shift;
            inputString = inputString.substring(1);
        }
        else if(inputString.charAt(0) == ')')  {         //shift )
            stackString += ")";
            stackString += shift;
            inputString = inputString.substring(1);
        }


    }

    String findAction(){                  // The method that finds correct action using LR table lookup
        int row = parseRowByAction(stackString);
        int col;
        if(inputString.charAt(0) == '$')
            col = findIndex("$");
        else if(inputString.charAt(0) == 'i')
            col = findIndex("id");
        else if(inputString.charAt(0) == '+')
            col = findIndex("+");
        else if(inputString.charAt(0) == '*')
            col = findIndex("*");
        else if(inputString.charAt(0) == '(')
            col = findIndex("(");
        else if(inputString.charAt(0) == ')')
            col = findIndex(")");

        else {
            col = -1;
            error();
        }

        return table[row][col];


    }


    void printStates(){             //Prints the stack, input, action history to the output file.
        if( (stack.size() != input.size()) && (stack.size() != action.size()) && input.size() != action.size()){
            System.out.println("Error occurred.");
            System.exit(1);
        }

        PrintWriter writer = null;
        try  {
            writer = new PrintWriter(new FileWriter(fileName));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }


        writer.print("Stack \t\t    Input         \tAction");

        for (int i = 0;i<stack.size();i++){

            int stackElementSize = stack.get(i).length();
            int inputElementSize = input.get(i).length();
            int actionElementSize = action.get(i).length();

            writer.println();
            writer.print(stack.get(i));
            for(int j = 0;j<20-stackElementSize;j++){
                writer.print(" ");
            }
            writer.print(input.get(i));
            for(int j = 0;j<20-inputElementSize;j++){
                writer.print(" ");
            }
            writer.print(action.get(i));

        }

        writer.close();

    }



    int toInteger(String s){        // String to Integer conversion method
        return Integer.parseInt(s);
    }

    int findIndex(String state){        //Returns the correct index of state.
        if (state.equals("id"))
            return 0;

        else if (state.equals("+"))
            return 1;
        else if (state.equals("*"))
            return 2;
        else if (state.equals("("))
            return 3;
        else if (state.equals(")"))
            return 4;
        else if (state.equals("$"))
            return 5;
        else if (state.equals("E"))
            return 6;
        else if (state.equals("T"))
            return 7;
        else if (state.equals("F"))
            return 8;
        else return -1;
    }

    int parseRowByReducer(String current){  //Method that parses the "number part" that Reduce generates.
        current = current.substring( 0,current.length()-1) ;
        int i = current.length();
        while (i > 0 && Character.isDigit(current.charAt(i-1))) {
            i--;
        }

        return toInteger(current.substring(i));
    }





    int parseRowByAction(String current){       //Method that parses the integer part of the stack to use find the next action.

        if (stackString.equals("0"))
            return 0;

        int i = current.length();
        while (i > 0 && Character.isDigit(current.charAt(i - 1))) {
            i--;
        }
        return toInteger(current.substring(i));

    }
    void accept(){
        System.out.println("The input has been parsed successfully");
        printStates();
    }

    void error(){
        System.out.println("Error occurred.");
        printStates();
    }



    public static void main(String[] args) {
        LRBottomUpParser o = new LRBottomUpParser(args[0],args[1]);
        o.bottomUpParser();
    }

}
