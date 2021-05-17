/**
 * Most selects can just extend this and change what they want.
 *
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Oct 6, 2002
 * Time: 10:59:58 PM
 * 
 */
package com.spaceprogram.accounting.html.forms;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/*@nullable_by_default@*/
public abstract class DefaultSelect implements Select {
    public String selectName = "selector";
    public List optionables; // get things like this to extend Optionable with getName and getId
    public int selected = 0;

    public List startOptions = new ArrayList();

    public void write(PrintWriter out){
        out.print("<select name=\"" + selectName + "\">\n");
        for (int j = 0; j < startOptions.size(); j++) {
            Option option = (Option) startOptions.get(j);


            out.print("<option value=\"" + option.getId() + "\"");
            if(option.isSelected()){
                out.print(" selected");
                }
            out.print(">" + option.getName());

        }
        for (int i = 0; i < optionables.size(); i++) {
            Optionable account = (Optionable) optionables.get(i);
            int aid = account.getId();
            out.print("<option value=\"" + aid + "\"");
            if(selected == aid) out.print(" selected");
            out.print(">" + account.getName());
        }
        out.print("</select>");


    }

    //@ also
    //@ post !\result.equals("");
    public String getSelectName() {
        return selectName;
    }

    public void setSelectName(String selectName) {
        this.selectName = selectName;
    }

    //@ also
    //@ post \result == this.selected;
    public int getSelected() {
        return selected;
    }

    //@ also
    //@ pre selected >=0;
    public void setSelected(int selected) {
        this.selected = selected;
    }



    //@ pre o != null;
    public void addOptionToStart(Option o){
        startOptions.add(o);
    }

    public void clearStartOptions(){
        startOptions = new ArrayList();
    }


}
