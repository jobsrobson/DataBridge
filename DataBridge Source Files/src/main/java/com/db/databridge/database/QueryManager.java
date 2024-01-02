package com.db.databridge.database;

import com.db.databridge.util.ConnectionUtil;
import com.db.databridge.window.Home;
import java.awt.event.ItemEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.DefaultTableModel;


public class QueryManager extends javax.swing.JDialog {
    
    private final Home home;
    public DatabaseUploader databaseUploader;
    private final ConnectionUtil connectionManager;
    
    private DefaultTableModel tableModel;

    public QueryManager(java.awt.Frame parent, boolean modal) {
	super(parent, modal);
	initComponents();
	
	home = (Home) parent;
	connectionManager = home.connectionManager;
	
	/* Preenche o combo com os nomes das colunas da tabela */
	populateComboTableNames();
	
	// Adicione um ouvinte de alterações ao JComboBox "nome_tabelas"
	tableNames.addItemListener((ItemEvent e) -> {
	    if (e.getStateChange() == ItemEvent.SELECTED) {
		// O usuário selecionou uma tabela, atualize o JComboBox "coluna"
		popularNomeColunasComboBox((String) tableNames.getSelectedItem());
		
		System.out.println("Combo nome_tabelas populado.");
	    }
	});
	
	// Configure o modelo da tabela
        String[] columnNames = {}; // Inicialmente, nenhum nome de coluna
        tableModel = new DefaultTableModel(columnNames, 0);
        resultsTable.setModel(tableModel);
	
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        comboColumnNames = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        comboOperator = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        comboQueryText = new javax.swing.JTextField();
        queryButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        labelResultQuantity = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        resultsTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        tableNames = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Filtragem de Dados");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Consulta SQL"));

        jLabel1.setText("Coluna:");

        comboOperator.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "equals", "does not equal", "less then", "greater than" }));

        jLabel2.setText("Operador:");

        jLabel3.setText("Valor:");

        queryButton.setText("Pesquisar");
        queryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                queryButton(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboColumnNames, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboOperator, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(comboQueryText, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(queryButton)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboColumnNames, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboOperator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboQueryText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(queryButton))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Resultados"));

        labelResultQuantity.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelResultQuantity.setText("Quantidade de Resultados:  NaN");

        resultsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        resultsTable.setCellSelectionEnabled(true);
        resultsTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane2.setViewportView(resultsTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(labelResultQuantity, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(labelResultQuantity)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                .addContainerGap())
        );

        closeButton.setText("Fechar");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButton(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(closeButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Tabela"));

        jLabel5.setText("Selecione uma tabela presente no banco de dados para realizar uma nova consulta:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(tableNames, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
                        .addContainerGap(117, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableNames, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    
    
    /*********/
    
    private void populateComboTableNames() {
	// Chame o método getAvailableTables da sua classe ConnectionUtil
	List<String> tabelas = connectionManager.getAvailableTables();

	// Limpe o JComboBox
	//nome_tabelas.removeAllItems();

	// Preencha o JComboBox com os nomes das tabelas
	tableNames.addItem("Selecione uma tabela...");
	for (String tabela : tabelas) {
	    tableNames.addItem(tabela);
	}
    }
    
    private void popularNomeColunasComboBox(String tabela) {
	// Chame o método getTableColumns da sua classe ConnectionUtil
	List<String> colunas = connectionManager.getTableColumns(tabela);

	// Limpe o JComboBox combo_coluna
	comboColumnNames.removeAllItems();

	// Preencha o JComboBox combo_coluna com os nomes das colunas
	comboColumnNames.addItem("Selecione uma coluna...");
	for (String colunaNome : colunas) {
	    System.out.println(colunaNome);
	    comboColumnNames.addItem(colunaNome);
	}
    }
   
    /********/
    
    

    
    private void closeButton(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButton
        dispose();
    }//GEN-LAST:event_closeButton

    private void queryButton(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_queryButton

    // Obtém os valores selecionados dos comboboxes e campo de texto
    String tabelaSelecionada = (String) tableNames.getSelectedItem();
    String colunaSelecionada = (String) comboColumnNames.getSelectedItem();
    String operadorSelecionado = (String) comboOperator.getSelectedItem();
    String valorConsulta = comboQueryText.getText();

    // Mapeia o operador selecionado para um operador SQL válido
    String operadorSQL = "="; // Valor padrão para "equals"

    if (null != operadorSelecionado) switch (operadorSelecionado) {
	    case "does not equal" -> operadorSQL = "<>";
	    case "less than" -> operadorSQL = "<";
	    case "greater than" -> operadorSQL = ">";
	    default -> {
	}
    }

    // Monta a consulta SQL com base nas seleções do usuário
    String query = "SELECT * FROM " + tabelaSelecionada + " WHERE " + colunaSelecionada + " " + operadorSQL + " ?";


        try {
            try (PreparedStatement preparedStatement = connectionManager.getPreparedStatement(query)) {
                preparedStatement.setString(1, valorConsulta);

                // Remove todas as linhas existentes do modelo da tabela
                while (tableModel.getRowCount() > 0) {
                    tableModel.removeRow(0);
                }

                int count = 0;
                // Adicione as colunas como nomes de colunas da tabela
                ResultSetMetaData metaData = preparedStatement.getMetaData();
                int numColumns = metaData.getColumnCount();
                String[] columnNames = new String[numColumns];
                for (int i = 1; i <= numColumns; i++) {
                    columnNames[i - 1] = metaData.getColumnName(i);
                }
                tableModel.setColumnIdentifiers(columnNames);

                // Adicione as linhas com os valores das colunas
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String[] rowData = new String[numColumns];
                        for (int i = 1; i <= numColumns; i++) {
                            rowData[i - 1] = resultSet.getString(i);
                        }
                        tableModel.addRow(rowData);
                        count++;
                    }
                }

                // Atualize o JLabel com a quantidade de resultados
                labelResultQuantity.setText("Quantidade de Resultados: " + count);
		System.out.println(query);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }//GEN-LAST:event_queryButton


    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JComboBox<String> comboColumnNames;
    private javax.swing.JComboBox<String> comboOperator;
    private javax.swing.JTextField comboQueryText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelResultQuantity;
    private javax.swing.JButton queryButton;
    private javax.swing.JTable resultsTable;
    private javax.swing.JComboBox<String> tableNames;
    // End of variables declaration//GEN-END:variables
}
