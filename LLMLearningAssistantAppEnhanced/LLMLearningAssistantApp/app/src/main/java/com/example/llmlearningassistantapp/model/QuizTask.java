package com.example.llmlearningassistantapp.model;

import java.io.Serializable;
import java.util.List;

public class QuizTask implements Serializable {

    private final String taskTitle;
    private final String taskDescription;
    private final String lessonTopic;
    private final List<Question> questions;

    public QuizTask(String taskTitle, String taskDescription, String lessonTopic, List<Question> questions) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.lessonTopic = lessonTopic;
        this.questions = questions;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getLessonTopic() {
        return lessonTopic;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
