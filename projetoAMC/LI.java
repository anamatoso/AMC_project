package projetoAMC;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class LI {


	private JFrame frame;
	private JTextField textField_1;
	private JTextField textField_3;
	private JTextField textField_2;
	private Amostrad a;
	private int npais;
	private int maxgrafos;
	private BN b;
	private JTextArea textField_4;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LI window = new LI();
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
	public LI() {
		initialize();
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		}
		catch(Exception e) {
			return false;

		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 767, 563);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JFileChooser fc = new JFileChooser();
		try {
			File path = new File(new File(".").getCanonicalPath());
			fc.setCurrentDirectory(path);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}



		JLabel lblNewLabel = new JLabel("Learning Interface");
		lblNewLabel.setForeground(new Color(0, 0, 205));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Century Gothic", Font.BOLD, 28));
		lblNewLabel.setBounds(474, 11, 277, 72);
		frame.getContentPane().add(lblNewLabel);



		textField_3 = new JTextField(); //ficheiro adicionado
		textField_3.setHorizontalAlignment(SwingConstants.CENTER);
		textField_3.setBounds(175, 124, 376, 26);
		frame.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		textField_3.setFont(new Font("Century Gothic", Font.PLAIN, 12));
		textField_3.setVisible(false);


		JLabel IST_logo1 = new JLabel("",new ImageIcon(IM.class.getResource("/projetoAMC/IST_logo1.png")),JLabel.CENTER);
		IST_logo1.setBounds(24, 446, 173, 65);
		frame.getContentPane().add(IST_logo1);

		JButton aprende = new JButton("LEARN");
		aprende.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		aprende.setBounds(213, 335, 115, 29);
		frame.getContentPane().add(aprende);
		aprende.setVisible(false);




		JButton pasta = new JButton(new ImageIcon(IM.class.getResource("/projetoAMC/pasta.png")));
		pasta.setBounds(62, 71, 88, 91);
		frame.getContentPane().add(pasta);
		pasta.setOpaque(false);
		pasta.setContentAreaFilled(false);
		pasta.setBorderPainted(false);
		pasta.addActionListener( new ActionListener() {
			public void actionPerformed (ActionEvent e) {	    		 
				if( fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ) {
					java.io.File file = fc.getSelectedFile();
					try {
						a= new Amostrad (file.getName());
						textField_3.setText("you have selected: " + file.getName());
						textField_3.setVisible(true);
						textField_3.setEditable(false);
						aprende.setVisible(true);	  
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}}
		}
				);




		JLabel lblNewLabel_1 = new JLabel("Please, choose the .csv file you want to learn from.");
		lblNewLabel_1.setBounds(165, 83, 499, 31);
		frame.getContentPane().add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Century Gothic", Font.PLAIN, 16));


		JLabel lblNewLabel_1_1 = new JLabel("Write the maximum number of parents of each node:");
		lblNewLabel_1_1.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		lblNewLabel_1_1.setBounds(72, 178, 421, 31);
		frame.getContentPane().add(lblNewLabel_1_1);

		JLabel lblNewLabel_2 = new JLabel("Write the number of inicial random graphs:");
		lblNewLabel_2.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(72, 250, 412, 31);
		frame.getContentPane().add(lblNewLabel_2);

		textField_1 = new JTextField(); //numero maximo de pais 
		textField_1.setFont(new Font("Century Gothic", Font.PLAIN, 12));
		textField_1.setBounds(521, 181, 146, 26);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		//converter string para inteiros 


		textField_2 = new JTextField(); // numero aleatório de grafos inicial
		textField_2.setFont(new Font("Century Gothic", Font.PLAIN, 12));
		textField_2.setBounds(521, 253, 146, 26);
		frame.getContentPane().add(textField_2);


		textField_4 = new JTextArea();
		textField_4.setFont(new Font("Century Gothic", Font.PLAIN, 12));
		textField_4.setBounds(129, 110, 478, 39);
		frame.getContentPane().add(textField_4);
		textField_4.setColumns(10);
		textField_4.setVisible(false);
		textField_4.setEditable(false);
		textField_4.setText("  The learning process has finished." + "\n" + "  Please decide whether you want to save the learned Bayes Network or not.");

		JButton Guardar = new JButton(new ImageIcon(IM.class.getResource("/projetoAMC/disquete_icon_azul.png")));
		Guardar.setBounds(200, 210, 130, 90);
		frame.getContentPane().add(Guardar);
		Guardar.setVisible(false);
		Guardar.setOpaque(false);
		Guardar.setContentAreaFilled(false);
		Guardar.setBorderPainted(false);


		JButton Lixo = new JButton(new ImageIcon(IM.class.getResource("/projetoAMC/lixo_icon_azul.png")));
		Lixo.setBounds(400 ,210 , 130, 90);
		frame.getContentPane().add(Lixo);
		Lixo.setVisible(false);
		Lixo.setOpaque(false);
		Lixo.setContentAreaFilled(false);
		Lixo.setBorderPainted(false);

		JLabel salvar = new JLabel("Save");
		salvar.setBounds(230,297, 69, 20);
		frame.getContentPane().add(salvar);
		salvar.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		salvar.setVisible(false);
		salvar.setHorizontalAlignment(SwingConstants.CENTER);	     

		JLabel cancelar = new JLabel("Cancel");
		cancelar.setBounds(430 ,297, 100, 20);
		frame.getContentPane().add(cancelar);
		cancelar.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		cancelar.setVisible(false); 

		aprende.addActionListener( new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				if (isInteger(textField_1.getText()))  {
					try {
						if (Integer.parseInt(textField_1.getText())>=0 && Integer.parseInt(textField_1.getText())<a.element(0).length) {
							npais=Integer.parseInt(textField_1.getText());
							if (isInteger(textField_2.getText()))  {
								try {
									if (Integer.parseInt(textField_2.getText())>=0) {
										textField_1.setVisible(false);
										textField_2.setVisible(false);
										textField_3.setVisible(false);
										lblNewLabel_2.setVisible(false);
										lblNewLabel_1.setVisible(false);
										lblNewLabel_1_1.setVisible(false);
										pasta.setVisible(false);
										aprende.setVisible(false);
										maxgrafos=Integer.parseInt(textField_2.getText());
										b =GrafoOti.aprende(a,npais,maxgrafos);
										Guardar.setVisible(true);
										Lixo.setVisible(true);
										salvar.setVisible(true);
										cancelar.setVisible(true);
										textField_4.setVisible(true);
									}
									else textField_2.setText("ERROR: must be an integer greater than or equal to 0 ");
								} catch (Exception e1) {e1.printStackTrace();}
							}
							else textField_2.setText("ERROR: must be an integer");
						}
						else textField_1.setText("ERROR: must be an integer between 0 and the number of variables of the sample");
					} catch (Exception e1) {e1.printStackTrace();}
				}
				else textField_1.setText("ERROR: must be an integer");
			}

		});

		Guardar.addActionListener( new ActionListener() { 
			public void actionPerformed (ActionEvent e) {
				// parent component of the dialog
				JFrame parentFrame = new JFrame();

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");
				//	    		  fileChooser.showSaveDialog(null);


				int userSelection = fileChooser.showSaveDialog(parentFrame);

				if (userSelection == JFileChooser.APPROVE_OPTION) {
					FileOutputStream f;
					String filename=fileChooser.getSelectedFile().getName();	
					//	    			  getSelectedFile().getAbsolutePath();
					//	    			  String path=fileChooser.getCurrentDirectory();
					File file=new File(fileChooser.getCurrentDirectory(), filename);

					try {
						f = new FileOutputStream(file);
						ObjectOutputStream o= new ObjectOutputStream(f);
						o.writeObject(b);
						o.close();
						f.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					File fileToSave = file;
					fileToSave.renameTo(fileToSave);
					textField_4.setText("  The file was saved with the following path: " + "\n" + "  "+ fileToSave.getAbsolutePath());
				}

			}
		}
				);

		Lixo.addActionListener( new ActionListener() { 
			public void actionPerformed (ActionEvent e) {
				textField_1.setVisible(true);
				textField_2.setVisible(true);
				lblNewLabel_2.setVisible(true);
				lblNewLabel_1.setVisible(true);
				lblNewLabel_1_1.setVisible(true);
				pasta.setVisible(true);
				aprende.setVisible(false);
				Guardar.setVisible(false);
				Lixo.setVisible(false);
				salvar.setVisible(false);
				cancelar.setVisible(false);
				textField_4.setVisible(false);
				aprende.setVisible(true);
				textField_1.setText("");
				textField_2.setText("");
				textField_4.setText(" The learning process has finished." + "\n" + " Please decide whether you want to save the learned Bayes Network or not.");
			}
		}
				);


		JLabel background = new JLabel ("", new ImageIcon(IM.class.getResource("/projetoAMC/unknown.png")),JLabel.CENTER);
		background.setBounds(0,0,767,563);
		frame.getContentPane().add(background);



	}

}  
