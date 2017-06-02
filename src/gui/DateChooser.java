package gui;

import java.util.Date;
import javafx.scene.control.Control;
/*
 * This class we use for choosing date during adding new flight to the Schedule.
 * Author and owner of the code: Toni Epple
 * The code below is not used for commercial purpose.
 */
public class DateChooser extends Control{

    private static final String DEFAULT_STYLE_CLASS = "date-chooser";
    private Date date;

    public DateChooser(Date preset) {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        this.date = preset;
    }

    public DateChooser() {
        this(new Date(System.currentTimeMillis()));
    }

    @Override
    protected String getUserAgentStylesheet() {
        return "gui/calendar.css";
    }

    public Date getDate() {
        return date;
    }
	public String getDateInNiceFormat(){
		
		String month = ""+(date.getMonth()+1);
		
		// Checking if month is less than 10, then adding zero at the beggining if true.
		if(month.length()<2)
			month = "0" + month;
		
		return (date.getYear()+1900)+"-"+month+"-"+
				date.toString().substring(8, 10);
	}
}
