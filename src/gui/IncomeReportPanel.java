package gui;

import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import operations.Query;

/**
 * @author M&F
 */
public class IncomeReportPanel extends VBox {
	public IncomeReportPanel() {
		// Creates a String array that will contain last 4 months dates.
		String[] monthsDates = new String[4];
		
		// Gets the current month and year and puts it into index 0 of monthsDates
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH) + 1;
		if (month < 10) {
			monthsDates[0] = c.get(Calendar.YEAR) + "-0" + month + "-01";
		} else {
			monthsDates[0] = c.get(Calendar.YEAR) + "-" + month + "-01";
		}
		
		// Calculates the other months dates by using SQL date functions
		monthsDates[1] = Query.getQueryObject().getLastMonthDate(monthsDates[0] + "-01");
		for (int i = 2; i < 4; i++) {
			monthsDates[i] = Query.getQueryObject().getLastMonthDate(monthsDates[i-1]);
		}
		
		// Creates a 4 index int array that will contain last 4 months income amount.
		int[] monthlyIncome = new int[4];
		
		// Gets income for last 4 months and saves the max value
		int maxIncome = Integer.MIN_VALUE;
		for (int i = 0; i < 4; i++) {
			monthlyIncome[i] = Query.getQueryObject().getMonthlyIncome(monthsDates[i]);
			if (monthlyIncome[i] > maxIncome) {
				maxIncome = monthlyIncome[i];
			}
		}
		
		// Removes day from the dates. It was necessary for SQL date function.
		for (int i = 0; i < monthsDates.length; i++) {
			monthsDates[i] = monthsDates[i].substring(0, 7);
		}
		
		// Adds 15% to maxIncome, for a better view in the chart
		maxIncome = maxIncome / 100 * 115;
		
		// Creates chart axes
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setCategories(FXCollections.<String>observableArrayList(monthsDates));
		NumberAxis yAxis = new NumberAxis(null, 0, maxIncome, maxIncome/4);
		yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis,null," DKK"));
		
		// Creates chart data
		ObservableList<BarChart.Data> barChartData = FXCollections.observableArrayList();
		for (int i = (monthlyIncome.length - 1); i >= 0; i--) {
			barChartData.add(new BarChart.Data<>(monthsDates[i], monthlyIncome[i]));
		}
		
		// Creates chart series based on chart data
		ObservableList<BarChart.Series> barChartSeries = FXCollections.observableArrayList(
												new BarChart.Series("Monthly income", barChartData));
		
		// Creates the chart
		BarChart barChart = new BarChart(xAxis, yAxis, barChartSeries, 25);
		
		// Creates the title label
		Label titleL = new Label("Income report for the last 4 months.");
		titleL.setStyle("-fx-font-size: 14pt;");
		
		// Adds padding to main panel
		this.setStyle("-fx-padding: 10;");
		this.setAlignment(Pos.CENTER);
		
		// Adds components to parent
		this.getChildren().addAll(titleL, barChart);
	}
}
