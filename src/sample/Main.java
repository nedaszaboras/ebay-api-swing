package sample;

import com.google.gson.JsonArray;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    private JFrame frame;
    private JPanel mainPanel;
    private JButton startButton;
    private JTextField inputTextField;
    private JTable results;
    private JScrollPane scroll;
    private JLabel resultCount;
    private EbayAPI ebayAPI;
    String input;
    String url;
    String name;
    String condition;
    String price;
    String shipping;



   /* @Override
    public void start(Stage primaryStage) throws Exception{

        //primaryStage.show();

    }*/


    public Main() {

        ebayAPI = new EbayAPI();
        ebayAPI.main = this;


        String col[] = {"Name", "Condition", "Price", "Shipping", "URL"};
        DefaultTableModel tableModel = new DefaultTableModel(col,0);
        //tableModel.addColumn(col);
        //tableModel.addRow(col);



        results.setModel(tableModel);
        results.setRowHeight(35);



        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input = inputTextField.getText();
                ebayAPI.Create();
                System.out.println("response from MAIN" +ebayAPI.getNames(ebayAPI.response));

                //resultList.setVisibleRowCount(ebayAPI.resultCount);

                for (int i = 0; i <= ebayAPI.resultCount; i++){
                    url = ebayAPI.getUrl(ebayAPI.response).get(i).toString();
                    name = ebayAPI.getNames(ebayAPI.response).get(i).toString();
                    price = ebayAPI.getPrices(ebayAPI.response).get(i).toString();
                    condition = ebayAPI.getCondition(ebayAPI.response).get(i).toString();
                    shipping = ebayAPI.getShippingPrices(ebayAPI.response).get(i).toString();

                    Object[] data = {name, condition, price, shipping, url};
                    tableModel.addRow(data);
                    int t = i +1;
                    resultCount.setText("rezultatų kiekis " + t);
                }



            }
        });



    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ebay API");
        frame.setContentPane(new Main().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);





    }
}
