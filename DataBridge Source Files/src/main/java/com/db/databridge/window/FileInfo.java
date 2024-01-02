package com.db.databridge.window;

import com.db.databridge.util.FileUtil;
import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

public class FileInfo extends javax.swing.JDialog {
    
    private final Home home;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###");
    public File arquivoCSV = new File("csvImportado.csv");  
    public DefaultTableModel tableModelAboutData;
    
    public FileInfo(java.awt.Frame parent, boolean modal) {
	super(parent, modal);
	initComponents();

	home = (Home) parent;
        
        // Inicie a carga de dados em segundo plano
        LoadDataWorker worker = new LoadDataWorker();
        worker.execute();

    }
    
     
    /* Atualiza a tabela de informações */
    public void updateTableData() {
	DefaultTableModel model = (DefaultTableModel) tableAboutData.getModel();
	model.setRowCount(0); // Limpa todas as linhas da tabela

	// Obter os nomes das colunas do arquivo
	List<String> columnNames = FileUtil.getFileColumnNames(arquivoCSV);

	// Para cada coluna, calcular a contagem de valores faltantes, valores únicos e registros
	for (String columnName : columnNames) {
	    int columnIndex = model.getRowCount(); // Índice da próxima linha na tabela
	    int missingDataCount = FileUtil.countMissingDataInColumn(arquivoCSV, columnIndex);
	    int uniqueValuesCount = FileUtil.countUniqueValuesInColumn(arquivoCSV, columnIndex);
	    int recordCount = FileUtil.countRecordsInColumn(arquivoCSV, columnIndex);
	    String dataType = FileUtil.determineDataTypeInColumn(arquivoCSV, columnIndex);

	    // Adicionar uma nova linha com os dados da coluna, tipo de dado, contagem de valores faltantes, valores únicos e registros
	    model.addRow(new Object[]{columnName, dataType, missingDataCount, uniqueValuesCount, recordCount});
	}
    }
    
    private class LoadDataWorker extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() throws Exception {
	/* Criação do Modelo da Tabela */
	    tableAboutData.setModel(new DefaultTableModel(
		new Object[][]{
		    {null, null, null, null, null}, 
		    {null, null, null, null, null},
		    {null, null, null, null, null},
		    {null, null, null, null, null}
		},
		new String[]{
		    "Coluna", "Tipo", "Ausentes", "Únicos", "Total" // Nome das Colunas
		}
	    ) {
		Class[] types = new Class[]{
		    java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class // Definido o tipo das colunas
		};
		boolean[] canEdit = new boolean[]{
		    false, false, false, false, false
		};

		public Class getColumnClass(int columnIndex) {
		    return types[columnIndex];
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {
		    return canEdit[columnIndex];
		}
	    });


	    /* Atualiza nome_arquivo */
	    String nomeArquivo = home.getFileName();
	    if (nomeArquivo != null) {
		fileName.setText(nomeArquivo);
	    } else {
		fileName.setText("[Originado da Web]");
	    }

	    /* Atualiza tamanho_arquivo */
	    String tamanhoArquivo = FileUtil.getFileSize(arquivoCSV);
	    fileSize.setText(tamanhoArquivo);

	    /* Atualiza local_arquivo 
	    String localArquivo = home.getFileAddress();
	    fileLocation.setText(localArquivo);*/

	    /* Atualiza linhas_arquivo */
	    int quantLinhas = FileUtil.getFileRowCount(arquivoCSV);
	    fileRows.setText(decimalFormat.format(quantLinhas));

	    /* Atualiza colunas_arquivo */
	    int quantColunas = FileUtil.getFileColumnCount(arquivoCSV);
	    fileColumns.setText(decimalFormat.format(quantColunas));

	    /* Atualiza registros_arquivo */ 
	    int quantRegistros = (FileUtil.getFileRowCount(arquivoCSV) - 1);
	    fileRegistries.setText(decimalFormat.format(quantRegistros));

	    /* Atualiza separador_arquivo */
	    String separadorArquivo = (FileUtil.determineFileSeparator(arquivoCSV));
	    if (";".equals(separadorArquivo)) {
		fileSeparator.setText("Ponto-e-Vírgula (;)"); 
	    } if (",".equals(separadorArquivo)) {
		fileSeparator.setText("Vírgula (,)");
	    } if ("\t".equals(separadorArquivo)) {
		fileSeparator.setText("Tabulação (Tab)");
	    }

	    updateTableData();
	    
            return null;
        }

        @Override
        protected void done() {
            try {
                // Atualize a interface do usuário após a conclusão do carregamento
                get();
                updateTableData(); // Método para atualizar a tabela na classe FileInfo
            } catch (InterruptedException | ExecutionException e) {
                // Lide com erros, se necessário
            }
        }
    }
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        fileRows = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        fileColumns = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        fileRegistries = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        fileSeparator = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        fileName = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        fileSize = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        okButton = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableAboutData = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Informações do CSV");
        setResizable(false);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações Gerais"));

        jLabel4.setText("Linhas:");

        fileRows.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        fileRows.setText("NaN");

        jLabel5.setText("Colunas:");

        fileColumns.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        fileColumns.setText("NaN");

        jLabel6.setText("Registros:");

        fileRegistries.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        fileRegistries.setText("NaN");

        jLabel8.setText("Separador:");

        fileSeparator.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        fileSeparator.setText("NaN");

        jLabel1.setText("Nome do Arquivo:");

        fileName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        fileName.setText("NaN");

        jLabel3.setText("Tamanho:");

        fileSize.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        fileSize.setText("NaN");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8))
                        .addGap(102, 102, 102)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fileRegistries)
                            .addComponent(fileSeparator)
                            .addComponent(fileColumns)
                            .addComponent(fileRows)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(62, 62, 62)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fileSize)
                            .addComponent(fileName))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(fileRows))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(fileColumns))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(fileRegistries))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(fileSeparator))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(fileName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(fileSize))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButton(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(okButton)
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados"));

        tableAboutData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Coluna", "Tipo de Dado", "Dados Faltantes", "Dados Únicos"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableAboutData.setFocusable(false);
        jScrollPane1.setViewportView(tableAboutData);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void okButton(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButton
        dispose();
    }//GEN-LAST:event_okButton



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel fileColumns;
    private javax.swing.JLabel fileName;
    private javax.swing.JLabel fileRegistries;
    private javax.swing.JLabel fileRows;
    private javax.swing.JLabel fileSeparator;
    private javax.swing.JLabel fileSize;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton okButton;
    private javax.swing.JTable tableAboutData;
    // End of variables declaration//GEN-END:variables
}
