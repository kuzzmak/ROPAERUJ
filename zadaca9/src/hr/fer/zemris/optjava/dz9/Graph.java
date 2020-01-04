package hr.fer.zemris.optjava.dz9;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Graph extends JFrame {

	private static XYSeries added = new XYSeries("Added");
	private static String chartName;
	private static JFreeChart jfc;

	public Graph(String chartName, List<double[]> fv) {
		super(chartName);
		Graph.chartName = chartName;
		final ChartPanel chartPanel = createDemoPanel(fv);
		this.add(chartPanel, BorderLayout.CENTER);
	}

	private ChartPanel createDemoPanel(List<double[]> fv) {
		Graph.jfc = ChartFactory.createScatterPlot(Graph.chartName, "X", "Y", createSampleData(fv),
				PlotOrientation.VERTICAL, true, true, false);
		 
		XYPlot xyPlot = (XYPlot) Graph.jfc.getPlot();
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);
		XYItemRenderer renderer = xyPlot.getRenderer();
		renderer.setSeriesPaint(0, Color.blue);
		NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
		domain.setVerticalTickLabels(true);

		
		return new ChartPanel(Graph.jfc);
	}

	private XYDataset createSampleData(List<double[]> fv) {
		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		XYSeries series = new XYSeries("Random");

		for (int i = 0; i < fv.size(); i++) {
			double[] point = fv.get(i);
			series.add(point[0], point[1]);
		}

		xySeriesCollection.addSeries(series);
		xySeriesCollection.addSeries(added);
		return xySeriesCollection;
	}
	
	public static void saveChart() {
		
		OutputStream out;
		try {
			ChartPanel cp = new ChartPanel(jfc);
			out = new FileOutputStream(chartName);
			ChartUtils.writeChartAsPNG(out, jfc, cp.getWidth(), cp.getHeight());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}