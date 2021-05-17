/**
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Aug 1, 2002
 * Time: 11:03:07 AM
 * 
 */
package com.spaceprogram.accounting.common;

import com.crossdb.sql.*;

import java.util.Date;
import java.sql.Connection;
import java.sql.SQLException;

public class Queue
{
	/*@spec_public@*/int id;

    /**
     * Leads or queues
     */
    /*@spec_public@*/int appKey;
    /**
     * Company key
     */
    /*@spec_public@*/int itemKey;
    /*@spec_public@*/String name;
    /*@spec_public@*/Date created;
    /*@spec_public@*/int createdBy;
    /*@spec_public@*/Date updated;
    /*@spec_public@*/int updatedBy;

    public Queue(int id, int appKey, int itemKey, String name, Date created, int createdBy, Date updated, int updatedBy)
    {
        this.id = id;
        this.appKey = appKey;
        this.itemKey = itemKey;
        this.name = name;
        this.created = created;
        this.createdBy = createdBy;
        this.updated = updated;
        this.updatedBy = updatedBy;
    }

    //@ requires true;
    //@ ensures \result == id;
    public int getId()
    {
        return id;
    }

    //@ requires id >= 0;
    //@ ensures this.id == id;
    public void setId(int id)
    {
        this.id = id;
    }

    public int getAppKey()
    {
        return appKey;
    }

    public void setAppKey(int appKey)
    {
        this.appKey = appKey;
    }

    //@ post \result == itemKey;
    public int getItemKey()
    {
        return itemKey;
    }

    public void setItemKey(int itemKey)
    {
        this.itemKey = itemKey;
    }

    //@ post !name.equals("");
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }

    public int getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(int createdBy)
    {
        this.createdBy = createdBy;
    }

    public Date getUpdated()
    {
        return updated;
    }

    public void setUpdated(Date updated)
    {
        this.updated = updated;
    }

    public int getUpdatedBy()
    {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy)
    {
        this.updatedBy = updatedBy;
    }

    /**
     * persist queue into db
     */
    
    //@ signals_only SQLException;
    public void save(SQLFactory factory, Connection conn) throws SQLException {
        if(id == 0){
            id = Queue.insertQueue(factory, conn, appKey, itemKey, name, created,  createdBy, updated,  updatedBy);
        }
        else{
            Queue.updateQueue(factory, conn, id, appKey, itemKey, name, created,  createdBy, updated,  updatedBy);
        }

    }

    /**
     * Insert queue into db
     */
    public static int insertQueue(SQLFactory factory, Connection conn, int appKey, int itemKey, String name, Date created, int createdBy, Date updated, int updatedBy) throws SQLException {

        InsertQuery q = factory.getInsertQuery();
        q.returnID(true);
        q.setTable("CRMQueues");
        q.addAutoIncrementColumn("queue_id");
        q.addColumn("queue_app_key", appKey);
        q.addColumn("queue_item_key", itemKey);
        q.addColumn("queue_created_by", createdBy);
        q.addColumn("queue_created", created);
        q.addColumn("queue_update_by", updatedBy);
        q.addColumn("queue_updated", updated);
        q.addColumn("queue_name", name);
        return q.execute(conn);

    }
     /**
     * update queue into db
     */
    public static int updateQueue(SQLFactory factory, Connection conn, int id, int appKey, int itemKey, String name, Date created, int createdBy, Date updated, int updatedBy) throws SQLException {

         UpdateQuery q = factory.getUpdateQuery();
         q.setTable("CRMQueues");
        //q.addAutoIncrementColumn("queue_id");
        q.addColumn("queue_app_key", appKey);
        q.addColumn("queue_item_key", itemKey);
        q.addColumn("queue_created_by", createdBy);
        q.addColumn("queue_created", created);
        q.addColumn("queue_update_by", updatedBy);
        q.addColumn("queue_updated", updated);
        q.addColumn("queue_name", name);

         q.addWhereCondition("queue_id", WhereCondition.EQUAL_TO, id);
        return q.execute(conn);
    }
}
