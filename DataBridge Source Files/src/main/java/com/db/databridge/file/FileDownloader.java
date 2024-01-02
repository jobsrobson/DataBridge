package com.db.databridge.file;

/* Imports */
import com.db.databridge.util.FileUtil;
import com.db.databridge.window.Home;

import java.io.IOException;
import javax.swing.SwingWorker;

public class FileDownloader extends javax.swing.JDialog {
    
    /* Atributos */
    public Home main;

    /* Construtor da Classe */
    public FileDownloader(java.awt.Frame parent, boolean modal) {
	super(parent, modal);
	initComponents();
	
	main = (Home) parent;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonPanel = new javax.swing.JPanel();
        buttonOK = new javax.swing.JButton();
        urlPanel = new javax.swing.JPanel();
        textfieldURL = new javax.swing.JTextField();
        buttonDownload = new javax.swing.JButton();
        statusPanel = new javax.swing.JPanel();
        consoleWebScrollPane = new javax.swing.JScrollPane();
        consoleWeb = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Abrir Arquivo da Web");
        setIconImage(null);
        setResizable(false);

        buttonOK.setText("OK");
        buttonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOK(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonOK)
                .addContainerGap())
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonOK)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        urlPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("URL"));

        buttonDownload.setText("Download");
        buttonDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDownload(evt);
            }
        });

        javax.swing.GroupLayout urlPanelLayout = new javax.swing.GroupLayout(urlPanel);
        urlPanel.setLayout(urlPanelLayout);
        urlPanelLayout.setHorizontalGroup(
            urlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(urlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(textfieldURL, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonDownload)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        urlPanelLayout.setVerticalGroup(
            urlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(urlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(urlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textfieldURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonDownload))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statusPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Status"));

        consoleWebScrollPane.setBorder(null);

        consoleWeb.setEditable(false);
        consoleWeb.setColumns(20);
        consoleWeb.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        consoleWeb.setForeground(new java.awt.Color(102, 153, 255));
        consoleWeb.setLineWrap(true);
        consoleWeb.setRows(3);
        consoleWeb.setWrapStyleWord(true);
        consoleWeb.setFocusable(false);
        consoleWebScrollPane.setViewportView(consoleWeb);

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(consoleWebScrollPane)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(consoleWebScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(urlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(statusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(urlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
                             
    
    
    /* Métodos da Interface Gráfica */
    
    /* Método de ação do botão Download */
    private void buttonDownload(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDownload
	String urlArquivo = textfieldURL.getText();
	consoleWeb.setText("Preparando download...");
	SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
        @Override
        protected Void doInBackground() throws Exception {
            try {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		buttonOK.setEnabled(false);
		buttonDownload.setEnabled(false);
		
                FileUtil.downloadFileFromWeb(urlArquivo, consoleWeb);
		
		buttonOK.setEnabled(true);
		buttonDownload.setEnabled(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            } catch (IOException e1) {
                consoleWeb.setText("Ocorreu um erro ao fazer o download do arquivo. \n" + e1.getMessage());
            }
            return null;
        }
    };

    worker.execute();

    }//GEN-LAST:event_buttonDownload

    /* Método de ação do botão OK */
    private void buttonOK(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOK
        main.checkFileExistence();
	dispose();
    }//GEN-LAST:event_buttonOK


    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonDownload;
    private javax.swing.JButton buttonOK;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JTextArea consoleWeb;
    private javax.swing.JScrollPane consoleWebScrollPane;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTextField textfieldURL;
    private javax.swing.JPanel urlPanel;
    // End of variables declaration//GEN-END:variables
}
