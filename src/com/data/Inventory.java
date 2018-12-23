package com.data;

public class Inventory {

    private int gold;
    private int keys;

    // TODO: a list of thingies that give boost to player's stats etc.

    public Inventory() {
        this.gold = 0;
        this.keys = 0;
    }

    public void useKey() {
        this.keys -= 1;
    }

    public boolean hasKeys() {
        return this.keys > 0;
    }

    public void addKeys(int a) {
        this.keys += a;
    }

    public void addGold(int a) {
        this.gold += a;
    }

    public int getGold() {
        return gold;
    }

    public int getKeys() {
        return keys;
    }
}
