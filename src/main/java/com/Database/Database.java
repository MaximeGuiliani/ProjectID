package com.Database;


import java.sql.*;
import java.util.*;

/**
 * Fonctions pour la Database
 */

public class Database {
    //Liste des connexions
    private final Map<String, Connection> connections;
    // Instance unique
    private static Database instance;

    /**
     * Constructeur
     */
    private Database() {
        connections = new HashMap<>();
    }

    public static void updateDB(String dbName, String query) {
    }

    // Créer les fonctions de Database :

}

