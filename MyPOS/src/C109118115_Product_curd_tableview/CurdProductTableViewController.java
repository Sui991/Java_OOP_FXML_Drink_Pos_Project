/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package C109118115_Product_curd_tableview;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import C109118115_models.DBConnection;
import  C109118115_models.Product;
import C109118115_models.ProductDAO;

public class CurdProductTableViewController implements Initializable {

    private List<Product>products = new ArrayList();

  
    
    //方便操作資料庫的物件
    private ProductDAO stdao = new ProductDAO();

    @FXML
    private TableView<Product> table_Product;
    @FXML
    private TableColumn<Product, String> col_id;
    @FXML
    private TableColumn<Product, String> col_type;
    @FXML
    private TableColumn<Product, String> col_name;
    @FXML
     private TableColumn<Product, String> col_price;
    @FXML
    private TableColumn<Product, String> col_photo;
    @FXML
    private Pagination pagination;

    //表格的每一頁顯示幾個rows
    private final int RowsPerPage = 2;

    @FXML
    private TextField queryID;
    @FXML
    private TextField queryName;
    @FXML
    private TextArea dispalyCourse;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable(); //表格初始化
    }

    //表格初始化很複雜請仔細分解動作
    private void initTable() {

        //表格最後一欄是空白，不要顯示!
        table_Product.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //table_Product.setPrefHeight(200);
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
         col_photo.setCellValueFactory(new PropertyValueFactory<>("photo"));
         col_type.setCellValueFactory(new PropertyValueFactory<>("type"));

        //按下頁次會驅動的事件，寫法格式有點難理解，說明如後:
        //ObservableValue<? extends Number> 是介面，
        // ? extends Number 表示某種型態繼承Number類別  ?表示此型態沒被用到所以用?代替
        // changed 有三個參數: ObservableValue、舊的頁次、新的頁次
        // ObservableValue是頁次物件的一些屬性 印出如下的結果:
        //IntegerProperty [bean: Pagination[id=pagination, styleClass=pagination], name: currentPageIndex, value: 1]
        pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                showTablePage(newValue.intValue(), RowsPerPage);
                //System.out.println(observable);
            }
        });

        // 表格切換到一下筆，對應的驅動方法，此處暫時沒用到，寫法與前面類似
        table_Product.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println(newValue);
            }
        });

        //讓表格內容可以修改
        table_Product.setEditable(true);
        //表格欄位設定成可以編輯必須分別塞入一個TextFieldTableCell類別元件
        col_id.setCellFactory(TextFieldTableCell.forTableColumn());
        col_name.setCellFactory(TextFieldTableCell.forTableColumn());
        col_type.setCellFactory(TextFieldTableCell.forTableColumn());
        col_price.setCellFactory(TextFieldTableCell.forTableColumn());
       col_photo.setCellFactory(TextFieldTableCell.forTableColumn());



        //學生學號欄位若有修改驅動這個方法
        col_id.setOnEditCommit(new EventHandler<CellEditEvent<Product, String>>() {
            @Override
            public void handle(CellEditEvent<Product, String> event) {
                //拿到表格中所在的該筆紀錄(是一筆學生物件)
                Product prod = table_Product.getSelectionModel().getSelectedItem();
                prod.setProduct_id(event.getNewValue()); //將該筆學生物件修改成新的值
            }
        });

        //學生姓名欄位若有修改驅動這個方法
        col_name.setOnEditCommit(new EventHandler<CellEditEvent<Product, String>>() {
            @Override
            public void handle(CellEditEvent<Product, String> event) {
                //取得該筆紀錄的方式有以下3種寫法:
                //Product prod = event.getTableView().getItems().get(event.getTablePosition().getRow());
                //Product prod = (Product) event.getTableView().getItems().get(event.getTablePosition().getRow());
                Product prod = table_Product.getSelectionModel().getSelectedItem();
                System.out.println(event.getNewValue());
                prod.setName(event.getNewValue());
                //也可這樣更新新值寫法2:
                //((Product) event.getTableView().getItems().get(event.getTablePosition().getRow())).setName(t.getNewValue());
            }
        });
