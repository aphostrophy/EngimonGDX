package com.ungabunga.model.entities;
import com.ungabunga.model.exceptions.*;

public class Bag {
    Inventory<SkillItem> skillItemInventory;
    Inventory<PlayerEngimon> engimonInventory;

    public Bag(){
        this.skillItemInventory = new Inventory<SkillItem>();
        this.engimonInventory = new Inventory<PlayerEngimon>();
    }
    public int getCurrBagCapacity(){
        return (this.skillItemInventory.getSkillItemAmount() + this.engimonInventory.getFilledSlot());
    }
    public void insertToBag(SkillItem skillitem) throws FullInventoryException {
        this.skillItemInventory.insertToInventory(skillitem,getCurrBagCapacity());
    }
    public void insertToBagForTesting(SkillItem skillitem,int amount) throws FullInventoryException {
        this.skillItemInventory.insertToInventoryForTesting(skillitem,getCurrBagCapacity(),amount);
    }
    public void insertToBag(PlayerEngimon engimon) throws FullInventoryException {
        this.engimonInventory.insertToInventory(engimon,getCurrBagCapacity());
    }
    public void deleteFromBag(SkillItem skillitem){
        this.skillItemInventory.deleteFromInventory(skillitem);
    }
    public void deleteFromBag(PlayerEngimon engimon){
        this.engimonInventory.deleteFromInventory(engimon);
    }
    public PlayerEngimon getEngimonByIndex(int index){
        return engimonInventory.getItemByIndex(index);
    }
    public SkillItem getSkillItemByIndex(int index){
        return skillItemInventory.getItemByIndex(index);
    }
    public void showInventory(){
        this.skillItemInventory.displaySkillItem();
        this.engimonInventory.displayEngimon();
    }
    public int getEngimonIndex(PlayerEngimon engimon){
        return engimonInventory.getItemIndex(engimon);
    }
    public int getSkillItemIndex(SkillItem skillitem){
        return skillItemInventory.getItemIndex(skillitem);
    }

    public PlayerEngimon getEngimonByID(int ID) throws EngimonNotFoundException {
        return engimonInventory.getItemByIndex(engimonInventory.getEngimonIndexByID(ID));
    }
    public SkillItem getSkillItemByName(String name) throws SkillItemNotFound{
        return skillItemInventory.getItemByIndex(skillItemInventory.getSkillItemIndexByName(name));
    }

    public Inventory<SkillItem> getSkillItemInventory() {
        return skillItemInventory;
    }

    public Inventory<PlayerEngimon> getEngimonInventory() {
        return engimonInventory;
    }
}
