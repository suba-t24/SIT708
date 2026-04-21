package com.example.sportsnewsfeedapp.data;

import com.example.sportsnewsfeedapp.R;
import com.example.sportsnewsfeedapp.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class DummyData {

    public static List<NewsItem> getAllNews() {
        List<NewsItem> list = new ArrayList<>();

        list.add(new NewsItem(
                1,
                "Football Finals Tonight",
                "Two top football clubs will meet tonight in a highly anticipated final. Both teams have shown strong form throughout the season, making this one of the most exciting matches of the year.",
                "Football",
                R.drawable.sport1,
                true
        ));

        list.add(new NewsItem(
                2,
                "Basketball Team Wins Thriller",
                "The basketball team secured a dramatic win after a close final quarter. The game remained competitive until the last minute and ended with a memorable performance.",
                "Basketball",
                R.drawable.sport2,
                true
        ));

        list.add(new NewsItem(
                3,
                "Cricket Series Update",
                "The latest cricket series remains evenly balanced. Strong batting and disciplined bowling have kept both teams in contention heading into the next match.",
                "Cricket",
                R.drawable.sport3,
                true
        ));

        list.add(new NewsItem(
                4,
                "Football Transfer Rumours Grow",
                "Several football clubs are preparing major transfer offers ahead of the next season. Fans are eagerly waiting for official announcements.",
                "Football",
                R.drawable.sport4,
                false
        ));

        list.add(new NewsItem(
                5,
                "Basketball MVP Race Heats Up",
                "The race for MVP is becoming more competitive as multiple players continue delivering outstanding performances week after week.",
                "Basketball",
                R.drawable.sport5,
                false
        ));

        list.add(new NewsItem(
                6,
                "Cricket Captain Confident Before Decider",
                "Ahead of the deciding match, the cricket captain has expressed confidence in the squad’s preparation and ability to perform under pressure.",
                "Cricket",
                R.drawable.sport6,
                false
        ));

        return list;
    }
}