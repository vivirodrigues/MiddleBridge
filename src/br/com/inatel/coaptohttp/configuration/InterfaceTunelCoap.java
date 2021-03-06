package br.com.inatel.coaptohttp.configuration;
import javax.swing.table.DefaultTableModel;
import org.apache.http.client.methods.HttpPost;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.SocketException;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import br.com.inatel.coaptohttp.coap.CoAPConnection;
import br.com.inatel.coaptohttp.coap.DataCoAP;
import br.com.inatel.configuration.Message;
import br.com.inatel.http.DataHTTP;
import br.com.inatel.http.Headers;
import br.com.inatel.http.HttpConnection;

import javax.swing.JCheckBox;


@SuppressWarnings("serial")
public class InterfaceTunelCoap extends JFrame {	
	
	private JPanel contentPane;
	private JTextField txt_IPCoAP;
	private JTextField txt_PortCoAP;
	private JTextField txt_PathCoAP;
	private JTextField txt_PathHttp;
	private JTextField txt_PortHTTP;
	private JTextField txt_ServerHttp;
	private JTextField txt_PreviewHTTP;
	private JTextField txt_PreviewCoAP;
	private JTable tableHeaders;
	private JTable tableHeaders1;
	private JTextField txt_VariableHeader;
	private JTextField txt_ValueHeader;
	private HttpConnection httpConnection = new HttpConnection();
	private JTextField txtVariableModel;
	public DataHTTP dataHttp = new DataHTTP();
	public DataCoAP dataCoAP = new DataCoAP();
	

