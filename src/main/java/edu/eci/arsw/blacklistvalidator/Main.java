/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import java.util.List;

/**
 *
 * @author hcadavid
 */
public class Main {
    
    public static void main(String a[]){
        HostBlackListsValidator hblv=new HostBlackListsValidator();
        int N=80000;
//        System.out.println("Esperado:  The host was found in the following blacklists:[29, 10034, 20200, 31000, 70500]" + "para ip 202.24.34.55");
        List<Integer> blackListOcurrences=hblv.checkHost("202.24.34.55",100);
        System.out.println("The host was found in the following blacklists:"+blackListOcurrences);
//        blackListOcurrences=hblv.checkHost("202.24.24.55",N);
//        System.out.println("The host was found in the following blacklists:"+blackListOcurrences);
   
    }
}
