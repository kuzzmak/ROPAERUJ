package hr.fer.zemris.optjava.dz10;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.JFrame;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Razred za crtanje xy grafa
 *
 */
public class Graph extends JFrame {

	private static final long serialVersionUID = 1L;

	public Graph(String title, List<Double[]> fv) {
		
		super(title);
		
		XYSeries data = new XYSeries(title);
		
		for (int i = 0; i < fv.size(); i++) {
			data.add(fv.get(i)[0], fv.get(i)[1]);
		}

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(data);
		
		JFreeChart xylineChart = ChartFactory.createXYLineChart(title, "f1", "f2", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		
		XYPlot plot = xylineChart.getXYPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false, true);
		plot.setRenderer(renderer);

		ChartPanel panel = new ChartPanel(xylineChart);
		setContentPane(panel);
		
		int width = 640; 
		int height = 480; 
		File XYChart = new File(title + ".jpeg");
		try {
			ChartUtils.saveChartAsJPEG(XYChart, xylineChart, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}