package hr.fer.zemris.numeric;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Graph extends ApplicationFrame {

	/**
	 * A demonstration application showing an XY series containing a null value.
	 *
	 * @param title the frame title.
	 */
	public Graph(List<double[]> graphData, final String title) {

		super(title);
		final XYSeries series = new XYSeries("Random Data");
//		for(int j = 0; j < graphData.size(); j++) {
//			System.out.println(graphData.get(j)[0] + " " + graphData.get(j)[1]);
//		}
		for(int i = 0; i < graphData.size(); i++) {
//			System.out.println(graphData.get(i)[0] + " " + graphData.get(i)[1]);
			series.add(graphData.get(i)[0], graphData.get(i)[1]);
		}
		
//		series.add(0,10);
//		series.add(0, 20);
//		series.add(-1, 15);
		
		final XYSeriesCollection data = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart("XY Series Demo", "X", "Y", data,
				PlotOrientation.VERTICAL, true, true, false);

		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);

	}
//
//
//	public static void main(final String[] args) {
//
//		final Graph demo = new Graph("XY Series Demo");
//		demo.pack();
//		RefineryUtilities.centerFrameOnScreen(demo);
//		demo.setVisible(true);
//
//	}

}