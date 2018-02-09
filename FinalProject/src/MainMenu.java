/** 
 * Class Main Menu
 * Final Project
 * May 9, 2017
 * Nicholas Pilkington
*/

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.awt.event.ActionEvent;
import java.awt.Font;

import sun.applet.Main;

import java.awt.Color;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.sqlite.SQLiteException;

import java.sql.*;

import javax.swing.*;
import java.awt.SystemColor;

public class MainMenu {

	public JFrame frmMainMenu;
	private JTextField textFieldLand;
	private JTextField textFieldWater;
	private JTextField textFieldFlyers;	
	private JTextField textFieldTotal;
	public boolean tableFound = false; // Is used to check if a table is found in the database file
	public static boolean fileLoaded =false;//Is used to check if the user loaded a file or created a new file
	private String fileNameLoad;
	private String fileNameNew;
	public static String dbConnectionNew; // Is used to load the database if a new file is created
	public static String dbConnectionLoad; // Is used to load the database if a file has been loaded
	private static JLabel lblTableNotFound = new JLabel("You need to Load or Create a Table first");
	Connection connection = null; // Sets the current database connection to null
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu window = new MainMenu();
					window.frmMainMenu.setVisible(true);						
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
// Ask user for the sqlite file they want to open and use.
	public void openFile() throws IOException
			{
				fileNameLoad= "";
				String sqlite = ".sqlite";
				
			// get the current working directory
				String currentDir = System.getProperty("user.dir");
				
			// Create a file chooser object   
				JFileChooser chooser = new JFileChooser();
			    
			// Set the extension type 													
			    FileNameExtensionFilter filter = new FileNameExtensionFilter( 
			        "SQlite Files", "sqlite");			    
			    chooser.setFileFilter(filter);
			    
			 // Set the chooser's directory to current working directory
			    chooser.setCurrentDirectory(new File(currentDir));
			    
			 // open the Open chooser to select file, get response     							
			    int returnVal = chooser.showOpenDialog(null);
			    
			 // If file returned, 
			    if(returnVal == JFileChooser.APPROVE_OPTION ) {					
			       fileNameLoad = chooser.getCurrentDirectory()+ "\\" + 
			                   chooser.getSelectedFile().getName();
			       
			       dbConnectionLoad = "jdbc:sqlite:" + fileNameLoad;
			       if (fileNameLoad.contains(sqlite)){
			    	   dbConnectorLoaded();
			    	   lblTableNotFound.setVisible(false);
			       JOptionPane.showMessageDialog(null, "Database Loaded");	
			       tableFound = true;
				   fileLoaded = true;
			// Loads the totals for the Dinosaurs
				   loadTotals();			   
			       }
			       else 
			    	   JOptionPane.showMessageDialog(null, "Not a Valid File Name");
		           }			   
			}
	
// Creates and saves a new Database and new table
	public void saveFile() throws IOException
	{	    					
				tableFound = false;
				Statement statement;
				
				// Sets up a String to add .txt to then end of the filename if user doesn't add it.
				String sqlite = ".sqlite"; 				
				
				// get the current working directory
				String currentDir = System.getProperty("user.dir");		
				
				// Create a file chooser object
			    JFileChooser chooser = new JFileChooser();			    
			    
			    // Set the extension type 																	
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(   
			        "SQlite Files", "sqlite");
			    
			    chooser.setFileFilter(filter);
			 // Set the chooser's directory to current working directory
			    chooser.setCurrentDirectory(new File(currentDir));
			      																
			    int returnVal = chooser.showSaveDialog(null);
			    
			    
			    // If file returned open the Save chooser to Create a File
			    if(returnVal == JFileChooser.APPROVE_OPTION) {					
			       fileNameNew= chooser.getCurrentDirectory()+ "\\" + 
			                   chooser.getSelectedFile().getName();
			       
			    // Checks if the user named file contains .sqlite in the name and
			    // adds it if it does not.
			     if (fileNameNew.contains(sqlite))
			     {
			    	 dbConnectionNew = "jdbc:sqlite:" + fileNameNew;	
			     }
			     else
			    	 dbConnectionNew = "jdbc:sqlite:" + fileNameNew + sqlite;				   
			    			    
			  // Creates the table for the database  
			   	try {			     
			    	 String query = "CREATE TABLE ARK4 (" 
					           + "Dinosaur TEXT,"  
					           + "Gender TEXT," 
					           + "Name TEXT NOT NULL UNIQUE,"  
					           + "Level INTEGER, "
					           + "Health DOUBLE,"
					           + "Stamina DOUBLE,"
					           + "Oxygen DOUBLE,"
					           + "Weight DOUBLE,"
					           + "Melee DOUBLE,"
					           + "PRIMARY KEY(Name))"; 	   
					      
			    	Class.forName("org.sqlite.JDBC");
				    connection = DriverManager.getConnection(dbConnectionNew);						    
				    statement = connection.createStatement();					    	
			    	statement.executeUpdate(query);
			    	lblTableNotFound.setVisible(false);
			    	JOptionPane.showMessageDialog(null, "Database was Created successfully");   
			    	fileLoaded = false;	 
			    	tableFound = true;
			    	// Loads the totals once the file has been successfully created
					  loadTotals();
					   }
			   	// Prevents users from overwriting an existing file
					   catch (SQLException e ) {								
					       JOptionPane.showMessageDialog(null, "You Cannot Overwrite An Exsiting File!");
					       tableFound = false;
					       fileLoaded = false;							       
					       dbConnectionNew = null;
					       loadTotals();
					       textFieldLand.setText(" ");
					       textFieldWater.setText(" ");
					       textFieldFlyers.setText(" ");
					       textFieldTotal.setText(" ");	
					       return;				       
					   }
					   catch (ClassNotFoundException e) {
					       System.out.println("An Mysql drivers were not found");
					   }
			    	}
			 }			
	
// Connects the Loaded sqlite database
	public static Connection dbConnectorLoaded(){		 
		       try {		    	  
			       Class.forName("org.sqlite.JDBC");
			       Connection connection = DriverManager.getConnection(dbConnectionLoad);			      			   
			       return connection;			       
			   }
			   catch (SQLException e ) {
				   System.out.println("Table Already Exist");			       
			   		}
			   catch (ClassNotFoundException e) {
			       System.out.println("An Mysql drivers were not found");
			   		}
		       	return null;			
			}
	
// Connects the New Created sqlite database		
	public static Connection dbConnectorNew(){				
			     try {
			    	   Class.forName("org.sqlite.JDBC");
			    	   Connection connection = DriverManager.getConnection(dbConnectionNew);			    	   
			    	   return connection;		       
				   }
			     	catch (SQLException e ) {
			    	 System.out.println("Table Already Exist");				       
			     		}
				   catch (ClassNotFoundException e) {
				       System.out.println("An Mysql drivers were not found");
				   		}
			     	return null;							
			}		     
			
// This method is to load the totals once a new database and table have been created,
// or loaded in.
	public void loadTotals(){
		if (tableFound == true){							
			try{	
			// Checks which database file to load	
				if (fileLoaded == true){	
					connection= dbConnectorLoaded();				
				}
				else{
					connection= dbConnectorNew();				
				}
			// Checks if there are any Land Dinosaurs and adds them up if there are
				try {
				String query="Select count(Dinosaur) from ARK4 where "
						+ "Dinosaur='Rex' "
						+ "Or Dinosaur='Trike'"
						+ "Or Dinosaur='Compy'"
						+ "Or Dinosaur='Dodo'";				
				PreparedStatement pst=connection.prepareStatement(query);
				ResultSet rs=pst.executeQuery();				
				if(rs.next()){
					String sum=rs.getString("count(Dinosaur)");
					textFieldLand.setText(sum);}
				}catch(SQLiteException e2){
					tableFound = false;
					fileLoaded = false;					 
					JOptionPane.showMessageDialog(null, "No Table Found");					 
					 return;
				}					
				connection.close();
			} catch (Exception e2) {
					e2.printStackTrace();
				}				
			try{
			// Checks which database file to load			
				if (fileLoaded == true){				
					connection = DriverManager.getConnection(dbConnectionLoad);
					}
					else{
					connection = DriverManager.getConnection(dbConnectionNew);
					}
			// Checks if there are any Water Creatures and adds them up if there are
				String query="Select count(Dinosaur) from ARK4 where "
						+ "Dinosaur='Megalodon'";				
				PreparedStatement pst=connection.prepareStatement(query);
				ResultSet rs=pst.executeQuery();
				
				if(rs.next()){
					String sum=rs.getString("count(Dinosaur)");
					textFieldWater.setText(sum);					
				}		
				connection.close();
			} catch (Exception e2) {
					e2.printStackTrace();
				}			
				
			try{	
			// Checks which database file to load
				if (fileLoaded == true){				
					connection = DriverManager.getConnection(dbConnectionLoad);
					}
					else{
					connection = DriverManager.getConnection(dbConnectionNew);
					}
			// Checks if there are any Flyers Dinosaurs and adds them up if there are
				String query="Select count(Dinosaur) from ARK4 where "
						+ "Dinosaur='Pteranodon'";				
				PreparedStatement pst=connection.prepareStatement(query);
				ResultSet rs=pst.executeQuery();
				
				if(rs.next()){
					String sum=rs.getString("count(Dinosaur)");
					textFieldFlyers.setText(sum);					
				}		
				connection.close();
			} catch (Exception e2) {
					e2.printStackTrace();
				}					
			try{
			// Checks which database file to load
				if (fileLoaded == true){				
					connection = DriverManager.getConnection(dbConnectionLoad);
					}
					else{
					connection = DriverManager.getConnection(dbConnectionNew);
					}
			// Checks the total number of Dinosaurs		
				String query="Select count(Dinosaur) from ARK4";
				PreparedStatement pst=connection.prepareStatement(query);
				ResultSet rs=pst.executeQuery();
				
				if(rs.next()){
					String sum=rs.getString("count(Dinosaur)");
					textFieldTotal.setText(sum);					
				}		
				connection.close();
			} catch (Exception e2) {
					e2.printStackTrace();
				}			
		 }		
	}
	
