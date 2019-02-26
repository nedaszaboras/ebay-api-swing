package sample;

import com.google.gson.JsonArray;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class Main {

    private JFrame frame;
    private JPanel mainPanel;
    private JButton startButton;
    private JTable results;
    private JScrollPane scroll;
    private JLabel resultCount;
    private JLabel totalResultsCount;
    private JTextArea inputJTextArea;
    private JTree tree;
    private JTabbedPane firstPane;
    private JScrollPane treeScroll;
    private JButton inputsButton;
    private EbayItem ebayItem;

    String url;
    String name;
    String condition;
    float price;
    float shipping;

    public String key;
    public String[] inputArray;
    int sum = 0;
    float averagePrices = 0;


    ArrayList<String> queryNames = new ArrayList<String>();
    ArrayList<EbayItem> ebayItemList = new ArrayList<EbayItem>();



    public Main() {

        tree.setPreferredSize(null);
        try {
            key = new String(Files.readAllBytes(Paths.get("ebay_key.txt")));
        }
        catch (Exception e) {
            System.out.println("Key file exception : " +e);
        }

        String col[] = {"Results"};
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Results");

        DefaultTableModel tableModel = new DefaultTableModel(col,0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        DefaultTreeModel treeModel = new DefaultTreeModel(root);

        tree.setModel(treeModel);
        results.setModel(tableModel);
        results.setRowHeight(35);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);
                resultCount.setText("");
                totalResultsCount.setText("");

                //input = inputTextField.getText();
                inputArray = inputJTextArea.getText().split("\\n");
                for (int j = 0; j < inputArray.length; j++) {

                    ebayItem = new EbayItem(inputArray[j], key);
                    ebayItemList.add(ebayItem);

                    sum += ebayItem.parsedData.get(j).totalresults;
                    totalResultsCount.setText("Viso rastų rezultatų kiekis " + sum + " ");

                    DefaultMutableTreeNode inputNames = new DefaultMutableTreeNode(inputArray[j]);
                    root.add(inputNames);

                    System.out.println("parsed data in main : size " + ebayItem.parsedData.size());
                    System.out.println("total result count @ " + inputArray[j] + " : " + ebayItem.parsedData.get(j).totalresults);

                        name = inputArray[j] + " | Price average : " + averagePrices + " | Item count : " + ebayItem.parsedData.size();
                        Object[] data = {name};
                        tableModel.addRow(data);

                    for (int i = 0; i < ebayItem.parsedData.size(); i++) {

                        /*url = ebayItem.parsedData.get(i).url;
                        name = ebayItem.parsedData.get(i).name;
                        condition = ebayItem.parsedData.get(i).condition;
                        price = ebayItem.parsedData.get(i).price;
                        shipping = ebayItem.parsedData.get(i).shippingPrice;

                        Object[] data = {name, condition, price, shipping, url};
                        tableModel.addRow(data);*/




                        DefaultMutableTreeNode resultNames = new DefaultMutableTreeNode(ebayItem.parsedData.get(i).name + " | Price : " +ebayItem.parsedData.get(i).price);
                        inputNames.add(resultNames);

                        DefaultMutableTreeNode resultPrice = new DefaultMutableTreeNode("Price : " +ebayItem.parsedData.get(i).price);
                        DefaultMutableTreeNode resultCondition = new DefaultMutableTreeNode("Condition : " +ebayItem.parsedData.get(i).condition);
                        DefaultMutableTreeNode resultShippingPrice = new DefaultMutableTreeNode("Shipping Price : " +ebayItem.parsedData.get(i).shippingPrice);

                        resultNames.add(resultPrice);
                        resultNames.add(resultCondition);
                        resultNames.add(resultShippingPrice);

                    }
                }

                resultCount.setText(" Rodomų rezultatų kiekis " + tableModel.getRowCount());

            }
        });

        results.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    System.out.println("clicked row : " + row);
                    //Table newTable = new Table();
                    //newTable.setVisible(true);
                    System.out.println("pirmas name @ main " + ebayItemList);
                    ResultsWindow table = new ResultsWindow(ebayItemList,row);
                    table.setVisible(true);
                }
            }
        });

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ebay API");
        frame.setContentPane(new Main().firstPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
