package GUI;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Kalenderprogram {
	static JLabel lblMonth, lblYear;
	static JButton btnNext, btnPrev;
	static JTable tblCalendar;
	static JComboBox cmbYear;
	static JFrame frmMain;
	static Container pane;
	static DefaultTableModel mtblCalendar; //Table model
	static JScrollPane stblCalendar; //The scrollpane
	static JPanel pnlCalendar; //The panel
	static int realDay, realMonth, realYear, currentMonth, currentYear, realWeek, currentWeek;
	
	public static void main(String[] args) {
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch (ClassNotFoundException e) {}
		catch (InstantiationException e) {}
		catch (IllegalAccessException e) {}
		catch (UnsupportedLookAndFeelException e) {}
		
		frmMain = new JFrame("Calendar application");
		frmMain.setSize(375, 375);
		pane = frmMain.getContentPane();
		pane.setLayout(null);
		frmMain.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		
		
		lblMonth = new JLabel ("1");
		lblYear = new JLabel ("Change year:");
		cmbYear = new JComboBox();
		btnPrev = new JButton ("<<");
		btnNext = new JButton (">>");
		mtblCalendar = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
		tblCalendar = new JTable(mtblCalendar); //Table using the above model
		stblCalendar = new JScrollPane(tblCalendar); //The scrollpane of the above table
		pnlCalendar = new JPanel(null); //Create the "panel" to place components
		pnlCalendar.setBorder(BorderFactory.createTitledBorder("Calendar")); //Set border

		//Add controls to pane
		pane.add(pnlCalendar);
		pnlCalendar.add(lblMonth);
		pnlCalendar.add(lblYear);
		pnlCalendar.add(cmbYear);
		pnlCalendar.add(btnPrev);
		pnlCalendar.add(btnNext);
		pnlCalendar.add(stblCalendar);

		//Set bounds
		pnlCalendar.setBounds(0, 0, 330, 350);
		lblMonth.setBounds(160-lblMonth.getPreferredSize().width/2, 25, 100, 25);
		lblYear.setBounds(10, 305, 110, 20);
		cmbYear.setBounds(230, 305, 100, 20);
		btnPrev.setBounds(10, 25, 50, 25);
		btnNext.setBounds(260, 25, 50, 25);
		stblCalendar.setBounds(10, 50, 300, 250);

		frmMain.setResizable(false);
		frmMain.setVisible(true);

		//Get real month/year
		GregorianCalendar cal = new GregorianCalendar(); //Create calendar
		realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
		realMonth = cal.get(GregorianCalendar.MONTH); //Get month
		realYear = cal.get(GregorianCalendar.YEAR); //Get year
		realWeek = cal.get(GregorianCalendar.WEEK_OF_YEAR);
		currentMonth = realMonth; //Match month and year
		currentYear = realYear;
		currentWeek = realWeek;
		
		
		//Add headers
		String[] headers = {" ", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; //All headers
		for (int i=0; i<7; i++){
			mtblCalendar.addColumn(headers[i]);
		}
		
		tblCalendar.getParent().setBackground(tblCalendar.getBackground()); //Set background

		//No resize/reorder
		tblCalendar.getTableHeader().setResizingAllowed(false);
		tblCalendar.getTableHeader().setReorderingAllowed(false);

		//Single cell selection
		tblCalendar.setColumnSelectionAllowed(true);
		tblCalendar.setRowSelectionAllowed(true);
		tblCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//Set row/column count
		tblCalendar.setRowHeight(42);
		tblCalendar.setShowGrid(true);
		tblCalendar.setShowVerticalLines(true);
		tblCalendar.setShowHorizontalLines(true);
		tblCalendar.setGridColor(Color.BLACK);
		mtblCalendar.setColumnCount(7);
		mtblCalendar.setRowCount(12);

		
		//Populate combo box
		for (int i=realYear-100; i<=realYear+100; i++){
			cmbYear.addItem(String.valueOf(i));
		}
		
		refreshCalendar (realMonth, realYear, realWeek); // refreshes yo calandah, yeh boi!
		
		//Register action listeners
		btnPrev.addActionListener(new btnPrev_Action());
		btnNext.addActionListener(new btnNext_Action());
		cmbYear.addActionListener(new cmbYear_Action());
		
		
		
	}
	public static void refreshCalendar(int month, int year, int week){
		String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		int nod, som; //Number Of Days, Start Of Month
			
		btnPrev.setEnabled(true); //Enable buttons at first
		btnNext.setEnabled(true);
		if (week == 0 && year <= realYear-10){btnPrev.setEnabled(false);} //Too early
		if (week == 51 && year >= realYear+100){btnNext.setEnabled(false);} //Too late
		lblMonth.setText("Uke: "+(week+1)); //Refresh the month label (at the top)
		lblMonth.setBounds(160-lblMonth.getPreferredSize().width/2, 25, 180, 25); //Re-align label with calendar
		cmbYear.setSelectedItem(String.valueOf(year)); //Select the correct year in the combo box
		
		//Get first day of month and number of days
		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		som = cal.get(GregorianCalendar.DAY_OF_WEEK);
		
		//Clear table
		for (int i=0; i<6; i++){
			for (int j=0; j<7; j++){
				mtblCalendar.setValueAt(null, i, j);
			}
		}

		//Draw calendar
		String[] time = {"07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"};
		for (int i = 0; i < 12; i++) {
			mtblCalendar.setValueAt(time[i], i, 0);
		}
	}
	static class btnPrev_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (currentWeek == 0){ //Back one year
				currentWeek = 51;
				currentYear -= 1;
			}
			else{ //Back one month
				currentWeek -= 1;
			}
			refreshCalendar(currentMonth, currentYear, currentWeek);
		}
	}
	static class btnNext_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (currentWeek == 51){ //Foward one year
				currentWeek = 0;
				currentYear += 1;
			}
			else{ //Foward one month
				currentWeek += 1;
			}
			refreshCalendar(currentMonth, currentYear, currentWeek);
		}
	}
	static class cmbYear_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (cmbYear.getSelectedItem() != null){
				String b = cmbYear.getSelectedItem().toString();
				currentYear = Integer.parseInt(b); //Get the numeric value
				refreshCalendar(currentMonth, currentYear, currentWeek); //Refresh
			}
		}
	}

}
