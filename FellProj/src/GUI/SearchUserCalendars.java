package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import Logic.Appointment;
import Logic.User;
import Server.Database;

public class SearchUserCalendars extends JPanel implements ActionListener, KeyListener {

	private ArrayList<User> users;
	private JList list;
	private DefaultListModel listmodel;
	private JButton viewCalendar, viewMyCalendar;
	private JTextField searchField;
	private User myUser;
	private GridBagConstraints c;
	
	public SearchUserCalendars(User user) {
		this.myUser = user;
		listmodel = new DefaultListModel();
		list = new JList(listmodel);
		
		searchField = new JTextField();
		searchField.addKeyListener(this);
		
		viewCalendar = new JButton("Vis brukerens kalender");
		viewCalendar.addActionListener(this);
		viewMyCalendar = new JButton("Vis min kalender");
		viewMyCalendar.addActionListener(this);
		
		filllist();
		
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		add(searchField, c);
		c.gridy++;
		
		add(new JScrollPane(list), c);
		getComponent(1).setPreferredSize(new Dimension(170, 250));

		c.gridy++;
		add(viewCalendar, c);
		c.gridy++;
		add(viewMyCalendar, c);
		
		setVisible(true);
	}
	
	private void filllist() {
		
		try {
			users = Database.getAllUsers();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (ClassNotFoundException e) {
		} catch (SQLException e) {
		}

		listmodel.clear();
		for (User user : users) {
			if(user.getName().toLowerCase().matches(".*"+searchField.getText().toLowerCase()+".*") && !user.getUsername().equals(myUser.getUsername())) {
				listmodel.addElement(user);
			}
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == viewCalendar && list.getSelectedValue() != null) {
			System.out.println("viser " + list.getSelectedValue() + " sin kalender");
			//vis kalender
		}
		else if(e.getSource() == viewMyCalendar) {
			System.out.println("viser din kalender");
			//vis min kalender
		}
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getSource() == searchField) {
			filllist();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
	
	
}
