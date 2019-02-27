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
import java.util.Collections;
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
    private EbayItem ebayItem;

    public String key;
    public String[] inputArray;
    int totalresults;
    int totalresultsSum;
    int showingresultsSum;
    float maxPrice;
    float minPrice;

    ArrayList<String> queryNames = new ArrayList<String>();
    ArrayList<EbayItem> ebayItemList = new ArrayList<EbayItem>();
    ArrayList<Float> prices = new ArrayList<Float>();



    public Main() {

        tree.setPreferredSize(null);
        try {
            key = new String(Files.readAllBytes(Paths.get("ebay_key.txt")));
        }
        catch (Exception e) {
            System.out.println("Key file exception : " +e);
        }

        String col[] = {"Name", "Min Price", "Max Price", "Total results found"};
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
        results.setRowHeight(25);
        results.getColumnModel().getColumn(0).setPreferredWidth(300);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);
                resultCount.setText("");
                totalResultsCount.setText("");
                ebayItemList.clear();
                root.removeAllChildren();
                tree.revalidate();
                tree.repaint();
                tree.updateUI();

                inputArray = inputJTextArea.getText().split("\\n");
                for (int j = 0; j < inputArray.length; j++) {

                    ebayItem = new EbayItem(inputArray[j], key);
                    ebayItemList.add(ebayItem);

                    DefaultMutableTreeNode inputNames = new DefaultMutableTreeNode(inputArray[j]);
                    root.add(inputNames);

                    if (ebayItem.parsedData.size() != 0) totalresults = ebayItem.parsedData.get(j).totalresults;
                    else totalresults = 0;

                    System.out.println("parsed data in main : size " + ebayItem.parsedData.size());
                    System.out.println("total result count @ " + inputArray[j] + " : " + ebayItem.parsedData.get(j).totalresults);

                    totalresultsSum += totalresults;
                    showingresultsSum += ebayItem.parsedData.size();

                    totalResultsCount.setText("Viso rastų rezultatų kiekis " + totalresultsSum + " ");
                    resultCount.setText(" Rodomų rezultatų kiekis " + showingresultsSum);

                    for (int i = 0; i < ebayItem.parsedData.size(); i++) {

                        prices.add(ebayItem.parsedData.get(i).price + ebayItem.parsedData.get(i).shippingPrice);

                        DefaultMutableTreeNode resultNames = new DefaultMutableTreeNode("Price : " +ebayItem.parsedData.get(i).price + " | " +ebayItem.parsedData.get(i).name);
                        inputNames.add(resultNames);

                        DefaultMutableTreeNode resultPrice = new DefaultMutableTreeNode("Price : " +ebayItem.parsedData.get(i).price);
                        DefaultMutableTreeNode resultCondition = new DefaultMutableTreeNode("Condition : " +ebayItem.parsedData.get(i).condition);
                        DefaultMutableTreeNode resultShippingPrice = new DefaultMutableTreeNode("Shipping Price : " +ebayItem.parsedData.get(i).shippingPrice);

                        resultNames.add(resultPrice);
                        resultNames.add(resultCondition);
                        resultNames.add(resultShippingPrice);

                        maxPrice = Collections.max(prices);
                        minPrice = Collections.min(prices);


                    }

                    Object[] data = {inputArray[j], minPrice, maxPrice, totalresults};
                    tableModel.addRow(data);

                    prices.clear();

                }
            }

        });

        results.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
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
