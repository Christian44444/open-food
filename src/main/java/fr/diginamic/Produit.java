package fr.diginamic;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUIT")
public class Produit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", length = 11, nullable = false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "ID_CATEGORIE")
	private Categorie categorie;
	
	@ManyToMany
	@JoinTable(name = "INGREDIENT_PRODUIT", 
	   joinColumns = @JoinColumn(name = "ID_PRODUIT", referencedColumnName = "ID" ),
	   inverseJoinColumns = @JoinColumn(name = "ID_INGREDIENT", referencedColumnName = "ID"))
	private List<Ingredient> ingredients = new ArrayList<Ingredient>();

	/**
	 * 
	 */
	public Produit() {
		super();
	}

	@Override
	public String toString() {
		return " Produit " + id + ", categorie=" + categorie + ", ingredients=" + ingredients + "]";
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the categorie
	 */
	public Categorie getCategorie() {
		return categorie;
	}

	/**
	 * @param categorie the categorie to set
	 */
	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	/**
	 * @return the ingredients
	 */
	public List<Ingredient> getIngredients() {
		return ingredients;
	}
	
	
}
