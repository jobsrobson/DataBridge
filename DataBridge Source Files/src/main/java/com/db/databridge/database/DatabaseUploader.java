package com.db.databridge.database;

import com.db.databridge.util.ConnectionUtil;
import com.db.databridge.window.Home;
import com.db.databridge.util.UserSettings;
import com.db.databridge.util.FileUtil;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class DatabaseUploader extends javax.swing.JDialog {
    
    private final Home main;
    private final ConnectionUtil connectionManager;
    private final DatabaseConnector databaseConfig;
    UserSettings userSettings = new UserSettings();	
    
    private final CardLayout cardLayout;
    private final List<String> cardNames;
    private int currentIndex;
    boolean dadosEnviados = false;
    public File arquivoCSV = new File("csvImportado.csv");
    
    
    public DatabaseUploader(java.awt.Frame parent, boolean modal) {
	super(parent, modal);
	initComponents();
	
	/* Instâncias */
	main = (Home) parent;
        connectionManager = main.connectionManager;
	databaseConfig = main.databaseConfig;
	
	/* Definições gerais */
	cardLayout = (CardLayout) mainPanel.getLayout();
        cardNames = new ArrayList<>();
        cardNames.add("panelOne");
        cardNames.add("panelTwo");
        cardNames.add("panelThree");
        currentIndex = 0;
	
/**/	int quantLinhas = FileUtil.getFileRowCount(arquivoCSV);
	labelRowCount.setText(Integer.toString(quantLinhas));
	var tamanhoArquivo = FileUtil.getFileSize(arquivoCSV);
	labelFileSize.setText(tamanhoArquivo);
	
	visualizarArquivoNoRevisaoConsole("csvImportado.csv", 10);
	
	buttonNext.setEnabled(true);
	buttonBack.setEnabled(false);
	
	updateTextFieldFromDatabaseConfig();
	
        populateSchemasComboBox();
	
	
	/* Obtém os nomes das colunas do arquivo CSV e exibe na TextArea */
	List<String> nomesColunas = FileUtil.getFileColumnNames(arquivoCSV);
	StringBuilder textoNomeColuna = new StringBuilder();
	for (String nomeColuna : nomesColunas) {
	    textoNomeColuna.append(nomeColuna).append("\n");
	}
	
	textareaViewColumns.setText(textoNomeColuna.toString());
	
	// Preenche o combo_PrimaryKeyColumn com os nomes das colunas
	List<String> columnNames = FileUtil.getFileColumnNames(arquivoCSV);
	comboSelectPK.addItem("Definir Chave Primária automaticamente");
        for (String columnName : columnNames) {
            comboSelectPK.addItem(columnName);
	    
        }
	
	
	
	
/* OUVINTES DE EVENTOS - Registram em UserSettings os valores escolhidos pelo usuário */
	
	/* Card 1: Ouvinte de Eventos do ComboBox 'separadorDados' */
	comboSeparatorSelection.addActionListener((var e) -> {
	    String selectedOption = comboSeparatorSelection.getSelectedItem().toString();
	    userSettings.setSelectedSeparator(selectedOption);

	    // Verifique se a opção selecionada é "Definir automaticamente"
	    if (selectedOption.equals("Definir automaticamente")) {
		// Chame a função setSeparadorArquivo() para obter o separador
		String separadorSelecionado = FileUtil.determineFileSeparator(arquivoCSV);
		// Atribua o separador detectado à variável selectedSeparator
		userSettings.setSelectedSeparator(separadorSelecionado);
		atualiza_textarea_VisualizarColunas(separadorSelecionado);
	    } else {
		atualiza_textarea_VisualizarColunas(selectedOption);
	    }
	});
	
	
	/* Card 2: Ouvinte de Eventos do Combobox 'comboBoxSchemas' */
        comboSelectSchema.addActionListener((ActionEvent e) -> {
	    String selectedSchema = (String) comboSelectSchema.getSelectedItem(); /* Obtém a seleção do usuário na ComboBox */
	    userSettings.setSelectedSchema(selectedSchema); /* Atualiza a escolha do usuário na classe UserSettings */
	    updateSchemaLabel();
	});
	
	/* Card 2: Ouvinte de Eventos do Combobox 'combo_PrimaryKeyColumn' */
	comboSelectPK.addActionListener((ActionEvent e) -> {
	   String chavePrimaria = comboSelectPK.getSelectedItem().toString();
	   userSettings.setPrimaryKey(chavePrimaria);
	   updateChavePrimariaLabel();
	   
	});
	
	/* Card 2: Ouvinte de Eventos do TextField 'campo_NomeTabela' */
	buttonValidateTableName.addActionListener((java.awt.event.ActionEvent evt) -> {

	    String tableName = textTableName.getText();
	    
	    // Verifica se o nome da tabela atende às restrições (apenas letras minúsculas, números e underscores)
	    if (validateTableName(tableName)) {
		userSettings.setTableName(tableName);
		updateTableNameLabel();
		labelError.setText("");
		labelValid.setText("O nome da tabela é válido.");
	    } else {
		labelValid.setText("");
		labelError.setText("Nome inválido! Use apenas letras minúsculas, números e underscores.");
	    }
	});

    }
    

    
/* MÉTODOS GERAIS */
    
    /* Card 1: Função para atualizar o conteúdo da TextArea com base na seleção do ComboBox */
    private void atualiza_textarea_VisualizarColunas(String selectedSeparator) {
	
	String selectedOption = comboSeparatorSelection.getSelectedItem().toString();

	switch (selectedOption) {
	    case "Definir automaticamente" -> /* Usa a função setSeparadorArquivo */
		selectedSeparator = FileUtil.determineFileSeparator(arquivoCSV);
	    case "Vírgula" -> selectedSeparator = ",";
	    case "Ponto-e-Vírgula ( ; )" -> selectedSeparator = ";";
	    case "Tabulação (Tab)" -> selectedSeparator = "\t";
	    default -> {
	    }
	}

	/* Obtém os nomes das colunas com base no separador selecionado */
	List<String> columnNames = FileUtil.getFileColumnNamesWithSeparator(arquivoCSV, selectedSeparator);

	/* Atualiza a textArea com os nomes das colunas */
	StringBuilder textoNomesColunas = new StringBuilder();
	for (String columnName : columnNames) {
	    textoNomesColunas.append(columnName).append("\n");
	}
	
	textareaViewColumns.setText(textoNomesColunas.toString());
	// Conta as linhas no textarea_VisualizarColunas
	int numberOfLines = countLinesInTextArea(textareaViewColumns);
	labelColumnCount.setText(Integer.toString(numberOfLines));
    }
    
    public final int countLinesInTextArea(JTextArea textarea_VisualizarColunas) {
	String text = textarea_VisualizarColunas.getText();
	String[] lines = text.split("\n");
	return lines.length;
    }
    
    
    // Card 2: Método para preencher o TextField com o valor de "databaseAtivo"
    private void updateTextFieldFromDatabaseConfig() {
        if (databaseConfig != null) {
            textDatabaseName.setText(databaseConfig.databaseAtivo);
        }
    }
    
    /* Card 2: Função para popular o combobox 'comboBoxSchemas' com os schemas existentes no banco de dados */
    private void populateSchemasComboBox() {
	// Obtém a lista de schemas do ConnectionUtil
	List<String> schemas = connectionManager.getSchemas();

	// Adicione a opção "Selecione uma opção..." como a primeira entrada
	schemas.add(0, "Selecione uma opção...");

	// Preenche o ComboBox com os schemas
	DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
	for (String schema : schemas) {
	    model.addElement(schema);
	}
	comboSelectSchema.setModel(model);

	// Selecione a opção "Selecione uma opção..." por padrão
	comboSelectSchema.setSelectedIndex(0);
    }
    
    /* Card 2: Função para validar o nome da tabela digitado com uma expressão regular */
    private boolean validateTableName(String tableName) {
        return tableName.matches("^[a-z0-9_]+$"); // A expressão regular permite apenas letras minúsculas, números e underscores
    }

    
    /* Card 3: Função para atualizar o label com o nome da tabela */
    public void updateTableNameLabel() {
        label_nomeTabela.setText(userSettings.getTableName());
    }
    
    /* Card 3: Função para atualizar o label com o Schema */
    public void updateSchemaLabel() {
	label_nomeSchema.setText(userSettings.getSelectedSchema());
    }
    
    /* Card 3: Função para atualizar o label com o Separador */
    public void updateChavePrimariaLabel() {
	label_primaryKey.setText(userSettings.getPrimaryKey());
    }
    
    /* Card 3: Enviar os dados para o banco de dados */
    public final void visualizarArquivoNoRevisaoConsole(String fileName, int numLinhas) {
	try {
	    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
		String linha;
		StringBuilder conteudoCSV = new StringBuilder();
		
		int linhasLidas = 0;
		boolean primeiraLinha = true;
		while ((linha = reader.readLine()) != null && linhasLidas < numLinhas) {
		    if (primeiraLinha) {
			// Converter a primeira linha em minúsculas
			linha = linha.toLowerCase();
			primeiraLinha = false;
		    }
		    conteudoCSV.append(linha).append("\n");
		    linhasLidas++;
		}
		
		RevisaoConsole.setText(conteudoCSV.toString());
	    }

	} catch (FileNotFoundException e) {
	    RevisaoConsole.setText("Erro: arquivo não encontrado.");
	} catch (IOException e) {
	    System.err.println("Falha ao visualizar o CSV no Console.");
	}
    }
    
    /* Compartilhável: Define se os dados foram enviados com sucesso para o banco de dados */
    public boolean isDadosEnviados() {
	return dadosEnviados;
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        oneCard = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        labelColumnNamesSettings = new javax.swing.JLabel();
        radioUseColumnNamesFromFile = new javax.swing.JRadioButton();
        radioUseCustomColumnNames = new javax.swing.JRadioButton();
        jPanel10 = new javax.swing.JPanel();
        labelTitle6 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        labelSelectSeparator = new javax.swing.JLabel();
        comboSeparatorSelection = new javax.swing.JComboBox<>();
        labelSeparatorSelectionHint = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textareaViewColumns = new javax.swing.JTextArea();
        twoCard = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        labelTitle2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        labelDatabaseName = new javax.swing.JLabel();
        textDatabaseName = new javax.swing.JTextField();
        labelSelectSchema = new javax.swing.JLabel();
        comboSelectSchema = new javax.swing.JComboBox<>();
        labelSchemaHint = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        labelSelectPK = new javax.swing.JLabel();
        comboSelectPK = new javax.swing.JComboBox<>();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        labelChooseTableName = new javax.swing.JLabel();
        textTableName = new javax.swing.JTextField();
        buttonValidateTableName = new javax.swing.JButton();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        labelValid = new javax.swing.JLabel();
        labelError = new javax.swing.JLabel();
        threeCard = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        labelTitle7 = new javax.swing.JLabel();
        enviarDados_Final = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        label_nomeSchema = new javax.swing.JLabel();
        label_primaryKey = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        label_nomeTabela = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        labelFileSize = new javax.swing.JLabel();
        labelColumnCount = new javax.swing.JLabel();
        labelRowCount = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        RevisaoConsole = new javax.swing.JTextArea();
        jTextArea3 = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        buttonNext = new javax.swing.JButton();
        buttonBack = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Enviar para Banco de Dados");
        setResizable(false);

        mainPanel.setLayout(new java.awt.CardLayout());

        oneCard.setPreferredSize(new java.awt.Dimension(677, 496));

        labelColumnNamesSettings.setText("Nomes das Colunas:");

        radioUseColumnNamesFromFile.setSelected(true);
        radioUseColumnNamesFromFile.setText("Usar os nomes definidos pelo arquivo");
        radioUseColumnNamesFromFile.setEnabled(false);

        radioUseCustomColumnNames.setText("Definir manualmente");
        radioUseCustomColumnNames.setEnabled(false);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelColumnNamesSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radioUseColumnNamesFromFile, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(radioUseCustomColumnNames, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelColumnNamesSettings)
                    .addComponent(radioUseColumnNamesFromFile))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioUseCustomColumnNames)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        labelTitle6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        labelTitle6.setText("1. Configuração dos Dados");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitle6)
                .addContainerGap(113, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(labelTitle6)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        labelSelectSeparator.setText("Separador dos Dados:");

        comboSeparatorSelection.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione uma opção...", "Vírgula", "Ponto-e-Vírgula ( ; )", "Tabulação (Tab)", "Definir automaticamente" }));

        labelSeparatorSelectionHint.setForeground(new java.awt.Color(102, 102, 102));
        labelSeparatorSelectionHint.setText("<html>Defina o separador dos dados contidos no arquivo.");
        labelSeparatorSelectionHint.setFocusable(false);
        labelSeparatorSelectionHint.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelSelectSeparator)
                .addGap(24, 24, 24)
                .addComponent(comboSeparatorSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelSeparatorSelectionHint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(123, 123, 123))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelSelectSeparator)
                    .addComponent(comboSeparatorSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelSeparatorSelectionHint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Visualizar Colunas"));

        jScrollPane1.setBorder(null);

        textareaViewColumns.setColumns(20);
        textareaViewColumns.setForeground(new java.awt.Color(0, 204, 0));
        textareaViewColumns.setLineWrap(true);
        textareaViewColumns.setRows(5);
        textareaViewColumns.setWrapStyleWord(true);
        textareaViewColumns.setBorder(null);
        textareaViewColumns.setEnabled(false);
        textareaViewColumns.setFocusable(false);
        textareaViewColumns.setOpaque(false);
        jScrollPane1.setViewportView(textareaViewColumns);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout oneCardLayout = new javax.swing.GroupLayout(oneCard);
        oneCard.setLayout(oneCardLayout);
        oneCardLayout.setHorizontalGroup(
            oneCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(oneCardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(oneCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(oneCardLayout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, oneCardLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        oneCardLayout.setVerticalGroup(
            oneCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(oneCardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainPanel.add(oneCard, "panel0");

        labelTitle2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        labelTitle2.setText("2. Configuração do Banco de Dados");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitle2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(labelTitle2)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        labelDatabaseName.setText("Database:");

        textDatabaseName.setEnabled(false);
        textDatabaseName.setFocusable(false);
        textDatabaseName.setOpaque(true);

        labelSelectSchema.setText("Schema:");

        comboSelectSchema.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione uma opção..." }));

        labelSchemaHint.setForeground(new java.awt.Color(102, 102, 102));
        labelSchemaHint.setText("Selecione o Schema que será usado para criar a nova tabela.");
        labelSchemaHint.setFocusable(false);
        labelSchemaHint.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(labelSchemaHint))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labelDatabaseName)
                        .addGap(73, 73, 73)
                        .addComponent(textDatabaseName, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labelSelectSchema)
                        .addGap(82, 82, 82)
                        .addComponent(comboSelectSchema, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelDatabaseName)
                    .addComponent(textDatabaseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelSelectSchema)
                    .addComponent(comboSelectSchema, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelSchemaHint)
                .addContainerGap())
        );

        labelSelectPK.setText("Chave Primária:");

        comboSelectPK.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione uma opção..." }));
        comboSelectPK.setToolTipText("<html>A definição de uma Chave Primária pode não estar disponível em todos os bancos de dados.");

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("Selecione a coluna que será usada como Chave Primária (PK) na tabela. A coluna selecionada como PK não pode conter valores nulos e também não pode possuir valores duplicados. Você também pode optar por não selecionar uma Chave Primária agora.");
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setBorder(null);
        jTextArea1.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        jTextArea1.setEnabled(false);
        jTextArea1.setFocusable(false);
        jTextArea1.setOpaque(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(labelSelectPK, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(comboSelectPK, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboSelectPK, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSelectPK))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        labelChooseTableName.setText("Nome da Tabela:");

        textTableName.setToolTipText("<html>O nome da tabela só pode conter caracteres minúsculos e underscore.");

        buttonValidateTableName.setText("Validar");

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(2);
        jTextArea2.setText("Digite o nome da tabela a ser criada.\nValide o nome para confirmá-lo.");
        jTextArea2.setWrapStyleWord(true);
        jTextArea2.setBorder(null);
        jTextArea2.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        jTextArea2.setEnabled(false);
        jTextArea2.setFocusable(false);
        jTextArea2.setOpaque(false);

        jPanel6.setLayout(new javax.swing.OverlayLayout(jPanel6));

        labelValid.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelValid.setForeground(new java.awt.Color(51, 153, 0));
        labelValid.setToolTipText("");
        labelValid.setAlignmentY(0.0F);
        jPanel6.add(labelValid);

        labelError.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelError.setForeground(new java.awt.Color(255, 0, 51));
        labelError.setToolTipText("");
        labelError.setAlignmentY(0.0F);
        jPanel6.add(labelError);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelChooseTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(textTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonValidateTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextArea2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(17, 17, 17))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textTableName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelChooseTableName)
                    .addComponent(buttonValidateTableName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextArea2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout twoCardLayout = new javax.swing.GroupLayout(twoCard);
        twoCard.setLayout(twoCardLayout);
        twoCardLayout.setHorizontalGroup(
            twoCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(twoCardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(twoCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(twoCardLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1, Short.MAX_VALUE))
                    .addGroup(twoCardLayout.createSequentialGroup()
                        .addGroup(twoCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(twoCardLayout.createSequentialGroup()
                                .addGroup(twoCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(twoCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        twoCardLayout.setVerticalGroup(
            twoCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(twoCardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainPanel.add(twoCard, "panel1");

        labelTitle7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        labelTitle7.setText("3. Revisão Final");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitle7)
                .addContainerGap(250, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(labelTitle7)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        enviarDados_Final.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        enviarDados_Final.setText("Enviar Dados");
        enviarDados_Final.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enviarDados_FinalActionPerformed(evt);
            }
        });

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Configurações da Tabela"));

        jLabel22.setText("Schema:");

        label_nomeSchema.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        label_nomeSchema.setForeground(new java.awt.Color(102, 153, 255));
        label_nomeSchema.setText("Selecione Schema");

        label_primaryKey.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        label_primaryKey.setForeground(new java.awt.Color(102, 153, 255));
        label_primaryKey.setText("Selecione Chave Primária");

        jLabel21.setText("PK:");

        jLabel23.setText("Tabela:");

        label_nomeTabela.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        label_nomeTabela.setForeground(new java.awt.Color(102, 153, 255));
        label_nomeTabela.setText("Preencha Nome da Tabela");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23))
                .addGap(29, 29, 29)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_primaryKey)
                    .addComponent(label_nomeSchema)
                    .addComponent(label_nomeTabela))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(label_nomeSchema))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(label_primaryKey))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(label_nomeTabela))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Configurações dos Dados"));

        jLabel18.setText("Linhas:");

        jLabel19.setText("Colunas:");

        jLabel20.setText("Tamanho do upload:");

        labelFileSize.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelFileSize.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelFileSize.setText("NaN");

        labelColumnCount.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelColumnCount.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelColumnCount.setText("NaN");

        labelRowCount.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelRowCount.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelRowCount.setText("NaN");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(labelRowCount)
                    .addComponent(labelFileSize)
                    .addComponent(labelColumnCount))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(labelRowCount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(labelColumnCount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(labelFileSize))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Prévia do conteúdo da tabela"));

        jScrollPane2.setBorder(null);

        RevisaoConsole.setEditable(false);
        RevisaoConsole.setColumns(20);
        RevisaoConsole.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        RevisaoConsole.setForeground(new java.awt.Color(102, 153, 255));
        RevisaoConsole.setRows(5);
        RevisaoConsole.setFocusable(false);
        jScrollPane2.setViewportView(RevisaoConsole);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel17Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 175, Short.MAX_VALUE)
            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel17Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTextArea3.setEditable(false);
        jTextArea3.setColumns(20);
        jTextArea3.setLineWrap(true);
        jTextArea3.setRows(5);
        jTextArea3.setText("Antes de enviar os dados para o banco de dados, certifique-se de que as todas as configurações foram realizadas. O processo de upload poderá durar vários minutos e sua interrupção pode ocasionar em dados corrompidos.");
        jTextArea3.setWrapStyleWord(true);
        jTextArea3.setBorder(null);
        jTextArea3.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        jTextArea3.setEnabled(false);
        jTextArea3.setFocusable(false);
        jTextArea3.setOpaque(false);

        javax.swing.GroupLayout threeCardLayout = new javax.swing.GroupLayout(threeCard);
        threeCard.setLayout(threeCardLayout);
        threeCardLayout.setHorizontalGroup(
            threeCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(threeCardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(threeCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, threeCardLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jTextArea3, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addComponent(enviarDados_Final, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(threeCardLayout.createSequentialGroup()
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(threeCardLayout.createSequentialGroup()
                        .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        threeCardLayout.setVerticalGroup(
            threeCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(threeCardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(threeCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(threeCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(threeCardLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jTextArea3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(threeCardLayout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(enviarDados_Final, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );

        mainPanel.add(threeCard, "panel2");

        buttonNext.setText("Próximo");
        buttonNext.setFocusCycleRoot(true);
        buttonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNextActionPerformed(evt);
            }
        });

        buttonBack.setText("Anterior");
        buttonBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBackActionPerformed(evt);
            }
        });

        buttonCancel.setText("Cancelar");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 197, Short.MAX_VALUE)
                .addComponent(buttonBack, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonNext, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonNext)
                    .addComponent(buttonBack)
                    .addComponent(buttonCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    
    
    private void buttonNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNextActionPerformed
        cardLayout.next(mainPanel);
	if (currentIndex < cardNames.size() - 1) {
            currentIndex++;
            cardLayout.show(mainPanel, cardNames.get(currentIndex));
            buttonBack.setEnabled(true);
        }
        if (currentIndex == cardNames.size() - 1) {
            buttonNext.setEnabled(false);
        }
    }//GEN-LAST:event_buttonNextActionPerformed

    private void buttonBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBackActionPerformed
        cardLayout.previous(mainPanel);
	if (currentIndex > 0) {
            currentIndex--;
            cardLayout.show(mainPanel, cardNames.get(currentIndex));
            buttonNext.setEnabled(true);
        }
        if (currentIndex == 0) {
            buttonBack.setEnabled(false);
        }
    }//GEN-LAST:event_buttonBackActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        dispose();
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void enviarDados_FinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enviarDados_FinalActionPerformed
	sendFileToDatabase();
    }//GEN-LAST:event_enviarDados_FinalActionPerformed

    
    
    
    
/* MÉTODOS PARA ENVIO DOS DADOS PARA O BANCO DE DADOS */
    
    private void sendFileToDatabase() {
	// Verifique se as configurações do usuário estão definidas
	if (userSettings.getSelectedSeparator() == null || userSettings.getSelectedSchema() == null || userSettings.getTableName() == null) {
	    JOptionPane.showMessageDialog(this, "Certifique-se de configurar todas as opções antes de enviar para o banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
	    return;
	}

	// Crie um JDialog para o diálogo de progresso
	JDialog progressDialog = new JDialog(this, "Enviando...", true);
	progressDialog.setResizable(false);

	// Crie e configure uma JProgressBar
	JProgressBar progressBar = new JProgressBar(0, 100);
	progressBar.setStringPainted(true);
	progressDialog.add(progressBar, BorderLayout.CENTER);

	progressDialog.pack();
	progressDialog.setLocationRelativeTo(this);

	// Crie um SwingWorker para realizar o envio de dados em segundo plano
	SwingWorker<Void, Integer> sendDataWorker = new SwingWorker<Void, Integer>() {
	    @Override
	    protected Void doInBackground() {
		int progress = 0; // Defina o progresso inicial
		try {
		    // Obtenha o caminho absoluto para o arquivo CSV
		    String caminhoAbsoluto = "csvImportado.csv"; // Substitua pelo caminho real do arquivo CSV
		    String separador = userSettings.getSelectedSeparator();
		    
		    // Crie um leitor CSV com o separador selecionado
		    CSVReader csvReader = new CSVReaderBuilder(new FileReader(caminhoAbsoluto))
			    .withCSVParser(new CSVParserBuilder().withSeparator(separador.charAt(0)).build())
			    .build();

		    String[] colunas = csvReader.readNext();

		    if (colunas != null) {
			// Converter as colunas para minúscula, para evitar erros
			for (int i = 0; i < colunas.length; i++) {
			    colunas[i] = colunas[i].toLowerCase();
			}
			// Crie a tabela no banco de dados
			generateNewTableSQL(colunas);

			// Obtenha o número total de linhas do CSV para calcular o progresso
/**/			int totalLinhas = FileUtil.getFileRowCount(arquivoCSV);

			String insertSQL = generateFileContentSQL(colunas);

			// Crie um objeto PreparedStatement para executar a inserção
			PreparedStatement preparedStatement = connectionManager.connection.prepareStatement(insertSQL);

			String[] linha;
			int linhaAtual = 0;
			try {
			    while ((linha = csvReader.readNext()) != null) {
				for (int i = 0; i < linha.length; i++) {
				    preparedStatement.setString(i + 1, linha[i]);
				}
				preparedStatement.executeUpdate();

				linhaAtual++;
				// Calcule o progresso com base no número de linhas processadas
				progress = (int) (((double) linhaAtual / totalLinhas) * 100);
				publish(progress); // Publique o progresso para a atualização da barra de progresso
			    }
			} catch (CsvValidationException ex) {
			    System.out.println("ERRO: Falha no carregamento dos dados. 1 " + ex);
			}

			// Feche o PreparedStatement
			preparedStatement.close();
		    } else {
			JOptionPane.showMessageDialog(DatabaseUploader.this, "O arquivo CSV não contém colunas.", "Erro", JOptionPane.ERROR_MESSAGE);
		    }
		} catch (IOException | SQLException e) {
		    JOptionPane.showMessageDialog(DatabaseUploader.this, "Erro ao enviar dados para o banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		} catch (CsvValidationException ex) {
		    Logger.getLogger(DatabaseUploader.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	    }

	    @Override
	    protected void done() {
		// Feche o diálogo de progresso quando a tarefa estiver concluída
		progressDialog.dispose();
		JOptionPane.showMessageDialog(DatabaseUploader.this, "Dados enviados com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		dadosEnviados = true;
		dispose();
	    }

	    @Override
	    protected void process(List<Integer> chunks) {
		// Atualize a barra de progresso à medida que os chunks são publicados
		for (Integer progress : chunks) {
		     SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
		}
	    }
	};

	// Inicie o SwingWorker para executar o envio de dados em segundo plano
	sendDataWorker.execute();

	progressDialog.setVisible(true); // Mostre o diálogo de progresso
    }

    

    
    private String generateFileContentSQL(String[] colunas) {
	StringBuilder insertSQL = new StringBuilder("INSERT INTO ");
	insertSQL.append("\"").append(userSettings.getSelectedSchema()).append("\"");
	insertSQL.append(".");
	insertSQL.append("\"").append(userSettings.getTableName()).append("\"");
	insertSQL.append(" (");

	// Lógica para a chave primária
	String primaryKey = userSettings.getPrimaryKey();
	if (primaryKey.equals("Definir Chave Primária automaticamente")) {
	    insertSQL.append("\"ID\", ");
	} else {
	    insertSQL.append("\"").append(primaryKey).append("\", ");
	}

	// Adiciona as colunas restantes
	insertSQL.append(String.join(", ", Arrays.stream(colunas).map(c -> "\"" + c + "\"").toArray(String[]::new)));
	insertSQL.append(") VALUES (");

	// Adiciona a lógica para a chave primária
	if (primaryKey.equals("Definir Chave Primária automaticamente")) {
	    insertSQL.append("DEFAULT, ");
	} else {
	    insertSQL.append("?, ");
	}

	for (int i = 0; i < colunas.length; i++) {
	    insertSQL.append("?");
	    if (i < colunas.length - 1) {
		insertSQL.append(", ");
	    }
	}

	insertSQL.append(")");
	System.out.println(insertSQL.toString());
	return insertSQL.toString();
    }
    
    
    private void generateNewTableSQL(String[] colunas) throws SQLException {
	try {
	    // Execute a instrução SQL para criar a tabela
	    StringBuilder createTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
	    createTableSQL.append(userSettings.getSelectedSchema()); // Use o schema selecionado
	    createTableSQL.append(".");
	    createTableSQL.append(userSettings.getTableName()); // Use o nome da tabela definido pelo usuário
	    createTableSQL.append(" (");

	    // Adiciona a lógica para a chave primária
	    String primaryKey = userSettings.getPrimaryKey();
	    if (primaryKey.equals("Definir Chave Primária automaticamente")) {
		// Gera uma nova coluna e define como chave primária
		createTableSQL.append("\"ID\" SERIAL PRIMARY KEY,");
	    } else {
		// Usa a chave primária escolhida pelo usuário
		createTableSQL.append("\"").append(primaryKey).append("\" VARCHAR PRIMARY KEY,");
	    }

	    // Adiciona as colunas restantes
	    for (int i = 0; i < colunas.length; i++) {
		String columnName = colunas[i].trim();
		String dataType = "VARCHAR"; // Define todos os tipos como VARCHAR - MELHORIA FUTURA

		if (!columnName.equals(primaryKey)) {
		    createTableSQL.append("\"").append(columnName).append("\"").append(" ").append(dataType);

		    if (i < colunas.length - 1) {
			createTableSQL.append(", ");
		    }
		}
	    }

	    createTableSQL.append(")");

	    try (Statement statement = connectionManager.connection.createStatement()) {
		statement.executeUpdate(createTableSQL.toString());
	    }
	} catch (SQLException e) {
	    System.out.println("ERRO: Falha na criação da tabela. " + e);
	}
    }
    
    /* **************************************** */
    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea RevisaoConsole;
    private javax.swing.JButton buttonBack;
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonNext;
    private javax.swing.JButton buttonValidateTableName;
    private javax.swing.JComboBox<String> comboSelectPK;
    private javax.swing.JComboBox<String> comboSelectSchema;
    private javax.swing.JComboBox<String> comboSeparatorSelection;
    private javax.swing.JButton enviarDados_Final;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JLabel labelChooseTableName;
    private javax.swing.JLabel labelColumnCount;
    private javax.swing.JLabel labelColumnNamesSettings;
    private javax.swing.JLabel labelDatabaseName;
    private javax.swing.JLabel labelError;
    private javax.swing.JLabel labelFileSize;
    private javax.swing.JLabel labelRowCount;
    private javax.swing.JLabel labelSchemaHint;
    private javax.swing.JLabel labelSelectPK;
    private javax.swing.JLabel labelSelectSchema;
    private javax.swing.JLabel labelSelectSeparator;
    private javax.swing.JLabel labelSeparatorSelectionHint;
    private javax.swing.JLabel labelTitle2;
    private javax.swing.JLabel labelTitle6;
    private javax.swing.JLabel labelTitle7;
    private javax.swing.JLabel labelValid;
    private javax.swing.JLabel label_nomeSchema;
    private javax.swing.JLabel label_nomeTabela;
    private javax.swing.JLabel label_primaryKey;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel oneCard;
    private javax.swing.JRadioButton radioUseColumnNamesFromFile;
    private javax.swing.JRadioButton radioUseCustomColumnNames;
    private javax.swing.JTextField textDatabaseName;
    private javax.swing.JTextField textTableName;
    private javax.swing.JTextArea textareaViewColumns;
    private javax.swing.JPanel threeCard;
    private javax.swing.JPanel twoCard;
    // End of variables declaration//GEN-END:variables
}
