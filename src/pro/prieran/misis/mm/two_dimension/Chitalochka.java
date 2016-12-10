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
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import pro.prieran.misis.Point;

import java.util.List;

import static java.lang.Math.abs;

public class Chitalochka extends Application {

    /**
     * Слайдер изменяет значение t, но стоит выводит в это число раз меньше точек
     */
    private static final int SLIDER_DECREASE = 10;

    /**
     * Аналитическое решение, нашлось руками
     */
    private static final Function2<Double, Double, Double> ANALYTIC_SOLUTION = (x, t) -> x * x - t * t;

    // Начальные условия
    private static final Function2<Double, Double, Double> C = (x, t) -> 1.0;
    private static final Function2<Double, Double, Double> F = (x, t) -> -2 * t + 2 * x;

    private static final Function1<Double, Double> X_INITIAL = x -> ANALYTIC_SOLUTION.invoke(x, 0.0);
    private static final Function1<Double, Double> T_INITIAL = t -> ANALYTIC_SOLUTION.invoke(0.0, t);

    private static final int COUNT_OF_T_STEPS = 1000;
    private static final double T_FROM = 0;
    private static final double T_TO = 10;

    private static final int COUNT_OF_X_STEPS = 1000;
    private static final double X_FROM = 0;
    private static final double X_TO = 10;

    /**
     * Возвращает шаг по оси X
     */
    private static final Function1<Integer, Double> X_STEPS = x -> abs(X_TO - X_FROM) / COUNT_OF_X_STEPS;

    /**
     * Возвращает шаг по оси T
     */
    private static final Function1<Integer, Double> T_STEPS = t -> abs(T_TO - T_FROM) / COUNT_OF_T_STEPS;

    /**
     * Посчитанные значения
     */
    private Values numericalSolution;

    /**
     * Значения t для каждого шага
     */
    private double[] tValues;

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
        lineChart.setAnimated(false);

        final int initialKValue = 0;

        XYChart.Series<Number, Number> solvedSeries = new XYChart.Series<>();
        solvedSeries.setName("Численно");
        initSolvedGraph(solvedSeries, initialKValue);
        lineChart.getData().add(solvedSeries);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Аналитически");
        initGraph(series, initialKValue);
        lineChart.getData().add(series);

        Slider slider = makeSlider(initialKValue);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> initSolvedGraph(solvedSeries, newValue.intValue() * SLIDER_DECREASE));
        slider.valueProperty().addListener((observable, oldValue, newValue) -> initGraph(series, newValue.intValue() * SLIDER_DECREASE));

        GridPane gridPane = makeGridPane();
        gridPane.add(lineChart, 0, 0);
        gridPane.add(slider, 0, 1);

        Scene scene = new Scene(gridPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void solveIt() {
        // Считаем значения t, подставляются в аналитическое решение
        tValues = new double[COUNT_OF_T_STEPS + 1];
        tValues[0] = T_FROM;
        for (int i = 0; i < COUNT_OF_T_STEPS; i++) {
            tValues[i + 1] += tValues[i] + T_STEPS.invoke(i);
        }

        Solver solver = new Solver();
        numericalSolution = solver.solve(T_INITIAL, X_INITIAL, F, C, X_STEPS, T_STEPS, COUNT_OF_X_STEPS, COUNT_OF_T_STEPS, X_FROM, T_FROM);
    }

    private Slider makeSlider(int initValue) {
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax((COUNT_OF_T_STEPS - 1) / SLIDER_DECREASE);
        slider.setValue(initValue);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(false);
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

    private void initSolvedGraph(XYChart.Series<Number, Number> series, int t) {
        ObservableList data = series.getData();
        data.clear();
        List<Point> pointsList = numericalSolution.getValuesForT(t);
        for (int i = 0; i < pointsList.size(); i += 20) { // Для наглядности покажем не все точки
            Point point = pointsList.get(i);
            data.add(new XYChart.Data<>(point.x, point.y));
        }
    }

    private void initGraph(XYChart.Series<Number, Number> series, int t) {
        ObservableList data = series.getData();
        data.clear();
        List<Point> pointsList = numericalSolution.getValuesForT(t);
        for (int i = 0; i < pointsList.size(); i += 40) { // Для наглядности покажем не все точки
            Point point = pointsList.get(i);
            data.add(new XYChart.Data<>(point.x, ANALYTIC_SOLUTION.invoke(point.x, tValues[t])));
        }
    }
}