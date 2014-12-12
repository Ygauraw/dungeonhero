package com.glevel.dungeonhero.models.items;

import com.glevel.dungeonhero.models.characters.Unit;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public class Consumable extends Item implements Serializable {

    private static final long serialVersionUID = 3416498502861499207L;

    public Consumable(String identifier) {
        super(identifier, true);
    }

    public void use(Unit owner) {

    }

}
