package hr.fer.zemris.optjava.dz9;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.*;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class Graph extends JFrame {

    private static final String title = "Fronts";
    private static final Random rand = new Random();
    private static XYSeries added = new XYSeries("Added");

    public Graph(String s, List<List<double[]>> fronts) {
        super(s);
        final ChartPanel chartPanel = createDemoPanel(fronts);
        this.add(chartPanel, BorderLayout.CENTER);
    }

    public static ChartPanel createDemoPanel(List<List<double[]>> fronts) {
        JFreeChart jfreechart = ChartFactory.createScatterPlot(
            title, "X", "Y", createSampleData(fronts),
            PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
        domain.setVerticalTickLabels(true);
        return new ChartPanel(jfreechart);
    }

    public static XYDataset createSampleData(List<List<double[]>> fronts) {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        XYSeries series = new XYSeries("Random");
        
        for (int i = 0; i < fronts.size(); i++) {
            for(int j = 0; j < fronts.get(i).size(); j++) {
            	double[] point = fronts.get(i).get(j);
            	series.add(point[0], point[1]);
            }
        	
            
        }
        xySeriesCollection.addSeries(series);
        xySeriesCollection.addSeries(added);
        return xySeriesCollection;
    }

   
}