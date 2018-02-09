/** 
 * Class JTable
 * Final Project
 * May 9, 2017
 * Nicholas Pilkington
*/

import java.awt.EventQueue;
import java.net.URL;

import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

import net.proteanit.sql.DbUtils;
import sun.applet.Main;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import javax.swing.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.LineBorder;

public class JTable extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private javax.swing.JTable table;
	public boolean tableFound = true;
	public boolean fileLoaded;
	public String dbConnectionLoad;
	public String dbConnectionNew;
	@SuppressWarnings("rawtypes")
	private JComboBox comboBoxdDinosaur;
	@SuppressWarnings("rawtypes")
	private JComboBox comboBoxdGender;
	@SuppressWarnings("rawtypes")
	private JComboBox comboBoxSelect;
	private JLabel lblRecordDeleted;
	private JLabel lblBlankName;
	private JLabel lblEnterALevel;
	private JLabel lblEnterTheHealth;
	private JLabel lblEnterTheStamina;						
	private JLabel lblEnterTheOxygen;
	private JLabel lblEnterTheWeight;						
	private JLabel lblEnterTheMelee;
	private JLabel lblDataAdded;
	private JLabel lblError;
	private JLabel lblNameAlreadyExist;	
	private JLabel lblMustEnterExistingName;
	private JLabel lblRecordUpdated;
	private JTextField textFieldName;
	private JTextField textFieldLevel;
	private JTextField textFieldHealth;
	private JTextField textFieldStamina;
	private JTextField textFieldOxygen;
	private JTextField textFieldWeight;
	private JTextField textFieldMelee;		
	private int blank = 0; // Once equal to 7, it will add a record
	private int level = 0;
	private double health = 0.0;
	private double stamina = 0.0;
	private double oxygen = 0.0;
	private double weight = 0.0;
	private double melee = 0.0;	
	private JTextField textFieldSearch;
	Connection connection; // Declares the connection variable
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JTable frame = new JTable();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}	
	
// Clears the labels by setting there visibility to false	
	public void clearLabels(){		
		lblBlankName.setVisible(false);						
		lblEnterALevel.setVisible(false);
		lblEnterTheHealth.setVisible(false);
		lblEnterTheStamina.setVisible(false);						
		lblEnterTheOxygen.setVisible(false);
		lblEnterTheWeight.setVisible(false);						
		lblEnterTheMelee.setVisible(false);
		lblDataAdded.setVisible(false);
		lblError.setVisible(false);
		lblNameAlreadyExist.setVisible(false);
		lblRecordDeleted.setVisible(false);
		lblMustEnterExistingName.setVisible(false);
		lblRecordUpdated.setVisible(false);
	}
	
// Clears the labels by setting there visibility to false
// and clears the textFields setting them the blank and combo boxes to there default
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void clearFieldsAndLabels(){
		lblBlankName.setVisible(false);						
		lblEnterALevel.setVisible(false);
		lblEnterTheHealth.setVisible(false);
		lblEnterTheStamina.setVisible(false);						
		lblEnterTheOxygen.setVisible(false);
		lblEnterTheWeight.setVisible(false);						
		lblEnterTheMelee.setVisible(false);
		lblDataAdded.setVisible(false);
		lblError.setVisible(false);
		lblNameAlreadyExist.setVisible(false);
		lblRecordDeleted.setVisible(false);	
		lblMustEnterExistingName.setVisible(false);
		lblRecordUpdated.setVisible(false);
		textFieldName.setText("");
		textFieldLevel.setText("");
		textFieldHealth.setText("");
		textFieldStamina.setText("");
		textFieldOxygen.setText("");
		textFieldWeight.setText("");
		textFieldMelee.setText("");
		comboBoxdDinosaur.setModel(new DefaultComboBoxModel(new String[] {"Rex", "Trike", "Compy", 
				"Pteranodon", "Megalodon", "Dodo"}));
		comboBoxdGender.setModel(new DefaultComboBoxModel(new String[] {"Male", "Female"}));
	}
	
