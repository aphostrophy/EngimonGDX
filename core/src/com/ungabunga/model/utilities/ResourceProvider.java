package com.ungabunga.model.utilities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ungabunga.model.entities.Engimon;
import com.ungabunga.model.entities.LivingEngimon;
import com.ungabunga.model.entities.Skill;
import com.ungabunga.model.enums.CellType;
import com.ungabunga.model.enums.DIRECTION;
import com.ungabunga.model.enums.IElements;
import com.ungabunga.model.exceptions.OutOfBoundException;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ungabunga.Settings.ANIM_TIMER;

public class ResourceProvider {

    AssetManager assetManager;
    CopyOnWriteArrayList<? extends Engimon> engimon;
    CopyOnWriteArrayList<Skill> skills;

    HashMap<IElements, CellType> mapElementToBiome;

    AnimationSet arcanineAnimationSet;
    AnimationSet blastoiseAnimationSet;
    AnimationSet cuboneAnimationSet;
    AnimationSet diglettAnimationSet;
    AnimationSet glaceonAnimationSet;
    AnimationSet glalieAnimationSet;
    AnimationSet magmarAnimationSet;
    AnimationSet mareepAnimationSet;
    AnimationSet ninetalesAnimationSet;
    AnimationSet psyduckAnimationSet;
    AnimationSet raichuAnimationSet;
    AnimationSet sharkAnimationSet;
    AnimationSet tongkolAnimationSet;
    AnimationSet voltorbAnimationSet;
    AnimationSet wartortleAnimationSet;

    public ResourceProvider(){
        this.assetManager = new AssetManager();
        assetManager = new AssetManager();
        assetManager.load("pic/packed/avatarTextures.atlas", TextureAtlas.class);
        assetManager.load("pic/packed/uipack.atlas", TextureAtlas.class);
        assetManager.load("pic/font/small_letters_font.fnt", BitmapFont.class);

        assetManager.load("pic/engimon_packed/arcanine.atlas", TextureAtlas.class);
        assetManager.load("pic/engimon_packed/blastoise.atlas", TextureAtlas.class);
        assetManager.load("pic/engimon_packed/cubone.atlas", TextureAtlas.class);
        assetManager.load("pic/engimon_packed/diglett.atlas", TextureAtlas.class);
        assetManager.load("pic/engimon_packed/glaceon.atlas", TextureAtlas.class);
        assetManager.load("pic/engimon_packed/glalie.atlas", TextureAtlas.class);
        assetManager.load("pic/engimon_packed/magmar.atlas", TextureAtlas.class);
        assetManager.load("pic/engimon_packed/mareep.atlas", TextureAtlas.class);
        assetManager.load("pic/engimon_packed/ninetales.atlas", TextureAtlas.class);
        assetManager.load("pic/engimon_packed/psyduck.atlas", TextureAtlas.class);
        assetManager.load("pic/engimon_packed/raichu.atlas", TextureAtlas.class);
        assetManager.load("pic/engimon_packed/shark.atlas", TextureAtlas.class);
        assetManager.load("pic/engimon_packed/tongkol.atlas", TextureAtlas.class);
        assetManager.load("pic/engimon_packed/voltorb.atlas", TextureAtlas.class);
        assetManager.load("pic/engimon_packed/wartotle.atlas", TextureAtlas.class);

        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get("pic/engimon_packed/arcanine.atlas", TextureAtlas.class);
        arcanineAnimationSet = new AnimationSet(
                new Animation<TextureRegion>(ANIM_TIMER/3f, atlas.findRegions("arcanine_walking_south"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation<TextureRegion>(ANIM_TIMER/3f, atlas.findRegions("arcanine_walking_north"),Animation.PlayMode.LOOP_PINGPONG),
                new Animation<TextureRegion>(ANIM_TIMER/3f, atlas.findRegions("arcanine_walking_west"),Animation.PlayMode.LOOP_PINGPONG),
                new Animation<TextureRegion>(ANIM_TIMER/3f, atlas.findRegions("arcanine_walking_east"),Animation.PlayMode.LOOP_PINGPONG),
                atlas.findRegion("arcanine_standing_south"),
                atlas.findRegion("arcanine_standing_north"),
                atlas.findRegion("arcanine_standing_west"),
                atlas.findRegion("arcanine_standing_east")
        );

        atlas = assetManager.get("pic/engimon_packed/blastoise.atlas", TextureAtlas.class);
        blastoiseAnimationSet = new AnimationSet(
                new Animation<TextureRegion>(ANIM_TIMER/3f, atlas.findRegions("blastoise_walking_south"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation<TextureRegion>(ANIM_TIMER/3f, atlas.findRegions("blastoise_walking_north"),Animation.PlayMode.LOOP_PINGPONG),
                new Animation<TextureRegion>(ANIM_TIMER/3f, atlas.findRegions("blastoise_walking_west"),Animation.PlayMode.LOOP_PINGPONG),
                new Animation<TextureRegion>(ANIM_TIMER/3f, atlas.findRegions("blastoise_walking_east"),Animation.PlayMode.LOOP_PINGPONG),
                atlas.findRegion("blastoise_standing_south"),
                atlas.findRegion("blastoise_standing_north"),
                atlas.findRegion("blastoise_standing_west"),
                atlas.findRegion("blastoise_standing_east")
        );

        this.skills = fileUtil.readSkillCSV();
        this.engimon = fileUtil.readEngimonCSV(this);

        HashMap<IElements, CellType> elementBiomeMap = new HashMap<>();
        elementBiomeMap.put(IElements.FIRE,CellType.MOUNTAIN);
        elementBiomeMap.put(IElements.WATER,CellType.SEA);
        elementBiomeMap.put(IElements.GROUND,CellType.GRASSLAND);
        elementBiomeMap.put(IElements.ELECTRIC,CellType.GRASSLAND);
        elementBiomeMap.put(IElements.ICE,CellType.TUNDRA);
        this.mapElementToBiome = elementBiomeMap;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public Skill getSkill(String name){
        for(Skill skill : this.skills){
            if(skill.getSkillName().equals(name)){
                return skill;
            }
        }
        return null;
    }

    public Engimon randomizeEngimon(CellType biomes){
        ArrayList<Engimon> candidates = new ArrayList<>();
        for(Engimon engimon: this.engimon){
            for(IElements engimonElement: engimon.getElements()){
                if(mapElementToBiome.get(engimonElement).equals(biomes)){
                    candidates.add(engimon);
                }
            }
        }
        if(candidates.size()==0){
            return null;
        }
        int x = ThreadLocalRandom.current().nextInt(0,candidates.size());
        return candidates.get(x);
    }

    public Engimon getEngimon(String species) throws OutOfBoundException {
        for(Engimon engimon: this.engimon){
            if(engimon.getSpecies().equals(species)){
                return engimon;
            }
        }
        throw new OutOfBoundException("Engimon not found!");
    }

    public TextureRegion getSprite(LivingEngimon engimon){
        return blastoiseAnimationSet.getStanding(engimon.getDirection());
    }
}
