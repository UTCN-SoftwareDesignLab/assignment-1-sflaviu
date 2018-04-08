package controller;

// Credits for using a HashMap for selecting controllers: Marius Supuran

public interface Controller {

    void openNextController(String next);
    void setActiveUser(Long userId);
    void hideGUI();
    void showGUI();
}
