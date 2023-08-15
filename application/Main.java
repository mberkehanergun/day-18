package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import mainpackage.ComputerEngineer;
import mainpackage.Engineer;
import mainpackage.Engineers;
import mainpackage.dao.NamedParamJdbcDaoImpl;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Main extends Application {
	
    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Engineers Login Application");

            Label usernameLabel = new Label("Username:");
            TextField usernameField = new TextField();

            Label passwordLabel = new Label("Password:");
            PasswordField passwordField = new PasswordField();

            Button loginButton = new Button("Login");
            Label outputLabel = new Label();

            outputLabel.setText("Enter 'exit' for both username and password if you want to exit.");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 20, 20, 20));

            grid.add(usernameLabel, 0, 0);
            grid.add(usernameField, 1, 0);
            grid.add(passwordLabel, 0, 1);
            grid.add(passwordField, 1, 1);
            grid.add(loginButton, 1, 2);
            grid.add(outputLabel, 0, 3, 2, 1);

            Scene scene = new Scene(grid, 500, 400);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("engineersinfo.xml");
            Engineers engineers = ctx.getBean("engineers", Engineers.class);

            AbstractApplicationContext ctx2 = new ClassPathXmlApplicationContext("namedparamjdbcdaoimpl.xml");
            NamedParamJdbcDaoImpl dao = ctx2.getBean("namedParamJdbcDaoImpl", NamedParamJdbcDaoImpl.class);

            loginButton.setOnAction(event -> {

                String inputname = usernameField.getText();
                String inputpw = passwordField.getText();
                boolean ifNotExit = !inputname.equals("exit") || !inputpw.equals("exit");
                boolean loginFail = engineers.login(inputname, inputpw) && ifNotExit;
                if (loginFail == false && ifNotExit == true) {

                    Button companySettingsButton = new Button("Company Settings");
                    Button loadCustomersButton = new Button("Load Customers");
                    Button acceptInternsButton = new Button("Accept Interns");
                    Button processWorkButton = new Button("Process Work");
                    Button backButton = new Button("Back");

                    VBox buttonLayout = new VBox(10);
                    buttonLayout.setAlignment(Pos.CENTER);
                    buttonLayout.getChildren().addAll(companySettingsButton, loadCustomersButton, acceptInternsButton, processWorkButton, backButton);
                    Scene buttonScene = new Scene(buttonLayout, 600, 500);

                    primaryStage.setScene(buttonScene);

                    backButton.setOnAction(backEvent -> {
                        // Go back to the original login scene
                        primaryStage.setScene(scene);
                        usernameField.clear();
                        passwordField.clear();
                        outputLabel.setText("Enter 'exit' for both username and password if you want to exit."); // Clear the output message
                    });
                    
                    // Handle button actions
                    companySettingsButton.setOnAction(companySettingsEvent -> {
                        // Create a new scene for company settings
                    	Label resultLabel = new Label("Results will show up here.");

                        Button createTableButton1 = new Button("Create Customer Table");
                        Button dropTableButton1 = new Button("Drop Customer Table");
                        Button createTableButton2 = new Button("Create Intern Tables");
                        Button dropTableButton2 = new Button("Drop Intern Tables");
                        Button createTableButton3 = new Button("Create CompanyConfig Table");
                        Button dropTableButton3 = new Button("Drop CompanyConfig Table");
                        Button changeNumButton1 = new Button("Change IANUM");
                        Button backButtonToButtonScene1 = new Button("Back");

                        VBox settingsLayout = new VBox(10);
                        settingsLayout.setAlignment(Pos.CENTER);
                        settingsLayout.getChildren().addAll(resultLabel, createTableButton1, dropTableButton1, createTableButton2, dropTableButton2, createTableButton3, dropTableButton3, changeNumButton1, backButtonToButtonScene1);
                        Scene settingsScene = new Scene(settingsLayout, 600, 500);

                        primaryStage.setScene(settingsScene);

                        // Handle "Create Customer Table" button action
                        createTableButton1.setOnAction(createTableEvent -> {
                        	dao.createCustomerTable();
                        	resultLabel.setText("Customer table created successfully.");
                        });

                        // Handle "Drop Customer Table" button action
                        dropTableButton1.setOnAction(dropTableEvent -> {
                        	dao.dropCustomerTable();
                        	resultLabel.setText("Customer table dropped successfully.");
                        });
                        
                        createTableButton2.setOnAction(createTableEvent -> {
                        	dao.createInternTable();
                        	dao.createExInternTable();
                        	resultLabel.setText("Intern tables created successfully.");
                        });

                        dropTableButton2.setOnAction(dropTableEvent -> {
                        	dao.dropInternTable();
                        	dao.dropExInternTable();
                        	resultLabel.setText("Intern tables dropped successfully.");
                        });
                        
                        createTableButton3.setOnAction(createTableEvent -> {
                        	dao.createCompanyConfigTable();
                        	resultLabel.setText("CompanyConfig table created successfully.");
                        });

                        dropTableButton3.setOnAction(dropTableEvent -> {
                        	dao.dropCompanyConfigTable();
                        	resultLabel.setText("CompanyConfig table dropped successfully.");
                        });
                        
                        changeNumButton1.setOnAction(changeNumEvent -> {
                            // Create a new stage for changing IANUM
                            Stage changeNumStage = new Stage();
                            changeNumStage.setTitle("Change IANUM");
                            
                            TextField newNumField = new TextField();
                            Button changeNumButton = new Button("Change");

                            VBox changeNumLayout = new VBox(10);
                            changeNumLayout.setAlignment(Pos.CENTER);
                            changeNumLayout.getChildren().addAll(newNumField, changeNumButton);

                            Scene changeNumScene = new Scene(changeNumLayout, 400, 300);
                            changeNumStage.setScene(changeNumScene);

                            // Handle the "Change" button action
                            changeNumButton.setOnAction(changeNumButtonEvent -> {
                                int newNum = Integer.parseInt(newNumField.getText());
                                dao.changeIANUM(newNum);
                                resultLabel.setText("IANUM changed successfully");
                                changeNumStage.close(); // Close the changeNumStage after changing IANUM
                            });

                            changeNumStage.show();
                        });
                        
                        backButtonToButtonScene1.setOnAction(backToButtonEvent -> {
                            primaryStage.setScene(buttonScene); // Switch to the "Button" scene
                        });
                    });

                    loadCustomersButton.setOnAction(loadCustomersEvent -> {
                    	
                    	// Original terminal-like interface scene
                        TextArea terminalTextArea = new TextArea();
                        terminalTextArea.setEditable(true);
                        terminalTextArea.setPrefRowCount(10);

                        Button backButtonToButtonScene2 = new Button("Back");
                        Button insertButton = new Button("Insert");
                        Button deleteButton = new Button("Delete");

                        HBox buttonBox = new HBox(10);
                        buttonBox.setAlignment(Pos.CENTER);
                        buttonBox.getChildren().addAll(backButtonToButtonScene2, insertButton, deleteButton);

                        VBox terminalLayout = new VBox(10);
                        terminalLayout.setAlignment(Pos.CENTER);
                        terminalLayout.getChildren().addAll(terminalTextArea, buttonBox);
                        Scene terminalScene = new Scene(terminalLayout, 600, 500);
                        
                        insertButton.setDisable(false);
                        deleteButton.setDisable(true);
                        
                        // Handle terminal button actions
                        insertButton.setOnAction(insertEvent -> {
                            dao.fillTableFromCsv("C:\\Users\\berkehan\\Downloads\\CustomerData.csv");
                            List<String> NandS = dao.retrieveNamesAndSurnames();
                            for (String item : NandS) {
                                terminalTextArea.appendText("\n" + item);
                            }
                            insertButton.setDisable(true);
                            deleteButton.setDisable(false);
                        });

                        deleteButton.setOnAction(deleteEvent -> {
                            dao.deleteCustomersUsingCsv("C:\\Users\\berkehan\\Downloads\\CustomerData.csv");
                            List<String> NandS = dao.retrieveNamesAndSurnames();
                            for (String item : NandS) {
                                terminalTextArea.appendText("\n" + item);
                            }
                            terminalTextArea.appendText("\nData from the CSV file has been successfully deleted from the table.");
                            insertButton.setDisable(false);
                            deleteButton.setDisable(true);
                        });

                        backButtonToButtonScene2.setOnAction(backToButtonEvent -> {
                            primaryStage.setScene(buttonScene); // Switch to the "Button" scene
                        });
                    	
                        // Reuse the existing terminal-like interface scene
                        primaryStage.setScene(terminalScene);
                    });
                    
                    acceptInternsButton.setOnAction(loadCustomersEvent -> {
                    	
                    	// Original terminal-like interface scene
                        TextArea terminalTextArea = new TextArea();
                        terminalTextArea.setEditable(true);
                        terminalTextArea.setPrefRowCount(10);

                        Button backButtonToButtonScene2 = new Button("Back");
                        Button fillButton = new Button("Fill");
                        Button deleteButton = new Button("Delete");
                        Button loadAllButton = new Button("LoadAll");
                        

                        HBox buttonBox = new HBox(10);
                        buttonBox.setAlignment(Pos.CENTER);
                        buttonBox.getChildren().addAll(backButtonToButtonScene2, fillButton, deleteButton, loadAllButton);

                        VBox terminalLayout = new VBox(10);
                        terminalLayout.setAlignment(Pos.CENTER);
                        terminalLayout.getChildren().addAll(terminalTextArea, buttonBox);
                        Scene terminalScene = new Scene(terminalLayout, 600, 500);
                        
                        loadAllButton.setDisable(true);
                        deleteButton.setDisable(true);
                        
                        fillButton.setOnAction(insertEvent -> {
                            dao.fillInternTableFromCsv("C:\\Users\\berkehan\\Downloads\\InternData.csv");
                            List<String> NandS = dao.retrieveInternNandS();
                            for (String item : NandS) {
                                terminalTextArea.appendText("\n" + item);
                            }
                            terminalTextArea.appendText("\nIntern table filled with all names from the file succesfully.");
                            fillButton.setDisable(true);
                            deleteButton.setDisable(false);
                        });
                        
                        // Handle terminal button actions
                        loadAllButton.setOnAction(insertEvent -> {
                            dao.loadAll();
                            List<String> NandS = dao.retrieveInternNandS();
                            for (String item : NandS) {
                                terminalTextArea.appendText("\n" + item);
                            }
                            terminalTextArea.appendText("\nAll deleted names loaded succesfully.");
                            loadAllButton.setDisable(true);
                        });

                        deleteButton.setOnAction(changeNumEvent -> {
                            // Create a new stage for changing IANUM
                            Stage deleteNamesStage = new Stage();
                            deleteNamesStage.setTitle("Delete Names");
                            
                            
                            TextArea newNamesArea = new TextArea();
                            Button deleteNamesButton = new Button("Delete");

                            VBox deleteNamesLayout = new VBox(10);
                            deleteNamesLayout.setAlignment(Pos.CENTER);
                            deleteNamesLayout.getChildren().addAll(newNamesArea, deleteNamesButton);

                            Scene deleteNamesScene = new Scene(deleteNamesLayout, 400, 300);
                            deleteNamesStage.setScene(deleteNamesScene);

                            // Handle the "Change" button action
                            deleteNamesButton.setOnAction(changeNumButtonEvent -> {
                            	String text = newNamesArea.getText();
                            	String[] linesArray = text.split("\n");
                            	List<String> l = dao.removeSelectedRows(linesArray);
                            	for (String s : l) {
                            		terminalTextArea.appendText("\n"+s);
                                }
                            	List<String> NandS = dao.retrieveInternNandS();
                                for (String item : NandS) {
                                    terminalTextArea.appendText("\n" + item);
                                }
                                terminalTextArea.appendText("\nSelected names deleted succesfully.");
                                loadAllButton.setDisable(false);
                                deleteNamesStage.close(); // Close the changeNumStage after changing IANUM
                            });

                            deleteNamesStage.show();
                        });

                        backButtonToButtonScene2.setOnAction(backToButtonEvent -> {
                            primaryStage.setScene(buttonScene); // Switch to the "Button" scene
                        });
                    	
                        // Reuse the existing terminal-like interface scene
                        primaryStage.setScene(terminalScene);
                    });
                    
                    processWorkButton.setOnAction(processWorkEvent -> {

                        TextArea processTextArea = new TextArea();
                        processTextArea.setEditable(true);

                        Label resultLabel = new Label("Results will show up here.");
                        
                        Button backButtonToButtonScene3 = new Button("Back");
                        Button processButton = new Button("Process");

                        HBox buttonBox = new HBox(10);
                        buttonBox.setAlignment(Pos.CENTER);
                        buttonBox.getChildren().addAll(backButtonToButtonScene3, processButton);

                        VBox processLayout = new VBox(10);
                        processLayout.setAlignment(Pos.CENTER);
                        processLayout.getChildren().addAll(processTextArea, buttonBox, resultLabel);
                        Scene processScene = new Scene(processLayout, 600, 500);

                        primaryStage.setScene(processScene);

                        // Handle "Process" button action
                        processButton.setOnAction(processButtonEvent -> {
                        	int value;
                        	String inputCsvFile = "C:\\Users\\berkehan\\Downloads\\WorkedHoursForDay.csv";
                			try (BufferedReader br = new BufferedReader(new FileReader(inputCsvFile))) {
                	            String outputCsvFile = "C:\\Users\\berkehan\\Downloads\\TotalSalaryForDay.csv";
                	            try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputCsvFile))) {
                	            	value = Integer.parseInt(br.readLine().trim());
                	            	processTextArea.appendText("\nComputer Engineer 1 worked "+value+" hours today and got salary of "+processCEngSalary(engineers.getCe1(), value, bw));
                	            	value = Integer.parseInt(br.readLine().trim());
                	            	processTextArea.appendText("\nComputer Engineer 2 worked "+value+" hours today and got salary of "+processCEngSalary(engineers.getCe2(), value, bw));
                	            	value = Integer.parseInt(br.readLine().trim());
                	            	processTextArea.appendText("\nComputer Engineer 3 worked "+value+" hours today and got salary of "+processCEngSalary(engineers.getCe3(), value, bw));
                	            	value = Integer.parseInt(br.readLine().trim());
                	            	processTextArea.appendText("\nComputer Engineer 4 worked "+value+" hours today and got salary of "+processCEngSalary(engineers.getCe4(), value, bw));
                	            	value = Integer.parseInt(br.readLine().trim());
                	            	processTextArea.appendText("\nComputer Engineer 5 worked "+value+" hours today and got salary of "+processCEngSalary(engineers.getCe5(), value, bw));
                	            	value = Integer.parseInt(br.readLine().trim());
                	            	processTextArea.appendText("\nComputer Engineer 6 worked "+value+" hours today and got salary of "+processCEngSalary(engineers.getCe6(), value, bw));
                	            	value = Integer.parseInt(br.readLine().trim());
                	            	processTextArea.appendText("\nComputer Engineer 7 worked "+value+" hours today and got salary of "+processCEngSalary(engineers.getCe7(), value, bw));
                	            	value = Integer.parseInt(br.readLine().trim());
                	            	processTextArea.appendText("\nEngineer 1 worked "+value+" hours today and got salary of "+processEngSalary(engineers.getE1(), value, bw));
                	            	value = Integer.parseInt(br.readLine().trim());
                	            	processTextArea.appendText("\nEngineer 2 worked "+value+" hours today and got salary of "+processEngSalary(engineers.getE2(), value, bw));
                	            	value = Integer.parseInt(br.readLine().trim());
                	            	processTextArea.appendText("\nEngineer 3 worked "+value+" hours today and got salary of "+processEngSalary(engineers.getE3(), value, bw));
                	            	value = Integer.parseInt(br.readLine().trim());
                	            	processTextArea.appendText("\nEngineer 4 worked "+value+" hours today and got salary of "+processEngSalary(engineers.getE4(), value, bw));
                	                resultLabel.setText("Data has been written to "+outputCsvFile);
                	                processButton.setDisable(true);
                	            } catch (Exception e) {
                	                e.printStackTrace();
                	            }
                	        } catch (Exception e) {
                	            e.printStackTrace();
                	        }
                            String processResult = "\nWork processed successfully.";
                            processTextArea.appendText(processResult);
                        });

                        // Handle "Back" button action
                        backButtonToButtonScene3.setOnAction(backToButtonEvent -> {
                            primaryStage.setScene(buttonScene); // Switch back to the "Button" scene
                        });
                    });

                } else if (loginFail == false && ifNotExit == false) {
                    primaryStage.close();
                } else {
                    outputLabel.setText("Login failed. Please check your credentials.");
                }

            });

            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> {
                // Close both Spring application contexts when the application is shutting down
                ctx.close();
                ctx2.close();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static int processCEngSalary(ComputerEngineer compeng, int hour, BufferedWriter bw) {
    	int value = hour*compeng.getSalaryPerHour()+ComputerEngineer.getDailySalary();
    	try {
	        bw.write(String.valueOf(value));
	        bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return value;
	}
	
	public static int processEngSalary(Engineer eng, int hour, BufferedWriter bw) {
		int value = hour*eng.getSalaryPerHour()+Engineer.getDailySalary();
		try {
	        bw.write(String.valueOf(value));
	        bw.newLine();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
    
    public static void main(String[] args) {

        launch(args);

    }
}