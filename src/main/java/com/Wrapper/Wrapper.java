package com.Wrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper SQL
 */
public abstract class Wrapper {
    /**
     * Mode de débogage
     */
    protected final boolean debuggingMode;

    /**
     * Chemin vers le fichier
     */
    protected String filePath;

    /**
     * Nom de la base de données
     */
    protected String jdbc;

    /**
     * Nom de la table
     */
    protected String tableName;

    /**
     * Liste des attributs
     */
    protected List<String> attributes;

    /**
     * Données du fichier
     */
    protected List<String[]> datas;

    /**
     * Constructeur
     *
     * @param _jdbc nom de la base de données
     * @param _tableName nom de la table
     * @param _filePath  chemin du fichier
     * @param _debuggingMode mode de débugage
     */
    protected Wrapper(String _jdbc, String _tableName, String _filePath, boolean _debuggingMode) {
        debuggingMode = _debuggingMode;
        filePath = _filePath;
        jdbc = _jdbc;
        tableName = _tableName;
        attributes = new ArrayList<>();
        datas = new ArrayList<>();
    }

    /**
     * Lire un fichier
     * @param filePath chemin du fichier
     * @param args arguments
     */
    protected abstract void readFile(String filePath, String... args);

    /**
     * Convertir les données d'un fichier en une requête SQL
     * @return requête SQL
     */
    protected abstract String convertToSQL();

    /**
     * Obtenir la liste des valeurs d'un attribut
     * @param attribute attribut
     * @return liste des valeurs
     */
    protected List<String> getAttributeData(String attribute) {

        int attributeIndex = attributes.indexOf(attribute);
        if (attributeIndex > -1) {
            List<String> data = new ArrayList<>();
            for (String[] value : datas) data.add(value[attributeIndex]);
            return data;
        }
        return null;
    }

    /**
     * Obtenir le type d'un attribut
     * @param attribute attribut
     * @return type de l'attribut
     */
    protected String getAttributeType(String attribute) {

        List<String> dataAttribute = getAttributeData(attribute);
        String attributeType = "TEXT";
        boolean onlyFloat = true;
        boolean onlyInteger = true;

        for (String string : dataAttribute) {
            if (!onlyFloat && !onlyInteger)
                break;
            if (onlyFloat && !string.matches("[-+]?[0-9]*\\.?[0-9]+"))
                onlyFloat = false;
            if (!string.matches("[0-9]+"))
                onlyInteger = false;
        }
        if (onlyFloat) attributeType = "REAL";
        else if (onlyInteger) attributeType = "INTEGER";
        if (!dataAttribute.contains("")) attributeType += " NOT NULL";

        return attributeType;
    }

}
