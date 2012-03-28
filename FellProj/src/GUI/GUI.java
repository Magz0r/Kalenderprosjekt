package GUI;

import javax.swing.*;

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
	
	private JFrame frame;
	private Container pane;
	private JButton opprett, loggUt, refresh;
	private JScrollPane scrNot;
	private JTabbedPane notify;
	private JPanel kalender, nye;
	private MineAppointmentsView mine;
	private JLabel lblMonth;
	private JButton btnNext, btnPrev;
	private JTable tblCalendar;
	private DefaultTableModel mtblCalendar; //Table model
	private JScrollPane stblCalendar; //The scrollpane
	private JPanel pnlCalendar; //The panel
	private int realDay, realMonth, realYear, currentDay, currentMonth, currentYear, realWeek, currentWeek;
	private CreateAppointmentGUI lag;
	private User user;
	private ArrayList<Appointment> list;
	private Appointment meeting;
	private Date date;
	private Calendar cal2;
	private JPanel search;
	
	public GUI(String username) {
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
		refresh = new JButton("Oppdater");
		scrNot = new JScrollPane();
		kalender = new JPanel(null);
		mine = new MineAppointmentsView(user, this);
		nye = new NotificationsView(user);
		search = new SearchUserCalendars(user);
		
		pane.add(kalender);
		pane.add(opprett);
		pane.add(loggUt);
		pane.add(refresh);
		pane.add(scrNot);
		
		opprett.setBounds(10, 32, 225, 64);
		loggUt.setBounds(980, 6, 160-loggUt.getPreferredSize().width/2, 25);
		refresh.setBounds(980, 32, 160-loggUt.getPreferredSize().width/2, 25);
		notify = new JTabbedPane();
		notify.setBounds(10, 137, 224, 441);
		frame.getContentPane().add(notify);
		notify.addTab("Mine", mine);
		notify.addTab("Nye", nye);
		notify.addTab("S¿k", search);
		
		lblMonth = new JLabel ("1");
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
		pnlCalendar.add(btnPrev);
		pnlCalendar.add(btnNext);
		pnlCalendar.add(stblCalendar);

		//Set bounds
		pnlCalendar.setBounds(246, 32, 682, 540);
		lblMonth.setBounds(319, 28, 48, 16);
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

		
		refreshCalendar(); // refreshes yo calandah, yeh boi!
		
		
		//Register action listeners
		btnPrev.addActionListener(new btnPrev_Action());
		btnNext.addActionListener(new btnNext_Action());
		opprett.addActionListener(new opprett_Action());
		loggUt.addActionListener(new loggUt_Action());
		refresh.addActionListener(new refresh_Action());
		
	}
	
	public void refreshCalendar(){
		
		
		btnPrev.setEnabled(true); //Enable buttons at first
		btnNext.setEnabled(true);

		lblMonth.setText("Uke: "+(currentWeek)); //Refresh the month label (at the top)
		
		
		//Clear table
		for (int i=0; i<12; i++){
			for (int j=0; j<8; j++){
				mtblCalendar.setValueAt(null, i, j);
			}
		}

		//Draw calendar
		String[] time = {" ", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "0:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00"};
		for (int i = 1; i < 25; i++) {
			mtblCalendar.setValueAt(time[i], i, 0);
		}
		int[] days ={31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		
		ArrayList<Integer> datoer = new ArrayList<Integer>();
		
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
	public void getAppointments() {
		try {
			list = Database.getAppointmentsForUser(user.getUsername());
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (ClassNotFoundException e) {
		} catch (SQLException e) {
		}
		
		for (int i = 1; i < mtblCalendar.getColumnCount(); i++) {
			for (int j = 1; j < mtblCalendar.getRowCount(); j++) {
				mtblCalendar.setValueAt("", j, i);
			}
		}
		
		System.out.println(list);
		if(!list.isEmpty()){
			System.out.println(list);
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i).getStart().getWeek() == currentWeek){
					for (int j = 1; j < mtblCalendar.getColumnCount(); j++) {
						if (mtblCalendar.getValueAt(0, j).equals(list.get(i).getStart().getDay())) {
							for (int j2 = 1; j2 < mtblCalendar.getRowCount(); j2++) {
								if (mtblCalendar.getValueAt(j2, 0).equals(list.get(i).getStart().getClock())){
									System.out.println(list.get(i).getTitle() + " - start: " + list.get(i).getStart().getClock());
									mtblCalendar.setValueAt(list.get(i).getTitle(), j2, j);
									if(list.get(i).getEnd().getClock() != null){
										for (int k = j2; k < mtblCalendar.getRowCount(); k++) {
											mtblCalendar.setValueAt(list.get(i).getTitle(), k, j);
											if(mtblCalendar.getValueAt(k, 0).equals(list.get(i).getEnd().getClock())){
												mtblCalendar.setValueAt(list.get(i).getTitle(), k, j);
												break;
												
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
			
	}




	
	class btnPrev_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (currentWeek == 1){ //Back one year
				currentWeek = 52;
				currentYear -= 1;
			}
			else{ //Back one month
				currentWeek -= 1;
			}
			refreshCalendar();
		}
	}
	class btnNext_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (currentWeek == 52){ //Foward one year
				currentWeek = 1;
				currentYear += 1;
			}
			else{ //Foward one month
				currentWeek += 1;
			}

			refreshCalendar();

		}
	}
	class refresh_Action implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			refreshCalendar();
			mine.filllist();
		}
		
	}
	
	class opprett_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			lag = new CreateAppointmentGUI(user, false);
		}
	}
	class loggUt_Action implements ActionListener {
		public void actionPerformed (ActionEvent e) {
		
			new Login();
			frame.setVisible(false);
		
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
