package com.glevel.dungeonhero;

import android.content.Context;

import com.glevel.dungeonhero.utils.database.DatabaseHelper;
import com.glevel.dungeonhero.utils.database.Repository;
import com.glevel.dungeonhero.models.Game;

/**
 * Created by guillaume on 10/2/14.
 */
public class MyDatabase extends DatabaseHelper {

    private final static int DB_VERSION = 1;
    private static final String DB_NAME = "dungeon_hero";

    public enum Repositories {
        GAME
    }

    public MyDatabase(Context context) {
        super(context, DB_NAME, DB_VERSION);

        // add repositories
        addRepository(Repositories.GAME.name(), new Repository<Game>(this));
    }

}
