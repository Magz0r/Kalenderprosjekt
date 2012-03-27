package GUI;

import javax.swing.*;

import javax.swing.event.*;
import javax.swing.table.*;


import Logic.User;
import Logic.Date;
import Logic.Appointment;
import Server.Database;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.*;

public class GUI implements ActionListener {
	
	static JFrame frame;
	static Container pane;
	static JButton opprett, loggUt;
	static JScrollPane scrNot;
	static JTabbedPane notify;
	static JPanel kalender, mine, nye;
	static JLabel lblMonth, lblYear;
	static JButton btnNext, btnPrev;
	static JTable tblCalendar;
	static JComboBox cmbYear;
	static DefaultTableModel mtblCalendar; //Table model
	static JScrollPane stblCalendar; //The scrollpane
	static JPanel pnlCalendar; //The panel
	static int realDay, realMonth, realYear, currentDay, currentMonth, currentYear, realWeek, currentWeek;
	static CreateAppointmentGUI lag;
	static User user;
	static ArrayList<Appointment> list;
	static Appointment meeting;
	static Date date;
	static Calendar cal2;
	
	public GUI(String username){
		try {
			user = Database.getUser(username);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch (ClassNotFoundException e) {}
		catch (InstantiationException e) {}
		catch (IllegalAccessException e) {}
		catch (UnsupportedLookAndFeelException e) {}
		
		frame = new JFrame("Kalender");
		frame.setSize(1100, 600);
		pane = frame.getContentPane();
		pane.setLayout(null);
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		
		opprett = new JButton("Opprett");
		loggUt = new JButton("Logg ut");
		scrNot = new JScrollPane();
		kalender = new JPanel(null);
		mine = new JPanel(null);
		nye = new JPanel(null);
		
		pane.add(kalender);
		pane.add(opprett);
		pane.add(loggUt);
		pane.add(scrNot);
		
		opprett.setBounds(10, 32, 225, 64);
		loggUt.setBounds(980, 6, 160-loggUt.getPreferredSize().width/2, 25);
		notify = new JTabbedPane();
		notify.setBounds(10, 137, 224, 441);
		frame.getContentPane().add(notify);
		notify.addTab("Mine", mine);
		notify.addTab("Nye", nye);
		
		lblMonth = new JLabel ("1");
		lblYear = new JLabel ("Bytt Œr:");
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
		pnlCalendar.setBounds(246, 32, 682, 540);
		lblMonth.setBounds(319, 28, 48, 16);
		lblYear.setBounds(10, 514, 110, 20);
		cmbYear.setBounds(441, 515, 100, 20);
		btnPrev.setBounds(10, 25, 50, 25);
		btnNext.setBounds(626, 25, 50, 25);
		stblCalendar.setBounds(10, 50, 666, 452);


		//Get real month/year
		GregorianCalendar cal = new GregorianCalendar(); //Create calendar
		realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
		realMonth = cal.get(GregorianCalendar.MONTH); //Get month
		realYear = cal.get(GregorianCalendar.YEAR); //Get year
		realWeek = cal.get(GregorianCalendar.WEEK_OF_YEAR);
		currentMonth = realMonth; //Match month and year
		currentYear = realYear;
		currentWeek = realWeek;
		currentDay = realDay;
		
		
		//Add headers
		String[] headers = {" ", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; //All headers
		for (int i=0; i<8; i++){
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
		mtblCalendar.setColumnCount(8);
		mtblCalendar.setRowCount(13);

		
		//Populate combo box
		for (int i=realYear-100; i<=realYear+100; i++){
			cmbYear.addItem(String.valueOf(i));
		}
		
		refreshCalendar (realMonth, realYear, realWeek); // refreshes yo calandah, yeh boi!
		
		//Register action listeners
		btnPrev.addActionListener(new btnPrev_Action());
		btnNext.addActionListener(new btnNext_Action());
		cmbYear.addActionListener(new cmbYear_Action());
		opprett.addActionListener(new opprett_Action());
		loggUt.addActionListener(new loggUt_Action());
		
	}
	public static void refreshCalendar(int month, int year, int week){
		int nod, som; //Number Of Days, Start Of Month
			
		btnPrev.setEnabled(true); //Enable buttons at first
		btnNext.setEnabled(true);
		if (week == 1 && year <= realYear-10){btnPrev.setEnabled(false);} //Too early
		if (week == 52 && year >= realYear+100){btnNext.setEnabled(false);} //Too late
		lblMonth.setText("Uke: "+(week)); //Refresh the month label (at the top)
		cmbYear.setSelectedItem(String.valueOf(year)); //Select the correct year in the combo box
		
		//Get first day of month and number of days
		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		som = cal.get(GregorianCalendar.DAY_OF_WEEK);
		
		//Clear table
		for (int i=0; i<12; i++){
			for (int j=0; j<8; j++){
				mtblCalendar.setValueAt(null, i, j);
			}
		}

		//Draw calendar
		String[] time = {"07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"};
		for (int i = 1; i < 12; i++) {
			mtblCalendar.setValueAt(time[i], i, 0);
		}
		
		int verdi  =  realDay;
		cal2 = Calendar.getInstance();
		int numday = cal2.getActualMaximum(Calendar.DAY_OF_MONTH);
		System.out.println(numday);
		
		for (int i = 0; i < 7; i++) {
			if(currentWeek == realWeek){
				int weekday = cal2.get(Calendar.DAY_OF_WEEK);
				int col = weekday;
				if (col+i >= 8){
					col = 0;
					mtblCalendar.setValueAt(verdi-2+1, 0, col+1);
					break;
				}
				mtblCalendar.setValueAt(verdi+i, 0, col+i);
			}
			else if(currentWeek != realWeek){
				int uim = cal2.get(Calendar.WEEK_OF_MONTH);
				System.out.println(uim);
				if(currentWeek < realWeek && uim < 4){
					int weekday = cal2.get(Calendar.DAY_OF_WEEK);
					int dif = (realWeek - currentWeek)*7 + weekday-1;
					for (int j = 0; j < 7; j++) {
						mtblCalendar.setValueAt(realDay-dif+i, 0, i+1);
					}
					
				}
			}

		}
		
		tblCalendar.setDefaultRenderer(tblCalendar.getColumnClass(0), new tblCalendarRenderer());
		
	}
	public static void getAppointments(){
		try {
			list = Database.getAppointmentsForUser(user.getUsername());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < list.size(); i++) {
			
		}
	}
	static class tblCalendarRenderer extends DefaultTableCellRenderer{
		public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column){
			super.getTableCellRendererComponent(table, value, selected, focused, row, column);

			

			return this;  
		}
	}



	
	static class btnPrev_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (currentWeek == 1){ //Back one year
				currentWeek = 52;
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
			if (currentWeek == 52){ //Foward one year
				currentWeek = 1;
				currentYear += 1;
			}
			else{ //Foward one month
				currentWeek += 1;
			}


			refreshCalendar(currentMonth, currentYear, currentWeek);
		}
	}
	static class opprett_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			lag = new CreateAppointmentGUI(user);
		}
	}
	static class loggUt_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			new Login();
			frame.setVisible(false);
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
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
