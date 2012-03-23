package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Server.Database;

public class Login extends JPanel implements ActionListener, KeyListener {

	private JFrame frame;

	private JLabel usernameLabel, passwordLabel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private GridBagConstraints c;
	private JButton confirm;

	public Login() {

		frame = new JFrame();
		frame.setTitle("Logg inn");

		usernameField = new JTextField(20);
		passwordField = new JPasswordField(20);

		confirm = new JButton("Logg inn");
		confirm.addActionListener(this);
		confirm.setActionCommand("login");

		usernameLabel = new JLabel("Brukernavn");
		passwordLabel = new JLabel("Passord");

		usernameField.addKeyListener(this);
		passwordField.addKeyListener(this);

		setPreferredSize(new Dimension(400, 250));
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		add(usernameLabel, c);

		c.gridy = 0;
		c.gridx = 1;
		add(usernameField, c);

		c.gridy = 1;
		c.gridx = 0;
		add(passwordLabel, c);

		c.gridy = 1;
		c.gridx = 1;
		add(passwordField, c);

		c.gridy = 2;
		c.gridx = 1;
		add(confirm, c);


		setVisible(true);

		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	public static void main(String[] args) {
		new Login();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("login")) {
			login();
		}
	}
	
	public void login() {
		String username = usernameField.getText();
		String password = passwordField.getText();
		
		if (true /* login failed */) {
			JOptionPane.showMessageDialog(null, "Brukernavnet eller passordet er feil.", "Ugyldig brukernavn/passord", JOptionPane.ERROR_MESSAGE);
		}



//		if(Database.login(username, password)) {
//			new Kalenderprogram(username);
//		}
//		else {
//			JOptionPane.showMessageDialog(this, "Feil brukernavn / passord");
//		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyChar() == KeyEvent.VK_ENTER) {
			login();
		}
	} 

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}


}
