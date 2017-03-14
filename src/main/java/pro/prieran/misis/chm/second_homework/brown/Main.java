package pro.prieran.misis.chm.second_homework.brown;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    private static final int COUNT_OF_VISIBLE_POINTS = 200;
    private static final double FROM = -2.0;
    private static final double TO = 4.0;

    private final Brown brown = new Brown();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        brown.doIt();
        List<Double> xPoints = brown.getXPoints();
        List<Double> yPoints = brown.getYPoints();
        List<Double> zPoints = brown.getZPoints();

        primaryStage.setTitle("График");

        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("x");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("y");
        final NumberAxis zAxis = new NumberAxis();
        zAxis.setLabel("z");
        final NumberAxis vAxis = new NumberAxis();
        zAxis.setLabel("value");

        /*
            Всё про X
        */
        final LineChart<Number, Number> lineChartX = new LineChart<>(xAxis, vAxis);
        lineChartX.setLegendVisible(true);
        lineChartX.setAnimated(false);

        XYChart.Series<Number, Number> seriesF1X = new XYChart.Series<>();
        seriesF1X.setName("X_F1");
        initXGraph(seriesF1X, 1);
        lineChartX.getData().add(seriesF1X);

        XYChart.Series<Number, Number> seriesF2X = new XYChart.Series<>();
        seriesF2X.setName("X_F2");
        initXGraph(seriesF2X, 2);
        lineChartX.getData().add(seriesF2X);

        XYChart.Series<Number, Number> seriesF3X = new XYChart.Series<>();
        seriesF3X.setName("X_F3");
        initXGraph(seriesF3X, 3);
        lineChartX.getData().add(seriesF3X);

        XYChart.Series<Number, Number> seriesX = new XYChart.Series<>();
        seriesX.setName("Шаги по X");
        ObservableList dataX = seriesX.getData();
        dataX.clear();
        for (int i = 0; i < xPoints.size(); i++) {
            dataX.add(new XYChart.Data<>(xPoints.get(i), 0.0));
        }
        lineChartX.getData().add(seriesX);

        /*
            Всё про Y
        */
        final LineChart<Number, Number> lineChartY = new LineChart<>(yAxis, vAxis);
        lineChartY.setLegendVisible(true);
        lineChartY.setAnimated(false);

        XYChart.Series<Number, Number> seriesF1Y = new XYChart.Series<>();
        seriesF1Y.setName("Y_F1");
        initYGraph(seriesF1Y, 1);
        lineChartY.getData().add(seriesF1Y);

        XYChart.Series<Number, Number> seriesF2Y = new XYChart.Series<>();
        seriesF2Y.setName("Y_F2");
        initYGraph(seriesF2Y, 2);
        lineChartY.getData().add(seriesF2Y);

        XYChart.Series<Number, Number> seriesF3Y = new XYChart.Series<>();
        seriesF3Y.setName("Y_F3");
        initYGraph(seriesF3Y, 3);
        lineChartY.getData().add(seriesF3Y);

        XYChart.Series<Number, Number> seriesY = new XYChart.Series<>();
        seriesY.setName("Шаги по Y");
        ObservableList dataY = seriesY.getData();
        dataY.clear();
        for (int i = 0; i < yPoints.size(); i++) {
            dataY.add(new XYChart.Data<>(yPoints.get(i), 0.0));
        }
        lineChartY.getData().add(seriesY);

        /*
            Всё про Z
        */
        final LineChart<Number, Number> lineChartZ = new LineChart<>(zAxis, vAxis);
        lineChartZ.setLegendVisible(true);
        lineChartZ.setAnimated(false);

        XYChart.Series<Number, Number> seriesF1Z = new XYChart.Series<>();
        seriesF1Z.setName("Z_F1");
        initZGraph(seriesF1Z, 1);
        lineChartZ.getData().add(seriesF1Z);

        XYChart.Series<Number, Number> seriesF2Z = new XYChart.Series<>();
        seriesF2Z.setName("Z_F2");
        initZGraph(seriesF2Z, 2);
        lineChartZ.getData().add(seriesF2Z);

        XYChart.Series<Number, Number> seriesF3Z = new XYChart.Series<>();
        seriesF3Z.setName("Z_F3");
        initZGraph(seriesF3Z, 3);
        lineChartZ.getData().add(seriesF3Z);

        XYChart.Series<Number, Number> seriesZ = new XYChart.Series<>();
        seriesZ.setName("Шаги по Z");
        ObservableList dataZ = seriesZ.getData();
        dataZ.clear();
        for (int i = 0; i < zPoints.size(); i++) {
            dataZ.add(new XYChart.Data<>(zPoints.get(i), 0.0));
        }
        lineChartZ.getData().add(seriesZ);


        /*
            Всё про вкладки
        */
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tabX = new Tab();
        tabX.setText("Ось X");
        tabX.setContent(lineChartX);
        tabPane.getTabs().add(tabX);

        Tab tabY = new Tab();
        tabY.setText("Ось Y");
        tabY.setContent(lineChartY);
        tabPane.getTabs().add(tabY);

        Tab tabZ = new Tab();
        tabZ.setText("Ось Z");
        tabZ.setContent(lineChartZ);
        tabPane.getTabs().add(tabZ);

        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initXGraph(XYChart.Series<Number, Number> series, int numberOfFunction) {
        ObservableList data = series.getData();
        data.clear();

        for (double x = FROM; x < TO; x += (TO - FROM) / COUNT_OF_VISIBLE_POINTS) {
            double value;
            switch (numberOfFunction) {
                case 1:
                    value = brown.f1(x, 1.50389, 3.03557);
                    break;
                case 2:
                    value = brown.f2(x, 1.50389, 3.03557);
                    break;
                case 3:
                    value = brown.f3(x, 1.50389, 3.03557);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            data.add(new XYChart.Data<>(x, value));
        }
    }

    private void initYGraph(XYChart.Series<Number, Number> series, int numberOfFunction) {
        ObservableList data = series.getData();
        data.clear();

        for (double y = FROM; y < TO; y += (TO - FROM) / COUNT_OF_VISIBLE_POINTS) {
            double value;
            switch (numberOfFunction) {
                case 1:
                    value = brown.f1(1.00084, y, 3.03557);
                    break;
                case 2:
                    value = brown.f2(1.00084, y, 3.03557);
                    break;
                case 3:
                    value = brown.f3(1.00084, y, 3.03557);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            data.add(new XYChart.Data<>(y, value));
        }
    }

    private void initZGraph(XYChart.Series<Number, Number> series, int numberOfFunction) {
        ObservableList data = series.getData();
        data.clear();

        for (double z = FROM; z < TO; z += (TO - FROM) / COUNT_OF_VISIBLE_POINTS) {
            double value;
            switch (numberOfFunction) {
                case 1:
                    value = brown.f1(1.00084, 1.50389, z);
                    break;
                case 2:
                    value = brown.f2(1.00084, 1.50389, z);
                    break;
                case 3:
                    value = brown.f3(1.00084, 1.50389, z);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            data.add(new XYChart.Data<>(z, value));
        }
    }
}