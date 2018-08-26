package br.com.inatel.configuration;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import br.com.inatel.coaptohttp.configuration.InterfaceTunelCoap;
import br.com.inatel.ddstohttp.configuration.InterfaceTunelDDS;
import br.com.inatel.mqtttohttp.configuration.InterfaceTunelMqtt;
import br.com.inatel.websockettohttp.configuration.InterfaceTunelWebsocket;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class Init extends JFrame {
	InterfaceTunelCoap interfaceTunelCoap;
	InterfaceTunelMqtt interfaceTunelMqtt;
	InterfaceTunelDDS interfaceTunelDds;
	InterfaceTunelWebsocket interfaceTunelWebsocket;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
		
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Init frame = new Init();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Init() {
		//Main broker = new Main();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JRadioButton rdbtnMqtt = new JRadioButton("MQTT");
		rdbtnMqtt.setBounds(49, 70, 109, 23);
		contentPane.add(rdbtnMqtt);
		
		JRadioButton rdbtnDds = new JRadioButton("DDS");
		rdbtnDds.setBounds(49, 130, 109, 23);
		contentPane.add(rdbtnDds);
		
		JRadioButton rdbtnCoap = new JRadioButton("CoAP");
		rdbtnCoap.setBounds(49, 100, 109, 23);
		contentPane.add(rdbtnCoap);
		
		JRadioButton rdbtnHttp = new JRadioButton("HTTP");
		rdbtnHttp.setSelected(true);
		rdbtnHttp.setBounds(260, 70, 109, 23);
		contentPane.add(rdbtnHttp);
		
		JRadioButton rdbtnWeb = new JRadioButton("WebSocket");
		rdbtnWeb.setBounds(49, 160, 109, 23);
		contentPane.add(rdbtnWeb);
		
		ButtonGroup group = new ButtonGroup();
	    group.add(rdbtnMqtt);
	    group.add(rdbtnCoap);
	    group.add(rdbtnDds);
	    group.add(rdbtnWeb);	    				
		
			    
		JLabel lblChooseTheProtocols = new JLabel("Choose the sending protocol");
		lblChooseTheProtocols.setBounds(27, 38, 169, 14);
		contentPane.add(lblChooseTheProtocols);
		
		JLabel lblNewLabel = new JLabel("Choose the receiving protocol");
		lblNewLabel.setBounds(231, 38, 169, 14);
		contentPane.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setForeground(Color.BLACK);
		panel_1.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_1.setBounds(219, 11, 195, 196);
		contentPane.add(panel_1);
		
		JButton btnConfirm = new JButton("CONFIRM");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {							
				
				if(rdbtnMqtt.isSelected()) {
					// start MQTT
					if(interfaceTunelMqtt==null) {
						interfaceTunelMqtt = new InterfaceTunelMqtt();
						interfaceTunelMqtt.setVisible(true);						
					}else {
						interfaceTunelMqtt.setVisible(true);
						interfaceTunelMqtt.setState(InterfaceTunelMqtt.NORMAL);
					}
				}
				else if(rdbtnDds.isSelected()) {
					// start MQTT
					if(interfaceTunelDds==null) {
						try {
							interfaceTunelDds = new InterfaceTunelDDS();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						interfaceTunelDds.setVisible(true);						
					}else {
						interfaceTunelDds.setVisible(true);
						interfaceTunelDds.setState(InterfaceTunelMqtt.NORMAL);
					}
				}
				else if(rdbtnCoap.isSelected()) {
					// start CoAP
					if(interfaceTunelCoap==null) {
						try {
							interfaceTunelCoap = new InterfaceTunelCoap();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						interfaceTunelCoap.setVisible(true);
					}else {
						interfaceTunelCoap.setVisible(true);
						interfaceTunelCoap.setState(InterfaceTunelCoap.NORMAL);
					}
					
				}
				else if(rdbtnWeb.isSelected()) {
					// start websocket
					if(interfaceTunelWebsocket==null) {
						try {
							interfaceTunelWebsocket = new InterfaceTunelWebsocket();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						interfaceTunelWebsocket.setVisible(true);
					}else {
						interfaceTunelWebsocket.setVisible(true);
						interfaceTunelWebsocket.setState(InterfaceTunelWebsocket.NORMAL);
					}
					
				}
				else {
					JOptionPane.showMessageDialog(null, "Select some protocol");
				}
			}
		});
		btnConfirm.setBounds(170, 227, 89, 23);
		contentPane.add(btnConfirm);
		
		JPanel panel = new JPanel();
		panel.setForeground(Color.BLACK);
		panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel.setBounds(14, 11, 195, 196);
		contentPane.add(panel);
					
	}
}
