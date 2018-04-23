package pl.edu.wat.wcy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.edu.wat.wcy.controllers.MainController;
import pl.edu.wat.wcy.server_do_usuniecia.Server;

public class Main extends Application {
    public static void main(String[] args) {
        new Server().start();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        primaryStage.setTitle("Jar - Editor");
        Parent root = loader.load(ResourceLoader.getFxml("main.fxml"));

        MainController mainController = loader.getController();
        primaryStage.setScene(new Scene(root, 1000, 600));
        mainController.setMainStage(primaryStage);

        primaryStage.show();
    }
}
