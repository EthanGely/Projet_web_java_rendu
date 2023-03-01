package fr.iut2.project1.utility;

import org.sqlite.SQLiteConfig;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class GestionFactory {

    private static final String PERSISTENCE_UNIT_NAME = "Projet_JPA_SQLITE";

    // Factory pour la création d'EntityManager (gestion des transactions)
    public static EntityManagerFactory factory;

    // Creation de la factory
    public static void open() {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    // Fermeture de la factory
    public static void close() {
        factory.close();
    }

}