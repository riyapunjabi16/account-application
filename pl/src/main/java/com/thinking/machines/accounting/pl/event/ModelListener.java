package com.thinking.machines.accounting.pl.model.event;
public interface ModelListener
{
public void customerAdded(ModelEvent modelEvent);
public void customerUpdated(ModelEvent modelEvent);
public void customerDeleted(ModelEvent modelEvent);
public void pdfCreated(ModelEvent modelEvent);
public void selectedIndexChanged(ModelEvent modelEvent);
}
