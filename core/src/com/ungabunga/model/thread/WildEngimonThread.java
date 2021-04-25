package com.ungabunga.model.thread;

import com.ungabunga.model.GameState;
import com.ungabunga.model.entities.WildEngimon;

import java.util.concurrent.ThreadLocalRandom;

public class WildEngimonThread extends Thread{

    WildEngimon wildEngimon;
    GameState gameState;

    public WildEngimonThread(WildEngimon wildEngimon, GameState gameState){
        this.wildEngimon = wildEngimon;
        this.gameState = gameState;
    }
    public void run(){
        System.out.println(Thread.currentThread().getName() + " HAHAHAHAHA AKU HIDUP");
        while(!wildEngimon.isDead()){
            wildEngimon.randomizeMove();
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(2000,5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " YAH AKU MATI");
        wildEngimon.removeFromMap();
        gameState.reduceWildEngimon();
    }
}