// Reloads the table after a change has been made	
	public void refreshTable(){
		try{						
			if (MainMenu.fileLoaded == true){					
				connection = MainMenu.dbConnectorLoaded();				
			}					
			else if (MainMenu.fileLoaded == false){					
				connection = MainMenu.dbConnectorNew();					
			}
			String query="SELECT * FROM ARK4";
			PreparedStatement pst=connection.prepareStatement(query);
			ResultSet rs=pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			pst.close();
			rs.close();
			connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
	}

// Checks TextFields and increases or decreases the blank value
// based on what fields have been left empty, invalid type and what textFields have
// the correct values.
	public void checkFields(){
		level = 0;
		health = 0.0;
		stamina = 0.0;
		oxygen = 0.0;
		weight = 0.0;
		melee = 0.0;	
		lblDataAdded.setVisible(false);
		clearLabels();
		blank = 0;	
		
		// Checks if Field Name is blank						
			if (textFieldName.getText().equals(""))
			{						
				lblBlankName.setVisible(true);	
				subtractBlank();
			}									
			else 
			{
				lblBlankName.setVisible(false);	
				addBlank();
			}								
					
	// Checks if Field Level is blank or 0
			try{
				level = Integer.parseInt(textFieldLevel.getText());
				if (level == 0)
					{						
						lblEnterALevel.setVisible(true);
						subtractBlank();						
					}
				else if (textFieldLevel.getText().contains("-"))
					{						
						lblEnterALevel.setVisible(true);
						subtractBlank();					
					}
					else 
					{
						lblEnterALevel.setVisible(false);						
						addBlank();
					}
			}catch (NumberFormatException e){
				lblEnterALevel.setVisible(true);
				subtractBlank();
			}
			
	// Checks if Field Health is blank or 0.0
			try{
				health = Double.parseDouble(textFieldHealth.getText());
				if (health == 0.0)
					{						
						lblEnterTheHealth.setVisible(true);
						subtractBlank();					
					}
				else if (textFieldHealth.getText().contains("-"))
					{						
						lblEnterTheHealth.setVisible(true);
						subtractBlank();						
					}
					else
					{
						lblEnterTheHealth.setVisible(false);
						addBlank();
					}
			}catch (NumberFormatException e){
				lblEnterTheHealth.setVisible(true);
				subtractBlank();
			}
	// Checks if Field Stamina is blank	or 0.0	
			try{
				stamina = Double.parseDouble(textFieldStamina.getText());
				if (stamina == 0.0)
					{						
						lblEnterTheStamina.setVisible(true);
						subtractBlank();						
					}
				else if (textFieldStamina.getText().contains("-"))
				{						
					lblEnterTheStamina.setVisible(true);
					subtractBlank();					
				}
				else
				{
					lblEnterTheStamina.setVisible(false);
					addBlank();
				}
			}catch (NumberFormatException e){
				lblEnterTheStamina.setVisible(true);
				subtractBlank();
			}
	// Checks if Field Oxygen is blank or 0.0	
			try{
				oxygen = Double.parseDouble(textFieldOxygen.getText());
				if (oxygen == 0.0)
					{						
						lblEnterTheOxygen.setVisible(true);
						subtractBlank();					
					}
				else if (textFieldOxygen.getText().contains("-"))
				{						
					lblEnterTheOxygen.setVisible(true);
					subtractBlank();					
				}
					else
					{
						lblEnterTheOxygen.setVisible(false);
						addBlank();
					}
			}catch (NumberFormatException e){
				lblEnterTheOxygen.setVisible(true);
				subtractBlank();
			}
	// Checks if Field Weight is blank or 0.0		
			try{
				weight = Double.parseDouble(textFieldWeight.getText());
				if (weight == 0.0)
					{						
						lblEnterTheWeight.setVisible(true);
						subtractBlank();					
					}
				else if (textFieldWeight.getText().contains("-"))
				{						
					lblEnterTheWeight.setVisible(true);
					subtractBlank();					
				}
					else
					{
						lblEnterTheWeight.setVisible(false);
						addBlank();
					}	
			}catch (NumberFormatException e){
				lblEnterTheWeight.setVisible(true);
				subtractBlank();	
			}
	// Checks if Field Melee is blank or 0.0
			try{
				melee = Double.parseDouble(textFieldMelee.getText());
				if (melee == 0.0)
					{						
						lblEnterTheMelee.setVisible(true);
						subtractBlank();				
					}
				else if (textFieldMelee.getText().contains("-"))
				{						
					lblEnterTheMelee.setVisible(true);
					subtractBlank();					
				}
					else
					{
						lblEnterTheMelee.setVisible(false);
						addBlank();
					}
			}catch (NumberFormatException e){
				lblEnterTheMelee.setVisible(true);
				subtractBlank();
			}		
	}
	
// Subtracts blank if a textField is left blank when a user tries to add, update or remove Dinosaur		
	public void subtractBlank()
	{
		blank = blank - 1;
	}

// Add blank if a textField is left blank when a user tries to add, update or remove Dinosaur	
	public void addBlank()
	{
		blank = blank + 1;
	}
	
	//protected Object textFieldDinosaur;
    
	/**
	 * Create the frame.
	 */
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JTable() {
		setResizable(false);
		setTitle("Table");		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);		
		setBounds(100, 100, 1245, 610);
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setForeground(Color.RED);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);		
		setLocationRelativeTo(null);
		
	
		
	// Creates the table	
		table = new javax.swing.JTable();
		JTextField tf = new JTextField();
		tf.setEditable(false);
		DefaultCellEditor editor = new DefaultCellEditor(tf);
		table.setDefaultEditor(Object.class, editor);
		table.setFillsViewportHeight(true);
		table.setSurrendersFocusOnKeystroke(true);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);		
		table.setBorder(new LineBorder(Color.WHITE));
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setOpaque(true);	
		table.setGridColor(Color.WHITE);
		table.getTableHeader().setBackground(Color.GRAY);
		table.setBackground(Color.GRAY);
		table.setForeground(Color.WHITE);
		
	// Sets the table header	
		JTableHeader header = table.getTableHeader();
		header.setForeground(Color.WHITE);
		header.setFont(new Font("Tahoma", Font.BOLD, 15));
		header.setBorder(new LineBorder(Color.WHITE));
		
	// Loads table from data base file 
				try {							
					if (MainMenu.fileLoaded == true){					
						connection = MainMenu.dbConnectorLoaded();							
					}					
					else if (MainMenu.fileLoaded == false){					
						connection = MainMenu.dbConnectorNew();					
					}					
					String query="SELECT * FROM ARK4";
					PreparedStatement pst=connection.prepareStatement(query);
					ResultSet rs=pst.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
					pst.close();
					rs.close();
					connection.close();	
				} catch (Exception e) {
					e.printStackTrace();
				}
				
