package com.renaldorasa.sudoku;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renaldorasa.sudoku.dxl.DancingLinksAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    DancingLinksAlgorithm solver;

    @Autowired
    SudokuGenerator sudokuGenerator;

    @Autowired
    BoardManipulator boardManipulator;

    private boolean isBoardSolved;

    @GetMapping("/sudoku")
    public void createSolvableBoard (Model model){

        if(isBoardSolved){
            int[][] solvedBoard = boardManipulator.stringTo2DArray(solver.printableSolve(sudokuGenerator.getMat()));
            model.addAttribute("board", solvedBoard);
        }else{
            sudokuGenerator.generateBoard(9, 20);
            sudokuGenerator.fillValues();
            model.addAttribute("board", sudokuGenerator.getMat());
        }
    }

    @RequestMapping(value = "/sudoku/status", method = RequestMethod.POST)
    public @ResponseBody
    boolean checkBoardStatus(@RequestBody String formData) throws IOException {

        if (solver.printableSolve(sudokuGenerator.getMat()).equals
                (boardManipulator.formDataToString(formData)))
            return true;

        return false;
    }

    @RequestMapping(value = "/sudoku/update_status", method = RequestMethod.POST)
    public @ResponseBody void setBoardStatus(@RequestBody boolean statusUpdate) {
        isBoardSolved = statusUpdate;
    }

    @RequestMapping(value = "/sudoku/move", method = RequestMethod.POST)
    public @ResponseBody boolean checkMove(@RequestBody String data) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(data);

        JsonNode position = actualObj.get("position");
        JsonNode value = actualObj.get("value");

        String cellPosition = position.asText().replaceAll("[^0-9]", "");
        int cellValue = value.asInt();


        int row = Integer.parseInt(String.valueOf(cellPosition.charAt(0)));
        int column = Integer.parseInt(String.valueOf(cellPosition.charAt(1)));

        if(solver.isSafe(sudokuGenerator.getMat(), row, column, cellValue))
            return true;

        return false;
    }
}
