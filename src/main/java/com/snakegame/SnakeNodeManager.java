package com.snakegame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.LinkedList;


public class NodeManager {

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

    private Pane root;

    //this variable is used to "queue" the creation of a new SnakeNode which is done after updating
    //the position of every existing node
    private boolean toSpawn = false;

    private SnakeHeadController.direction currHeadDirection;

    public void setRoot(Pane root) {
        this.root = root;
    }

    public void setCurrHeadDirection(SnakeHeadController.direction dir){
        currHeadDirection = dir;
    }

    public void spawnSnakeNode(){
        //this method simply means that at the end of the main-loop when updateSnake() is called, it will
        //create a new node
        toSpawn = true;
    }
    public void addFirstPosition(double[] arr){
        positions.addFirst(arr);
    }
    public boolean updateSnake() throws IOException {
        for (int i = 0; i < nodes.size(); i++){
            setNodePosition(nodes.get(i), positions.get(i+1));
            setNodeImageView(i);
        }
        if (toSpawn) {
            loadNode(positions.getLast());
            toSpawn = false;
        }
        else if (positions.size() > 1) {
            positions.removeLast();
        }

        return !isCollidingHeadAndNode();
    }

    private void setNodeImageView(int index){
        SnakeNodeController currNodeController = nodeControllers.get(index);
        if (index == 0) {
            currNodeController.setVertical(currHeadDirection == SnakeHeadController.direction.UP || currHeadDirection == SnakeHeadController.direction.DOWN);
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
    public boolean isCollidingHeadAndNode() {
        for (Parent node : nodes) {
            if (positions.getFirst()[0] == node.getLayoutX() && positions.getFirst()[1] == node.getLayoutY()){
                return true;
            }
        }
        return false;
    }

    public LinkedList<double[]> getPositions(){
        return positions;
    }
}