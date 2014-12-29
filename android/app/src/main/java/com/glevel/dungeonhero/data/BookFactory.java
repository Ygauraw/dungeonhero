package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.models.Book;
import com.glevel.dungeonhero.models.Chapter;
import com.glevel.dungeonhero.models.dungeons.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/6/14.
 */
public class BookFactory {

    public static final int INTRODUCTION_BOOK_ID = 1;

    public static List<Book> getAll() {
        List<Book> lst = new ArrayList<>();
        lst.add(buildInitiationBook());
        lst.add(buildInitiationBook());
        return lst;
    }

    public static Book buildInitiationBook() {
        List<Chapter> chapters = new ArrayList<>();

        // chapter 1
        List<Event> events = new ArrayList<>();
        chapters.add(new Chapter("initiation_chapter_1", "", events));
        return new Book(1, "initiation", "initiation_intro", "initiation_outro", chapters, null);
    }

}
