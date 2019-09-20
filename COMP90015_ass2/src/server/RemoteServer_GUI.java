package server;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class RemoteServer_GUI {
	// Define GUI elements
	private JFrame frame;
	private JLabel titleOfFrame;
	public static JTextArea dicArea;
	public static JTextArea statusArea;
	private JPanel panel;
	private JScrollPane scrollPaneForStatus;
	private JButton initiateServerButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RemoteServer_GUI window = new RemoteServer_GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RemoteServer_GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Create the server object
		RemoteServer remoteServer = new RemoteServer();
		
		// Initialize the frame
		frame = new JFrame();
		frame.setBounds(100, 100, 836, 624);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// Initialize the panel
		panel = new JPanel();
		panel.setBounds(0, 0, 813, 571);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		// Initialize the title of the frame
		titleOfFrame = new JLabel("Server GUI DEMO");
		titleOfFrame.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		titleOfFrame.setBounds(15, 15, 219, 34);
		panel.add(titleOfFrame);

		// Initialize the scroll bar for status area
		scrollPaneForStatus = new JScrollPane();
		scrollPaneForStatus.setBounds(10, 121, 747, 175);
		panel.add(scrollPaneForStatus);

		// Initialize the area to display connection status
		statusArea = new JTextArea();
		statusArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		statusArea.setEditable(false);
		scrollPaneForStatus.setViewportView(statusArea);

		// Initiate RMI 
		initiateServerButton = new JButton("Initiate RMI");
		initiateServerButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		initiateServerButton.setBounds(15, 65, 179, 29);
		panel.add(initiateServerButton);

		// Add listener for initiate button
		initiateServerButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				try {
					// Initiate the server, including establishing server socket and RMI
					boolean isInitiated = remoteServer.initiateRMI() ? true: false;
					
					// Check whether there is no error of building RMI and server socket
					if(isInitiated) {
						statusArea.append("RMI and server socket are initiated.\n");;
					}
					else {
						JOptionPane.showMessageDialog(null, "RMI initiation failed. Please check your RMI status and socket settings.", "NOTE", JOptionPane.INFORMATION_MESSAGE);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "RMI initiation failed.", "NOTE", JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});

	}

}
