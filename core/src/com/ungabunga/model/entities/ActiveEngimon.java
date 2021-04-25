package com.ungabunga.model.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.ungabunga.model.GameState;
import com.ungabunga.model.enums.AVATAR_STATE;
import com.ungabunga.model.enums.DIRECTION;
import com.ungabunga.model.exceptions.EngimonConflictException;
import com.ungabunga.model.utilities.Pair;
import com.ungabunga.model.utilities.ResourceProvider;

import static com.ungabunga.Settings.ANIM_TIMER;

/*
Active Engimon merepresentasikan Player Engimon yang sedang aktif merupakan turunan dari PlayerEngimon
yang memiliki posisi
 */

public class ActiveEngimon extends PlayerEngimon implements LivingEngimon{
    Pair<Integer,Integer> position;
    DIRECTION direction;
    AVATAR_STATE state;

    Player player;
    GameState gameState;

    ResourceProvider resourceProvider;

    private float srcX,srcY;
    private float destX,destY;
    private float worldX,worldY;
    private float animTimer;

    private float stateTimer;

    public ActiveEngimon(){

    }

    public ActiveEngimon(PlayerEngimon PE, Player P, int x, int y, GameState gameState, ResourceProvider resourceProvider){
        super(PE);
        this.position = new Pair<Integer,Integer>(x,y);
        this.direction = P.getDirection();
        this.state = P.getState();

        this.player = P;
        this.gameState = gameState;
        this.resourceProvider = resourceProvider;

        this.worldX = x;
        this.worldY = y;

        this.animTimer = 0f;
    }

    private void initializeMove(int dx,int dy){
        this.srcX = this.getX();
        this.srcY = this.getY();
        this.destX = this.getX() + dx;
        this.destY = this.getY() + dy;
        this.worldX = this.getX();
        this.worldY = this.getY();
        this.animTimer = 0f;
        this.state = AVATAR_STATE.WALKING;
    }

    private void finishMove(){
        this.state = AVATAR_STATE.STANDING;
        this.worldX = this.getX();
        this.worldY = this.getY();
    }

    public void update(float delta) throws EngimonConflictException {
        if(state == AVATAR_STATE.WALKING) {
            animTimer += delta;
            stateTimer += delta;
            worldX = Interpolation.pow2.apply(this.srcX,this.destX,animTimer/ANIM_TIMER);
            worldY = Interpolation.pow2.apply(this.srcY,this.destY,animTimer/ANIM_TIMER);

            if(animTimer > ANIM_TIMER){
                stateTimer -= (animTimer - ANIM_TIMER);
                finishMove();
                if(player.getMoveFrameRequest()){
                    followPlayer();
                } else{
                    stateTimer = 0f;
                }
            }
        }
    }

    // I.S. Movement bisa dilakukan dengan valid
    // F.S Engimon langsung berpindah ke tujuan secara memori namun secara visual seolah-olah berjalan
    public void move(int dx, int dy){
        initializeMove(dx,dy);
        gameState.map.get(this.getY()).get(this.getX()).occupier=null;
        this.position.setFirst(this.getX()+dx);
        this.position.setSecond(this.getY()+dy);
        gameState.map.get(this.getY()).get(this.getX()).occupier=this;
    }

    public void moveUp() throws EngimonConflictException {
        if(state == AVATAR_STATE.STANDING){
            direction = DIRECTION.UP;
            if(gameState.map.get(this.getX()).get(this.getY()+1).occupier!=null){
                repositionOnCellConflict();
            }
            move(0,1);
        }
    }

    public void moveDown() throws EngimonConflictException {
        if(state == AVATAR_STATE.STANDING){
            direction = DIRECTION.DOWN;
            if(gameState.map.get(this.getX()).get(this.getY()-1).occupier!=null){
                repositionOnCellConflict();
            }
            move(0,-1);
        }
    }

    public void moveLeft() throws EngimonConflictException {
        if(state == AVATAR_STATE.STANDING){
            direction = DIRECTION.LEFT;
            if(gameState.map.get(this.getX()-1).get(this.getY()).occupier!=null){
                repositionOnCellConflict();
            }
            move(-1,0);
        }
    }

    public void moveRight() throws EngimonConflictException {
        if(state == AVATAR_STATE.STANDING){
            direction = DIRECTION.RIGHT;
            if(gameState.map.get(this.getX()+1).get(this.getY()).occupier!=null){
                repositionOnCellConflict();
            }
            move(1,0);
        }
    }

    //I.S Player sukses bergerak
    //F.S Engimon berhasil menempati posisi terakhir pemain, direposisi apabila posisi terakhir pemain ditempati engimon baru, dan throw exception dan remove engimon apabila sekeliling pemain tidak ada tempat spawn engimon
    public boolean followPlayer() throws EngimonConflictException {
        if(this.getX()<this.player.getWorldX()){
            moveRight();
            return true;
        }
        if(this.getX()>this.player.getWorldX()){
            moveLeft();
            return true;
        }
        if(this.getY()<this.player.getWorldY()){
            moveUp();
            return true;
        }
        if(this.getY()>this.player.getWorldY()){
            moveDown();
            return true;
        }
        return false;
    }

    @Override
    public Pair<Integer,Integer> getPosition() {
        return this.position;
    }

    @Override
    public void repositionOnCellConflict() throws EngimonConflictException {
        if(gameState.map.get(player.getY()).get(player.getX()-1).occupier == null){
            this.position.setFirst(player.getX()-1);
            this.position.setSecond(player.getY());

            this.worldX = player.getX()-1;
            this.worldY = player.getY();
            return;
        }
        if(gameState.map.get(player.getY()).get(player.getX()+1).occupier == null){
            this.position.setFirst(player.getX()+1);
            this.position.setSecond(player.getY());

            this.worldX = player.getX()+1;
            this.worldY = player.getY();
            return;
        }
        if(gameState.map.get(player.getY()-1).get(player.getX()).occupier == null){
            this.position.setFirst(player.getX());
            this.position.setSecond(player.getY()-1);

            this.worldX = player.getX();
            this.worldY = player.getY()-1;
            return;
        }
        if(gameState.map.get(player.getY()+1).get(player.getX()).occupier == null){
            this.position.setFirst(player.getX());
            this.position.setSecond(player.getY()+1);

            this.worldX = player.getX();
            this.worldY = player.getY()+1;
            return;
        }
        throw new EngimonConflictException("Can't reposition your engimon as you're surrounded by wild engimons");
    }

    @Override
    public int getRemainingLives() {
        return this.remainingLives;
    }

    @Override
    public boolean isDead() {
        return this.remainingLives<=0;
    }

    public float getWorldX() {
        return worldX;
    }

    public float getWorldY() {
        return worldY;
    }

    @Override
    public void reduceLives() {
        this.remainingLives--;
    }

    @Override
    public DIRECTION getDirection() {
        return this.direction;
    }

    @Override
    public float getKeyFrame() {
        return stateTimer;
    }

    @Override
    public AVATAR_STATE getState(){return this.state;}

    @Override
    public String getEngimonSpecies(){return this.getSpecies();}
    
    public int getX(){
        return this.position.getFirst();
    }

    public int getY(){
        return this.position.getSecond();
    }

    public TextureRegion getSprite(){
        return resourceProvider.getSprite((LivingEngimon) this);
    }

    public TextureRegion getSpriteAura(){
        return resourceProvider.getSpriteAura((LivingEngimon) this);
    }
}
