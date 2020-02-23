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
	 * Recipe object to pass to menu when editing existing recipe.
	 */
	private Recipe selected = null;
	

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
	public void saveRecipe(Recipe r) {
		cookBookDao.saveRecipe(r);
	}
	
	/**
	 * Calls Dao to update existing Recipe object.
	 * @param r Recipe object to update.
	 */
	public void updateRecipe(Recipe r) {
		cookBookDao.updateRecipe(r);
	}
	
	/**
	 * Calls Dao to create new category entry to DB.
	 * @param toSave
	 */
	public void saveCategory(Category toSave) {
	  cookBookDao.saveCategory(toSave);
	}
	
	/**
	 * Calls Dao to delete Recipe object from DB.
	 * @param r
	 */
	public void deleteRecipe(Recipe r) {
		cookBookDao.deleteRecipe(r);
	}
	
	/**
	 * Calls Dao to delete Category object from DB.
	 * @param c
	 */
	public void deleteCategory(Category c) {
		cookBookDao.deleteCategory(c);
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
	
	/**
	 * Database call to find all recipes that belong to a given category.
	 * @param C The category to look for
	 * @return List of recipes in given category.
	 */
	public List<Recipe> getRecipesByCategory(Category C) {
		return cookBookDao.getRecipesByCategory(C);
	}
	
	/**
	 * Returns the Recipe object stored by class
	 * @return the Recipe object stored by this Controller instance.
	 */
	public Recipe getSelected() {
		return selected;
	}
	
	/**
	 * Set the Recipe object stored by this instance.
	 * @param selected the Recipe object to store in this Controller instance.
	 */
	public void setSelected(Recipe selected) {
		this.selected = selected;
	}
	
}
