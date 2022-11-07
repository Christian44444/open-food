package fr.diginamic;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;


/**
 * Classe Intégration fichier open food facts
 * sert de point d'entrée pour charger en base le fichier
 *  
 * @author cmich
 *
 */
public class IntegrationOpenFood {
	
	public static void main(String[] args) throws IOException {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("open-food");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trans = em.getTransaction();
		
		String monFichier = ClassLoader.getSystemClassLoader().getResource("open-food-facts.csv").getFile();
		java.nio.file.Path chemin = Paths.get(monFichier);
		List<String> lignes = Files.readAllLines(chemin, StandardCharsets.UTF_8);
		
		// ne pas traiter la première ligne
		lignes.remove(0);
		for (String l: lignes) {
			String[] ligneDecoupee = l.split("|",-1);
			 
			// Vérifier la catégorie ligneDecoupee[0]
			TypedQuery<Categorie> q1 = em.createQuery("SELECT c FROM categorie c WHERE c.libelle = ?1" , Categorie.class);
			q1.setParameter(1, ligneDecoupee[0]);
			List<Categorie> l1 = q1.getResultList();
			Categorie c1;
			
			if (l1.size() == 0) {
				trans.begin();
				c1 = new Categorie();
				c1.setLibelle(ligneDecoupee[0]);
				em.persist(c1);
				trans.commit();
			} else {
				c1 = l1.get(0);
			}
			
			trans.begin();
			Produit p1 = new Produit();
			p1.setCategorie(c1);
			em.persist(p1);
			trans.commit();
			
		}
	}

}
