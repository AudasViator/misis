package pro.prieran.misis.ctg;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.QuadCurveBuilder;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class Main extends Application {
    private static final int SCENE_SIZE = 800;
    private static final int CIRCLE_SIZE = 12;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, SCENE_SIZE, SCENE_SIZE, Color.WHITE);

        Group viz1 = new Group();

        int[] i = {0, 1, 2, 1, 0};
        int[] j = {1, 2, 3, 3, 2};

        pro.prieran.misis.ctg.Graph ourGraph = new pro.prieran.misis.ctg.Graph(i, j);

        Graph<Number, Number> graph = ourGraph.makeGraph();

        Layout<Number, Number> circleLayout = new edu.uci.ics.jung.algorithms.layout.KKLayout<>(graph);

        new DefaultVisualizationModel<>(circleLayout, new Dimension(SCENE_SIZE, SCENE_SIZE));

        renderGraph(graph, circleLayout, viz1);

        root.getChildren().add(viz1);
        stage.setScene(scene);
        stage.show();
    }

    private void renderGraph(Graph<Number, Number> graph, Layout<Number, Number> layout, Group viz) {
        final Random random = new Random();
        for (Number n : graph.getEdges()) {
            final Pair<Number> endpoints = graph.getEndpoints(n);

            final Point2D start = layout.apply(endpoints.getFirst());
            final Point2D end = layout.apply(endpoints.getSecond());
            final double middleX = (start.getX() + end.getX()) / 2;
            final double middleY = (start.getY() + end.getY()) / 2;

            final double margin;
            if (random.nextBoolean()) {
                margin = 0.1;
            } else {
                margin = -0.1;
            }
            final Point2D control = new Point2D.Double(middleX * (1 + margin), middleY * (1 - margin));
            final QuadCurve curve = QuadCurveBuilder.create()
                    .startX(start.getX())
                    .startY(start.getY())
                    .endX(end.getX())
                    .endY(end.getY())
                    .controlX(control.getX())
                    .controlY(control.getY())
                    .strokeWidth(4)
                    .fill(null)
                    .stroke(Color.DARKSLATEGRAY)
                    .build();

            viz.getChildren().add(curve);
        }

        for (Number v : graph.getVertices()) {
            final Point2D p = layout.apply(v);
            final Circle circle = CircleBuilder.create()
                    .centerX(p.getX())
                    .centerY(p.getY())
                    .radius(CIRCLE_SIZE)
                    .strokeWidth(1)
                    .fill(Color.YELLOW)
                    .stroke(Color.BLACK)
                    .build();

            viz.getChildren().add(circle);
        }
    }
}
