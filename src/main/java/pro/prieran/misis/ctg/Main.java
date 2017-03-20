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

public class Main {
    private static final int SIZE = 800;

    public static void main(String[] args) {

        int[] i = {0, 1, 2, 1, 0};
        int[] j = {1, 2, 3, 3, 2};

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