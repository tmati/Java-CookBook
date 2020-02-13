package main.controller;

import java.util.ArrayList;
import java.util.List;

import main.model.Category;
import main.model.Dao;
import main.model.Ingredient;
import main.model.Recipe;
/**
 * Controller class. Handles use of models and database. Methods carry data for View class.
 * @author tmati
 */
public class Controller {
	
	/**
	 * Database Access Object Singleton creation.
	 */
	private final Dao cookBookDao = Dao.getInstance();
	
	/**
	 * Controller instance singleton creation
	 */
	private static Controller instance = null;
	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}
	
	/**
	 * Find recipe from DB by name. Calls Dao method to use DB.
	 * @param name The recipe name to look for.
	 * @return recipe with corresponding name
	 */
	public Recipe getRecipeByName(String name) {
		return cookBookDao.findRecipeByName(name);
	}
	
	/**
	 * Get all recipes from DB.
	 * @return list of all recipes in DB.
	 */
	public List<Recipe> getRecipes() {
		return cookBookDao.getRecipes();
	}
	
	/**
	 * Calls Dao to add a new recipe entry into the DB recipes table.
	 * @param r the recipe to add.
	 */
	public void SaveRecipe(Recipe r) {
		cookBookDao.SaveRecipe(r);
	}
	
	/**
	 * Calls Dao to save new Category object into DB.
	 * @param categoryName the name for the new category.
	 * @return saved category object.
	 */
	public Category SaveCategory(String categoryName) {
		Category C = cookBookDao.saveCategory(categoryName);
		return C;
	}
	
	/**
	 * Calls Dao test method to create Ingredients array with console input. Removed later upon completion of GUI based adding of recipes.
	 * @return List of collected ingredients.
	 */
	public List<Ingredient> collectIngredients() {
		return cookBookDao.collectIngredients();
	}
	
	/**
	 * Calls dao to return all Category objects from DB.
	 * @return List of Category objects from DB.
	 */
	public List<Category> getCategories () {
	return cookBookDao.getCategories();
	}
	
	/**
	 * Calls Dao to find a category that corresponds to the given name.
	 * @param name Category name to look for.
	 * @return Category found by name.
	 */
	public Category getCategoryByName (String name) {
		return cookBookDao.findCategoryByName(name);
	}
	
}
