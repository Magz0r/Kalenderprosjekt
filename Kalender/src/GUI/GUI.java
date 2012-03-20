package GUI;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import net.miginfocom.swing.MigLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.FlowLayout;
import javax.swing.border.TitledBorder;
import java.awt.CardLayout;

public class GUI {
	
	public static JFrame frame = new JFrame();
	
	public static void main(String[] args) {
		frame.setSize(1024, 768);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JTabbedPane notifications = new JTabbedPane(JTabbedPane.TOP);
		notifications.setBounds(6, 314, 216, 416);
		panel.add(notifications);
		
		JPanel panel_1 = new JPanel();
		notifications.addTab("Mine", null, panel_1, null);
		
		JPanel panel_2 = new JPanel();
		notifications.addTab("Nye", null, panel_2, null);
		
		JButton btnOpprett = new JButton("Opprett");
		btnOpprett.setBounds(6, 209, 216, 71);
		panel.add(btnOpprett);
		
		JButton button = new JButton("New button");
		button.setBounds(886, 6, 117, 29);
		panel.add(button);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Fuck off", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setBounds(228, 187, 781, 542);
		panel.add(panel_4);
		panel_4.setLayout(null);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(6, 22, 769, 514);
		panel_4.add(panel_3);
		panel_3.setLayout(null);
		
		JLabel lblKalender = new JLabel("Kalender");
		lblKalender.setBounds(97, 70, 153, 16);
		panel.add(lblKalender);
		
	}
}
