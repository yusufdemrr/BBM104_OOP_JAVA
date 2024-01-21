import java.util.Arrays;

public class Main {
    public static char temp ;
    public static char temp2 ;
    public static int counter = -1;
    static int point = 0;

    static void points(char a) {
        if (a == 'B') {
            point = point - 5;
            temp = 'X';
            temp2 = 'X';
        } else if (a == 'Y') {
            point = point + 5;
            temp = 'X';
            temp2 = 'X';
        } else if (a == 'R') {
            point = point + 10;
            temp = 'X';
            temp2 = 'X';
        }
    }



    public static void main(String[] args) {

        String boardarg = args[0];
        String movearg = args[1];

        FileInput fileInput = new FileInput();
        String[] board = fileInput.readFile(boardarg, true, true);
        String[] movements = fileInput.readFile(movearg, true, true);
//        System.out.println(Arrays.toString(board));
//        System.out.println(Arrays.toString(movements));

        String movements_string = movements[0];
        String[] move = movements_string.split(" ");


        // 2D ARRAY
        int rows = board.length;
        int cols = board[0].split("\\s+").length;
        char[][] gameBoard = new char[rows][cols];

        // Convert one dimensional array to 2 dimensional array
        for (int i = 0; i < rows; i++) {
            String[] chars = board[i].split("\\s+");
            for (int j = 0; j < cols; j++) {
                gameBoard[i][j] = chars[j].charAt(0);
            }
        }

        FileOutput fileOutput = new FileOutput();
//        System.out.println("Game board:");
        fileOutput.writeToFile("output.txt","Game board:",false,true);
        // Print the gameboard
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
//                    System.out.print(gameBoard[i][j] + " ");
                    fileOutput.writeToFile("output.txt",gameBoard[i][j] + " ",true,false);
                }
//                System.out.println();
                fileOutput.writeToFile("output.txt","",true,true);
            }
//        System.out.println();
        fileOutput.writeToFile("output.txt","",true,true);

