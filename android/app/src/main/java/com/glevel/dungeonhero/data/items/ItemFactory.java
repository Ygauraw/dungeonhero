package com.glevel.dungeonhero.data.items;

import com.glevel.dungeonhero.models.items.Item;

/**
 * Created by guillaume ON 10/6/14.
 */
public class ItemFactory {

    public static Item getRandomItem(int threatLevel) {
        int level = (int) Math.max(0, Math.min(6, threatLevel - 1 + Math.random() * 3));
        int random = (int) (Math.random() * 7);
        switch (random) {
            case 0:
            case 1:
            case 2:
                return null;
            case 3:
                return PotionFactory.getRandomPotion();
            case 4:
                return RingFactory.getRandomRing(level);
            case 5:
                return ArmorFactory.getRandomArmor(level);
            default:
                return WeaponFactory.getRandomWeapon(level);
        }
    }

}
