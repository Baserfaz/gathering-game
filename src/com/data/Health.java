package com.data;

import com.gameobjects.Actor;
import com.gameobjects.GameObject;

public class Health {

    private GameObject object;
    
    private int currentHP;
    private int maxHP = 3;
    private boolean isDead = false;

    public Health(int hp, GameObject obj) {
        this.maxHP = hp;
        this.currentHP = this.maxHP;
        this.object = obj;
    }
    
    private void die() {
        this.isDead = true;
        
        if(this.object instanceof Actor) {
            ((Actor)this.object).onDeath();
        } else {
            System.out.println("Health::die: not supported for type: " + this.object.getClass().getTypeName());
        }
    }
    
    public void takeDamage(int amount) {
        if(this.isDead) return;
        this.currentHP -= amount;
        if(currentHP <= 0) { this.die(); }
        
        if(this.object instanceof Actor) {
            Actor actor = ((Actor)this.object);
        }
    }

    public void healDamage(int amount) {
        if(this.isDead) return;
        this.currentHP += amount;
        if(currentHP > maxHP) currentHP = maxHP;
    }

    public void setMaxHP(int amount) {
        this.maxHP = amount;
        if(this.maxHP > 10) this.maxHP = 10;
    }
    
    public void subtractMaxHP(int amount) {
        this.maxHP -= amount;
        if(this.maxHP < 1) this.maxHP = 1;
    }
    
    // ---- GETTERS & SETTERS ----
    public int getCurrentHP() { return currentHP; }
    public void setCurrentHP(int currentHP) { this.currentHP = currentHP; }
    public int getMaxHP() { return this.maxHP; }
    public boolean isDead() { return isDead; }
    public void setDead(boolean isDead) { this.isDead = isDead; }
    public GameObject getObject() { return object; }
    public void setObject(GameObject object) { this.object = object; }
}