	/**
	 * Create the application.
	 */
	public MainMenu() {
		initialize();		
	}
		
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMainMenu = new JFrame();
		frmMainMenu.setResizable(false);
		frmMainMenu.setForeground(Color.WHITE);
		frmMainMenu.setTitle("Main Menu");
		frmMainMenu.getContentPane().setBackground(Color.WHITE);	   
		frmMainMenu.setBounds(100, 100, 614, 545);
		frmMainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMainMenu.getContentPane().setLayout(null);
		frmMainMenu.setLocationRelativeTo(null);
		
	// Button for if user clicks on Create New Database	
		JButton btnCreateNewData = new JButton("Create New Database");
		btnCreateNewData.setForeground(Color.BLACK);
		btnCreateNewData.setFont(new Font("Times New Roman", Font.BOLD, 25));
		btnCreateNewData.setBackground(new Color(105, 105, 105));
		btnCreateNewData.setBounds(10, 85, 280, 80);
		frmMainMenu.getContentPane().add(btnCreateNewData);
		btnCreateNewData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Creates the table	
				try {
					saveFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					
				}		
		});	
		
	// Label for if user clicks on on Open Table if no file has been created or opened	
		lblTableNotFound.setForeground(new Color(255, 255, 0));
		lblTableNotFound.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTableNotFound.setHorizontalAlignment(SwingConstants.CENTER);
		lblTableNotFound.setBounds(318, 70, 284, 14);
		frmMainMenu.getContentPane().add(lblTableNotFound);
		lblTableNotFound.setVisible(false);
		
	// Button for if user clicks on Open Table		
		JButton btnOpenTable = new JButton("Open Table");
		btnOpenTable.setForeground(Color.BLACK);
		btnOpenTable.setBackground(new Color(105, 105, 105));
		btnOpenTable.setFont(new Font("Times New Roman", Font.BOLD, 25));
		btnOpenTable.setBounds(318, 85, 280, 80);
		frmMainMenu.getContentPane().add(btnOpenTable);
		btnOpenTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				try {
					connection.close();
					
				// Checks if boolean fileLoaded and tableFound are true or false
				// and will do one of the actions based on the boolean values
					if (fileLoaded == true){						
						lblTableNotFound.setVisible(false);					
						frmMainMenu.dispose();						
						JTable arkTable = new JTable();
						arkTable.fileLoaded = true;
						arkTable.setVisible(true);
						arkTable.tableFound = true;						
					}
				// Checks if file is loaded if false and if a table was found
					else if (fileLoaded == false && tableFound == true)
					{								
						lblTableNotFound.setVisible(false);					
						frmMainMenu.dispose();
						JTable arkTable = new JTable();
						arkTable.fileLoaded = true;
						arkTable.setVisible(true);
						arkTable.tableFound = true;						
					}
				// Will display a message "You need to Load or Create a Table first"
					else if (fileLoaded == false && tableFound == false){
						lblTableNotFound.setVisible(true);	
						}					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
				catch(NullPointerException e2){
					lblTableNotFound.setVisible(true);				
				 }			
			}
		});
		
	// Button for if user clicks on Load Database			
		JButton btnLoadDatabase = new JButton("Load Database");
		btnLoadDatabase.setForeground(Color.BLACK);
		btnLoadDatabase.setFont(new Font("Times New Roman", Font.BOLD, 25));
		btnLoadDatabase.setBackground(new Color(105, 105, 105));
		btnLoadDatabase.setBounds(10, 176, 280, 80);
		frmMainMenu.getContentPane().add(btnLoadDatabase);	
		btnLoadDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					openFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
			}
		});
		
	// Button for if user clicks on Exit				
		JButton btnExit = new JButton("Exit");
		btnExit.setForeground(Color.BLACK);
		btnExit.setBackground(new Color(105, 105, 105));
		btnExit.setFont(new Font("Times New Roman", Font.BOLD, 25));
		btnExit.setBounds(318, 176, 280, 80);
		frmMainMenu.getContentPane().add(btnExit);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			// Exits the program
				System.exit(0);
			}
		});
		
	// Labels for number of Dinosaurs, Water Creatures, Flyers, and Total Number of 
	// all of them added together
		JLabel lblNumberOfLand = new JLabel("Number of Land Dinosaurs:");
		lblNumberOfLand.setBackground(new Color(105, 105, 105));
		lblNumberOfLand.setForeground(Color.WHITE);
		lblNumberOfLand.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblNumberOfLand.setBounds(10, 281, 376, 41);
		frmMainMenu.getContentPane().add(lblNumberOfLand);
		
		JLabel lblNumberOfWater = new JLabel("Number of Water Creatures:");
		lblNumberOfWater.setBackground(new Color(105, 105, 105));
		lblNumberOfWater.setForeground(Color.WHITE);
		lblNumberOfWater.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblNumberOfWater.setBounds(10, 340, 376, 41);
		frmMainMenu.getContentPane().add(lblNumberOfWater);
		
		JLabel lblNumberOfFlyers = new JLabel("Number of Flyers:");
		lblNumberOfFlyers.setBackground(new Color(105, 105, 105));
		lblNumberOfFlyers.setForeground(Color.WHITE);
		lblNumberOfFlyers.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblNumberOfFlyers.setBounds(10, 392, 376, 41);
		frmMainMenu.getContentPane().add(lblNumberOfFlyers);
		
		JLabel lblTotalNumberOf = new JLabel("Total Number of Dinosaurs:");
		lblTotalNumberOf.setBackground(new Color(105, 105, 105));
		lblTotalNumberOf.setForeground(Color.WHITE);
		lblTotalNumberOf.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblTotalNumberOf.setBounds(10, 448, 376, 41);
		frmMainMenu.getContentPane().add(lblTotalNumberOf);
		
	// TextField for number of Dinosaurs, Water Creatures, Flyers, and Total Number of 
	// all of them added together		
		textFieldLand = new JTextField();
		textFieldLand.setForeground(Color.WHITE);
		textFieldLand.setBackground(new Color(105, 105, 105));
		textFieldLand.setFont(new Font("Tahoma", Font.BOLD, 29));
		textFieldLand.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldLand.setEditable(false);		
		textFieldLand.setBounds(382, 281, 209, 41);
		frmMainMenu.getContentPane().add(textFieldLand);
		textFieldLand.setColumns(10);		
				
		textFieldWater = new JTextField();
		textFieldWater.setForeground(Color.WHITE);
		textFieldWater.setBackground(new Color(105, 105, 105));
		textFieldWater.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldWater.setFont(new Font("Tahoma", Font.BOLD, 29));
		textFieldWater.setEditable(false);
		textFieldWater.setColumns(10);
		textFieldWater.setBounds(382, 337, 209, 41);
		frmMainMenu.getContentPane().add(textFieldWater);
		
		textFieldFlyers = new JTextField();
		textFieldFlyers.setForeground(Color.WHITE);
		textFieldFlyers.setBackground(new Color(105, 105, 105));
		textFieldFlyers.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldFlyers.setFont(new Font("Tahoma", Font.BOLD, 29));
		textFieldFlyers.setEditable(false);
		textFieldFlyers.setColumns(10);
		textFieldFlyers.setBounds(382, 392, 209, 41);
		frmMainMenu.getContentPane().add(textFieldFlyers);					
				
		textFieldTotal = new JTextField();
		textFieldTotal.setForeground(Color.WHITE);
		textFieldTotal.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldTotal.setFont(new Font("Tahoma", Font.BOLD, 29));
		textFieldTotal.setEditable(false);
		textFieldTotal.setColumns(10);
		textFieldTotal.setBackground(new Color(105, 105, 105));
		textFieldTotal.setBounds(382, 448, 209, 41);
		frmMainMenu.getContentPane().add(textFieldTotal);					
				
	// Label for Creating a barline 			
		JLabel labelbar = new JLabel("______________________________________________");
		labelbar.setEnabled(false);
		labelbar.setForeground(Color.WHITE);
		labelbar.setBackground(new Color(220, 220, 220));
		labelbar.setHorizontalAlignment(SwingConstants.CENTER);
		labelbar.setFont(new Font("Times New Roman", Font.BOLD, 25));
		labelbar.setBounds(0, 26, 612, 35);
		frmMainMenu.getContentPane().add(labelbar);		
		
	// Label for Creating a Title	
		JLabel lblArkDatabaseCreator = new JLabel("Ark Database Creator");
		lblArkDatabaseCreator.setBackground(new Color(105, 105, 105));
		lblArkDatabaseCreator.setForeground(Color.WHITE);
		lblArkDatabaseCreator.setFont(new Font("Times New Roman", Font.BOLD, 50));
		lblArkDatabaseCreator.setHorizontalAlignment(SwingConstants.CENTER);
		lblArkDatabaseCreator.setBounds(10, 11, 592, 48);
		frmMainMenu.getContentPane().add(lblArkDatabaseCreator);
		
	// Label for setting a background	
		JLabel lblBackgroundImg = new JLabel("");
		lblBackgroundImg.setForeground(SystemColor.text);
		lblBackgroundImg.setBackground(Color.GRAY);
		lblBackgroundImg.setBounds(0, 0, 612, 520);
		frmMainMenu.getContentPane().add(lblBackgroundImg);
		URL url = Main.class.getResource("/BackGroundMainMenu.png");
		lblBackgroundImg.setIcon(new ImageIcon(url));
	}
	
	protected static void setVisible(boolean b) {
		// TODO Auto-generated method stub
	}
}
