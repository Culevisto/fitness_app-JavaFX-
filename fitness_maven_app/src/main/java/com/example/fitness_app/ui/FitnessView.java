package com.example.fitness_app.ui;

import com.example.fitness_app.model.StepEntry;
import com.example.fitness_app.storage.StorageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.List;

public class FitnessView {

    private VBox root = new VBox();
    private StorageManager storage = new StorageManager();
    private ObservableList<StepEntry> allEntries = FXCollections.observableArrayList();

    public FitnessView() {
        root.setSpacing(15);
        root.setPadding(new Insets(20));

        DatePicker datePicker = new DatePicker(LocalDate.now());

        TextField stepsField = new TextField();
        stepsField.setPromptText("Введите шаги");
        stepsField.setPrefHeight(35);
        stepsField.setStyle("-fx-font-size: 16px;");
        stepsField.setPrefWidth(120); // компактное поле

        TextField noteField = new TextField();
        noteField.setPromptText("Введите заметку (до 150 символов)");
        noteField.setPrefWidth(200); // короче
        noteField.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getControlNewText().length() <= 150) {
                return change;
            } else {
                return null;
            }
        }));

        Button saveBtn = new Button("Сохранить");
        Button updateBtn = new Button("Обновить");
        Button deleteBtn = new Button("Удалить");
        Button showAllBtn = new Button("Показать все");

        for (Button b : new Button[]{saveBtn, updateBtn, deleteBtn, showAllBtn}) {
            b.setPrefWidth(100);
            b.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
        }

        HBox buttonBox = new HBox(10, saveBtn, updateBtn, deleteBtn, showAllBtn);

        TableView<StepEntry> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // убираем пустые ячейки

        TableColumn<StepEntry, String> colDate = new TableColumn<>("Дата");
        colDate.setCellValueFactory(v -> new javafx.beans.property.SimpleStringProperty(v.getValue().getDate().toString()));
        colDate.setPrefWidth(100);

        TableColumn<StepEntry, Integer> colSteps = new TableColumn<>("Шаги");
        colSteps.setCellValueFactory(v -> new javafx.beans.property.SimpleIntegerProperty(v.getValue().getSteps()).asObject());
        colSteps.setPrefWidth(80);

        TableColumn<StepEntry, Double> colCal = new TableColumn<>("Ккал");
        colCal.setCellValueFactory(v -> new javafx.beans.property.SimpleDoubleProperty(v.getValue().getCalories()).asObject());
        colCal.setPrefWidth(80);

        TableColumn<StepEntry, String> colNote = new TableColumn<>("Заметка");
        colNote.setCellValueFactory(v -> new javafx.beans.property.SimpleStringProperty(v.getValue().getNote()));
        colNote.setPrefWidth(200);

        // перенос текста в заметке
        colNote.setCellFactory(tc -> {
            TableCell<StepEntry, String> cell = new TableCell<>();
            Text text = new Text();
            text.wrappingWidthProperty().bind(colNote.widthProperty());
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

        table.getColumns().addAll(colDate, colSteps, colCal, colNote);

        allEntries.addAll(storage.load());
        filterByDate(datePicker.getValue(), table);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0, 32000, 4000); // шаг 4000 до 30k
        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        xAxis.setLabel("Дата");
        yAxis.setLabel("Шаги");
        chart.setTitle("Шаги за неделю");
        chart.setPrefHeight(500); // график больше
        updateChart(chart);

        saveBtn.setOnAction(e -> saveOrUpdate(datePicker, stepsField, noteField, table, chart));
        updateBtn.setOnAction(e -> saveOrUpdate(datePicker, stepsField, noteField, table, chart));
        deleteBtn.setOnAction(e -> {
            LocalDate date = datePicker.getValue();
            storage.delete(date);
            allEntries.removeIf(en -> en.getDate().equals(date));
            filterByDate(date, table);
            updateChart(chart);
        });
        showAllBtn.setOnAction(e -> {
            List<StepEntry> sorted = allEntries.stream()
                    .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                    .toList();
            table.setItems(FXCollections.observableArrayList(sorted));
        });

        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> filterByDate(newVal, table));

        root.getChildren().addAll(
                new Label("Дата:"), datePicker,
                new Label("Шаги:"), stepsField,
                new Label("Заметка:"), noteField,
                buttonBox,
                table,
                chart
        );
    }

    private void saveOrUpdate(DatePicker datePicker, TextField stepsField, TextField noteField,
                              TableView<StepEntry> table, LineChart<String, Number> chart) {
        try {
            String txt = stepsField.getText().trim();
            if (txt.isEmpty()) {
                showAlert("Введите число шагов!");
                return;
            }
            int steps = Integer.parseInt(txt);
            String note = noteField.getText().trim();

            StepEntry entry = new StepEntry(datePicker.getValue(), steps, note);

            storage.update(entry);
            allEntries.removeIf(en -> en.getDate().equals(entry.getDate()));
            allEntries.add(entry);

            filterByDate(datePicker.getValue(), table);
            updateChart(chart);
            stepsField.clear();
            noteField.clear();
        } catch (NumberFormatException ex) {
            showAlert("Введите целое число шагов!");
        }
    }

    private void filterByDate(LocalDate date, TableView<StepEntry> table) {
        List<StepEntry> filtered = allEntries.stream().filter(e -> e.getDate().equals(date)).toList();
        table.setItems(FXCollections.observableArrayList(filtered));
    }

    private void updateChart(LineChart<String, Number> chart) {
        chart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Шаги");

        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(6);

        // идём по каждому дню от weekAgo до today
        for (int i = 0; i < 7; i++) {
            LocalDate day = weekAgo.plusDays(i);
            int steps = allEntries.stream()
                    .filter(e -> e.getDate().equals(day))
                    .mapToInt(StepEntry::getSteps)
                    .findFirst()
                    .orElse(0); // если нет записи, показываем 0
            series.getData().add(new XYChart.Data<>(day.toString(), steps));
        }

        chart.getData().add(series);
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.showAndWait();
    }

    public VBox getRoot() {
        return root;
    }
}