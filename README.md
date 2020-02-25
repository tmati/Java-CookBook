# Java-CookBook
Java project utilizing Maven, JavaFX and JPA. 

Java-CookBook is a small project that resembles a cook book. Users can add and edit recipes to the database and then categorize and sort them.
All actions are handled through a JavaFX graphical user interface.

To run this application, you must have an available MySQL instance. Connection settings can be set up in src/META-INF/persistence.xml.
The file has two persistence units, one (CookBookDCPU) for first time users. Using this persistence unit will create the required database structures for the application. The other persistence unit (CookBookPU) should then be used next time the application is ran. It will use the existing DB structure without losing added data.

**To change the persistence unit, add the desired value as a parameter in the line where the JPA EntityManagerFactory object is created near the top of src/main/model/java.**

### Future development ideas

* Implement multiplication of recipe ingredient amount value for different amounts of people
* Create printable files from recipes
