package com.utilities;

import com.enumerations.ItemType;
import com.enumerations.SpriteType;
import com.gameobjects.Item;

import java.awt.*;

public class ItemCreator {

    public static Item createItem(Point tilePosition, ItemType itemType) {

        Item createdItem = null;

        switch (itemType) {
            case APPLE:
                createdItem = new Item(tilePosition, itemType, SpriteType.APPLE);
                break;
            default:
                System.out.println(
                        "ItemCreator.createItem: error, not valid itemType: " + itemType);
                break;
        }

        return createdItem;
    }

}
