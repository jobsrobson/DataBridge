package com.db.databridge.file;

import com.db.databridge.window.Home;
import java.io.File;


public class FileClose extends javax.swing.JDialog {
 
    public FileClose(java.awt.Frame parent, boolean modal) {
	super(parent, modal);
	initComponents();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        labelIcon = new javax.swing.JLabel();
        labelTitle = new javax.swing.JLabel();
        labelText = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        buttonCloseFile = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Fechar Arquivo");
        setResizable(false);

        labelIcon.setFont(new java.awt.Font("Segoe MDL2 Assets", 0, 36)); // NOI18N
        labelIcon.setForeground(new java.awt.Color(255, 204, 0));
        labelIcon.setText("");
        labelIcon.setIconTextGap(0);
        labelIcon.setMaximumSize(new java.awt.Dimension(48, 48));
        labelIcon.setMinimumSize(new java.awt.Dimension(48, 48));
        labelIcon.setPreferredSize(new java.awt.Dimension(48, 48));

        labelTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelTitle.setText("Deseja realmente fechar o arquivo aberto?");
        labelTitle.setToolTipText("");

        labelText.setEditable(false);
        labelText.setColumns(20);
        labelText.setLineWrap(true);
        labelText.setRows(5);
        labelText.setText("Caso o arquivo aberto seja proveniente de um download, será necessário baixá-lo novamente.");
        labelText.setWrapStyleWord(true);
        labelText.setAutoscrolls(false);
        labelText.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        labelText.setFocusable(false);
        labelText.setMargin(new java.awt.Insets(0, 0, 0, 0));
        labelText.setPreferredSize(new java.awt.Dimension(200, 180));
        labelText.setRequestFocusEnabled(false);
        labelText.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(labelIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelTitle))
                .addContainerGap(88, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labelTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelText, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labelIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(37, 37, 37))))
        );

        buttonCloseFile.setText("Fechar arquivo");
        buttonCloseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCloseFile(evt);
            }
        });

        buttonCancel.setText("Cancelar");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancel(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonCloseFile)
                .addGap(14, 14, 14))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonCloseFile)
                    .addComponent(buttonCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    
    /* Confirmar Fechamento de Arquivo - Botão */
    private void buttonCloseFile(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCloseFile
        // Antes de fechar o arquivo, atualize a JTextArea na classe Home
	Home mainInstance = (Home) getParent();
	mainInstance.updateConsoleMessage("Arquivo fechado com sucesso");
	
	File arquivoAberto = new File("csvImportado.csv");
	if (arquivoAberto.exists()) {
	    arquivoAberto.delete();
	}
	
	mainInstance.tableModel.setRowCount(0);
	mainInstance.tableModel.setColumnCount(0);
	
	mainInstance.checkFileExistence();
	
	dispose();
    }//GEN-LAST:event_buttonCloseFile
    
    
    /* Cancelar Fechamento de Arquivo - Botão */
    private void buttonCancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancel
        dispose();
    }//GEN-LAST:event_buttonCancel


    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonCloseFile;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel labelIcon;
    private javax.swing.JTextArea labelText;
    private javax.swing.JLabel labelTitle;
    // End of variables declaration//GEN-END:variables
}
