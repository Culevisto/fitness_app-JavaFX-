package com.example.fitness_app.storage;

import com.example.fitness_app.model.StepEntry;
import java.io.FileWriter;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {

    private static final Path FILE = Paths.get("steps.csv");

    public StorageManager() {
        try {
            if (!Files.exists(FILE)) Files.createFile(FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(StepEntry entry) {
        try (FileWriter writer = new FileWriter(FILE.toFile(), true)) {
            writer.write(entry.getDate() + "," + entry.getSteps() + "," + entry.getCalories() + "," + entry.getNote() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(StepEntry entry) {
        List<StepEntry> all = load();
        all.removeIf(e -> e.getDate().equals(entry.getDate()));
        all.add(entry);

        try (FileWriter writer = new FileWriter(FILE.toFile(), false)) {
            for (StepEntry e : all) {
                writer.write(e.getDate() + "," + e.getSteps() + "," + e.getCalories() + "," + e.getNote() + "\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void delete(LocalDate date) {
        List<StepEntry> all = load();
        all.removeIf(e -> e.getDate().equals(date));

        try (FileWriter writer = new FileWriter(FILE.toFile(), false)) {
            for (StepEntry e : all) {
                writer.write(e.getDate() + "," + e.getSteps() + "," + e.getCalories() + "," + e.getNote() + "\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<StepEntry> load() {
        List<StepEntry> list = new ArrayList<>();
        try {
            if (!Files.exists(FILE)) return list;

            for (String line : Files.readAllLines(FILE)) {
                if (line.isBlank()) continue;
                String[] p = line.split(",");

                LocalDate date = LocalDate.parse(p[0]);
                int steps = Integer.parseInt(p[1]);
                String note = p.length > 3 ? p[3] : "";

                StepEntry entry = new StepEntry(date, steps, note);
                list.add(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}