package com.db.databridge.window;

import com.db.databridge.file.*;
import com.db.databridge.util.*;
import com.db.databridge.database.*;

/* Classes Importadas */
//<editor-fold defaultstate="collapsed" desc="(collapsed) General Imported Classes">
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.util.SystemInfo;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
//</editor-fold>

public class Home extends javax.swing.JFrame {
    
    /* Instâncias */
    public ConnectionUtil connectionManager;
    public DatabaseUploader databaseUploader;
    public DatabaseConnector databaseConfig;
    public UserSettings userSettings;
    public FileInfo infoCSV;
    public static Home instance;
    public DefaultTableModel tableModel;
    
    /* Atributos - Gerenciamento do Arquivo */    
    String nomeArquivoOriginal = null;
    String nomeArquivo = "csvImportado.csv";
    String diretorio = System.getProperty("user.dir");
    String caminhoArquivo = diretorio + File.separator + nomeArquivo;
    public String tamanhoCSV;
    public File arquivo = new File(caminhoArquivo);
    public File arquivoCSV = new File("csvImportado.csv");
    boolean arquivoEstaAberto = false;
    
    /* Contadores do Tempo de Processamento do Arquivo */
    public long startTime = 0;
    public long endTime = 0;
    

    /* Construtor Principal da Classe */
    public Home() {
	instance = this;
	this.tamanhoCSV = FileUtil.getFileSize(arquivoCSV);
	initComponents();
	
	/* Métodos executados ao abrir a janela */
	if (arquivoCSV.exists()) {
	    arquivoCSV.delete();
	} 
	
	checkFileExistence();
	
	/* Criação de Instâncias de Classes Auxiliares */
	connectionManager = new ConnectionUtil();
	userSettings = new UserSettings();
	
	/* Criação do Modelo da Tabela */
	tableModel = new DefaultTableModel();
	tableFileContent.setModel(tableModel);
	
	
	/* Visibilidade de itens ao abrir a janela */
	buttonForceCloseConnection.setVisible(false);
	labelConnectionStatus.setText("Banco desconectado");
	labelFileCounts.setText("");
        labelFileSize.setText("");
	labelFileProcessingTime.setText("");
	
	setIcon();
	
	/* Ouvinte de Eventos de Mouse para abrir o Menu de Contexto na Tabela*/
	tableFileContent.addMouseListener(new MouseAdapter() {
	    File csvFile = new File("csvImportado.csv");
	    
	    @Override
	    public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger() && csvFile.exists()) {
		    contextMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	    }

	    @Override
	    public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger() && csvFile.exists()) {
		    contextMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	    }
	});
	
	/* Ações do Menu de Contexto da Tabela*/
	copyMenuItem.addActionListener((ActionEvent e) -> {
	    copySelectedCellsToClipboard();
	});
	
	closeFileMenuItem.addActionListener((ActionEvent e) -> {
	    menuCloseFile(e);
	});
	
	uploadFileMenuItem.addActionListener((ActionEvent e) -> {
	    if (connectionManager.isConnectionActive()) {
		menuUploadToDB(e); 
	    } else {
		JOptionPane.showMessageDialog(this, "O upload requer conexão com um banco de dados e não há uma conexão no momento. Conecte-se a um banco de dados e tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
	    }
	});

    }
    

    /* Define os ícones do aplicativo */
    private void setIcon() {
        List<Image> icons = new ArrayList<>();

        // Carrega as imagens do Classpath
        try {
            Image icon16 = ImageIO.read(getClass().getResource("/16x16.png"));
            Image icon32 = ImageIO.read(getClass().getResource("/32x32.png"));
            Image icon48 = ImageIO.read(getClass().getResource("/48x48.png"));
            Image icon128 = ImageIO.read(getClass().getResource("/128x128.png"));
            Image icon256 = ImageIO.read(getClass().getResource("/256x256.png"));

            icons.add(icon16);
            icons.add(icon32);
            icons.add(icon48);
            icons.add(icon128);
            icons.add(icon256);
        } catch (IOException e) { }

        setIconImages(icons);
    }

    /* Main - Definição do tema da UI */
    public static void main(String args[]) {

	    if (SystemInfo.isLinux) {
		JFrame.setDefaultLookAndFeelDecorated( true );
		JDialog.setDefaultLookAndFeelDecorated( true );
	    }
	    
	    /* Define o tema da UI */
	    FlatMacDarkLaf.setup();

	    java.awt.EventQueue.invokeLater(() -> {
		new Home().setVisible(true);
	    });
	}
    
    
    
