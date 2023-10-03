package org.genevaers.utilities.menu;

/*
 * Copyright Contributors to the GenevaERS Project. SPDX-License-Identifier: Apache-2.0 (c) Copyright IBM Corporation 2008
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import static org.fusesource.jansi.Ansi.ansi;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;

public abstract class Menu {
    public static final String WELCOME_MESSAGE = "Hello, welcome to the server";
    public static final String BLACK = "\\[001b[0;30m\\]";
    public static final String BLACKB = "\\[001b[1;30m\\]";
    public static final String RED = "\u001b[0;31m";
    public static final String REDB = "\\001b[31m";
    public static final String GREEN = "\u001b[32m";
    public static final String GREENB = "\u001b[1;32m\\]";
    public static final String YELLOW = "\u001b[33m";
    public static final String YELLOWB = "\u001b[1;33m\\]";
    public static final String BLUE = "\u001b[0;34m";
    public static final String BLUEB = "\u001b[1;34m\\]";
    public static final String PURPLE = "\u001b[0;35m";
    public static final String PURPLEB = "\u001b[1;35m\\]";
    public static final String CYAN = "\u001b[0;36m";
    public static final String CYANB = "\u001b[1;36m\\]";
    public static final String WHITE = "\u001b[0;37m\\]";
    public static final String WHITEB = "\u001b[1;37m\\]";
    public static final String RESET = "\u001b[0m";
    public static final String CLEARSCREAN = "\u001b[27m";

    protected List<MenuItem> menuItems = new ArrayList<>();

    private String nextMenu;
    private static String header;

    public void showMenu() {
        boolean keepGoing = true;
        while (keepGoing) {
            buildMenu();
            Scanner sc = new Scanner(System.in);
            try {
                keepGoing = handleChoice(Integer.parseInt(sc.nextLine()));
            } catch (NumberFormatException e) {
                promptedWrite(RED+"Only numbers accepted. Enter to continue."+RESET);
            }
            Menu.clearScreen();
        }
    }

    public abstract void showSettings(StringBuilder menuStr);

    public boolean handleChoice(int opt) {
        boolean keepGoing = true;
        if(opt > menuItems.size()) {
            promptedWrite(RED+"Option out of range."+RESET);
        } else {
            MenuItem mi = menuItems.get(opt-1);
            keepGoing = mi.doIt();
            nextMenu = mi.getNextMenu() != null ? mi.getNextMenu().toString() : null;
        }
        return keepGoing;
    }

    private void buildMenu() {
        showHeader();
        StringBuilder menuStr = new StringBuilder();
        showSettings(menuStr);
        System.out.println(menuStr.toString());
        int o = 1;
        for(MenuItem mi : menuItems) {
            if(mi.getHeader() != null) {
                showSection(mi);
            }
            showItem(o++, mi);
        }
        System.out.println(ansi().a("\nChoose an option:"));
    }

    private void showItem(int o, MenuItem mi) {
        System.out.println(ansi().a(String.format(" %d) %-20s", o, mi.getPrompt())));
    }

    private void showSection(MenuItem mi) {
        System.out.println(ansi().fgBright(mi.getColor()).a(String.format("\n%-20s", mi.getHeader())).fg(Color.DEFAULT));
    }

    @SuppressWarnings("resource")
    public static String promptedRead(String p) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter " + p);
        return sc.nextLine();
    }

    @SuppressWarnings("resource")
    public static String promptedWrite(String p) {
        System.out.println(p);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static void clearScreen() {
        System.out.println(ansi().a("\033[H\033[2J"));
    }

    public static void showHeader() {
        System.out.println(ansi().fg(Ansi.Color.GREEN).a(header).reset());
    }

    public static void addMenuSummaryItem(StringBuilder menu, String description, String item, String comment) {
        menu.append(YELLOW + String.format("%-20s: ", description));
        if(item != null) {
            menu.append(CYAN +String.format("%-20s ",item));
        } else {
            menu.append(RED + String.format("%-20s","not set "));            
        }
        if(comment != null && comment.length() > 0) {
            menu.append(YELLOW + "(" + comment + ")\n");
        } else {
            menu.append("\n");
        }
    }

    public String getNextMenu() {
        return nextMenu;
    }

    public static void setHeader(String header) {
        Menu.header = header;
    }

    public static void writeError(String err) {
        promptedWrite(RED+err+"\nEnter to continue"+RESET);
    }

}
