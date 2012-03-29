package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Logic.Appointment;
import Logic.User;
import Server.Database;

public class MineAppointmentsView extends JPanel implements ActionListener {

	private ArrayList<Appointment> appointments;
	private JList list;
	private DefaultListModel listmodel;
	private JButton viewAppointmentButton;
	private User user;
	private GridBagConstraints c;
	private Object mainView;
	
	public MineAppointmentsView(User user, Object mainView) {
		this.mainView = mainView;
		this.user = user;
		listmodel = new DefaultListModel();
		list = new JList(listmodel);
		
		viewAppointmentButton = new JButton("Vis m�te");
		viewAppointmentButton.addActionListener(this);
		
		filllist();
		
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(new JScrollPane(list), c);
		getComponent(0).setPreferredSize(new Dimension(170, 280));

		c.gridy++;
		add(viewAppointmentButton, c);
		
		setVisible(true);
		
	}
	
	public void filllist() {
		
		try {
			appointments = Database.getAppointmentsForUser(user.getUsername());
			//ArrayList<Appointment> temp = Database.getAppointmentsByOwner(user);
			//for (Appointment appointment : temp) {
				//appointments.add(appointment);
			//}
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (ClassNotFoundException e) {
		} catch (SQLException e) {
		}
		
		listmodel.clear();
		for (Appointment appointment : appointments) {
			listmodel.addElement(appointment);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == viewAppointmentButton) {
			
			new MeetingView((Appointment) list.getSelectedValue(), user);
			
		}
		
	}
	
	
}
