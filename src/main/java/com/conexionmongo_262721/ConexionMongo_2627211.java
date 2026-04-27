/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.conexionmongo_262721;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import java.util.ArrayList;
import java.util.Arrays;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author valeria
 */
public class ConexionMongo_2627211 {

    public static void main(String[] args) {
        // CONFIGURACIÓN
        MongoClient client = MongoClients.create("mongodb://localhost:27017"); // Conexion al servidor
        MongoDatabase db = client.getDatabase("restaurantsDB"); // Seleccionar la base de datos
        MongoCollection<Document> col = db.getCollection("restaurants"); // Seleccionar la coleccion
        
        // ------ ASIGNACIÓN ------
        col.deleteMany(new Document()); // Eliminar todo lo del ejercicio
        
        // 1. Insertar un solo documento
        Document document2 = new Document();
        document2.append("name", "Cafe de la Plaza");
        document2.append("stars", 4.3);
        document2.append("categories", Arrays.asList(new String[]{"Cafe","Postres","Desayuno"}));
        
        // 2. Insertar varios documentos adicionales
        ArrayList<Document> lista = new ArrayList<>();
        
        lista.add(new Document ("name", "Expresso Express")
                .append("stars", 4.8)
                .append("categories", Arrays.asList(new String[]{"Te", "Infusiones","Postres"}))
        );
        
        lista.add(new Document ("name", "The Tea House")
                .append("stars", 3.9)
                .append("categories", Arrays.asList(new String[]{"Te", "Infusiones","Postres"}))
        );
        
        lista.add(new Document ("name", "Morning Brew")
                .append("stars", 4)
                .append("categories", Arrays.asList(new String[]{"Cafe", "Desayuno","Bakery"}))
        );
        
        // 3. Filtros a mostrar
        System.out.println("=== FILTROS ===");
        
        // Restaurantes con 4.5 o mas estrellas
        for(Document d : col.find(Filters.gte("stars", 4.5))){
            System.out.println(d.toJson());
        }
        
        // Restaurantes cuyo nombre diga "Cafe"
        for(Document d : col.find(Filters.regex("name", "Cafe"))){
            System.out.println(d.toJson());
        }
        
        // Restaurantes con categories que incluyan "Postres".
        for(Document d : col.find(Filters.in("categories", Arrays.asList("Postres")))){
            System.out.println(d.toJson());
        }
        
        // Restaurantes con stars entre 3 y 4.3
        Bson filtro = Filters.and(Filters.gte("stars", 3), Filters.lte("stars",4.3));
        for(Document d : col.find(filtro)){
            System.out.println(d.toJson());
        }
        
        // Restaurantes cuyo nombre empieza con "T".
        for(Document d : col.find(Filters.regex("name", "^T"))){
            System.out.println(d.toJson());
        }
        
        // 3. Updates
        System.out.println("=== UPDATES ===");
        
        // Cambiar stars a 4.5 para "Morning Brew"
        col.updateOne(Filters.eq("name", "Morning Brew"),
                Updates.set("stars", 4.5)
        );
        
        // Incrementar stars +0.2 para documentos con categories que contengan "Bakery" o "Desayuno"
        col.updateMany(Filters.in("categories", Arrays.asList("Bakery", "Desayuno")),
                Updates.inc("stars", 0.2)
        );
        
        // Agregar campos phone = "555-111-2222" y open = true a "Café de la Plaza"
        col.updateOne(Filters.eq("name", "Café de la Plaza"),
                Updates.combine(
                        Updates.set("phone", "555-111-2222"),
                        Updates.set("open", true)     
                )
        );
        
        // 4. Updates
        System.out.println("=== DELETES ===");
        
        // Eliminar documento con name = "Espresso Express"
        col.deleteOne(Filters.eq("name", "Expresso Express"));
        
        // Eliminar todos los documentos con stars < 4
        col.deleteMany(Filters.lt("stars", 4));
        /* 
        // ------ FIN ASIGNACIÓN ------
        
        
        /* ------ EJERCICIO -------
        // -- CREATE
        // Insertar un solo documento
        Document document = new Document();
        document.append("name", "Tacos de la Allende");
        document.append("stars", 4.5);
        col.insertOne(document);
        
        // Insertar varios documentos
        ArrayList<Document> lista = new ArrayList<>();
        
        lista.add(new Document ("name","Sun Bakery Trattoria")
                .append("stars",4)
                .append("categories", Arrays.asList(new String[]{"Pizza","Pasta","Italian","Coffe"})) 
        );
        
        lista.add(new Document ("name","Sushilito")
                .append("stars",5)
                .append("categories", Arrays.asList(new String[]{"Sushi","China","Fastfood","Te"})) 
        );
        
        lista.add(new Document ("name","Litte Ceasears")
                .append("stars",3)
                .append("categories", Arrays.asList(new String[]{"Pizza","Fastfood","wings"})) 
        );
        
        col.insertMany(lista);
        System.out.println("Documentos insertados correctamente");
        
        // -- READ
        System.out.println("\n=== FILTROS ===");
        
        // 1. Igualdad
        for(Document d : col.find(Filters.eq("name","Sushilito"))){
            System.out.println(d.toJson());
        }
        
        // 2. Mayor que
        for(Document d : col.find(Filters.gt("stars",4))){
            System.out.println(d.toJson());
        }
        
        // 3. Entre 3 y 4 estrellas
        Bson filtro = Filters.and(Filters.gte("stars",3), Filters.lte("stars",4));
        for(Document d : col.find(filtro)){
            System.out.println(d.toJson());
        }
        
        // 4. Categoria contiene "Pizza"
        for(Document d : col.find(Filters.in("categories",Arrays.asList("Pizza")))){
            System.out.println(d.toJson());
        }
        
        // 4. Nombre que empiece con "S"
        for(Document d : col.find(Filters.regex("name","^S"))){
            System.out.println(d.toJson());
        }
        
        // -- UPDATE
        System.out.println("\n=== UPDATES ===");
        
        col.updateOne(Filters.eq("name","Sushilito"),
                Updates.set("stars", 4));
        
        col.updateOne(Filters.in("categories", Arrays.asList("Pizza","Fastfood")),
                Updates.inc("stars", 1) // Suma +1 a estrellas donde haya pizza o fastfood
        ); 
        
        col.updateOne(Filters.eq("name", "Sun Bakery Trattoria"),
                Updates.combine(
                        Updates.set("phone", "555-444-322"),
                        Updates.set("open", true)
                )); // Agrega nuevos campos
        
        System.out.println("Updates realizados correctamente");
        
        // -- DELETES
        System.out.println("\n=== DELETES ===");
        
        // 3 ejemplos de eliminaciones
        col.deleteOne(Filters.eq("name","Litte Ceasears")); // Elimina un registro exacto
        
        col.deleteMany(Filters.lt("stars", 3)); // Elimina todos con menos de 3 estrellas
        
        col.deleteMany(Filters.in("categories", Arrays.asList("China", "Te")));
        
        System.out.println("Deletes realizados correctamente");
        ---------- FIN EJERCICIO -----------
        */
    }
}
