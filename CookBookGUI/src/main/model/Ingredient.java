package main.model;

import java.io.Serializable;

/**
 * Ingredient objects to be stored in arraylist on a recipe
 * @author tmati
 *
 */

public class Ingredient implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name for ingredient
	 */
	private String name;
	
	/**
	 * Amount of ingredient required
	 */
	private double amount;
	
	/**
	 * Unit of measuring for ingredient
	 */
	private String measure;
	
	/**
	 * Ingredient with amount, name and measurement data
	 * @param name name of the ingredient.
	 * @param amount amount of units of measure for ingredient.
	 * @param measure units an ingredient is measured in, e.g. ml, kg, one and a half.
	 */
	public Ingredient(String name, double amount, String measure) {
		this.name = name;
		this.amount = amount;
		this.measure = measure;
	}
	
	/**
	 * Use this constructor to split ingredients table to different parts e.g. base, toppings...
	 * @param name the name for the part
	 */
	public Ingredient(String name) {
		this.name = name;
		this.amount = 0;
		this.measure = null;
	}
	
	/**
	 * String information
	 */
	public String toString() {
		return this.getAmount() + " " + this.getMeasure()+ "\t" + this.getName();
	}
	
	public String getName() {
		return name;
	}

	public double getAmount() {
		return amount;
	}

	public String getMeasure() {
		return measure;
	}
	
}
