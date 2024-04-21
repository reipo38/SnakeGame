package com.snakegame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

/*
    This class manages the creation of every new node, updates their positions and images
    the corresponding logic for the SnakeHead is handled in the SnakeHeadController class
 */

public class SnakeManager {

    /*ho
    These arraylists are used to keep
     the positions of every single Parent object on the scene including the SnakeHead
     the instances of every single Parent object deriving from SnakeNode
     the controllers for every single SnakeNode
     */
    private final LinkedList<double[]> positions = new LinkedList<>();
    private final LinkedList<Parent> nodes = new LinkedList<>();

    /*
    the controllers are used for updating the direction of the nodes
    index i of nodeControllers corresponds to index i of nodes
     */
    private final LinkedList<SnakeNodeController> nodeControllers = new LinkedList<>();

    private final Pane root;

    //the snake head controller is used only to check for which direction the SnakeHead is looking in
    //order to update the direction of the SnakeNode
    private final SnakeHeadController headController;

    //this variable is used to "queue" the creation of a new SnakeNode which is done after updating
    //the position of every existing node
    private boolean toSpawn = false;

    public SnakeManager(Pane root, SnakeHeadController headController) {
        this.root = root;
        this.headController = headController;

    }
    public void spawnSnakeNode(){
        //this method simply means that at the end of the main-loop when updateSnake() is called, it will
        //create a new node
        toSpawn = true;
    }
    public void updateSnake(Parent head) throws IOException {
        positions.addFirst(new double[]{head.getLayoutX(), head.getLayoutY()});
        for (int i = 0; i < nodes.size(); i++){
            setNodePosition(nodes.get(i), positions.get(i+1));
            setNodeImageView(i);
        }
        if (toSpawn) {
            loadNode(positions.getLast());
            toSpawn = false;
        }
        /*last position is removed only if size greater than 1
        if size is greater than 1 this means one of two is true:
            1.
            there are no nodes.
            it keeps the current(0) and previous(1) position of the Head.
            The current will become previous in the next iteration of the main-loop
            and might be used to create a new node, so we keep it
            the previous now will not be used, so we discard it

            2.
            it keeps the positions of every node, including the previous pos of the last node,
            we could have used it in this iteration to create a new node, but if not we discard it.
         */
        else if (positions.size() > 1) {
            positions.removeLast();
        }
    }

    private void setNodeImageView(int index){
        SnakeNodeController currNodeController = nodeControllers.get(index);
        if (index == 0) {
            currNodeController.setVertical(headController.getDir() == SnakeHeadController.direction.UP || headController.getDir() == SnakeHeadController.direction.DOWN);
        }
        else{
            currNodeController.setVertical(nodes.get(index - 1).getLayoutY() - nodes.get(index).getLayoutY() != 0);
        }
        currNodeController.setImageView();
    }

    private void setNodePosition(Parent node, double[] newPos){
        node.setLayoutX(newPos[0]);
        node.setLayoutY(newPos[1]);
    }

    private void loadNode(double[] newPos) throws IOException{
        FXMLLoader nodeLoader = new FXMLLoader(getClass().getResource("/com/snakegame/SnakeNode.fxml"));
        Parent node = nodeLoader.load();
        SnakeNodeController nodeController = nodeLoader.getController();

        nodes.add(node);
        nodeControllers.add(nodeController);
        root.getChildren().add(node);

        setNodePosition(node, newPos);
        setNodeImageView(nodes.size()-1);
    }
    public boolean isCollidingHeadAndNode(Parent head) {
        for (Parent node : nodes) {
            if (head.getLayoutX() == node.getLayoutX() && head.getLayoutY() == node.getLayoutY()){
                return true;
            }
        }
        return false;
    }
    public boolean containsPos(double[] pos){
        return positions.stream()
                .anyMatch(arr -> Arrays.equals(arr, pos));
    }
}