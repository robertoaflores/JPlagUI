package edu.cnu.cs.jplagui.swing;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

@SuppressWarnings("serial")
public class JPlagUI extends JFrame {
	private static final String[] LANGUAGES = { "java17", "java15", "java15dm", "java12", "java11", "c/c++", "c#-1.2", "char", "text", "scheme" };

	private final JTextField        jtfFolder;
	private final JCheckBox         recursive;
	private final JCheckBox         display;
	private final JButton           run;
	private final JComboBox<String> language;
	private final JButton           quit;
	
	private final static Dimension WINDOW          = new Dimension( 600, 230 );
	private final static Rectangle FOLDER_LABEL    = new Rectangle(  25,  24, 100,  16 );
	private final static Rectangle FOLDER_FIELD    = new Rectangle( 130,  18, 350,  28 );
	private final static Rectangle BROWSE_BUTTON   = new Rectangle( 480,  19, 100,  29 );
	private final static Rectangle LANGUAGE_LABEL  = new Rectangle(  25,  52, 100,  16 );
	private final static Rectangle LANGUAGE_COMBO  = new Rectangle( 130,  36, 200,  50 );
	private final static Rectangle RECURSIVE_CHECK = new Rectangle( 130,  80, 350,  23 );
	private final static Rectangle DISPLAY_CHECK   = new Rectangle( 130, 100, 350,  23 );
	private final static Rectangle RUN_BUTTON      = new Rectangle( 250, 135, 100,  29 );
	private final static Rectangle QUIT_BUTTON     = new Rectangle( 480, 135, 100,  29 );

	public JPlagUI() {
		super( "JPlag: Interface Window" );
		
		setSize  ( WINDOW );
		setLayout( null );
		
		JLabel jblFolder = new JLabel("Code directory");
		       jblFolder.setBounds( FOLDER_LABEL );
		add(   jblFolder );
		
		jtfFolder = new JTextField();
		jtfFolder.setBounds   ( FOLDER_FIELD );
		jtfFolder.setEditable ( false );
		jtfFolder.setCaret( new DefaultCaret() {
			@Override
			public void focusGained(FocusEvent e) {
				super.focusGained(e);
				setVisible(true);
			}
		});
		add(jtfFolder);
				
		JButton browse = new JButton( "Browse" );
		browse.setBounds( BROWSE_BUTTON );
		add(browse);
		browse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				processBrowse();
			}
		});
		
		JLabel lblLanguage = new JLabel( "Language" );
		lblLanguage.setBounds( LANGUAGE_LABEL );
		add( lblLanguage );
		
		language = new JComboBox<>( LANGUAGES );
		language.setBounds( LANGUAGE_COMBO );
		add( language );
		
		recursive = new JCheckBox( "Recurse sub-directories" );
		recursive.setSelected( true );
		recursive.setBounds  ( RECURSIVE_CHECK );
		add( recursive );

		display = new JCheckBox( "Show results in web browser" );
		display.setSelected( true );
		display.setBounds  ( DISPLAY_CHECK );
		add( display );

		run = new JButton( "Run JPlag" );
		run.setBounds( RUN_BUTTON );
		add( run );
		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				processRun();
			}
		});

		quit = new JButton( "Quit" );
		quit.setBounds( QUIT_BUTTON );
		add( quit );
		quit.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JMenuBar  menu    = new JMenuBar();
		JMenu     program = new JMenu( "Program" );
		JMenuItem about   = program.add( "About..." );
		program.addSeparator();
		JMenuItem quit    = program.add( "Quit" ); 
		menu.add   ( program );
		setJMenuBar( menu );
		
		about.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog( JPlagUI.this, new JLabel( "<html><hr><b>JPlagWindow v0.1</b><br>Roberto A. Flores (roberto.flores@cnu.edu)<br><br><b>JPlag v2.11.8</b><br>Mathias Landhäußer's (mathias.landhaeusser@kit.edu)<br>https://github.com/jplag/<hr></html>"), "About JPlagWindow", JOptionPane.INFORMATION_MESSAGE );
			}
		});
		quit.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPlagUI.this.dispose();
			}
		});
		
		
		browse.requestFocus();
		run   .setEnabled( false );

		setResizable            ( false );
		setLocationRelativeTo   ( null );
		setDefaultCloseOperation( DISPOSE_ON_CLOSE );
	}
	
	private void processBrowse() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle            ( "JPlag: Folder with files");
		fc.setFileSelectionMode      ( JFileChooser.DIRECTORIES_ONLY );
		fc.setAcceptAllFileFilterUsed( false );
		
		String current = jtfFolder.getText();
		if (!current.isEmpty()) {
			Path path   = Paths.get( current );
			Path parent = path.getParent();
			fc.setCurrentDirectory( parent.toFile() );
		}
		int result = fc.showOpenDialog( JPlagUI.this );

		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			jtfFolder.setText( file.getAbsolutePath() );
			run.setEnabled  ( true );
			run.requestFocus();
		}
	}
	private void delete(File f) {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				delete( c );
		}
		if (f.exists() && !f.delete()) {
			throw new RuntimeException( "Failed to delete file: " + f );
		}
	}
	public void processRun() {
		List<String> list   = new ArrayList<String>();
		boolean      subdir = recursive.isSelected();
		String       parse  = LANGUAGES[ language.getSelectedIndex() ];
		String       folder = jtfFolder.getText();
		String       result = folder + "/jplag";
		
		// remove results folder (if existing)
		Path toDelete = Paths.get( result );
		delete( toDelete.toFile() );
		
		// recursing sub-directories
		if (subdir) { 
			list.add( "-s" );
		}
		// parsing language
		list.add( "-l" );
		list.add( parse );

		// results written to "./jplag"
		list.add( "-r" );
		list.add( result );

		// code location
		list.add( folder );

		String[] array = new String[ list.size() ];
		for (int i = 0; i < array.length; i++) {
			array[ i ] = list.get( i );
		}
		
		// capture console
		PrintStream           console = System.out;
		ByteArrayOutputStream out     = new ByteArrayOutputStream();
		System.setOut( new PrintStream( out ));
		
		// invoke JPlag
		jplag.JPlag.main( array );

		// display results
		System.setOut( console  );
		String output = out.toString();

		// create a JTextArea & ScrollPane
		JTextArea   textArea   = new JTextArea( 20, 50 );
		JScrollPane scrollPane = new JScrollPane( textArea );
		textArea.setText         ( output );
		textArea.setEditable     ( false );
		textArea.setCaretPosition( 0 );
		
		// display them in a message dialog
		JOptionPane.showMessageDialog( this, scrollPane, "JPlag: Output", JOptionPane.INFORMATION_MESSAGE );		
		// display results in browser

		boolean inBrowser = display.isSelected();
		if     (inBrowser) {
			try {
				Path html = Paths.get( result+"/index.html" );
				if (html.toFile().exists()) {
					java.awt.Desktop.getDesktop().browse( html.toUri() );
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		quit.requestFocus();
	}

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new JPlagUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