//   col_price.setOnEditCommit(new EventHandler<CellEditEvent<Product, String>>() {
//     !!       @Override
//            public void handle(CellEditEvent<Product, String> event) {
//                //取得該筆紀錄的方式有以下3種寫法:
//                //Product prod = event.getTableView().getItems().get(event.getTablePosition().getRow());
//                //Product prod = (Product) event.getTableView().getItems().get(event.getTablePosition().getRow());
//                Product prod = table_Product.getSelectionModel().getSelectedItem();
//                System.out.println(event.getNewValue());
//                
//                prod.setPrice(event.getNewValue());
//                //也可這樣更新新值寫法2:
//                //((Product) event.getTableView().getItems().get(event.getTablePosition().getRow())).setName(t.getNewValue());
//            }
//        });
        //學生電話欄位若有修改驅動這個方法
        col_photo.setOnEditCommit(new EventHandler<CellEditEvent<Product, String>>() {
            @Override
            public void handle(CellEditEvent<Product, String> event) {
                Product prod = table_Product.getSelectionModel().getSelectedItem();
                prod.setPhoto(event.getNewValue());
            }
        });
 col_type.setOnEditCommit(new EventHandler<CellEditEvent<Product, String>>() {
            @Override
            public void handle(CellEditEvent<Product, String> event) {
                Product prod = table_Product.getSelectionModel().getSelectedItem();
                prod.setCategory(event.getNewValue());
            }
        });
    }

    //表格內容載入
    private void loadTable() {
        int totalPage = (int) (Math.ceil(products.size() * 1.0 / RowsPerPage));
        pagination.setPageCount(totalPage);
        //pagination.setCurrentPageIndex(0);
        int currentpg = pagination.getCurrentPageIndex();
        showTablePage(currentpg, RowsPerPage);
    }

    //顯示某一個頁面的表格內容
    private void showTablePage(int pg, int row_per_pg) {
        table_Product.getItems().clear(); //先清除表格內容
        int from = pg * row_per_pg;  //計算在此頁面顯示第幾筆到第幾筆
        int to = Math.min(from + row_per_pg, products.size());
        //Products一筆一筆加到表格中
        for (int i = from; i < to; i++) {
            table_Product.getItems().add(products.get(i));
        }
    }

    //學生電話欄位若有修改驅動這個方法，可以在SceneBuider中定義這個事件
//  !!  private void onPhoneEditCommit(CellEditEvent<Product, String> event) {
//        Product prod = table_Product.getSelectionModel().getSelectedItem();
//        prod.setPhone(event.getNewValue());
//    }

    //更新一筆紀錄
//    @FXML
//    private void update(ActionEvent event) {
//        Product prod = table_Product.getSelectionModel().getSelectedItem();
//        String id = prod.getProduct_id();
//        String name = prod.getName();
//            int price = prod.getPrice();
//        String photo = prod.getPhoto();
//          String type = prod.getCategory();
//        stdao.update(new Product(id, name, photo));
//        products = stdao.getAllProducts();
//        loadTable();
//    }

    // 刪除一筆紀錄
    @FXML
    private void delete(ActionEvent event) {

        Product prod = table_Product.getSelectionModel().getSelectedItem();
       String id = prod.getProduct_id();

        boolean sucess = stdao.delete(id);
        products = stdao.getAllProducts();
        loadTable();
    }

//    //新增一筆紀錄
//    @FXML
//    private void insert(ActionEvent event) {
//        Product prod = table_Product.getSelectionModel().getSelectedItem();
//     String id = prod.getProduct_id();
//        String name = prod.getName();
//        String phone = prod.getPhone();
//        stdao.add(new Product(id, name, phone));
//        Products = stdao.getAllProducts();
//        loadTable();
//    }

    //新增空白的一筆紀錄
//    @FXML
//    private void blankRecord(ActionEvent event) {
//        table_Product.getItems().add(new Product("p-j-101", "李大同?", "12345?"));
//    }
//
//    //搜尋特定學生學號
//    @FXML
//    private void findID(ActionEvent event) {
//
//        Products.clear();
//        Products.add(stdao.selectByID(queryID.getText()));
//
//        loadTable();
//    }

    //搜尋特定姓名
//    @FXML
//    private void findName(ActionEvent event) {
//        //stdao.selectByName(queryName.getText());
//        Products = stdao.selectByName(queryName.getText());
//        loadTable();
//    }

    //搜尋全部
    @FXML
    private void findAll(ActionEvent event) {
        stdao.getAllProducts();

        products = stdao.getAllProducts();

        //若Products是ObservableList<Product>要這樣寫才可行:
        //Products.addAll( stdao.getAllProducts()); 
        loadTable();
    }

    @FXML
    private void selectAllCourses(ActionEvent event) {

        Connection conn = DBConnection.getConnection();

        String query = "select * from course";
        String msg="";

        try {
            PreparedStatement state
                    = conn.prepareStatement(query);
            ResultSet rset = state.executeQuery();

            while (rset.next()) {

                msg+=rset.getString("course_id")+"\t";
                msg+=rset.getString("course_name")+"\t";
                msg+=rset.getString("credit")+"\t";
                msg+=rset.getString("teacher_id")+"\t";
                msg+="\n";
                
                System.out.println(rset.getString("course_name"));
                
                dispalyCourse.setText(msg);

            }
        } catch (SQLException ex) {
            System.out.println("getAllcourses異常:" + ex.toString());
        }

    }

}
