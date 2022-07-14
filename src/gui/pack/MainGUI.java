package gui.pack;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import dbfs.data.SystemParametersData;
import dbfs.pack.ActionProcessor;
import dbfs.pack.OperationProcessor;
import dbfs.pack.SystemParameters;
import javax.swing.JComboBox;
import java.awt.Color;


/**
 * The main class of the software.
 * Initializes the GUI and starts the software.
 * @author maciej.wojciga
 */

public class MainGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	public static Logger logger = Logger.getLogger(MainGUI.class);

	public static MainGUI mainGUIfrm;
	public JPanel mainPane;

	// Visible fields.
	public JLabel lblConnectedtolbl;
	public JTextArea routeTextArea;
	public JTextField timeToChangeTextField;
	public JComboBox cbVelocity;
	public JComboBox cbDirection;

	// Other
	Properties confProperties;

	OperationProcessor operationProcessor = null;
	ActionProcessor actionProcessor = null;
	SystemParametersData systemParametersData = new SystemParametersData();

	private void createObjects() {
		operationProcessor = new OperationProcessor(this);
		actionProcessor = new ActionProcessor(this);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainGUIfrm = new MainGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public MainGUI() {
		configureProperties();
		initialize();
		setVisible(true);
		createObjects();
		operationProcessor.searchForPorts();
		checkIfFirstLaunch();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws ParseException 
	 * 
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	private void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		Image icon = Toolkit.getDefaultToolkit().getImage("./img/imim_logo.gif");
		setIconImage(icon);
		setTitle(confProperties.getProperty("name") + " v." + confProperties.getProperty("version"));
		setBounds(01000, 01000, 592, 288);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		/* MENU */
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmConnect = new JMenuItem("Connect...");
		mnFile.add(mntmConnect);

		JMenuItem mntmDisconnect = new JMenuItem("Disconnect");
		mnFile.add(mntmDisconnect);

		JMenuItem mntmAbout = new JMenuItem("About");
		mnFile.add(mntmAbout);

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);

		/* MAIN FRAME */
		mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPane);
		mainPane.setLayout(null);
		mainPane.setEnabled(false);

		JButton btnStart = new JButton("Start");
		btnStart.setBounds(10, 66, 89, 23);
		mainPane.add(btnStart);

		JButton btnStop = new JButton("Stop");
		btnStop.setBounds(109, 66, 92, 23);
		mainPane.add(btnStop);

		routeTextArea = new JTextArea();
		routeTextArea.setEditable(false);
		routeTextArea.setBounds(211, 11, 362, 216);
		mainPane.add(routeTextArea);
		routeTextArea.setEnabled(false);

		JLabel lblConnectedTo = new JLabel("Connected to:");
		lblConnectedTo.setBounds(10, 11, 69, 14);
		mainPane.add(lblConnectedTo);

		lblConnectedtolbl = new JLabel("");
		lblConnectedtolbl.setEnabled(false);
		lblConnectedtolbl.setBounds(89, 11, 112, 14);
		mainPane.add(lblConnectedtolbl);
		
		JLabel lblTimeToChange = new JLabel("Stop after [min]:");
		lblTimeToChange.setBounds(10, 36, 89, 14);
		mainPane.add(lblTimeToChange);

		timeToChangeTextField = new JTextField();
		timeToChangeTextField.setBounds(109, 36, 89, 20);
		mainPane.add(timeToChangeTextField);
		timeToChangeTextField.setColumns(10);
		
		String[] velocity = { "R1", "R2" };
		cbVelocity = new JComboBox(velocity);
		cbVelocity.setBounds(109, 100, 92, 20);
		mainPane.add(cbVelocity);
		
		JLabel lblVelocity = new JLabel("Velocity:");
		lblVelocity.setBounds(10, 103, 89, 14);
		mainPane.add(lblVelocity);
		
		JLabel lblDirection = new JLabel("Direction:");
		lblDirection.setBounds(10, 134, 89, 14);
		mainPane.add(lblDirection);
		
		String[] direction = { "Right", "Left" }; 
		cbDirection = new JComboBox(direction);
		cbDirection.setBounds(109, 131, 92, 20);
		mainPane.add(cbDirection);
		
		JButton btnPlusVelocity = new JButton("+V");
		btnPlusVelocity.setBounds(154, 196, 47, 23);
		mainPane.add(btnPlusVelocity);
		
		JButton btnMinusVelocity = new JButton("-V");
		btnMinusVelocity.setBounds(154, 162, 47, 23);
		mainPane.add(btnMinusVelocity);
		
		JButton btnStatus = new JButton("STATUS");
		btnStatus.setBounds(10, 162, 89, 23);
		mainPane.add(btnStatus);
		
		JButton btnEmergencyStop = new JButton("STOP (!)");
		btnEmergencyStop.setBackground(Color.RED);
		btnEmergencyStop.setBounds(10, 196, 89, 23);
		mainPane.add(btnEmergencyStop);

		/* Actions */
		mntmConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				mntmConnectActionPerformed(event);
			}
		});

		mntmDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				mntmDisconnectActionPerformed(event);
			}
		});
		
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btnStartActionPerformed(event);
			}
		});
		
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btnStopActionPerformed(event);
			}
		});
		
		btnStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btnStatusActionPerformed(event);
			}
		});	
	
		btnEmergencyStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btnEmergencyStopActionPerformed(event);
			}
		});
		
		btnMinusVelocity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btnMinusVelocityActionPerformed(event);
			}
		});
		
		btnPlusVelocity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btnPlusVelocityActionPerformed(event);
			}
		});
		
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(mainGUIfrm, confProperties.getProperty("name") + " v" + confProperties.getProperty("version") + "\nWritten by: " + confProperties.getProperty("author") + "\n\nCooperator: dr. Roman Major\nInstitute of Metallurgy and Materials Science\nPolish Academy of Sciences", "About", JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}

	/* Action methods */
	private void mntmDisconnectActionPerformed(ActionEvent event) {
		logger.trace("[C]: mntmDisconnect");
		operationProcessor.disconnect();
	}
	
	private void btnStartActionPerformed(ActionEvent event) {
		int direction = 0;
		if(cbDirection.getSelectedItem().toString().equals("Right")) {
			direction = 0;
		} else {
			direction = 1;
		}
		int velocity = Integer.parseInt(cbVelocity.getSelectedItem().toString().substring(1));
		operationProcessor.writeData(direction, velocity, Integer.parseInt(timeToChangeTextField.getText()), 0);
	}
	
	private void btnPlusVelocityActionPerformed(ActionEvent event) {
		int velocity = 3;
		operationProcessor.writeData(0, velocity, 0, 0);
	}	

	private void btnMinusVelocityActionPerformed(ActionEvent event) {
		int velocity = 4;
		operationProcessor.writeData(0, velocity, 0, 0);
	}
	
	private void btnStopActionPerformed(ActionEvent event) {
		operationProcessor.stopRobotMovement();
	}
	
	private void btnEmergencyStopActionPerformed(ActionEvent event) {
		operationProcessor.stopEmergencyRobotMovement();
	}
	
	private void btnStatusActionPerformed(ActionEvent event) {
		operationProcessor.checkStatus();
	}

	private void mntmConnectActionPerformed(ActionEvent event) {
		logger.trace("[C]: mntmConnect");
		ArrayList<String> availableCommPorts = operationProcessor.searchForPorts();
		String[] possibilities = availableCommPorts.toArray(new String[availableCommPorts.size()]);
		String selectedPort = (String)JOptionPane.showInputDialog(mainGUIfrm, "Connect to:", "Connect", JOptionPane.PLAIN_MESSAGE, null, possibilities, null);
		if (selectedPort != null) {
			operationProcessor.connect(selectedPort);
			if (operationProcessor.isConnectedToPort() == true)
			{
				if (operationProcessor.initIOStream() == true)
				{
					operationProcessor.initListener();
				}
			}
		}
	}

	/* Other methods */
	public void configureProperties() {
		confProperties = new Properties();
		try {
			FileInputStream propertiesFileIS =  new FileInputStream(SystemParameters.PROPERTYFILE);
			confProperties.load(propertiesFileIS);
			propertiesFileIS.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void checkIfFirstLaunch() {
		try {
			if (confProperties.getProperty("first.time").equals("1")) {
				File changeLogFile = new File(SystemParameters.CHANGELOGFILE);
				String changelog = actionProcessor.takeFileAndWriteToString(changeLogFile, "[" + confProperties.getProperty("version") + "]");
				JOptionPane.showMessageDialog(mainGUIfrm, changelog + "\n\nNote that you can view this information later in the changelog.txt file in \"conf\" directory.", "What is new in version " + confProperties.getProperty("version") + "?", JOptionPane.INFORMATION_MESSAGE);
			}
			confProperties.setProperty("first.time", "0");
			confProperties.store(new FileOutputStream(new File(SystemParameters.PROPERTYFILE)), "Changed.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