/* MÉTODOS DE EXIBIÇÃO */
    
    /* Método para exibir o CSV local na Tabela */
    private void showFileContentInTable(String fileName) {
	String detectedSeparator = FileUtil.determineFileSeparator(arquivoCSV);
	
	try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
	    
	    // Limpar a tabela antes de adicionar novas linhas
	    tableModel.setRowCount(0);
	    tableModel.setColumnCount(0);
	    
            startTime = System.currentTimeMillis();
	    String row;
	    boolean firstRow = true;
	    
	    while ((row = reader.readLine()) != null) {
		if (firstRow) {
		    row = row.toLowerCase();
		    firstRow = false;
		    String[] columns = row.split(Pattern.quote(detectedSeparator));
		    tableModel.setColumnIdentifiers(columns);
		    
		} else {
		    String[] fileData = row.split(Pattern.quote(detectedSeparator));
		    tableModel.addRow(fileData);
		    
		}
	    }
	} catch (IOException e) {
	    System.out.println("ERRO: Falha ao preencher a tabela com CSV. " + e.getMessage());
	} 
	
        endTime = System.currentTimeMillis();
        
        updateProcessingTimeLabel();
	updateFileValues();
    }
    

    /* Método para verificar a existência do arquivo na pasta e ativar/desativar menus da interface */
    public final Boolean checkFileExistence() {
	File csvFile = new File("csvImportado.csv");
	if (csvFile.exists()) {
	    menuCloseFile.setEnabled(true);
	    menuFileInfo.setEnabled(true);
	    menuFileInfo.setEnabled(true);
	    
	    if (connectionManager.activeConnection == true) {
		menuUploadToDB.setEnabled(true); 
	    }
	    
	    showFileContentInTable(nomeArquivo);
	    return true;
	} else {
	    menuCloseFile.setEnabled(false);
	    menuFileInfo.setEnabled(false); 
	    menuUploadToDB.setEnabled(false); 
	    menuFileInfo.setEnabled(false);
	    return false;
	}
    }
    
    

    