	Message message = new Message();
	public String model;
	String path;
	String port;
	String IP;
	public int numHeaders;
	private boolean validation;	
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfaceTunelCoap frame = new InterfaceTunelCoap();
					//frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
		});
	}
	public InterfaceTunelCoap() throws IOException {
		
		initialize();
	}	

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public void initialize() throws IOException {						
		setResizable(false);
		Color myColor = new Color(240,240,240);
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 735, 662);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		/******************************** PANEL 1  *********************************/
		
		/* HTTP */
		JPanel panel = new JPanel();
		panel.setBackground(UIManager.getColor("InternalFrame.inactiveTitleBackground"));
		panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel.setBounds(368, 11, 348, 562);
		contentPane.add(panel);
		panel.setLayout(null);
		panel.setBackground(myColor);
				
		JLabel lblDadosHttp = new JLabel("HTTP");
		lblDadosHttp.setBounds(126, 7, 102, 41);
		lblDadosHttp.setForeground(Color.BLACK);
		lblDadosHttp.setFont(new Font("Times New Roman", Font.BOLD, 35));
		panel.add(lblDadosHttp);
		
		JLabel lblPathDaRequisio = new JLabel("Path: *");
		lblPathDaRequisio.setHorizontalAlignment(SwingConstants.CENTER);
		lblPathDaRequisio.setForeground(Color.BLACK);
		lblPathDaRequisio.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblPathDaRequisio.setBounds(40, 129, 69, 24);
		panel.add(lblPathDaRequisio);
		
		JLabel lblServerIp = new JLabel("Address: *");
		lblServerIp.setHorizontalAlignment(SwingConstants.CENTER);
		lblServerIp.setForeground(Color.BLACK);
		lblServerIp.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblServerIp.setBounds(40, 59, 94, 24);
		panel.add(lblServerIp);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setHorizontalAlignment(SwingConstants.CENTER);
		lblPort.setForeground(Color.BLACK);
		lblPort.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblPort.setBounds(40, 94, 51, 24);
		panel.add(lblPort);
		
		/* Path */
		txt_PathHttp = new JTextField();	
		txt_PathHttp.setText("data");
		txt_PathHttp.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		txt_PathHttp.setColumns(10);
		txt_PathHttp.setBounds(143, 134, 162, 20);
		panel.add(txt_PathHttp);
		
		/* Port */
		txt_PortHTTP = new JTextField();		
		txt_PortHTTP.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		txt_PortHTTP.setColumns(10);
		txt_PortHTTP.setBounds(143, 99, 162, 20);
		panel.add(txt_PortHTTP);
		
		/* Server IP */
		txt_ServerHttp = new JTextField();	
		txt_ServerHttp.setText("https://api.tago.io");
		txt_ServerHttp.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		txt_ServerHttp.setColumns(10);
		txt_ServerHttp.setBounds(143, 64, 162, 20);
		panel.add(txt_ServerHttp);
		
		
		JLabel lblPreviewDaRequisio = new JLabel("Request preview");
		lblPreviewDaRequisio.setHorizontalAlignment(SwingConstants.CENTER);
		lblPreviewDaRequisio.setForeground(Color.BLACK);
		lblPreviewDaRequisio.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblPreviewDaRequisio.setBounds(75, 164, 198, 24);
		panel.add(lblPreviewDaRequisio);
				
		txt_PreviewHTTP = new JTextField();
		txt_PreviewHTTP.setEditable(false);
		txt_PreviewHTTP.setForeground(Color.BLACK);
		txt_PreviewHTTP.setHorizontalAlignment(SwingConstants.CENTER);
		txt_PreviewHTTP.setText("IP:PORT/PATH");		
		txt_PreviewHTTP.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		txt_PreviewHTTP.setBounds(10, 189, 328, 20);
		panel.add(txt_PreviewHTTP);
		txt_PreviewHTTP.setColumns(10);
		
		/* CoAP */
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(UIManager.getColor("InternalFrame.inactiveTitleBackground"));
		panel_1.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_1.setBounds(10, 11, 348, 216);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		panel_1.setBackground(myColor);		
		
		JLabel lblNewLabel = new JLabel("CoAP");
		lblNewLabel.setBounds(114, 7, 108, 41);
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 35));
		panel_1.add(lblNewLabel);
		
		JLabel lblIp = new JLabel("Address: *");
		lblIp.setHorizontalAlignment(SwingConstants.CENTER);
		lblIp.setForeground(Color.BLACK);
		lblIp.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblIp.setBounds(40, 59, 91, 24);
		panel_1.add(lblIp);
		
		JLabel lblNewLabel_1 = new JLabel("Port: *");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setForeground(Color.BLACK);
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_1.setBounds(40, 94, 59, 24);
		panel_1.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Path: *");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setForeground(Color.BLACK);
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_2.setBounds(40, 129, 69, 24);
		panel_1.add(lblNewLabel_2); 
		
		/* IP */
		txt_IPCoAP = new JTextField();
		txt_IPCoAP.setText("coap://localhost");
		txt_IPCoAP.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		txt_IPCoAP.setBounds(143, 64, 162, 20);
		panel_1.add(txt_IPCoAP);
		txt_IPCoAP.setColumns(10);
		
		/* Port */
		txt_PortCoAP = new JTextField();
		txt_PortCoAP.setText("5683");
		txt_PortCoAP.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		txt_PortCoAP.setBounds(143, 99, 162, 20);
		panel_1.add(txt_PortCoAP);
		txt_PortCoAP.setColumns(10);
		
		/* Path */
		txt_PathCoAP = new JTextField();
		txt_PathCoAP.setText("hello");
		txt_PathCoAP.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		txt_PathCoAP.setBounds(143, 134, 162, 20);
		panel_1.add(txt_PathCoAP);
		txt_PathCoAP.setColumns(10);
		
		JLabel lblPreviewCoAP = new JLabel("Request preview");
		lblPreviewCoAP.setBounds(75, 164, 198, 24);
		panel_1.add(lblPreviewCoAP);
		lblPreviewCoAP.setHorizontalAlignment(SwingConstants.CENTER);
		lblPreviewCoAP.setForeground(Color.BLACK);
		lblPreviewCoAP.setFont(new Font("Times New Roman", Font.BOLD, 20));
		
		txt_PreviewCoAP = new JTextField();
		txt_PreviewCoAP.setBounds(10, 189, 328, 20);
		panel_1.add(txt_PreviewCoAP);
		txt_PreviewCoAP.setText("IP:PORT/PATH");
		txt_PreviewCoAP.setHorizontalAlignment(SwingConstants.CENTER);
		txt_PreviewCoAP.setForeground(Color.BLACK);
		txt_PreviewCoAP.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		txt_PreviewCoAP.setEditable(false);
		txt_PreviewCoAP.setColumns(10);
		
		/******************************** PANEL 2  *********************************/
						
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_2.setBackground(myColor);
		panel_2.setBounds(10, 235, 348, 387);
		contentPane.add(panel_2);
		
		JLabel lblPayloadExample = new JLabel("Payload example *");
		lblPayloadExample.setForeground(Color.BLACK);
		lblPayloadExample.setFont(new Font("Times New Roman", Font.BOLD, 23));
		lblPayloadExample.setBounds(80, 50, 197, 41);
		panel_2.add(lblPayloadExample);
		
		final JEditorPane editorPane = new JEditorPane();
		editorPane.setBounds(10, 102, 328, 274);
		editorPane.setText("{ \n    \"variable\":\"x\",\n    \"value\":x, \n    \"unit\":\"x\" \n}");
		panel_2.add(editorPane);
		
		JLabel lblVariavel = new JLabel("Payload value to replace: *");
		lblVariavel.setBounds(10, 18, 218, 24);
		panel_2.add(lblVariavel);
		lblVariavel.setHorizontalAlignment(SwingConstants.CENTER);
		lblVariavel.setForeground(Color.BLACK);
		lblVariavel.setFont(new Font("Times New Roman", Font.BOLD, 18));
		
		txtVariableModel = new JTextField();
		txtVariableModel.setBounds(236, 22, 102, 20);
		panel_2.add(txtVariableModel);
		txtVariableModel.setText("x");
		txtVariableModel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		txtVariableModel.setColumns(10);
		model = editorPane.getText();						
		
		/******************************** FIRST TABLE  *********************************/		
						
		JScrollPane scrollPaneHeaders = new JScrollPane();
		scrollPaneHeaders.setBounds(10, 330, 328, 74);
		panel.add(scrollPaneHeaders);
		
		tableHeaders = new JTable();
		
		tableHeaders.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Header name", "Header value"
			}
		) {			
			boolean[] columnEditables = new boolean[] {
				false, false
			};
			public boolean isCellEditable(int row, int column) {				
				return columnEditables[column];				
			}
			
		});
				
		tableHeaders.getColumnModel().getColumn(0).setResizable(false);
		tableHeaders.getColumnModel().getColumn(1).setResizable(false);
		
		scrollPaneHeaders.setViewportView(tableHeaders);
		DefaultTableModel val = (DefaultTableModel) tableHeaders.getModel();
		
		/** Delete header of second table **/
		
		JButton btnDeleteHeader = new JButton("Delete Header");
		btnDeleteHeader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(tableHeaders.getSelectedRow()>-1) {
					((DefaultTableModel) tableHeaders.getModel()).removeRow(tableHeaders.getSelectedRow());
					tableHeaders.repaint();				
				}else {
					JOptionPane.showMessageDialog(null, "Select Header to delete");
				}
			}
		});
		btnDeleteHeader.setToolTipText("Select the Header in the table to delete");
		
		btnDeleteHeader.setForeground(Color.BLACK);
		btnDeleteHeader.setFont(new Font("Times New Roman", Font.BOLD, 13));
		btnDeleteHeader.setBounds(223, 406, 115, 23);
		panel.add(btnDeleteHeader);
					
		/******************************** SECOND TABLE  *********************************/
		
		JScrollPane scrollPaneHeaders1;
		scrollPaneHeaders1 = new JScrollPane();
		scrollPaneHeaders1.setBounds(10, 453, 328, 74);
		panel.add(scrollPaneHeaders1);
		
		tableHeaders1 = new JTable();
		
		tableHeaders1.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Header name", "Header value"
			}
		) {			
			boolean[] columnEditables = new boolean[] {
				false, false
			};
			public boolean isCellEditable(int row, int column) {				
				return columnEditables[column];				
			}
			
		});
				
		tableHeaders1.getColumnModel().getColumn(0).setResizable(false);
		tableHeaders1.getColumnModel().getColumn(1).setResizable(false);
		
		scrollPaneHeaders1.setViewportView(tableHeaders1);
		DefaultTableModel val1 = (DefaultTableModel) tableHeaders1.getModel();
		
		/** Delete header of second table **/
		JButton btnDeleteIncompleteHeaders = new JButton("Delete Header"); //table with incomplete headers
		
		btnDeleteIncompleteHeaders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(tableHeaders1.getSelectedRow()>-1) {
					((DefaultTableModel) tableHeaders1.getModel()).removeRow(tableHeaders1.getSelectedRow());
					tableHeaders1.repaint();				
				}else {
					JOptionPane.showMessageDialog(null, "Select Header to delete");
				}
			}
		});
		btnDeleteIncompleteHeaders.setToolTipText("Select the Header in the table to delete");
		
		btnDeleteIncompleteHeaders.setForeground(Color.BLACK);
		btnDeleteIncompleteHeaders.setFont(new Font("Times New Roman", Font.BOLD, 13));
		btnDeleteIncompleteHeaders.setBounds(223, 529, 115, 23);
		panel.add(btnDeleteIncompleteHeaders);
					
		/******************************** ADD HEADER  *********************************/
		
		//Inserting new row in table
		JButton btnAddHeader = new JButton("Add Header");
		btnAddHeader.setForeground(Color.BLACK);
				
		btnAddHeader.addActionListener(new ActionListener() {										
            
			public void actionPerformed(ActionEvent arg0) {
				Headers he = new Headers();
				// add Header value and Header variable
				he.setHeaders(txt_VariableHeader.getText().trim(), txt_ValueHeader.getText().trim());
				
				// if the value is not empty
				if(txt_ValueHeader.getText().length()<1) {
					httpConnection.list_headers_v.add(he);
					numHeaders +=1;
					val1.addRow(new String [] {he.getHeadersName(), he.getHeadersValue()});								
				}
				// if the value is empty
				else {
					val.addRow(new String [] {he.getHeadersName(), he.getHeadersValue()});					
				}				
				 												
				//clearing the fields
				txt_VariableHeader.setText("");
				txt_ValueHeader.setText("");
				txt_VariableHeader.requestFocus();
				//rowIndexHeader ++;
			}			
		});
						 						 						
		btnAddHeader.setFont(new Font("Times New Roman", Font.BOLD, 15));
		btnAddHeader.setBounds(93, 273, 156, 23);
		panel.add(btnAddHeader);			
																				
		/*********************** HEADERS ***********************************/
		
		Headers he = new Headers();
		he.setHeaders("content-type", "application/json");
		httpConnection.list_headers.add(he);
		val.addRow(new String [] {he.getHeadersName(), he.getHeadersValue()});
		//rowIndexHeader ++;
																				
		/* Name variable */
		txt_VariableHeader = new JTextField();
		txt_VariableHeader.setBackground(Color.WHITE);
		txt_VariableHeader.setText("Device-Token");
		txt_VariableHeader.setBounds(10, 245, 156, 20);
		panel.add(txt_VariableHeader);
		txt_VariableHeader.setColumns(10);
		
		/* Value variable */
		txt_ValueHeader = new JTextField();
		txt_ValueHeader.setText("75f27eff-a635-4b54-ad50-120fcfa51e7f");
		txt_ValueHeader.setToolTipText("#");
		txt_ValueHeader.setBounds(182, 245, 156, 20);
		panel.add(txt_ValueHeader);
		txt_ValueHeader.setColumns(10);
		
		JLabel lblNameHeader = new JLabel("Header name *");
		lblNameHeader.setForeground(Color.BLACK);
		lblNameHeader.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNameHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblNameHeader.setBounds(10, 220, 156, 24);
		panel.add(lblNameHeader);
		
		JLabel lblValueHeader = new JLabel("Header value");
		lblValueHeader.setForeground(Color.BLACK);
		lblValueHeader.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblValueHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblValueHeader.setBounds(182, 220, 156, 24);
		panel.add(lblValueHeader);
		
		JLabel lblCompleteHeaders = new JLabel("Complete Headers");
		lblCompleteHeaders.setHorizontalAlignment(SwingConstants.CENTER);
		lblCompleteHeaders.setForeground(Color.BLACK);
		lblCompleteHeaders.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblCompleteHeaders.setBounds(10, 305, 156, 24);
		panel.add(lblCompleteHeaders);
		
		JLabel lblIncompleteHeaders = new JLabel("Incomplete Headers");
		lblIncompleteHeaders.setHorizontalAlignment(SwingConstants.CENTER);
		lblIncompleteHeaders.setForeground(Color.BLACK);
		lblIncompleteHeaders.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblIncompleteHeaders.setBounds(10, 430, 162, 24);
		panel.add(lblIncompleteHeaders);
			
		/******************************** SUBSCRIBE  *********************************/
		
		JButton btnSubscribe = new JButton("Subscribe");
		btnSubscribe.setBounds(576, 598, 140, 23);
		contentPane.add(btnSubscribe);
		btnSubscribe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				CoAPConnection coapConnection = null;
				
				try {
					coapConnection = new CoAPConnection();
				} catch (SocketException e) {				
					e.printStackTrace();
				}
				
				// configure the data
				setMessage();
				setHttp();
				setCoAP();
							
				// create the HTTP object
				HttpPost httppost = new HttpPost(dataHttp.getURL());
				
				// set the headers
				setHeader(val, httppost);

				// start coap connection
				coapConnection.connection(dataCoAP, dataHttp, message, httpConnection.list_headers_v, httppost);
			}
		});
		btnSubscribe.setForeground(Color.BLACK);
		btnSubscribe.setFont(new Font("Times New Roman", Font.BOLD, 15));
		
		/***************************** UPDATE VALIDATION *********************************/
		
		/** Activate the validation of the number of variables
		 *  received per MQTT message in relation to the number of payload variables
		 */
		
		JCheckBox chckbxValidation = new JCheckBox("Enable validation");
		chckbxValidation.setBounds(368, 599, 152, 23);
		contentPane.add(chckbxValidation);
		
		chckbxValidation.addItemListener(new ItemListener() {
			 
		    @Override
		    public void itemStateChanged(ItemEvent event) {
		        int state = event.getStateChange();
		        if (state == ItemEvent.SELECTED) {		 
		        	validation = true;
		        } else if (state == ItemEvent.DESELECTED) {
		        	validation = false;
		        }
		    }						
		});			
		
		
		/******************************** UPDATE IP HTTP  *********************************/											
		
		txt_ServerHttp.getDocument().addDocumentListener(new DocumentListener() {
	        public void insertUpdate(DocumentEvent de) {
	            warn();
	        }

	        public void removeUpdate(DocumentEvent de) {
	            warn();
	        }

	        public void changedUpdate(DocumentEvent de) {
	            warn();
	        }
	        
	        public void warn(){
	            path = txt_PathHttp.getText();
	            port = txt_PortHTTP.getText();
	            IP = txt_ServerHttp.getText();
	            if(txt_PortHTTP.getText().isEmpty())
	                port = "";
	            if(txt_ServerHttp.getText().isEmpty())
	                IP = "IP";
	            if(txt_PathHttp.getText().isEmpty())
	                path = "PATH";
	                       
	            
	            if(txt_PortHTTP.getText().isEmpty())
	            	txt_PreviewHTTP.setText(IP + "/" + path);
	            else
	            	txt_PreviewHTTP.setText(IP+ ":" + port + "/" + path);
	        }
			
	    });
		
		/******************************** UPDATE IP CoAP  *********************************/
		
		txt_IPCoAP.getDocument().addDocumentListener(new DocumentListener() {
	        public void insertUpdate(DocumentEvent de) {
	            warn();
	        }

	        public void removeUpdate(DocumentEvent de) {
	            warn();
	        }

	        public void changedUpdate(DocumentEvent de) {
	            warn();
	        }
	        
	        public void warn(){
	            path = txt_PathCoAP.getText();
	            port = txt_PortCoAP.getText();
	            IP = txt_IPCoAP.getText();
	            if(txt_PortCoAP.getText().isEmpty())
	                port = "";
	            if(txt_IPCoAP.getText().isEmpty())
	                IP = "IP";
	            if(txt_PathCoAP.getText().isEmpty())
	                path = "PATH";
	                       
	            
	            if(txt_PortCoAP.getText().isEmpty())
	            	txt_PreviewCoAP.setText(IP + "/" + path);
	            else
	            	txt_PreviewCoAP.setText(IP+ ":" + port + "/" + path);
	        }
			
	    });
		
		/******************************** UPDATE PORT HTTP  *********************************/
		
		txt_PortHTTP.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent de) {
                warn();
            }

            public void removeUpdate(DocumentEvent de) {
                warn();
            }

            public void changedUpdate(DocumentEvent de) {
                warn();
            }
            
            public void warn(){
                path = txt_PathHttp.getText();
                port = txt_PortHTTP.getText();
                IP = txt_ServerHttp.getText();
                if(txt_PortHTTP.getText().isEmpty())
                    port = "";
                if(txt_ServerHttp.getText().isEmpty())
                    IP = "IP";
                if(txt_PathHttp.getText().isEmpty())
                    path = "PATH";
                           
                
                if(txt_PortHTTP.getText().isEmpty())
                	txt_PreviewHTTP.setText(IP + "/" + path);
                else
                	txt_PreviewHTTP.setText(IP+ ":" + port + "/" + path);
            }
        });
		
		/******************************** UPDATE PORT CoAP  *********************************/
		
		txt_PortCoAP.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent de) {
                warn();
            }

            public void removeUpdate(DocumentEvent de) {
                warn();
            }

            public void changedUpdate(DocumentEvent de) {
                warn();
            }
            
            public void warn(){
                path = txt_PathCoAP.getText();
                port = txt_PortCoAP.getText();
                IP = txt_IPCoAP.getText();
                if(txt_PortCoAP.getText().isEmpty())
                    port = "";
                if(txt_IPCoAP.getText().isEmpty())
                    IP = "IP";
                if(txt_PathCoAP.getText().isEmpty())
                    path = "PATH";
                           
                
                if(txt_PortCoAP.getText().isEmpty())
                	txt_PreviewCoAP.setText(IP + "/" + path);
                else
                	txt_PreviewCoAP.setText(IP+ ":" + port + "/" + path);
            }
        });
		
		/******************************** UPDATE PATH HTTP  *********************************/						
		
		txt_PathHttp.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent de) {
                warn();
            }

            public void removeUpdate(DocumentEvent de) {
                warn();
            }

            public void changedUpdate(DocumentEvent de) {
                warn();
            }
            
            public void warn(){
                
                path = txt_PathHttp.getText();
                port = txt_PortHTTP.getText();
                IP = txt_ServerHttp.getText();
                if(txt_PortHTTP.getText().isEmpty())
                    port = "";
                if(txt_ServerHttp.getText().isEmpty())
                    IP = "IP";
                if(txt_PathHttp.getText().isEmpty())
                    path = "PATH";
                           
                if(txt_PortHTTP.getText().isEmpty())
                	txt_PreviewHTTP.setText(IP + "/" + path);
                else
                	txt_PreviewHTTP.setText(IP+ ":" + port + "/" + path);
            }
		});
		
		/******************************** UPDATE PATH CoAP  *********************************/
		
		txt_PathCoAP.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent de) {
                warn();
            }

            public void removeUpdate(DocumentEvent de) {
                warn();
            }

            public void changedUpdate(DocumentEvent de) {
                warn();
            }
            
            public void warn(){
                
                path = txt_PathCoAP.getText();
                port = txt_PortCoAP.getText();
                IP = txt_IPCoAP.getText();
                if(txt_PortCoAP.getText().isEmpty())
                    port = "";
                if(txt_IPCoAP.getText().isEmpty())
                    IP = "IP";
                if(txt_PathCoAP.getText().isEmpty())
                    path = "PATH";
                           
                if(txt_PortCoAP.getText().isEmpty())
                	txt_PreviewCoAP.setText(IP + "/" + path);
                else
                	txt_PreviewCoAP.setText(IP+ ":" + port + "/" + path);
            }
		});

		/******************************** UPDATE MODEL MESSAGE  *********************************/
		
		editorPane.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent de) {
                warn();
            }

            public void removeUpdate(DocumentEvent de) {
                warn();
            }

            public void changedUpdate(DocumentEvent de) {
                warn();
            }
            
            public void warn(){
            	model = editorPane.getText();            	
            }   
		});
				
	}
	
	/******************************** CLASS UPDATE  *********************************/
	
	public void setHttp() {	
		// set the HTTP server
		String httpIP = txt_ServerHttp.getText();
		dataHttp.setIpHttp(httpIP);
		// set the HTTP path
		String htttpPath = txt_PathHttp.getText();
		dataHttp.setPathHttp(htttpPath);
		// set the HTTP port
		String htttpPort = txt_PortHTTP.getText();
		dataHttp.setPortHttp(htttpPort);
		// set the HTTP URL
		dataHttp.setURL(httpIP, htttpPort, htttpPath);
	}
	
	public void setCoAP() {
		// set the CoAP server	
		String ip = txt_IPCoAP.getText();
		dataCoAP.setIpCoAP(ip);
		// set the CoAP port	
		String port = txt_PortCoAP.getText();
		dataCoAP.setPortCoAP(port);
		// set the CoAP path	
		String path = txt_PathCoAP.getText();
		dataCoAP.setPathCoAP(path);;
	}
	
	public void setMessage() {					
		// set the payload variable
		message.setVariable(txtVariableModel.getText());
		// remove spaces from the payload message template
		String model1 = model.replaceAll(" ", "");				
		model1 = model1.replaceAll("\n", "");					
		message.setModel(model1);
		// calculate the number of bytes reduced by taking the spaces
		int decreased = model.length() - model1.length();
		// count the number of variables in the payload message model
		int variables = countIndexOf(model1,message.getVariable());
		// set the message data
		message.setMessageSize(variables+numHeaders);
		message.setVariablesSize(variables);
		message.setHeaderSize(numHeaders);		
		message.setValidation(validation);		
		JOptionPane.showMessageDialog(null, 
				  "                                                    Connected \n"
				+ "In each message transmission devices messages will be "+ decreased +" Bytes shorter");
	}
	private int countIndexOf(String texto, String valor) {
		int pos = 0;
		int count = 0;
		
		if (texto != null) {
			// Search for the next occurrence of the word
			pos = texto.indexOf(valor);
			// While finding other occurrences, he adds counter
			while (pos >= 0) {
				++count;
				// Look for the next occurrence from the last point
				pos = texto.indexOf(valor, pos + 1);
			}
		}
		return count;
	}
	
	/** Full headers (value and variable) are immediately configured **/
	public void setHeader(DefaultTableModel val, HttpPost httppost) {					
		for(int i=0; i < val.getRowCount(); i++) {			
			Headers he = new Headers();
			String name = (String) val.getValueAt(i, 0); // header name
			String value = (String) val.getValueAt(i,1); // header value
			he.setHeaders(name, value);
			httpConnection.list_headers.add(he);
			httppost.setHeader(name, value);
			//System.out.println("Header " + name + " valor "+ value + " adicionado.");			
		}		
	}	
}