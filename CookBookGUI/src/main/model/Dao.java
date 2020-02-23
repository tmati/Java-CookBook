package main.model;

import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
/**
 * The Database Access Object for this project. Handles use of DB. Passes data to Controller class methods.
 * @author tmati
 */
public class Dao {
	
	/**
	 * Singleton pattern instance creation.
	 */
	private static Dao instance = null;
	
	public static Dao getInstance () {
		if (instance == null) {
			instance = new Dao();
		}
		return instance;
	}
	
	/**
	 * JPA entity management
	 */
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("CookBookPU");
	private Scanner keyboard;
	
	/**
	 * Saving Category object to DB.
	 * @param toSave the Category object to save.
	 */
	public void saveCategory(Category toSave) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(toSave);
		em.getTransaction().commit();
		em.close();
	}
	
	/**
	 * Updates existing Recipe object in DB.
	 * @param toUpdate the Recipe object to update.
	 */
	public void updateRecipe(Recipe toUpdate) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.merge(toUpdate);
		em.getTransaction().commit();
		em.close();
	}
	
	/**
	 * Saves a new Recipe object to DB.
	 * @param newRecipe The Recipe object to persist.
	 */
	public void saveRecipe(Recipe newRecipe) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(newRecipe);
		
		em.getTransaction().commit();
		em.close();
	}
	
	/**
	 * Deletes given Recipe object from the database.
	 * @param r the Recipe to delete.
	 */
	public void deleteRecipe(Recipe r) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Recipe toDelete = em.find(Recipe.class, r.getRecipeId());
		em.remove(toDelete);
		em.getTransaction().commit();
	}
	
	/**
	 * Deletes given Category object from database.
	 * @param c The category to delete.
	 */
	public void deleteCategory(Category c) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Category toDelete = em.find(Category.class, c.getCategoryID());
		em.remove(toDelete);
		em.getTransaction().commit();
	}
	
	/**
	 * Get all recipes from DB.
	 * @return List of all Recipe objects.
	 */
	public List<Recipe> getRecipes() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		List<Recipe> allRecipes = em.createQuery("Select r from Recipe r", Recipe.class).getResultList();
		em.getTransaction().commit();
		em.close();
		return allRecipes;
	}
	
	/**
	 * Finds a recipe by given name String.
	 * @param recipeName The searched name.
	 * @return Found recipe, null if not found.
	 */
	public Recipe findRecipeByName (String recipeName) {
		List <Recipe> allRecipes = getRecipes();
		for (Recipe r : allRecipes) {
			if(r.getRecipename().matches(recipeName)) {
				return r;
			}
		}
		return null;
	}
	
	/**
	 * Gets all categories from DB.
	 * @return List of categories.
	 */
	public List<Category> getCategories() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		List<Category> allCategories = em.createQuery("Select c from Category c", Category.class).getResultList();
		em.getTransaction().commit();
		em.close();
		return allCategories;
	}
	
	/**
	 * Get all recipes from DB that belong to a given category.
	 * @param C The category to look for.
	 * @return All Recipe objects that belong to the given category.
	 */
	public List<Recipe> getRecipesByCategory(Category C) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("Select r from Recipe r where r.category = :category");
		query.setParameter("category", C);
		return query.getResultList();
	}
	
	/**
	 * Find category by name parameter
	 * @param categoryName The name to search for.
	 * @return Corresponding category if found, null if not.
	 */
	public Category findCategoryByName (String categoryName) {
		List <Category> allCategories = getCategories();
		for (Category c : allCategories) {
			if(c.getCategoryDescription().matches(categoryName)) {
				return c;
			}
		}
		return null;
	}
}
