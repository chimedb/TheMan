package Pacman;

import java.awt.*;
import java.util.Random;

public class Boards {

    public static void GetBoard(Board board, int nr)
    {
        if ( nr <= 0 || nr > 10)
            return;
        GetFirstBoard(board);
    }

    private static void GetFirstBoard(Board board)
    {
        AddFirstPlayer(board);
        AddFirstAnimals(board);
        AddFirstTrees(board);
        AddFirstMushrooms(board);
    }

    private static void AddFirstTrees(Board board)
    {
        for (int i = 1; i < 10; i++)
            AddTree(board,i,1);
        for (int i = 1; i < 6; i++)
            AddTree(board,i,5);
        for (int i = 7; i < 20; i++)
            AddTree(board,i,5);
        for (int i = 13; i < 25; i++)
            AddTree(board,i,8);
        for (int i = 26; i < 28; i++)
            AddTree(board,i,8);
        for (int i = 5; i < 12; i++)
            AddTree(board,i,10);
        for (int i = 15; i < 24; i++)
            AddTree(board,i,3);
        for (int i = 16; i < 22; i++)
            AddTree(board,i,4);
        for (int i = 22; i < 28; i++)
            AddTree(board,i,18);
        for (int i = 8; i < 15; i++)
            AddTree(board,i,16);
        for (int i = 17; i < 25; i++)
            AddTree(board,i,14);
        for (int i = 3; i < 9; i++)
            AddTree(board,i,12);
        for (int i = 10; i < 17; i++)
            AddTree(board,i,12);
        for (int i = 1; i < 13; i++)
            AddTree(board,i,19);

        for (int j=1;j<10;j++)
            AddTree(board,27,j);
        for (int j=8;j<17;j++)
            AddTree(board,29,j);
        for (int j=11;j<18;j++)
            AddTree(board,3,j);
        for (int j=12;j<19;j++)
            AddTree(board,18,j);
        for (int j=8;j<12;j++)
            AddTree(board,23,j);
        for (int j=3;j<8;j++)
            AddTree(board,7,j);
        for (int j=13;j<16;j++)
            AddTree(board,5,j);
        for (int i=1;i<5;i++)
            AddTree(board,i,8);
    }

    private static void AddFirstAnimals(Board board)
    {
        board.AddBear(15*21,13*21);
        board.AddWolf(13*21,0*21);
        board.AddDeer(6*21,7*21);
        board.AddBoar(4*21,13*21);
    }

    private static void AddFirstMushrooms(Board board)
    {
        board.AddMushromsWithProbability(0.6);
    }

    private static void AddFirstPlayer(Board board)
    {
        board.AddPlayer(0*21,0*21);
    }

    private static void AddTree(Board board, int x, int y)
    {
        board.AddTree(x*21,y*21);
    }

    private static void AddMushroom(Board board, int x, int y)
    {
        board.AddMushroom(x*21,y*21);
    }
}
