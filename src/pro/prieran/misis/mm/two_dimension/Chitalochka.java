package pro.prieran.misis.mm.two_dimension;

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
import pro.prieran.misis.mm.Point;
import pro.prieran.misis.mm.two_dimension.interfaces.Func1;
import pro.prieran.misis.mm.two_dimension.interfaces.Func2;
import pro.prieran.misis.mm.two_dimension.interfaces.Values;

import java.util.List;

public class Chitalochka extends Application {
    private static final int SLIDER_DECREASE = 10;

    private static final Func1 T_ALPHA = t -> t * t;
    private static final Func1 T_BETA = x -> x * x;
    private static final Func2 C = (x, t) -> x / t;
    private static final Func2 F = (x, t) -> Math.cos(0.1 * x) * t + 1;

    private static final int COUNT_OF_T_STEPS = 1400;
    private static final double T_FROM = T_ALPHA.get(0);
    private static final double T_TO = 700;

    private static final int COUNT_OF_X_STEPS = 1000;
    private static final double X_FROM = T_BETA.get(0);
    private static final double X_TO = 100;

    private static final Func1 X_STEPS = x -> Math.abs(X_TO - X_FROM) / COUNT_OF_X_STEPS;
    private static final Func1 T_STEPS = t -> Math.abs(T_TO - T_FROM) / COUNT_OF_T_STEPS;

    private Values values;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        solveIt();

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
        initGraph(series, initValue);
        lineChart.getData().add(series);

        Slider slider = makeSlider(initValue);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> initGraph(series, newValue.intValue()));

        GridPane gridPane = makeGridPane();
        gridPane.add(lineChart, 0, 0);
        gridPane.add(slider, 0, 1);


        Scene scene = new Scene(gridPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void solveIt() {
        Solver solver = new Solver();
        values = solver.solve(T_ALPHA, T_BETA, F, C, X_STEPS, T_STEPS, COUNT_OF_X_STEPS, COUNT_OF_T_STEPS, X_FROM);
    }

    private Slider makeSlider(int initValue) {
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax((COUNT_OF_T_STEPS - 1) / SLIDER_DECREASE);
        slider.setValue(initValue);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(1f);
        slider.setBlockIncrement(1f);
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

    private void initGraph(XYChart.Series<Number, Number> series, int t) {
        ObservableList data = series.getData();
        data.clear();
        List<Point> pointsList = values.getValuesForT(t * SLIDER_DECREASE);
        for (int i = 10; i < pointsList.size(); i++) {
            Point point = pointsList.get(i);
            if (point != null) {
                data.add(new XYChart.Data<>(point.x, point.y));
            }
        }
    }
}
