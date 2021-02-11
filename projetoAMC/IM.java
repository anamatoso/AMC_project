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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.ButtonGroup;
import javax.swing.JButton;

public class IM {

	private JFrame frame;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private BN b;



	public static BN read(String filename) throws IOException, ClassNotFoundException { //ler os ficheiros
		FileInputStream f = new FileInputStream(new File(filename).getCanonicalPath());
		ObjectInputStream o = new ObjectInputStream(f);
		BN b = (BN) o.readObject();
		o.close();
		f.close();
		return b;
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

	public double probcancro(int[] param) throws Exception {//dá a P de TER cancro sabendo os parametros
		int[] paramc=new int[param.length+1];
		paramc[param.length]=1;//tem cancro
		int[] paramnotc=new int[param.length+1];
		paramnotc[param.length]=0;//nao ter cancro
		for(int i=0; i<param.length; i++) {
			paramc[i]=param[i];
			paramnotc[i]=param[i];
		}
		return  b.prob(paramc)/(b.prob(paramc)+b.prob(paramnotc));
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IM window = new IM();
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
	public IM() {
		initialize();
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

		JButton seta1 = new JButton(new ImageIcon(IM.class.getResource("Seta2.png")));
		seta1.setBounds(659, 304, 71, 52);
		seta1.setOpaque(false);
		seta1.setContentAreaFilled(false);
		seta1.setBorderPainted(false);
		frame.getContentPane().add(seta1);
		seta1.setVisible(false);

		JButton seta2 = new JButton(new ImageIcon(IM.class.getResource("Seta2.png")));
		seta2.setBounds(659, 304, 71, 52);
		frame.getContentPane().add(seta2);
		seta2.setOpaque(false);
		seta2.setContentAreaFilled(false);
		seta2.setBorderPainted(false);
		seta2.setVisible(false);

		JLabel lblNewLabel = new JLabel("Medical Interface");
		lblNewLabel.setForeground(new Color(0, 0, 205));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Century Gothic", Font.BOLD, 28));
		lblNewLabel.setBounds(474, 11, 277, 72);
		frame.getContentPane().add(lblNewLabel);

		JRadioButton BC = new JRadioButton("Breast Cancer");
		buttonGroup.add(BC);
		BC.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		BC.setBounds(106, 144, 129, 23);
		BC.setOpaque(false);
		frame.getContentPane().add(BC);
		BC.addActionListener( new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				seta1.setVisible(true);	  
			}
		}
				);

		JRadioButton TC = new JRadioButton("Thyroid Cancer");
		buttonGroup.add(TC);
		TC.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		TC.setBounds(106, 183, 151, 23);
		TC.setOpaque(false);
		frame.getContentPane().add(TC);
		TC.addActionListener( new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				seta1.setVisible(true);	  
			}
		}
				);

		JRadioButton D = new JRadioButton("Diabetes");
		buttonGroup.add(D);
		D.setOpaque(false);
		D.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		D.setBounds(106, 223, 129, 23);
		frame.getContentPane().add(D);
		D.addActionListener( new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				seta1.setVisible(true);	  
			}
		}
				);


		JRadioButton H = new JRadioButton("Hepatitis");
		buttonGroup.add(H);
		H.setOpaque(false);
		H.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		H.setBounds(106, 263, 129, 23);
		frame.getContentPane().add(H);
		H.addActionListener( new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				seta1.setVisible(true);	  
			}
		}
				);

		JLabel lblNewLabel_1 = new JLabel("Select the disease:");
		lblNewLabel_1.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(106, 104, 181, 33);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel IST_logo1 = new JLabel("",new ImageIcon(IM.class.getResource("IST_logo1.png")),JLabel.CENTER);
		IST_logo1.setBounds(24, 446, 173, 65);
		frame.getContentPane().add(IST_logo1);

		JTextField parametros = new JTextField();
		parametros.setHorizontalAlignment(SwingConstants.LEFT);
		parametros.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		parametros.setBounds(49, 183, 668, 33);
		frame.getContentPane().add(parametros);
		parametros.setColumns(10);	  
		parametros.setVisible(false);

		JLabel lblNewLabel_2 = new JLabel("Please, write the patient's measures separated with a comma (no spaces in between):");
		lblNewLabel_2.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(39, 128, 691, 52);
		frame.getContentPane().add(lblNewLabel_2);
		lblNewLabel_2.setVisible(false);

		JButton tryagain = new JButton("Try again");
		tryagain.setFont(new Font("Century Gothic", Font.PLAIN, 12));
		tryagain.setBounds(322, 344, 117, 29);
		frame.getContentPane().add(tryagain);
		tryagain.setVisible(false);


		seta1.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				if (BC.isSelected()) { //verificar a que botao corresponde a BN aprendida -> guardar c estes nomes
					try {
						b=read("bcancer");
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
				}
				else if (TC.isSelected()) {
					try {
						b=read("thyroid");
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
				}
				else if (D.isSelected()) {
					try {
						b=read("diabetes");
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
				}
				else if (H.isSelected()) {
					try {
						b=read("hepatitis");
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
				}
				BC.setVisible(false);
				TC.setVisible(false);
				D.setVisible(false);
				H.setVisible(false);
				lblNewLabel_1.setVisible(false);
				seta1.setVisible(false);
				parametros.setVisible(true);
				lblNewLabel_2.setVisible(true);
				seta2.setVisible(true);

			}
		});
		JLabel resultado = new JLabel(new ImageIcon(IM.class.getResource("/projetoAMC/Medical-62-512.png")));
		//      resultado.setFont(new Font("Dialog", Font.PLAIN, 16));
		resultado.setBounds(50, 72, 91, 92);
		frame.getContentPane().add(resultado);
		resultado.setVisible(false);

		JLabel results = new JLabel("Results: ");
		results.setForeground(new Color(0, 139, 139));
		results.setFont(new Font("Century Gothic", Font.BOLD, 22));
		results.setBounds(136, 87, 91, 63);
		frame.getContentPane().add(results);
		results.setVisible(false);

		JTextArea probabilidade = new JTextArea();
		probabilidade.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		probabilidade.setBounds(50, 179, 667, 58);
		frame.getContentPane().add(probabilidade);
		probabilidade.setColumns(10);	  
		probabilidade.setVisible(false);
		probabilidade.setEditable(false);

		seta2.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				try {
					String line = parametros.getText();
					String virgula = ",";
					String[] arraystring = line.split(virgula);
					int vet[] = new int [arraystring.length];
					for (int i = 0; i < vet.length; i++) vet[i]=Integer.parseInt(arraystring[i]);
					try {
						float p=(float) (probcancro(vet)*100);
						resultado.setVisible(true);
						results.setVisible(true);
						probabilidade.setVisible(true);
						parametros.setVisible(false);
						parametros.setVisible(false);
						seta2.setVisible(false);
						lblNewLabel_2.setVisible(false);
						tryagain.setVisible(true);
						if (p>=50) {
							probabilidade.setText(" The diagnosis dictates that the patient has cancer with a probability of "+p+"%. \n \n"
									+ " Be aware that if the probability is near 50% there is a chance that this algorithm might fail.");
						}
						else if (p<50) {
							probabilidade.setText(" The diagnosis dictates that the patient does not have cancer with a probability of "+(100-p)+"%. \n \n"
									+ " Be aware that if the probability is near 50% there is a chance that this algorithm might fail.");
						}

					} catch (Exception e1) {
						parametros.setText(" Write a vector with an apropriate set of parameters for the sample you have chosen");

					}
				} catch (Exception e2) {
					parametros.setText(" Write only numbers.");
				}
			}
		});

		tryagain.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				resultado.setVisible(false);
				results.setVisible(false);
				probabilidade.setVisible(false);
				parametros.setVisible(false);
				seta2.setVisible(false);
				lblNewLabel_2.setVisible(false);
				seta1.setVisible(true);
				BC.setVisible(true);
				TC.setVisible(true);
				D.setVisible(true);
				H.setVisible(true);
				lblNewLabel_1.setVisible(true);
				tryagain.setVisible(false);
				parametros.setText("");
			}
		});

		JLabel background = new JLabel ("",new ImageIcon(IM.class.getResource("unknown.png")),JLabel.CENTER);
		background.setBounds(0,0,767,534);
		frame.getContentPane().add(background);




	}
}