package pro.prieran.misis.chm.second_homework.brown;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("График");

        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("x");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("y");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setLegendVisible(true);
        lineChart.setAnimated(false);

        final int initValue = 0;

        XYChart.Series<Number, Number> seriesApr = new XYChart.Series<>();
        seriesApr.setName("Аппроксимация");
        initAprGraph(seriesApr, initValue);
        lineChart.getData().add(seriesApr);


        Scene scene = new Scene(lineChart, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initAprGraph(XYChart.Series<Number, Number> series, int maxPow) {
        ObservableList data = series.getData();
        data.clear();
    }
}
