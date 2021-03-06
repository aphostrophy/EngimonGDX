package com.ungabunga.model.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.ungabunga.model.GameState;
import com.ungabunga.model.entities.*;
import com.ungabunga.model.enums.AVATAR_STATE;
import com.ungabunga.model.enums.DIRECTION;
import com.ungabunga.model.exceptions.FeatureNotImplementedException;
import com.ungabunga.model.exceptions.FullInventoryException;
import com.ungabunga.model.save.Save;
import com.ungabunga.model.screen.GameScreen;
import com.ungabunga.model.utilities.Pair;

public class PlayerController extends InputAdapter{
    private GameScreen gameScreen;
    private GameState gameState;
    private DIRECTION direction;
    private AVATAR_STATE state;
    public boolean isInventoryOpen;
    public boolean isBreederOpen;
    public boolean isDetailOpen;

    public PlayerController(GameState gameState, GameScreen gameScreen) {
        this.gameState = gameState;
        this.isInventoryOpen = false;
        this.isBreederOpen = false;
        this.isDetailOpen = false;
        this.gameScreen = gameScreen;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.I) {
            if(gameState.player.getState() != AVATAR_STATE.STANDING){
                gameScreen.dialogueController.startExceptionDialogue(new FeatureNotImplementedException("You can't open your inventory\nwhile moving"));
            } else{
                isInventoryOpen = !isInventoryOpen;
            }
        }
        if(isInventoryOpen) {
            return false;
        }
        if (isBreederOpen) {
            return false;
        }
        if(keycode == Keys.W) {
            direction = DIRECTION.UP;
            state = AVATAR_STATE.WALKING;
        }
        if(keycode == Keys.S) {
            direction = DIRECTION.DOWN;
            state = AVATAR_STATE.WALKING;
        }
        if(keycode == Keys.A) {
            direction = DIRECTION.LEFT;
            state = AVATAR_STATE.WALKING;
        }
        if(keycode == Keys.D) {
            direction = DIRECTION.RIGHT;
            state = AVATAR_STATE.WALKING;
        }
        if(keycode == Keys.SHIFT_LEFT  && gameState.player.state == AVATAR_STATE.WALKING) {
            gameState.player.isRunning = true;
        }
        if(keycode == Keys.R){
            if(gameState.player.getActiveEngimon()==null){
                gameScreen.dialogueController.startDialogue("No active engimon");
            } else{
                try{
                    gameState.removePlayerEngimon();
                } catch (FullInventoryException e){
                    gameScreen.dialogueController.startDialogue("Can't remove engimon as your inventory is full");
                }
            }
        }
        if(keycode == Keys.E) {
            if(gameState.player.getActiveEngimon()!=null){
                isDetailOpen = !isDetailOpen;
            } else{
                gameScreen.dialogueController.startDialogue("No active engimon");
            }
        }
        if (keycode == Keys.B) {
            if(gameState.player.getActiveEngimon()==null){
                gameScreen.dialogueController.startDialogue("No active engimon");
            }
           battleHandler();
        }
        if (keycode ==Keys.Z){
            if(gameState.player.getActiveEngimon()!=null){
                gameScreen.dialogueController.startDialogue(gameState.player.getActiveEngimon().getSlogan());
            } else{
                gameScreen.dialogueController.startDialogue("No active engimon");
            }
        }
        if(keycode == Keys.X){
            instantKill();
        }
        if(keycode == Keys.H){
            gameScreen.dialogueController.startTutorialDialogue();
        }
        if(keycode == Keys.M){
            gameState.player.setPosition(gameState.map.length()/2,gameState.map.get(0).length()/2);
            try{
                gameState.removePlayerEngimon();
            } catch(FullInventoryException e){
                gameScreen.dialogueController.startDialogue("Your engimon got a heart attack and dies");
                gameState.map.get(gameState.player.getActiveEngimon().getY()).get(gameState.player.getActiveEngimon().getX()).occupier = null;
                gameState.player.removeActiveEngimon();
            }

        }
        if(keycode == Keys.F5){
            if(gameScreen.getGameState().player.getActiveEngimon()!=null){
                Json json = new Json();
                json.setOutputType(JsonWriter.OutputType.json);
                FileHandle file = Gdx.files.local("mysave.json");
                file.writeString(json.toJson(new Save(gameState)), false);
                gameScreen.dialogueController.startDialogue("Game saved");
            } else{
                gameScreen.dialogueController.startDialogue("You need to select a companion to save your game");
            }
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (Gdx.input.getX() < 220 && Gdx.input.getX() > 130 && Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() + 80 && Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() - 85) {
            if(gameState.player.getState() != AVATAR_STATE.STANDING){
                gameScreen.dialogueController.startExceptionDialogue(new FeatureNotImplementedException("You can't open your inventory\nwhile moving"));
            } else{
                isInventoryOpen = !isInventoryOpen;
            }
        }

        if (Gdx.input.getX() < 105 && Gdx.input.getX() > 25 && Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() + 80 && Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() - 85) {
            if(gameState.player.getState() != AVATAR_STATE.STANDING){
                gameScreen.dialogueController.startExceptionDialogue(new FeatureNotImplementedException("You can't breed your engimon\nwhile moving"));
            } else{
                isBreederOpen = !isBreederOpen;
            }
        }
        return super.touchUp(screenX, screenY, pointer, button);
    }

