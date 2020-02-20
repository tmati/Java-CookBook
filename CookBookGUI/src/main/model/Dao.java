package main.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
	
	public void saveCategory(Category toSave) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(toSave);
		em.getTransaction().commit();
		em.close();
	}
	
	/**
	 * Test method for collecting ingredient array data from console input. Removed after GUI recipe generation working.
	 * @return The list of given ingredients.
	 */
	public ArrayList<Ingredient> collectIngredients() {
		boolean exit = false;
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		String ingredientName = "";
		double amount;
		String measure;
		keyboard = new Scanner(System.in);

		while (exit == false) {
			System.out.println(
					"\nWrite ingredient name! If you want to specify different parts of the recipe, do not write any measurements.");
			ingredientName = keyboard.nextLine();
			System.out.println("\nWrite amount of ingredient!");
			amount = keyboard.nextDouble();
			keyboard.nextLine();
			System.out.println("\nWrite units of measure for ingredient!");
			measure = keyboard.nextLine();
			Ingredient toAdd = new Ingredient(ingredientName, amount, measure);
			ingredients.add(toAdd);
			System.out.println("Add another ingredient Y/N?");
			if (keyboard.nextLine().equalsIgnoreCase("n")) {
				exit = true;
			}
		}

		return ingredients;
	}
	
	/**
	 * Saves a new Recipe object to DB.
	 * @param newRecipe The Recipe object to persist.
	 */
	public void SaveRecipe(Recipe newRecipe) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(newRecipe);
		
		em.getTransaction().commit();
		em.close();
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
