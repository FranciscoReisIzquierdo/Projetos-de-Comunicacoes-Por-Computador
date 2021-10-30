package com.company;


public class Main {

    public static void main(String[] args) throws Exception {
        //We need a folder plus at least a host
        if(args.length< 2){
            System.out.println("Invalid arguments.\n");
            return;
        }

        System.out.println("Program starting...\n");

        ControlManager c = new ControlManager(args);
        //System.out.println(c.toString());
        c.name();
    }
}
