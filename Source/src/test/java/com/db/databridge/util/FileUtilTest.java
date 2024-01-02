package com.db.databridge.util;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class FileUtilTest {
    
    /* Teste para verificar se a contagem de dados ausentes em uma coluna está funcionando corretamente */
    @Test
    void testCountMissingDataInColumn() {
        // Teste para verificar se a contagem de dados ausentes em uma coluna funciona corretamente
        String filePath = "C:\\Users\\jobsr\\Downloads\\GitHub-Java\\java_csv_downloader\\databridge\\src\\test\\java\\com\\db\\databridge\\testFile.csv";
        File testFile = new File(filePath);

        int columnIndex = 1; // Índice da coluna a ser testada
        int expectedMissingDataCount = 2; // Número esperado de dados ausentes

        int actualMissingDataCount = FileUtil.countMissingDataInColumn(testFile, columnIndex);

        assertEquals(expectedMissingDataCount, actualMissingDataCount);
    }

    /* Teste para verificar se a determinação automática do tipo de dado em uma coluna está funcionando corretamente */
    @Test
    void testDetermineDataTypeInColumn() {
        String filePath = "C:\\Users\\jobsr\\Downloads\\GitHub-Java\\java_csv_downloader\\databridge\\src\\test\\java\\com\\db\\databridge\\testFile.csv";
        File testFile = new File(filePath);

        int columnIndex = 1; // Índice da coluna a ser testada
        String expectedDataType = "INTEGER"; // Tipo de dado esperado

        String actualDataType = FileUtil.determineDataTypeInColumn(testFile, columnIndex);

        assertEquals(expectedDataType, actualDataType);
    }

    /* Teste para verificar se a contagem de valores únicos em uma coluna está funcionando corretamente */
    @Test
    void testCountUniqueValuesInColumn() {
        String filePath = "C:\\Users\\jobsr\\Downloads\\GitHub-Java\\java_csv_downloader\\databridge\\src\\test\\java\\com\\db\\databridge\\testFile.csv";
        File testFile = new File(filePath);

        int columnIndex = 0; // Índice da coluna a ser testada
        int expectedUniqueValuesCount = 418; // Número esperado de valores únicos

        int actualUniqueValuesCount = FileUtil.countUniqueValuesInColumn(testFile, columnIndex);

        assertEquals(expectedUniqueValuesCount, actualUniqueValuesCount);
    }
    
    
    /* Teste para verificar se a contagem de registros totais em uma coluna está funcionando corretamente */
    @Test
    void testCountRecordsInColumn() {
        String filePath = "C:\\Users\\jobsr\\Downloads\\GitHub-Java\\java_csv_downloader\\databridge\\src\\test\\java\\com\\db\\databridge\\testFile.csv";
        File testFile = new File(filePath);

        int columnIndex = 0; // Índice da coluna a ser testada
        int expectedRecordCount = 418; // Número esperado de registros totais

        int actualRecordCount = FileUtil.countRecordsInColumn(testFile, columnIndex);

        assertEquals(expectedRecordCount, actualRecordCount);
    }
    
    
    /* Teste para verificar se o diálogo e a abertura de um arquivo local funcionam corretamente */
    @Test
    void testOpenLocalFile() {  
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir"))); 

        File selectedFile = FileUtil.openLocalFile(fileChooser);

        assertNotNull(selectedFile);
        assertTrue(selectedFile.isFile());
    }
    
    /* Teste para verificar se a cópia de um arquivo local para a pasta raiz do programa funciona corretamente */
    @Test
    void testCopyFile() {
        File sourceFile = new File("C:\\Users\\jobsr\\Downloads\\GitHub-Java\\java_csv_downloader\\databridge\\testFile.csv");
        File destinationFile = new File("testecsvImportado.csv");

        try {
            FileUtil.copyFile(sourceFile, destinationFile);
            assertTrue(destinationFile.exists());
        } catch (IOException e) {
            fail("Falha na cópia do arquivo: " + e.getMessage());
        }
    }
    
    /* Teste para validar se o download de um arquivo da web funciona corretamente */
    @Test
    void testDownloadFileFromWeb() {
        // Este teste assume que a URL fornecida é válida e contém um arquivo para download
        String url = "https://raw.githubusercontent.com/jobsrobson/General/main/Public%20Databases/Titanic%20-%20Kaggle/test.csv";
        JTextArea consoleWeb = new JTextArea();

        assertDoesNotThrow(() -> {
            FileUtil.downloadFileFromWeb(url, consoleWeb);
            assertTrue(new File("csvImportado.csv").exists());
        });
    }
    

}
