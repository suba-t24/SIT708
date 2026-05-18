package com.example.llmlearningassistantapp.data;

import com.example.llmlearningassistantapp.model.Question;
import com.example.llmlearningassistantapp.model.QuizTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummyRepository {

    public static QuizTask getGeneratedTask(List<String> interests) {
        String mainTopic = interests.isEmpty() ? "Algorithms" : interests.get(0);

        List<Question> questions = new ArrayList<>();

        questions.add(new Question(
                "What is the time complexity of binary search in a sorted array?",
                Arrays.asList("O(n)", "O(log n)", "O(n log n)", "O(1)"),
                1
        ));

        questions.add(new Question(
                "Which data structure follows FIFO behaviour?",
                Arrays.asList("Stack", "Queue", "Tree", "Graph"),
                1
        ));

        questions.add(new Question(
                "What does HTML stand for?",
                Arrays.asList(
                        "Hyper Trainer Marking Language",
                        "Hyper Text Markup Language",
                        "High Text Machine Language",
                        "Hyperlink Text Marking Language"
                ),
                1
        ));

        return new QuizTask(
                "Generated Task 1",
                "Small personalised learning task generated based on your selected interests.",
                mainTopic,
                questions
        );
    }
}