/* MÉTODOS GERAIS */

    /* Método compartilhável para permitir alteração da mensagem no Console */
    public void updateConsoleMessage(String mensagem) {
	newConsole.setText(mensagem);
    }
    
    /* Método para atualizar a caixa de status do Banco */
    public void updateDatabaseConnectionStatus(boolean status, String host) {
	labelConnectionStatus.setText("Conectado a: " + host);
	buttonForceCloseConnection.setVisible(true);
	System.out.println("conexaoAtiva: " + connectionManager.isConnectionActive());
    }
    
    /* Método para exibir o status de conexão com o Banco */
    private void forceCloseConnection() {
	if (connectionManager.isConnectionActive()) {
	    connectionManager.closeConnection();
	    labelConnectionStatus.setText("Banco desconectado");
	    buttonForceCloseConnection.setVisible(false);
	} else {
	    labelConnectionStatus.setText("Não foi possível encerrar a conexão");
	    System.out.println("ERRO: não foi possível encerrar a conexão com o banco de dados.");
	}
    }
    
    /* Método para obter o nome do arquivo */
    public String getFileName() {
	if (arquivoEstaAberto) {
	    return nomeArquivo;
	} else {
	    return nomeArquivoOriginal;
	}
    }
    
    /* Método compartilhável para retornar o local do arquivo */
    public String getFileAddress() {
	return caminhoArquivo;
    }
   
    /* Método para atualizar o label com o tempo de processamento do arquivo */
    private void updateProcessingTimeLabel() {
	if (startTime > 0 && endTime > 0) {
	    long processingTime = endTime - startTime;
	    // Formate o tempo para exibição, por exemplo, em segundos
	    double processingTimeInSeconds = processingTime / 1000.0;
	    labelFileProcessingTime.setText(processingTimeInSeconds + " segundos");
	} else {
	    labelFileProcessingTime.setText("Tempo de processamento: N/A");
	}
    }

    /* Método para atualizar os mostradores de valores da Bottom Bar */
    private void updateFileValues() {
	newConsole.setText("Registros lidos: " + FileUtil.getFileRowCount(arquivoCSV));
	labelFileCounts.setText("[" + FileUtil.getFileColumnCount(arquivoCSV) + ", " + (FileUtil.getFileRowCount(arquivoCSV) - 1) + "]");
	labelFileSize.setText(FileUtil.getFileSize(arquivoCSV));
    }
    
    
    /* Método para copiar somente a seleção de texto na Tabela */
    private void copySelectedCellsToClipboard() {
        int[] selectedRows = tableFileContent.getSelectedRows();
        int[] selectedColumns = tableFileContent.getSelectedColumns();

        StringBuilder copiedText = new StringBuilder();

        for (int row : selectedRows) {
            for (int col : selectedColumns) {
                Object cellValue = tableFileContent.getValueAt(row, col);
                copiedText.append(cellValue).append("\t"); // Use "\t" para separar as células
            }
            copiedText.append("\n"); // Nova linha após cada linha da tabela
        }

        // Copia o texto para a área de transferência
        StringSelection stringSelection = new StringSelection(copiedText.toString());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        contextMenu = new javax.swing.JPopupMenu();
        copyMenuItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        uploadFileMenuItem = new javax.swing.JMenuItem();
        closeFileMenuItem = new javax.swing.JMenuItem();
        superiorPanel = new javax.swing.JPanel();
        buttonViewFileContent = new javax.swing.JButton();
        newConsole = new javax.swing.JLabel();
        inferiorPanel = new javax.swing.JToolBar();
        labelConnectionStatus = new javax.swing.JLabel();
        buttonForceCloseConnection = new javax.swing.JButton();
        toolbarFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        labelFileCounts = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        labelFileSize = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        labelFileProcessingTime = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        tableScrollPane = new javax.swing.JScrollPane();
        tableFileContent = new javax.swing.JTable();
        menuBar = new javax.swing.JMenuBar();
        mainMenuFile = new javax.swing.JMenu();
        menuOpenLocalFile = new javax.swing.JMenuItem();
        menuOpenFileFromWeb = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuCloseFile = new javax.swing.JMenuItem();
        menuExit = new javax.swing.JMenuItem();
        mainMenuTools = new javax.swing.JMenu();
        menuFileInfo = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menuConnectToDB = new javax.swing.JMenuItem();
        menuUploadToDB = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        menuNewQuery = new javax.swing.JMenuItem();
        mainMenuHelp = new javax.swing.JMenu();
        menuKeyboardShortcuts = new javax.swing.JMenuItem();
        menuAbout = new javax.swing.JMenuItem();

        fileChooser.setApproveButtonText("Abrir");
        fileChooser.setDialogTitle("Abrir arquivo local");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos CSV", "csv"));

        copyMenuItem.setText("Copiar seleção");
        contextMenu.add(copyMenuItem);
        contextMenu.add(jSeparator4);

        uploadFileMenuItem.setText("Fazer upload...");
        contextMenu.add(uploadFileMenuItem);

        closeFileMenuItem.setText("Fechar arquivo");
        contextMenu.add(closeFileMenuItem);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DataBridge Pro");
        setMinimumSize(new java.awt.Dimension(600, 600));

        buttonViewFileContent.setText("Visualizar CSV");
        buttonViewFileContent.setToolTipText("Recarrega o conteúdo do arquivo na Tabela.");
        buttonViewFileContent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonViewFileContent(evt);
            }
        });

        newConsole.setText("Abra um arquivo para começar");

        javax.swing.GroupLayout superiorPanelLayout = new javax.swing.GroupLayout(superiorPanel);
        superiorPanel.setLayout(superiorPanelLayout);
        superiorPanelLayout.setHorizontalGroup(
            superiorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, superiorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newConsole)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonViewFileContent, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        superiorPanelLayout.setVerticalGroup(
            superiorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(superiorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(superiorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonViewFileContent)
                    .addComponent(newConsole))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        inferiorPanel.setRollover(true);

        labelConnectionStatus.setForeground(new java.awt.Color(102, 102, 102));
        labelConnectionStatus.setLabelFor(labelConnectionStatus);
        labelConnectionStatus.setToolTipText("");
        inferiorPanel.add(labelConnectionStatus);

        buttonForceCloseConnection.setText("Encerrar conexão");
        buttonForceCloseConnection.setToolTipText("Força o encerramento da conexão com o banco de dados");
        buttonForceCloseConnection.setFocusable(false);
        buttonForceCloseConnection.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonForceCloseConnection.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonForceCloseConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonForceCloseConnection(evt);
            }
        });
        inferiorPanel.add(buttonForceCloseConnection);
        inferiorPanel.add(toolbarFiller);

        labelFileCounts.setText("FileCounts");
        labelFileCounts.setToolTipText("[Colunas, Linhas]");
        inferiorPanel.add(labelFileCounts);
        inferiorPanel.add(filler2);

        labelFileSize.setText("FileSize");
        labelFileSize.setToolTipText("Tamanho do arquivo no disco");
        inferiorPanel.add(labelFileSize);
        inferiorPanel.add(filler1);

        labelFileProcessingTime.setText("ProcessingTime");
        labelFileProcessingTime.setToolTipText("Tempo de processamento do arquivo");
        inferiorPanel.add(labelFileProcessingTime);

        jLayeredPane1.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jLayeredPane1.setMinimumSize(null);

        tableScrollPane.setName(""); // NOI18N

        tableFileContent.setModel(new javax.swing.table.DefaultTableModel(
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
        tableFileContent.setAlignmentX(0.0F);
        tableFileContent.setAlignmentY(0.0F);
        tableFileContent.setCellSelectionEnabled(true);
        tableFileContent.setFillsViewportHeight(true);
        tableFileContent.setFocusable(false);
        tableFileContent.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        tableFileContent.setSelectionBackground(new java.awt.Color(102, 153, 255));
        tableFileContent.setShowGrid(true);
        tableScrollPane.setViewportView(tableFileContent);

        jLayeredPane1.setLayer(tableScrollPane, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 773, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
        );

        mainMenuFile.setText("Arquivo");

        menuOpenLocalFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuOpenLocalFile.setText("Abrir arquivo local...");
        menuOpenLocalFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenLocalFile(evt);
            }
        });
        mainMenuFile.add(menuOpenLocalFile);

        menuOpenFileFromWeb.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuOpenFileFromWeb.setText("Abrir arquivo da Web...");
        menuOpenFileFromWeb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenFileFromWeb(evt);
            }
        });
        mainMenuFile.add(menuOpenFileFromWeb);
        mainMenuFile.add(jSeparator1);

        menuCloseFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuCloseFile.setText("Fechar arquivo");
        menuCloseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCloseFile(evt);
            }
        });
        mainMenuFile.add(menuCloseFile);

        menuExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        menuExit.setText("Sair");
        menuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExit(evt);
            }
        });
        mainMenuFile.add(menuExit);

        menuBar.add(mainMenuFile);

        mainMenuTools.setText("Comandos");

        menuFileInfo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuFileInfo.setText("Informações do CSV");
        menuFileInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileInfo(evt);
            }
        });
        mainMenuTools.add(menuFileInfo);
        mainMenuTools.add(jSeparator2);

        menuConnectToDB.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuConnectToDB.setText("Conectar Banco de Dados...");
        menuConnectToDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuConnectToDB(evt);
            }
        });
        mainMenuTools.add(menuConnectToDB);

        menuUploadToDB.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuUploadToDB.setText("Enviar arquivo para Banco de Dados");
        menuUploadToDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuUploadToDB(evt);
            }
        });
        mainMenuTools.add(menuUploadToDB);
        mainMenuTools.add(jSeparator3);

        menuNewQuery.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuNewQuery.setText("Filtrar dados");
        menuNewQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuNewQuery(evt);
            }
        });
        mainMenuTools.add(menuNewQuery);

        menuBar.add(mainMenuTools);

        mainMenuHelp.setText("Ajuda");

        menuKeyboardShortcuts.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuKeyboardShortcuts.setText("Atalhos de Teclado");
        menuKeyboardShortcuts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuKeyboardShortcuts(evt);
            }
        });
        mainMenuHelp.add(menuKeyboardShortcuts);

        menuAbout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuAbout.setText("Sobre");
        menuAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAbout(evt);
            }
        });
        mainMenuHelp.add(menuAbout);

        menuBar.add(mainMenuHelp);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inferiorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(superiorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(superiorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inferiorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents



/* AÇÕES GERAIS DA INTERFACE */

    /* Abrir arquivo Local - Menu */
    private void menuOpenLocalFile(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenLocalFile
	File selectedFile = FileUtil.openLocalFile(fileChooser);
	if (selectedFile != null) {
	    nomeArquivoOriginal = selectedFile.getName();
	    File copyFile = new File("csvImportado.csv");

	    try {
		FileUtil.copyFile(selectedFile, copyFile);
		showFileContentInTable(selectedFile.getAbsolutePath());
		checkFileExistence();
		updateFileValues();
	    } catch (IOException e) {
		System.out.println("ERRO: Impossível abrir arquivo local. " + e);
	    }
	}
    }//GEN-LAST:event_menuOpenLocalFile

    
    /* Visualizar Arquivo no Console - Botão */
    private void buttonViewFileContent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonViewFileContent
        String allowedFileName = "csvImportado.csv";
	String selectedFileName = null;
	
	File file = new File(allowedFileName);
	if (file.exists()) {
	    selectedFileName = allowedFileName;
	}
	
	if (selectedFileName != null) {
	    showFileContentInTable(selectedFileName);
	    
	} else {
	    newConsole.setText("Não há nenhum arquivo aberto");
	}
    }//GEN-LAST:event_buttonViewFileContent
  
    
    /* Abrir arquivo da Web - Menu */
    private void menuOpenFileFromWeb(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenFileFromWeb
        FileDownloader dialogoAbrirWeb = new FileDownloader(this, true);
	dialogoAbrirWeb.setVisible(true);
    }//GEN-LAST:event_menuOpenFileFromWeb

    
    /* Sair do Aplicativo - Menu */
    private void menuExit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExit
        dispose();
    }//GEN-LAST:event_menuExit

    
    /* Fechar Arquivo Aberto no momento - Menu */
    private void menuCloseFile(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCloseFile
	FileClose dialogFecharArquivo = new FileClose(this, true);
	dialogFecharArquivo.setVisible(true);
	labelFileCounts.setText("");
        labelFileSize.setText("");
	labelFileProcessingTime.setText("");
    }//GEN-LAST:event_menuCloseFile

    
    /* Conectar ao Banco de Dados - Menu */
    private void menuConnectToDB(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuConnectToDB
        databaseConfig = new DatabaseConnector(this, true);
	databaseConfig.setVisible(true);
    }//GEN-LAST:event_menuConnectToDB

    
    /* Encerrar Conexão com o Banco - Botão Especial */
    private void buttonForceCloseConnection(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonForceCloseConnection
	forceCloseConnection();
    }//GEN-LAST:event_buttonForceCloseConnection

    
    /* Enviar Arquivo para o Banco de Dados - Menu */
    private void menuUploadToDB(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuUploadToDB
        DatabaseUploader dialogoUpload = new DatabaseUploader(this, true);
	dialogoUpload.setVisible(true);
    }//GEN-LAST:event_menuUploadToDB

    
    /* Informações do CSV - Menu */
    private void menuFileInfo(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileInfo
        FileInfo dialogoInfoCSV = new FileInfo(this, true);
	dialogoInfoCSV.setVisible(true);
    }//GEN-LAST:event_menuFileInfo

    
    /* Filtrar fileData do Arquivo - Menu */
    private void menuNewQuery(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNewQuery
	if (connectionManager.activeConnection == true) {
	    QueryManager dialogFiltragemDados = new QueryManager(this, true);
	    dialogFiltragemDados.setVisible(true);
	} else {
	    JOptionPane.showMessageDialog(this, "A filtragem de dados requer conexão com um banco de dados e não há uma conexão no momento. Conecte-se a um banco de dados e tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
	}
    }//GEN-LAST:event_menuNewQuery

    
    /* About - Menu */
    private void menuAbout(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAbout
        About dialogSobre = new About(this, true);
	dialogSobre.setVisible(true);
    }//GEN-LAST:event_menuAbout

    private void menuKeyboardShortcuts(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuKeyboardShortcuts
        KeyboardShortcuts dialogAtalhos = new KeyboardShortcuts(this, true);
	dialogAtalhos.setVisible(true);
    }//GEN-LAST:event_menuKeyboardShortcuts

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonForceCloseConnection;
    private javax.swing.JButton buttonViewFileContent;
    private javax.swing.JMenuItem closeFileMenuItem;
    private javax.swing.JPopupMenu contextMenu;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JToolBar inferiorPanel;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    public javax.swing.JLabel labelConnectionStatus;
    public javax.swing.JLabel labelFileCounts;
    public javax.swing.JLabel labelFileProcessingTime;
    public javax.swing.JLabel labelFileSize;
    private javax.swing.JMenu mainMenuFile;
    private javax.swing.JMenu mainMenuHelp;
    private javax.swing.JMenu mainMenuTools;
    private javax.swing.JMenuItem menuAbout;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem menuCloseFile;
    private javax.swing.JMenuItem menuConnectToDB;
    private javax.swing.JMenuItem menuExit;
    private javax.swing.JMenuItem menuFileInfo;
    private javax.swing.JMenuItem menuKeyboardShortcuts;
    private javax.swing.JMenuItem menuNewQuery;
    private javax.swing.JMenuItem menuOpenFileFromWeb;
    private javax.swing.JMenuItem menuOpenLocalFile;
    private javax.swing.JMenuItem menuUploadToDB;
    private javax.swing.JLabel newConsole;
    private javax.swing.JPanel superiorPanel;
    private javax.swing.JTable tableFileContent;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.Box.Filler toolbarFiller;
    private javax.swing.JMenuItem uploadFileMenuItem;
    // End of variables declaration//GEN-END:variables
}
