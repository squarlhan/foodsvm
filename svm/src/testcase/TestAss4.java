/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testcase;

import adaboost.DataSet;
import adaboost.Evaluation;

/**
 *
 * @author daq
 */
public class TestAss4 {

    public static void main(String[] args) {
        // for RandomForest
        System.out.println("for RandomForest");
//        String pre = "C:/Users/install/Desktop/hxs/TCM/hnc/RandomForest_AdaBoost-master/";
//        String[] dataPaths = new String[]{pre+"breast-cancer.data", pre+"segment.data"};
        String[] dataPaths = new String[]{"C:/Users/install/Desktop/hxs/TCM/hnc/allast.csv"};
//        for (String path : dataPaths) {
//            DataSet dataset = new DataSet(path);
//
//            // conduct 10-cv 
//            Evaluation eva = new Evaluation(dataset, "RandomForest");
//            eva.crossValidation();
//
//            // print mean and standard deviation of accuracy
//            System.out.println("Dataset:" + path + ", mean and standard deviation of accuracy:" + eva.getAccMean() + "," + eva.getAccStd());
//        }

        // for AdaBoost
        System.out.println("\nfor AdaBoost");
        for (String path : dataPaths) {
            DataSet dataset = new DataSet(path);

            // conduct 10-cv 
            Evaluation eva = new Evaluation(dataset, "AdaBoost");
            eva.crossValidation();

            // print mean and standard deviation of accuracy
            System.out.println("Dataset:" + path + ", mean and standard deviation of accuracy:" + eva.getAccMean() + "," + eva.getAccStd());
        }
    }
}
