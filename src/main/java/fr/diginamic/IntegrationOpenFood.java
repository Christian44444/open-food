package fr.diginamic;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
		int i = 0;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("open-food");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trans = em.getTransaction();
		//String monFichier = ClassLoader.getSystemClassLoader().getResource("open-food-facts.csv").getFile();
		String monFichier = ".\\src\\main\\resources\\open-food-facts.csv"; 
		Path chemin = Paths.get(monFichier);
		List<String> lignes = Files.readAllLines(chemin, StandardCharsets.UTF_8);
		
		// ne pas traiter la première ligne
		lignes.remove(0);
		for (String l: lignes) {
			String[] ligneDecoupee = l.split("\\|",-1);
			trans.begin();
			Produit p1 = new Produit();
			// Mettre à jour le nom du produit ligneDecoupee[2]
			p1.setNom(ligneDecoupee[2]);
			
			// Vérifier la catégorie ligneDecoupee[0]
			TypedQuery<Categorie> q1 = em.createQuery("SELECT c FROM Categorie c WHERE c.libelle = ?1" , Categorie.class);
			q1.setParameter(1, ligneDecoupee[0]);
			List<Categorie> l1 = q1.getResultList();
			Categorie c1;
			
			if (l1.size() == 0) {
				c1 = new Categorie();
				c1.setLibelle(ligneDecoupee[0]);
				em.persist(c1);
			} else {
				c1 = l1.get(0);
			}
			p1.setCategorie(c1);
			
			// Ingrédients liste d'ingrédients séparés par des virgules ligneDecoupee[4]
			String[] ingredients = ligneDecoupee[4].split(",",-1);
			for (String s1 : ingredients) {
				TypedQuery<Ingredient> q2 = em.createQuery("SELECT i FROM Ingredient i WHERE i.libelle = ?1" , Ingredient.class);
				q2.setParameter(1, s1);
				List<Ingredient> l2 = q2.getResultList();
				Ingredient e1;
			
				if (l2.size() == 0) {
					e1 = new Ingredient();
					e1.setLibelle(s1);
					em.persist(e1);
				} else {
					e1 = l2.get(0);
				}
				p1.getIngredients().add(e1);
			}
			
			
			em.persist(p1);
			trans.commit();
			
			if (i >=100) {
			   return;
			}
			i++;
			
		} //for (String l: lignes)
	}

}
