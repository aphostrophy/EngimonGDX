package com.ungabunga.model.utilities;

import com.ungabunga.model.entities.Engimon;
import com.ungabunga.model.entities.Skill;
import com.ungabunga.model.enums.CellType;

import java.util.concurrent.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class ResourceProvider {
    CopyOnWriteArrayList<? extends Engimon> engimon;
    CopyOnWriteArrayList<Skill> skills;

    public ResourceProvider(){
        this.skills = fileUtil.readSkillCSV();
        this.engimon = fileUtil.readEngimonCSV(this);
    }

    public Skill getSkill(String name){
        for(Skill skill : this.skills){
            if(skill.getSkillName().equals(name)){
                return skill;
            }
        }
        return null;
    }

    public Engimon randomizeEngimon(CellType biome){
        ArrayList<Engimon> candidates = new ArrayList<>();
        for(Engimon engimon: this.engimon){
            if(engimon.getElements().contains(biome)){
                candidates.add(engimon);
            }
        }
        return null;
    }
}