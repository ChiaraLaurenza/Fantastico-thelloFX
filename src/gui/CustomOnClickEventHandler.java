package gui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;

//Event is a class and a handler is a method.

 public class CustomOnClickEventHandler implements EventHandler<Event> {
        private GuiController guiController;

        CustomOnClickEventHandler(GuiController guiController) {
            this.guiController = guiController;
        }

        @Override
        public void handle(Event evt) {
            ImageView imageView = (ImageView) evt.getSource();
            guiController.onSlotClicked(imageView);
        }
}