//      Locating the star
        int starRow = -1;
        int starCol = -1;
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] == '*') {
                    starRow = i;
                    starCol = j;
                    break;
                }
            }
            if (starRow != -1 && starCol != -1) {
                break;
            }
        }


        boolean game = true;

        for (String step : move) {
            if (step.equals("U")) {
                counter++;
                if (starRow > 0) {
                    temp = gameBoard[starRow - 1][starCol];
                    if (temp == 'W'){
                        if (starRow == gameBoard.length - 1){
                            if (gameBoard[0][starCol] == 'H'){
                                gameBoard[starRow][starCol] = ' ';
                                starRow = 0;
                                game = false;
                                break;
                            } else {
                                temp2 = gameBoard[0][starCol];
                                points(temp2);
                                gameBoard[0][starCol] = '*';
                                gameBoard[starRow][starCol] = temp2;
                                starRow = 0;
                            }
                        } else {
                            temp2 = gameBoard[starRow + 1][starCol];
                            points(temp2);
                            gameBoard[starRow + 1][starCol] = '*';
                            gameBoard[starRow][starCol] = temp2;
                            starRow++;
                        }
                    } else if (temp == 'H') {
                        gameBoard[starRow][starCol] = ' ';
                        starRow--;
                        game = false;
                        break;
                    } else {
                        points(temp);
                        gameBoard[starRow - 1][starCol] = '*';
                        gameBoard[starRow][starCol] = temp;
                        starRow--;
                    }
                } else {
                    temp = gameBoard[gameBoard.length - 1][starCol];
                    if (temp == 'W'){
                        temp2 = gameBoard[starRow + 1][starCol];
                        points(temp2);
                        gameBoard[starRow + 1][starCol] = '*';
                        gameBoard[starRow][starCol] = temp2;
                        starRow++;
                    } else if (temp == 'H') {
                        gameBoard[starRow][starCol] = ' ';
                        starRow = gameBoard.length - 1;
                        game = false;
                        break;
                    } else {
                        points(temp);
                        gameBoard[gameBoard.length - 1][starCol] = '*';
                        gameBoard[starRow][starCol] = temp;
                        starRow = gameBoard.length - 1;
                    }
                }
            } else if (step.equals("D")) {
                counter++;
                if (starRow < gameBoard.length - 1) {
                    temp = gameBoard[starRow + 1][starCol];
                    if (temp == 'W'){
                        if(starRow == 0){
                            if (gameBoard[gameBoard.length-1][starCol] == 'H'){
                                gameBoard[starRow][starCol] = ' ';
                                starRow = gameBoard.length - 1;
                                game = false;
                                break;
                            } else {
                                temp2 = gameBoard[gameBoard.length-1][starCol];
                                points(temp2);
                                gameBoard[gameBoard.length-1][starCol] = '*';
                                gameBoard[starRow][starCol] = temp2;
                                starRow = gameBoard.length -1;
                            }
                        } else {
                            temp2 = gameBoard[starRow - 1][starCol];
                            points(temp2);
                            gameBoard[starRow - 1][starCol] = '*';
                            gameBoard[starRow][starCol] = temp2;
                            starRow--;
                        }
                    } else if (temp == 'H') {
                        gameBoard[starRow][starCol] = ' ';
                        starRow++;
                        game = false;
                        break;
                    } else {
                        points(temp);
                        gameBoard[starRow + 1][starCol] = '*';
                        gameBoard[starRow][starCol] = temp;
                        starRow++;
                    }
                } else {
                    temp = gameBoard[0][starCol];
                    if (temp == 'W'){
                        temp2 = gameBoard[starRow - 1][starCol];
                        points(temp2);
                        gameBoard[starRow - 1][starCol] = '*';
                        gameBoard[starRow][starCol] = temp2;
                        starRow--;
                    } else if (temp == 'H') {
                        gameBoard[starRow][starCol] = ' ';
                        starRow = 0;
                        game = false;
                        break;
                    } else {
                        points(temp);
                        gameBoard[0][starCol] = '*';
                        gameBoard[starRow][starCol] = temp;
                        starRow = 0;
                    }
                }
            } else if (step.equals("R")) {
                counter++;
                if (starCol < gameBoard[starRow].length - 1) {
                    temp = gameBoard[starRow][starCol + 1];
                    if (temp == 'W'){
                        if(starCol == 0){
                            if(gameBoard[starRow][gameBoard[0].length-1] == 'H'){
                                gameBoard[starRow][starCol] = ' ';
                                starCol = gameBoard[0].length-1;
                                game = false;
                                break;
                            } else {
                                temp2 = gameBoard[starRow][gameBoard[0].length-1];
                                points(temp2);
                                gameBoard[starRow][gameBoard[0].length-1] = '*';
                                gameBoard[starRow][starCol] = temp2;
                                starCol = gameBoard[0].length-1;
                            }
                        } else {
                            temp2 = gameBoard[starRow][starCol - 1];
                            points(temp2);
                            gameBoard[starRow][starCol - 1] = '*';
                            gameBoard[starRow][starCol] = temp2;
                            starCol--;
                        }
                    } else if (temp == 'H') {
                        gameBoard[starRow][starCol] = ' ';
                        starCol++;
                        game = false;
                        break;
                    } else {
                        points(temp);
                        gameBoard[starRow][starCol + 1] = '*';
                        gameBoard[starRow][starCol] = temp;
                        starCol++;
                    }
                } else {
                    temp = gameBoard[starRow][0];
                    if (temp == 'W'){
                        temp2 = gameBoard[starRow][starCol - 1];
                        points(temp2);
                        gameBoard[starRow][starCol - 1] = '*';
                        gameBoard[starRow][starCol] = temp2;
                        starCol--;
                    } else if (temp == 'H') {
                        gameBoard[starRow][starCol] = ' ';
                        starCol = 0;
                        game = false;
                        break;
                    } else {
                        points(temp);
                        gameBoard[starRow][0] = '*';
                        gameBoard[starRow][starCol] = temp;
                        starCol = 0;
                    }
                }
            } else if (step.equals("L")) {
                counter++;
                if (starCol > 0) {
                    temp = gameBoard[starRow][starCol - 1];
                    if (temp == 'W'){
                        if (starCol == gameBoard[0].length-1){
                            if (gameBoard[starRow][0] == 'H'){
                                gameBoard[starRow][starCol] = ' ';
                                starCol = 0;
                                game = false;
                                break;
                            } else {
                                temp2 = gameBoard[starRow][0];
                                points(temp2);
                                gameBoard[starRow][0] = '*';
                                gameBoard[starRow][starCol] = temp2;
                                starCol = 0;
                            }
                        } else {
                            temp2 = gameBoard[starRow][starCol + 1];
                            points(temp2);
                            gameBoard[starRow][starCol + 1] = '*';
                            gameBoard[starRow][starCol] = temp2;
                            starCol++;
                        }
                    } else if (temp == 'H') {
                        gameBoard[starRow][starCol] = ' ';
                        starCol--;
                        game = false;
                        break;
                    } else {
                        points(temp);
                        gameBoard[starRow][starCol - 1] = '*';
                        gameBoard[starRow][starCol] = temp;
                        starCol--;
                    }
                } else {
                    temp = gameBoard[starRow][gameBoard[starRow].length - 1];
                    if (temp == 'W'){
                        temp2 = gameBoard[starRow][starCol +1];
                        points(temp2);
                        gameBoard[starRow][starCol +1] = '*';
                        gameBoard[starRow][starCol] = temp2;
                        starCol++;
                    } else if (temp == 'H') {
                        gameBoard[starRow][starCol] = ' ';
                        starCol = gameBoard[starRow].length - 1;
                        game = false;
                        break;
                    } else {
                        points(temp);
                        gameBoard[starRow][gameBoard[starRow].length - 1] = '*';
                        gameBoard[starRow][starCol] = temp;
                        starCol = gameBoard[starRow].length - 1;
                    }
                }

            }
        }

        if (game == true){
//            System.out.println("Your movement is:");
            fileOutput.writeToFile("output.txt","Your movement is:",true,true);
            for(String m : move){
//                System.out.print(m + " ");
                fileOutput.writeToFile("output.txt",m + " ",true,false);
            }
//            System.out.println();
//            System.out.println();
            fileOutput.writeToFile("output.txt","",true,true);
            fileOutput.writeToFile("output.txt","",true,true);
//            System.out.println("Your output is:");
            fileOutput.writeToFile("output.txt","Your output is:",true,true);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
//                    System.out.print(gameBoard[i][j] + " ");
                    fileOutput.writeToFile("output.txt",gameBoard[i][j]+ " ",true,false);
                }
