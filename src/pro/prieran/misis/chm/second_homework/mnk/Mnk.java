package pro.prieran.misis.chm.second_homework.mnk;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import pro.prieran.misis.Point;

import java.util.ArrayList;
import java.util.List;

public class Mnk extends Application {

    private List<Point> pointsList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        pointsList = new ArrayList<>();

        pointsList.add(new Point(1, 1));
        pointsList.add(new Point(2, -2));
        pointsList.add(new Point(3, 3));
        pointsList.add(new Point(4, 11));
        pointsList.add(new Point(5, 5));
        pointsList.add(new Point(6, 7));
        pointsList.add(new Point(7, 4));
        pointsList.add(new Point(8, 12));
        pointsList.add(new Point(9, 5));
        pointsList.add(new Point(10, 1));


        pointsList.sort(null);

        primaryStage.setTitle("График");

        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("x");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("y");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);

        final int initValue = 0;
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        initGraph(series);
        lineChart.getData().add(series);

        XYChart.Series<Number, Number> seriesApr = new XYChart.Series<>();
        initAprGraph(seriesApr, initValue);
        lineChart.getData().add(seriesApr);

        Slider slider = makeSlider(initValue);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> initAprGraph(seriesApr, newValue.intValue()));

        GridPane gridPane = makeGridPane();
        gridPane.add(lineChart, 0, 0);
        gridPane.add(slider, 0, 1);


        Scene scene = new Scene(gridPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Slider makeSlider(int initValue) {
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(pointsList.size());
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
        columnConstraints.setPercentWidth(100);
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
        for (int i = 0; i < pointsList.size(); i++) {
            Point point = pointsList.get(i);
            if (point != null) {
                data.add(new XYChart.Data<>(point.x, point.y));
            }
        }
    }

    private void initAprGraph(XYChart.Series<Number, Number> series, int maxPow) {
        Polynome polynome = new Polynome();
        Double[] coefs = polynome.gramMatrix(pointsList, maxPow + 1);
        ObservableList data = series.getData();
        data.clear();

        double first = pointsList.get(0).x;
        double last = pointsList.get(pointsList.size() - 1).x;

        for (; first < last; first += 0.1) {
            double value = 0;
            for (int j = 0; j < coefs.length; j++) {
                value += coefs[j] * Math.pow(first, j);
            }
            data.add(new XYChart.Data<>(first, value));
        }
    }
}
