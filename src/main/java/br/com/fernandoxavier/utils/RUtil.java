package br.uniriotec.ppgi.fernandoxavier.utils;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

public class RUtil {
    public Rengine rengine;

    public REXP eval(String s) {
        rengine = new Rengine(new String[] { "--vanilla" }, false, null);
        rengine.eval(".libPaths('C:/Users/Dell/Documents/R/win-library/3.1')");
        rengine.eval("library(SPEI)");
        return rengine.eval(s);
    }

/*    public static void main (String[] args) {
        RUtil rUtil = new RUtil();

        //rengine =  new Rengine(new String[] { "--vanilla" }, false, null);
//        Rengine rengine =  new Rengine(new String[] { "--vanilla" }, false, null);
       // rengine.eval(".libPaths('C:/Users/Dell/Documents/R/win-library/3.1')");
       // rengine.eval("library(SPEI)");
        REXP result = rUtil.eval("penman(c(20,21,22,20,21,22,20,21,22,20,21,22)," +
                "c(27,28,29,21,23,24,28,29,20,23,23,23),c(1,1,1,1,1,1,1,1,1,1,1,1)," +
                "NA,-7.6,NA,c(3,3,3,3,4,4,4,4,5,5,5,5),7.83,NA,NA,87.09,NA,NA,170)");
        System.out.println(result.toString());
        System.exit(-1);
    }*/
}
