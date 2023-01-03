package C109118115_Product_curd_tableview;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppJavaFxMain extends Application{
    @Override
    public void start(Stage stage) throws Exception {

        //測試不同的模組功能
        Parent root = FXMLLoader.load(getClass().getResource("CrudProducttTableView.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("產品資料庫管理系統");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}