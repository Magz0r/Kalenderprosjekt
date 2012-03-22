package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CreateAppointmentGUI extends JPanel implements ActionListener {

	private JFrame frame;
	private JLabel newAppointmentLabel, fromLabel, toLabel, titleLabel, descriptionLabel, addUserLabel, roomLabel, participantsLabel;
	private JTextField fromField, toField, titleField, searchUserField, searchRoomField;
	private JTextArea descriptionArea;
	private JList userList, roomList, participantsList;
	private GridBagConstraints c;
	private JButton removeUserButton, removeAllUsersButton, saveButton, cancelButton, deleteButton;
	private DefaultListModel userListModel, roomListModel, participantListModel;
	private JScrollPane userScroll, roomScroll, participantsScroll;
	
	public CreateAppointmentGUI(String username) {
		
		frame = new JFrame();
		
		
		
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
		descriptionLabel = new JLabel("M¿tetekst:");
		addUserLabel = new JLabel("Legg til deltager");
		roomLabel = new JLabel("Rom");
		participantsLabel = new JLabel("Deltagere");
		
		//textfield
		fromField = new JTextField(5);
		toField = new JTextField(5);
		titleField = new JTextField(10);
		searchUserField = new JTextField(10);
		searchRoomField = new JTextField(10);
		
		//textarea
		descriptionArea = new JTextArea(13, 15);
		descriptionArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		//lists
		userList = new JList(userListModel);
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
		fillRooms();
		
		participantsScroll = new JScrollPane(participantsList);
		participantsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		participantsScroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		participantsScroll.setPreferredSize(new Dimension(130, 250));
		
		//buttons
		removeUserButton = new JButton("Fjern");
		removeAllUsersButton = new JButton("Fjern alle");
		saveButton = new JButton("Lagre");
		cancelButton = new JButton("Avbryt");
		deleteButton = new JButton("Slett");
		
		//actioncommands
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("Cancel");
		saveButton.addActionListener(this);
		saveButton.setActionCommand("Save");
		deleteButton.addActionListener(this);
		deleteButton.setActionCommand("Delete");
		
		setPreferredSize(new Dimension(1100, 600));
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
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
		add(fromField, c);
		
		c.gridy = 2;
		add(toField, c);
		
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
		add(searchRoomField, c);
		
		c.gridy = 4;
		add(roomScroll, c);
		
		
		c.gridx = 4;
		c.gridy = 3;
		add(participantsLabel, c);
		
		c.gridy = 4;
		add(participantsScroll, c);
		
		c.gridy = 5;
		add(removeUserButton, c);
		c.gridx++;
		add(removeAllUsersButton, c);
		
		c.gridx = 0;
		c.gridy = 6;
		add(saveButton, c);
		c.gridx++;
		add(cancelButton, c);
		c.gridx++;
		add(deleteButton,c);
		
		
		
		setVisible(true);

		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	
	private void fillRooms() {
		// TODO Auto-generated method stub
	}


	private void fillUsers() {
		// TODO Auto-generated method stub
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Cancel")) {
			int ans = JOptionPane.showConfirmDialog(this, "Sikker pŒ at du vil avbryte?", "Avbryt", JOptionPane.YES_NO_OPTION);
			if(ans == 0) {
				frame.setVisible(false);
				new Login();
			}
			
		}
		else if(e.getActionCommand().equals("Save")) {
			//frame.setVisible(false);
			//new Gui(user)
		}
		else if(e.getActionCommand().equals("Delete")) {
			int ans = JOptionPane.showConfirmDialog(this, "Er du sikker pŒ at du vil slette avtalen?", "Slette avtale", JOptionPane.YES_NO_OPTION);
			if(ans == 0) {
				//database -> slett avtale
				frame.setVisible(false);
				//new Gui(user)
			}
		}
	}
	
	
}
