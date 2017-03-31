package pro.prieran.misis.ctg;

import com.google.common.base.Functions;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.BasicVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.GradientVertexRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private static final int SIZE = 800;

    // TODO: Краскала с непересекающимися множествами (сжатием путей); поиск в ширину от данной вершины, выводит список дуг
    // TODO: Поиск в глубину
    // TODO: Дейкстры
    public static void main(String[] args) throws IOException {

        int[] i = {0, 1, 2, 1};
        int[] j = {1, 2, 3, 3};

        Grapf grapf = new Grapf(i, j);

        grapf.delete(0, 1);
        grapf.delete(1, 2);
        grapf.delete(2, 3);
        grapf.delete(1, 3);
        grapf.delete(0, 2);

        grapf.add(0, 1);
        grapf.add(1, 2);
        grapf.add(2, 3);
        grapf.add(1, 3);
        grapf.add(0, 2);
        grapf.add(0, 3);

        printIt(grapf.makeJungGraph());
//        printIt(parseIt().makeJungGraph());
    }

    private static Grapf parseIt() throws IOException {
        Path path = Paths.get("C:\\Users\\prieran\\YandexDisk\\МИСиС\\6семестр\\Графы\\graph.txt");

        BufferedReader reader = Files.newBufferedReader(path);
        String line = reader.readLine();
        String[] numbers = line.split(" ");

        final int countOfVertexes = Integer.parseInt(numbers[0]);
        final int countOfEdges = Integer.parseInt(numbers[1]);

        int[] i = new int[countOfEdges];
        int[] j = new int[countOfEdges];

        for (int k = 0; k < numbers.length; k++) {
            i[k] = Integer.parseInt(numbers[0]);
            j[k] = Integer.parseInt(numbers[1]);
        }

        for (int m = 0; m < countOfEdges; m++) {
            line = reader.readLine();
            numbers = line.split(" ");
            i[m] = Integer.parseInt(numbers[0]);
            j[m] = Integer.parseInt(numbers[1]);
        }

        reader.close();

        return new Grapf(i, j);
    }

    private static void printIt(Graph<Number, Number> graph) {
        VisualizationImageServer<Number, Number> imageServer
                = new VisualizationImageServer<>(new CircleLayout<>(graph), new Dimension(SIZE, SIZE));

        imageServer.getRenderer().setVertexRenderer(
                new GradientVertexRenderer<>(
                        Color.white, Color.LIGHT_GRAY,
                        Color.white, Color.LIGHT_GRAY,
                        imageServer.getPickedVertexState(),
                        false));
        imageServer.getRenderContext().setEdgeDrawPaintTransformer(Functions.constant(Color.DARK_GRAY));
        imageServer.getRenderContext().setArrowFillPaintTransformer(Functions.constant(Color.DARK_GRAY));
        imageServer.getRenderContext().setArrowDrawPaintTransformer(Functions.constant(Color.DARK_GRAY));

        imageServer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        imageServer.getRenderer().getVertexLabelRenderer().setPositioner(new BasicVertexLabelRenderer.OutsidePositioner());
        imageServer.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);

        final JFrame frame = new JFrame();
        Container content = frame.getContentPane();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Image im = imageServer.getImage(new Point2D.Double(SIZE / 2, SIZE / 2), new Dimension(SIZE, SIZE));
        Icon icon = new ImageIcon(im);
        JLabel label = new JLabel(icon);
        content.add(label);
        frame.pack();
        frame.setVisible(true);
    }
}