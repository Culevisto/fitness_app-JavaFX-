package com.example.fitness_app.model;

import java.time.LocalDate;

public class StepEntry {
    private LocalDate date;
    private int steps;
    private double calories;
    private String note; // новое поле для заметки

    public StepEntry(LocalDate date, int steps, String note) {
        this.date = date;
        this.steps = steps;
        this.note = note;
        this.calories = calculateCalories(steps);
    }

    private double calculateCalories(int steps) {
        return steps * 0.04; // средний расход калорий за шаг
    }

    public LocalDate getDate() {
        return date;
    }

    public int getSteps() {
        return steps;
    }

    public double getCalories() {
        return calories;
    }

    public String getNote() {
        return note;
    }

    public void setSteps(int steps) {
        this.steps = steps;
        this.calories = calculateCalories(steps);
    }

    public void setNote(String note) {
        this.note = note;
    }
}