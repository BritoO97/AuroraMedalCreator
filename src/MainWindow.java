import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class MainWindow {

	// GUI Variables
	// Frames and Panels
	private JFrame frmAuroraMedalCreator;
	private JPanel medalPanel;
	private JPanel controlPanel;
	private JPanel mainPanel;
	private JPanel mainLeft;
	private JPanel mainRight;
	
	
	// Tables
	private JTable medalTable;
	private JTable conditionTable;
	
	// Buttons
	private JButton btnNewMedalSet;
	private JButton btnSaveMedal;
	private JButton btnOpenMedalSet;
	private JButton btnSaveMedalSet;
	
	// Input (Text Boxes, Text Areas, Checkboxes, Etc)
	private JTextField medalNameField;
	private JTextField scoreField;
	private JTextField medalAbbreviation;
	private JTextArea medalDescription;
	private JComboBox<String> conditionSelector;
	private JCheckBox multipleCheckbox;
	
	// Labels
	private JLabel medalImage;
	private JLabel lblMultiple;
	
	// Data Variables
	private ArrayList<Medal> medalArray;
	private ArrayList<String> conditionArray;
	
	private String currentMedalFile;
	private ImageIcon currentIcon;
	private Medal currentMedal;
	private boolean isNewMedal;
	private boolean editingMedal;
	private JPanel medalImageWrapper;
	
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			/*
			 * 
			 * The following is a workaround for a known bug in Java 8 for Windows 10 when using screen scaling where some icons will get cropped.
			 * 
			 */
			
			try
			{
				String[][] icons =
				    {
				        {"OptionPane.errorIcon", "65581"},
				        {"OptionPane.warningIcon", "65577"},
				        {"OptionPane.questionIcon", "65579"},
				        {"OptionPane.informationIcon", "65583"}
				    };
			    
			    //obtain a method for creating proper icons
			    Method getIconBits = Class.forName("sun.awt.shell.Win32ShellFolder2").getDeclaredMethod("getIconBits", new Class[]{long.class, int.class});
			    getIconBits.setAccessible(true);
			    //calculate scaling factor
			    double dpiScalingFactor = Toolkit.getDefaultToolkit().getScreenResolution() / 96.0;
			    int icon32Size = (dpiScalingFactor == 1)?(32):((dpiScalingFactor == 1.25)?(40):((dpiScalingFactor == 1.5)?(45):((int) (32 * dpiScalingFactor))));
			    Object[] arguments = {null, icon32Size};
			    for (String[] s:icons)
			    {
			        if (UIManager.get(s[0]) instanceof ImageIcon)
			        {
			            arguments[0] = Long.valueOf(s[1]);
			            //this method is static, so the first argument can be null
			            int[] iconBits = (int[]) getIconBits.invoke(null, arguments);
			            if (iconBits != null)
			            {
			                //create an image from the obtained array
			                BufferedImage img = new BufferedImage(icon32Size, icon32Size, BufferedImage.TYPE_INT_ARGB);
			                img.setRGB(0, 0, icon32Size, icon32Size, iconBits, 0, icon32Size);
			                ImageIcon newIcon = new ImageIcon(img);
			                //override previous icon with the new one
			                UIManager.put(s[0], newIcon);
			            }
			        }
			    }
			}
			catch (Exception e)
			{
			    e.printStackTrace();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmAuroraMedalCreator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		conditionArray = ConditionReader.readConditions();
				
		int width = 1079;
		int height = 781;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width / 2) - (width / 2);
		int y = (screenSize.height / 2) - (height / 2);
		
		frmAuroraMedalCreator = new JFrame();
		frmAuroraMedalCreator.setResizable(false);
		frmAuroraMedalCreator.setTitle("Aurora Medal Creator");
		frmAuroraMedalCreator.setBounds(x, y, width, height);
		frmAuroraMedalCreator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		controlPanel = new JPanel();
		controlPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		medalPanel = new JPanel();
		medalPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		mainPanel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(frmAuroraMedalCreator.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, 1049, Short.MAX_VALUE)
						.addComponent(medalPanel, GroupLayout.DEFAULT_SIZE, 1049, Short.MAX_VALUE)
						.addComponent(controlPanel, GroupLayout.DEFAULT_SIZE, 1049, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(controlPanel, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(medalPanel, GroupLayout.PREFERRED_SIZE, 359, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, 207, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JButton btnNewMedal = new JButton("New Medal");
		btnNewMedal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageSelection();
			}
		});
		
		btnNewMedalSet = new JButton("New Medal Set");
		btnNewMedalSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newMedalSet();
			}
		});
		btnNewMedalSet.setMinimumSize(new Dimension(100, 25));
		btnNewMedalSet.setMaximumSize(new Dimension(100, 25));
		
		btnSaveMedal = new JButton("Save Medal");
		btnSaveMedal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isNewMedal)
					saveMedal();
				else
					updateMedal();
			}
		});
		
		btnSaveMedalSet = new JButton("Save Medal Set");
		btnSaveMedalSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveMedalSet();
			}
		});
		
		JButton btnDeleteMedal = new JButton("Delete Medal");
		btnDeleteMedal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteMedal();
			}
		});
		
		btnOpenMedalSet = new JButton("Open Medal Set");
		btnOpenMedalSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openMedalSet();
			}
		});
		
		JButton btnSettings = new JButton("Settings");
		btnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openSettings();
			}
		});
		GroupLayout gl_controlPanel = new GroupLayout(controlPanel);
		gl_controlPanel.setHorizontalGroup(
			gl_controlPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_controlPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnNewMedal)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSaveMedal)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnDeleteMedal)
					.addPreferredGap(ComponentPlacement.RELATED, 245, Short.MAX_VALUE)
					.addComponent(btnNewMedalSet, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSaveMedalSet)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnOpenMedalSet)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSettings)
					.addContainerGap())
		);
		gl_controlPanel.setVerticalGroup(
			gl_controlPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_controlPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_controlPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnSaveMedalSet, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
						.addComponent(btnSaveMedal, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
						.addComponent(btnDeleteMedal, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
						.addComponent(btnNewMedal, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
						.addComponent(btnOpenMedalSet, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
						.addComponent(btnNewMedalSet, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
						.addComponent(btnSettings, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
					.addContainerGap())
		);
		controlPanel.setLayout(gl_controlPanel);
		
		String[] tableHeaders = {"Ribbon", "Name", "Description", "Abbreviation", "Score", "Multiple", "File Name"};
		DefaultTableModel tableModel = new DefaultTableModel(new Object[][] {}, tableHeaders) 
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 5255732970743609703L;

			@Override
		    public Class<?> getColumnClass(int column) 
			{
				switch(column) 
				{
	            	case 0: 
	            		return ImageIcon.class;
	            	default: 
	            		return Object.class;
				}
			}
			
			@Override
			public boolean isCellEditable(int row, int column)
		    {
		      return false;
		    }
		};
		
		JScrollPane scrollPane = new JScrollPane();
				
		GroupLayout gl_medalPanel = new GroupLayout(medalPanel);
		gl_medalPanel.setHorizontalGroup(
			gl_medalPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 1047, Short.MAX_VALUE)
		);
		gl_medalPanel.setVerticalGroup(
			gl_medalPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
		);
		medalTable = new JTable(tableModel);
		medalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		medalTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event)
			{
				rowSelected(medalTable.getSelectedRow());
			}
		});
		scrollPane.setViewportView(medalTable);
		medalTable.setRowHeight(30);
		medalPanel.setLayout(gl_medalPanel);
		medalTable.getTableHeader().setReorderingAllowed(false);
		
		mainLeft = new JPanel();
		mainLeft.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		medalNameField = new JTextField();
		medalNameField.setBorder(new LineBorder(new Color(171, 173, 179)));
		medalNameField.setHorizontalAlignment(SwingConstants.CENTER);
		medalNameField.setText("Default Medal Name");
		medalNameField.setToolTipText("");
		medalNameField.setColumns(10);
		
		medalImageWrapper = new JPanel();
		medalImageWrapper.setAlignmentY(0.0f);
		medalImageWrapper.setAlignmentX(0.0f);
		medalImageWrapper.setPreferredSize(new Dimension(100, 30));
		medalImageWrapper.setSize(new Dimension(100, 30));
		
		medalDescription = new JTextArea();
		medalDescription.setBorder(new LineBorder(new Color(0, 0, 0)));
		medalDescription.setText("Default Medal Description");
		medalDescription.setToolTipText("");
		
		scoreField = new JTextField();
		scoreField.setHorizontalAlignment(SwingConstants.CENTER);
		scoreField.setText("100");
		scoreField.setColumns(10);
		
		JPanel panel = new JPanel();
		
		medalAbbreviation = new JTextField();
		medalAbbreviation.setHorizontalAlignment(SwingConstants.CENTER);
		medalAbbreviation.setText("DMN");
		medalAbbreviation.setColumns(10);
		GroupLayout gl_mainLeft = new GroupLayout(mainLeft);
		gl_mainLeft.setHorizontalGroup(
			gl_mainLeft.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainLeft.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_mainLeft.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_mainLeft.createSequentialGroup()
							.addComponent(medalImageWrapper, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(medalNameField, GroupLayout.PREFERRED_SIZE, 317, GroupLayout.PREFERRED_SIZE))
						.addComponent(medalDescription, GroupLayout.PREFERRED_SIZE, 424, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_mainLeft.createParallelGroup(Alignment.LEADING)
						.addComponent(scoreField, GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
						.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
						.addComponent(medalAbbreviation, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_mainLeft.setVerticalGroup(
			gl_mainLeft.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainLeft.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_mainLeft.createParallelGroup(Alignment.LEADING)
						.addComponent(medalImageWrapper, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(scoreField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(medalNameField, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_mainLeft.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_mainLeft.createSequentialGroup()
							.addComponent(medalAbbreviation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(77))
						.addGroup(gl_mainLeft.createSequentialGroup()
							.addComponent(medalDescription, GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
							.addContainerGap())))
		);
		panel.setLayout(new BorderLayout(0, 0));
		
		multipleCheckbox = new JCheckBox("");
		multipleCheckbox.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(multipleCheckbox, BorderLayout.SOUTH);
		
		lblMultiple = new JLabel("Multiple");
		panel.add(lblMultiple, BorderLayout.NORTH);
		lblMultiple.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblMultiple.setHorizontalAlignment(SwingConstants.CENTER);
		
		medalImage = new JLabel("");
		medalImage.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		medalImage.setAlignmentY(0.0f);
		medalImageWrapper.add(medalImage);
		medalImage.setIcon(null);
		mainLeft.setLayout(gl_mainLeft);
		
		mainRight = new JPanel();
		mainRight.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JButton btnDeleteCondition = new JButton("Delete Condition");
		btnDeleteCondition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteMedalCondition();
			}
		});
		
		conditionSelector = new JComboBox<String>();
		for (String s : conditionArray)
		{
			conditionSelector.addItem(s);
		}
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JButton btnAddCondition = new JButton("Add Condition");
		btnAddCondition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addMedalCondition();
			}
		});
		GroupLayout gl_mainRight = new GroupLayout(mainRight);
		gl_mainRight.setHorizontalGroup(
			gl_mainRight.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_mainRight.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_mainRight.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
						.addComponent(conditionSelector, Alignment.LEADING, 0, 492, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_mainRight.createSequentialGroup()
							.addComponent(btnDeleteCondition)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAddCondition)))
					.addContainerGap())
		);
		gl_mainRight.setVerticalGroup(
			gl_mainRight.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_mainRight.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_mainRight.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnDeleteCondition, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnAddCondition))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(conditionSelector, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		String[] conditionHeaders = {"ID", "Condition"};
		DefaultTableModel conditionTableModel = new DefaultTableModel(new Object[][] {}, conditionHeaders) 
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = -5800989207513644992L;

			@Override
		    public Class<?> getColumnClass(int column) 
			{
				switch(column) 
				{
	            	case 0: 
	            		return Integer.class;
	            	default: 
	            		return Object.class;
				}
			}
			
			@Override
			public boolean isCellEditable(int row, int column)
		    {
		      return false;
		    }
		};
		
		conditionTable = new JTable(conditionTableModel);
		conditionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		conditionTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		scrollPane_1.setViewportView(conditionTable);
		conditionTable.setBorder(new LineBorder(new Color(0, 0, 0)));
		conditionTable.getColumnModel().getColumn(0).setPreferredWidth(35);
		conditionTable.getColumnModel().getColumn(0).setMaxWidth(35);
		conditionTable.getColumnModel().getColumn(0).setMinWidth(35);
		conditionTable.getTableHeader().setReorderingAllowed(false);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		conditionTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		
		mainRight.setLayout(gl_mainRight);
		GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
		gl_mainPanel.setHorizontalGroup(
			gl_mainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addComponent(mainLeft, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(mainRight, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_mainPanel.setVerticalGroup(
			gl_mainPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(mainRight, GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
						.addComponent(mainLeft, GroupLayout.PREFERRED_SIZE, 202, Short.MAX_VALUE))
					.addContainerGap())
		);
		mainPanel.setLayout(gl_mainPanel);
		frmAuroraMedalCreator.getContentPane().setLayout(groupLayout);
		
		newSetup();
		
		Config.readConfig();
		if (!Config.configSet())
			openSettings();
	}
	
	private void newSetup()
	{
		((DefaultTableModel)medalTable.getModel()).setRowCount(0);
		((DefaultTableModel)conditionTable.getModel()).setRowCount(0);
		currentMedal = null;
		isNewMedal = true;
		editingMedal = false;
		
		medalArray = new ArrayList<Medal>();
		
		cleanFields();
		
	}
	
	private void cleanFields()
	{
		medalImage.setIcon(null);
		medalNameField.setText("Default Medal Name");
		medalDescription.setText("Default Medal Description");
		medalAbbreviation.setText("DMN");
		multipleCheckbox.setSelected(false);
		scoreField.setText("100");
	}
	
	private void updateMedalTable(Medal medal, ImageIcon icon)
	{
		Object[] dataRow = {icon, medal.getName(), medal.getDescription(), medal.getAbbreviation(), medal.getScore(), medal.getMultiple(), medal.getImgName()};
		DefaultTableModel model = (DefaultTableModel) medalTable.getModel();
		model.addRow(dataRow);
		medalTable.setModel(model);
	}
	
	private void updateMedalTable(Medal medal)
	{
		Object[] dataRow = {currentIcon, medal.getName(), medal.getDescription(), medal.getAbbreviation(), medal.getScore(), medal.getMultiple(), medal.getImgName()};
		DefaultTableModel model = (DefaultTableModel) medalTable.getModel();
		model.addRow(dataRow);
		medalTable.setModel(model);
	}
	
	private void rowSelected(int row)
	{
		
		if (row < 0)
			return;
		
		Medal selected = medalArray.get(row);
		
		medalNameField.setText(selected.getName());
		medalDescription.setText(selected.getDescription());
		medalAbbreviation.setText(selected.getAbbreviation());
		if (selected.getMultiple().equalsIgnoreCase("Y"))
			multipleCheckbox.setSelected(true);
		else
			multipleCheckbox.setSelected(false);
		medalImage.setIcon((ImageIcon)medalTable.getValueAt(row, 0));
		scoreField.setText(selected.getScore());
		
		currentMedal = selected;
		
		// clean old conditions table
		((DefaultTableModel)conditionTable.getModel()).setRowCount(0);
		
		
		ArrayList<Integer> medalCond = selected.getConditions();
		if (medalCond != null) {
			for (Integer id : medalCond)
			{
				String condition = conditionArray.get(id - 1);
				
				Object[] dataRow = {id, condition};
				DefaultTableModel model = (DefaultTableModel) conditionTable.getModel();
				boolean newCond = true;
				int numRows = model.getRowCount();
				for (int i = 0; i< numRows; i++)
				{
					String rowValue = (String)conditionTable.getValueAt(i, 1);
					if (rowValue.equals(condition))
					{
						newCond = false;
					}
				}
				if (newCond)
				{
					model.addRow(dataRow);
				}
				conditionTable.setModel(model);
				
			}
		}
		
		isNewMedal = false;
		editingMedal = true;
		
	}
	
	private void imageSelection()
	{
		FileDialog dialog = new FileDialog(frmAuroraMedalCreator, "Pick a medal Ribbon", FileDialog.LOAD);
		dialog.setDirectory(Config.defaultImageDirectory);
		dialog.setVisible(true);
		String directory = dialog.getDirectory();
		String fileName = dialog.getFile();
		
		if (fileName != null)
		{
			if (!validateImageFileName(fileName))
			{			
				String message = "The image files must be \"png\", \"jpg\", \"jpeg\", \"gif\", \"tiff\" or \"tif\" ";
				JOptionPane.showMessageDialog(frmAuroraMedalCreator, message, "Error" , JOptionPane.INFORMATION_MESSAGE);
				
				return;
			}
			
			cleanFields();
			
			currentIcon = new ImageIcon(directory + fileName);
			currentMedalFile = fileName;
			medalImage.setIcon(currentIcon);
			isNewMedal = true;
			editingMedal = true;
			((DefaultTableModel)conditionTable.getModel()).setRowCount(0);
			
			medalImageWrapper.setMinimumSize(new Dimension(100, 30));
			medalImageWrapper.setMaximumSize(new Dimension(100, 30));
			medalImageWrapper.setPreferredSize(new Dimension(100, 30));
		}
	}
	
	
	
	private void deleteMedal()
	{
		int medalID = medalTable.getSelectedRow();
		if (medalID >= 0)
		{
			medalArray.remove(medalID);
			((DefaultTableModel)medalTable.getModel()).removeRow(medalID);
			editingMedal = false;
		}
	}
	
	private void updateMedal()
	{
		// saving an existing medal (ie, changing values)
		if (!editingMedal)
			return;
		
		int selectedRow = medalTable.getSelectedRow();
		Medal medal = medalArray.get(selectedRow);
		
		String name = medalNameField.getText();
		String abbreviation = medalAbbreviation.getText();
		String desc = medalDescription.getText();
		String score = scoreField.getText();
		String multiple = "";
		if (multipleCheckbox.isSelected())
			multiple += "Y";
		else
			multiple += "N";
		
		medal.setName(name);
		medal.setAbbreviation(abbreviation);
		medal.setDescription(desc);
		medal.setScore(score);
		medal.setMultiple(multiple);
		
		DefaultTableModel model = (DefaultTableModel)medalTable.getModel();
		model.setValueAt(name, selectedRow, 1);
		model.setValueAt(abbreviation, selectedRow, 3);
		model.setValueAt(desc, selectedRow, 2);
		model.setValueAt(score, selectedRow, 4);
		model.setValueAt(multiple, selectedRow, 5);
	}
	
	private void saveMedal()
	{
		if (!editingMedal)
			return;
		String name = medalNameField.getText();
		String abbreviation = medalAbbreviation.getText();
		String desc = medalDescription.getText();
		String score = scoreField.getText();
		String multiple = "";
		if (multipleCheckbox.isSelected())
			multiple += "Y";
		else
			multiple += "N";
		String file = currentMedalFile;
		
		Medal newMedal = new Medal(name, abbreviation, score, multiple, desc, file, new ArrayList<Integer>());
		medalArray.add(newMedal);
		currentMedal = newMedal;
		
		updateMedalTable(newMedal);
		
		
	}
	
	private void addMedalCondition()
	{
		String condition = (String)conditionSelector.getSelectedItem();
		int conditionID = conditionArray.indexOf(condition) + 1;
		
		Object[] dataRow = {new Integer(conditionID), condition};
		DefaultTableModel model = (DefaultTableModel) conditionTable.getModel();
		int numRows = model.getRowCount();
		for (int i = 0; i< numRows; i++)
		{
			String rowValue = (String)conditionTable.getValueAt(i, 1);
			if (rowValue.equals(condition))
			{
				return;
			}
		}
		model.addRow(dataRow);
		conditionTable.setModel(model);
		
		currentMedal.addCondition(conditionID);
		
		
	}
	
	private void deleteMedalCondition()
	{
		int conditionSelected = conditionTable.getSelectedRow();
		if (conditionSelected >= 0)
		{
			int condID = (int)conditionTable.getValueAt(conditionSelected, 0);
			currentMedal.removeCondition(condID);
			((DefaultTableModel)conditionTable.getModel()).removeRow(conditionSelected);
			
		}
	}
	
	private void newMedalSet()
	{
		newSetup();
	}
	
	private void saveMedalSet()
	{
		if (medalTable.getRowCount() == 0)
			return;
		
		FileDialog dialog = new FileDialog(frmAuroraMedalCreator, "Create a Medal Set", FileDialog.SAVE);
		dialog.setDirectory(Config.defaultSaveDirectory);
		dialog.setVisible(true);
		String directory = dialog.getDirectory();
		String fileName = dialog.getFile();
		
		fileName = validateSaveFileName(fileName);
		
		if (fileName != null)
		{
			MedalWriter.writeToFile(medalArray, directory+fileName);
		}
		else
		{
			String message = "The medal set file must be \"csv\". If you do not specify a file extension the program will automatically write a \"csv\" file.";
			JOptionPane.showMessageDialog(frmAuroraMedalCreator, message, "Error" , JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private void openMedalSet()
	{		
		int choice = JOptionPane.showConfirmDialog(frmAuroraMedalCreator, "This will delete the current medal set if it has not been saved. Proceed?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		
		if (choice != 0)
			return;
		
		FileDialog dialog = new FileDialog(frmAuroraMedalCreator, "Open a Medal Set", FileDialog.LOAD);
		dialog.setDirectory(Config.defaultSaveDirectory);
		dialog.setVisible(true);
		String directory = dialog.getDirectory();
		String fileName = dialog.getFile();
		
		fileName = validateSaveFileName(fileName);
		
		if (fileName != null)
		{
			medalArray = MedalReader.readFromFile(directory + fileName);
			((DefaultTableModel)medalTable.getModel()).setRowCount(0);
			showAllMedals();
		}
		else
		{
			String message = "The medal set file must be \"csv\".";
			JOptionPane.showMessageDialog(frmAuroraMedalCreator, message, "Error" , JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	
	private void showAllMedals()
	{
		for (Medal m : medalArray)
		{
			updateMedalTable(m, new ImageIcon(Config.defaultImageDirectory + m.getImgName()));
		}
	}
	
	private boolean validateImageFileName(String fileName)
	{	
				
		if (fileName.trim().length() < 1)
			return false;
				
		String[] split = fileName.trim().split("\\.");
		
		if (split.length < 2)
			return false;
		
		String fileType = split[1];
		
		String[] validTypes = Config.validImageTypes;
		
		for (String type : validTypes)
		{			
			if (type.equalsIgnoreCase(fileType))
				return true;
		}
		return false;
	}
	
	private String validateSaveFileName(String fileName)
	{
		if ((fileName == null) || (fileName.trim().length() < 1))
		{
			return null;
		}
		
		String[] split = fileName.trim().split("\\.");
		if (split.length < 2)
			return fileName + ".csv";
		
		String fileType = split[1];
		
		if (fileType.equalsIgnoreCase("csv"))
			return fileName;
		else
			return null;
	}
	
	private void openSettings()
	{
		java.awt.EventQueue.invokeLater(new Runnable() {
	        public void run() {
	        	SettingsWindow.getSettingsWindow().setVisible(true);
	        }
	    });
	}
}