    public void finishBreeding() {
        isBreederOpen = !isBreederOpen;
    }

    public void closeInventory() {
        isInventoryOpen = !isInventoryOpen;
    }

    public void closeDetail() {
        isDetailOpen = !isDetailOpen;
    }

    @Override
    public boolean keyUp(int keycode){
        if(keycode == Keys.W) {
            state = AVATAR_STATE.STANDING;
        }
        if(keycode == Keys.S) {
            state = AVATAR_STATE.STANDING;
        }
        if(keycode == Keys.A) {
            state = AVATAR_STATE.STANDING;
        }
        if(keycode == Keys.D) {
            state = AVATAR_STATE.STANDING;
        }
        if (keycode == Keys.SHIFT_LEFT && gameState.player.state == AVATAR_STATE.WALKING) {
            gameState.player.isRunning= false;
        }
        return false;
    }

    public void update(float delta){
        if(direction == DIRECTION.UP && state!=AVATAR_STATE.STANDING){
            try{
                gameState.movePlayerUp();
            } catch(Exception e){
                gameScreen.dialogueController.startExceptionDialogue(e);
            }
        }
        else if(direction == DIRECTION.DOWN && state!=AVATAR_STATE.STANDING){
            try{
                gameState.movePlayerDown();
            } catch(Exception e){
                gameScreen.dialogueController.startExceptionDialogue(e);
            }
        }
        else if(direction == DIRECTION.LEFT && state!=AVATAR_STATE.STANDING){
            try{
                gameState.movePlayerLeft();
            } catch(Exception e){
                gameScreen.dialogueController.startExceptionDialogue(e);
            }
        }
        else if(direction == DIRECTION.RIGHT && state!=AVATAR_STATE.STANDING){
            try{
                gameState.movePlayerRight();
            } catch(Exception e){
                gameScreen.dialogueController.startExceptionDialogue(e);
            }
        }
    }

    public void battleHandler(){
        if(this.gameState.player.getActiveEngimon()!=null){
            Pair<Integer,Integer> dir = new Pair<>(0,0);
            DIRECTION d = this.gameState.player.getDirection();
            DIRECTION facingPlayer = d;
            if(d == DIRECTION.UP) {
                dir = new Pair<>(0,1);
                facingPlayer = DIRECTION.DOWN;
            } else if(d == DIRECTION.DOWN) {
                dir = new Pair<>(0,-1);
                facingPlayer = DIRECTION.UP;
            } else if(d == DIRECTION.RIGHT) {
                dir = new Pair<>(1,0);
                facingPlayer = DIRECTION.LEFT;
            } else if(d == DIRECTION.LEFT) {
                dir = new Pair<>(-1,0);
                facingPlayer = DIRECTION.RIGHT;
            }


            if((this.gameState.player.getY() + dir.getSecond())
                    != this.gameState.player.getActiveEngimon().getY()
                    || (this.gameState.player.getX() + dir.getFirst())
                    != this.gameState.player.getActiveEngimon().getX() ) {
                if(gameState.map.get(this.gameState.player.getY() + dir.getSecond()).get(this.gameState.player.getX() + dir.getFirst()).occupier != null){
                    WildEngimon occupier = (WildEngimon) gameState.map.get(this.gameState.player.getY() + dir.getSecond()).get(this.gameState.player.getX() + dir.getFirst()).occupier;

                    if (occupier != null) {
                        occupier.direction = facingPlayer;
                        occupier.isInBattle = true;
                        gameScreen.dialogueController.startBattleDialogue(occupier);
                    }
                }
            }
            else {
            }
        }
    }

    public void instantKill(){
        if(this.gameState.player.getActiveEngimon()!=null){
            Pair<Integer,Integer> dir = new Pair<>(0,0);
            DIRECTION d = this.gameState.player.getDirection();
            if(d == DIRECTION.UP) {
                dir = new Pair<>(0,1);
            } else if(d == DIRECTION.DOWN) {
                dir = new Pair<>(0,-1);
            } else if(d == DIRECTION.RIGHT) {
                dir = new Pair<>(1,0);
            } else if(d == DIRECTION.LEFT) {
                dir = new Pair<>(-1,0);
            }
            if((this.gameState.player.getY() + dir.getSecond()) != this.gameState.player.getActiveEngimon().getY() || (this.gameState.player.getX() + dir.getFirst()) != this.gameState.player.getActiveEngimon().getX() ) {
                if(gameState.map.get(this.gameState.player.getY() + dir.getSecond()).get(this.gameState.player.getX() + dir.getFirst()).occupier != null){
                    WildEngimon occupier = (WildEngimon) gameState.map.get(this.gameState.player.getY() + dir.getSecond()).get(this.gameState.player.getX() + dir.getFirst()).occupier;
                    occupier.reduceLives();
                }
            }   else {
            }
        }
    }
}
