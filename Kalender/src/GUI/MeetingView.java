package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Logic.Appointment;
import Logic.Date;
import Logic.Room;
import Logic.User;

public class MeetingView extends JPanel {

	private JFrame frame;
	private JLabel titleLabel, dateLabel, timeLabel, roomLabel, descriptionLabel, attendingLabel, notAttendingLabel, waitingLabel, contentTimeLabel, contentRoomLabel, contentDateLabel;
	private JTextArea descriptionArea;
	private JButton editButton, attendingButton, notAttendingButton;
	private GridBagConstraints c;
	private JList attendingList, notAttendingList, waitingList;
	private DefaultListModel attendingListModel, notAttendingListModel, waitingListModel;
	private Appointment model;
	
	public MeetingView(Appointment appointment) {
		model = appointment;
		frame = new JFrame();
		
		//labels
		titleLabel = new JLabel(appointment.getTitle());
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		dateLabel = new JLabel("Dato:");
		contentDateLabel = new JLabel(appointment.getStart().getDate());
		timeLabel = new JLabel("Tid:");
		contentTimeLabel = new JLabel("XX:XX - YY:YY");
		roomLabel = new JLabel("Sted:");
		contentRoomLabel = new JLabel(appointment.getRoom().getName());
		descriptionLabel = new JLabel("Beskrivelse");
		attendingLabel = new JLabel("Deltar");
		notAttendingLabel = new JLabel("Deltar ikke");
		waitingLabel = new JLabel("Venter på svar");
		
		
		//area
		descriptionArea = new JTextArea();
		descriptionArea.setPreferredSize(new Dimension(300, 250));
		descriptionArea.setText(appointment.getDescription());
		descriptionArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		descriptionArea.setLineWrap(true);
		descriptionArea.setWrapStyleWord(true);
		descriptionArea.setEditable(false);
		
		//buttons
		editButton = new JButton("Rediger");
		attendingButton = new JButton("Deltar");
		notAttendingButton = new JButton("Deltar ikke");
		
		//listmodel
		attendingListModel = new DefaultListModel();
		fillAttending();
		notAttendingListModel = new DefaultListModel();
		fillNotAttending();
		waitingListModel = new DefaultListModel();
		fillWaiting();
		
		//lists
		attendingList = new JList(attendingListModel);
		notAttendingList = new JList(notAttendingListModel);
		waitingList = new JList(waitingListModel);
		attendingList.setPreferredSize(new Dimension(100, 100));
		notAttendingList.setPreferredSize(new Dimension(100, 100));
		waitingList.setPreferredSize(new Dimension(100, 100));
		
		setPreferredSize(new Dimension(1100, 600));
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		
		c.insets = new Insets(3,3,3,3);
		c.ipadx = 30;
		c.gridx = 1;
		c.gridy = 0;
		add(titleLabel, c);
		
		c.gridx = 0;
		c.gridy = 1;
		add(dateLabel, c);
		
		c.gridx++;
		add(contentDateLabel, c);
		
		c.gridx--;
		c.gridy++;
		add(timeLabel, c);
		
		c.gridx++;
		add(contentTimeLabel, c);
		
		c.gridx--;
		c.gridy++;
		add(roomLabel, c);
		
		c.gridx++;
		add(contentRoomLabel, c);
		
		c.gridx--;
		c.gridy++;
		add(descriptionLabel, c);
		
		c.gridx++;
		add(new JScrollPane(descriptionArea), c);

		
		c.gridx++;
		c.gridy = 1;
		add(attendingLabel, c);
		
		c.gridy++;
		add(attendingList, c);
		
		c.gridy++;
		add(notAttendingLabel, c);
		
		c.gridy++;
		add(notAttendingList, c);
		
		c.gridy++;
		add(waitingLabel, c);

		c.gridy++;
		add(waitingList, c);
		
		
		setVisible(true);

		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void fillWaiting() {
		// TODO Auto-generated method stub
		waitingListModel.addElement("tregmes1");
		waitingListModel.addElement("tregmes2");
		waitingListModel.addElement("tregmes3");
	}

	private void fillNotAttending() {
		// TODO Auto-generated method stub
		notAttendingListModel.addElement("kjemes1");
		notAttendingListModel.addElement("kjemes2");
		notAttendingListModel.addElement("kjemes3");
	}

	private void fillAttending() {
		// TODO Auto-generated method stub
		attendingListModel.addElement("test1");
		attendingListModel.addElement("test2");
		attendingListModel.addElement("test3");
	}

	public static void main(String[] args) {

		Date start = new Date(2012, 4, 4, 12, 00);
		Date end = new Date(2012, 4, 4, 13, 00);
		User owner = new User("tandberg", "tandeey@gmail.com", "tandberg");
		
		String lorem = "Lorem Ipsum er rett og slett dummytekst fra og for trykkeindustrien. Lorem Ipsum har vært bransjens standard for dummytekst helt siden 1500-tallet, da en ukjent boktrykker stokket en mengde bokstaver for å lage et prøveeksemplar av en bok. Lorem Ipsum har tålt tidens tann usedvanlig godt, og har i tillegg til å bestå gjennom fem århundrer også tålt spranget over til elektronisk typografi uten vesentlige endringer. Lorem Ipsum ble gjort allment kjent i 1960-årene ved lanseringen av Letraset-ark med avsnitt fra Lorem Ipsum, og senere med sideombrekkingsprogrammet Aldus PageMaker som tok i bruk nettopp Lorem Ipsum for dummytekst.";
		
		Appointment appointment = new Appointment(new Room("R60", 3), start, end, owner, "Avtale nr 12", lorem, false);
		new MeetingView(appointment);
	}
}
