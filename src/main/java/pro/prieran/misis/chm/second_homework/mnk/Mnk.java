package pro.prieran.misis.chm.second_homework.mnk;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.util.Callback;
import pro.prieran.misis.Point;

import java.text.DecimalFormat;
import java.text.Format;

public class Mnk extends Application {

    private ObservableList<Point> points =
            FXCollections.observableArrayList(
                    new Point(1, 1),
                    new Point(2, 4),
                    new Point(3, 9),
                    new Point(4, 1),
                    new Point(5, 25),
                    new Point(6, 12),
                    new Point(7, 49),
                    new Point(8, 64),
                    new Point(9, 81),
                    new Point(10, 10)
            );

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        points.sort(null);

        primaryStage.setTitle("График");

        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("x");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("y");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setLegendVisible(true);
        lineChart.setAnimated(false);

        final int initValue = 0;
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Значения");
        initGraph(series);
        lineChart.getData().add(series);

        XYChart.Series<Number, Number> seriesApr = new XYChart.Series<>();
        seriesApr.setName("Аппроксимация");
        initAprGraph(seriesApr, initValue);
        lineChart.getData().add(seriesApr);

        Slider slider = makeSlider(initValue);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> initAprGraph(seriesApr, newValue.intValue()));

        TableView table = makeTableView();

        GridPane gridPane = makeGridPane();
        gridPane.add(lineChart, 0, 0);
        gridPane.add(slider, 0, 1);
        gridPane.add(table, 1, 0, 1, 2);

        Scene scene = new Scene(gridPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableView makeTableView() {
        TableView table = new TableView();
        table.setFocusTraversable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn xCol = new TableColumn("x");
        xCol.setCellValueFactory(new PropertyValueFactory<Point, Double>("x"));
        xCol.setCellFactory(new ColumnFormatter<Point, Double>(new DecimalFormat("0.0000")));
        xCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn yCol = new TableColumn("y");
        yCol.setCellValueFactory(new PropertyValueFactory<Point, Double>("y"));
        yCol.setCellFactory(new ColumnFormatter<Point, Double>(new DecimalFormat("0.0000")));
        yCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn yAprCol = new TableColumn("yApr");
        yAprCol.setCellValueFactory(new PropertyValueFactory<Point, Double>("yApr"));
        yAprCol.setCellFactory(new ColumnFormatter<Point, Double>(new DecimalFormat("0.0000")));
        yAprCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn deltaCol = new TableColumn("Delta");
        deltaCol.setCellValueFactory(new PropertyValueFactory<Point, Double>("delta"));
        deltaCol.setCellFactory(new ColumnFormatter<Point, Double>(new DecimalFormat("0.0000")));
        deltaCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        table.getColumns().addAll(xCol, yCol, yAprCol, deltaCol);

        table.setItems(points);

        return table;
    }

    private Slider makeSlider(int initValue) {
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(points.size());
        slider.setValue(initValue);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(1f);
        slider.setBlockIncrement(1f);
        slider.setSnapToTicks(true);
        slider.setMinorTickCount(0);
        return slider;
    }

    private GridPane makeGridPane() {
        GridPane gridPane = new GridPane();

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().add(columnConstraints);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS);
        gridPane.getRowConstraints().add(rowConstraints);

        return gridPane;
    }

    private void initGraph(XYChart.Series<Number, Number> series) {
        ObservableList data = series.getData();
        data.clear();
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (point != null) {
                data.add(new XYChart.Data<>(point.x, point.y));
            }
        }
    }

    private void initAprGraph(XYChart.Series<Number, Number> series, int maxPow) {
        Approximator approximator = new Approximator(points, maxPow);
        ObservableList data = series.getData();
        data.clear();

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (point != null) {
                double value = approximator.getValue(point.x);
                point.yAprProperty().set(value);
                point.deltaProperty().set(value - point.y);
            }
        }

        double first = points.get(0).x;
        double last = points.get(points.size() - 1).x;

        for (; first < last; first += 0.1) {
            double value = approximator.getValue(first);
            data.add(new XYChart.Data<>(first, value));
        }
    }

    private class ColumnFormatter<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {
        private Format format;

        ColumnFormatter(Format format) {
            super();
            this.format = format;
        }

        @Override
        public TableCell<S, T> call(TableColumn<S, T> arg0) {
            return new TableCell<S, T>() {
                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(new Label(format.format(item)));
                    }
                }
            };
        }
    }
}