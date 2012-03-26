package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Logic.Appointment;
import Logic.Date;
import Logic.Room;
import Logic.User;
import Server.Database;

public class CreateAppointmentGUI extends JPanel implements ActionListener, KeyListener, ListSelectionListener {

	private JFrame frame;
	private JLabel newAppointmentLabel, fromLabel, toLabel, titleLabel, descriptionLabel, addUserLabel, roomLabel, participantsLabel, visability;
	private JTextField fromDateField, fromClockField, toDateField, toClockField, titleField, searchUserField;
	private JTextArea descriptionArea;
	private JList userList, roomList, participantsList;
	private GridBagConstraints c;
	private JButton removeUserButton, removeAllUsersButton, saveButton, cancelButton, deleteButton, searchRoomButton;
	private DefaultListModel userListModel, roomListModel, participantListModel;
	private JScrollPane userScroll, roomScroll, participantsScroll;
	private ArrayList<User> allUsers;
	private ArrayList<Room> allRooms;
	private ButtonGroup radioGroup;
	private JRadioButton personal, published;
	private User user;
	private JPanel visabilityPanel;
	private MeetingView meet;
	
	public static void main(String[] args) {
		new CreateAppointmentGUI(new User("tand", null, "tandberg"));
	}
	public CreateAppointmentGUI(User username) {
		frame = new JFrame();
		this.user = username;
		
		allUsers = null;
		try {
			allUsers = Database.getAllUsers();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (ClassNotFoundException e) {
		} catch (SQLException e) {}
		
		
		//Listmodels
		userListModel = new DefaultListModel();
		roomListModel = new DefaultListModel();
		participantListModel = new DefaultListModel();
		
		
		//labels
		newAppointmentLabel = new JLabel("Ny avtale");
		newAppointmentLabel.setFont(new Font("Arial", Font.BOLD, 24));
		fromLabel = new JLabel("Fra:");
		toLabel = new JLabel("Til:");
		titleLabel = new JLabel("Tittel:");
		descriptionLabel = new JLabel("Møtetekst:");
		addUserLabel = new JLabel("Legg til deltager");
		roomLabel = new JLabel("Rom");
		participantsLabel = new JLabel("Deltagere");
		visability = new JLabel("Synlighet");
		
		//textfield
		fromDateField = new JTextField("DD.MM.YYYY", 8);
		fromClockField = new JTextField("HH:MM", 4);
		toDateField = new JTextField("DD.MM.YYYY", 8);
		toClockField = new JTextField("HH:MM", 4);
		titleField = new JTextField(10);
		searchUserField = new JTextField(10);
		searchUserField.addKeyListener(this);
	
		
		//textarea
		descriptionArea = new JTextArea(15, 15);
		descriptionArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		//lists
		userList = new JList(userListModel);
		userList.addListSelectionListener(this);
		roomList = new JList(roomListModel);
		participantsList = new JList(participantListModel);
		
		//scrollpane
		userScroll = new JScrollPane(userList);
		userScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		userScroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		userScroll.setPreferredSize(new Dimension(130, 250));
		fillUsers();
		
		roomScroll = new JScrollPane(roomList);
		roomScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		roomScroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		roomScroll.setPreferredSize(new Dimension(130, 250));
		
		participantsScroll = new JScrollPane(participantsList);
		participantsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		participantsScroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		participantsScroll.setPreferredSize(new Dimension(170, 250));
		
		//buttons
		removeUserButton = new JButton("Fjern");
		removeUserButton.addActionListener(this);
		removeUserButton.setActionCommand("DeleteParticipant");
		removeAllUsersButton = new JButton("Fjern alle");
		removeAllUsersButton.addActionListener(this);
		removeAllUsersButton.setActionCommand("DeleteAllParticipants");
		saveButton = new JButton("Lagre");
		cancelButton = new JButton("Avbryt");
		deleteButton = new JButton("Slett");
		searchRoomButton = new JButton("Se etter rom");
		
		//radiobuttons
		personal = new JRadioButton("Privat");
		personal.setSelected(true);
		published = new JRadioButton("Offentlig");
		radioGroup = new ButtonGroup();
		radioGroup.add(personal);
		radioGroup.add(published);
		visabilityPanel = new JPanel();
		visabilityPanel.setLayout(new GridBagLayout());
		GridBagConstraints a = new GridBagConstraints();
		a.anchor = GridBagConstraints.WEST;
		a.gridy = 0;
		visabilityPanel.add(personal, a);
		a.gridy++;
		visabilityPanel.add(published, a);
		
		//actioncommands
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("Cancel");
		saveButton.addActionListener(this);
		saveButton.setActionCommand("Save");
		deleteButton.addActionListener(this);
		deleteButton.setActionCommand("Delete");
		searchRoomButton.addActionListener(this);
		searchRoomButton.setActionCommand("searchRoom");
		
		setPreferredSize(new Dimension(1100, 600));
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(3, 3, 3, 3);
		add(newAppointmentLabel, c);
		
		c.gridy = 1;
		add(fromLabel, c);
		
		c.gridy = 2;
		add(toLabel, c);
		
		c.gridy = 3;
		add(titleLabel, c);
		
		c.gridy = 4;
		add(descriptionLabel, c);
		
		c.gridx = 1;
		c.gridy = 1;
		add(fromDateField, c);
		c.anchor = GridBagConstraints.EAST;
		add(fromClockField, c);
		

		c.anchor = GridBagConstraints.WEST;
		c.gridy = 2;
		add(toDateField, c);
		c.anchor = GridBagConstraints.EAST;
		add(toClockField, c);
		

		c.anchor = GridBagConstraints.WEST;
		c.gridy = 3;
		add(titleField, c);
		
		c.gridy = 4;
		add(descriptionArea, c);
		
		c.gridx = 2;
		c.gridy = 2;
		add(addUserLabel, c);
		
		c.gridy = 3;
		add(searchUserField, c);
		
		c.gridy = 4;
		add(userScroll, c);
		
		c.gridx = 3;
		c.gridy = 2;
		add(roomLabel, c);
		
		c.gridy = 3;
		add(searchRoomButton, c);
		
		c.gridy = 4;
		add(roomScroll, c);
		
		
		c.gridx = 4;
		c.gridy = 3;
		add(participantsLabel, c);
		
		c.gridy = 4;
		add(participantsScroll, c);
		
		c.gridy = 5;
		add(removeUserButton, c);
		c.anchor = GridBagConstraints.EAST;
		add(removeAllUsersButton, c);
		

		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 5;
		add(saveButton, c);
		c.gridx++;
		add(cancelButton, c);
		c.anchor = GridBagConstraints.EAST;
		add(deleteButton,c);
		
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 5;
		c.gridy = 3;
		add(visability, c);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridy++;
		add(visabilityPanel, c);
		
		
		setVisible(true);

		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public CreateAppointmentGUI(Appointment appointment, User user) {
		this(user);
		// set fields.
	}
	
	
	private void fillRooms() {
		try {
			String[] startdato = fromDateField.getText().split("\\.");
			String[] starttid = fromClockField.getText().split(":");
			
			System.out.println(fromDateField.getText() + " - " + startdato.length);

			Date start = new Date(Integer.parseInt(startdato[2]), Integer.parseInt(startdato[1]), Integer.parseInt(startdato[0]), Integer.parseInt(starttid[0]), Integer.parseInt(starttid[1]));

			String[] sluttdato = toDateField.getText().split("\\.");
			String[] slutttid = toClockField.getText().split(":");

			Date end = new Date(Integer.parseInt(sluttdato[2]), Integer.parseInt(sluttdato[1]), Integer.parseInt(sluttdato[0]), Integer.parseInt(slutttid[0]), Integer.parseInt(slutttid[1]));
			
			
			try {
				allRooms = Database.getAvailableRooms(participantListModel.getSize(), start, end);
				roomListModel.clear();
				for (Room room : allRooms) {
					roomListModel.addElement(room);
				}
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			} catch (ClassNotFoundException e) {
			} catch (SQLException e) {
			}
		} catch(Exception exception) {
			JOptionPane.showMessageDialog(this, "Sjekk tidpunktene, formatet skal være: DD.MM.YYYY og HH:MM");
		}
		
	}


	private void fillUsers() {
		userListModel.clear();
		for (User user : allUsers) {
			if(user.getName().toLowerCase().matches(".*"+searchUserField.getText().toLowerCase()+".*")) {
				userListModel.addElement(user);
			}
			
		}
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Cancel")) {
			int ans = JOptionPane.showConfirmDialog(this, "Sikker på at du vil avbryte?", "Avbryt", JOptionPane.YES_NO_OPTION);
			if(ans == 0) {
				frame.setVisible(false);
				new Login();
			}
			
		}
		else if(e.getActionCommand().equals("Save")) {
			//frame.setVisible(false);
			//new Gui(user)
			
			if(!titleField.getText().equals("")) {
				
				if(!descriptionArea.getText().equals("")) {
					
					if(roomList.getSelectedValue() != null) {
						try {
							Room room = (Room) roomList.getSelectedValue();

							String[] startdato = fromDateField.getText().split("\\.");
							String[] starttid = fromClockField.getText().split(":");
							Date start = new Date(Integer.parseInt(startdato[2]), Integer.parseInt(startdato[1]), Integer.parseInt(startdato[0]), Integer.parseInt(starttid[0]), Integer.parseInt(starttid[1]));

							String[] sluttdato = toDateField.getText().split("\\.");
							String[] slutttid = toClockField.getText().split(":");

							Date end = new Date(Integer.parseInt(sluttdato[2]), Integer.parseInt(sluttdato[1]), Integer.parseInt(sluttdato[0]), Integer.parseInt(slutttid[0]), Integer.parseInt(slutttid[1]));

							String title = titleField.getText();
							String description = descriptionArea.getText();
							boolean hidden = personal.isSelected();

							Appointment appointment = new Appointment(room, start, end, user, title, description, hidden);

							try {
								if(JOptionPane.showConfirmDialog(this, "Er du sikker på at du vil opprette avtalen", "Opprette avtale", JOptionPane.YES_NO_OPTION) == 0) {
									Database.addAppointment(appointment);
									JOptionPane.showMessageDialog(this, "Møtet er nå lagt til.");
									meet = new MeetingView(appointment, user);
									frame.setVisible(false);
								}
							} catch (SQLException e1) {
							} catch (InstantiationException e1) {
							} catch (IllegalAccessException e1) {
							} catch (ClassNotFoundException e1) {
							}
						} catch (Exception exception) {
							JOptionPane.showMessageDialog(this, "Sjekk tidpunktene, formatet skal være: DD.MM.YYYY og HH:MM");
						}
						
					} else {
						JOptionPane.showMessageDialog(this, "Velg et rom");
					}
					
				} else {
					JOptionPane.showMessageDialog(this, "Sett inn en møtetekst");
				}
				
			} else {
				JOptionPane.showMessageDialog(this, "Sett inn en tittel");
			}
			
		}
		else if(e.getActionCommand().equals("Delete")) {
			int ans = JOptionPane.showConfirmDialog(this, "Er du sikker på at du vil slette avtalen?", "Slette avtale", JOptionPane.YES_NO_OPTION);
			if(ans == 0) {
				//database -> slett avtale
				frame.setVisible(false);
				//new Gui(user)
			}
		}
		else if(e.getActionCommand().equals("DeleteParticipant")) {
			if(participantsList.getSelectedValue() != null) {
				searchUserField.setText("");
				userListModel.addElement(participantsList.getSelectedValue());
				participantListModel.removeElement(participantsList.getSelectedValue());
			}
		}
		else if(e.getActionCommand().equals("DeleteAllParticipants")) {
			searchUserField.setText("");
			participantListModel.removeAllElements();
			fillUsers();
		}
		else if(e.getActionCommand().equals("searchRoom")) {
			System.out.println("fyller rom");
			fillRooms();
		}
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if(arg0.getSource() == searchUserField) {
			fillUsers();
		}
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource() == userList) {
			
			User select = (User) userList.getSelectedValue();
			if(select != null) {
				participantListModel.addElement(select);
				userListModel.removeElement(select);
			}
			
		}
	}
	
	
}
