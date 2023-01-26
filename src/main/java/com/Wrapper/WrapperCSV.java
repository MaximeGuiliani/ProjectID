package com.Wrapper;

import com.Database.Database;
// modification à effectuer pour l'importation :
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
//import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.util.Arrays;

/**
 * Wrapper CSV vers SQL
 */
public class WrapperCSV extends Wrapper {
    /**
     * Constructeur
     * @param dbName    nom de la base de données
     * @param tableName nom de la table
     * @param filePath  chemin vers le fichier
     * @param delimiter  délimiteur
     * @param debuggingMode mode de débogage
     */
    public WrapperCSV(String dbName, String tableName, String filePath, char delimiter, boolean debuggingMode) {
        super(dbName, tableName, filePath, debuggingMode);
        readFile(filePath, delimiter + "");
        String query = convertToSQL();
        if (debuggingMode) System.out.println(" Mise à jour de la BDD " + dbName + "...");
        Database.updateDB(dbName, query);
        if (debuggingMode) System.out.println(" Intégration terminée.\n");
    }

    @Override
    protected void readFile(String filePath, String... args) {
        // Vérification des arguments
        if (args.length != 1 || args[0].length() > 1) throw new IllegalArgumentException("Arguments invalides.");
        if (debuggingMode) System.out.println(" Lecture du fichier : " + filePath + "...");
        CSVParser parser = new CSVParserBuilder().withSeparator(args[0].charAt(0)).build();
        Reader reader = null;
        if (filePath.lastIndexOf("/") > 0) {
            try {
                reader = new FileReader(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            InputStream input = WrapperCSV.class.getClassLoader().getResourceAsStream(filePath);
            // if (input != null) reader = new InputStreamReader(input);
            // else  throw new FileNotFoundException("Arguments invalides.");
        }
        if (reader != null) {
            CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build();
            try {
                datas = csvReader.readAll();
            } catch (IOException  //| CsvException 
            e
            ) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected String convertToSQL() {
        if (debuggingMode) System.out.println(" Création de la requête SQL.");
        StringBuilder sb = new StringBuilder();
        // Suppression et création table
        sb.append("DROP TABLE IF EXISTS \"" + tableName + "\";\n");
        sb.append("CREATE TABLE \"" + tableName + "\" (\n");

        // Récupération des attributs
        attributes = Arrays.asList(datas.get(0));
        datas.remove(0);
        sb.append(attributes.get(0) + " " + getAttributeType(attributes.get(0)) + " PRIMARY KEY");
        for (int i = 1; i < attributes.size(); i++)
            sb.append(",\n" + attributes.get(i) + " " + getAttributeType(attributes.get(i)));
        sb.append(");\n");

        // Préparation de la requête d'insertion
        StringBuilder beginInsert = new StringBuilder("INSERT INTO \"" + tableName + "\" (");
        for (String attribute : attributes)
            beginInsert.append(attribute + ",");
        beginInsert.setLength(beginInsert.length() - 1);
        beginInsert.append(") VALUES (");

        // Création des requêtes d'insertion
        for (String[] tuple : datas) {
            sb.append(beginInsert);
            for (String value : tuple)
                sb.append(value.isEmpty() ? "NULL," : "'" + value.replaceAll("'", "''") + "',");
            sb.setLength(sb.length() - 1);
            sb.append(");\n");
        }
        return sb.toString();
    }
}
