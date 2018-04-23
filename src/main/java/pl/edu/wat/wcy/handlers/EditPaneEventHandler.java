package pl.edu.wat.wcy.handlers;

import javafx.event.Event;

import java.util.EventListener;

public interface EditPaneEventHandler<T extends Event> extends EventListener {

    boolean handle(T event, String val);
}