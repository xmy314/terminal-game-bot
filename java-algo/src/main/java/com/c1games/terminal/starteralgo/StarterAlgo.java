package com.c1games.terminal.starteralgo;

import com.c1games.terminal.algo.Config;
import com.c1games.terminal.algo.Coords;
import com.c1games.terminal.algo.GameIO;
import com.c1games.terminal.algo.io.GameLoop;
import com.c1games.terminal.algo.io.GameLoopDriver;
import com.c1games.terminal.algo.map.GameState;
import com.c1games.terminal.algo.map.MapBounds;
import com.c1games.terminal.algo.units.UnitType;
import com.c1games.terminal.algo.map.Unit;

import java.util.*;

/**
 * Java implementation of the standard starter algo.
 */
public class StarterAlgo implements GameLoop {
    public static void main(String[] args) {
        new GameLoopDriver(new StarterAlgo()).run();
    }

    private static final Coords[][] attacking_coords = {
        {new Coords(9 , 4),new Coords(10, 3)},
        {new Coords(18, 4),new Coords(17, 3)}
    };

    private static final Coords[] attacking_doors = {
        new Coords(23, 10),
        new Coords(4 ,10),
    };

    private static boolean attacking= false;

    private static int choice_of_attack;

    private static final int[][] directions ={
        {0,1},
        {1,1},
        {1,0},
        {1,-1},
        {0,-1},
        {-1,-1},
        {-1,0},
        {-1,1}
    };

    private static int[][] activation_type = {
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 1, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 1, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, 1, 0, 0, 1, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0},
        { 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0},
        { 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0},
        { 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1}
    };

    private static int[][] activated={
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-8,-8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-8,-4,-4,-8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-7,-4,-7, 0,-4,-7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-6,-4, 0,-8, 0, 0,-4,-6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-7,-7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-7, 0, 0,-7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 3, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
        { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
        { 0,-9, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,-9, 0},
        { 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1}
    };

    private final Random rand = new Random();



    @Override
    public void initialize(GameIO io, Config config) {
        io.debug().println("Configuring your custom java algo strategy...");
    }

    /**
     * Make a move in the game.
     */
    @Override
    public void onTurn(GameIO io, GameState move) {
        

    


        if (move.data.turnInfo.turnNumber==0){
            /*
                intializing the original setup 
            */
            for (int y=0 ; y<14 ; y++){
                for (int x=0 ; x<28 ; x++){
                    if (activated[y][x]>=1){
                        Coords pos=new Coords(x,y);
                        spawnBypos(move, pos);
                    }
                }
            }
        }else{
            /* check every status 1 unit, 
                if damaged or destroyed: rebuild and activate surrounding.
            */
            for (int y=13 ; y>=0 ; y-=1){
                for (int x=0 ; x<28 ; x++){
                    if (activated[y][x]>=1 && !( attacking && attacking_doors[choice_of_attack].x == x && attacking_doors[choice_of_attack].y == y) ){
                        Coords pos=new Coords(x,y);
                        Unit building = move.getWallAt(pos);
                        if (! (building == null || building.stability<0.85)){
                            continue;
                        }
                        if (building == null){
                            spawnBypos(move, pos);
                        }
                        if (building!=null && building.stability<0.5){
                            io.debug().println("@("+x+","+y+") is at"+building.stability);
                        }
                        
                        for (int[] dir : directions){
                            Coords activiting_pos = new Coords(x+dir[0],y+dir[1]);
                            if (activiting_pos.y<=13 && MapBounds.inArena(activiting_pos) && activation_type[activiting_pos.y][activiting_pos.x]!=0 && activated[activiting_pos.y][activiting_pos.x]<=0){
                                activated[activiting_pos.y][activiting_pos.x]=1;
                                spawnBypos(move, activiting_pos);
                            }
                        }
                    }
                }
            }
            /*
            check remaining core and decide to activate next
            */
            if (move.data.p1Stats.cores>=20){
                io.debug().println("let's expand with"+move.data.p1Stats.cores);

                int aim=0;
                for (int y=0 ; y<14 ; y++){
                    for (int x=0 ; x<28 ; x++){
                        if ( activated[y][x]<aim){
                            aim=activated[y][x];
                        }
                    }
                }

                for (int y=0 ; y<14 ; y++){
                    for (int x=0 ; x<28 ; x++){
                        if (activated[y][x] == aim){
                            activated[y][x]=1;
                            io.debug().println("expanded at("+x+","+y+")");
                            Coords pos=new Coords(x,y);
                            spawnBypos(move,pos);
                        }
                        if (move.data.p1Stats.cores<5){
                            break;
                        }
                    }

                    if (move.data.p1Stats.cores<=5){
                        break;
                    }
                }
            }
        }

        /*
            deploy information units
        */
        deployAttackers(move);
    }

    private void spawnBypos(GameState move, Coords pos){
        if (activation_type[pos.y][pos.x]==1){
            move.attemptSpawn(pos, UnitType.Filter);
        } else if(activation_type[pos.y][pos.x]==2){
            move.attemptSpawn(pos, UnitType.Destructor);
        } else if(activation_type[pos.y][pos.x]==3){
            move.attemptSpawn(pos, UnitType.Encryptor);
        }
    }

    private void deployAttackers(GameState move) {
        double gate=0.04*move.data.turnInfo.turnNumber+12;
        if (move.data.p1Stats.bits < gate && !attacking){
            return;
        }else if (attacking){
            int p=0;
            while (move.data.p1Stats.bits>=1){
                move.attemptSpawn(attacking_coords[choice_of_attack][p], UnitType.Ping);
                p=1-p;
            }
            attacking=false;
        }else if (move.data.p1Stats.bits*0.75+5 >= gate){
            choice_of_attack=rand.nextInt(2);
            Unit door = move.getWallAt(attacking_doors[choice_of_attack]);
            if (door!=null){
                move.removeFirewall(attacking_doors[choice_of_attack]);
            }
            attacking=true;
        }

    }

}
