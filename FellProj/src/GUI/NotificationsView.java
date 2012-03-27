package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Logic.Notification;
import Logic.User;
import Server.Database;

public class NotificationsView extends JPanel implements ActionListener, ListSelectionListener {

	private ArrayList<Notification> notifications;
	private JList list;
	private DefaultListModel listmodel;
	private User user;
	private JTextArea textarea;
	private JButton read;
	private GridBagConstraints c;
	
	public NotificationsView(User user) {
		
		this.user = user;
		listmodel = new DefaultListModel();
		list = new JList(listmodel);
		list.addListSelectionListener(this);
		list.setPreferredSize(new Dimension(160, 250));
		list.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		textarea = new JTextArea(4, 14);
		textarea.setLineWrap(true);
		textarea.setWrapStyleWord(true);
		textarea.setEditable(false);
		textarea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		textarea.setVisible(false);
		read = new JButton("Merk som lest");
		read.addActionListener(this);
		read.setVisible(false);
		
		filllist();
		
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(list, c);

		c.gridy++;
		add(textarea, c);

		c.gridy++;
		add(read, c);
		
		//test
		setVisible(true);

		
	}
	
	private void filllist() {
		
		try {
			notifications = Database.getNotifications(user.getUsername());
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (ClassNotFoundException e) {
		} catch (SQLException e) {
		}
		
		listmodel.clear();
		for (Notification notification : notifications) {
			listmodel.addElement(notification);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == read) {
			try {
				Database.setNotificationRead((Notification) list.getSelectedValue(), true);
			} catch (InstantiationException e1) {
			} catch (IllegalAccessException e1) {
			} catch (ClassNotFoundException e1) {
			} catch (SQLException e1) {
			}
			
			filllist();
			list.setSelectedIndex(-1);
		}
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(list.getSelectedValue() != null) {
			textarea.setText(((Notification) list.getSelectedValue()).getText());
			textarea.setVisible(true);
			read.setVisible(true);
		} else {
			textarea.setVisible(false);
			read.setVisible(false);
		}
		
	}
	
	
}
