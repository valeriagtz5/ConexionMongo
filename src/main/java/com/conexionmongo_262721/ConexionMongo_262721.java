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
public class ConexionMongo_262721 {

    public static void main(String[] args) {
        // CONFIGURACIÓN
        MongoClient client = MongoClients.create("mongodb://localhost:27017"); // Conexion al servidor
        MongoDatabase db = client.getDatabase("restaurantsDB"); // Seleccionar la base de datos
        MongoCollection<Document> col = db.getCollection("restaurants"); // Seleccionar la coleccion
        
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
        
    }
}
