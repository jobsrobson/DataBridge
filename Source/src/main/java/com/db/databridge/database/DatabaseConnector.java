package com.db.databridge.database;

import com.db.databridge.util.ConnectionUtil;
import com.db.databridge.window.Home;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class DatabaseConnector extends javax.swing.JDialog {
    
    private final Home main;
    private final ConnectionUtil connectionManager;
    
    public boolean conexaoAtiva = false;
    private String hostConexaoAtiva;
    public String databaseAtivo;
    
    
    /* Getters para variáveis dessa classe */
    public String getDatabaseAtivo() {
        return databaseAtivo;
    }
    

    public DatabaseConnector(java.awt.Frame parent, boolean modal) {
	super(parent, modal);
	initComponents();
	
	main = (Home) parent; // Obtém a instância da classe Home passando o parent
        connectionManager = main.connectionManager; // Obtém a instância de ConnectionUtil criada na classe Home
    }

    

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        buttonConnect = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        buttonTestConnection = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        labelDBType = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        labelHost = new javax.swing.JLabel();
        inputHost = new javax.swing.JTextField();
        labelPort = new javax.swing.JLabel();
        inputPort = new javax.swing.JTextField();
        labelDatabase = new javax.swing.JLabel();
        inputDatabase = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        labelUsername = new javax.swing.JLabel();
        inputUser = new javax.swing.JTextField();
        labelPassword = new javax.swing.JLabel();
        inputPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Conectar ao Banco de Dados");
        setResizable(false);

        buttonConnect.setText("Conectar");
        buttonConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonConnect(evt);
            }
        });

        buttonCancel.setText("Cancelar");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancel(evt);
            }
        });

        buttonTestConnection.setText("Testar Conexão");
        buttonTestConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTestConnection(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonTestConnection)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonConnect)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonConnect)
                    .addComponent(buttonCancel)
                    .addComponent(buttonTestConnection))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        labelDBType.setText("SGBD:");

        jRadioButton1.setSelected(true);
        jRadioButton1.setText("PostgreSQL");
        jRadioButton1.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelDBType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                .addComponent(jRadioButton1)
                .addGap(175, 175, 175))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelDBType)
                    .addComponent(jRadioButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        labelHost.setText("Host:");

        inputHost.setToolTipText("");

        labelPort.setText("Porta:");

        labelDatabase.setText("Nome do Banco: ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelDatabase)
                    .addComponent(labelPort)
                    .addComponent(labelHost))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(inputHost)
                    .addComponent(inputPort, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                    .addComponent(inputDatabase, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelHost)
                    .addComponent(inputHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPort)
                    .addComponent(inputPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelDatabase))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        labelUsername.setText("Nome de Usuário: ");

        labelPassword.setText("Senha:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelUsername)
                    .addComponent(labelPassword))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(inputUser, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                    .addComponent(inputPassword))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelUsername)
                    .addComponent(inputUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPassword)
                    .addComponent(inputPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    
    
    
    private void buttonConnect(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonConnect
        String host = inputHost.getText();
        String port = inputPort.getText();
        String database = inputDatabase.getText();
        String user = inputUser.getText();
        String password = new String(inputPassword.getPassword());
	
	if (host.isEmpty() || port.isEmpty() || database.isEmpty() || user.isEmpty() || password.isEmpty()) {
	    JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
	} else {
	    // Configuração da conexão. Passe essas informações para a classe ConnectionUtil para criar a conexão.
	    connectionManager.createConnection(host, port, database, user, password);
	    
	    // Verifique se a conexão foi bem-sucedida
	    if (connectionManager.isConnectionActive()) {
		conexaoAtiva = true;
		hostConexaoAtiva = host;
		databaseAtivo = database;
		JOptionPane.showMessageDialog(this, "Conexão bem-sucedida!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		
		// Chame o método para atualizar a JLabel na classe Home
		main.updateDatabaseConnectionStatus(conexaoAtiva, hostConexaoAtiva);
		main.checkFileExistence();
		dispose();
		
	    } else {
		// A conexão não foi bem-sucedida, exiba uma mensagem de erro
		JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados. Verifique as credenciais e tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
	    }
	}
    }//GEN-LAST:event_buttonConnect

    private void buttonCancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancel
        dispose();
    }//GEN-LAST:event_buttonCancel

    private void buttonTestConnection(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTestConnection
        String host = inputHost.getText();
        String port = inputPort.getText();
        String database = inputDatabase.getText();
        String user = inputUser.getText();
        String password = new String(inputPassword.getPassword());
	
	if (host.isEmpty() || port.isEmpty() || database.isEmpty() || user.isEmpty() || password.isEmpty()) {
	    JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
	} else {
	    // Configuração da conexão. Passe essas informações para a classe ConnectionUtil para testar a conexão.
	    connectionManager.testConnection(host, port, database, user, password);
	    
	    // Verifique se a conexão foi bem-sucedida
	    if (connectionManager.isSucceddedConnection()) {
		JOptionPane.showMessageDialog(this, "Teste de conexão bem-sucedido. Agora você pode conectar ao banco de dados.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		
	    } else {
		// A conexão não foi bem-sucedida, exiba uma mensagem de erro
		JOptionPane.showMessageDialog(this, "Falha na conexão. Verifique as credenciais e tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
	    }
	}
    }//GEN-LAST:event_buttonTestConnection




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonConnect;
    private javax.swing.JButton buttonTestConnection;
    private javax.swing.JTextField inputDatabase;
    private javax.swing.JTextField inputHost;
    private javax.swing.JPasswordField inputPassword;
    private javax.swing.JTextField inputPort;
    private javax.swing.JTextField inputUser;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel labelDBType;
    private javax.swing.JLabel labelDatabase;
    private javax.swing.JLabel labelHost;
    private javax.swing.JLabel labelPassword;
    private javax.swing.JLabel labelPort;
    private javax.swing.JLabel labelUsername;
    // End of variables declaration//GEN-END:variables
}
