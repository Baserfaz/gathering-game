package com.utilities;

import com.enumerations.ItemType;
import com.enumerations.SpriteType;
import com.gameobjects.Chest;
import com.gameobjects.Item;
import com.gameobjects.StepPlate;

import java.awt.*;

public class ItemCreator {

    public static Item createItem(Point tilePosition, ItemType itemType) {

        Item createdItem = null;

        switch (itemType) {
            case APPLE:
                createdItem = new Item(tilePosition, itemType, SpriteType.APPLE);
                break;
            case EXIT:
                createdItem = new Item(tilePosition, itemType, SpriteType.LADDER);
                break;
            case STONE:
                createdItem = new Item(tilePosition, itemType, SpriteType.STONE_1);
                break;
            case CHEST:
                createdItem = new Chest(tilePosition, false);
                break;
            case CHEST_LOCKED:
                createdItem = new Chest(tilePosition, true);
                break;
            case BUTTON:
                createdItem = new StepPlate(tilePosition);
                break;
            default:
                System.out.println(
                        "ItemCreator.createItem: error, not valid itemType: " + itemType);
                break;
        }

        return createdItem;
    }

}
