package GUI;

import javax.swing.*;

import javax.swing.event.*;
import javax.swing.table.*;

import java.awt.Component;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

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
	static JPanel search;
	
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
		mine = new MineAppointmentsView(user);
		nye = new NotificationsView(user);
		search = new SearchUserCalendars(user);
		
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
		notify.addTab("S¿k", search);
		
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
		mtblCalendar.setRowCount(25);

		
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
		
		
		btnPrev.setEnabled(true); //Enable buttons at first
		btnNext.setEnabled(true);
		if (week == 1 && year <= realYear-10){btnPrev.setEnabled(false);} //Too early
		if (week == 52 && year >= realYear+100){btnNext.setEnabled(false);} //Too late
		lblMonth.setText("Uke: "+(week)); //Refresh the month label (at the top)
		cmbYear.setSelectedItem(String.valueOf(year)); //Select the correct year in the combo box
		
		
		//Clear table
		for (int i=0; i<12; i++){
			for (int j=0; j<8; j++){
				mtblCalendar.setValueAt(null, i, j);
			}
		}

		//Draw calendar
		String[] time = {" ", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00"};
		for (int i = 1; i < 25; i++) {
			mtblCalendar.setValueAt(time[i], i, 0);
		}
		int[] days ={31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		
		ArrayList datoer = new ArrayList<Integer>();
		
		for (int i = 0; i < days.length; i++) {
			for (int j = 0; j < days[i]; j++) {
				datoer.add(j+1);
			}
		}
		
		for (int i = 1; i < 8; i++) {
			if(currentWeek != 1)
				mtblCalendar.setValueAt(datoer.get(currentWeek*7-9+i), 0, i);
			else
				mtblCalendar.setValueAt(datoer.get(i-1), 0, i);
		}
		
		getAppointments();
		
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
			System.out.println(list.get(i).getStart().getWeek()); 
			System.out.println(currentWeek + "kk");
		}
		
		if(!list.isEmpty()){
			System.out.println("0");
			for (int i = 0; i < list.size(); i++) {
				System.out.println("1st");
				if(list.get(i).getStart().getWeek() == currentWeek){
					System.out.println("2nd");
					for (int j = 1; j < mtblCalendar.getColumnCount(); j++) {
						System.out.println("3rd");
						if (mtblCalendar.getValueAt(0, j).equals(list.get(i).getStart().getDay())) {
							System.out.println("4th");
							for (int j2 = 1; j2 < mtblCalendar.getRowCount(); j2++) {
								System.out.println("5th");
								if (mtblCalendar.getValueAt(j2, 0).equals(list.get(i).getStart().getClock())){
									System.out.println("6th");
									mtblCalendar.setValueAt(list.get(i).getTitle(), j2, j);
								}
							}
						}
					}
				}
			}
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