// Adds a listener for if the table is clicked on				
		table.addMouseListener(new MouseAdapter() {
			@Override	
		// Checks if mouse has clicked the table
			public void mouseClicked(MouseEvent arg0) {
				clearLabels();
				String Name_ = null; // Will hold the values of the row selected
				int row = table.getSelectedRow();
				try{
				Name_=(table.getModel().getValueAt(row, 2)).toString();	
			// Catches an error if a user clicks on a blank space on the table 
				}catch (ArrayIndexOutOfBoundsException e)		{
					return;
				}
				try {					
					if (MainMenu.fileLoaded == true){					
						connection = MainMenu.dbConnectorLoaded();					
					}					
					else if (MainMenu.fileLoaded == false){					
						connection = MainMenu.dbConnectorNew();					
					}						
					String query="Select * from ARK4 where Name='"+Name_+"' ";					
					PreparedStatement pst=connection.prepareStatement(query);					
					ResultSet rs=pst.executeQuery();
				// Sets the TextFields and combobox to the values selected in the table
						while(rs.next())
						{								
							comboBoxdDinosaur.setSelectedItem(rs.getString("Dinosaur"));
							comboBoxdGender.setSelectedItem(rs.getString("Gender"));							
							textFieldName.setText(rs.getString("Name"));	
							textFieldLevel.setText(rs.getString("Level"));							
							textFieldHealth.setText(rs.getString("Health"));
							textFieldStamina.setText(rs.getString("Stamina"));
							textFieldOxygen.setText(rs.getString("Oxygen"));
							textFieldWeight.setText(rs.getString("Weight"));
							textFieldMelee.setText(rs.getString("Melee"));
						}						
						pst.close();						
						connection.close();											
				}catch (Exception e){
					e.printStackTrace();
				}
			}		
			
		});	
		
	// Creates the ScrollPane for the table
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setBackground(Color.GRAY);
		scrollPane.setBounds(237, 11, 675, 561);		
		scrollPane.setViewportView(table);
		contentPane.add(scrollPane);
				
	// Button for if user clicks on Add Dinosaur				
		JButton btnAddDinosaur = new JButton("Add Dinosaur");
		btnAddDinosaur.setForeground(Color.BLACK);
		btnAddDinosaur.setBackground(new Color(105, 105, 105));
		btnAddDinosaur.setFont(new Font("Times New Roman", Font.BOLD, 23));
		btnAddDinosaur.setBounds(10, 408, 208, 32);
		contentPane.add(btnAddDinosaur);
		btnAddDinosaur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {									
				try{	
					checkFields();
					if (MainMenu.fileLoaded == true){					
						connection = MainMenu.dbConnectorLoaded();							
					}					
					else if (MainMenu.fileLoaded == false){					
						connection = MainMenu.dbConnectorNew();					
					}					
				String query="insert into ARK4 (Dinosaur,Gender,Name,Level,Health,"
						+ "Stamina,Oxygen,Weight,Melee) values(?,?,?,?,?,?,?,?,?)";
			// Sets values based on user input
				PreparedStatement pst=connection.prepareStatement(query);
				pst.setObject(1, comboBoxdDinosaur.getSelectedItem());	
				pst.setObject(2, comboBoxdGender.getSelectedItem());				
				pst.setString(3, textFieldName.getText() );					
				pst.setInt(4, level);					
				pst.setDouble(5, health);				
				pst.setDouble(6, stamina);			
				pst.setDouble(7, oxygen);
				pst.setDouble(8, weight);
				pst.setDouble(9, melee);					
		
			// If all fields have been entered into, it will save the data	
					try {
					// Checks if all fields have been entered into correctly by using the blank value
						if (blank == 7)
						{								
							pst.execute();	
							refreshTable();
							lblError.setVisible(false);
							lblNameAlreadyExist.setVisible(false);
							lblDataAdded.setVisible(true);																
							blank = 0;								
							connection.close();							
						}else{									
							lblNameAlreadyExist.setVisible(false);
							lblError.setVisible(true);
							blank = 0;	
							connection.close();									
						}
			// If duplicate Name is entered, will display a message above the Name	
					}catch (org.sqlite.SQLiteException e) 
					{								
							lblError.setVisible(false);															
							lblNameAlreadyExist.setVisible(true);	
							connection.close();
							blank = 0;						
					}
				}										
				 catch (Exception e) {
					e.printStackTrace();
				}								
			}	
		});	
		
	// Button for if user clicks on Update Dinosaur	
		JButton btnUpdateDinosaur = new JButton("Update Dinosaur");
		btnUpdateDinosaur.setForeground(Color.BLACK);
		btnUpdateDinosaur.setFont(new Font("Times New Roman", Font.BOLD, 23));
		btnUpdateDinosaur.setBackground(SystemColor.controlDkShadow);
		btnUpdateDinosaur.setBounds(10, 451, 208, 32);
		contentPane.add(btnUpdateDinosaur);
		btnUpdateDinosaur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearLabels();	
				try{
					@SuppressWarnings("unused")
					String hold; // Holds values from table
					String countOfName; // Used hold the number of occurrences there are of Name
					int number;
					if (MainMenu.fileLoaded == true){					
						connection = MainMenu.dbConnectorLoaded();						
					}					
					else if (MainMenu.fileLoaded == false){					
						connection = MainMenu.dbConnectorNew();					
					}					
					String query="Select count(Name) from ARK4 where Name= '"+textFieldName.getText()+"'";					
						hold = textFieldName.getText();;
						countOfName = textFieldName.getText();
						PreparedStatement pst=connection.prepareStatement(query);
						ResultSet rs=pst.executeQuery();		
					// Adds up the number of Name occurrences
						if(rs.next()){
							String sum=rs.getString("count(Name)");
							countOfName = sum;							
						}
						pst.close();
						connection.close();
						number = Integer.parseInt(countOfName);
					// Checks if the Name Exist in the database table
						if (number == 0){
							lblMustEnterExistingName.setVisible(true);							
						}
						else if (number > 0)
						{
							checkFields();	
							if (MainMenu.fileLoaded == true){					
								connection = MainMenu.dbConnectorLoaded();	
								}					
							else if (MainMenu.fileLoaded == false){					
								connection = MainMenu.dbConnectorNew();					
							}						
							String query2 = "Update ARK4 set "
									+ "Dinosaur='"+comboBoxdDinosaur.getSelectedItem()+"'"
									+ ",Gender='"+comboBoxdGender.getSelectedItem()+"'"	
									+ ",Name='"+textFieldName.getText()+"'"
									+ ",Level='"+textFieldLevel.getText()+"'"
									+ ",Health='"+textFieldHealth.getText()+"'"
									+ ",Stamina='"+textFieldStamina.getText()+"'"
									+ ",Oxygen='"+textFieldOxygen.getText()+"'"
									+ ",Weight='"+textFieldWeight.getText()+"'"
									+ ",Melee='"+textFieldMelee.getText()+"'"
									+ "where Name='"+textFieldName.getText()+"'";						
							PreparedStatement pst2=connection.prepareStatement(query2);
						// Checks if any fields were left blank and sets the new values if a textFields
						// are valid.
							if (blank == 7)
							{								
								pst2.execute();
								refreshTable();
								clearFieldsAndLabels();
								lblRecordUpdated.setVisible(true);							
								pst2.close();
								connection.close();								
							}else{							
								lblError.setVisible(true);
								blank = 0;	
								connection.close();		
								}					
						}					
					}catch(Exception e){
						e.printStackTrace();
					}			
			 }
		});
		
	// Button for if user clicks on Remove Dinosaur
		JButton btnRemoveDinosaur = new JButton("Remove Dinosaur");
		btnRemoveDinosaur.setForeground(Color.BLACK);
		btnRemoveDinosaur.setFont(new Font("Times New Roman", Font.BOLD, 22));
		btnRemoveDinosaur.setBackground(new Color(105, 105, 105));
		btnRemoveDinosaur.setBounds(10, 497, 208, 32);
		contentPane.add(btnRemoveDinosaur);
		btnRemoveDinosaur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			// clears labels
				clearLabels();
				try {	
					@SuppressWarnings("unused")
					String hold; // Holds values from table
					String countOfName; // Used hold the number of occurrences there are of Name
					int number;
					if (MainMenu.fileLoaded == true){					
						connection = MainMenu.dbConnectorLoaded();						
					}					
					else if (MainMenu.fileLoaded == false){					
						connection = MainMenu.dbConnectorNew();					
					}					
					String query="Select count(Name) from ARK4 where Name= '"+textFieldName.getText()+"'";					
						hold = textFieldName.getText();;
						countOfName = textFieldName.getText();
						PreparedStatement pst=connection.prepareStatement(query);
						ResultSet rs=pst.executeQuery();
					// Adds up the number of Name occurrences
						if(rs.next()){
							String sum=rs.getString("count(Name)");
							countOfName = sum;							
						}
						pst.close();
						connection.close();
						number = Integer.parseInt(countOfName);
					// Checks if the Name Exist in the database table
						if (number == 0){
							lblMustEnterExistingName.setVisible(true);							
						}
						else if (number > 0){
							if (MainMenu.fileLoaded == true){					
								connection = MainMenu.dbConnectorLoaded();									
							}					
							else if (MainMenu.fileLoaded == false){					
								connection = MainMenu.dbConnectorNew();	
							}
						// Creates a dialog box asking if the user is sure they want to delete that record	
							int action = JOptionPane.showConfirmDialog(null, "Are you sure you want "
									+ "to Delete this record?",	"Delete", JOptionPane.YES_NO_OPTION);
						// If yes, then the record will be deleted
							if (action == 0){	
							String query2="delete from ARK4 where Name='"+textFieldName.getText()+"'";
							PreparedStatement pst2=connection.prepareStatement(query2);						
							pst2.execute();
							refreshTable();
							clearFieldsAndLabels();
							connection.close();
							lblRecordDeleted.setVisible(true);								
							}
						}
					}catch (Exception e){
					e.printStackTrace();
				}				
				
			}
		});		
		
		
	// Button for if user clicks on Clear Fields	
		JButton btnClearFields = new JButton("Clear Fields");
		btnClearFields.setForeground(Color.BLACK);
		btnClearFields.setBackground(new Color(105, 105, 105));
		btnClearFields.setFont(new Font("Times New Roman", Font.BOLD, 23));
		btnClearFields.setBounds(10, 540, 208, 32);
		contentPane.add(btnClearFields);
		btnClearFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				// Will set the textFields, Labels visibility to false 
				//and refresh the table.
					clearFieldsAndLabels();
					refreshTable();						
			}
		});	
		
	// Creates label for Dinosaurs
		JLabel lblDinosaur = new JLabel("Dinosaur:");
		lblDinosaur.setBackground(new Color(220, 220, 220));
		lblDinosaur.setForeground(Color.WHITE);
		lblDinosaur.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblDinosaur.setBounds(10, 13, 90, 25);
		contentPane.add(lblDinosaur);
		
	// Creates label for Gender		
		JLabel lblDinosaurGender;
		lblDinosaurGender = new JLabel("Gender:");
		lblDinosaurGender.setBackground(new Color(220, 220, 220));
		lblDinosaurGender.setForeground(Color.WHITE);
		lblDinosaurGender.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblDinosaurGender.setBounds(10, 49, 90, 32);
		contentPane.add(lblDinosaurGender);	
		
	// Creates label for Name
		JLabel lblName = new JLabel("Name:");
		lblName.setBackground(new Color(220, 220, 220));
		lblName.setForeground(Color.WHITE);
		lblName.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblName.setBounds(10, 94, 63, 25);
		contentPane.add(lblName);
		
	// Creates label for Level		
		JLabel lblLevel = new JLabel("Level:");
		lblLevel.setBackground(new Color(220, 220, 220));
		lblLevel.setForeground(Color.WHITE);
		lblLevel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblLevel.setBounds(10, 136, 63, 25);
		contentPane.add(lblLevel);
		
	// Creats label for Health	
		JLabel lblHealth = new JLabel("Health:");
		lblHealth.setBackground(new Color(220, 220, 220));
		lblHealth.setForeground(Color.WHITE);
		lblHealth.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblHealth.setBounds(10, 179, 74, 25);
		contentPane.add(lblHealth);
		
	// Creates label for Stamina	
		JLabel lblStamina = new JLabel("Stamina:");
		lblStamina.setBackground(new Color(220, 220, 220));
		lblStamina.setForeground(Color.WHITE);
		lblStamina.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblStamina.setBounds(10, 222, 90, 25);
		contentPane.add(lblStamina);
		
	// Creates label for Oxygen	
		JLabel lblOxygen = new JLabel("Oxygen:");
		lblOxygen.setBackground(new Color(220, 220, 220));
		lblOxygen.setForeground(Color.WHITE);
		lblOxygen.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblOxygen.setBounds(10, 265, 90, 25);
		contentPane.add(lblOxygen);

	// Creates label for Weight	
		JLabel lblWeight = new JLabel("Weight:");
		lblWeight.setBackground(new Color(220, 220, 220));
		lblWeight.setForeground(Color.WHITE);
		lblWeight.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblWeight.setBounds(10, 308, 74, 25);
		contentPane.add(lblWeight);
		
	// Creates label for Melee	
		JLabel lblMelee = new JLabel("Melee:");
		lblMelee.setBackground(new Color(220, 220, 220));
		lblMelee.setForeground(Color.WHITE);
		lblMelee.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblMelee.setBounds(10, 352, 63, 25);
		contentPane.add(lblMelee);			
	
	// Creates TextField for Name
		textFieldName = new JTextField();
		textFieldName.setFont(new Font("Tahoma", Font.BOLD, 15));
		textFieldName.setForeground(Color.WHITE);
		textFieldName.setBackground(new Color(105, 105, 105));
		textFieldName.setText("");
		textFieldName.setColumns(10);
		textFieldName.setBounds(110, 95, 108, 25);
		contentPane.add(textFieldName);
	
	// Creates TextField for Level
		textFieldLevel = new JTextField();
		textFieldLevel.setFont(new Font("Tahoma", Font.BOLD, 15));
		textFieldLevel.setForeground(Color.WHITE);
		textFieldLevel.setBackground(new Color(105, 105, 105));
		textFieldLevel.setText("");
		textFieldLevel.setColumns(10);
		textFieldLevel.setBounds(110, 138, 108, 25);
		contentPane.add(textFieldLevel);
		
	// Creates TextField for Health	
		textFieldHealth = new JTextField();
		textFieldHealth.setFont(new Font("Tahoma", Font.BOLD, 15));
		textFieldHealth.setForeground(Color.WHITE);
		textFieldHealth.setBackground(new Color(105, 105, 105));
		textFieldHealth.setText("");
		textFieldHealth.setColumns(10);
		textFieldHealth.setBounds(110, 181, 108, 25);
		contentPane.add(textFieldHealth);
	
	// Creates TextField for Stamina	
		textFieldStamina = new JTextField();
		textFieldStamina.setFont(new Font("Tahoma", Font.BOLD, 15));
		textFieldStamina.setForeground(Color.WHITE);
		textFieldStamina.setBackground(new Color(105, 105, 105));
		textFieldStamina.setText("");
		textFieldStamina.setColumns(10);
		textFieldStamina.setBounds(110, 224, 108, 25);
		contentPane.add(textFieldStamina);
	
	// Creates TextField for Oxygen
		textFieldOxygen = new JTextField();
		textFieldOxygen.setFont(new Font("Tahoma", Font.BOLD, 15));
		textFieldOxygen.setForeground(Color.WHITE);
		textFieldOxygen.setBackground(new Color(105, 105, 105));
		textFieldOxygen.setText("");
		textFieldOxygen.setColumns(10);
		textFieldOxygen.setBounds(110, 267, 108, 25);
		contentPane.add(textFieldOxygen);
	
	// Creates TextField for Weight
		textFieldWeight = new JTextField();
		textFieldWeight.setFont(new Font("Tahoma", Font.BOLD, 15));
		textFieldWeight.setForeground(Color.WHITE);
		textFieldWeight.setBackground(new Color(105, 105, 105));
		textFieldWeight.setText("");
		textFieldWeight.setColumns(10);
		textFieldWeight.setBounds(110, 310, 108, 25);
		contentPane.add(textFieldWeight);
	
	// Creates TextField for Melee
		textFieldMelee = new JTextField();
		textFieldMelee.setFont(new Font("Tahoma", Font.BOLD, 15));
		textFieldMelee.setForeground(Color.WHITE);
		textFieldMelee.setBackground(new Color(105, 105, 105));
		textFieldMelee.setText("");
		textFieldMelee.setColumns(10);
		textFieldMelee.setBounds(110, 353, 108, 25);
		contentPane.add(textFieldMelee);
		
	// Creates a Combo Box for Dinosaur
		comboBoxdDinosaur = new JComboBox();
		comboBoxdDinosaur.setForeground(Color.WHITE);
		comboBoxdDinosaur.setBackground(new Color(105, 105, 105));
		comboBoxdDinosaur.setFont(new Font("Tahoma", Font.BOLD, 13));
		comboBoxdDinosaur.setBounds(110, 12, 117, 25);
		contentPane.add(comboBoxdDinosaur);	
		comboBoxdDinosaur.setModel(new DefaultComboBoxModel(new String[] {"Rex", "Trike", "Compy", 
				"Pteranodon", "Megalodon", "Dodo"}));
				
	// Creates a Combo Box of Gender	
		comboBoxdGender = new JComboBox();
		comboBoxdGender.setForeground(Color.WHITE);
		comboBoxdGender.setBackground(new Color(105, 105, 105));
		comboBoxdGender.setFont(new Font("Tahoma", Font.BOLD, 15));
		comboBoxdGender.setBounds(110, 51, 117, 25);
		contentPane.add(comboBoxdGender);	
		comboBoxdGender.setModel(new DefaultComboBoxModel(new String[] {"Male", "Female"}));			
		
	// Will Display Red Text above the Name textField if nothing is entered
		lblBlankName = new JLabel("Enter a Name");
		lblBlankName.setVerticalAlignment(SwingConstants.BOTTOM);
		lblBlankName.setHorizontalAlignment(SwingConstants.CENTER);
		lblBlankName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblBlankName.setForeground(new Color(255, 255, 0));
		lblBlankName.setBackground(new Color(220, 220, 220));
		lblBlankName.setBounds(110, 75, 108, 20);
		contentPane.add(lblBlankName);
		lblBlankName.setVisible(false);
		
	// Will Display Red Text above the level textField if nothing is entered	
		lblEnterALevel = new JLabel("Enter A Valid Level");
		lblEnterALevel.setBackground(new Color(220, 220, 220));
		lblEnterALevel.setVerticalAlignment(SwingConstants.BOTTOM);
		lblEnterALevel.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterALevel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEnterALevel.setForeground(new Color(255, 255, 0));
		lblEnterALevel.setBounds(110, 106, 108, 32);
		contentPane.add(lblEnterALevel);
		lblEnterALevel.setVisible(false);
		
	// Will Display Red Text above the Health textField if nothing is entered		
		lblEnterTheHealth = new JLabel("Enter A Valid Health");
		lblEnterTheHealth.setBackground(new Color(220, 220, 220));
		lblEnterTheHealth.setVerticalAlignment(SwingConstants.BOTTOM);
		lblEnterTheHealth.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterTheHealth.setForeground(new Color(255, 255, 0));
		lblEnterTheHealth.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEnterTheHealth.setBounds(99, 149, 128, 34);
		contentPane.add(lblEnterTheHealth);
		lblEnterTheHealth.setVisible(false);
		
	// Will Display Red Text above the Stamina textField if nothing is entered		
		lblEnterTheStamina = new JLabel("Enter A Valid Stamina");
		lblEnterTheStamina.setVerticalAlignment(SwingConstants.BOTTOM);
		lblEnterTheStamina.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterTheStamina.setForeground(new Color(255, 255, 0));
		lblEnterTheStamina.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEnterTheStamina.setBounds(94, 194, 145, 31);
		contentPane.add(lblEnterTheStamina);
		lblEnterTheStamina.setVisible(false);
		
	// Will Display Red Text above the Oxygen textField if nothing is entered		
		lblEnterTheOxygen = new JLabel("Enter A Valid Oxygen");
		lblEnterTheOxygen.setVerticalAlignment(SwingConstants.BOTTOM);
		lblEnterTheOxygen.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterTheOxygen.setForeground(new Color(255, 255, 0));
		lblEnterTheOxygen.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEnterTheOxygen.setBounds(94, 243, 145, 25);
		contentPane.add(lblEnterTheOxygen);
		lblEnterTheOxygen.setVisible(false);
		
	// Will Display Red Text above the Weight textField if nothing is entered			
		lblEnterTheWeight = new JLabel("Enter A Valid Weight");
		lblEnterTheWeight.setVerticalAlignment(SwingConstants.BOTTOM);
		lblEnterTheWeight.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterTheWeight.setForeground(new Color(255, 255, 0));
		lblEnterTheWeight.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEnterTheWeight.setBounds(94, 279, 145, 31);
		contentPane.add(lblEnterTheWeight);
		lblEnterTheWeight.setVisible(false);
		
	// Will Display Red Text above the Melee textField if nothing is entered		
		lblEnterTheMelee = new JLabel("Enter A Valid Melee");
		lblEnterTheMelee.setVerticalAlignment(SwingConstants.BOTTOM);
		lblEnterTheMelee.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterTheMelee.setForeground(new Color(255, 255, 0));
		lblEnterTheMelee.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEnterTheMelee.setBounds(110, 321, 108, 33);
		contentPane.add(lblEnterTheMelee);
		lblEnterTheMelee.setVisible(false);
		
	// Will Display Orange Text above the Name textField if entered name already exist
		lblNameAlreadyExist = new JLabel("Name Already Exist");
		lblNameAlreadyExist.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNameAlreadyExist.setForeground(Color.ORANGE);
		lblNameAlreadyExist.setHorizontalAlignment(SwingConstants.CENTER);
		lblNameAlreadyExist.setBounds(105, 81, 122, 14);
		contentPane.add(lblNameAlreadyExist);
		lblNameAlreadyExist.setVisible(false);
	
	 // Will Display Green Text above the Add Dinosaur button when a record has been add successfully
		lblDataAdded = new JLabel("Record Added");
		lblDataAdded.setForeground(new Color(0, 255, 0));
		lblDataAdded.setFont(new Font("Tahoma", Font.ITALIC, 15));
		lblDataAdded.setHorizontalAlignment(SwingConstants.CENTER);
		lblDataAdded.setBounds(10, 383, 208, 25);
		contentPane.add(lblDataAdded);
		lblDataAdded.setVisible(false);
		
	// Will Display Yellow Text above the Add Dinosaur button when not all fields are valid or left blank	
		lblError = new JLabel("Please Enter Data All Fields");
		lblError.setHorizontalAlignment(SwingConstants.CENTER);
		lblError.setFont(new Font("Tahoma", Font.ITALIC, 15));
		lblError.setForeground(new Color(255, 255, 51));
		lblError.setBounds(10, 388, 208, 20);
		contentPane.add(lblError);
		lblError.setVisible(false);
		
	 // Will Display Green Text above the Add Dinosaur button when a record has been deleted successfully	
		lblRecordDeleted = new JLabel("Record Deleted");
		lblRecordDeleted.setFont(new Font("Tahoma", Font.ITALIC, 15));
		lblRecordDeleted.setForeground(Color.GREEN);
		lblRecordDeleted.setVerticalAlignment(SwingConstants.BOTTOM);
		lblRecordDeleted.setHorizontalAlignment(SwingConstants.CENTER);
		lblRecordDeleted.setBounds(10, 378, 208, 30);
		contentPane.add(lblRecordDeleted);
		lblRecordDeleted.setVisible(false);
		
	// Will Display Yellow Text above the Add Dinosaur button when a name entered 
	// does not match any database table names
		lblMustEnterExistingName = new JLabel("Must Enter An Existing Name");
		lblMustEnterExistingName.setFont(new Font("Tahoma", Font.ITALIC, 15));
		lblMustEnterExistingName.setForeground(Color.YELLOW);
		lblMustEnterExistingName.setVerticalAlignment(SwingConstants.BOTTOM);
		lblMustEnterExistingName.setHorizontalAlignment(SwingConstants.CENTER);
		lblMustEnterExistingName.setBounds(10, 378, 208, 30);
		lblMustEnterExistingName.setVisible(false);
		contentPane.add(lblMustEnterExistingName);
		
	// Will Display Green Text above the Add Dinosaur button when a record has been updated successfully	
		lblRecordUpdated = new JLabel("Record Updated");
		lblRecordUpdated.setFont(new Font("Tahoma", Font.ITALIC, 15));
		lblRecordUpdated.setForeground(Color.GREEN);
		lblRecordUpdated.setVerticalAlignment(SwingConstants.BOTTOM);
		lblRecordUpdated.setHorizontalAlignment(SwingConstants.CENTER);
		lblRecordUpdated.setBounds(10, 378, 208, 30);
		lblRecordUpdated.setVisible(false);
		contentPane.add(lblRecordUpdated);		
		
	// Creates a title above the search fields	
		JLabel lblSearchForDinosaur = new JLabel("Search For Dinosaur:");
		lblSearchForDinosaur.setBackground(new Color(220, 220, 220));
		lblSearchForDinosaur.setForeground(Color.WHITE);
		lblSearchForDinosaur.setHorizontalAlignment(SwingConstants.CENTER);
		lblSearchForDinosaur.setFont(new Font("Times New Roman", Font.BOLD, 31));
		lblSearchForDinosaur.setBounds(932, 29, 292, 32);
		contentPane.add(lblSearchForDinosaur);
		
	// Creates a Line under the title Search For Dinosaur 	
		JLabel labelUnderLine = new JLabel("______________________________");
		labelUnderLine.setForeground(Color.WHITE);
		labelUnderLine.setHorizontalAlignment(SwingConstants.CENTER);
		labelUnderLine.setFont(new Font("Times New Roman", Font.BOLD, 20));
		labelUnderLine.setBounds(922, 24, 312, 51);
		contentPane.add(labelUnderLine);		

	// Creates a combo box of categories to search in	
		comboBoxSelect = new JComboBox();
		comboBoxSelect.setForeground(Color.WHITE);
		comboBoxSelect.setBackground(new Color(105, 105, 105));
		comboBoxSelect.setFont(new Font("Tahoma", Font.BOLD, 17));
		comboBoxSelect.setModel(new DefaultComboBoxModel(new String[] {"Dinosaur", "Gender", 
				"Name", "Level", "Health", "Oxygen", "Weight", "Melee"}));
		comboBoxSelect.setBounds(975, 86, 193, 32);
		contentPane.add(comboBoxSelect);
		
	// Creates a textField to enter what you want to search for	
		textFieldSearch = new JTextField();
		textFieldSearch.setForeground(Color.WHITE);
		textFieldSearch.setBackground(new Color(105, 105, 105));
		textFieldSearch.setText("");
		textFieldSearch.setFont(new Font("Tahoma", Font.BOLD, 18));
		textFieldSearch.setBounds(975, 152, 193, 32);
		contentPane.add(textFieldSearch);
		textFieldSearch.setColumns(10);
		
	// Button for if user clicks on Load Database		
		JButton btnSearch = new JButton("Search");
		btnSearch.setForeground(Color.BLACK);
		btnSearch.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btnSearch.setBackground(new Color(105, 105, 105));
		btnSearch.setBounds(975, 216, 193, 32);
		contentPane.add(btnSearch);	
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{	
				// Sets the string selection to the values selected for the search combo box
					String selection=(String)comboBoxSelect.getSelectedItem();
					if (MainMenu.fileLoaded == true){					
						connection = MainMenu.dbConnectorLoaded();							
					}					
					else if (MainMenu.fileLoaded == false){					
						connection = MainMenu.dbConnectorNew();					
					}				
					String query="select Dinosaur,Gender,Name,Level,"
							+ "Health,Stamina,Oxygen,Weight,Melee from ARK4 where "+selection+" like ?";					
					PreparedStatement pst=connection.prepareStatement(query);
				// Sets the pst string value to what was entered in the textFieldSearch
					pst.setString(1, textFieldSearch.getText()+"%");						
					ResultSet rs=pst.executeQuery();
				// Sets the table to show only the records that contain what was in the search fields
					table.setModel(DbUtils.resultSetToTableModel(rs));						
					pst.close();
					rs.close();
					connection.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}				
			}
		});
		
	// Creates a search button	
		JButton btnClearSearch = new JButton("Clear Search");
		btnClearSearch.setForeground(Color.BLACK);
		btnClearSearch.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btnClearSearch.setBackground(new Color(105, 105, 105));
		btnClearSearch.setBounds(975, 280, 193, 32);
		contentPane.add(btnClearSearch);
		btnClearSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			// Will refresh the table and clear the textFieldSearch.
				refreshTable();
				textFieldSearch.setText("");
			}
		});			
			
	// Creates a bar line to separate the search fields area from the Menu and Exit buttons.				
		JLabel labelBarLine = new JLabel("______________________________");
		labelBarLine.setForeground(Color.WHITE);
		labelBarLine.setHorizontalAlignment(SwingConstants.CENTER);
		labelBarLine.setFont(new Font("Times New Roman", Font.BOLD, 20));
		labelBarLine.setBounds(922, 337, 312, 51);
		contentPane.add(labelBarLine);	
		
	// Creates a Menu button		
		JButton btnMenu = new JButton("Menu");
		btnMenu.setForeground(Color.BLACK);
		btnMenu.setBackground(new Color(105, 105, 105));
		btnMenu.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btnMenu.setBounds(975, 411, 193, 48);
		contentPane.add(btnMenu);			
		btnMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				// Goes back to the Main Menu
					connection.close();					
					setVisible(false);
					MainMenu window = new MainMenu();
					window.frmMainMenu.setVisible(true);	
					window.tableFound = true;
					window.loadTotals();					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	// Creates an Exit button	
			JButton btnExit = new JButton("Exit");
			btnExit.setForeground(Color.BLACK);
			btnExit.setFont(new Font("Times New Roman", Font.BOLD, 20));
			btnExit.setBackground(new Color(105, 105, 105));
			btnExit.setBounds(975, 493, 193, 43);
			contentPane.add(btnExit);
			btnExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
				// Exits the program
					System.exit(0);
				}
			});	
	// Creates a label for the background image
		JLabel lblBackgroundImg = new JLabel("");
		lblBackgroundImg.setBounds(0, 0, 1239, 582);
		contentPane.add(lblBackgroundImg);
		URL url = Main.class.getResource("/BackGroundTable.png");
		lblBackgroundImg.setIcon(new ImageIcon(url));	
	}
}