//                System.out.println();
                fileOutput.writeToFile("output.txt","",true,true);
            }
//            System.out.println();
            fileOutput.writeToFile("output.txt","",true,true);
            String puan = String.format("Score: %d",point);
//            System.out.println(puan);
            fileOutput.writeToFile("output.txt",puan,true,true);

        } else {
//            System.out.println("Your movement is:");
            fileOutput.writeToFile("output.txt","Your movement is:",true,true);
            for(int i = 0; i<= counter; i++){
//                System.out.print(move[i] + " ");
                fileOutput.writeToFile("output.txt",move[i] + " ",true,false);
            }
//            System.out.println();
//            System.out.println();
            fileOutput.writeToFile("output.txt","",true,true);
            fileOutput.writeToFile("output.txt","",true,true);
//            System.out.println("Your output is:");
            fileOutput.writeToFile("output.txt","Your output is:",true,true);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
//                    System.out.print(gameBoard[i][j] + " ");
                    fileOutput.writeToFile("output.txt",gameBoard[i][j] + " ",true,false);
                }
//                System.out.println();
                fileOutput.writeToFile("output.txt","",true,true);
            }
//            System.out.println();
            fileOutput.writeToFile("output.txt","",true,true);
//            System.out.println("Game Over!");
            fileOutput.writeToFile("output.txt","Game Over!",true,true);
            String puan = String.format("Score: %d",point);
//            System.out.println(puan);
            fileOutput.writeToFile("output.txt",puan,true,true);
        }

//            // Diziyi yazdır
//            for (int i = 0; i < rows; i++) {
//                for (int j = 0; j < cols; j++) {
//                    System.out.print(gameBoard[i][j] + " ");
//                }
//                System.out.println();
//            }

//        System.out.println(point);


        }
    }

//    Yusuf Demir 2210356074
