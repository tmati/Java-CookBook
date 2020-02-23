package main.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.controller.Controller;
import main.model.Category;
import main.model.Ingredient;
import main.model.Recipe;
/**
 * Controller for popup that handles creation and editing of Recipe values. 
 * @author tmati
 *
 */
public class RecipeController implements Initializable {

    @FXML
    private Label titleLabel;

    @FXML
    private Button closeBtn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField amountField;

    @FXML
    private TextField categoryField;

    @FXML
    private TableView<Ingredient> ingredientsTable;

    @FXML
    private TableColumn<Ingredient, Double> amountColumn;

    @FXML
    private TableColumn<Ingredient, String> measureColumn;

    @FXML
    private TableColumn<Ingredient, String> ingredientNameColumn;

    @FXML
    private Button addIngredientBtn;
    
    @FXML
    private Button delIngredientBtn;
    
    @FXML
    private Label errorLabel;

    @FXML
    private Button saveBtn;

    @FXML
    private Label nameLabel;

    @FXML
    private Label quantityLabel;

    @FXML
    private Label categoryLabel;
    
    @FXML
    private TextArea processTextArea;
    
	Controller controllerInstance = Controller.getInstance();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		amountColumn.setCellValueFactory(new PropertyValueFactory<Ingredient, Double>("amount"));
		amountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {

			@Override
			public Double fromString(String string) {
				return Double.parseDouble(string);
			}

			@Override
			public String toString(Double value) {
				return value.toString();
			}
		}));
		
		measureColumn.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("measure"));
		measureColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		
		ingredientNameColumn.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("name"));
		ingredientNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		
		if(controllerInstance.getSelected() != null) {
			Recipe toEdit = controllerInstance.getSelected();
			ingredientsTable.getItems().addAll(toEdit.getIngredients());
			titleLabel.setText("Edit recipe");
			nameField.setText(toEdit.getRecipename());
			amountField.setText(toEdit.getAmount());
			categoryField.setText(toEdit.getCategory().getCategoryDescription());
			processTextArea.setText(toEdit.getDescription());
		}

	}
    
    @FXML
    void closePopup(MouseEvent event) {
    	closeBtn.getScene().getWindow().hide();
    }
    
    @FXML
    void addIngredient(MouseEvent event) {
    	ingredientsTable.getItems().add(new Ingredient());
    }
    
    /**
     * Action to save the created recipe to DB after all fields are successfully filled.
     * @param event Mouse click on save button.
     */
    @FXML
    void saveRecipe(MouseEvent event) {
    	//Check for empty fields
	if (!nameField.getText().equals("") && !amountField.getText().equals("") && !categoryField.getText().equals("") && !ingredientsTable.getItems().isEmpty() && !processTextArea.getText().equals("")) {
		
		//Check if category already exists
		Category foundCategory = controllerInstance.getCategoryByName(categoryField.getText());
		if (foundCategory == null) {
			//Didn't exist. Save new category.
			Category categoryForNewRecipe = new Category(categoryField.getText());
			controllerInstance.saveCategory(categoryForNewRecipe);
		    if (controllerInstance.getSelected() != null) {
		    	controllerInstance.getSelected().setRecipename(nameField.getText());
		    	controllerInstance.getSelected().setAmount(amountField.getText());
		    	controllerInstance.getSelected().setIngredients(ingredientsTable.getItems());
		    	controllerInstance.getSelected().setDescription(processTextArea.getText());
		    	controllerInstance.getSelected().setCategory(categoryForNewRecipe);
		    	
		    controllerInstance.updateRecipe(controllerInstance.getSelected());
		    	
		    } else {
			Recipe toSave = new Recipe(nameField.getText(), amountField.getText(), processTextArea.getText(), categoryForNewRecipe, ingredientsTable.getItems());
			controllerInstance.saveRecipe(toSave);
		    }
			errorLabel.setText("");
		} else {
			
			//Existing category found. Use it.
		    if (controllerInstance.getSelected() != null) {
		    	controllerInstance.getSelected().setRecipename(nameField.getText());
		    	controllerInstance.getSelected().setAmount(amountField.getText());
		    	controllerInstance.getSelected().setIngredients(ingredientsTable.getItems());
		    	controllerInstance.getSelected().setDescription(processTextArea.getText());
		    	controllerInstance.getSelected().setCategory(foundCategory);
		    	
		    	controllerInstance.updateRecipe(controllerInstance.getSelected());
		    	
		    } else {
			Recipe toSave = new Recipe(nameField.getText(), amountField.getText(), processTextArea.getText(), foundCategory, ingredientsTable.getItems());
			controllerInstance.saveRecipe(toSave);
		    }
			errorLabel.setText("");
		}
		} else {
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Notice");
    		alert.setHeaderText(null);
    		alert.setContentText("Fill all fields and try again!");
    	    ((Stage)alert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
    	    alert.showAndWait();
		}
	saveBtn.getScene().getWindow().hide();
    }
    
    /**
     * Deleting ingredient from ingredients table.
     * @param event Mouse click on delete button.
     */
    @FXML
    public void delIngredient(MouseEvent event) {
    	if (ingredientsTable.getSelectionModel().getSelectedItem() != null) {
    	ingredientsTable.getItems().remove(ingredientsTable.getSelectionModel().getSelectedItem());
    	} else {
    		//If no ingredient is active in table, show alert
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Notice");
    		alert.setHeaderText(null);
    		alert.setContentText("Select an ingredient for deletion and try again.");
    	    ((Stage)alert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
    	    alert.showAndWait();
    	}
    }
    
    /**
     * Edit commit method for editing amount value in ingredients table.
     * @param event The CellEditEvent that fires on exiting the edited cell.
     */
    @FXML
    void amountOnEditCommit(TableColumn.CellEditEvent<Ingredient, Double> event) {
    	Ingredient toEdit = ingredientsTable.getSelectionModel().getSelectedItem();
    	toEdit.setAmount(event.getNewValue());
    }
    
    /**
     * Edit commit method for editing unit of measurement for ingredient.
     * @param event The CellEditEvent that fires on exiting the edited cell.
     */
    @FXML
    void measureOnEditCommit(TableColumn.CellEditEvent<Ingredient, String> event) {
    	Ingredient toEdit = ingredientsTable.getSelectionModel().getSelectedItem();
    	toEdit.setMeasure(event.getNewValue());
    }
    
    /**
     * Edit commit method for editing name of ingredient.
     * @param event The CellEditEvent that fires on exiting the edited cell.
     */
    @FXML
    void nameOnEditCommit(TableColumn.CellEditEvent<Ingredient, String> event) {
    	Ingredient toEdit = ingredientsTable.getSelectionModel().getSelectedItem();
    	toEdit.setName(event.getNewValue());
    }

}
