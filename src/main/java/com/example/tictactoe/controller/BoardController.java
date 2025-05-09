package com.example.tictactoe.controller;

import com.example.tictactoe.service.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.WindowEvent;

public class BoardController implements BoardUI {

    BoardImpl board;
    AiPlayer ai;
    HumanPlayer human;

    public BoardController() {
        board = new BoardImpl();
        ai = new AiPlayer(board);
        human = new HumanPlayer(board);
    }

    @FXML
    private GridPane MainGrid;

    @FXML
    void onclick_grid_btn(ActionEvent event) {
        Button button = (Button) event.getSource();
        int row = Integer.parseInt(button.getId().split("")[2]);
        int col = Integer.parseInt(button.getId().split("")[3]);

        human.move(row, col);
        ai.findBestMove();
        board.printBoard();
        updateUi();
        if (board.checkWinner() != null) {
            NotifyWinner(board.checkWinner().getWinningPiece());
        } else if (board.isBoardFull()) {
            showAlert("Tie");
        }
    }

    public void updateUi() {
        for (int i = 0; i < board.getPieces().length; i++) {
            for (int j = 0; j < board.getPieces()[i].length; j++) {
                update(i, j, board.getPieces()[i][j]);
            }
        }
    }

    @Override
    public void update(int row, int col, Piece piece) {
        String buttonId = "id" + row + col;
        for (Node node : MainGrid.getChildren()) {
            if (node instanceof Button button && buttonId.equals(node.getId())) {
                if (piece == Piece.X) {
                    button.setText("X");
                } else if (piece == Piece.O) {
                    button.setText("O");
                } else {
                    button.setText("");
                }
                break;
            }
        }
    }

    @Override
    public void NotifyWinner(Piece winner) {
        if (winner == Piece.X) {
            showAlert("X wins");
        } else if (winner == Piece.O) {
            showAlert("O wins");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION, message);
        alert.setOnCloseRequest((DialogEvent event) -> {
            board.initializeBoard();
            updateUi();
        });
        alert.showAndWait();
    }
}
