package com.renaldorasa.sudoku;

import org.springframework.stereotype.Controller;

@Controller
public class BoardManipulator {

    public int[][] stringTo2DArray(String board){

        int[][] boardArray = new int[9][9];

        int offset = 0;
        for (int i=0; i < 9; i++) {
            for (int j=0; j < 9; j++) {
                boardArray[i][j] = Integer.parseInt(String.valueOf(board.charAt(offset++)));
            }
        }
        return boardArray;
    }

    public String formDataToString (String formData){
        String[] boardData = formData.split("&");
        StringBuilder boardValues = new StringBuilder();

        for(String s : boardData){
            boardValues.append((s.charAt(12)));
        }

        return boardValues.toString();
    }
}
